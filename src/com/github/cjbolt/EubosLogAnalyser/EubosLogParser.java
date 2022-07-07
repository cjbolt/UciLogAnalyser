package com.github.cjbolt.EubosLogAnalyser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class EubosLogParser {
  
	public static void main(String[] args) {
		String filePath = System.getenv("EUBOS_CHESS_ENGINE_LOGS_FILEPATH");
	    File f = new File(filePath);

	    FilenameFilter textFilter = new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith("_eubos_uci_log.txt");
	        }
	    };

	    Analyser moves = new Analyser();
	    File[] files = f.listFiles(textFilter);
	    Long averageSpeed = (long)0;
	    Float alpha = 0.0f;
	    Float beta = 0.0f;
	    Double worstErrorRate = 0.0d;
	    Integer worstError = 0;
	    Integer meanError = 0;
	    Long sumMeanError = 0L;
	    Integer lazyErrorFileCount = 0;
	    
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
	        	System.out.println(lfa.getLazyAnalysis());
	        	System.out.println();
	        	
	        	averageSpeed += lfa.getSpeedMetrics().getMean();
	        	moves.addRecord(lfa.getNumMovesInGame());
	        	
	        	alpha += lfa.getLazyAnalysis().alphaCutOffs;
	        	beta += lfa.getLazyAnalysis().betaCutOffs;
	        	worstError = Math.max(worstError, lfa.getLazyAnalysis().getMax());
	        	worstErrorRate = Math.max(worstErrorRate, lfa.getLazyAnalysis().failRate);
	        	sumMeanError += lfa.getLazyAnalysis().mean;
	        	
	        	if (lfa.getLazyAnalysis().getMax() != 0) {
	        		lazyErrorFileCount++;
	        	}
	        }
	    }
	    long averageMeanError = sumMeanError / files.length;
	    
	    System.out.println(String.format("Average speed over %d games is %d nps", files.length, averageSpeed/files.length));
	    System.out.println(String.format("Move analysis: %s", moves.analyse()));
	    System.out.println(String.format(
	    		"Overall lazy evaluation statistics: alpha cut %.1f%%, beta cut %.1f%%, average mean error %d", 
	    		alpha/files.length, beta/files.length, averageMeanError));
	    if (lazyErrorFileCount != 0) {
	    	System.out.println(String.format(
	    			"LAZY EVAL THRESHOLD WAS EXCEEDED! worst error %d centipawns, worst rate %.7f%%",
	    			worstError, worstErrorRate));
	    	System.out.println(String.format("errors in %d files out of %d", lazyErrorFileCount, files.length));
	    }
	}
}
