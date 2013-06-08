package huffman;

import huffman.computers.HistogramComputer;

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
			startTime = System.currentTimeMillis();
			handleResult();
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "input file not found");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "could not read input file");
		}
	}
	
	protected void readFile() throws IOException {
		LOGGER.log(Level.FINE, "reading input file: {0}", state.getOriginalFilename());
		File inputFile = new File(state.getOriginalFilename());
		state.setData(FileUtils.readFileToByteArray(inputFile));
		
		LOGGER.log(Level.FINE, "creating computer...");
		HistogramComputer computer = HistogramComputer.createComputer(
				state.getAlgorithm(), state.getNumThreads(), state.getData());

		LOGGER.log(Level.FINE, "computing histogram...");
		state.setHistogram(computer.compute());
		LOGGER.log(Level.FINE, "histogram computed.");
	}
	
	protected void handleResult() {
		// write the histogram to an output file
		if (state.getHistogramFilename() != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						state.getHistogramFilename()));
				for (int i = 0; i < state.getHistogram().length; ++i) {
					writer.write(Integer.toString(state.getHistogram()[i]));
					writer.newLine();
				}
				writer.close();
				
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "could not write output file");
			}
		}
	}
}
