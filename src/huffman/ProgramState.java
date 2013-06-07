package huffman;

import java.util.logging.Level;
import java.util.logging.Logger;

import huffman.computers.HistogramAlgorithm;

public class ProgramState {
	static final Logger LOGGER = Logger.getLogger("huffman");
	
	private String inputFilename;
	private String outputFilename;
	private int	numThreads;
	private HistogramAlgorithm algorithm;

	private byte[] data;
	private int[] histogram;

	public String getInputFilename() {
		return inputFilename;
	}
	public void setInputFilename(String inputFileName) {
		LOGGER.log(Level.FINER, "setting input file name to: {0}", inputFileName);
		this.inputFilename = inputFileName;
	}
	public String getOutputFilename() {
		return outputFilename;
	}
	public void setOutputFilename(String outputFileName) {
		LOGGER.log(Level.FINER, "setting input file name to: {0}", outputFileName);
		this.outputFilename = outputFileName;
	}
	public int getNumThreads() {
		return numThreads;
	}
	public void setNumThreads(int numThreads) {
		LOGGER.log(Level.FINER, "setting number of threads to: {0}", numThreads);
		this.numThreads = numThreads;
	}
	public HistogramAlgorithm getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(HistogramAlgorithm algorithm) {
		LOGGER.log(Level.FINER, "setting algorithm to: {0}", algorithm);
		this.algorithm = algorithm;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int[] getHistogram() {
		return histogram;
	}
	public void setHistogram(int[] histogram) {
		this.histogram = histogram;
	}
}
