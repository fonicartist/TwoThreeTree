import java.util.ArrayList;
import java.util.Collections;

/**
 * Manages data nodes in a Two-Three Tree Structure
 * @author Victor La
 * @version 4/21/17 
 */
public class TwoThreeTree {
	// The only instance the class has is the reference to the root of the tree.
	private Node root;

	/**
	 * Constructs a new TwoThreeTree with an empty root
	 */
	public TwoThreeTree() {
		root = new Node();
	}

	/**
	 * Insert a value into the tree after searching for the 
	 * appropriate node to put it in.
	 * @param value The value to be inserted into the tree.
	 * @return True if the value was inserted, false if it 
	 * 		   already exists in the tree.
	 */
	public boolean insert(int value) {
		Node currNode = root.search(value);
		if (!currNode.keys.contains(value)) {
			currNode.insert(value);
			return true;
		}
		return false;
	}

	/**
	 * Returns the contents of the node where the searched 
	 * value would presumably reside.
	 * @param value The value being searched for.
	 * @return A String representation of the found node's content.
	 */
	public String search(int value) {
		String output = "";
		Node currNode = root.search(value);
		for (int i = 0; i < currNode.size(); i++) {
			if (i > 0) output += " ";
			output += currNode.keys.get(i);
		}
		return output;
	}
	
	// Private class to hold information in the tree.
	private class Node implements Comparable<Node>{

		private final int MAX = 2;	// Maximum number of keys a node can have;

		private Node parent;
		private ArrayList<Integer> keys;
		private ArrayList<Node> children;

		/**
		 * Constructor to create the root of the tree. 
		 * Only used once during initialization.
		 */
		public Node() {
			parent = null;
			keys = new ArrayList<>();
			children = new ArrayList<>();
		}

		/**
		 * Constructor to create a new child node containing 
		 * a passed value.
		 * @param key The value to add to the keys list in the node.
		 */
		public Node(int key) {
			parent = null;
			keys = new ArrayList<>();
			keys.add(key);
			children = new ArrayList<>();
		}

		/**
		 * Searches and returns a Node in which the passed 
		 * value is in or should be in.
		 * @param key The value to search for.
		 * @return A node containing the key or a leaf Node 
		 * 		   where the key would've been inserted.
		 */
		public Node search(int key) {
			if (isLeaf()) return this;	// Returns a leaf node. It's the end of the road, nowhere left to search.
			
			// Check if key value is in the current node
			for (int i = 0; i < size(); i++) {
				int check = keys.get(i);
				if (check == key)
					return this;
				else if (key < check)
					return children.get(i).search(key);
			}
			return children.get(children.size() - 1).search(key);
		}

		/**
		 * Makes a reference to the node the given value should be inserted 
		 * into then calls the function to add the key to the keys list.
		 * @param value The value being searched for.
		 */
		public void insert(int value) {
			Node currNode = search(value);
			currNode.add(value);
		}

		/**
		 * Adds the passed value to the keys list and checks to see if the 
		 * amount of keys have gone over maximum capacity.
		 * @param value
		 */
		public void add(int value) {
			keys.add(value);
			Collections.sort(keys);
			if (size() > MAX)
				split();
		}

		/**
		 * Splits the node into two new nodes, adds and updates necessary 
		 * children and parent nodes, then inserts the middle value into 
		 * the parent where another split may occur if the parent is full.
		 */
		public void split() {
			Node parentNode;
			Node left = new Node(keys.get(0));
			Node right = new Node(keys.get(2));
			int key = keys.get(1);
			
			// If node is not a leaf, it will have 4 children.
			// These children will be reassigned to the new nodes.
			if (!isLeaf()) {
				for (int i = 0; i < 2; i++) {
					left.children.add(children.get(i));
					children.get(i).parent = left;
					right.children.add(children.get(i + 2));
					children.get(i + 2).parent = right;
				}
			}
			
			// If the node is the root, it will be cleared to make it "new" again.
			if (parent == null) {
				parentNode = this;
				children.clear();
				keys.clear();
			}
			else {
				parentNode = parent;
				parentNode.children.remove(this);
			}
			
			parentNode.children.add(left);
			left.parent = parentNode;
			parentNode.children.add(right);
			right.parent = parentNode;
			Collections.sort(parentNode.children);
			parentNode.add(key);
		}

		/**
		 * Gets the amount of keys currently in the node.
		 * @return Size of keys ArrayList.
		 */
		public int size() {
			return keys.size();
		}

		/**
		 * Checks if the node is a leaf (has no children).
		 * @return True if children ArrayList has no elements.
		 */
		public boolean isLeaf() {
			return children.size() == 0;
		}

		@Override
		/**
		 * Compare one node to another to determine their order.
		 * @return An integer value representing whether the other 
		 * 		   node should go before or after the current one.
		 */
		public int compareTo(Node other) {
			return this.keys.get(0).compareTo(other.keys.get(0));
		}

	}

}
