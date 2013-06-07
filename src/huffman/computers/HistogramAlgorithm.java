package huffman.computers;

/**
 * Enumerates the different algorithms that can be used to compute a histogram.
 */
public enum HistogramAlgorithm {
	FULLY_PARALLEL("fully_parallel", "Fully parallel"),
	FISRT_STEP_PARALLEL("first_step_parallel", "With parallel first step");
	
	public static HistogramAlgorithm DEFAULT_ALGORITHM = FULLY_PARALLEL;
	
	HistogramAlgorithm(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public final String name;
	public final String description;
	
	public static HistogramAlgorithm getAlgorithm(String name) {
		HistogramAlgorithm result = null;
		for (HistogramAlgorithm algorithm : HistogramAlgorithm.values()) {
			if (algorithm.name.equals(name)) {
				result = algorithm;
			}
		}
		if (result == null) {
			return DEFAULT_ALGORITHM;
		}
		return result;
	}
	
	public String toString() {
		return name;
	}
}