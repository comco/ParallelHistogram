package huffman.computers;

/**
 * Enumerates the different options available for computing the histogram.
 */
public enum HistogramComputerOption {
	FULLY_PARALLEL("fully_parallel", "Fully parallel"),
	FISRT_STEP_PARALLEL("first_step_parallel", "With parallel first step");
	
	public static HistogramComputerOption DEFAULT_ALGORITHM = FULLY_PARALLEL;
	
	HistogramComputerOption(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public final String name;
	public final String description;
	
	public static HistogramComputerOption getAlgorithm(String name) {
		HistogramComputerOption result = null;
		for (HistogramComputerOption algorithm : HistogramComputerOption.values()) {
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