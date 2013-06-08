package huffman.pfile;

import huffman.Constants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ParallelFileProcessor {
	static final Logger LOGGER = Constants.LOGGER;
	
	private FileSplit split;
	private FileJobConstructor jobConstructor;
	
	public ParallelFileProcessor(FileSplit split,
			FileJobConstructor jobConstructor) {
		super();
		this.split = split;
		this.jobConstructor = jobConstructor;
		initializeThreads();
	}

	private Thread[] threads;
	
	/**
	 * Initializes threads
	 */
	private void initializeThreads() {
		threads = new Thread[split.numThreads];
		for (int threadId = 0; threadId < threads.length; ++threadId) {
			long offset = split.computeOffset(threadId);
			long size = split.computeSize(threadId);
			Runnable job = jobConstructor.constructJob(split, threadId, offset, size);
			threads[threadId] = new Thread(job);
		}
	}
	
	/**
	 * Processes the file
	 * @return - the elapsed time, in nanoseconds
	 * @throws InterruptedException
	 */
	public long processFile() throws InterruptedException {
		LOGGER.log(Level.FINE, "starting parallel file processing");

		long startTime = System.nanoTime();
		for (Thread t : threads) {
			t.start();
		}
		LOGGER.log(Level.FINE, "waiting for parallel jobs to finish");
		
		for (Thread t : threads) {
			t.join();
		}
		long elapsedTime = System.nanoTime() - startTime;
		LOGGER.log(Level.FINE, "parallel file processing finished for: " + elapsedTime / 1000 + "milliseconds");
		
		return elapsedTime;
	}
}
