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

	/* 
	 * METHODS
	 */
	public Graph(int n) {
		this.n = n;
		adj = new ArrayList<List<Integer>>(n);
		targets = new boolean[n];
		for (int i = 0; i < n; ++i) {
			adj.add(new ArrayList<Integer>());
			targets[i] = false;
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
	public void setAsTarget(int index) {
		targets[index] = true;
	}
}
