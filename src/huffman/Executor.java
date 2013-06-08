package huffman;

import huffman.computers.HistogramComputer;
import huffman.pfile.FileSplit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public abstract class Executor {
	static final Logger LOGGER = Logger.getLogger("huffman");
	
	ProgramState state;
	long startTime;
	public void execute() {
		try {
			readFile();
			startTime = System.nanoTime();
			handleResult();
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "input file not found");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "could not read input file");
		}
	}
	
	protected void readFile() throws IOException {
		LOGGER.fine(String.format("reading input file: (%s)", state.getOriginalFilename()));
		File inputFile = new File(state.getOriginalFilename());
		state.setData(FileUtils.readFileToByteArray(inputFile));
		
		LOGGER.fine("creating computer...");
		HistogramComputer computer = HistogramComputer.createComputer(
				state.getAlgorithm(), state.getNumThreads(), state.getData());

		LOGGER.fine("computing histogram...");
		File decoded = new File(state.getOriginalFilename());
		FileSplit split = new FileSplit(decoded, "r", state.getNumThreads());
		try {
			state.setHistogram(computer.computeHistogram(split));
			LOGGER.fine("histogram computed.");
		} catch (InterruptedException e) {
			LOGGER.severe("histogram computation failed");
		}
	}
	
	protected void handleResult() {
		// write the histogram to an output file
		if (state.getHistogramFilename() != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						state.getHistogramFilename()));
				for (int i = 0; i < state.getHistogram().length; ++i) {
					writer.write(Long.toString(state.getHistogram()[i]));
					writer.newLine();
				}
				writer.close();
				
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "could not write output file");
			}
		}
	}
}
