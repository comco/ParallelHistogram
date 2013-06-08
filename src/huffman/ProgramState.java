package huffman;

import java.util.logging.Level;
import java.util.logging.Logger;

import huffman.computers.HistogramComputerOption;

public class ProgramState {
	static final Logger LOGGER = Logger.getLogger("huffman");
	
	public ProgramState() {
		numThreads = 1;
	}
	
	private String originalFilename;
	private String histogramFilename;
	private String encodedFilename;
	private String decodedFilename;

	private int	numThreads;
	private HistogramComputerOption algorithm;

	private byte[] data;
	private long[] histogram;
	private HuffmanCompressor compressor;
	
	public String getDecodedFilename() {
		return decodedFilename;
	}
	public void setDecodedFilename(String decodedFilename) {
		LOGGER.log(Level.FINER, "setting decoded file name to: {0}", decodedFilename);
		this.decodedFilename = decodedFilename;
	}
	public String getEncodedFilename() {
		return encodedFilename;
	}
	public void setEncodedFilename(String encodedFilename) {
		LOGGER.log(Level.FINER, "setting encoded file name to: {0}", encodedFilename);
		this.encodedFilename = encodedFilename;
	}
	public HuffmanCompressor getCompressor() {
		return compressor;
	}
	public void setCompressor(HuffmanCompressor compressor) {
		this.compressor = compressor;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}
	public void setOriginalFilename(String originalFilename) {
		LOGGER.log(Level.FINER, "setting original file name to: {0}", originalFilename);
		this.originalFilename = originalFilename;
	}
	public String getHistogramFilename() {
		return histogramFilename;
	}
	public void setHistogramFilename(String outputFileName) {
		LOGGER.log(Level.FINER, "setting input file name to: {0}", outputFileName);
		this.histogramFilename = outputFileName;
	}
	public int getNumThreads() {
		return numThreads;
	}
	public void setNumThreads(int numThreads) {
		LOGGER.log(Level.FINER, "setting number of threads to: {0}", numThreads);
		this.numThreads = numThreads;
	}
	public HistogramComputerOption getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(HistogramComputerOption algorithm) {
		LOGGER.log(Level.FINER, "setting algorithm to: {0}", algorithm);
		this.algorithm = algorithm;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public long[] getHistogram() {
		return histogram;
	}
	public void setHistogram(long[] histogram) {
		this.histogram = histogram;
	}
}
