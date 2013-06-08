package huffman;

import huffman.computers.HistogramAlgorithm;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;

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
	JComboBox<HistogramAlgorithm> algorithmCombo;
	JButton computeButton;
	JButton encodeButton;
	JButton decodeButton;
	
	public GUIExecutor(ProgramState state) {
		LOGGER.log(Level.FINE, "creating gui...");
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
		long estimatedTime = System.currentTimeMillis() - startTime;
		String mess =  "Histogram computed. The elapsed time is " + estimatedTime + " mililiseconds.";
		JOptionPane.showMessageDialog(null, mess);
	
		super.handleResult();	
		
	}
}
