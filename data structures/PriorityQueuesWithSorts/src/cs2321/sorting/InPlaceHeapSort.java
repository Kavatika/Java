package cs2321.sorting;

import cs2321.TimeComplexity;

public class InPlaceHeapSort<K extends Comparable<K>> implements Sorter<K> {
	
	private int size;

	/**
	 * sort - Perform an in-place heap sort
	 * @param array - Array to sort
	 */
	@TimeComplexity("O(n lg n)")
	public void sort(K[] array) {
		/* TCJ
		 * This algorithm calls buildMaxHeap(), which has 
		 * a time cost of O(n lg n), and it also calls 
		 * maxHeapify() n times, giving it an effective time cost
		 * of 2n lg n, or n lg n.
		 */
		buildMaxHeap(array);
		for(int i = array.length - 1; i >= 1; i--)
		{
			K k = array[i];
			array[i] = array[0];
			array[0] = k;
			size--;
			maxHeapify(array, 0);
		}
	}
	
	/**
	 * Sorts the max heap. 
	 * @param a
	 * @param index
	 */
	@TimeComplexity("O(lg n)")
	private void maxHeapify(K[] a, int index)
	{
		/* TCJ
		 * This method functions much like the downheap method
		 * in the heap implementation, apart from the fact that
		 * it puts the larger values at the top of the heap. 
		 * Thus, it has time of n lg n. 
		 */
		int largest = index;
		
		int left = index * 2 + 1;
		int right = index * 2 + 2;
		
		if(left < size && a[left].compareTo(a[largest]) > 0)
		{
			largest = left;
		}
		if(right < size && a[right].compareTo(a[largest]) > 0)
		{
			largest = right;
		}
		if(largest != index)
		{
			K k = a[index];
			a[index] = a[largest];
			a[largest] = k;
			maxHeapify(a, largest);
		}
		return;
	}
	
	/**
	 * Creates a max heap. 
	 * @param a
	 */
	@TimeComplexity("O(n lg n)")
	private void buildMaxHeap(K[] a)
	{
		/* TCJ
		 * This method calls maxHeapify() n/2 times.
		 * Since maxHeapify() has a time cost of O(lg n), 
		 * this algorithm has a cost of O(n lg n).
		 */
		size = a.length;
		for(int i = (a.length/2) - 1; i >= 0; i--)
		{
			maxHeapify(a, i);
		}
	}
	
	public static void main(String[] args)
	{
		InPlaceHeapSort<Integer> s = new InPlaceHeapSort<Integer>();
		Integer[] i = new Integer[]{5, 3, 9, 7, 1, 8, 0, 2, 4, 6};
		s.sort(i);
		for(Integer a : i)
		{
			System.out.println(a);
		}
	}
}
