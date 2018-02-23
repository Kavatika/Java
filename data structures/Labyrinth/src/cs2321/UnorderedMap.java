package cs2321;

import java.util.Comparator;
import java.util.Iterator;

import net.datastructures.Entry;
import net.datastructures.Map;

public class UnorderedMap<K,V> implements Map<K,V>{
	
	private ArrayList<Item> list = new ArrayList<Item>();
	private Comparator<K> c;
	
	/**
	 * Creates an instance of UnorderedMap with a
	 * default comparator.
	 */
	public UnorderedMap() {
		c = new DefaultComparator<K>();
	}
	
	/**
	 * Creates an instance of UnorderedMap with the given 
	 * comparator.
	 * @param comparator - on construction, set comparator
	 */
	public UnorderedMap(Comparator<K> comparator) {
		c = comparator;
	}
	
	/**
	 * Changes the set comparator.
	 * @param comparator - the comparator to set
	 */
	@TimeComplexity("O(1)")
	public void setComparator(Comparator<K> comparator) {
		c = comparator;
	}
	
	/**
	 * Returns the size of this UnorderedMap.
	 * @return the size of this UnorderedMap
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
		return list.size();
	}

	/**
	 * Returns whether or not this UnorderedMap is empty.
	 * @return true if empty
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		return list.isEmpty();
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
		 * This method must iterate through every
		 * entry and perform simple operations. 
		 */
		for(Item i : list)
		{
			if(c.compare(key, i.getKey()) == 0)
			{
				return i.getValue();
			}
		}
		return null;
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
		 * This method must iterate through every entry
		 * in the map and perform simple operations. 
		 */
		for(Item i : list)
		{
			if(c.compare(key, i.getKey()) == 0)
			{
				return i.setValue(value);
			}
		}
		list.addLast(new Item(key, value));
		return null;
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
		 * This method must check n entries for the
		 * given key and perform a simple operation on
		 * the selected entry. 
		 */
		for(int i = 0; i < list.size(); i++)
		{
			Item item = list.get(i);
			if(c.compare(key, item.getKey()) == 0)
			{
				V value = item.getValue();
				list.remove(i);
				return value;
			}
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
	 * A inner class instance of the Entry interface.
	 */
	protected class Item implements Entry<K, V>
	{
		private K key;
		private V value;
		
		/**
		 * Creates an instance of the Item class using the given
		 * key and value. 
		 * @param key - the key of this item
		 * @param value - the value of this item
		 */
		protected Item(K key, V value)
		{
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Returns the key stored in this item.
		 * @return the key stored in this item
		 */
		@Override
		@TimeComplexity("O(1)")
		public K getKey() {
			return key;
		}

		/**
		 * Returns the value stored in this item.
		 * @return the value stored in this item
		 */
		@Override
		@TimeComplexity("O(1)")
		public V getValue() {
			return value;
		}
		
		/**
		 * Changes the value of this item and returns
		 * the old value.
		 * @param value - the new value of this item
		 * @return the old value
		 */
		@TimeComplexity("O(1)")
		protected V setValue(V value)
		{
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}
	}
}
