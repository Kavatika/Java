package cs2321;

import net.datastructures.*;
import java.util.Comparator;
import java.util.Iterator;

public class LookupTable<K, V> implements SortedMap<K, V> {
	
	private ArrayList<Item> list = new ArrayList<Item>();
	private Comparator<K> c;
	
	public static void main(String[] args)
	{
		LookupTable<Integer, Integer> lt = new LookupTable<Integer, Integer>();
		lt.put(0, 0);
		lt.put(1, 1);
		lt.put(3, 3);
		lt.put(6, 6);
		lt.put(8, 8);
		lt.put(5, 5);
		for(Entry<Integer, Integer> e : lt.subMap(-1, 6))
		{
			System.out.println(e.getKey());
		}
	}
	
	/**
	 * Creates a LookupTable with the default comparator.
	 */
	public LookupTable(){
		c = new DefaultComparator<K>();
	}
	
	/** 
	 * Creates a LookupTable with the given comparator
	 * @param comparator - on construction, set comparator
	 */
	public LookupTable(Comparator<K> comparator) {
		c = comparator;
	}
	
	/**
	 * Sets the comparator to be used by this LookupTable
	 * @param comparator - the comparator to set
	 */
	public void setComparator(Comparator<K> comparator) {
		c = comparator;
	}

	/**
	 * Returns the number of entries in the map.
	 * @return number of entries in the map
	 */
	@Override
	public int size() {
		return list.size();
	}

	/**
	 * Tests whether the map is empty.
	 * @return true if the map is empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/**
	 * Returns the value associated with the specified key, or null if no such entry exists.
	 * @param key  the key whose associated value is to be returned
	 * @return the associated value, or null if no such entry exists
	 */
	@Override
	public V get(K key) {
		int location = binarySearch(key);
		return (c.compare(list.get(location).getKey(), key) == 0) ? list.get(location).getValue() : null;
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
	public V put(K key, V value) {
		int location = binarySearch(key);
		if(!isEmpty() && location < list.size())
		{
			Item item = list.get(location);
			if(c.compare(item.getKey(), key) == 0)
			{
				return item.setValue(value);
			}
		}
		list.add(location, new Item(key, value));
		return null;
	}

	/**
	 * Removes the entry with the specified key, if present, and returns
	 * its associated value. Otherwise does nothing and returns null.
	 * @param key  the key whose entry is to be removed from the map
	 * @return the previous value associated with the removed key, or null if no such entry exists
	 */
	@Override
	public V remove(K key) {
		int location = binarySearch(key);
		Item item = list.get(location);
		if(c.compare(item.getKey(), key) == 0)
		{
			V value = item.getValue();
			list.remove(location);
			return value;
		}
		return null;
	}

	/**
	 * Returns an iterable collection of the keys contained in the map.
	 *
	 * @return iterable collection of the map's keys
	 */
	@Override
	public Iterable<K> keySet() {
		return new Iterable<K>()
				{

					@Override
					public Iterator<K> iterator() {
						return new Iterator<K>()
								{
									private int index = 0;
									@Override
									public boolean hasNext() {
										return index < list.size();
									}

									@Override
									public K next() {
										K key = list.get(index).getKey();
										index++;
										return key;
									}
								};
					}
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
	public Iterable<V> values() {
		return new Iterable<V>()
		{
			@Override
			public Iterator<V> iterator() {
				return new Iterator<V>()
						{
							private int index = 0;
							@Override
							public boolean hasNext() {
								return index < list.size();
							}

							@Override
							public V next() {
								V value = list.get(index).getValue();
								index++;
								return value;
							}
						};
			}
		};
	}

	/**
	 * Returns an iterable collection of all key-value entries of the map.
	 *
	 * @return iterable collection of the map's entries
	 */
	@Override
	public Iterable<Entry<K, V>> entrySet() {
		return new Iterable<Entry<K, V>>()
		{
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new Iterator<Entry<K, V>>()
						{
							private int index = 0;
							@Override
							public boolean hasNext() {
								return index < list.size();
							}

							@Override
							public Entry<K, V> next() {
								Entry<K, V> entry = list.get(index);
								index++;
								return entry;
							}
						};
			}
		};
	}

	/**
	 * Returns the entry having the least key (or null if map is empty).
	 * @return entry with least key (or null if map is empty)
	 */
	@Override
	public Entry<K, V> firstEntry() {
		return (isEmpty()) ? null : list.get(0);
	}

	/**
	 * Returns the entry having the greatest key (or null if map is empty).
	 * @return entry with greatest key (or null if map is empty)
	 */
	@Override
	public Entry<K, V> lastEntry() {
		return (isEmpty()) ? null : list.get(size() - 1);
	}

	/**
	 * Returns the entry with least key greater than or equal to given key
	 * (or null if no such key exists).
	 * @return entry with least key greater than or equal to given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> ceilingEntry(K key)  {
		int location = binarySearch(key);
		if(location < size())
		{
			return list.get(location);
		}
		return null;
	}

	/**
	 * Returns the entry with greatest key less than or equal to given key
	 * (or null if no such key exists).
	 * @return entry with greatest key less than or equal to given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> floorEntry(K key)  {
		int location = binarySearch(key);
		if(location > 0)
		{
			return (c.compare(list.get(location).getKey(), key) == 0) ? list.get(location) : list.get(location - 1);
		}
		return null;
	}

	/**
	 * Returns the entry with greatest key strictly less than given key
	 * (or null if no such key exists).
	 * @return entry with greatest key strictly less than given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> lowerEntry(K key) {
		int location = binarySearch(key);
		if(location > 0)
		{
			return list.get(location - 1);
		}
		return null;
	}

	/**
	 * Returns the entry with least key strictly greater than given key
	 * (or null if no such key exists).
	 * @return entry with least key strictly greater than given (or null if no such entry)
	 * @throws IllegalArgumentException if the key is not compatible with the map
	 */
	@Override
	public Entry<K, V> higherEntry(K key) {
		int location = binarySearch(key);
		if(location < size())
		{
			return (c.compare(list.get(location).getKey(), key) == 0) ? list.get(location + 1) : list.get(location);
		}
		return null;
	}

	/**
	 * Returns an iterable containing all keys in the range from
	 * <code>fromKey</code> inclusive to <code>toKey</code> exclusive.
	 * @return iterable with keys in desired range
	 * @throws IllegalArgumentException if <code>fromKey</code> or <code>toKey</code> is not compatible with the map
	 */
	@Override
	public Iterable<Entry<K, V>> subMap(K fromKey, K toKey){
		return new Iterable<Entry<K, V>>()
		{
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new Iterator<Entry<K, V>>()
						{
							private int index = binarySearch(fromKey);
							private int limit = binarySearch(toKey);
							
							@Override
							public boolean hasNext() {
								return index < limit;
							}

							@Override
							public Entry<K, V> next() {
								Entry<K, V> entry = list.get(index);
								index++;
								return entry;
							}
						};
			}
		};
	}
	
	/**
	 * Finds the index of the given key or where it
	 * should be located based on the concept of
	 * a binary search. 
	 * @param key - the key to search for
	 * @return the index where the key should be located
	 */
	protected int binarySearch(K key)
	{
		int low = 0;
		int high = list.size() - 1;
		while(low <= high)
		{
			int mid = (low+high)/2;
			K k = list.get(mid).getKey();
			if(c.compare(k, key) == 0)
			{
				return mid;
			}
			if(c.compare(k, key) < 0)
			{
				low = mid + 1;
			}
			else
			{
				high = mid - 1;
			}
		}
		return low;
	}

	protected class Item implements Entry<K, V>
	{
		private K key;
		private V value;
		
		protected Item(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
		
		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}
		
		protected V setValue(V newValue)
		{
			V oldValue = value;
			value = newValue;
			return oldValue;
		}
	}
}
