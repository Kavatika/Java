/**
 * 
 */
package cs2321;

import java.util.Iterator;

import net.datastructures.BinaryTree;
import net.datastructures.Position;

/**
 * @author CS2321 Instructor
 *
 */
public class LinkedBinaryTree<E> implements BinaryTree<E> {

	protected Node root = null;
	private int size = 0;
	
	/**
	 * Returns the root Position of the tree (or null if tree is empty).
	 * @return root Position of the tree (or null if tree is empty)
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> root() {
		return root;
	}
	
	/**
	 * If the current root is not null, throw IllegalArgumentException
	 * @param e - the element to add at the root. 
	 * @return the position representing the root
	 */
	@TimeComplexity("O(1)")
	public Position<E> addRoot(E e) throws IllegalArgumentException{
		if(root == null)
		{
			root = new Node(e, null, null, null);
			size++;
			return root;
		}
		else throw new IllegalArgumentException("Root already added.");
	}

	/**
	 * Returns the Position of p's parent (or null if p is root).
	 *
	 * @param p    A valid Position within the tree
	 * @return Position of p's parent (or null if p is root)
	 * @throws IllegalArgumentException if p is not a valid Position for this tree.
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			return n.getParent();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns an iterable collection of the Positions representing p's children.
	 *
	 * @param p    A valid Position within the tree
	 * @return iterable collection of the Positions of p's children
	 * @throws IllegalArgumentException if p is not a valid Position for this tree.
	 */	
	@Override
	@TimeComplexity("O(1)")
	public Iterable<Position<E>> children(Position<E> p)
			throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			ArrayList<Position<E>> snapshot = new ArrayList<Position<E>>();
			if(n.getLeft() != null) snapshot.addLast(n.getLeft());
			if(n.getRight() != null) snapshot.addLast(n.getRight());
			return snapshot;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns the number of children of Position p.
	 *
	 * @param p    A valid Position within the tree
	 * @return number of children of Position p
	 * @throws IllegalArgumentException if p is not a valid Position for this tree.
	 */
	@Override
	@TimeComplexity("O(1)")
	public int numChildren(Position<E> p) throws IllegalArgumentException 
	{
		if(p != null && isValid(p))
		{
			int count = 0;
			Node n = (Node) p;
			if(n.getLeft() != null) count++;
			if(n.getRight() != null) count++;
			return count;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns true if Position p has one or more children.
	 *
	 * @param p    A valid Position within the tree
	 * @return true if p has at least one child, false otherwise
	 * @throws IllegalArgumentException if p is not a valid Position for this tree.
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isInternal(Position<E> p) throws IllegalArgumentException {
		return numChildren(p) > 0;
	}

	/**
	 * Returns true if Position p does not have any children.
	 *
	 * @param p    A valid Position within the tree
	 * @return true if p has zero children, false otherwise
	 * @throws IllegalArgumentException if p is not a valid Position for this tree.
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isExternal(Position<E> p) throws IllegalArgumentException {
		return numChildren(p) == 0;
	}

	/**
	 * Returns true if Position p represents the root of the tree.
	 *
	 * @param p    A valid Position within the tree
	 * @return true if p is the root of the tree, false otherwise
	 * @throws IllegalArgumentException if p is not a valid Position for this tree.
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isRoot(Position<E> p) throws IllegalArgumentException {
		return root.equals(p);
	}

	/**
	 * Returns the number of nodes in the tree.
	 * @return number of nodes in the tree
	 */
	@Override
	@TimeComplexity("O(1)")
	public int size() {
		return size;
	}

	/**
	 * Tests whether the tree is empty.
	 * @return true if the tree is empty, false otherwise
	 */
	@Override
	@TimeComplexity("O(1)")
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns an iterator of the elements stored in the tree.
	 * @return iterator of the tree's elements
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterator<E> iterator() {
		/* TCJ
		 * This method calls positions() to initialize its 
		 * internal iterator, which has a time complexity
		 * of O(n). 
		 */
		return new Iterator<E>(){
			private Iterator<Position<E>> it = positions().iterator();
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public E next() {
				return it.next().getElement();
			}
		};
	}

	/**
	 * Returns an instance of the iterable interface that 
	 * represents all positions in this Tree in preorder. 
	 * @return an Iterable representing all positions in the tree
	 */
	@Override
	@TimeComplexity("O(n)")
	public Iterable<Position<E>> positions() {
		/* TCJ
		 * This method calls preorderSubtree from the root node, 
		 * which has a time complexity of O(n). 
		 */
		ArrayList<Position<E>> snapshot = new ArrayList<Position<E>>();
		if(!isEmpty())
		{
			preorderSubtree(root, snapshot);
		}
		return snapshot;
	}
	
	/**
	 * Recursively builds a preordered arraylist from the given Position. 
	 * @param p - the current position in recursion
	 * @param snapshot - the arraylist to add positions to
	 */
	@TimeComplexity("O(n)")
	private void preorderSubtree(Position<E> p, ArrayList<Position<E>> snapshot)
	{
		/* TCJ
		 * This method iterates through every position
		 * descended from the initially given position. 
		 */
		snapshot.addLast(p);
		for(Position<E> c : children(p))
		{
			preorderSubtree(c, snapshot);
		}
	}

	/**
	 * Returns the Position of p's left child (or null if no child exists).
	 *
	 * @param p A valid Position within the tree
	 * @return the Position of the left child (or null if no child exists)
	 * @throws IllegalArgumentException if p is not a valid Position for this tree
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> left(Position<E> p) throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			return n.getLeft();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns the Position of p's right child (or null if no child exists).
	 *
	 * @param p A valid Position within the tree
	 * @return the Position of the right child (or null if no child exists)
	 * @throws IllegalArgumentException if p is not a valid Position for this tree
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> right(Position<E> p) throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			return n.getRight();
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Returns the Position of p's sibling (or null if no sibling exists).
	 *
	 * @param p A valid Position within the tree
	 * @return the Position of the sibling (or null if no sibling exists)
	 * @throws IllegalArgumentException if p is not a valid Position for this tree
	 */
	@Override
	@TimeComplexity("O(1)")
	public Position<E> sibling(Position<E> p) throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			return (n.equals(n.getParent().getLeft()))? n.getParent().getRight() : n.getParent().getLeft();
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * Insert a new position containing the given element as
	 * the left child of p and returns the new position. 
	 * Throws an exception if the given position is invalid. 
	 * @param p - the position to insert to the left of
	 * @param e - the element to insert
	 * @return the newly inserted position
	 */
	@TimeComplexity("O(1)")
	public Position<E> insertLeft(Position<E> p, E e) throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			if(n.getLeft() == null) 
			{
				Node w = new Node(e, n, null, null);
				n.setLeft(w);
				size++;
				return w;
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Insert a new position containing the given element as
	 * the right child of p and returns the new position. 
	 * Throws an exception if the given position is invalid. 
	 * @param p - the position to insert to the right of
	 * @param e - the element to insert
	 * @return the newly inserted position
	 */
	@TimeComplexity("O(1)")
	public Position<E> insertRight(Position<E> p, E e) throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			if(n.getRight() == null) 
			{
				Node w = new Node(e, n, null, null);
				n.setRight(w);
				size++;
				return w;
			}
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * Removes the given position from the subtree and returns
	 * its parent. Throws an exception if the given position has
	 * more than one child. 
	 * @param p - the position to remove
	 * @return the parent of the remvoed node
	 * @throws IllegalArgumentException
	 */
	@TimeComplexity("O(1)")
	public Position<E> remove(Position<E> p) throws IllegalArgumentException {
		if(p != null && isValid(p))
		{
			Node n = (Node) p;
			if(n.getLeft() != null && n.getRight() != null)
			{
				throw new IllegalArgumentException();
			}
			Node w = null;
			if(n.getLeft() != null)
			{
				w = n.getLeft();
			}
			else if(n.getRight() != null)
			{
				w = n.getRight();
			}
			if(root.equals(n))
			{
				root = w;
			}
			else
			{
				if(n.equals(n.getParent().getLeft()))
				{
					n.getParent().setLeft(w);
				}
				else
				{
					n.getParent().setRight(w);
				}
			}
			if(w != null)
			{
				w.setParent(n.getParent());
			}
			n.invalidate();
			size--;
			return n.getParent();
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * Returns whether or not the given position is valid. 
	 * @param p the position to verify
	 * @return true if the position is valid
	 */
	@TimeComplexity("O(1)")
	protected boolean isValid(Position<E> p)
	{
		
		if(root.getClass().isInstance(p))
		{
			Node n = (Node) p;
			return n.isValid() && this.hashCode() == n.parentHashCode();
		}
		else return false;
	}

	/**
	 * Sets the height of the given node. 
	 * @param n - the node to set the height of
	 */
	@TimeComplexity("O(1)")
	protected void setHeight(Node n)
	{
			int leftHeight = (n.getLeft() != null) ? n.getLeft().height : 0;
			int rightHeight = (n.getRight() != null) ? n.getRight().height : 0;
			n.height = Math.max(leftHeight, rightHeight) + 1;
	}
	
	protected class Node implements Position<E>
	{
		private E e = null;
		private Node parent;
		private Node left;
		private Node right;
		
		protected int height;
		private boolean isValid = true;
		private int parenthashcode = LinkedBinaryTree.this.hashCode();
		
		protected Node(E e, Node parent, Node left, Node right)
		{
			this.e = e;
			this.parent = parent;
			this.left = left;
			this.right = right;
			setHeight(this);
		}
		
		@Override
		public E getElement() throws IllegalStateException {
			return e;
		}
		
		protected Node getParent()
		{
			return parent;
		}
		
		protected Node getLeft()
		{
			return left;
		}
		
		protected Node getRight()
		{
			return right;
		}
		
		protected E setElement(E e)
		{
			E oldValue = this.e;
			this.e = e;
			isValid = true;
			return oldValue;
		}
		
		protected void setParent(Node p)
		{
			parent = p;
		}
		
		protected void setLeft(Node l)
		{
			left = l;
		}
		
		protected void setRight(Node r)
		{
			right = r;
		}
		
		protected int parentHashCode()
		{
			return parenthashcode;
		}
		
		protected boolean isValid()
		{
			return isValid;
		}
		
		protected void invalidate()
		{
			isValid = false;
		}
	}
}
