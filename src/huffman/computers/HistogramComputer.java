package huffman.computers;

import huffman.HistogramAlgorithm;

/**
 * Interface representing a particular histogram computation algorithm.
 */
public abstract class HistogramComputer {
	/**
	 * Performs the histogram computation.
	 * @return
	 */
	abstract int[] compute();
	
	public static HistogramComputer createComputer(HistogramAlgorithm algorithm, final int numThreads, final byte[] data) {
		HistogramComputer computer;
		switch (algorithm) {
		case FULLY_PARALLEL: 
			//computer = new ParallelHistogram(numThreads, data);
			break;
		case FISRT_STEP_PARALLEL:
			//com
		}
		
		return null;
	}
}
