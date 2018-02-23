package cs2321;

import java.util.Comparator;
import java.util.Iterator;

import net.datastructures.Entry;
import net.datastructures.SortedMap;


public class BinarySearchTree<K,V> extends LinkedBinaryTree<Entry<K,V>> implements SortedMap<K,V> {
	
	private Comparator<K> c;
	protected Node last_inserted_node;
	protected Node parent_of_last_removed_node;

	/* 
	 * default constructor
	 */
	public BinarySearchTree() {
		c = new DefaultComparator<K>();
	}
	
	/* 
	 * @param comparator - on construction, set comparator
	 */
	public BinarySearchTree(Comparator<K> comparator) {
		c = comparator;
	}
	
	/**
	 * Recursively searches for the location of the given key
	 * using a binary search. Stops if the desired key is found (in
	 * which case the node is returned), or no node exists where the
	 * key should be (in which case a new node is created with a null
	 * element). 
	 * @param key - the key to search for
	 * @param n - the node to check
	 * @return the node where the key is, or a new node if there is no node
	 * in the position where the key should be. 
	 */
	@TimeComplexity("O(n)")
	private Node binarySearch(K key, Node n)
	{
		/* TCJ
		 * At worst case, the tree will be structured in
		 * a linear chain, in which case searches will run in
		 * O(n). However, if the tree is well balanced, the 
		 * expected run time will be closer to O(lg n). 
		 */
		if(isEmpty())
		{
			return (Node) super.addRoot(null);
		}
		Node child;
		if(c.compare(key, n.getElement().getKey()) == 0)
		{
			return n;
		}
		if(c.compare(key, n.getElement().getKey()) < 0)
		{
			child = (Node) super.left(n);
			if(child == null)
			{
				return (Node) super.insertLeft(n, null);
			}
		}
		else
		{
			child = (Node) super.right(n);
			if(child == null)
			{
				return (Node) super.insertRight(n, null);
			}
		}
//		if(isExternal(child))
//		{
//			return child;
//		}
		return binarySearch(key, child);
	}
	
	/**
	 * Returns the number of entries in the map.
	 * @return number of entries in the map
	 */
	@TimeComplexity("O(1)")
	@Override
	public int size(){
		return super.size();
	}
	
	/**
	 * Returns the value associated with the specified key, or null if no such entry exists.
	 * @param key  the key whose associated value is to be returned
	 * @return the associated value, or null if no such entry exists
	 */
	@Override
	@TimeComplexity("O(n)")
	public V get(K key) {
		/* TCJ
		 * The binary search algorithm runs in O(n), although
		 * if the tree is balanced (like in AVL implementations), 
		 * it will run in O(lg n). 
		 */
		Node n = binarySearch(key, root);
		if(n.getElement() == null)
		{
			//the given key wasn't actually in the tree
			super.remove(n);
			return null;
		}
		return n.getElement().getValue();
	}

	/**
	 * Associates the given value with the given key. If an entry with
	 * the key was already in the map, this replaced the previous value
	 * with the new one and returns the old value. Otherwise, a new
	 * entry is added and null is returned.
	 * @param key    key with which the specified value is to be associated
	 * @param value  value to be associated with the specified key
	 * @return the previous value associated with the key (or null, if no such entry)
	 */
	@Override
	@TimeComplexity("O(n)")
	public V put(K key, V value) {
		/* TCJ
		 * The binary search algorithm runs in O(n), although
		 * if the tree is balanced (like in AVL implementations), 
		 * it will run in O(lg n). 
		 */
		Item i = new Item(key, value);
		Node n = binarySearch(key, root);
		V oldV = (n.getElement() == null) ? null : n.getElement().getValue();
		n.setElement(i);
		last_inserted_node = n;
		return oldV;
	}

	/**
	 * Removes the entry with the specified key, if present, and returns
	 * its associated value. Otherwise does nothing and returns null.
	 * @param key  the key whose entry is to be removed from the map
	 * @return the previous value associated with the removed key, or null if no such entry exists
	 */
	@Override
	@TimeComplexity("O(n)")
	public V remove(K key) {
		/* TCJ
		 * The binary search algorithm runs in O(n), although
		 * if the tree is balanced (like in AVL implementations), 
		 * it will run in O(lg n). 
		 */
		Node n = binarySearch(key, root);
		if(n.getElement() == null) 
		{
			// the given key wasn't actually in the tree
			super.remove(n);
			return null;
		}
		V old = n.getElement().getValue();
		try
		{
			super.remove(n);
		}catch(IllegalArgumentException e)
		{
			remove(n);
		}
		return old;
	}
	
	/**
	 * Helper method that does some magic but doesn't actually
	 * remove any nodes by itself. 
	 * @param v - the node to remove 
	 * @return the value associated with the removed node
	 */
	private V remove(Node v)
	{
		V old = v.getElement().getValue();
		if(v.getLeft() == null)
		{
			super.insertLeft(v, null);
			removeExternalAndParent(v.getLeft());
		}
		else if(v.getRight() == null)
		{
			super.insertLeft(v, null);
			removeExternalAndParent(v.getRight());
		}
		else
		{
			Node w = v.getRight();
			Node q = v;
			while(w != null)
			{
				q = w;
				w = w.getLeft();
			}
			v.setElement(q.getElement());
			w = (Node) super.insertLeft(q, null);
			removeExternalAndParent(w);
		}
		return old;
	}
	
	/**
	 * A helper method that actually removes the things. 
	 * @param v - the external node to remove
	 */
	private void removeExternalAndParent(Node v)
	{
		super.remove(v);
		super.remove(v.getParent());
		parent_of_last_removed_node = v.getParent().getParent();
	}

	/**
	 * Returns an iterable collection of the keys contained in the map.
	 *
	 * @return iterable collection of the map's keys
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<K> keySet() {
		/* TCJ
		 * This method establishes its inner iterator by
		 * making a call to entrySet(), which runs in O(n). 
		 */
		return () -> {
				return new Iterator<K>()
						{
							Iterator<Entry<K, V>> it = entrySet().iterator();
							@Override
							public boolean hasNext() {
								return it.hasNext();
							}

							@Override
							public K next() {
								return it.next().getKey();
							}
						};
			};
	}

	/**
	 * Returns an iterable collection of the values contained in the map.
	 * Note that the same value will be given multiple times in the result
	 * if it is associated with multiple keys.
	 *
	 * @return iterable collection of the map's values
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<V> values() {
		/* TCJ
		 * This method establishes its inner iterator by
		 * making a call to entrySet(), which runs in O(n). 
		 */
		return () -> {
			return new Iterator<V>()
					{
						Iterator<Entry<K, V>> it = entrySet().iterator();
						@Override
						public boolean hasNext() {
							return it.hasNext();
						}

						@Override
						public V next() {
							return it.next().getValue();
						}
					};
		};
	}

	/**
	 * Returns an iterable collection of all key-value entries of the map.
	 *
	 * @return iterable collection of the map's entries
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<Entry<K, V>> entrySet() {
		/* TCJ
		 * This method calls inOrderSubtree(), which runs in O(n). 
		 */
		ArrayList<Entry<K, V>> snapshot = new ArrayList<Entry<K, V>>();
		if(!isEmpty())
		{
			inOrderSubtree(root, snapshot, null, null);
		}
		return snapshot;
	}
	
	/**
	 * Recursively builds an arraylist storing entries on the tree
	 * in key order. Can opt to only store keys that are within a 
	 * certain range. 
	 * @param n - the node in the current recursive step
	 * @param snapshot - the arraylist to store nodes in
	 * @param start - the min key to store. if null, there will be no lower bound.
	 * @param stop - the max key to store. if null, there will be no upper bound. 
	 */
	@TimeComplexity("O(n)")
	private void inOrderSubtree(Node n, ArrayList<Entry<K, V>> snapshot, K start, K stop)
	{
		/* TCJ
		 * This method will iterate through every node
		 * in the tree. 
		 */
		if(n.getLeft() != null)
		{
			inOrderSubtree(n.getLeft(), snapshot, start, stop);
		}
		if((stop != null && c.compare(n.getElement().getKey(), stop) >= 0))
		{
			return;
		}
		if(start == null || c.compare(n.getElement().getKey(), start) >= 0)
		{
			snapshot.addLast(n.getElement());
		}	
		if(n.getRight() != null)
		{
			inOrderSubtree(n.getRight(), snapshot, start, stop);
		}
	}

	/**
	 * Returns the entry having the least key (or null if map is empty).
	 * @return entry with least key (or null if map is empty)
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> firstEntry() {
		/* TCJ
		 * At worst case, the tree will be structured in
		 * a linear chain, in which case searches will run in
		 * O(n). However, if the tree is well balanced, the 
		 * expected run time will be closer to O(lg n). 
		 */
		if(!isEmpty())
		{
			Node n = (Node) root();
			while(super.left(n) != null)
			{
				n = (Node) super.left(n);
			}
			return n.getElement();
		}
		return null;
	}

	/**
	 * Returns the entry having the greatest key (or null if map is empty).
	 * @return entry with greatest key (or null if map is empty)
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> lastEntry() {
		/* TCJ
		 * At worst case, the tree will be structured in
		 * a linear chain, in which case searches will run in
		 * O(n). However, if the tree is well balanced, the 
		 * expected run time will be closer to O(lg n). 
		 */
		if(!isEmpty())
		{
			Node n = (Node) root();
			while(super.right(n) != null)
			{
				n = (Node) super.right(n);
			}
			return n.getElement();
		}
		return null;
	}
	
	/**
	 * Finds the least node with key greater than the 
	 * given node. 
	 * @param n - the node to start from
	 * @return the least node with key greater than the given node
	 */
	@TimeComplexity("O(n)")
	private Node successor(Node n)
	{
		/* TCJ
		 * At worst case, the tree will be structured in
		 * a linear chain, in which case searches will run in
		 * O(n). However, if the tree is well balanced, the 
		 * expected run time will be closer to O(lg n). 
		 */
		if(n.getRight() != null)
		{
			Node w = n.getRight();
			while(w.getLeft() != null)
			{
				w = w.getLeft();
			}
			return w;
		}
		else if(n.getParent() != null)
		{
			Node q = n;
			Node w;
			do
			{
				w = q.getParent();
				if(q.equals(w.getLeft()))
				{
					return w;
				}
				q = w;
			}while(q.getParent() != null);
		}
		return null;
	}
	
	/**
	 * Finds the greatest node with key less than the 
	 * given node. 
	 * @param n - the node to start from
	 * @return the greatest node with key less than the given node
	 */
	@TimeComplexity("O(n)")
	private Node predecessor(Node n)
	{
		/* TCJ
		 * At worst case, the tree will be structured in
		 * a linear chain, in which case searches will run in
		 * O(n). However, if the tree is well balanced, the 
		 * expected run time will be closer to O(lg n). 
		 */
		if(n.getLeft() != null)
		{
			Node w = n.getLeft();
			while(w.getRight() != null)
			{
				w = w.getRight();
			}
			return w;
		}
		else if(n.getParent() != null)
		{
			Node q = n;
			Node w;
			do
			{
				w = q.getParent();
				if(q.equals(w.getRight()))
				{
					return w;
				}
				q = w;
			}while(q.getParent() != null);
		}
		return null;
	}

	/**
	 * Returns the entry with least key greater than or equal to given key
	 * (or null if no such key exists).
	 * @return entry with least key greater than or equal to given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
		/* TCJ
		 * The binary search algorithm runs in O(n), although
		 * if the tree is balanced (like in AVL implementations), 
		 * it will run in O(lg n). 
		 */
		Node n = binarySearch(key, root);
		if(n.getElement() != null)
		{
			if(c.compare(n.getElement().getKey(), key) == 0)
			{
				return n.getElement();
			}
			else
			{
				return successor(n).getElement();
			}
		}
		Node w = successor(n);
		super.remove(n);
		return (w == null) ? null : w.getElement();
	}

	/**
	 * Returns the entry with greatest key less than or equal to given key
	 * (or null if no such key exists).
	 * @return entry with greatest key less than or equal to given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
		/* TCJ
		 * The binary search algorithm runs in O(n), although
		 * if the tree is balanced (like in AVL implementations), 
		 * it will run in O(lg n). 
		 */
		Node n = binarySearch(key, root);
		if(n.getElement() != null)
		{
			if(c.compare(n.getElement().getKey(), key) == 0)
			{
				return n.getElement();
			}
			else
			{
				return predecessor(n).getElement();
			}
		}
		Node w = predecessor(n);
		super.remove(n);
		return (w == null) ? null : w.getElement();
	}

	/**
	 * Returns the entry with greatest key strictly less than given key
	 * (or null if no such key exists).
	 * @return entry with greatest key strictly less than given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
		/* TCJ
		 * The binary search algorithm runs in O(n), although
		 * if the tree is balanced (like in AVL implementations), 
		 * it will run in O(lg n). 
		 */
		Node n = binarySearch(key, root);
		if(n.getElement() != null)
		{
			return predecessor(n).getElement();
		}
		Node w = predecessor(n);
		super.remove(n);
		return (w == null) ? null : w.getElement();
	}

	/**
	 * Returns the entry with least key strictly greater than given key
	 * (or null if no such key exists).
	 * @return entry with least key strictly greater than given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
		/* TCJ
		 * The binary search algorithm runs in O(n), although
		 * if the tree is balanced (like in AVL implementations), 
		 * it will run in O(lg n). 
		 */
		Node n = binarySearch(key, root);
		if(n.getElement() != null)
		{
			return successor(n).getElement();
		}
		Node w = successor(n);
		super.remove(n);
		return (w == null) ? null : w.getElement();
	}

	/**
	 * Returns an iterable containing all keys in the range from
	 * <code>fromKey</code> inclusive to <code>toKey</code> exclusive.
	 * @return iterable with keys in desired range
	 * @throws IllegalArgumentException if <code>fromKey</code> or <code>toKey</code> is not compatible with the map
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<Entry<K, V>> subMap(K fromKey, K toKey)
			throws IllegalArgumentException {
		/* TCJ
		 * This method makes a call to inOrderSubtree(), a recursive
		 * method that runs in O(n). 
		 */
		ArrayList<Entry<K, V>> snapshot = new ArrayList<Entry<K, V>>();
		if(!isEmpty())
		{
			inOrderSubtree(root, snapshot, fromKey, toKey);
		}
		return snapshot;
	}
	
	protected class Item implements Entry<K, V>
	{
		private K key;
		private V value;
		
		public Item(K k, V v)
		{
			key = k;
			value = v;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
	}
}
