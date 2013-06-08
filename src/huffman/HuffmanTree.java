package huffman;

import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.logging.Logger;

/**
 * Huffman tree
 *
 */
public class HuffmanTree {
	static final Logger LOGGER = Constants.LOGGER;
	
	long[] histogram;
	Node root;
	
	public HuffmanTree(long[] histogram) {
		this.histogram = histogram;
		computeTree();
	}
	
	private void computeTree() {
		LOGGER.fine("computing Huffman tree out of a histogram...");

		PriorityQueue<Node> pq = new PriorityQueue<Node>();
		for (int i = 0; i < Constants.NUM_CHARS; ++i) {
			if (histogram[i] > 0) {
				pq.add(new LeafNode(i, histogram[i]));
			}
		}

		Node parent = null;
		while (pq.size() > 1) {
			Node left = pq.poll();
			Node right = pq.poll();
			parent = new InternalNode(left, right);
			pq.add(parent);
		}
		root = parent;
		LOGGER.fine("Huffman tree computed");
	}
	
	public Code[] computeCodes() {
		Code[] codes = new Code[Constants.NUM_CHARS];
		BitSet code = new BitSet();
		root.computeCodes(codes, 0, code);
		return codes;
	}
	
	/**
	 * Code for a byte
	 * @author krassi
	 *
	 */
	public static class Code {
		public Code(BitSet code, int size) {
			this.bitset = code;
			this.size = size;
		}

		BitSet bitset;
		int size;

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < size; ++i) {
				builder.append(bitset.get(i) ? '1' : '0');
			}
			return builder.toString();
		}
	}
	
	/**
	 * Tree node for the Huffman tree
	 */
	public static abstract class Node implements Comparable<Node> {
		long frequency;

		public Node(long frequency) {
			this.frequency = frequency;
		}

		@Override
		public int compareTo(Node o) {
			return Long.compare(this.frequency, o.frequency);
		}

		/**
		 * Traverses the Huffman tree and populates the sizes.
		 * 
		 * @param codes
		 * @param size
		 * @param code
		 */
		abstract void computeCodes(Code[] codes, int size, BitSet code);
	}
	
	/**
	 * Leaf tree node for the Huffman tree
	 */
	public static class LeafNode extends Node {
		int which;

		public LeafNode(int which, long frequency) {
			super(frequency);
			this.which = which;
		}

		@Override
		void computeCodes(Code[] codes, int size, BitSet code) {
			BitSet bs = (BitSet) code.clone();
			codes[which] = new Code(bs, size);
		}
	}
	
	/**
	 * Internal tree node for the Huffman tree
	 */
	public static class InternalNode extends Node {
		public InternalNode(Node left, Node right) {
			super(left.frequency + right.frequency);
			this.left = left;
			this.right = right;
		}

		Node left, right;

		@Override
		void computeCodes(Code[] codes, int size, BitSet code) {
			code.set(size, false);
			left.computeCodes(codes, size + 1, code);
			code.set(size, true);
			right.computeCodes(codes, size + 1, code);
		}
	}
}
