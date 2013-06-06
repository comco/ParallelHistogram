package huffman;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

public class Program {

	public static void main(String[] args) {
		ProgramState state = new ProgramState();
		
		Options options = new Options();
		options.addOption("t", false, "number of tasks");
		options.addOption("q", false, "quiet mode - do not use GUI");
		options.addOption("f", false, "input filename");
		
		//CommandLineParser parser = new DefaultParser();
	}
}
