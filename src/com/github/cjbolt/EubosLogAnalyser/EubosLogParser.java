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
	        }
	    }
	    System.out.println(String.format("Average speed over %d games is %d nps", files.length, averageSpeed/files.length));
	    System.out.println("Move analysis:" + moves.analyse());
	}
}
