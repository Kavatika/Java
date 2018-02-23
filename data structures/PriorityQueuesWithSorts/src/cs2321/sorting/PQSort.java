package cs2321.sorting;

import cs2321.TimeComplexity;
import net.datastructures.*;

/**
 * A class that performs three forms of PQ Sort:
 *   SelectionSort (Unordered PQ)
 *   InsertionSort (Ordered PQ)
 *   HeapSort (Heap PQ)
 *
 * @author CS2321 Instructor
 * @param <K>
 */
public abstract class PQSort<K extends Comparable<K>>{
	
	/*
	 * Simple MinPQSort - relies on a functional minimum PQ and 
	 * a Sequence's addLast, first, last, and next operations are used.
	 */
	@TimeComplexity("O(n^2)")
	protected void sort(K[] kArray, PriorityQueue<K,K> pq) {
		/*
		 * The unsortedPQ and sortedPQ both have a time cost 
		 * of O(n^2), due to their insert/removeMin methods which
		 * have a time cost of O(n), respectively, being called 
		 * for each element in the array. However, the HeapPQ 
		 * sort's insert() and removeMin() methods both have a
		 * time cost of O(lg n), and both are called for each
		 * element in the array, so for HeapPQ implementations, 
		 * this method has a time cost of O(n lg n). 
		 */
		for(K k : kArray)
		{
			pq.insert(k, k);
		}
		for(int i = 0; i < kArray.length; i++)
		{
			kArray[i] = pq.removeMin().getKey();
		}
	}
	
	public static void main(String[] args)
	{
		InsertionSort<Integer> s = new InsertionSort<Integer>();
		Integer[] i = new Integer[]{5, 3, 9, 7, 1, 8, 0, 2, 4, 6};
		s.sort(i);
		for(Integer a : i)
		{
			System.out.println(a);
		}
	}
}
