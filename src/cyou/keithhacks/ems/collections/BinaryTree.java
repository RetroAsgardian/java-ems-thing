package cyou.keithhacks.ems.collections;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * A generic implementation of a binary tree.
 * @author Keith
 *
 * @param <K> The data type of the keys.
 * @param <V> The data type of the values.
 */
public class BinaryTree<K extends Comparable<K>, V> {
	
	protected static enum TraversalType {
		PREORDER,
		INORDER,
		POSTORDER
	}
	
	protected Node root;
	
	protected class Node {
		public K key;
		public V value;
		
		public Node childA;
		public Node childB;
		
		public Node(K key, V value) {
			this.key = key;
			this.value = value;
			this.childA = null;
			this.childB = null;
		}
	}
	
	public BinaryTree() {
		root = null;
	}
	
	/**
	 * Map the given value to the specified key, overwriting any previous value.
	 * @param key The key to map.
	 * @param value The value to map.
	 * @return The previous value of the key, or null if there was no previous value.
	 */
	public V put(K key, V value) {
		if (root == null) {
			root = new Node(key, value);
			return null;
		}
		
		return put(root, new Node(key, value));
	}
	protected V put(Node tree, Node leaf) {
		if (leaf.key.equals(tree.key)) {
			V old = tree.value;
			tree.value = leaf.value;
			
			if (leaf.childA != null)
				put(tree, leaf.childA);
			if (leaf.childB != null)
				put(tree, leaf.childB);
			
			return old;
		}
		
		if (leaf.key.compareTo(tree.key) < 0) {
			if (tree.childA != null)
				return put(tree.childA, leaf);
			tree.childA = leaf;
			return null;
		} else {
			if (tree.childB != null)
				return put(tree.childB, leaf);
			tree.childB = leaf;
			return null;
		}
	}
	
	/**
	 * Get the value mapped to the given key.
	 * @param key The key of the mapping to get.
	 * @return The value, or null if there is no mapping for the given key.
	 */
	public V get(K key) {
		if (root == null)
			return null;
		
		Node node = getNode(root, key);
		if (node == null)
			return null;
		
		return node.value;
	}
	protected Node getNode(Node tree, K key) {
		if (key.equals(tree.key))
			return tree;
		
		if (key.compareTo(tree.key) < 0) {
			if (tree.childA != null)
				return getNode(tree.childA, key);
			return null;
		} else {
			if (tree.childB != null)
				return getNode(tree.childB, key);
			return null;
		}
	}
	
	protected Node getParent(Node tree, K key) {
		if (key.equals(tree.key))
			return null;
		
		if (key.compareTo(tree.key) < 0) {
			if (tree.childA == null)
				return null;
			
			if (key.equals(tree.childA.key))
				return tree;
			
			return getParent(tree.childA, key);
		} else {
			if (tree.childB == null)
				return null;
			
			if (key.equals(tree.childB.key))
				return tree;
			
			return getParent(tree.childB, key);
		}
	}
	
	/**
	 * Remove the value mapped to the given key.
	 * @param key The key of the mapping to remove.
	 * @return The value that was removed, or null if there was no mapping.
	 */
	public V remove(K key) {
		if (root == null)
			return null;
		
		Node oldNode = getNode(root, key);
		if (oldNode == null)
			return null;
		
		Node childA = oldNode.childA;
		Node childB = oldNode.childB;
		
		Node parent = getParent(root, key);
		if (parent == null) {
			if (!root.key.equals(key)) {
				System.err.println("remove(): getParent() returned null but root key does not match");
				return null;
			}
			// we want to remove the root
			if (childA == null && childB == null)
				root = null;
			else if (childA == null)
				root = childB;
			else {
				root = childA;
				if (childB != null)
					put(root, childB);
			}
		} else {
			if (parent.childA.key.equals(key)) {
				// we want to remove parent.childA
				if (childA == null && childB == null)
					parent.childA = null;
				else if (childA == null)
					parent.childA = childB;
				else {
					parent.childA = childA;
					if (childB != null)
						put(root, childB);
				}
			} else if (parent.childB.key.equals(key)) {
				// we want to remove parent.childB
				if (childA == null && childB == null)
					parent.childB = null;
				else if (childA == null)
					parent.childB = childB;
				else {
					parent.childB = childA;
					if (childB != null)
						put(root, childB);
				}
			} else {
				System.err.println("remove(): getParent() returned a node but it is not the parent");
				return null;
			}
		}
		
		return oldNode.value;
	}
	
	protected void traverse(Node tree, BiConsumer<K, V> forEach, TraversalType type) {
		if (type == TraversalType.PREORDER)
			forEach.accept(tree.key, tree.value);
		
		if (tree.childA != null)
			traverse(tree.childA, forEach, type);
		
		if (type == TraversalType.INORDER)
			forEach.accept(tree.key, tree.value);
		
		if (tree.childB != null)
			traverse(tree.childB, forEach, type);
		
		if (type == TraversalType.POSTORDER)
			forEach.accept(tree.key, tree.value);
	}
	
	public void preorder(BiConsumer<K, V> forEach) {
		if (root == null)
			return;
		
		traverse(root, forEach, TraversalType.PREORDER);
	}
	
	public void inorder(BiConsumer<K, V> forEach) {
		if (root == null)
			return;
		
		traverse(root, forEach, TraversalType.INORDER);
	}
	
	public void postorder(BiConsumer<K, V> forEach) {
		if (root == null)
			return;
		
		traverse(root, forEach, TraversalType.POSTORDER);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("{");
		inorder((k, v) -> {
			builder.append(k.toString());
			builder.append("=");
			builder.append(v.toString());
			builder.append(", ");
		});
		builder.append("}");
		
		return builder.toString();
	}
	
	public int size() {
		// Java won't let this just be an int, so we need to use a mutable integer class
		AtomicInteger count = new AtomicInteger(0);
		
		// Use the inorder() traversal function to count every item
		inorder((k, v) -> {
			count.incrementAndGet();
		});
		
		return count.intValue();
	}
	
}
