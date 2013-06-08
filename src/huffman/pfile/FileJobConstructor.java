package huffman.pfile;

public interface FileJobConstructor {
	Runnable constructJob(FileSplit split, int threadId, long offset, long size); 
}
