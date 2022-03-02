package com.github.cjbolt.EubosLogAnalyser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LazyAnalyser extends Analyser {
	List<Long> nodes;
	List<Long> alpha;
	List<Long> beta;
	List<Integer> errorCount;
	List<Integer> exceededCount;
	List<Integer> MaxExceededValue;
	
	public class LazyAnalysis extends Analysis{
		public Float alphaCutOffs;
		public Float betaCutOffs;
		public Double failRate;

		public LazyAnalysis(Integer max, Integer min, Integer mean, Float alpha, Float beta, Double failRate) {
			super(max, min, mean);
			alphaCutOffs = alpha;
			betaCutOffs = beta;
			this.failRate = failRate;
		}
		
		public String toString() {
			return String.format("alphaCut %.1f%% betaCut %.1f%% errorMax %d, errorMean %d failRate %.7f%%", alphaCutOffs, betaCutOffs, max, mean, failRate);
		}
	}
	
	public LazyAnalyser() {
		super();
		nodes = new ArrayList<Long>();
		alpha = new ArrayList<Long>();
		beta = new ArrayList<Long>();
		errorCount = new ArrayList<Integer>();
		exceededCount = new ArrayList<Integer>();
		MaxExceededValue = new ArrayList<Integer>();
	}
	
	public void addRecord(String record) {
		String [] tokens = record.split("[, <>]+");
		for (String str : tokens) {
			String temp[] = null;
			// "LazyStats A=%d B=%d nodes=%d failSum=%d exceededCount=%d maxExceeded=%d"
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
	
	private Double getFailRate() {
		Long totalNumErrors = (long) errorCount.stream().mapToInt(Integer::intValue).sum();
		Long totalNodes = nodes.stream().mapToLong(Long::longValue).sum();
		Double lazyThreshFailRatePercent = (totalNumErrors * 100.0d)/totalNodes;
		return lazyThreshFailRatePercent;
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
		if (nodes.size() != 0) {
			// Also report maxErrorCount (total worst total number of Lazy Eval threshold errors in a search)
			return new LazyAnalysis(Collections.max(MaxExceededValue), 0, calculateAverage(MaxExceededValue), getAverageAlpha(), getAverageBeta(), getFailRate());
		} else {
			return new LazyAnalysis(0, 0, 0, 0.0f, 0.0f, 0.0d);
		}
	}
}