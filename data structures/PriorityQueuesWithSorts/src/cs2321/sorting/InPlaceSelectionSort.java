package cs2321.sorting;

import cs2321.TimeComplexity;

public class InPlaceSelectionSort<K extends Comparable<K>> implements Sorter<K> {

	/**
	 * sort - Perform an in-place selection sort
	 * @param array - Array to sort
	 */
	@TimeComplexity("O(n^2)")
	public void sort(K[] array) {
		for(int i = 0; i < array.length - 1; i++)
		{
			int min = i;
			for(int j = i+1; j < array.length; j++)
			{
				if(array[j].compareTo(array[min]) < 0)
				{
					min = j;
				}
			}
			if(min != i)
			{
				K k = array[min];
				array[min] = array[i];
				array[i] = k;
			}
		}
	}

	public static void main(String[] args)
	{
		InPlaceSelectionSort<Integer> s = new InPlaceSelectionSort<Integer>();
		Integer[] i = new Integer[]{5, 3, 9, 7, 1, 8, 0, 2, 4, 6};
		s.sort(i);
		for(Integer a : i)
		{
			System.out.println(a);
		}
	}
}
