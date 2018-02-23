package cs2321.sorting;

import cs2321.TimeComplexity;

public class InPlaceInsertionSort<K extends Comparable<K>> implements Sorter<K> {

	/**
	 * sort - Perform an in-place insertion sort
	 * @param array - Array to sort
	 */
	@TimeComplexity("O(n^2)")
	public void sort(K[] array) {
		for(int i = 1; i < array.length; i++)
		{
			K x = array[i];
			int j = i - 1;
			while(j >= 0 && array[j].compareTo(x) > 0)
			{
				array[j+1] = array[j];
				j--;
			}
			array[j+1] = x;
		}
	}

	public static void main(String[] args)
	{
		InPlaceInsertionSort<Integer> q = new InPlaceInsertionSort<Integer>();
		Integer[] i = new Integer[]{5, 3, 9, 7, 1, 8, 0, 2, 4, 6};
		q.sort(i);
		for(Integer a : i)
		{
			System.out.println(a);
		}
	}
}
