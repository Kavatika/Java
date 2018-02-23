package cs2321;

import java.util.Iterator;

import net.datastructures.*;

/*
 * Implement Graph interface. A graph can be declared as either directed or undirected.
 * In the case of an undirected graph, methods outgoingEdges and incomingEdges return the same collection,
 * and outDegree and inDegree return the same value.
 * 
 * @author CS2321 Instructor
 */
public class AdjListGraph<V, E> implements Graph<V, E> {
	
	private boolean directed;
	private DoublyLinkedList<Point> vertices = new DoublyLinkedList<Point>();
	private DoublyLinkedList<Katana> edges = new DoublyLinkedList<Katana>();

	public AdjListGraph(boolean directed) {
		this.directed = directed; 
	}

	public AdjListGraph() {
		this(false);
	}


	/** Returns the edges of the graph as an iterable collection */
	@TimeComplexity("O(1)")
	public Iterable<Edge<E>> edges() {
		return new Iterable<Edge<E>>()
				{

					@Override
					public Iterator<Edge<E>> iterator() {
						return new Iterator<Edge<E>>()
								{
									Iterator<Katana> it = edges.iterator();
									@Override
									public boolean hasNext() {
										return it.hasNext();
									}

									@Override
									public Edge<E> next() {
										return (Edge<E>) it.next();
									}
							
								};
					}
			
				};
	}

	/**
	 * Returns the vertices of edge e as an array of length two.
	 * If the graph is directed, the first vertex is the origin, and
	 * the second is the destination.  If the graph is undirected, the
	 * order is arbitrary.
	 */
	@Override
	@TimeComplexity("O(1)")
	public Vertex[] endVertices(Edge<E> e) throws IllegalArgumentException {
		isValid(e);
		Katana k = (Katana) e;
		Vertex[] ends = new Vertex[2];
		ends[0] = k.getFrom();
		ends[1] = k.getTo();
		return ends;
	}


	/**
	 * Inserts and returns a new edge between vertices u and v, storing given element.
	 *
	 * @throws IllegalArgumentException if u or v are invalid vertices, or if an edge already exists between u and v.
	 */
	@TimeComplexity("O(1)")
	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E o)
			throws IllegalArgumentException {
		isValid(u);
		isValid(v);
		Point a = (Point) u;
		Point b = (Point) v;
		Katana k = new Katana(a, b, o);
		k.setFromReference(a.addEdge(k));
		k.setToReference(b.addEdge(k));
		k.setPosition(edges.addLast(k));
		return k;
	}

	/** Inserts and returns a new vertex with the given element. */
	@TimeComplexity("O(1)")
	public Vertex<V> insertVertex(V o) {
		Point p = new Point(o);
		p.setPosition(vertices.addLast(p));
		return p;
	}

	/** Returns the number of edges of the graph */
	@TimeComplexity("O(1)")
	public int numEdges() {
		return edges.size();
	}

	/** Returns the number of vertices of the graph */
	@TimeComplexity("O(1)")
	public int numVertices() {
		return vertices.size();
	}

	/** Returns the vertex that is opposite vertex v on edge e. */
	@TimeComplexity("O(1)")
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e)
			throws IllegalArgumentException {
		isValid(v);
		isValid(e);
		Katana k = (Katana) e;
		return (k.getFrom().equals(v)) ? k.getTo() : k.getFrom();
	}

	/** Removes an edge from the graph. */
	@TimeComplexity("O(1)")
	public void removeEdge(Edge<E> e) throws IllegalArgumentException {
		isValid(e);
		Katana k = (Katana) e;
		k.getFrom().removeEdge(k.getFromReference());
		k.getTo().removeEdge(k.getToReference());
		edges.remove(k.getPosition());
		k = null;
	}

	/** Removes a vertex and all its incident edges from the graph. */
	
	public void removeVertex(Vertex<V> v) throws IllegalArgumentException {
		isValid(v);
		Point p = (Point) v;
		p.removeAllEdges();
		vertices.remove(p.getPosition());
		p = null;
	}

	/**
	 * Replaces the element stored by the given edge and returns
	 * the old element. 
	 * @param p - the edge to replace
	 * @param o - the new element
	 * @return the old element
	 */
	@TimeComplexity("O(1)")
	public E replace(Edge<E> p, E o) throws IllegalArgumentException {
		isValid(p);
		return ((Katana) p).setElement(o);
	}

	/**
	 * Replaces the element stored by the given vertex and returns
	 * the old element. 
	 * @param p - the vertex to replace
	 * @param o - the new element
	 * @return the old element
	 */
	@TimeComplexity("O(1)")
	public V replace(Vertex<V> p, V o) throws IllegalArgumentException {
		isValid(p);
		return ((Point) p).setElement(o);
	}

	/** Returns the vertices of the graph as an iterable collection */
	@TimeComplexity("O(1)")
	public Iterable<Vertex<V>> vertices() {
		return new Iterable<Vertex<V>>()
		{

			@Override
			public Iterator<Vertex<V>> iterator() {
				return new Iterator<Vertex<V>>()
						{
							Iterator<Point> it = vertices.iterator();
							@Override
							public boolean hasNext() {
								return it.hasNext();
							}

							@Override
							public Vertex<V> next() {
								return (Vertex<V>) it.next();
							}
					
						};
			}
	
		};
	}

	/**
	 * Returns the number of edges leaving vertex v.
	 * Note that for an undirected graph, this is the same result
	 * returned by inDegree
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	@Override
	public int outDegree(Vertex<V> v) throws IllegalArgumentException {
		return ((ArrayList<Edge<E>>) outgoingEdges(v)).size();
	}

	/**
	 * Returns the number of edges for which vertex v is the destination.
	 * Note that for an undirected graph, this is the same result
	 * returned by outDegree
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	@Override
	public int inDegree(Vertex<V> v) throws IllegalArgumentException {
		return ((ArrayList<Edge<E>>) incomingEdges(v)).size();
	}

	/**
	 * Returns an iterable collection of edges for which vertex v is the origin.
	 * Note that for an undirected graph, this is the same result
	 * returned by incomingEdges.
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	@Override
	@TimeComplexity("O(deg(v))")
	public Iterable<Edge<E>> outgoingEdges(Vertex<V> v)
			throws IllegalArgumentException {
		isValid(v);
		Point a = (Point) v;
		ArrayList<Edge<E>> outgoing = new ArrayList<Edge<E>>();
		for(Katana k : a.adjacentEdges)
		{
			if(directed)
			{
				if(k.getFrom().equals(a))
				{
					outgoing.addLast(k);
				}
			}
			else
			{
				outgoing.addLast(k);
			}
		}
		return outgoing;
	}

	/**
	 * Returns an iterable collection of edges for which vertex v is the destination.
	 * Note that for an undirected graph, this is the same result
	 * returned by outgoingEdges.
	 * @throws IllegalArgumentException if v is not a valid vertex
	 */
	@Override
	@TimeComplexity("O(deg(v))")
	public Iterable<Edge<E>> incomingEdges(Vertex<V> v)
			throws IllegalArgumentException {
		isValid(v);
		Point a = (Point) v;
		ArrayList<Edge<E>> incoming = new ArrayList<Edge<E>>();
		for(Katana k : a.adjacentEdges)
		{
			if(directed)
			{
				if(k.getTo().equals(a))
				{
					incoming.addLast(k);
				}
			}
			else
			{
				incoming.addLast(k);
			}
		}
		return incoming;
	}

	/** Returns the edge from u to v, or null if they are not adjacent. */
	@Override
	@TimeComplexity("O(deg(u))")
	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v)
			throws IllegalArgumentException {
		isValid(u);
		isValid(v);
		Point a = (Point) u;
		Point b = (Point) v;
		for(Katana k : a.adjacentEdges)
		{
			if(directed)
			{
				if(k.getTo().equals(b))
				{
					return k;
				}
			}
			else
			{
				if(k.getFrom().equals(b) || k.getTo().equals(b))
				{
					return k;
				}
			}
		}
		return null;
	}
	
	@TimeComplexity("O(1)")
	private boolean isValid(Vertex<V> v) throws IllegalArgumentException
	{
		if(v != null)
		{
			Point p = new Point(null);
			if(p.getClass().isInstance(v))
			{
				return true;
			}
		}
		throw new IllegalArgumentException();
	}
	
	@TimeComplexity("O(1)")
	private boolean isValid(Edge<E> e) throws IllegalArgumentException
	{
		if(e != null)
		{
			Katana k = new Katana(null, null, null);
			if(k.getClass().isInstance(e))
			{
				return true;
			}
		}
		throw new IllegalArgumentException();
	}
	
	private class Point implements Vertex<V>
	{
		private V v;
		private Position<Point> location;
		
		private DoublyLinkedList<Katana> adjacentEdges = new DoublyLinkedList<Katana>();
		
		protected Point(V v)
		{
			this.v = v;
		}
		
		@Override
		public V getElement() {
			return v;
		}
		
		protected V setElement(V v)
		{
			V oldV = this.v;
			this.v = v;
			return oldV;
		}
		
		protected Position<Point> getPosition()
		{
			return location;
		}
		
		protected void setPosition(Position<Point> p)
		{
			location = p;
		}
		
		protected Position<Katana> addEdge(Katana k)
		{
			return adjacentEdges.addLast(k);
		}
		
		protected void removeEdge(Position<Katana> p)
		{
			adjacentEdges.remove(p);
		}
		
		protected void removeAllEdges()
		{
			for(Katana k : adjacentEdges)
			{
				AdjListGraph.this.removeEdge(k);
			}
		}
	}
	
	private class Katana implements Edge<E>
	{
		private E e;
		private Position<Katana> location;
		
		private Point from;
		private Point to;
		
		private Position<Katana> toReference;
		private Position<Katana> fromReference;
		
		protected Katana(Point from, Point to, E e)
		{
			this.from = from;
			this.to = to;
			this.e = e;
		}
		
		@Override
		public E getElement() {
			return e;
		}
		
		protected E setElement(E e)
		{
			E oldE = this.e;
			this.e = e;
			return oldE;
		}
		
		protected Point getFrom()
		{
			return from;
		}
		
		protected Point getTo()
		{
			return to;
		}
		
		protected Position<Katana> getFromReference()
		{
			return fromReference;
		}
		
		protected Position<Katana> getToReference()
		{
			return toReference;
		}
		
		protected Position<Katana> getPosition()
		{
			return location;
		}
		
		protected void setPosition(Position<Katana> p)
		{
			location = p;
		}
		
		protected void setToReference(Position<Katana> to)
		{
			toReference = to;
		}
		
		protected void setFromReference(Position<Katana> from)
		{
			fromReference = from;
		}
	}
}
