package huffman;

/**
 * Enumerates the different algorithms that can be used to compute a histogram.
 */
public enum HistogramAlgorithm {
	FULLY_PARALLEL("Напълно паралелен"),
	FISRT_STEP_PARALLEL("С паралелна първа стъпка");
	
	HistogramAlgorithm(String description) {
		this.description = description;
	}
	
	public final String description;
}