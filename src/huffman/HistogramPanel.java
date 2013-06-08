package huffman;

import huffman.computers.HistogramComputerOption;

import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HistogramPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	static final Logger LOGGER = Logger.getLogger("huffman");
	
	ProgramState state;
	GUIExecutor executor;	

	JTextField originalFilenameField;
	JTextField histogramFilenameField;
	
	JSpinner numThreadsSpinner;
	JComboBox algorithmCombo;
	JButton computeButton;
	
	public HistogramPanel(GUIExecutor executor, ProgramState state) {
		super(new GridLayout(5, 2));
		this.executor = executor;
		this.state = state;
	
		
	// original filename
		{
			this.add(new JLabel("original filename: "));
			
			originalFilenameField = new JTextField(state.getOriginalFilename());
			originalFilenameField.addActionListener(this.new OriginalFilenameActionListener());
			this.add(originalFilenameField);
		}
		
		// histogram filename
		{
			this.add(new JLabel("histogram filename: "));
			histogramFilenameField = new JTextField(state.getHistogramFilename());
			histogramFilenameField.addActionListener(this.new HistogramFilenameActionListener());
			this.add(histogramFilenameField);
		}
		
		// number of threads
		{
			JLabel numThreadsLabel = new JLabel("number of threads: ");
			this.add(numThreadsLabel);
			int value = state.getNumThreads();
			SpinnerModel model = new SpinnerNumberModel(value, 1, 20, 1);
			numThreadsSpinner = new JSpinner(model);
			numThreadsSpinner.addChangeListener(this.new NumThreadsChangeListener());
			this.add(numThreadsSpinner);
		}
		
		// algorithm
		{
			this.add(new JLabel("algorithm: "));
			algorithmCombo = new JComboBox(HistogramComputerOption.values());
			algorithmCombo.setSelectedItem(state.getAlgorithm());
			algorithmCombo.addActionListener(this.new AlgorithmActionListener());
			this.add(algorithmCombo);
		}
		
		// compute button
		{
			this.add(new JLabel());
			computeButton = new JButton("compute histogram");
			computeButton.addActionListener(this.new ComputeActionListener());
			this.add(computeButton);
		}
	}
		
			
	protected void openHistogramFile() {
        try {
        	Desktop desktop = null;
            // Before more Desktop API is used, first check 
            // whether the API is supported by this particular 
            // virtual machine (VM) on this particular host.
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                
                String path = state.getHistogramFilename();
            	File file = new File(path);
            	LOGGER.fine("opening histogram file...");
            	desktop.open(file);
            }    	    
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	// Listener classes
	private class OriginalFilenameActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			state.setOriginalFilename(originalFilenameField.getText());
		}

	}
	
	private class HistogramFilenameActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			state.setHistogramFilename(histogramFilenameField.getText());
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
			state.setAlgorithm((HistogramComputerOption) algorithmCombo.getSelectedItem());
		}
		
	}
	
	private class ComputeActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (originalFilenameField.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "The path to original file is empty!");
			}
			else if (histogramFilenameField.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Please, fill the path to histogram file.");
			}
			else {
				LOGGER.fine("computing histogram...");
				
				executor.execute();
				openHistogramFile();
			}
		}
	}
}
