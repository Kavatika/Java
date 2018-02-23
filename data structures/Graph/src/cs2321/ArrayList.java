package cs2321;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.datastructures.List;

/**
 * 
 * @author overw
 *
 * @param <E>
 */
public class ArrayList<E> implements List<E> {
	
	private int increment;
	private int size = 0;
	private E[] array;
	
	/**
	 * Default constructor
	 */
	public ArrayList()
	{
		increment = 10;
		array = (E[]) new Object[increment];
	}
	
	/**
	 * Creates an ArrayList with the given initial capacity.
	 */
	public ArrayList(int capacity)
	{
		increment = capacity;
		array = (E[]) new Object[increment];
	}
	
	/**
	 * Returns the number of elements in the list.
	 * @return number of elements in the list
	 */
	@TimeComplexity("O(1)")
	@Override
	public int size() {
		/* TCJ
		 * this method returns one variable. it runs in constant time.
		 */
		return size;
	}
	
	/**
	 * Tests whether the list is empty.
	 * @return true if the list is empty, false otherwise
	 */
	@TimeComplexity("O(1)")
	@Override
	public boolean isEmpty() {
		/* TCJ
		 * this method makes one comparison and returns the result.
		 * it runs in constant time. 
		 */
		return size == 0;
	}

	/**
	 * Returns (but does not remove) the element at index i.
	 * @param  i   the index of the element to return
	 * @return the element at the specified index
	 * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
	 */
	@TimeComplexity("O(1)")
	@Override
	public E get(int i) throws IndexOutOfBoundsException {
		/* TCJ
		 * this method access one element of an array and 
		 * returns it. it runs in constant time.
		 */
		return array[i];
	}

	/**
	 * Replaces the element at the specified index, and returns the element previously stored.
	 * @param  i   the index of the element to replace
	 * @param  e   the new element to be stored
	 * @return the previously stored element
	 * @throws IndexOutOfBoundsException if the index is negative or greater than size()-1
	 */
	@TimeComplexity("O(1)")
	@Override
	public E set(int i, E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * every operation in this method has a constant cost.
		 * this method runs in constant time. 
		 */
		if(i < 0 || i > size - 1)
		{
			throw new IndexOutOfBoundsException();
		}
		E old = array[i];
		array[i] = e;
		return old;
	}

	/**
	 * Inserts the given element at the specified index of the list, shifting all
	 * subsequent elements in the list one position further to make room.
	 * @param  i   the index at which the new element should be stored
	 * @param  e   the new element to be stored
	 * @throws IndexOutOfBoundsException if the index is negative or greater than size()
	 */
	@TimeComplexity("O(n)")
	@Override
	public void add(int i, E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * at worst case, this method must increase the capacity
		 * of the stored array and shift every single element to 
		 * the right. both of those operations run in O(n) time, 
		 * and are performed only once. the rest of the method 
		 * is constant time operations. therfore, the time complexity
		 * is O(n).
		 */
		if(i < 0 || i > size)
		{
			throw new IndexOutOfBoundsException();
		}
		if(size == array.length)
		{
			increaseCapacity();
		}
		if(i < size)
		{
			shiftRight(i);
		}
		array[i] = e;
		size++;
	}
	
	/**
	 * Removes and returns the element at the given index, shifting all subsequent
	 * elements in the list one position closer to the front.
	 * @param  i   the index of the element to be removed
	 * @return the element that had be stored at the given index
	 * @throws IndexOutOfBoundsException if the index is negative or greater than size()
	 */
	@TimeComplexity("O(n)")
	@Override
	public E remove(int i) throws IndexOutOfBoundsException {
		/* TCJ
		 * at worst case, this method must shift every element to
		 * the left. this operation has O(n) time complexity. the rest
		 * of the method is constant operations. therefore the time
		 * complexity is O(n).
		 */
		if(i < 0 || i >= size)
		{
			throw new IndexOutOfBoundsException();
		}
		E e = array[i];
		array[i] = null;
		if(i < size - 1)
		{
			shiftLeft(i);
		}
		size--;
		return e;
	}

	/**
	 * Returns an iterator of the elements stored in the list.
	 * @return iterator of the list's elements
	 */
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E> () {
			private int position = 0;
			
			/**
			 * Returns true if the iteration has more elements.
			 * @return true if the iteration has more elements
			 */
			@TimeComplexity("O(1)")
			@Override
			public boolean hasNext() {
				/* TCJ
				 * This method has one constant operation.
				 */
				return position < size;
			}

			/**
			 * Returns the next element in the iteration. 
			 * @return the next element in the iteration
			 * @throws NoSuchElementException - if the iteration has no more elements
			 */
			@TimeComplexity("O(1)")
			@Override
			public E next() 
			{
				/* TCJ
				 * Every operation in this method is constant.
				 */
				if(this.hasNext())
				{
					E current = ArrayList.this.array[position];
					position++;
					return current;
				}
				else throw new NoSuchElementException();
			}
			
		};
	}

	/**
	 * Adds the given element to the beginning of the list, as per the 
	 * add method. 
	 * @param e the element to add
	 * @throws IndexOutOfBoundsException
	 */
	@TimeComplexity("O(n)")
	public void addFirst(E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * this method only calls the add method. since all elements
		 * in the array must be shifted to the right in every case, 
		 * this has the same worst case parameters as the add method.
		 * therefore, it has the same time complexity as the add method.
		 * it runs in O(n).
		 */
		add(0, e);
	}
	
	/**
	 * Adds the given element to the end of the list, as per the 
	 * add method. 
	 * @param e the element to add
	 * @throws IndexOutOfBoundsException
	 */
	@TimeComplexity("O(n)")
	public void addLast(E e) throws IndexOutOfBoundsException {
		/* TCJ
		 * this method only calls the add method. in this method, 
		 * no elements will need to be shifted to right, however
		 * the size of the array may need to be increased, an 
		 * operation that runs in O(n). the rest of the add
		 * method accessed in this case runs in constant time, 
		 * so the time complexity for this method is O(n). 
		 */
		add(size, e);
	}
	
	/**
	 * Removes the first element in the list, as per the remove method, 
	 * and then returns that element. 
	 * @return the element at the beginning of the list
	 * @throws IndexOutOfBoundsException
	 */
	@TimeComplexity("O(n)")
	public E removeFirst() throws IndexOutOfBoundsException {
		/* TCJ
		 * this method only calls the remove method, however
		 * it calls its worst case. therefore this method has 
		 * the same time complexity as the remove method, which
		 * is O(n).
		 */
		return remove(0);
	}
	
	/**
	 * Removes the last element in the list, as per the remove method, 
	 * and then returns that element. 
	 * @return the element at the end of the list
	 * @throws IndexOutOfBoundsException
	 */
	@TimeComplexity("O(1)")
	public E removeLast() throws IndexOutOfBoundsException {
		/* TCJ
		 * this method only calls the remove method. however, 
		 * it calls the best case for the remove method. every
		 * operation accessed in the remove method is performed 
		 * a constant number of times, therefore this method's
		 * time complexity is O(1).
		 */
		return remove(size - 1);
	}
	
	/**
	 * A utility method that moves elements to the right to create 
	 * an empty space at the target index. 
	 * @param target the index to create space at
	 */
	@TimeComplexity("O(n)")
	private void shiftRight(int target)
	{
		/* TCJ
		 * all the elements starting at the target index have to shift
		 * to the right. the number of elements that must shift is 
		 * n-target. at worst case, target is 0. that makes for n 
		 * operations. 
		 */
		for(int i = size; i > target; i--)
		{
			array[i] = array[i - 1];
		}
	}
	
	/**
	 * A utility method that moves elements to the left to fill
	 * an empty space at the target index. 
	 * @param target the index to fill space at
	 */
	@TimeComplexity("O(n)")
	private void shiftLeft(int target)
	{
		/* TCJ
		 * all the elements after the target index have to shift
		 * to the left. the number of elements that must shift is 
		 * n-target. at worst case, target is 0. that makes for n 
		 * operations. 
		 */
		for(int i = target + 1; i < size; i++)
		{
			array[i - 1] = array[i];
		}
		array[size - 1] = null;
	}
	
	/**
	 * A utility method that adds more space to the current list 
	 * by replacing the old array with a larger copy.
	 */
	@TimeComplexity("O(n)")
	private void increaseCapacity()
	{
		/* TCJ
		 * this method must make a copy of the current array with more
		 * space in two steps: copying and assignment. the copying 
		 * portion takes n operations, and the array assignment 
		 * operation takes n+increment operations. each step
		 * is performed once, so the time complexity is O(n).
		 */
		E[] copy = (E[]) new Object[array.length + increment];
		for(int i = 0; i < size; i++)
		{
			copy[i] = array[i];
		}
		array = copy;
	}
}
