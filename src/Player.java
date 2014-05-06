import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

class Player {
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
			PriorityQueue<Edge> queue = new PriorityQueue<Edge>();
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
						queue.add(new Edge(current.to, k, g.adjTargets(k)));
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


class Graph {
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
	
	/**
	 * @param index
	 * @return The number of target nodes adjacent to this node
	 */
	public int adjTargets(int index) {
		List<Integer> adj = getAdjacentNodes(index);
		int result = 0;
		for (Integer k : adj) {
			if (isTarget(k))
				result ++;
		}
		return result;
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

class Edge implements Comparable<Edge> {
	public int from, to;
	public int priority;
	
	Edge(int from, int to, int priority) {
		this.from = from;
		this.to = to;
		this.priority = priority;
	}
	Edge(int from, int to) {
		this(from, to, 0);
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
	@Override
	public int compareTo(Edge o) {
		//System.out.println("Comparing " + to + "(" + priority + ") to " + o.to + "(" + o.priority + ")");
		return (o.priority - this.priority);
	}
}