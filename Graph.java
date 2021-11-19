import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Graph.java created by Zhengjia Mao on Macbook Pro in p4
 *
 * Author: 	 Zhengjia Mao(zmao27@wisc.edu)
 * Date: 	 @date 04/2/2020
 * 
 * Course: 	 CS400
 * Semester: Spring 2020
 * Lecture:	 002
 * 
 * IDE:		 Eclipse IDE for Java Developers
 * Version:  2019-12 (4.14.0)
 * Build id: 20191212-1212
 *
 * Device: 	 Zane's MacBook Pro
 * OS: 		 macOS Mojave
 * Version:  10.14.6
 * OS Build: 18G95
 *
 * List Collaborators: NONE
 *
 * Other Credits: NONE
 *
 * Known Bugs: NONE
 */

/**
 * Filename: Graph.java Project: p4 Authors: zmao27
 * 
 * Directed and unweighted graph implementation
 */
public class Graph implements GraphADT {

	/**
	 * private fields
	 */
	private List<Graphnode> vertexList;
	private List<Edge> edgeList;
	private List<String> storage;
	private List<String> edgeStorage;
	private List<String> dfs = new ArrayList<>();
	private int numVertex;
	private int numEdge;

	/**
	 * Graphnode - an inner class that represents a single vertex node that stores
	 * the data information and the adjacent nodes
	 * 
	 * @author zmao27 (2020)
	 *
	 */
	private class Graphnode {
		private String data;
		private List<Graphnode> successors;
		private List<Graphnode> predecessors;
		private String mark;

		/**
		 * default no-arg constructor
		 */
		private Graphnode() {
			data = null;
			successors = new ArrayList<Graphnode>();
			predecessors = new ArrayList<Graphnode>();
			mark = "UNVISITED";
		}

		/**
		 * constructor with args
		 */
		private Graphnode(String data) {
			this.data = data;
			successors = new ArrayList<Graphnode>();
			predecessors = new ArrayList<Graphnode>();
			mark = "UNVISITED";
		}
	}

	/**
	 * Edge - an inner class that represents a directional edge from vertex1 to
	 * vertex2
	 * 
	 * @author zmao27 (2020)
	 *
	 */
	private class Edge {
		private String vertex1;
		private String vertex2;

		private Edge(String vertex1, String vertex2) {
			this.vertex1 = vertex1;
			this.vertex2 = vertex2;
		}

		private String direction() {
			return (vertex1 + vertex2);
		}
	}

	/*
	 * Default no-argument constructor
	 */
	public Graph() {
		vertexList = new ArrayList<Graphnode>();
		edgeList = new ArrayList<Edge>();
		storage = new ArrayList<String>();
		edgeStorage = new ArrayList<String>();
		numVertex = 0;
		numEdge = 0;
	}

	/**
	 * Add new vertex to the graph.
	 *
	 * If vertex is null or already exists, method ends without adding a vertex or
	 * throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in
	 * the graph
	 * 
	 * @param vertex the vertex to be added
	 */
	@Override
	public void addVertex(String vertex) {
		if (vertex == null) {
			return;
		}
		if (vertexList.isEmpty()) {
			vertexList.add(new Graphnode(vertex));
			storage.add(vertex);
			numVertex++;
			return;
		}
		if (storage.contains(vertex)) {
			return;
		}
		vertexList.add(new Graphnode(vertex));
		storage.add(vertex);
		numVertex++;
	}

	/**
	 * Remove a vertex and all associated edges from the graph.
	 * 
	 * If vertex is null or does not exist, method ends without removing a vertex,
	 * edges, or throwing an exception.
	 * 
	 * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in
	 * the graph
	 * 
	 * @param vertex the vertex to be removed
	 */
	@Override
	public void removeVertex(String vertex) {
		if (vertex == null) {
			return;
		}
		if (vertexList.isEmpty()) {
			return;
		}
		if (!storage.contains(vertex)) {
			return;
		}
		vertexList.remove(storage.indexOf(vertex));
		storage.remove(vertex);
		numVertex--;
	}

	/**
	 * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and
	 * unweighted)
	 * 
	 * If either vertex does not exist, VERTEX IS ADDED and then edge is created. No
	 * exception is thrown.
	 *
	 * If the edge exists in the graph, no edge is added and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are in
	 * the graph 3. the edge is not in the graph
	 * 
	 * @param vertex1 the first vertex (src)
	 * @param vertex2 the second vertex (dst)
	 */
	@Override
	public void addEdge(String vertex1, String vertex2) {
		if (vertex1 != null && vertex2 != null) {

			if (storage.contains(vertex1) && storage.contains(vertex2)) {
				if (vertex1.equals(vertex2)) {
					return;
				}
				if (edgeStorage.contains(vertex1 + vertex2)) {
					return;
				} else {
					Edge edge = new Edge(vertex1, vertex2);
					edgeList.add(edge);
					edgeStorage.add(edge.direction());
					int i1 = storage.indexOf(vertex1);
					int i2 = storage.indexOf(vertex2);
					vertexList.get(i1).successors.add(vertexList.get(i2));
					vertexList.get(i2).predecessors.add(vertexList.get(i1));
					numEdge++;
				}
			} else if (storage.contains(vertex1)) {
				addVertex(vertex2);
				Edge edge = new Edge(vertex1, vertex2);
				edgeList.add(edge);
				edgeStorage.add(edge.direction());
				int i1 = storage.indexOf(vertex1);
				int i2 = storage.indexOf(vertex2);
				vertexList.get(i1).successors.add(vertexList.get(i2));
				vertexList.get(i2).predecessors.add(vertexList.get(i1));
				numEdge++;
			} else if (storage.contains(vertex2)) {
				addVertex(vertex1);
				Edge edge = new Edge(vertex1, vertex2);
				edgeList.add(edge);
				edgeStorage.add(edge.direction());
				int i1 = storage.indexOf(vertex1);
				int i2 = storage.indexOf(vertex2);
				vertexList.get(i1).successors.add(vertexList.get(i2));
				vertexList.get(i2).predecessors.add(vertexList.get(i1));
				numEdge++;
			} else {
				addVertex(vertex1);
				addVertex(vertex2);
				Edge edge = new Edge(vertex1, vertex2);
				edgeList.add(edge);
				edgeStorage.add(edge.direction());
				int i1 = storage.indexOf(vertex1);
				int i2 = storage.indexOf(vertex2);
				vertexList.get(i1).successors.add(vertexList.get(i2));
				vertexList.get(i2).predecessors.add(vertexList.get(i1));
				numEdge++;
			}

		}
	}

	/**
	 * Remove the edge from vertex1 to vertex2 from this graph. (edge is directed
	 * and unweighted) If either vertex does not exist, or if an edge from vertex1
	 * to vertex2 does not exist, no edge is removed and no exception is thrown.
	 * 
	 * Valid argument conditions: 1. neither vertex is null 2. both vertices are in
	 * the graph 3. the edge from vertex1 to vertex2 is in the graph
	 * 
	 * @param vertex1 the first vertex
	 * @param vertex2 the second vertex
	 */
	@Override
	public void removeEdge(String vertex1, String vertex2) {
		if (vertex1 != null && vertex2 != null) {
			if (storage.contains(vertex1) && storage.contains(vertex2)) {
				if (vertex1.equals(vertex2)) {
					return;
				}
				if (edgeStorage.contains(vertex1 + vertex2)) {
					edgeList.remove(edgeStorage.indexOf(vertex1 + vertex2));
					edgeStorage.remove(vertex1 + vertex2);
					int i1 = storage.indexOf(vertex1);
					int i2 = storage.indexOf(vertex2);
					vertexList.get(i1).successors.remove(vertexList.get(i2));
					vertexList.get(i2).predecessors.add(vertexList.get(i1));
					numEdge--;
				}
			}
		}
	}

	/**
	 * Returns a Set that contains all the vertices
	 * 
	 * @return a Set<String> which contains all the vertices in the graph
	 */
	@Override
	public Set<String> getAllVertices() {
		Set<String> s = new HashSet<String>();
		for (int i = 0; i < vertexList.size(); i++) {
			s.add(vertexList.get(i).data);
		}
		return s;
	}

	/**
	 * Returns a Set that contains all the edges
	 * 
	 * @return a Set<String> which contains all the edges in the graph
	 */
	public Set<String> getAllEdges() {
		Set<String> s = new HashSet<String>();
		for (int i = 0; i < edgeList.size(); i++) {
			// System.out.println(edgeList.get(i).direction());
			s.add(edgeList.get(i).direction());
		}
		return s;
	}

	/**
	 * Algorithm to detect cycles from the given vertex, credits to
	 * https://pages.cs.wisc.edu/~deppeler/cs400/readings/Graphs/
	 * 
	 * @param vertex
	 * @return true if has a cycle from the given vertex
	 */
	public boolean hasCycle(String vertex) {
		Graphnode node = vertexList.get(storage.indexOf(vertex));
		node.mark = "IN_PROGRESS";
		for (Graphnode m : node.successors) {
			if (m.mark.equals("IN_PROGRESS")) {
				return true;
			}
			if (!m.mark.equals("DONE")) {
				if (hasCycle(m.data)) {
					return true;
				}
			}
		}
		node.mark = "DONE";
		return false;
	}

	/**
	 * Algorithm to detect cycles in the graph, credits to
	 * https://pages.cs.wisc.edu/~deppeler/cs400/readings/Graphs/
	 * 
	 * @return true if the graph has cycles
	 */
	public boolean graphHasCycle() {
		reset();
		for (Graphnode node : vertexList) {
			if (node.mark.equals("UNVISITED")) {
				if (hasCycle(node.data)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Mark all vertexs as unvisited
	 */
	private void reset() {
		for (Graphnode node : vertexList) {
			node.mark = "UNVISITED";
		}
	}

	/**
	 * Get all the neighbor (adjacent-dependencies) of a vertex
	 * 
	 * For the example graph, A->[B, C], D->[A, B] getAdjacentVerticesOf(A) should
	 * return [B, C].
	 * 
	 * In terms of packages, this list contains the immediate dependencies of A and
	 * depending on your graph structure, this could be either the predecessors or
	 * successors of A.
	 * 
	 * @param vertex the specified vertex
	 * @return an List<String> of all the adjacent vertices for specified vertex
	 */
	@Override
	public List<String> getAdjacentVerticesOf(String vertex) {
		List<String> l = new ArrayList<String>();
		if (vertex == null) {
			return null;
		}
		if (vertexList.isEmpty()) {
			return null;
		}
		if (storage.contains(vertex)) {
			Graphnode node = vertexList.get(storage.indexOf(vertex));
			for (int i = 0; i < node.successors.size(); i++) {
				l.add(node.successors.get(i).data);
			}
			return l;
		} else {
			return null;
		}
	}

	/**
	 * Get all the predecessor of a vertex
	 * 
	 * @param vertex
	 * @return an List<String> of all the predecessor vertices for specified vertex
	 */
	public List<String> getPredecessorsOf(String vertex) {
		List<String> l = new ArrayList<String>();
		if (vertex == null) {
			return null;
		}
		if (vertexList.isEmpty()) {
			return null;
		}
		if (storage.contains(vertex)) {
			Graphnode node = vertexList.get(storage.indexOf(vertex));
			for (int i = 0; i < node.predecessors.size(); i++) {
				l.add(node.predecessors.get(i).data);
			}
			return l;
		} else {
			return null;
		}
	}

	/**
	 * Return a complete list of string representing a directed order for the whole
	 * graph, from the precedesors to successors
	 * 
	 * @return a list of vertex
	 */
	public List<String> allOder() {
		List<String> order = new ArrayList<>();
		Stack<Graphnode> st = new Stack<Graphnode>();
		reset();
		for (Graphnode n : vertexList) {
			if (n.successors.isEmpty()) {
				n.mark = "VISITED";
				st.push(n);
			}
		}
		while (!st.isEmpty()) {
			Graphnode c = st.peek();
			List<String> temp = OrderFrom(c.data);
			for (String s : temp) {
				if (!order.contains(s)) {
					order.add(s);
				}
			}
			st.pop();
		}
		return order;
	}

	/**
	 * Private recursive helper to traverse the vertex in depth-first search orer
	 * 
	 * @param vertex
	 */
	private void DFS(String vertex) {
		Graphnode v = vertexList.get(storage.indexOf(vertex));
		v.mark = "VISITED";
		dfs.add(v.data);
		for (Graphnode u : v.predecessors) {
			if (dfs.contains(u.data)) {
				dfs.remove(u.data);
				DFS(u.data);
			}
			if (u.mark.equals("UNVISITED")) {
				DFS(u.data);
			}
		}
	}

	/**
	 * Gives a list of string as a directed order that leads to the given vertex.
	 * For example, if A depends on B, OrderFrom(A) will outputs [B, A]
	 */
	public List<String> OrderFrom(String vertex) {
		List<String> reverse = new ArrayList<>();
		dfs.clear();
		reset();
		DFS(vertex);
		for (int i = dfs.size(); i > 0; i--) {
			reverse.add(dfs.get(i - 1));
		}
		return reverse;
	}

	/**
	 * Returns the number of edges in this graph.
	 * 
	 * @return number of edges in the graph.
	 */
	@Override
	public int size() {
		return numEdge;
	}

	/**
	 * Returns the number of vertices in this graph.
	 * 
	 * @return number of vertices in graph.
	 */
	@Override
	public int order() {
		return numVertex;
	}

	public static void main(String[] args) {
	}

}
