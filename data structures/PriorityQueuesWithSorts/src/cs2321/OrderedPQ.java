package cs2321;

import java.util.Comparator;

import net.datastructures.*;
/**
 * A PriorityQueue based on an ordered Doubly Linked List. 
 * 
 * Course: CS2321 Section ALL
 * Assignment: #3
 * @author
 */

public class OrderedPQ<K,V> implements PriorityQueue<K,V> {

	private DoublyLinkedList<Entry<K, V>> list = new DoublyLinkedList<Entry<K, V>>();
	private Comparator<K> c;
	
	public static void main(String[] args)
	{
		OrderedPQ<Integer, String> pq = new OrderedPQ<Integer, String>();
		System.out.println(pq.isEmpty());
		pq.insert(0, "s");
		System.out.println(pq.size());
	}
	
	/**
	 * Creates an OrderedPQ that uses the default comparator. 
	 */
	@TimeComplexity("O(1)")
	public OrderedPQ() {
		c = new DefaultComparator<K>();
	}
	
	/**
	 * Creates an OrderedPQ that uses the given comparator. 
	 * @param c - the comparator to be used by this OrderedPQ
	 */
	@TimeComplexity("O(1)")
	public OrderedPQ(Comparator<K> c) {
		this.c = c;
	}
	
	/**
	 * Returns the size of this PQ
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
	 * Creates an entry for the given key and value and inserts it 
	 * into the appropriate position in the PQ. 
	 * @param key - the key to be stored
	 * @param value - the value to be stored
	 * @return the entry representing the given key and value
	 */
	@Override
	@TimeComplexity("O(n)")
	public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
		/* TCJ
		 * Each operation in this method runs in constant time, 
		 * except for a while loop that searches for the correct spot
		 * for the given entry, which at most must search the whole 
		 * PQ, giving a time complexity of O(n). 
		 */
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
		if(list.isEmpty())
		{
			list.addFirst(entry);
		}
		else
		{
			Position<Entry<K, V>> p = list.last();
			while(p.getElement() != null && c.compare(entry.getKey(), p.getElement().getKey()) < 0)
			{
				p = list.before(p);
			}
			if(p.getElement() == null)
			{
				list.addFirst(entry);
			}
			else 
			{
				list.addAfter(p, entry);
			}
		}
		return entry;
	}

	/**
	 * Returns the minimum entry stored by this PQ. 
	 * @return the minimum entry stored by this PQ, or null if 
	 * this PQ is empty. 
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> min() {
		return (list.isEmpty()) ? null : list.first().getElement();
	}

	/**
	 * Removes and returns the minimum entry stored by this PQ.
	 * @return the minimum entry stored by this PQ, or null if 
	 * this PQ is empty. 
	 */
	@Override
	@TimeComplexity("O(1)")
	public Entry<K, V> removeMin() {
		return (list.isEmpty()) ? null : list.removeFirst();
	}
}
