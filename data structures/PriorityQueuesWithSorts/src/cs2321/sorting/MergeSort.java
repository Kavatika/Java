package cs2321.sorting;

import cs2321.TimeComplexity;

public class MergeSort<E extends Comparable<E>> implements Sorter<E> {
	
	/**
	 * Performs a MergeSort on the given array. 
	 * @param array - the array to sort
	 */
	@TimeComplexity("O(n lg n)")
	public void sort(E[] array) {
		/* TCJ
		 * Each time this method is called, it splits
		 * the given array in half and calls the merge(),
		 * which has a time cost of O(n). Thus, it has a 
		 * time cost of O(n lg n).
		 */
		int n = array.length;
		if(n < 2)
		{
			return;
		}
		int mid = n/2;
		E[] a1 = copyOf(array, 0, mid);
		E[] a2 = copyOf(array, mid, n);
		
		sort(a1);
		sort(a2);
		merge(array, a1, a2);
	}
	
	/**
	 * Merges the two given arrays into one destination array.
	 * @param dest - the destination array
	 * @param a1 - the first array to merge
	 * @param a2 - the second array to merge
	 */
	@TimeComplexity("O(n)")
	private void merge(E[] dest, E[] a1, E[] a2)
	{
		/* TCJ
		 * This algorithm runs through each element in
		 * arrays a1 and a2 and inserts every element into
		 * a. Thus, its cost is the combined size of a1 and a2. 
		 */
		int i = 0, j = 0, k = 0;
		while(i < a1.length && j < a2.length)
		{
			if(a1[i].compareTo(a2[j]) < 0)
			{
				dest[k] = a1[i]; 
				i++;
				k++;
			}
			else
			{
				dest[k] = a2[j];
				j++;
				k++;
			}
		}
		if(i < a1.length)
		{
			for(;i < a1.length; i++, k++)
			{
				dest[k] = a1[i];
			}
		}
		if(j < a2.length)
		{
			for(;j < a2.length; j++, k++)
			{
				dest[k] = a2[j];
			}
		}
	}
	
	private E[] copyOf(E[] a, int from, int to)
	{
		return java.util.Arrays.copyOfRange(a, from, to);
	}
	
	public static void main(String[] args)
	{
		MergeSort<Integer> q = new MergeSort<Integer>();
		Integer[] i = new Integer[]{5, 3, 9, 7, 1, 8, 0, 2, 4, 6};
		q.sort(i);
		for(Integer a : i)
		{
			System.out.println(a);
		}
	}
}

