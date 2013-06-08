package huffman;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CompressionPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	static final Logger LOGGER = Logger.getLogger("huffman");
	
	ProgramState state;
	GUIExecutor executor;

	JTextField originalFilenameField;
	JTextField histogramFilenameField;
	JTextField encodedFilenameField;
	
	JSpinner numThreadsSpinner;
	JButton encodeButton;
	JButton decodeButton;
	
	public CompressionPanel(GUIExecutor executor, ProgramState state) {
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
		
		// encoded filename
		{
			this.add(new JLabel("encoded filename: "));
			encodedFilenameField = new JTextField(state.getEncodedFilename());
			encodedFilenameField.addActionListener(this.new EncodedFilenameActionListener());
			this.add(encodedFilenameField);
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
		
		// encode button
		{
			//this.add(new JLabel());
			encodeButton = new JButton("encode");
			encodeButton.addActionListener(this.new EncodeActionListener());
			this.add(encodeButton);
		}
		
		// decoded button
		{
			//this.add(new JLabel());
			decodeButton = new JButton("decode");
			decodeButton.addActionListener(this.new DecodeActionListener());
			this.add(decodeButton);
		}

	}
	
	private long[] readHistogram(String histogramFilename) {
		long[] histogram = new long[Constants.NUM_CHARS];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					histogramFilename));
			String line;
			int i = 0;
			while ((line = reader.readLine()) != null) {
				histogram[i] = Long.parseLong(line);
				++i;
			}
			reader.close();
		} catch (IOException e) {
			LOGGER.severe("cannot open histogram file");
		}
		
		return histogram;
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
	
	private class EncodedFilenameActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			state.setEncodedFilename(encodedFilenameField.getText());
		}

	}
	
	
	private class NumThreadsChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			state.setNumThreads((Integer) numThreadsSpinner.getValue());
		}
		
	}
	
	
	private class EncodeActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LOGGER.fine("encoding file...");
			long startTime = System.currentTimeMillis();
			long[] histogram = readHistogram(state.getHistogramFilename());
			HuffmanCompressor compressor = new HuffmanCompressor(histogram);
			
			File originalFile = new File(state.getOriginalFilename());
			File encodedFile = new File(state.getEncodedFilename());
			
			try{
				compressor.encode(originalFile, encodedFile);
			} catch (IOException ex1) {
				LOGGER.log(Level.SEVERE, "could not operate with original file or encoded file");
			}
			
			long estimatedTime = System.currentTimeMillis() - startTime;
			String mess = "Encoding complete! The elapsed time is " + estimatedTime + " mililiseconds.";
			JOptionPane.showMessageDialog(null, mess);
		}		
	}
	
	private class DecodeActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			LOGGER.fine("decoding file...");
			long startTime = System.currentTimeMillis();
			long[] histogram = readHistogram(state.getHistogramFilename());
			HuffmanCompressor compressor = new HuffmanCompressor(histogram);
			
			File encodedFile = new File(state.getEncodedFilename());
			File decodedFile = new File(state.getOriginalFilename());
			
			try{
				compressor.decode(encodedFile, decodedFile);
			} catch (IOException ex1) {
				LOGGER.log(Level.SEVERE, "could not operate with decoded file or encoded file");
			}
			
			long estimatedTime = System.currentTimeMillis() - startTime;
			String mess = "Decoding complete! The elapsed time is " + estimatedTime + " mililiseconds.";
			JOptionPane.showMessageDialog(null, mess);
		}
	
	}
}

