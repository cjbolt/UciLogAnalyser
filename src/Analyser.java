import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Analyser {
	List<Integer> records;
		
	public Analyser() {
		records = new ArrayList<Integer>();
	}
	
	public void addRecord(Integer record) {
		records.add(record);
	}
	
	protected Integer calculateAverage(List<Integer> list) {
		  Long sum = (long) 0;
		  if(!list.isEmpty()) {
		    for (Integer rec : list) {
		        sum += rec;
		    }
		    return (int)(sum / list.size());
		  }
		  return 0;
		}
	
	public Analysis analyse() {
		if (records.size() != 0) {
			return new Analysis(Collections.max(records), Collections.min(records), calculateAverage(records));
		} else {
			return new Analysis(0, 0, 0);
		}
	}	
}
