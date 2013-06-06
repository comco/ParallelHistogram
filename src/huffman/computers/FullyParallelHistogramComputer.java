package huffman.computers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class FullyParallelHistogramComputer extends HistogramComputer {
	static final int NUM_CHARS = 256;
	
	final int numThreads;
	final byte[] data;
	final int[][] histogramBlocks;
	
	final Thread[] threads;
	
	final CyclicBarrier histogramBlockBarrier;
	final CyclicBarrier[] reduceLevelBarriers;
	
	public FullyParallelHistogramComputer(final int numThreads, final byte[] data) {
		System.out.format("Creating a ParallelHistogram object for %d threads", numThreads);
		this.numThreads = numThreads;
		this.data = data;
		this.histogramBlocks = new int[numThreads][NUM_CHARS];
		
		// initialize threads
		System.out.println("Initializing blocks...");
		threads = new Thread[numThreads];
		int dataOffset = 0;
		for (int id = 0; id < numThreads; ++id) {
			int dataSize = (data.length + id) / numThreads; 
			threads[id] = new Thread(new HistogramJob(id, dataOffset, dataSize));
			dataOffset += dataSize;
		}
		
		// initialize barriers
		System.out.println("Initializing barriers...");
		histogramBlockBarrier = new CyclicBarrier(numThreads);
		reduceLevelBarriers = new CyclicBarrier[1 + level(data.length)];
		for (int i = 0; i <= level(data.length); ++i) {
			reduceLevelBarriers[i] = new CyclicBarrier(numThreads);
		}
	}
	
	@Override
	public int[] compute() {
		try {
			System.out.println("Starting threads...");
			for (int id = 0; id < numThreads; ++id) {
				threads[id].start();
			}
			System.out.println("Waiting for the threads to finish...");
			for (int id = 0; id < numThreads; ++id) {
					threads[id].join();
			}
			
			System.out.println("Threads have finished.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return histogramBlocks[0];
	}
	
	static int level(final int n) {
		return (31 - Integer.numberOfLeadingZeros(n));
	}
	
	static boolean atLevel(final int level, final int n) {
		return Integer.numberOfTrailingZeros(n) == level;
	}
	
	void mergeHistogramBlocks(final int from, final int to) {
		for (int i = 0; i < NUM_CHARS; ++i) {
			histogramBlocks[to][i] += histogramBlocks[from][i];
		}
	}
	
	public static void main(String[] args) {
		
		for (int numThreads = 1; numThreads <= 4; ++numThreads) {
			for (int n = 10000; n <= 1000000000; n *= 10) {
				long millis = test(numThreads, n);
				System.out.format("numThreads = %d, n = %d, time = %d\n", numThreads, n, millis);
			}
		}
		
//		int n = 1000000000;
//		int numThreads = 4;
//		byte[] data = new byte[n];
//		for (int i = 0; i < n; ++i) {
//			data[i] = (byte) i;
//		}
//		
//		long startProccessing = System.currentTimeMillis();
//		
//		ParallelHistogram parallelHistogram = new ParallelHistogram(numThreads, data);
//		//int[] histogram = parallelHistogram.run();
//		
//		long endProcessing = System.currentTimeMillis();
//		//System.out.format("Processing took %d milliseconds.\n", mean(parallelHistogram));
	}
	
	public static long test(int numThreads, int n) {
		byte[] data = new byte[n];
		for (int i = 0; i < n; ++i) {
			data[i] = (byte) i;
		}
		
		long startProccessing = System.currentTimeMillis();
		
		FullyParallelHistogramComputer parallelHistogram = new FullyParallelHistogramComputer(numThreads, data);
		int[] histogram = parallelHistogram.compute();
		
		long endProcessing = System.currentTimeMillis();
		return endProcessing - startProccessing;
	}
	
	public static long mean(FullyParallelHistogramComputer hist) {
		int tests = 5;
		long startProcessing = System.currentTimeMillis();
		for (int i = 0; i < tests; ++i) {
			hist.compute();
		}
		long endProcessing = System.currentTimeMillis();
		return (endProcessing - startProcessing) / tests;
	}
	
	static void writeHistogram(final int[] histogram) {
		for (int i = 0; i < histogram.length; ++i) {
			System.out.println(histogram[i]);
		}
	}
	
	class HistogramJob implements Runnable {
		final int id;
		final int dataOffset;
		final int dataSize;
		
		public HistogramJob(final int id, final int dataOffset, final int dataSize) {
			System.out.format("Creating histogram job with id=%d, dataOffset=%d, dataSize=%d\n", id, dataOffset, dataSize);
			this.id = id;
			this.dataOffset = dataOffset;
			this.dataSize = dataSize;
		}

		void incrementHistogramBlock(final byte b) {
			++histogramBlocks[id][b + 128];
		}
		
		byte getDataByteAt(final int at) {
			return data[dataOffset + at];
		}
		
		
		@Override
		public void run() {
			try {
				System.out.format("%d: Thread started; computing histogram block...\n", id);
				// compute the histogram block
				for (int i = 0; i < dataSize; ++i) {
					byte b = getDataByteAt(i);
					incrementHistogramBlock(b);
				}
				
				// wait for all threads to compute its results
				System.out.format("%d: Histogram block computed; got to the histogramBlockBarrier...\n", id);
				histogramBlockBarrier.await();
				
				System.out.format("%d: Got after the histogram block barrier.\n", id);
				int step = 1;
				int level = 0;
				while (step < data.length) {
					if (atLevel(level, id)) {
						System.out.format("%d: at level %d\n", id, level);
						mergeHistogramBlocks(id, id - step);
					}
					System.out.format("%d: waiting at level barrier %d\n", id, level);
					//reduceLevelBarriers[level].await();
					step *= 2;
					++level;
				}
				System.out.format("%d: At the end.\n", id);
			} catch (InterruptedException | BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

