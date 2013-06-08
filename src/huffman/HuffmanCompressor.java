package huffman;

import huffman.HuffmanTree.Code;
import huffman.HuffmanTree.InternalNode;
import huffman.HuffmanTree.LeafNode;
import huffman.HuffmanTree.Node;
import huffman.pfile.FileSplit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Logger;

public class HuffmanCompressor {
	static final Logger LOGGER = Constants.LOGGER;
	static final int CHUNK_SIZE = (int) FileSplit.DEFAULT_CHUNK_SIZE;
	HuffmanTree tree;
	Code[] codes;
	
	public HuffmanCompressor(long[] histogram) {
		tree = new HuffmanTree(histogram);
		codes = tree.computeCodes();
	}

	class OutputWriter {
		public OutputWriter(OutputStream output) {
			this.output = output;
			this.outputBuffer = new byte[CHUNK_SIZE];
			this.offset = 0;
		}
		
		OutputStream output;
		byte[] outputBuffer;
		int offset;
		
		public int getOffset() {
			return offset;
		}
		
		void writeCode(Code code) throws IOException {
			for (int i = 0; i < code.size; ++i) {
				writeBit(offset, code.bitset.get(i));
				++offset;
				if (offset >= 8 * code.size) {
					output.write(outputBuffer);
					Arrays.fill(outputBuffer, (byte) 0);
					offset = 0;
				}
			}
		}

		private void writeBit(int at, boolean b) {
			if (b) {
				outputBuffer[at / 8] |= 1 << (at % 8);
			}
		}
		
		private void finalWrite() throws IOException {
			output.write(outputBuffer, 0, (offset + 7) / 8);
			// write the offset bits in the last byte
			output.write(offset % 8);
		}
	}
	
	public void encode(File original, File encoded) throws IOException {
		InputStream input = new FileInputStream(original);
		OutputStream output = new FileOutputStream(encoded);
		
		OutputWriter writer = new OutputWriter(output);
		
		byte[] inputBuffer = new byte[CHUNK_SIZE];
		
		int readBytes;
		while ((readBytes = input.read(inputBuffer)) > 0) {
			for (int i = 0; i < readBytes; ++i) {
				Code code = codes[inputBuffer[i] & 0xff];
				writer.writeCode(code);
			}
		}
		writer.finalWrite();
		input.close();
		output.close();
	}
	
	class InputReader {
		RandomAccessFile input;
		byte[] buffer;
		int readBytes;
		int offset;
		long length;
		int stride;
		
		public InputReader(RandomAccessFile input) throws IOException {
			this.input = input;
			length = input.length();
			input.seek(length - 1);
			stride = input.read();
			this.buffer = new byte[CHUNK_SIZE];
			input.seek(0);
			this.readBytes = input.read(buffer);
		}
		
		public int getBit() throws IOException {
			int result = (buffer[offset / 8] >> (offset % 8)) & 1;
			++offset;
			if (offset >= 8 * readBytes) {
				readBytes = input.read(buffer);
				offset = 0;
			}
			return result;
		}
		
		/**
		 * The actual length, in bits
		 * @return
		 */
		public long getActualSize() {
			return (length - 1) * 8 + stride;
		}
	}
	
	public void decode(File encoded, File original) throws IOException {
		RandomAccessFile input = new RandomAccessFile(encoded, "r");
		OutputStream output = new FileOutputStream(original);
		
		InputReader reader = new InputReader(input);
		long actualSize = reader.getActualSize();
		
		Node current = tree.root;
		for (long i = 0; i < actualSize; ++i) {
			int t = reader.getBit();
			if (t == 1) {
				current = ((InternalNode) current).left;
			} else {
				current = ((InternalNode) current).right;
			}
			if (current instanceof LeafNode) {
				LeafNode leaf = (LeafNode) current;
				output.write((byte) leaf.which);
				current = tree.root;
			}
		}
		input.close();
		output.close();
	}
}
