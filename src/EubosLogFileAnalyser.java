import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class EubosLogFileAnalyser {
	
	static final String DEPTH = "depth";
	static final String NPS = "nps";
	
	private DepthAnalyser depth = new DepthAnalyser();
	private Analyser speed = new Analyser();
	
	public EubosLogFileAnalyser(File file) {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
		depth = new DepthAnalyser();
		speed = new Analyser();
		
		BufferedReader r = new BufferedReader(fr);
	    try {
	    	String line = null;
	    	while ((line = r.readLine()) != null) {
			    if (line.contains(DEPTH)) {
			    	depth.addRecord(getDepth(line));
			    } else if (line.contains(NPS)) {
			    	speed.addRecord(getSpeed(line));
			    } else {
			    	// pass
			    }
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Integer getDepth(String line) {
		return getValue(DEPTH, line);
	}
	
	private Integer getSpeed(String line) {
		return getValue(NPS, line);
	}
	
	private Integer getValue(String find, String line) {
		Integer value = 0;
		StringTokenizer tok = new StringTokenizer(line);
		while (tok.hasMoreTokens()) {
		    if (tok.nextToken().equals(find)) {
		    	String[] str_value = tok.nextToken().split("<");
		    	value = Integer.parseInt(str_value[0]);
		    }
		}
		return value;
	}
	
	public Analysis getSpeedMetrics() {
		return speed.analyse();
	}
	
	public Analysis getDepthMetrics() {
		return depth.analyse();
	}
}
