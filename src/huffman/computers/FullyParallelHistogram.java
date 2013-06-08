package huffman.computers;

import huffman.Constants;
import huffman.pfile.FileJob;
import huffman.pfile.FileJobConstructor;
import huffman.pfile.FileSplit;
import huffman.pfile.ParallelFileProcessor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FullyParallelHistogram extends HistogramComputer implements
		FileJobConstructor {
	static final Logger LOGGER = Constants.LOGGER;

	int numThreads;
	long blockHistograms[][];

	CyclicBarrier blockHistogramBarrier;
	CyclicBarrier[] reduceLevelBarriers;

	void setNumThreads(int numThreads, long length) {
		this.numThreads = numThreads;
		this.blockHistograms = new long[numThreads][Constants.NUM_CHARS];
		// initialize barriers
		blockHistogramBarrier = new CyclicBarrier(numThreads);
		reduceLevelBarriers = new CyclicBarrier[1 + level(length)];
		for (int i = 0; i <= level(length); ++i) {
			reduceLevelBarriers[i] = new CyclicBarrier(numThreads);
		}
	}

	public FullyParallelHistogram() {
	}

	public long[] computeHistogram(FileSplit split) throws InterruptedException {
		LOGGER.fine(String.format("computing histogram of file (%s)",
				split.file.toPath()));

		setNumThreads(split.numThreads, split.fileSize);
		ParallelFileProcessor processor = new ParallelFileProcessor(split, this);
		long elapsedTime = processor.processFile();

		long[] histogram = new long[Constants.NUM_CHARS];
		for (int i = 0; i < numThreads; ++i) {
			for (int j = 0; j < Constants.NUM_CHARS; ++j) {
				histogram[j] += blockHistograms[i][j];
			}
		}

		LOGGER.fine(String
				.format("histogram computation for file (%s) finished for %d milliseconds",
						split.file.toPath(), elapsedTime / 1000 / 1000));
		return histogram;
	}

	@Override
	public Runnable constructJob(FileSplit split, int threadId, long offset,
			long size) {
		return this.new FullyParallelHistogramJob(split, threadId, offset, size);
	}

	class FullyParallelHistogramJob extends FileJob {
		public FullyParallelHistogramJob(FileSplit split, int threadId,
				long offset, long size) {
			super(split, threadId, offset, size);
		}

		@Override
		protected void processBlock(byte[] block) {
			for (int i = 0; i < size; ++i) {
				byte b = block[i];
				++blockHistograms[threadId][b & 0xff];
			}
		}

		@Override
		public void run() {
			// perform the usual processing
			super.run();
			// wait at histogram block barrier
			LOGGER.finer(String
					.format("%s: histogram block computed; got to the histogramBlockBarrier...\n",
							threadPrompt()));
			try {
				blockHistogramBarrier.await();
				threadLogFiner("got after the block histogram barrier\n");
				int step = 1;
				int level = 0;
				while (step < numThreads) {
					if (atLevel(level, threadId)) {
						threadLogFiner("at level %d\n", level);
						mergeHistogramBlocks(threadId, threadId - step);
					}
					threadLogFiner("waiting at level barrier %d\n", level);
					reduceLevelBarriers[level].await();
					step *= 2;
					++level;
				}
				threadLogFiner("at the end\n");
			} catch (InterruptedException e) {
				LOGGER.severe(String
						.format("%s: parallel processing interrupted\n",
								threadPrompt()));
			} catch (BrokenBarrierException e) {
				LOGGER.severe(String
						.format("%s: parallel processing interrupted\n",
								threadPrompt()));
			}
		}

		private void threadLogFiner(String format, Object... args) {
			threadLog(Level.FINER, format, args);
		}

		private void threadLog(Level level, String format, Object... args) {
			String msg = threadPrompt() + ":" + String.format(format, args);
			LOGGER.log(level, msg);
		}
	}

	static int level(final long n) {
		return (63 - Long.numberOfLeadingZeros(n));
	}

	static boolean atLevel(final int level, final long n) {
		return Long.numberOfTrailingZeros(n) == level;
	}

	void mergeHistogramBlocks(final int from, final int to) {
		for (int i = 0; i < Constants.NUM_CHARS; ++i) {
			blockHistograms[to][i] += blockHistograms[from][i];
		}
	}

}
