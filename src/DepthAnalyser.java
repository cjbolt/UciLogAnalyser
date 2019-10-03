import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepthAnalyser extends Analyser {
	
	public DepthAnalyser() {
		super();
	}

	public Analysis analyse() {
		// Need to parse the records before we can calculate the average depth
		List<Integer> finalDepths = new ArrayList<Integer>();
		Integer lastDepth = 0;
		for (Integer currDepth : records) {
			if (currDepth < lastDepth) {
				finalDepths.add(lastDepth);
			}
			lastDepth = currDepth;
		}
		if (records.size() != 0) {
			return new Analysis(Collections.max(records), Collections.min(records), calculateAverage(finalDepths));
		} else {
			return new Analysis(0, 0, 0);
		}
	}
}
