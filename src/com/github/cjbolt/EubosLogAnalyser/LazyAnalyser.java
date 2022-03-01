package com.github.cjbolt.EubosLogAnalyser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LazyAnalyser extends Analyser {
	List<Long> nodes;
	List<Long> alpha;
	List<Long> beta;
	List<Integer> errorCount;
	List<Integer> maxError;
	List<Integer> averageError;
	List<Integer> exceededCount;
	List<Integer> MaxExceededValue;
	
	public class LazyAnalysis extends Analysis{
		private Float alphaCutOffs;
		private Float betaCutOffs;

		public LazyAnalysis(Integer max, Integer min, Integer mean, Float alpha, Float beta) {
			super(max, min, mean);
			alphaCutOffs = alpha;
			betaCutOffs = beta;
		}
		
		public String toString() {
			return String.format("alphaCut %.1f%% betaCut %.1f%% errorMax %d, errorMean %d", alphaCutOffs, betaCutOffs, max, mean);
		}
	}
	
	public LazyAnalyser() {
		super();
		nodes = new ArrayList<Long>();
		alpha = new ArrayList<Long>();
		beta = new ArrayList<Long>();
		errorCount = new ArrayList<Integer>();
		maxError = new ArrayList<Integer>();
		averageError = new ArrayList<Integer>();
		exceededCount = new ArrayList<Integer>();
		MaxExceededValue = new ArrayList<Integer>();
	}
	
	public void addRecord(String record) {
		String [] tokens = record.split("[, <>]+");
		for (String str : tokens) {
			String temp[] = null;
			// "LazyStats A=%d B=%d nodes=%d failSum=%d maxFail=%d meanFail=%.1f exceededCount=%d maxExceeded=%d"
			if (str.contains("A=")) {
				temp = str.split("=");
				alpha.add(Long.valueOf(temp[1]));
			} else if (str.contains("B=")) {
				temp = str.split("=");
				beta.add(Long.valueOf(temp[1]));
			} else if (str.contains("nodes=")) {
				temp = str.split("=");
				nodes.add(Long.valueOf(temp[1]));
			} else if (str.contains("failSum=")) {
				temp = str.split("=");
				errorCount.add(Integer.valueOf(temp[1]));
			} else if (str.contains("maxFail=")) {
				temp = str.split("=");
				maxError.add(Integer.valueOf(temp[1]));
			} else if (str.contains("meanFail=")) {
				temp = str.split("=");
				averageError.add(Math.round(Float.valueOf(temp[1])));
			} else if (str.contains("exceededCount=")) {
				temp = str.split("=");
				exceededCount.add(Integer.valueOf(temp[1]));
			} else if (str.contains("maxExceeded=")) {
				temp = str.split("=");
				MaxExceededValue.add(Integer.valueOf(temp[1]));
			} else {
				assert false : "Unknown token in LazyStatistics parsing.";
			}
		}
	}

	private Float getAverageAlpha() {
		Long totalAlphaCut = alpha.stream().mapToLong(Long::longValue).sum();
		Long totalNodes = nodes.stream().mapToLong(Long::longValue).sum();
		Float averageAlphaCutPercent = (totalAlphaCut * 100.0f) / totalNodes;
		return averageAlphaCutPercent;
	}
	
	private Float getAverageBeta() {
		Long totalBetaCut = beta.stream().mapToLong(Long::longValue).sum();
		Long totalNodes = nodes.stream().mapToLong(Long::longValue).sum();
		Float averageCutPercent = (totalBetaCut * 100.0f) / totalNodes;
		return averageCutPercent;
	}
	
	public LazyAnalysis analyse() {
		if (nodes.size() != 0 && maxError.size() != 0 && averageError.size() != 0) {
			return new LazyAnalysis(Collections.max(maxError), 0, calculateAverage(averageError), getAverageAlpha(), getAverageBeta());
		} else {
			return new LazyAnalysis(0, 0, 0, 0.0f, 0.0f);
		}
	}
}