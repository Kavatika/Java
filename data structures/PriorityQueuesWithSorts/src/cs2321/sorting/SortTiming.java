package cs2321.sorting;

/**
 * A test driver for Sorts.
 * 
 * Course: CS2321 Section ALL
 * Assignment: #4
 * @author Chris Brown (cdbrown@mtu.edu)
 */

public class SortTiming {

	public static void main(String [] args){
		
		//#Examples of using testSort
		Integer[] a = new Integer[100000];
		java.util.Random  r = new java.util.Random();
		
		for(int i = 0; i < a.length; i++)
		{
			a[i] = r.nextInt(100000);
		}
		double time = testSort(a, new QuickSort<Integer>()) / 1000000;
		System.out.println("QuickSort\t" + time);
		
		time = testSort(a, new MergeSort<Integer>()) / 1000000;
		System.out.println("MergeSort\t" + time);
		
		time = testSort(a, new InsertionSort<Integer>()) / 1000000;
		System.out.println("InsertionSort\t" + time);
		
		time = testSort(a, new HeapSort<Integer>()) / 1000000;
		System.out.println("HeapSort\t" + time);
		
		time = testSort(a, new SelectionSort<Integer>()) / 1000000;
		System.out.println("SelectionSort\t" + time);
		
		time = testSort(a, new InPlaceInsertionSort<Integer>()) / 1000000;
		System.out.println("InPlaceInsertionSort\t" + time);
		
		time = testSort(a, new InPlaceSelectionSort<Integer>()) / 1000000;
		System.out.println("InPlaceSelectionSort\t" + time);
		
		time = testSort(a, new InPlaceHeapSort<Integer>()) / 1000000;
		System.out.println("InPlaceheapSort\t" + time);
	}

	/**
	 * Algorithm: testSort
	 * @param arr - an array of Integers to use for empirical measurement of a sort
	 * @param sortClass - the Class representing the sorting algorithm to be run
	 * @param iterations - the number of times the sort is repeated
	 * @return average time taken for a single execution of a sort (in nanoseconds)
	 * 
	 * A copy (clone) of the array is made to test over, so that the original may be reused.
	 */
	public static double testSort(Integer[] arr, Sorter<Integer> sortClass){
		long startTime = 0, endTime = 0;
		int samples = 0;

		System.gc();
		startTime = System.nanoTime();
		//#repeated measurements (no less than .5 seconds worth of repeats)
		do{
			//create a copy of the array for each test case
			Integer[] testCase = arr.clone();
			//the sorting algorithm, based on the Sorter Class
			sortClass.sort(testCase);
			
			samples++;
			endTime = System.nanoTime();
		}while(endTime - startTime < 500000000);
				
		return (double)(endTime - startTime) / samples;
	}
	
}
