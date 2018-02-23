package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A PriorityQueue based on an Unordered Doubly Linked List. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author
 */

public class UnorderedPQ<K,V> implements PriorityQueue<K,V> {

	private DoublyLinkedList<Entry<K, V>> list = new DoublyLinkedList<Entry<K, V>>();
	private Comparator<K> c;
	
	public static void main(String[] args)
	{
		UnorderedPQ<Integer, String> pq = new UnorderedPQ<Integer, String>();
		System.out.println(pq.isEmpty());
		pq.insert(50, "A");
		System.out.println(pq.size());
		System.out.println(pq.min().getKey());
	}
	
	/**
	 * Creates an instance of UnorderedPQ using the default comparator. 
	 */
	@TimeComplexity("O(1)")
	public UnorderedPQ() {
		c = new DefaultComparator<K>();
	}
	
	/**
	 * Creates an instance of UnorderedPQ using the given comparator. 
	 * @param c - the comparator for this PQ to use
	 */
	@TimeComplexity("O(1)")
	public UnorderedPQ(Comparator<K> c) {
		this.c = c;
	}

	/**
	 * Returns the size of this PQ. 
	 * @return the size of this PQ
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
		return list.size();
	}

	/**
	 * Returns whether or not this PQ is empty. 
	 * @return true if this PQ is empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	/**
	 * A helper method that finds the position of the minimum entry. 
	 * @return the position of the minimum entry
	 */
	@TimeComplexity("O(n)")
	private Position<Entry<K, V>> findMin()
	{
		/* TCJ
		 * This operation must search every entry in the PQ 
		 * for the minimum and only requires constant time
		 * operations for each entry, giving it a time 
		 * complexity of O(n).
		 */
		Position<Entry<K, V>> small = list.first();
		for(Position<Entry<K, V>> p : list.positions())
		{
			if(c.compare(p.getElement().getKey(), small.getElement().getKey()) < 0)
			{
				small = p;
			}
		}
		return small;
	}

	/**
	 * Creates an entry for the given key and value and inserts it into
	 * this PQ. 
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		Entry<K, V> entry = new Entry<K, V>()
				{
					private K k = key;
					private V v = value;
					@Override
					public K getKey() {
						return k;
					}

					@Override
					public V getValue() {
						return v;
					}
				};
		list.addLast(entry);
		return entry;
	}

	/**
	 * Returns the minimum entry for this PQ.
	 * @return the minimum entry of this PQ
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> min() {
		/* TCJ
		 * Each operation is a simple operation, except for the 
		 * call to findMin, which runs in O(n). 
		 */
		if(isEmpty())
		{
			return null;
		}
		return findMin().getElement();
	}

	/**
	 * Removes and returns the minimum entry for this PQ.
	 * @return the minimum entry for this PQ
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> removeMin() {
		/* TCJ
		 * Each operation is a simple operation, except for the 
		 * call to findMin, which runs in O(n). 
		 */
		if(isEmpty())
		{
			return null;
		}
		return list.remove(findMin());
	}
}
