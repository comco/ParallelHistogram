package huffman;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineExecutor extends Executor {
	static final Logger LOGGER = Logger.getLogger("huffman");

	public CommandLineExecutor(ProgramState state) {
		super();
		this.state = state;
	}

	@Override
	protected void handleResult() {
		super.handleResult();
		// print the histogram on screen
		for (int i = 0; i < state.getHistogram().length; ++i) {
			System.out.print(state.getHistogram()[i] + " ");
		}
		System.out.println();
	}
}
