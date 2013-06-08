package huffman.pfile;

import huffman.Constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

public abstract class FileJob implements Runnable {
	static final Logger LOGGER = Constants.LOGGER;
	
	protected final FileSplit split;
	protected final int threadId;
	protected final long offset;
	protected final long size;
	
	public FileJob(FileSplit split, int threadId, long offset, long size) {
		this.split = split;
		this.threadId = threadId;
		this.offset = offset;
		this.size = size;
	}

	@Override
	public void run() {
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(split.file, split.mode);
			file.seek(offset);
			byte[] block = new byte[split.blockSize];
			long processed = 0;
			while (processed < size) {
				file.read(block);
				processBlock(block);
				processed += split.blockSize;
			}
		} catch (FileNotFoundException e) {
			String msg = String.format("%s: file (%s) not found", thread(), split.file.toPath());
			LOGGER.severe(msg);
		} catch (IOException e) {
			String msg = String.format("%s: can't process file (%s)", thread(), split.file.toPath());
			LOGGER.severe(msg);
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					// :)
				}
			}
		}
	}
	
	protected abstract void processBlock(byte[] block);

	public String thread() {
		return String.format("[thread %d]", threadId);
	}
}
