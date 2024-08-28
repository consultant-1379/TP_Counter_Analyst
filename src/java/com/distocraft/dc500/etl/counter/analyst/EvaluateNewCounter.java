package distocraft.dc500.etl.counter.analyst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class EvaluateNewCounter {
	
	Logger logger = Logger.getLogger(EvaluateNewCounter.class.getName());
	
	ModelTReader modelTReader;
	CounterFileReader counterFileReader;
	List<String> newCounter;

	public List<String> getNewCounter() {
		return newCounter;
	}

	public EvaluateNewCounter(ModelTReader modelTReader, CounterFileReader counterFileReader) {
		this.modelTReader = modelTReader;
		this.counterFileReader = counterFileReader;
	}

	public void analyzeNewCounter() {
		logger.info("Checking new counters");
		newCounter = new ArrayList<>();
		List<String> counter_from_modelt = modelTReader.getCounters_from_modelT();
		Collection<String> counters_from_pmfile = counterFileReader.getCounters_from_pmfile();
		logger.info("counter_from_modelt --> " + counter_from_modelt);
		logger.info("counters_from_pmfile --> " + counters_from_pmfile);
		Iterator<String> pmfileIter = counters_from_pmfile.iterator();
		while(pmfileIter.hasNext()) {
			String counter = pmfileIter.next();
			if(!counter_from_modelt.contains(counter)) {
				newCounter.add(counter);
			}
		}
		if(getNewCounter().isEmpty()) {
			logger.info("No new counters found");
			System.out.println("No new counters found");
		} else {
			logger.info("New Counter List --> " + getNewCounter());
			System.out.println("List of new counters ::: ");
			for(String counter: getNewCounter()) {
				System.out.println(counter);
			}
		}
	}

}

