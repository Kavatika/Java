package cs2321.sorting;

import cs2321.TimeComplexity;

public class QuickSort<E extends Comparable<E>> implements Sorter<E> {

	/**
	 * Sorts the given array using QuickSort. 
	 * @param array - the array to sort. 
	 */
	@TimeComplexity("O(n^2)")
	public void sort(E[] array) {
		quicksort(array, 0, array.length - 1);
	}
	
	/**
	 * Partitions the given array based on a pivot
	 * and then recursively sorts the two partitions. 
	 * @param a - the array to sort
	 * @param p - the beginning index
	 * @param r - the index of the pivot 
	 */
	@TimeComplexity("O(n^2)")
	private void quicksort(E[] a, int p, int r)
	{
		/* TCJ
		 * At worst case, each pivot will be at the extreme
		 * ends of the data ranges for each partition. This would
		 * result in the total cost for each recursive call being
		 * n + (n-1) + ... + 2 + 1. However, there is a 50% chance 
		 * any given pivot being 'good' (or towards the middle of 
		 * the data range), so as proven in class, the expected
		 * time complexity would actually be closer to O(n lg n). 
		 */
		if(p < r)
		{
			int q = partition(a, p, r);
			quicksort(a, p, q - 1);
			quicksort(a, q + 1, r);
		}
	}
	
	/**
	 * 
	 * @param a - 
	 * @param p - the index of the beginning of the area to partition
	 * @param r - the index of the pivot
	 * @return
	 */
	@TimeComplexity("O(n)")
	private int partition(E[] a, int p, int r)
	{
		/* TCJ
		 * This method compares every element in the given
		 * range to the pivot. The given range has a size 
		 * of n. 
		 */
		E x = a[r];
		int i = p - 1;
		for(int j = p; j < r; j++)
		{
			if(a[j].compareTo(x) <= 0)
			{
				swap(a, i+1, j);
				i++;
			}
		}
		swap(a, i + 1, r);
		return i+1;
	}
	
	@TimeComplexity("O(1)")
	private void swap(E[] a, int i, int j)
	{
		E e = a[i];
		a[i] = a[j];
		a[j] = e;
	}
	
	public static void main(String[] args)
	{
		QuickSort<Integer> q = new QuickSort<Integer>();
		Integer[] i = new Integer[]{5, 3, 9, 7, 1, 8, 0, 2, 4, 6};
		q.sort(i);
		for(Integer a : i)
		{
			System.out.println(a);
		}
	}
}
