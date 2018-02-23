package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A Adaptable PriorityQueue based on an heap. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author
 */

public class HeapPQ<K,V> implements AdaptablePriorityQueue<K,V> {
	
	private ArrayList<Item> heap = new ArrayList<Item>();
	private Comparator<K> c;
	
	/**
	 * Creates an instance of HeapPQ with the default comparator. 
	 */
	@TimeComplexity("O(1)")
	public HeapPQ() {
		c = new DefaultComparator<K>();
	}
	
	/**
	 * Creates an instance of HeapPQ that uses the given comparator. 
	 * @param c - the comparator to use
	 */
	@TimeComplexity("O(1)")
	public HeapPQ(Comparator<K> c) {
		this.c = c;
	}
	
	
	
	/**
	 * The entry at index j was made smaller and needs to be 
	 * bubbled up to its appropriate position. 
	 * @param int move the entry at index j higher if necessary, to restore the heap property
	 */
	@TimeComplexity("O(lg n)")
	private void upheap(int j){
		/* TCJ
		 * The 'pointer' for this algorithm ascends through each level
		 * of the heap, which has a height of log(n). At worst case, 
		 * an entry must be moved from the bottom of the tree to the top,
		 * going through log(n) loop iterations.
		 */
		while(j > 0)
		{
			int p = parent(j);
			if(c.compare(heap.get(j).getKey(), heap.get(p).getKey()) >= 0) break;
			swap(j, p);
			j = p;
		}
	}
	
	/**
	 * The entry at index j was made larger and needs to be 
	 * bubbled down to its appropriate position. 
	 * @param int move the entry at index j lower if necessary, to restore the heap property
	 */
	@TimeComplexity("O(lg n)")
	private void downheap(int j){
		/* TCJ
		 * The 'pointer' for this algorithm descends through each level
		 * of the heap, which has a height of log(n). At worst case, 
		 * an entry must be moved from the top of the tree to the bottom,
		 * going through log(n) loop iterations. 
		 */
		while(hasLeft(j))
		{
			int left = left(j);
			int small = left;
			if(hasRight(j))
			{
				int right = right(j);
				if(c.compare(heap.get(left).getKey(), heap.get(right).getKey()) > 0)
				{
					small = right;
				}
			}
			if(c.compare(heap.get(small).getKey(), heap.get(j).getKey()) >= 0)
				break;
			swap(j, small);
			j = small;
		}
	}

	/**
	 * Returns the current size of this heap.
	 * @return the size of the heap
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
		return heap.size();
	}

	/**
	 * Returns true if this heap is empty.
	 * @return true if this heap is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		return heap.isEmpty();
	}

	/**
	 * Creates an entry representing the given key and value and 
	 * inserts it into the heap. 
	 * @param key - the key for the entry
	 * @param value - the value of the entry
	 * @return the entry representing the given key and value
	 */
	@Override
	@TimeComplexity("O(lg n)")
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		/* TCJ
		 * Each operation runs in constant time, except for
		 * the call to upheap, which runs in log(n) time.  
		 */
		Item i = new Item(key, value);
		i.location = heap.size();
		heap.addLast(i);
		upheap(i.location);
		return i;
	}

	/**
	 * Returns the entry with the smallest key. 
	 * @return the minimum entry
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> min() {
		Entry<K, V> min = null;
		if(!isEmpty())
		{
			min = heap.get(0);
		}
		return min;
	}

	/**
	 * Returns and removes the entry with the smallest key.
	 * @return the minimum entry
	 */
	@Override
	@TimeComplexity("O(lg n)")
	public Entry<K, V> removeMin() {
		/* TCJ
		 * Each operation in this method runs in constant
		 * time except for the call to remove, which runs in
		 * log(n).
		 */
		Entry<K, V> min = min();
		if(min != null)
		{
			remove(min);
		}
		return min;
	}

	/**
	 * Removes the given entry from the heap. 
	 * @param entry - the entry to remove from the heap
	 */
	@Override
	@TimeComplexity("O(lg n)")
	public void remove(Entry<K, V> entry) throws IllegalArgumentException {
		/* TCJ
		 * Each operation in this method runs in constant time
		 * except for the call to downheap, which runs in log(n). 
		 */
		if(isValid(entry))
		{
			int i = ((Item) entry).location;
			swap(i, size() - 1);
			heap.removeLast();
			((Item) entry).invalidate();
			downheap(i);
		}
	}

	/**
	 * Changes the key for the given entry and repositions that
	 * entry as necessary. 
	 * @param entry - the entry to be changed
	 * @param key - the new key for the entry
	 */
	@Override
	@TimeComplexity("O(lg n)")
	public void replaceKey(Entry<K, V> entry, K key) throws IllegalArgumentException {
		/* TCJ
		 * Every operation in this method runs in constant
		 * time except for a guaranteed call to either upheap
		 * or downheap, which both run in log(n). 
		 */
		if(isValid(entry))
		{
			Item i = (Item) entry;
			K old = entry.getKey();
			i.setKey(key);
			if(c.compare(old, key) < 0)
			{
				downheap(i.location);
			}
			else
			{
				upheap(i.location);
			}
		}
		else throw new IllegalArgumentException();
	}

	/**
	 * Changes the value stored by the given entry.
	 * @param entry - the entry to be changed
	 * @param value - the new value for the entry
	 */
	@Override
	@TimeComplexity("O(1)")
	public void replaceValue(Entry<K, V> entry, V value) throws IllegalArgumentException {
		if(isValid(entry))
		{
			((Item)entry).setValue(value);
		}
		else throw new IllegalArgumentException();
	}
	
	/**
	 * Returns the parent index of the given index.
	 * @param i - the given index
	 * @return the parent index
	 */
	@TimeComplexity("O(1)")
	protected int parent(int i)
	{
		return (i - 1) / 2;
	}
	
	/**
	 * Returns the left child index of the given index.
	 * @param i - the given index
	 * @return the left child's index
	 */
	@TimeComplexity("O(1)")
	protected int left(int i)
	{
		return i * 2 + 1;
	}
	
	/**
	 * Returns the right child index of the given index. 
	 * @param i - the given index
	 * @return the right child's index
	 */
	@TimeComplexity("O(1)")
	protected int right(int i)
	{
		return i * 2 + 2;
	}
	
	/**
	 * Returns whether or not the given index has a left child. 
	 * @param i - the index to test
	 * @return true if the given index has a left child
	 */
	@TimeComplexity("O(1)")
	protected boolean hasLeft(int i)
	{
		return left(i) < size();
	}
	
	/**
	 * Returns whether or not the given index has a right child.
	 * @param i - the index to test
	 * @return true if the given index has a right child
	 */
	@TimeComplexity("O(1)")
	protected boolean hasRight(int i)
	{
		return right(i) < size();
	}
	
	/**
	 * Swaps the entries stored at the two given locations. 
	 * @param i - the first entry
	 * @param j - the second entry
	 */
	@TimeComplexity("O(1)")
	protected void swap(int i, int j)
	{
		Item temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
		heap.get(i).location = i;
		heap.get(j).location = j;
	}
	
	/**
	 * Returns whether or not the given entry is valid for this heap. 
	 * @param e - the entry to test
	 * @return true if the entry is valid
	 */
	@TimeComplexity("O(1)")
	protected boolean isValid(Entry<K, V> e)
	{
		if(new Item(null, null).getClass().isInstance(e))
		{
			Item i = (Item) e;
			return i.isValid() && this.hashCode() == i.parentHashCode();
		}
		else return false;
	}
	
	/**
	 * An inner class instance of the Entry interface. 
	 */
	private class Item implements Entry<K, V>
	{	
		/**
		 * The key and value stored by this Item.  
		 */
		private K key;
		private V value;
		
		/**
		 * The index where this Item is stored in the heap. Directly
		 * accessible by its parent class. Allows for location-aware 
		 * data storage. 
		 */
		protected int location = -1;
		
		/**
		 * valid and parentHashCode are used for verifying the validity 
		 * of any given Item. Each implementation of AdaptablePriorityQueue
		 * methods tests these two values to see if a given Item has been 
		 * removed and if it was created by this heap. 
		 */
		private boolean valid = true;
		private int parentHashCode = HeapPQ.this.hashCode();
		
		
		/**
		 * Creates an instance of Item with the given key and value. 
		 * @param key - the key to be stored
		 * @param value - the value to be stored
		 */
		@TimeComplexity("O(1)")
		protected Item(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Returns the key stored by this Item. 
		 * @return the key stored by this Item
		 */
		@Override
		@TimeComplexity("O(1)")
		public K getKey() {
			return key;
		}
		
		/**
		 * Returns the value stored by this Item. 
		 * @return the value stored by this Item
		 */
		@Override
		@TimeComplexity("O(1)")
		public V getValue() {
			return value;
		}
		
		/**
		 * Returns the validity of this Item. 
		 * @return true if this item is valid
		 */
		@TimeComplexity("O(1)")
		protected boolean isValid()
		{
			return valid;
		}
		
		/**
		 * Returns the hash code of the HeapPQ that created this Item. 
		 * @return the hash code the creating HeapPQ
		 */
		@TimeComplexity("O(1)")
		protected int parentHashCode()
		{
			return parentHashCode;
		}
		
		/**
		 * Changes the key stored by this Item. 
		 * @param key - the new key to be stored
		 */
		@TimeComplexity("O(1)")
		protected void setKey(K key)
		{
			this.key = key;
		}
		
		/**
		 * Changes the value stored by this Item.
		 * @param value - the new value to be stored
		 */
		@TimeComplexity("O(1)")
		protected void setValue(V value)
		{
			this.value = value;
		}
		
		/**
		 * Invalidates this Item. Called when an Item is removed
		 * from its HeapPQ. Irreversible. 
		 */
		@TimeComplexity("O(1)")
		protected void invalidate()
		{
			valid = false;
			location = -1;
		}
	}
}
