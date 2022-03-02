package com.github.cjbolt.EubosLogAnalyser;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class EubosLogParser {
  
	public static void main(String[] args) {
	    File f = new File("C:\\Users\\Chris\\lichess-bot-master");
	    f = new File("C:\\Program Files (x86)\\Arena\\Engines\\EubosDev");

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
	        	
	        	if (lfa.getLazyAnalysis().getMax() != 0) {
	        		lazyErrorFileCount++;
	        	}
	        }
	    }
	    
	    System.out.println(String.format("Average speed over %d games is %d nps", files.length, averageSpeed/files.length));
	    System.out.println(String.format("Move analysis: %s", moves.analyse()));
	    System.out.println(String.format(
	    		"Overall lazy evaluation statistics: alpha cut %.1f%%, beta cut %.1f%%", 
	    		alpha/files.length, beta/files.length));
	    if (lazyErrorFileCount != 0) {
	    	System.out.println(String.format(
	    			"LAZY EVAL THRESHOLD WAS EXCEEDED! worst error %d centipawns, worst rate %.7f%%", worstError, worstErrorRate));
	    	System.out.println(String.format("errors in %d files out of %d", lazyErrorFileCount, files.length));
	    }
	}
}
