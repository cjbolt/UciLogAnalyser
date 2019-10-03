
public class Analysis { 
	private Integer max;
	private Integer min;
	private Integer mean;

	public Analysis(Integer max, Integer min, Integer mean) {
		this.max = max;
		this.min = min;
		this.mean = mean;
	}
	
	public Integer getMax() {
		return max;
	}

	public Integer getMin() {
		return min;
	}

	public Integer getMean() {
		return mean;
	}
	
	public String toString() {
		return String.format("max %d, min %d, mean %d", max, min, mean);
	}
}
