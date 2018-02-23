package cs2321;

import java.util.Comparator;

import net.datastructures.*;

public class AVL<K,V> extends BinarySearchTree<K,V> implements  SortedMap<K,V> {

	public AVL() {
		super();
	}
	
	public AVL(Comparator<K> c) {
		super(c);
	}
	
	/**
	 * Returns the height of the given position provided 
	 * it is a valid position. 
	 * @param v - the position to return the height of
	 * @return the height of the given position
	 */
	@TimeComplexity("O(1)")
	public int height(Position<Entry<K,V>> v) {
		if(v != null && isValid(v))
		{
			return ((Node) v).height;
		}
		throw new IllegalArgumentException();
	}
	
	/**
	 * Determines whether or not the given node is 
	 * a balanced subtree, thus sealing its eternal
	 * fate. 
	 * @param z - the node to judge
	 * @return the fate of the node
	 */
	@TimeComplexity("O(1)")
	private boolean isBalanced(Node z)
	{
		int leftHeight = (z.getLeft() != null) ? z.getLeft().height : 0;
		int rightHeight = (z.getRight() != null) ? z.getRight().height : 0;
		int diff = Math.abs(leftHeight - rightHeight);
		return diff <= 1;
	}
	
	/**
	 * Like Vishnu, this method seeks to preserve
	 * order in the tree (and maintain O(lg n) for 
	 * many of the things). 
	 * @param z - the node that is under scrutiny
	 */
	@TimeComplexity("O(lg n)")
	private void rebalance(Node z)
	{
		/* TCJ
		 * Runs up from one node on the tree to the top, balancing
		 * subtrees. 
		 * Since the tree will be balanced, at worst
		 * this takes O(lg n). 
		 */
		while(z != null)
		{
			int oldHeight = z.height;
			
			super.setHeight(z);
			if(!isBalanced(z))
			{
				z = reconstruct(z);
				super.setHeight(z.getLeft());
				super.setHeight(z.getRight());
				super.setHeight(z);
			}
			int newHeight = z.height;
			if(newHeight == oldHeight) break;
			
			z = z.getParent();
		}
	}
	
	/**
	 * Enacts the will of the great rebalance(). 
	 * @param z - the subject to act upon
	 * @return the resulting center node
	 */
	@TimeComplexity("O(1)")
	private Node reconstruct(Node z)
	{
		Node p = z.getParent();
		Node y;
		if((z.getLeft() != null && z.getRight() == null) || 
				(z.getLeft() != null && z.getLeft().height > z.getRight().height))
		{
			y = z.getLeft();
		}
		else
		{
			y = z.getRight();
		}
		Node x;
		if((y.getLeft() != null && y.getRight() == null) || 
				(z.getLeft() != null && z.getLeft().height > z.getRight().height))
		{
			x = y.getLeft();
		}
		else
		{
			x = y.getRight();
		}
		
		Node a, b, c, t0, t1, t2, t3;
		if(z.getRight() == y && y.getRight() == x)
		{
			a = z;
			b = y;
			c = x;
			t0 = a.getLeft();
			t1 = b.getLeft();
			t2 = c.getLeft();
			t3 = c.getRight();
		}
		else if(z.getLeft() == y && y.getLeft() == x)
		{
			a = x;
			b = y;
			c = z;
			t0 = a.getLeft();
			t1 = a.getRight();
			t2 = b.getRight();
			t3 = c.getRight();
		}
		else if(z.getRight() == y && y.getLeft() == x)
		{
			a = z;
			b = x;
			c = y;
			t0 = a.getLeft();
			t1 = b.getLeft();
			t2 = b.getRight();
			t3 = c.getRight();
		}
		else
		{
			a = y;
			b = x;
			c = z;
			t0 = a.getLeft();
			t1 = b.getLeft();
			t2 = b.getRight();
			t3 = c.getRight();
		}
		b.setLeft(a); b.setRight(c); a.setParent(b); c.setParent(b);
		a.setLeft(t0); a.setRight(t1); 
		if(t0 != null)
		{
			t0.setParent(a);
		}
		if(t1 != null)
		{
			t1.setParent(a);
		}
		c.setLeft(t2); c.setRight(t3);
		if(t2 != null)
		{
			t2.setParent(c);
		}
		if(t3 != null)
		{
			t3.setParent(c);
		}
		b.setParent(p);
		if(p == null)
		{
			root = b;
		}
		else
		{
			if(p.getLeft() == z)
			{
				p.setLeft(b);
			}
			else
			{
				p.setRight(b);
			}
		}
		return b;
	}

	/**
	 * AVL implementation of the put method which 
	 * also maintains a balanced tree. 
	 * @param key - the key to insert
	 * @param value - the value to insert
	 * @return the old value, or null if no value was previously
	 * associated with the key
	 */
	@Override
	@TimeComplexity("O(lg n)")
	public V put(K key, V value) {
		/* TCJ
		 * Unlike the BST implementation, the AVL maintains 
		 * a balanced tree, so it all but guarantees O(lg n)
		 * time. 
		 */
		V oldV = super.put(key, value);
		if(oldV != null)
		{
			rebalance(super.last_inserted_node);
		}
		return oldV;
	}
	
	/**
	 * AVL implementation of the remove method which 
	 * also maintains a balanced tree. 
	 * @param key - the key to remove
	 * @return the old value, or null if no value was
	 * associated with the key
	 */
	@Override
	@TimeComplexity("O(lg n)")
	public V remove(K key) {
		/* TCJ
		 * Unlike the BST implementation, the AVL maintains 
		 * a balanced tree, so it all but guarantees O(lg n)
		 * time. 
		 */
		V oldV = super.remove(key);
		if(oldV != null)
		{
			rebalance(super.parent_of_last_removed_node);
		}
		return oldV;
	}
	
}
