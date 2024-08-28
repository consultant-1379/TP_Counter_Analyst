package distocraft.dc500.etl.counter.analyst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CounterFileReader extends DefaultHandler {
	
	Logger logger = Logger.getLogger(CounterFileReader.class.getName());
	
	String counterFile;
	
	List<String> counters_from_pmfile;
	
	public List<String> getCounters_from_pmfile() {
		return counters_from_pmfile;
	}

	public void setCounters_from_pmfile(List<String> counters_from_pmfile) {
		this.counters_from_pmfile = counters_from_pmfile;
	}

	public CounterFileReader(String counterFile) {
		this.counterFile = counterFile;
	}
	
	public void readFile() {
		logger.info("reading PM file:: " + counterFile);
		InputStream input = null;
		List<String> valueList = new ArrayList<String>();
		try {
			input = new FileInputStream(counterFile);
			if (counterFile.endsWith(".xml")) {
				valueList = parseXmlFile(input);
			} else if (counterFile.endsWith(".stat")) {
				valueList = parseCsvFile(input);
			} else if (counterFile.contains("IVR")) {
				valueList = parseIvrStatFile(input);
			}
			setCounters_from_pmfile(valueList);
		} catch (IOException e) {
			logger.info("Exception caught while reading the PM File:: " + e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				logger.info("Exception caught while closing the PM File:: " + e);
			}
		}
	}

	private List<String> parseIvrStatFile(InputStream input) {
		logger.info("parsing IVR XML file .... ");
		ArrayList<String> list = new ArrayList<String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(input);
			doc.getDocumentElement().normalize();
			NodeList measDataNodeList = doc.getElementsByTagName("mt");
			for (int i = 0; i < measDataNodeList.getLength(); i++) {
				Element e = (Element) measDataNodeList.item(i);
				list.add(e.getTextContent());
			}
		} catch (SAXException | IOException | ParserConfigurationException e1) {
			logger.info("Exception caught while parsing IVR STAT XML file " + e1);
		}
		return list;
	}

	private List<String> parseCsvFile(InputStream input) {
		logger.info("parsing CSV file .... ");
		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(counterFile)));
			String header = reader.readLine();
            logger.info("text = " + header );
            String strArray[] = header.split(",");
            for(String str : strArray) {
            	if(!str.equals("DATE/TIME")) {
            		list.add(str);
            	}
            }
		} catch (IOException e) {
			logger.info("Exception caught while parsing CSV file " + e);
		}
		return list;
	}

	private List<String> parseXmlFile(InputStream input) {
		logger.info("parsing XML file .... ");
		ArrayList<String> list = new ArrayList<String>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(input);
			doc.getDocumentElement().normalize();
			NodeList measDataNodeList = doc.getElementsByTagName("measData");
			for (int i = 0; i < measDataNodeList.getLength(); i++) {
				Element e = (Element) measDataNodeList.item(i);
				Element measInfo = (Element) e.getElementsByTagName("measInfo").item(0);
				NodeList measTypeNodeList = measInfo.getElementsByTagName("measType");
				for (int j = 0; j < measTypeNodeList.getLength(); j++) {
					Node measTypeNode = measTypeNodeList.item(j);
					list.add(measTypeNode.getTextContent());
				}
			}
		} catch (SAXException | IOException | ParserConfigurationException e1) {
			logger.info("Exception caught while parsing XML file " + e1);
		}
		return list;
	}

}
