package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Player {
	/*
	 * PROPERTIES
	 */
	public static final Edge NOT_FOUND = new Edge(-1, -1);

	/*
	 * METHODS
	 */
	public static void main(String args[]) {
		int nNodes, nLinks, nTargets;

		// ----- Init
		Scanner in = new Scanner(System.in);
		nNodes = in.nextInt();
		nLinks = in.nextInt();
		nTargets = in.nextInt();
		
		Graph g = new Graph(nNodes);
		for (int i = 0; i < nLinks; ++i) {
			int from = in.nextInt();
			int to = in.nextInt();
			g.addEdge(from, to);
		}
		for (int i = 0; i < nTargets; ++i) {
			int index = in.nextInt();
			g.setAsTarget(index);
		}
		
		//g.print();
		
		while (true) {
			// Current position in the graph
			// We suppose that `position` is not a target node,
			// otherwise we've lost already.
			int position = in.nextInt();
			// Result
			Edge result = NOT_FOUND.copy();
			
			// Find the closest target node using breadth-first-search
			Queue<Edge> queue = new LinkedList<Edge>();
			queue.add(new Edge(position));
			g.setVisited(position);
			while(!queue.isEmpty() && result.equals(NOT_FOUND)) {
				Edge current = queue.poll();
				
				List<Integer> adj = g.getAdjacentNodes(current.to);
				for (Integer k : adj) {
					if (!g.isVisited(k)) {
						g.setVisited(k);
						if (g.isTarget(k)) {
							queue.clear();
							result.from = current.to;
							result.to = k;
							break;
						}
						queue.add(new Edge(current.to, k));
					}
				}
			}
			g.resetVisited();
			
			// Cut out the last edge in the path to closest node
			if (!result.equals(NOT_FOUND)) {
				g.removeEdge(result.from, result.to);
				System.out.println(result.from + " " + result.to);
			}
			else
				System.out.println("No result found.");
		}
	}

	/*
	 * GETTERS & SETTERS
	 */
}
