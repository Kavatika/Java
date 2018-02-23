package cs2321.sorting;

import cs2321.UnorderedPQ;

public class SelectionSort<K extends Comparable<K>> extends PQSort<K> implements Sorter<K> {
	/**
	 * sort - Perform a PQ sort using an UnorderedPQ
	 * @param array - Array to sort
	 */
	public void sort(K[] array){
		super.sort(array, new UnorderedPQ<K,K>());
	}
}
