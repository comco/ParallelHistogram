package huffman.computers;


/**
 * Interface representing a particular histogram computation algorithm.
 */
public abstract class HistogramComputer {
	/**
	 * Performs the histogram computation.
	 * 
	 * @return
	 */
	public abstract int[] compute();

	public static HistogramComputer createComputer(
			HistogramAlgorithm algorithm, final int numThreads,
			final byte[] data) {
		HistogramComputer computer;
		switch (algorithm) {
		case FULLY_PARALLEL:
			computer = new FullyParallelHistogramComputer(numThreads, data);
			break;
		case FISRT_STEP_PARALLEL:
			computer = new FirstStepParallelHistogramComputer(numThreads, data);
			break;
		default:
			computer = new FullyParallelHistogramComputer(numThreads, data);
		}

		return computer;
	}
}
