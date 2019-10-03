import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class EubosLogParser {
  
	public static void main(String[] args) {
	    File f = new File("C:\\Users\\Chris\\lichess-bot-master");

	    FilenameFilter textFilter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith("_eubos_uci_log.txt");
	        }
	    };

	    File[] files = f.listFiles(textFilter);
	    for (File file : files) {
	        if (file.isFile()) {
	        	EubosLogFileAnalyser lfa = new EubosLogFileAnalyser(file);
	        	try {
					System.out.println(file.getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	System.out.println("Speed in nps: " + lfa.getSpeedMetrics());
	        	System.out.println("Depth in ply: " + lfa.getDepthMetrics());
	        }
	    }
	}
}
