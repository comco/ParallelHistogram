package huffman.computers;

import java.io.File;
import java.util.logging.Logger;

import huffman.Constants;
import huffman.pfile.FileJob;
import huffman.pfile.FileJobConstructor;
import huffman.pfile.FileSplit;
import huffman.pfile.ParallelFileProcessor;

public class ParallelHistogram implements FileJobConstructor {
	static final Logger LOGGER = Constants.LOGGER;
	
	int numThreads;
	long blockHistograms[][];
	
	void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
		this.blockHistograms = new long[numThreads][Constants.NUM_CHARS];
	}
	
	public long[] computeHistogram(FileSplit split) throws InterruptedException {
		LOGGER.fine(String.format("computing histogram of file (%s)",
				split.file.toPath()));

		setNumThreads(split.numThreads);
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
						split.file.toPath(), elapsedTime / 1000));
		return histogram;
	}
	
	@Override
	public Runnable constructJob(FileSplit split, int threadId, long offset,
			long size) {
		return this.new HistogramJob(split, threadId, offset, size);
	}
	
	class HistogramJob extends FileJob {
		public HistogramJob(FileSplit split, int threadId, long offset, long size) {
			super(split, threadId, offset, size);
		}

		@Override
		protected void processBlock(byte[] block) {
			for (int i = 0; i < size; ++i) {
				byte b = block[i];
				++blockHistograms[threadId][b & 0xff];
			}
		}
	}
	
	static void printHist(long[] histogram) {
		for (int i = 0; i < histogram.length; ++i) {
			System.out.println(histogram[i]);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		File file = new File("C:/data/input.txt");
		FileSplit split = new FileSplit(file, "r", 2);
		ParallelHistogram histc = new ParallelHistogram();
		long[] hist = histc.computeHistogram(split);
		System.out.println("histogram: ");
		printHist(hist);
	}
}
