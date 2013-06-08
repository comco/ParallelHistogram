package huffman.computers;

import huffman.pfile.FileSplit;


/**
 * Interface representing a particular histogram computation algorithm.
 */
public abstract class HistogramComputer {
	/**
	 * Performs the histogram computation
	 * 
	 * @return
	 * @throws InterruptedException 
	 */
	public abstract long[] computeHistogram(FileSplit split) throws InterruptedException;

	/**
	 * Creates a histogram computer from an option
	 * @param algorithm
	 * @param numThreads
	 * @param data
	 * @return
	 */
	public static HistogramComputer createComputer(
			HistogramComputerOption algorithm, final int numThreads,
			final byte[] data) {
		HistogramComputer computer;
		switch (algorithm) {
		case FULLY_PARALLEL:
			computer = new FullyParallelHistogram();
			break;
		case FISRT_STEP_PARALLEL:
			computer = new FirstStepParallelHistogram();
			break;
		default:
			computer = new FullyParallelHistogram();
		}

		return computer;
	}
}
