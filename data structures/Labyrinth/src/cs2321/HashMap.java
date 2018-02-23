package cs2321;

import java.util.Comparator;
import java.util.Iterator;

import net.datastructures.*;

public class HashMap<K, V> implements Map<K, V> {
	
	private UnorderedMap<K, V>[] map;
	private Comparator<K> c;
	private int size = 0;
	
	/**
	 * Constructor that takes a hash size
	 * @param hashsize The number of buckets to initialize in the HashMap
	 * @param comparator - on construction, set comparator
	 */
	public HashMap(int hashsize, Comparator<K> comparator){
		map = (UnorderedMap<K, V>[]) new UnorderedMap[hashsize];
		setComparator(comparator);
	}
	
	public HashMap(Comparator<K> comparator)
	{
		this(17, comparator);
	}

	/**
	 * Constructor that takes a hash size
	 * @param hashsize The number of buckets to initialize in the HashMap
	 */
	public HashMap(int hashsize) {
		this(hashsize, new DefaultComparator<K>());
	}
	
	/**
	 * Sets the comparator used by this HashMap
	 * @param comparator - the comparator to set
	 */
	@TimeComplexity("O(n)")
	public void setComparator(Comparator<K> comparator) {
		c = comparator;
		initialize();
	}
	
	public HashMap(){
		this(17);
	}
	
	/**
	 * Initializes the array of unordered maps.
	 */
	@TimeComplexity("O(n)")
	private void initialize()
	{
		/* TCJ
		 * This method must initialize n unordered maps.
		 */
		for(int i = 0; i < map.length; i++)
		{
			map[i] = new UnorderedMap<K, V>(c);
		}
	}

	/**
	 * Returns the number of entries in the map.
	 * @return number of entries in the map
	 */
	@Override
	@TimeComplexity("O(n)")
	public int size() {
		/* TCJ
		 * This method must sum the sizes of each
		 * unordered map stored.
		 */
		int size = 0;
		for(UnorderedMap<K, V> u : map)
		{
			size += u.size();
		}
		return size;
	}

	/**
	 * Tests whether the map is empty.
	 * @return true if the map is empty, false otherwise
	 */
	@Override
	@TimeComplexity("O(n)")
	public boolean isEmpty() {
		return size() == 0;
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
		 * This method must call the get method of one
		 * unordered map, which has a time cost of O(n), 
		 * however, provided minimal collision occurs, there
		 * should be so few entries in each unordered map that
		 * the time cost is effectively constant.
		 */
		return map[hash(key)].get(key);
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
		 * This method must call the put method of one
		 * unordered map, which has a time cost of O(n).
		 * However, provided minimal collision occurs, there
		 * should be so few entries in each unordered map that
		 * the time cost is effectively constant.
		 */
		return map[hash(key)].put(key, value);
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
		 * This method must call the remove method of one
		 * unordered map, which has a time cost of O(n).
		 * However, provided minimal collision occurs, there
		 * should be so few entries in each unordered map that
		 * the time cost is effectively constant.
		 */
		return map[hash(key)].remove(key);
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
									private Iterator<K> it = map[index].keySet().iterator();
									@Override
									public boolean hasNext() {
										if(!it.hasNext())
										{
											if(index >= map.length - 1) return false;
											for(int i = index + 1; i < map.length; i++)
											{
												if(!map[i].isEmpty()) return true;
											}
											return false;
										}
										return true;
									}

									@Override
									public K next() {
										if(!it.hasNext())
										{
											for(++index; index < map.length; index++)
											{
												it = map[index].keySet().iterator();
												if(it.hasNext()) break;
											}
										}
										return it.next();
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
							private Iterator<V> it = map[index].values().iterator();
							@Override
							public boolean hasNext() {
								if(!it.hasNext())
								{
									if(index >= map.length - 1) return false;
									for(int i = index + 1; i < map.length; i++)
									{
										if(!map[i].isEmpty()) return true;
									}
									return false;
								}
								return true;
							}

							@Override
							public V next() {
								if(!it.hasNext())
								{
									for(++index; index < map.length; index++)
									{
										it = map[index].values().iterator();
										if(it.hasNext()) break;
									}
								}
								return it.next();
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
							private Iterator<Entry<K, V>> it = map[index].entrySet().iterator();
							@Override
							public boolean hasNext() {
								if(!it.hasNext())
								{
									if(index >= map.length - 1) return false;
									for(int i = index + 1; i < map.length; i++)
									{
										if(!map[i].isEmpty()) return true;
									}
									return false;
								}
								return true;
							}

							@Override
							public Entry<K, V> next() {
								if(!it.hasNext())
								{
									for(++index; index < map.length; index++)
									{
										it = map[index].entrySet().iterator();
										if(it.hasNext()) break;
									}
								}
								return it.next();
							}
						};
			}
		};
	}

	/**
	 * A hash function. 
	 * @param key - the key to create a hash value for. 
	 * @return the hash value of the given key
	 */
	@TimeComplexity("O(1)")
	private int hash(K key)
	{
		return Math.abs((key.hashCode() * (map.length + 1) + 31) % map.length);
	}
}
