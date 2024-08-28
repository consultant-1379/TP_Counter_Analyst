package distocraft.dc500.etl.counter.analyst;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

public class CounterAnalystMain {
	static CounterFileReader counterFileReader;
	static ModelTReader modelTReader;
	static EvaluateNewCounter newCounter;
	public static void main(String args[]) {
		
		try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
//		String modelT = "H:\\counterAnalysis\\XML\\DC_E_CCN_28_R7A.xlsx";
//		String counterFile = "H:\\counterAnalysis\\ivr-stat\\ivr-stat\\A20230808.0930-1000_VXMLIVR";
//		String colName = "3GPP32435_CBA";
		
		String modelT = args[0];
		String counterFile = args[1];
		String colName = args[2];
//		String tableName = "";
		readModelTFile(modelT, colName);
		readCounterFile(counterFile);
		evaluateNewCounters();
	}

	private static void evaluateNewCounters() {
		newCounter = new EvaluateNewCounter(modelTReader, counterFileReader);
		newCounter.analyzeNewCounter();
	}

	private static void readCounterFile(String counterFile) {
		counterFileReader = new CounterFileReader(counterFile);
		counterFileReader.readFile();
	}

	private static void readModelTFile(String modelT, String colName) {
		modelTReader = new ModelTReader(modelT, colName);
		String sheets = "Data Format";
		modelTReader.readFile(sheets);
		
	}
}
