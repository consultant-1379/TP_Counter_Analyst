package distocraft.dc500.etl.counter.analyst;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;  
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 

public class ModelTReader {
	
	Logger logger = Logger.getLogger(ModelTReader.class.getName());
	
	String modelT;
	String colName;
	
	List<String> counters_from_modelT;

	public List<String> getCounters_from_modelT() {
		return counters_from_modelT;
	}

	public void setCounters_from_modelT(String counters) {
		this.counters_from_modelT.add(counters);
	}

	public ModelTReader(String modelT, String colName) {
		this.modelT = modelT;
		this.colName = colName;
	}

	public void readFile(String sheets) {
		logger.info("Reading Model-T file :: " + modelT);
		this.counters_from_modelT = new ArrayList<>();
		File file = new File(modelT);
		XSSFWorkbook wb = null;//creating a new file instance  
		FileInputStream fis = null;   //obtaining bytes from the file  
		//creating Workbook instance that refers to .xlsx file  
		try {
			fis = new FileInputStream(file);
			wb = new XSSFWorkbook(fis);
				int colNum = 0;
				XSSFSheet sheet = wb.getSheet(sheets);
				int num_of_row = sheet.getPhysicalNumberOfRows();
				Row row = sheet.getRow(0);
				Iterator<Cell> cellItr = row.cellIterator();
				logger.info("iterating through first row to get the required column" + colName);
				while(cellItr.hasNext()) {
					Cell cell = cellItr.next();
					if(cell.toString().equals(this.colName)) {
						logger.info("got the column :: " + colName);
						break;
					}
					colNum++;
				}
				for(int r=1; r<num_of_row; r++) {
					Row row1 = sheet.getRow(r);
					if(row1!=null) {
//						Cell firstCol = row1.getCell(0);
//						if(firstCol!=null && firstCol.toString().equals(this.tableName)) {
							Cell cell = row1.getCell(colNum);
							if(cell!=null && !cell.toString().isEmpty()) {
								String value = cell.toString();
								setCounters_from_modelT(value);
							}
//						}
					}
				}
			logger.info("counter list from model-T --> " + getCounters_from_modelT());
		} catch (IOException e) {
			logger.info("Exception while reading the model-T file " + e);
		}  
		
	}

}
