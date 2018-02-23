package cs2321;

import java.io.*;
import java.util.Scanner;
import java.util.Comparator;
import java.util.Iterator;
import net.datastructures.*;

/* CS2321 Project: The Labyrinth
 * 
 * Do NOT change the setupLabyrinth function.
 *         
 * Implement the dfsPath, bfsPath, shortestPath, and totalPathDistance functions below.
 *
 */
public class Labyrinth {
	public static final int WALL = 1;
	public static final String PARSE_CHARACTER = ",";

	private Graph<RoomCoordinate, Walkway> mGraph;
	private int mWidth = -1;
	private int mHeight = -1;
	private Comparator<Vertex<RoomCoordinate>> c = new VertexComparator();

	public Labyrinth(String aFileName) {
		mGraph = setupLabyrinth(aFileName);
	}

	/*
	 * Width of the labyrinth (# of squares, not pixels)
	 */
	public int getWidth() {
		return mWidth;
	}

	/*
	 * Height of the labyrinth (# of squares, not pixels)
	 */
	public int getHeight() {
		return mHeight;
	}

	/*
	 * Create the graph based on the maze specification in the input file !!!
	 * Don't Change this method !!!
	 */
	public Graph<RoomCoordinate, Walkway> setupLabyrinth(String aFileName) {
		Scanner lFile = null;
		try {
			lFile = new Scanner(new File(aFileName));
			lFile.useDelimiter(",\n");
		} catch (FileNotFoundException eException) {
			System.out.println(eException.getMessage());
			eException.printStackTrace();
			System.exit(-1);
		}

		//You need to copy your Adjacent Graph implementation to this project. 
		// Otherwise the following line has compiler error because AdjListGraph(...) does not exist.
		Graph<RoomCoordinate, Walkway> lGraph = new AdjListGraph<RoomCoordinate, Walkway>();

		try {
			int lXSize = 0;
			int lYSize = 0;
			if (lFile.hasNext()) {
				String[] lDimensions = lFile.nextLine().split(PARSE_CHARACTER);
				lXSize = Integer.parseInt(lDimensions[0]);
				lYSize = Integer.parseInt(lDimensions[1]);
			}

			mWidth = lXSize;
			mHeight = lYSize;

			/* Create all the room coordinates */
			Vertex<?>[][] lVertices = new Vertex<?>[lXSize][lYSize];
			for (int lYIndex = 0; lYIndex < lYSize; lYIndex++) {
				for (int lXIndex = 0; lXIndex < lXSize; lXIndex++) {
					RoomCoordinate lNextRoomCoordinate = new RoomCoordinate(lXIndex, lYIndex);
					Vertex<RoomCoordinate> lNextRoom = lGraph.insertVertex(lNextRoomCoordinate);
					lVertices[lXIndex][lYIndex] = lNextRoom;
				}
			}

			for (int lYIndex = 0; lYIndex < lYSize; lYIndex++) {
				String[] lWalls = lFile.nextLine().split(PARSE_CHARACTER);
				for (int lXIndex = 0; lXIndex < lXSize; lXIndex++) {
					if (Integer.parseInt(lWalls[lXIndex]) != WALL) {
						Vertex<RoomCoordinate> lVertex1 = (Vertex<RoomCoordinate>) lVertices[lXIndex][lYIndex];
						Vertex<RoomCoordinate> lVertex2 = (Vertex<RoomCoordinate>) lVertices[lXIndex][lYIndex - 1];

						Walkway lNewWalkway = new Walkway(
								lVertex1.getElement().toString() + lVertex2.getElement().toString(),
								Integer.parseInt(lWalls[lXIndex]));
						lGraph.insertEdge(lVertex1, lVertex2, lNewWalkway);
					}
				}
			}

			for (int lYIndex = 0; lYIndex < lYSize; lYIndex++) {
				String[] lWalls = lFile.nextLine().split(PARSE_CHARACTER);
				for (int lXIndex = 0; lXIndex < lXSize; lXIndex++) {
					if (Integer.parseInt(lWalls[lXIndex]) != WALL) {
						Vertex<RoomCoordinate> lVertex1 = (Vertex<RoomCoordinate>) lVertices[lXIndex][lYIndex];
						Vertex<RoomCoordinate> lVertex2 = (Vertex<RoomCoordinate>) lVertices[lXIndex - 1][lYIndex];

						Walkway lNewWalkway = new Walkway(
								lVertex1.getElement().toString() + lVertex2.getElement().toString(),
								Integer.parseInt(lWalls[lXIndex]));
						lGraph.insertEdge(lVertex1, lVertex2, lNewWalkway);
					}
				}
			}
		} catch (Exception eException) {
			System.out.println(eException.getMessage());
			eException.printStackTrace();
			System.exit(-1);
		}

		return lGraph;
	}
	
	/**
	 * Orders the edges surrounding the given Vertex in the order of
	 * NORTH, EAST, SOUTH, WEST
	 * @param v - the source vertex
	 * @return an iterable containing the edges surrounding the given vertex
	 * 	in cardinal order
	 */
	@TimeComplexity("O(1)")
	private Iterable<Edge<Walkway>> reorderEdges(Vertex<RoomCoordinate> v)
	{
		DoublyLinkedList<Edge<Walkway>> ordered = new DoublyLinkedList<Edge<Walkway>>();
		Edge<Walkway> north = null, east = null, south = null, west = null;
		for(Edge<Walkway> e : mGraph.outgoingEdges(v))
		{
			switch(getDirection(v, mGraph.opposite(v, e)))
			{
			case NORTH:
				north = e;
				break;
			case EAST:
				east = e;
				break;
			case SOUTH:
				south = e;
				break;
			case WEST:
				west = e;
				break;
			}
		}
		if(north != null)
		{
			ordered.addLast(north);
		}
		if(east != null)
		{
			ordered.addLast(east);
		}
		if(south != null)
		{
			ordered.addLast(south);
		}
		if(west != null)
		{
			ordered.addLast(west);
		}
		return ordered;
	}
	
	/**
	 * Returns the direction of the two vertices in relation to eachother. 
	 * @param a - the source vertex
	 * @param b - the target vertex
	 * @return the direction of vertex b in relation to vertex a
	 */
	@TimeComplexity("O(1)")
	private Direction getDirection(Vertex<RoomCoordinate> a, Vertex<RoomCoordinate> b)
	{
		int aX = a.getElement().getX(), aY = a.getElement().getY(), 
				bX = b.getElement().getX(), bY = b.getElement().getY();
		if(bX == aX && bY == (aY - 1))
		{
			return Direction.NORTH;
		}
		else if(bX == (aX + 1) && bY == aY)
		{
			return Direction.EAST;
		}
		else if(bX == aX && bY == (aY + 1))
		{
			return Direction.SOUTH;
		}
		else if(bX == (aX - 1) && bY == aY)
		{
			return Direction.WEST;
		}
		return null;
	}
	
	/**
	 * Constructs an iterable containing the walkways leading from vertex u to vertex v
	 * @param u - the first vertex in the path
	 * @param v - the last vertex in the path
	 * @param forest - the collection of found vertex-edge connections
	 * @return a path leading from vertex u to v
	 */
	@TimeComplexity("O(n)")
	private Iterable<Edge<Walkway>> constructPath(Vertex<RoomCoordinate> u, 
			Vertex<RoomCoordinate> v, HashMap<Vertex<RoomCoordinate>, Edge<Walkway>> forest)
	{
		/* TCJ
		 * This method must iterate through the desired path. 
		 */
		DoublyLinkedList<Edge<Walkway>> path = new DoublyLinkedList<Edge<Walkway>>();
		Vertex<RoomCoordinate> end = v;
		while(end != u)
		{
			Edge<Walkway> e = forest.get(end);
			path.addFirst(e);
			end = mGraph.opposite(end, e);
		}
		return path;
	}
	
	/**
	 * Finds the vertex corresponding to the given coordinates
	 * @param r - the coordinates of the desired vertex
	 * @return the desired vertex
	 */
	@TimeComplexity("O(n)")
	private Vertex<RoomCoordinate> findVertex(RoomCoordinate r)
	{
		/* TCJ
		 * This method must iterate through every
		 * vertex in the graph. 
		 */
		for(Vertex<RoomCoordinate> v : mGraph.vertices())
		{
			if(v.getElement().getX() == r.getX() && v.getElement().getY() == r.getY())
			{
				return v;
			}
		}
		return null;
	}

	/**
	 * Complete the dfsPath function by implementing a Depth First Search
	 * algorithm to find a path from start to end. At each vertex, the adjacent
	 * edges shall be searched in the order of NORTH, EAST, SOURTH and WEST. 
	 * @param a    an RoomCoordinate object represents the start location, 
	 * 				this object does not exist in the graph 
	 * @param b    an RoomCoordinate object represents the start location, 
	 * 				this object does not exist in the graph 
	 * @return 		Return the sequence of edges traversed in order to get from the
	 * 				start to the finish locations. If there is
	 * 				NO path, do NOT return null. Return an empty sequence. 
	 */
	@TimeComplexity("O(n + m)")
	public Iterable<Edge<Walkway>> dfsPath(RoomCoordinate a, RoomCoordinate b) {
		/* TCJ 
		 * This method calls bfsPath, which runs in 
		 * O(n + m).
		 */
		Vertex<RoomCoordinate> start = findVertex(a);
		Vertex<RoomCoordinate> end = findVertex(b);
		HashMap<Vertex<RoomCoordinate>, Boolean> visited = new HashMap<Vertex<RoomCoordinate>, Boolean>(c);
		HashMap<Vertex<RoomCoordinate>, Edge<Walkway>> forest = new HashMap<Vertex<RoomCoordinate>, Edge<Walkway>>(c);
		
		boolean hasPath = dfsPath(start, end, visited, forest);
		return (hasPath) ? constructPath(start, end, forest) : new DoublyLinkedList<Edge<Walkway>>();
	}
	
	/**
	 * Recursively searches through the graph for the given end vertex
	 * using a depth-first search algorithm. 
	 * @param u - The current vertex. 
	 * @param end - the vertex to search for
	 * @param visited - a HashMap that keeps track of all visited vertices
	 * @param forest - a HashMap that keeps track of the edges used to visit each vertex
	 * @return true if the end vertex can be reached, false otherwise
	 */
	@TimeComplexity("O(n + m)")
	private boolean dfsPath(Vertex<RoomCoordinate> u, Vertex<RoomCoordinate> end, 
			HashMap<Vertex<RoomCoordinate>, Boolean> visited, HashMap<Vertex<RoomCoordinate>, Edge<Walkway>> forest)
	{
		/* TCJ
		 * This method recursively iterates through
		 * n vertices and m edges. 
		 */
		visited.put(u, true);
		for(Edge<Walkway> e : reorderEdges(u))
		{
			Vertex<RoomCoordinate> v = mGraph.opposite(u, e);
			if(visited.get(v) == null)
			{
				forest.put(v, e);
				if(v.equals(end))
				{
					return true;
				}
				if(dfsPath(v, end, visited, forest)){
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * Complete the dfsPath function by implementing a Breadth First Search
	 * algorithm to find a path from start to end. At each vertex, the adjacent
	 * edges shall be searched in the order of NORTH, EAST, SOURTH and WEST. 
	 * @param a	   an RoomCoordinate object represents the start location, 
	 * 				this object does not exist in the graph 
	 * @param b    an RoomCoordinate object represents the start location, 
	 * 				this object does not exist in the graph 
	 * @return 		Return the sequence of edges traversed in order to get from the
	 * 				start to the finish locations. If there is
	 * 				NO path, do NOT return null. Return an empty sequence. 
	 */
	@TimeComplexity("O(n + m")
	public Iterable<Edge<Walkway>> bfsPath(RoomCoordinate a, RoomCoordinate b) {
		/* TCJ 
		 * This method calls bfsPath, which runs in 
		 * O(n + m).
		 */
		Vertex<RoomCoordinate> start = findVertex(a);
		Vertex<RoomCoordinate> end = findVertex(b);
		HashMap<Vertex<RoomCoordinate>, Edge<Walkway>> forest = new HashMap<Vertex<RoomCoordinate>, Edge<Walkway>>(c);
		boolean hasPath = bfsPath(start, end, forest);
		return (hasPath) ? constructPath(start, end, forest) : new DoublyLinkedList<Edge<Walkway>>();
	}
	
	/**
	 * Iteratively searches through the graph for the given end vertex
	 * using a breadth-first search algorithm. 
	 * @param start - the first vertex
	 * @param end - the vertex to search for
	 * @param forest - a HashMap that keeps track of the edges used to visit each vertex
	 * @return true if the end vertex can be reached, false otherwise
	 */
	@TimeComplexity("O(n + m)")
	private boolean bfsPath(Vertex<RoomCoordinate> start, Vertex<RoomCoordinate> end, 
			HashMap<Vertex<RoomCoordinate>, Edge<Walkway>> forest)
	{
		/* TCJ
		 * This method iterates through n vertices and
		 * m edges. 
		 */
		HashMap<Vertex<RoomCoordinate>, Boolean> visited = new HashMap<Vertex<RoomCoordinate>, Boolean>(c);
		DoublyLinkedList<Vertex<RoomCoordinate>> queue = new DoublyLinkedList<Vertex<RoomCoordinate>>();
		boolean hasPath = false;
		
		visited.put(start, true);
		queue.addLast(start);
		while(!queue.isEmpty())
		{
			Vertex<RoomCoordinate> u = queue.removeFirst();
			for(Edge<Walkway> e : reorderEdges(u))
			{
				Vertex<RoomCoordinate> w = mGraph.opposite(u, e);
				if(visited.get(w) == null)
				{
					visited.put(w, true);
					queue.addLast(w);
					forest.put(w, e);
					if(w.equals(end))
					{
						hasPath = true;
					}
				}
			}
		}
		return hasPath;
	}


	/**
	 * Complete the dfsPath function by implementing Dijkstra's
	 * algorithm to find a path from start to end. At each vertex, the adjacent
	 * edges shall be searched in the order of NORTH, EAST, SOURTH and WEST. 
	 * @param a an RoomCoordinate object represents the start location, 
	 * 				this object does not exist in the graph 
	 * @param b  an RoomCoordinate object represents the start location, 
	 * 				this object does not exist in the graph 
	 * @return 		Return the sequence of edges traversed in order to get from the
	 * 				start to the finish locations. If there is
	 * 				NO path, do NOT return null. Return an empty sequence. 
	 */
	@TimeComplexity("(n+m)lg n")
	public Iterable<Edge<Walkway>> shortestPath(RoomCoordinate a, RoomCoordinate b) {
		/* TCJ 
		 * The two outer for loops iterate through n vertices,
		 * calling the priority queue's insert/remove
		 * methods (which runs in O(lg n)) for each iteration. 
		 * The relaxation procedure also goes over m edges, calling
		 * the replace method (which runs in O(lg n)). 
		 */
		Vertex<RoomCoordinate> start = findVertex(a);
		Vertex<RoomCoordinate> end = findVertex(b);
		
		HashMap<Vertex<RoomCoordinate>, Integer> distances = new HashMap<Vertex<RoomCoordinate>, Integer>(c);
		HeapPQ<Integer, Vertex<RoomCoordinate>> pq = new HeapPQ<Integer, Vertex<RoomCoordinate>>();
		HashMap<Vertex<RoomCoordinate>, Entry<Integer, Vertex<RoomCoordinate>>> pqTokens = new HashMap<Vertex<RoomCoordinate>, Entry<Integer, Vertex<RoomCoordinate>>>(c);
		HashMap<Vertex<RoomCoordinate>, Integer> cloud = new HashMap<Vertex<RoomCoordinate>, Integer>(c);
		HashMap<Vertex<RoomCoordinate>, Edge<Walkway>> forest = new HashMap<Vertex<RoomCoordinate>, Edge<Walkway>>(c);
		
		for(Vertex<RoomCoordinate> v : mGraph.vertices())
		{
			int distance;
			if(v.equals(start))
			{
				distance = 0;
			}
			else
			{
				distance = Integer.MAX_VALUE;
			}
			distances.put(v, distance);
			pqTokens.put(v, pq.insert(distance, v));
		}
		while(!pq.isEmpty())
		{
			Entry<Integer, Vertex<RoomCoordinate>> entry = pq.removeMin();
			int distance = entry.getKey();
			Vertex<RoomCoordinate> u = entry.getValue();
			cloud.put(u, distance);
			pqTokens.remove(u);
			if(u.equals(end))
			{
				break;
			}
			for(Edge<Walkway> e : reorderEdges(u))
			{
				Vertex<RoomCoordinate> w = mGraph.opposite(u, e);
				if(cloud.get(w) == null && e != null)
				{
					int newDistance = distances.get(u) + e.getElement().getDistance();
					if(newDistance < distances.get(w))
					{
						distances.put(w, newDistance);
						forest.put(w, e);
						pq.replaceKey(pqTokens.get(w), newDistance);
					}
				}
			}
		}
		return (cloud.get(end) != null) ? constructPath(start, end, forest) : new DoublyLinkedList<Edge<Walkway>>();
	}

	/*
	 * Complete the totalPathDistance function, which calculates how far the
	 * given path traverses.
	 */
	@TimeComplexity("O(n)")
	public static double totalPathDistance(Iterable<Edge<Walkway>> path) {
		/* TCJ
		 * This algorithm iterates through the
		 * length of the given path. 
		 */
		double distance = 0.0;
		for(Edge<Walkway> e : path)
		{
			int weight = e.getElement().getDistance();
			distance += weight;
		}
		return distance;
	}

	public static void main(String[] aArguments) {
		Labyrinth lLabyrinth = new Labyrinth("SmallLabyrinth.txt");

		// TODO: Testing
	}

	private enum Direction
	{
		NORTH, EAST, SOUTH, WEST;
	}
	
	private class VertexComparator implements Comparator<Vertex<RoomCoordinate>>
	{

		@Override
		public int compare(Vertex<RoomCoordinate> a, Vertex<RoomCoordinate> b) {
			int aX = a.getElement().getX(), aY = a.getElement().getY(),
						bX = b.getElement().getX(), bY = b.getElement().getY();
			if(aX == bX && aY == bY)
			{
				return 0;
			}
			else if(aX < bX || aY < bY)
			{
				return -1;
			}
			else
			{
				return 1;
			}
		}
	}
}
