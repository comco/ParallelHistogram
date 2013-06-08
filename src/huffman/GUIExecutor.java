package huffman;

import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * GUI interface of the parallel histogram program.
 *
 */
public class GUIExecutor extends Executor {
	static final Logger LOGGER = Logger.getLogger("huffman");
	JFrame frame;
	JTabbedPane tabPane;

	JTextField originalFilenameField;
	JTextField histogramFilenameField;
	JTextField encodedFilenameField;
	JTextField decodedFilenameField;
	
	JSpinner numThreadsSpinner;
	JComboBox algorithmCombo;
	JButton computeButton;
	JButton encodeButton;
	JButton decodeButton;
	
	public GUIExecutor(ProgramState state) {
		LOGGER.fine("creating gui...");
		this.state = state;
		
		frame = new JFrame("Parallel Histogram");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		JPanel histogramPanel = new HistogramPanel(this, state);
		ProgramState comprState = new ProgramState();
		JPanel compressionPanel = new CompressionPanel(this, comprState);
		tabPane = new JTabbedPane();
		{
			tabPane.addTab("Histogram", null, histogramPanel, "Create histogram");
			tabPane.addTab("Compress", null, compressionPanel, "Encode and decode file");
		}
		
		frame.add(tabPane);
		frame.pack();
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	protected void handleResult() {
		// show a message
		long estimatedTimeNano = System.nanoTime() - startTime;
		long estimatedTimeMilli = estimatedTimeNano / 1000 / 1000;
		String mess =  "Histogram computed. The elapsed time is " + estimatedTimeMilli + " mililiseconds.";
		JOptionPane.showMessageDialog(null, mess);
	
		super.handleResult();	
		
	}
}
