package main;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	/*
	 * PROPERTIES
	 */
	protected int n;
	protected List<List<Integer>> adj;
	protected boolean[] targets;
	protected boolean[] visited;

	/* 
	 * METHODS
	 */
	public Graph(int n) {
		this.n = n;
		adj = new ArrayList<List<Integer>>(n);
		targets = new boolean[n];
		visited = new boolean[n];
		for (int i = 0; i < n; ++i) {
			adj.add(new ArrayList<Integer>());
			targets[i] = false;
			visited[i] = false;
		}
		
	}
	
	public void print() {
		for (int i = 0; i < n; ++i) {
			if (targets[i])
				System.out.print("[target] ");
			System.out.print("Node " + i + " is linked to: ");
			for (Integer k : adj.get(i))
				System.out.print(k + ", ");
			System.out.println("");
		}
	}
	
	/*
	 * GETTERS & SETTERS
	 */
	public void addEdge(int from, int to) {
		adj.get(from).add(to);
		adj.get(to).add(from);
	}
	public void removeEdge(Integer from, Integer to) {
		adj.get(from).remove(to);
		adj.get(to).remove(from);
	}
	public void setAsTarget(int index) {
		targets[index] = true;
	}
	public void setVisited(int index) {
		visited[index] = true;
	}
	public void resetVisited() {
		for (int i = 0; i < n; ++i)
			visited[i] = false;
	}
	
	public List<Integer> getAdjacentNodes(int index) {
		return adj.get(index);
	}
	public boolean isTarget(int index) {
		return targets[index];
	}
	public boolean isVisited(int index) {
		return visited[index];
	}
}

class Edge {
	public int from, to;
	
	Edge(int from, int to) {
		this.from = from;
		this.to = to;
	}
	// Starting edge in a path
	Edge(int to) {
		this(-1, to);
	}
	
	public Edge copy() {
		return new Edge(from, to);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Edge))
			return false;
		else {
			Edge other = (Edge)o;
			return (other.from == from && other.to == to);
		}
	}
	@Override
	public String toString() {
		return from + " -> " + to;
	}
}
