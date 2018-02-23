
/**
 * Implement Queue ADT using a fixed-length array in circular fashion 
 *
 * @author ruihong-adm
 * @param <E> - formal type 
 *
 */

package cs2321;

import net.datastructures.Queue;

public class CircularArrayQueue<E> implements Queue<E> {
	
	private E[] array;
	private int front = 0;
	private int rear = 0;
	private int size = 0;

	/**
	 * Creates a new CircularArrayQueue with the given queue size.
	 * @param queueSize - the size of the CircularArrayQueue
	 * @throws IllegalArgumentException - if queueSize is less than 1
	 */
	public CircularArrayQueue(int queueSize) {
		if(queueSize > 0)
		{
			array = (E[]) new Object[queueSize];
		}
		else
		{
			throw new IllegalArgumentException("The queue size must be at least 1.");
		}
	}
	
	@Override
	/**
	 * Returns the number of elements in the queue.
	 * @return number of elements in the queue
	 */
	@TimeComplexity("O(1)")
	public int size() {
		/* TCJ
		 * This method performs one simple operation. 
		 */
		return size;
	}

	@Override
	/**
	 * Tests whether the queue is empty.
	 * @return true if the queue is empty, false otherwise
	 */
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		/* TCJ
		 * This method performs one simple operation. 
		 */
		return size == 0;
	}
	
	@Override
	/**
	 * Inserts an element at the rear of the queue.
	 * @param e  the element to be inserted
	 */
	@TimeComplexity("O(1)")
	public void enqueue(E e) 
	{
		/* TCJ
		 * Each operation performed in this method are simple
		 * operations performed only once. 
		 */
		if(size < array.length)
		{
			array[rear] = e;
			size++;
			advance(Locale.REAR, rear);
			return;
		}
		else throw new IllegalStateException("The queue is full.");
	}

	@Override
	/**
	 * Returns, but does not remove, the first element of the queue.
     * @return the first element of the queue (or null if empty)
	 */
	@TimeComplexity("O(1)")
	public E first() {
		/* TCJ
		 * This method performs one constant operation. 
		 */
		return array[front];
	}

	@Override
	/**
	 * Removes and returns the first element of the queue.
	 * @return element removed (or null if empty)
	 */
	@TimeComplexity("O(1)")
	public E dequeue() {
		/* TCJ
		 * Each operation in this method is a simple operation 
		 * performed only once. 
		 */
		E first = null;
		if(!isEmpty())
		{
			first = array[front];
			array[front] = null;
			advance(Locale.FRONT, front);
			size--;
		}
		return first;
	}
	
	/**
	 * A utility method that advances either the front or rear 
	 * pointer according to the given Locale. 
	 * @param pointer - the pointer to advance
	 * @param current - the current value of the pointer
	 */
	@TimeComplexity("O(1)")
	private void advance(Locale pointer, int current)
	{
		/* TCJ
		 * Every operation performed in this method is a 
		 * simple operation performed only once. 
		 */
		current++;
		current = current % array.length;
		switch(pointer)
		{
		case FRONT:
			front = current; //Let's take this war to Germany
			break;
		case REAR:
			rear = current; //Sie dringen in unser Haus, Deutschland
			break;
		}
	}
	
	/**
	 * An enum used exclusively for the purpose of avoiding code duplication.
	 * @author overw
	 */
	private enum Locale
	{
		FRONT, REAR;
	}
}
