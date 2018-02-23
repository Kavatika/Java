package cs2321;

import java.util.Comparator;
import java.util.Iterator;

import net.datastructures.Position;

public class HashMultiMap <K, V> {

	private HashMap<K, DoublyLinkedList<V>> map;

	public HashMultiMap(int hashsize, Comparator<K> c) {
		map = new HashMap<K, DoublyLinkedList<V>>(hashsize, c);
	}
	
	public HashMultiMap() {
		map = new HashMap<K, DoublyLinkedList<V>>();
	}
	
	/**
	 * Returns the number of entries in the map.
	 * @return number of entries in the map
	 */
	@TimeComplexity("O(n)")
	public int size() {
		int size = 0;
		for(DoublyLinkedList<V> d : map.values())
		{
			size += d.size();
		}
		return size;
	}

	/**
	 * Tests whether the map is empty.
	 * @return true if the map is empty, false otherwise
	 */
	@TimeComplexity("O(n)")
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns a collection of all values associated with key k in the map.
	 * @param key - the key to search for
	 * @return a collection of values associated with the given key. if no 
	 * values are associated with that key, returns an empty collection.
	 */
	@TimeComplexity("O(n)")
	public Iterable<V> get(K key) {
		/* TCJ
		 * This method calls the HashMap's get function, 
		 * which has a time cost of O(n). However, provided
		 * minimal collision occurs, this should be effectively
		 * constant. 
		 */
		DoublyLinkedList<V> d = map.get(key);
		return (d == null) ? new DoublyLinkedList<V>() : d;
	}

	
	/**
	 * Adds a new entry to the multimap associating key k with value v, 
	 * without overwriting any existing mappings for key k.
	 * @param key - the key to insert
	 * @param value - the value to associate with the key
	 */
	@TimeComplexity("O(n)")
	public void put(K key, V value) {
		/* TCJ
		 * This method calls the HashMap's get and
		 * put function, which both have a tme cost 
		 * of O(n). However, provided minimal collision 
		 * occurs, this should be effectively constant. 
		 */
		DoublyLinkedList<V> d = map.get(key);
		if(d == null)
		{
			d = new DoublyLinkedList<V>();
			map.put(key, d);
		}
		d.addLast(value);
	}


	/**
	 * Removes an entry mapping key k to value v from the multimap. 
	 * @param key - the key associated with the value
	 * @param value - the value to remove
	 * @return true if the value exists for the given key, false otherwise
	 */
	@TimeComplexity("O(n)")
	public boolean remove(K key, V value) {
		/* TCJ
		 * This method calls the HashMap's get 
		 * function and iterates through the resulting
		 * DoublyLinkedList. The HashMap's get 
		 * function has an expected time cost of O(1),
		 * but it still must iterate through each
		 * value in the DoublyLinkedList.
		 */
		DoublyLinkedList<V> d = map.get(key);
		if(d != null)
		{
			for(Position<V> p : d.positions())
			{
				if(p.getElement().equals(value))
				{
					d.remove(p);
					if(d.isEmpty()) map.remove(key);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Removes all entries having key equal to k from the multimap. 
	 * @param key - the key to remove from the multimap
	 * @return a collection of all values associated with the given key
	 */
	@TimeComplexity("O(n)")
	public Iterable<V> removeAll(K key) {
		/* TCJ
		 * This method calls the HashMap's remove function,
		 * which has an expected time cost of O(1).
		 */
		DoublyLinkedList<V> d = map.remove(key);
		return (d == null) ? new DoublyLinkedList<V>() : d;
	}

	/**
	 * Returns a non-duplicative collection of keys in the multimap. 
	 * return an empty list if there is no key. Don't return null. 
	 */
	public Iterable<K> keySet() {
		return map.keySet();
	}

	/**
	 * Returns a collection of values for all entries in the multimap 
	 * @return a collection of values for all entires in the multimap
	 */
	public Iterable<V> values() {
		return new Iterable<V>()
				{
					@Override
					public Iterator<V> iterator() {
						return new Iterator<V>()
								{
									private Iterator<DoublyLinkedList<V>> lists = map.values().iterator();
									private Iterator<V> values;
									
									@Override
									public boolean hasNext() {
										return lists.hasNext() || values.hasNext();
									}

									@Override
									public V next() {
										if(values == null || !values.hasNext())
										{
											values = lists.next().iterator();
										}
										return values.next();
									}
								};
					}
				};
	}
	

}
