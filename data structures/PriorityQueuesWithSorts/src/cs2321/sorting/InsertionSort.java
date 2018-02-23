package cs2321.sorting;

import cs2321.OrderedPQ;

public class InsertionSort<K extends Comparable<K>> extends PQSort<K> implements Sorter<K> {

	/**
	 * sort - Perform an PQ sort using an OrderedPQ
	 * @param array - Array to sort
	 */
	public void sort(K[] array){
		super.sort(array, new OrderedPQ<K,K>());
	}
}
