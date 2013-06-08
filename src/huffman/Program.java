package huffman;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import huffman.computers.HistogramComputerOption;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * The program class
 */
public class Program {
	static final Logger LOGGER = Constants.LOGGER;
	
	static Options options;
	/**
	 * Entry point to the program
	 * @param args
	 */
	public static void main(String[] args) {
		buildOptions();
		CommandLineParser parser = new GnuParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			initializeLoggger(cmd);
			
			if (cmd.hasOption('h')) {
				displayHelp();
			}
			
			ProgramState state = buildProgramState(cmd);
			if (cmd.hasOption('q')) {
				CommandLineExecutor executor = new CommandLineExecutor(state);
				executor.execute();
			} else {
				GUIExecutor executor = new GUIExecutor(state);
				executor.show();
			}
			
		} catch (ParseException e) {
			LOGGER.log(Level.SEVERE, "cannot parse command line");
		}
	}
	
	public static ProgramState buildProgramState(CommandLine cmd) {
		ProgramState state = new ProgramState();
		
		if (cmd.hasOption('f')) {
			String inputFilename = cmd.getOptionValue('f');
			LOGGER.fine("input file name is: " + inputFilename);
			state.setOriginalFilename(inputFilename);
		}
		
		if (cmd.hasOption('o')) {
			String outputFilename = cmd.getOptionValue('o');
			LOGGER.fine("output file name is: " + outputFilename);
			state.setHistogramFilename(outputFilename);
		}
		
		int numThreads = 1;
		if (cmd.hasOption('t')) {
			String threadsString = cmd.getOptionValue('t');
			numThreads = Integer.parseInt(threadsString);
		}
		LOGGER.fine("number of threads is: " + numThreads);
		state.setNumThreads(numThreads);
		
		String algorithmCmd = null;
		if (cmd.hasOption('a')) {
			algorithmCmd = cmd.getOptionValue('a');
		}
		HistogramComputerOption algorithm = HistogramComputerOption.getAlgorithm(algorithmCmd);
		LOGGER.fine("algorithm is: " + algorithm.name);
		state.setAlgorithm(algorithm);
		return state;
	}
	
	public static void displayHelp() {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -cp huffman", options );
	}
	
	private static void initializeLoggger(CommandLine cmd) {
		Level level = Level.FINE;
		if (cmd.hasOption('l')) {
			String levelCmd = cmd.getOptionValue('l');
			if ("finer".equals(levelCmd)) {
				level = Level.FINER;
			}
		}
		LOGGER.setLevel(level);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(level);
		LOGGER.addHandler(handler);
	}

	/**
	 * Builds the command line options structure
	 * @return
	 */
	private static void buildOptions() {
		Option help = new Option("h", "help", false, "help");
		Option quiet = new Option("q", "quiet", false, "quiet mode");
		Option threads = new Option("t", "threads", true, "number of threads");
		Option logLevel = new Option("l", "loglvl", true, "logging level: fine, finer");
		
		Option inputFilename = new Option("f", "filename", true, "input filename");
		Option algorithm = new Option("a", "algorithm", true, "algorithm: fully_parallel, first_step_parallel");
		Option outputFilename = new Option("o", "output", true, "output filename");
		
		options = new Options();
		options.addOption(help);
		options.addOption(quiet);
		options.addOption(inputFilename);
		options.addOption(threads);
		options.addOption(algorithm);
		options.addOption(outputFilename);
		options.addOption(logLevel);
	}
}
