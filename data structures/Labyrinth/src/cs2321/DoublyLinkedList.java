package cs2321;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.datastructures.Position;
import net.datastructures.PositionalList;


public class DoublyLinkedList<E> implements PositionalList<E> {
	
	private Node header;
	private Node trailer;
	private int size = 0;
	
	public static void main(String[] args)
	{
		long[] data = experiment3();
		print(data, average(data));
	}
	
	public static long[] experiment1()
	{
		DoublyLinkedList<Integer>[] a = new DoublyLinkedList[5000];
		long[] time = new long[100];
		for(int i = 0; i < a.length; i++)
		{
			DoublyLinkedList<Integer> n = new DoublyLinkedList<Integer>();
			for(int j = 0; j < 100; j++)
			{
				n.addLast(1);
			}
			a[i] = n;
		}
		for(int i = 1; i <= 100; i++)
		{
			long initial = System.nanoTime();
			for(DoublyLinkedList<Integer> n : a)
			{
				Position<Integer> p = n.first();
				for(int j = 1; j < i; j++)
				{
					p = n.after(p);
				}
			}
			long last = System.nanoTime();
			time[i - 1] = last - initial;
		}
		return time;
	}
	
	public static long[] experiment2()
	{
		long[] time = new long[200];
		for(int i = 1; i <= 200; i++)
		{
			DoublyLinkedList<Integer>[] a = new DoublyLinkedList[5000];
			for(int j = 0; j < a.length; j++)
			{
				a[j] = new DoublyLinkedList<Integer>();
			}
			
			long initial = System.nanoTime();
			for(DoublyLinkedList<Integer> n : a)
			{
				for(int j = 1; j < i; j++)
				{
					n.addFirst(1);
				}
			}
			long last = System.nanoTime();
			time[i - 1] = last - initial;
		}
		return time;
	}
	
	public static long[] experiment3()
	{
		long[] time = new long[200];
		for(int i = 1; i <= 200; i++)
		{
			DoublyLinkedList<Integer>[] a = new DoublyLinkedList[5000];
			for(int j = 0; j < a.length; j++)
			{
				a[j] = new DoublyLinkedList<Integer>();
			}
			
			long initial = System.nanoTime();
			for(DoublyLinkedList<Integer> n : a)
			{
				for(int j = 1; j < i; j++)
				{
					n.addLast(1); 
				}
			}
			long last = System.nanoTime();
			time[i - 1] = last - initial;
		}
		return time;
	}
	
	public static void print(long[] data, long average)
	{
		double avg = (double) average;
		for(int i = 0; i < data.length; i++)
		{
			double time = data[i] / avg;
			System.out.println(time);
		}
	}
	
	public static long average(long[] time)
	{
		long average = 0;
		for(long l : time)
		{
			average+= l;
		}
		return (average / time.length);
	}

	public DoublyLinkedList() {
		header = new Node(null, null, null);
		trailer = new Node(null, header, null);
		header.setNext(trailer);
	}

	@Override
	/**
	 * Returns the number of elements in the list.
     * @return number of elements in the list
	 */
	@TimeComplexity("O(1)")
	public int size() {
		/* TCJ
		 * This method only performs a single constant time operation. 
		 */
		return size;
	}

	@Override
	/**
	 * Tests whether the list is empty.
	 * @return true if the list is empty, false otherwise
	 */
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		/* TCJ
		 * This method only performs a single constant time operation. 
		 */
		return size == 0;
	}

	@Override
	/**
	 * Returns the first Position in the list.
	 *
	 * @return the first Position in the list (or null, if empty)
	 */
	@TimeComplexity("O(1)")
	public Position<E> first() {
		/* TCJ
		 * This method only performs a constant number of simple operations
		 * and constant time method calls. 
		 */
		return (isEmpty()) ? null : header.getNext();
	}

	@Override
	/**
	 * Returns the last Position in the list.
	 *
	 * @return the last Position in the list (or null, if empty)
	 */
	@TimeComplexity("O(1)")
	public Position<E> last() {
		/* TCJ
		 * This method only performs a constant number of simple operations
		 * and constant time method calls. 
		 */
		return (isEmpty()) ? null : trailer.getPrevious();
	}

	@Override
	/**
	 * Returns the Position immediately before Position p.
	 * @param p   a Position of the list
	 * @return the Position of the preceding element (or null, if p is first)
	 * @throws IllegalArgumentException if p is not a valid position for this list
	 */
	@TimeComplexity("O(1)")
	public Position<E> before(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * This method only performs a constant number of simple operations
		 * and constant time method calls. 
		 */
		if(p == null) return null;
		if(isValid(p))
		{
			return (isEmpty()) ? null : (Position<E>) ((Node)p).getPrevious();
		}
		else throw new IllegalArgumentException("The given position was invalid.");
	}

	@Override
	/**
	 * Returns the Position immediately after Position p.
	 * @param p   a Position of the list
	 * @return the Position of the following element (or null, if p is last)
	 * @throws IllegalArgumentException if p is not a valid position for this list
	 */
	
	public Position<E> after(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * This method only performs a constant number of simple operations
		 * and constant time method calls. 
		 */
		if(p == null) return null;
		if(isValid(p))
		{
			return (Position<E>) ((Node)p).getNext();
		}
		else throw new IllegalArgumentException("The given position was invalid.");
	}

	@Override
	/**
	 * Inserts an element at the front of the list.
	 *
	 * @param e the new element
	 * @return the Position representing the location of the new element
	 */
	@TimeComplexity("O(1)")
	public Position<E> addFirst(E e) {
		/* TCJ
		 * This method only calls a constant time method. 
		 */
		return addAfter(header, e);
	}

	@Override
	/**
	 * Inserts an element at the back of the list.
	 *
	 * @param e the new element
	 * @return the Position representing the location of the new element
	 */
	@TimeComplexity("O(1)")
	public Position<E> addLast(E e) {
		/* TCJ
		 * This method only calls a constant time method. 
		 */
		return addBefore(trailer, e);
	}

	@Override
	/**
	 * Inserts an element immediately before the given Position.
	 *
	 * @param p the Position before which the insertion takes place
	 * @param e the new element
	 * @return the Position representing the location of the new element
	 * @throws IllegalArgumentException if p is not a valid position for this list
	 */
	@TimeComplexity("O(1)")
	public Position<E> addBefore(Position<E> p, E e)
			throws IllegalArgumentException {
		/* TCJ
		 * This method performs only simple operations and calls constant
		 * time methods a constant number of times. 
		 */
		if(isValid(p))
		{
			Node given = (Node) p;
			Node n = new Node(e, given.getPrevious(), given);
			given.getPrevious().setNext(n);
			given.setPrevious(n);
			size++;
			return (Position<E>) n;
		}
		else throw new IllegalArgumentException("The given position was invalid.");
	}

	@Override
	/**
	 * Inserts an element immediately after the given Position.
	 *
	 * @param p the Position after which the insertion takes place
	 * @param e the new element
	 * @return the Position representing the location of the new element
	 * @throws IllegalArgumentException if p is not a valid position for this list
	 */
	@TimeComplexity("O(1)")
	public Position<E> addAfter(Position<E> p, E e)
			throws IllegalArgumentException {
		/* TCJ
		 * This method performs only simple operations and calls constant
		 * time methods a constant number of times. 
		 */
		if(isValid(p))
		{
			Node given = (Node) p;
			Node n = new Node(e, given, given.getNext());
			given.getNext().setPrevious(n);
			given.setNext(n);
			size++;
			return (Position<E>) n;
		}
		else throw new IllegalArgumentException("The given position was invalid.");
	}

	@Override
	/**
	 * Replaces the element stored at the given Position and returns the replaced element.
	 *
	 * @param p the Position of the element to be replaced
	 * @param e the new element
	 * @return the replaced element
	 * @throws IllegalArgumentException if p is not a valid position for this list
	 */
	@TimeComplexity("O(1)")
	public E set(Position<E> p, E e) throws IllegalArgumentException {
		/* TCJ
		 * This method only performs simple operations and calls constant
		 * time methods a constant number of times. 
		 */
		if(isValid(p))
		{
		Node given = (Node) p;
		Node n = new Node(e, given.getPrevious(), given.getNext());
		given.getPrevious().setNext(n);
		given.getNext().setPrevious(n);
		E old = p.getElement();
		given.invalidate();
		return old;
		}
		else throw new IllegalArgumentException("The given position was invalid.");
	}

	@Override
	/**
	 * Removes the element stored at the given Position and returns it.
	 * The given position is invalidated as a result.
	 *
	 * @param p the Position of the element to be removed
	 * @return the removed element 
	 * @throws IllegalArgumentException if p is not a valid position for this list
	 */
	@TimeComplexity("O(1)")
	public E remove(Position<E> p) throws IllegalArgumentException {
		/* TCJ
		 * This method only performs simple operations and calls 
		 * constant time methods a constant number of times. 
		 */
		if(isValid(p))
		{
		Node given = (Node) p;
		given.getPrevious().setNext(given.getNext());
		given.getNext().setPrevious(given.getPrevious());
		size--;
		E e = p.getElement();
		given.invalidate();
		return e;
		}
		else throw new IllegalArgumentException("The given position was invalid.");
	}

	@Override
	/**
	 * Returns an iterator of the elements stored in the list.
	 * @return iterator of the list's elements
	 */
	public Iterator<E> iterator() {
		return new Iterator<E>()
				{
					private Node position = header;
					
					@Override
					public boolean hasNext() {
						return position.getNext().getElement() != null;
					}

					@Override
					public E next() {
						if(hasNext())
						{
							position = position.getNext();
							return position.getElement();
						}
						else
						{
							throw new NoSuchElementException();
						}
					}
			
				};
	}

	@Override
	/**
	 * Returns the positions of the list in iterable form from first to last.
	 * @return iterable collection of the list's positions
	 */
	public Iterable<Position<E>> positions() {
		return new Iterable<Position<E>>()
				{
					
					@Override
					public Iterator<Position<E>> iterator() {
						return new Iterator<Position<E>>()
						{
							private Node position = header;
							
							@Override
							public boolean hasNext() {
								return position.getNext().getElement() != null;
							}

							@Override
							public Position<E> next() {
								if(hasNext())
								{
									position = position.getNext();
									return (Position<E>) position;
								}
								else
								{
									throw new NoSuchElementException();
								}
							}
					
						};
					}
			
				};
	}
	
	/**
	 * Removes the first position in the list and returns the 
	 * element stored by it. 
	 * @return the element stored by the first position (or null if empty)
	 * @throws IllegalArgumentException
	 */
	public E removeFirst() throws IllegalArgumentException {
		return remove(header.getNext());
	}
	
	/**
	 * Removes the last position in the list and return the
	 * element stored by it. 
	 * @return the element stored by the last position (or null if empty)
	 * @throws IllegalArgumentException
	 */
	public E removeLast() throws IllegalArgumentException {
		return remove(trailer.getPrevious());
	}
	
	/**
	 * Returns whether or not the given position is valid for this list. 
	 * @param p - the position to test
	 * @return true if the given position is valid
	 */
	@TimeComplexity("O(1)")
	public boolean isValid(Position<E> p)
	{
		if(header.getClass().isInstance(p))
		{
			Node n = (Node) p;
			return n.isValid() && this.hashCode() == n.parentHashCode();
		}
		else return false;
	}
	
	/**
	 * An implementation of the Position interface that holds 
	 * the preceding and succeeding Nodes. 
	 * @author Evan Overweg
	 *
	 */
	private class Node implements Position<E>
	{

		private E element;
		private Node previous;
		private Node next;
		
		private boolean isValid = true;
		private int parentHashCode = DoublyLinkedList.this.hashCode();
		
		/**
		 * A constructor for Node objects. 
		 * @param element - the element for this node to store
		 * @param previous - the previous node in the list
		 * @param next - the next node in the list
		 */
		protected Node(E element, Node previous, Node next)
		{
			this.element = element;
			this.previous = previous;
			this.next = next;
		}
		
		@Override
		/**
		 * Returns the element stored at this position.
		 *
		 * @return the stored element
		 * @throws IllegalStateException if position no longer valid
		 */
		@TimeComplexity("O(1)")
		public E getElement() throws IllegalStateException {
			/* TCJ
			 * This method performs two constant-time operations. 
			 */
			if(isValid)
			{
				return element;
			}
			else throw new IllegalStateException();
		}
		
		/**
		 * Returns the node before this one.
		 * @return the previous node
		 */
		@TimeComplexity("O(1)")
		protected Node getPrevious()
		{
			/* TCJ
			 * This method performs one constant-time operation. 
			 */
			return previous;
		}
		
		/**
		 * Returns the node after this one.
		 * @return the next node
		 */
		@TimeComplexity("O(1)")
		protected Node getNext()
		{
			/* TCJ
			 * This method performs one constant-time operation. 
			 */
			return next;
		}
		
		/**
		 * Returns whether or not this position is still valid. 
		 * @return true if valid
		 */
		@TimeComplexity("O(1)")
		protected boolean isValid()
		{
			/* TCJ
			 * This method performs one constant-time operation. 
			 */
			return isValid;
		}
		
		/**
		 * Returns the hash code of the DoublyLinkedList this Node
		 * was made for. 
		 * @return the list's hash code
		 */
		@TimeComplexity("O(1)")
		protected int parentHashCode()
		{
			/* TCJ
			 * This method performs one constant-time operation. 
			 */
			return parentHashCode;
		}
		
		/**
		 * Changes the node stored before this one.
		 * @param previous - the new node to store
		 */
		@TimeComplexity("O(1)")
		protected void setPrevious(Node previous)
		{
			/* TCJ
			 * This method performs one constant-time operation. 
			 */
			this.previous = previous;
		}
		
		/**
		 * Changes the node stored after this one.
		 * @param next - the new node to store
		 */
		@TimeComplexity("O(1)")
		protected void setNext(Node next)
		{
			/* TCJ
			 * This method performs one constant-time operation. 
			 */
			this.next = next;
		}
		
		/**
		 * Invalidates this position. Cannot be reversed. 
		 */
		@TimeComplexity("O(1)")
		protected void invalidate()
		{
			/* TCJ
			 * This method performs one constant-time operation. 
			 */
			isValid = false;
		}
	}
}
