package huffman;

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
		// print elapsed time
		long elapsedTimeNano = System.nanoTime() - startTime;
		long elapsedTimeMilli = elapsedTimeNano / 1000 / 1000;
		System.out.println("time: " + elapsedTimeMilli);
	}
}
