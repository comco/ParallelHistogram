package huffman.pfile;

import java.io.File;

/**
 * Splits a file in parallel to be processed by different threads
 */
public class FileSplit {
	public static final int DEFAULT_BLOCK_SIZE = 64 * 1024; // 64 KB
	public static final long DEFAULT_CHUNK_SIZE = DEFAULT_BLOCK_SIZE * 1024; // 64 MB

	public FileSplit(File file, String mode, long chunkSize, int blockSize,
			int numThreads) {
		super();
		this.file = file;
		this.mode = mode;
		this.fileSize = file.length();
		this.chunkSize = chunkSize;
		this.blockSize = blockSize;
		this.numThreads = numThreads;
	}
	
	public FileSplit(File file, String mode, int numThreads) {
		this(file, mode, DEFAULT_CHUNK_SIZE, DEFAULT_BLOCK_SIZE, numThreads);
	}

	/**
	 * The file to split
	 */
	public final File file;
	
	/**
	 * The mode for reading/writing the file
	 */
	public final String mode;
	
	/**
	 * File size
	 */
	public final long fileSize;
	
	/**
	 * Chunk size - the minimal size a single thread will process
	 */
	public final long chunkSize;
	
	/**
	 * Block size for thread processing
	 */
	public final int blockSize;
	
	/**
	 * Number of threads
	 */
	public final int numThreads;
	
	/**
	 * Computes the number of chunks for the splitted file
	 * @return - the number of chunks
	 */
	long numChunks() {
		return (fileSize + chunkSize - 1) / chunkSize;
	}
	
	/**
	 * Computes the offset for a thread
	 * @param threadId
	 * @return
	 */
	long computeOffset(int threadId) {
		if (threadId == numThreads) {
			return fileSize;
		} else {
			long step = numChunks() / numThreads;
			long remains = numChunks() % numThreads;
			long add = Math.max(0, threadId + remains - numThreads);
			return chunkSize * (step * threadId + add);
		}
	}
	
	/**
	 * Computes the size for a thread
	 * @param threadId
	 * @return
	 */
	long computeSize(int threadId) {
		return computeOffset(threadId + 1) - computeOffset(threadId);
	}
}
