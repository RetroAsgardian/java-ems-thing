package cyou.keithhacks.ems.collections;

import java.util.ArrayList;

/**
 * An open-hashing hash table using a BinaryTree as the secondary data structure.
 * @author Keith
 *
 * @param <K> The key type.
 * @param <V> The value type.
 */
public class OpenBTreeHashTable<K extends Comparable<K>, V> {
	
	protected BinaryTree<K, V>[] buckets;
	
	@SuppressWarnings("unchecked")
	public OpenBTreeHashTable(int bucketCount) {
		buckets = new BinaryTree[bucketCount];
		
		for (int i = 0; i < buckets.length; i++) {
			buckets[i] = new BinaryTree<K, V>();
		}
	}
	
	protected BinaryTree<K, V> getBucket(K key) {
		return buckets[key.hashCode() % buckets.length];
	}
	
	/**
	 * Add a new key-value mapping.
	 * @param key The key to add a mapping for.
	 * @param value The value to map.
	 * @return The value previously mapped to the key, or null if none existed.
	 */
	public V put(K key, V value) {
		return getBucket(key).put(key, value);
	}
	
	/**
	 * Get the mapped value for a given key.
	 * @param key The key to search for.
	 * @return The value mapped to the key, or null if no mapping exists.
	 */
	public V get(K key) {
		return getBucket(key).get(key);
	}

	/**
	 * Remove the mapped value for a given key.
	 * @param key The key to remove the mapping for.
	 * @return The value previously mapped to the key, or null if none existed.
	 */
	public V remove(K key) {
		return getBucket(key).remove(key);
	}
	
	public int size() {
		int size = 0;
		
		for (BinaryTree<K, V> bucket : buckets)
			size += bucket.size();
		
		return size;
	}
	
	public String toString() {
		String result = "";
		
		for (BinaryTree<K, V> bucket : buckets)
			result = result + bucket.toString() + "\n";
		
		return result;
	}
	
	public ArrayList<K> keys() {
		ArrayList<K> result = new ArrayList<K>();
		
		for (BinaryTree<K, V> bucket : buckets) {
			bucket.inorder((k, v) -> {
				result.add(k);
			});
		}
		
		return result;
	}
}
