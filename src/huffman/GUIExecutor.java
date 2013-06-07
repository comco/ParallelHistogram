package huffman;

import huffman.computers.HistogramAlgorithm;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * GUI interface of the parallel histogram program.
 *
 */
public class GUIExecutor extends Executor {
	static final Logger LOGGER = Logger.getLogger("huffman");
	JFrame frame;
	JPanel panel;

	JTextField inputFilenameField;
	JTextField outputFilenameField;
	
	JSpinner numThreadsSpinner;
	JComboBox<HistogramAlgorithm> algorithmCombo;
	JButton computeButton;
	
	public GUIExecutor(ProgramState state) {
		LOGGER.log(Level.FINE, "creating gui...");
		this.state = state;
		
		frame = new JFrame("Parallel Histogram");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel(new GridLayout(5, 2));
		
		// input filename
		{
			panel.add(new JLabel("input filename: "));
			
			inputFilenameField = new JTextField(state.getInputFilename());
			inputFilenameField.addActionListener(this.new InputFilenameActionListener());
			panel.add(inputFilenameField);
		}
		
		// output filename
		{
			panel.add(new JLabel("output filename: "));
			outputFilenameField = new JTextField(state.getOutputFilename());
			outputFilenameField.addActionListener(this.new OutputFilenameActionListener());
			panel.add(outputFilenameField);
		}
		
		// number of threads
		{
			JLabel numThreadsLabel = new JLabel("number of threads: ");
			panel.add(numThreadsLabel);
			int value = state.getNumThreads();
			SpinnerModel model = new SpinnerNumberModel(value, 1, 20, 1);
			numThreadsSpinner = new JSpinner(model);
			numThreadsSpinner.addChangeListener(this.new NumThreadsChangeListener());
			panel.add(numThreadsSpinner);
		}
		
		// algorithm
		{
			panel.add(new JLabel("algorithm: "));
			algorithmCombo = new JComboBox<>(HistogramAlgorithm.values());
			algorithmCombo.setSelectedItem(state.getAlgorithm());
			algorithmCombo.addActionListener(this.new AlgorithmActionListener());
			panel.add(algorithmCombo);
		}
		
		// compute button
		{
			panel.add(new JLabel());
			computeButton = new JButton("compute histogram");
			computeButton.addActionListener(this.new ComputeActionListener());
			panel.add(computeButton);
		}
		
		frame.add(panel);
		frame.pack();
	}
	
	public void show() {
		frame.setVisible(true);
	}
	
	protected void handleResult() {
		// show a message
		JOptionPane.showMessageDialog(null, "Histogram computed.");
		super.handleResult();
	}
	
	// Listener classes
	private class InputFilenameActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			state.setInputFilename(inputFilenameField.getText());
		}

	}
	
	private class OutputFilenameActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			state.setOutputFilename(outputFilenameField.getText());
		}

	}
	
	private class NumThreadsChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			state.setNumThreads((Integer) numThreadsSpinner.getValue());
		}
		
	}
	
	private class AlgorithmActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			state.setAlgorithm((HistogramAlgorithm) algorithmCombo.getSelectedItem());
		}
		
	}
	
	private class ComputeActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LOGGER.log(Level.FINE, "computing histogram...");
			execute();
		}
	}
}
