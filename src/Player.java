import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

class Player {
	/*
	 * PROPERTIES
	 */
	public static final Edge NOT_FOUND = new Edge(-1, -1);
	public static final int MAX_DEPTH = 3;

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

		// g.print();

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
			while (!queue.isEmpty() && result.equals(NOT_FOUND)) {
				Edge e = queue.poll();
				System.err.println("Examining " + e);

				if (g.isTarget(e.to)) {
					result = e;

					// Debug
					System.err.println("Chose " + e + ", rest of queue was:");
					while (!queue.isEmpty()) {
						Edge edge = queue.poll();
						System.err.println(edge);
					}
					break;
				}
				if (!g.isVisited(e.to)) {
					g.setVisited(e.to);
				}

				// Explore the rest of the edges
				PriorityQueue<Edge> adj = g.getEdges(e.to);
				while (!adj.isEmpty()) {
					Edge edge = adj.poll();
					if (!g.isVisited(edge.to))
						queue.add(edge);
				}
			}
			g.resetVisited();

			// Cut out the last edge in the path to closest node
			if (!result.equals(NOT_FOUND)) {
				g.removeEdge(result.from, result.to);
				System.out.println(result.from + " " + result.to);
			} else
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
	public float heuristic(int index, int depth, int maxDepth) {
		if (depth >= maxDepth || isTarget(index)) {
			if (isTarget(index))
				return (2 / (float) depth);
			else
				return 0;
		} else {
			List<Integer> adj = getAdjacentNodes(index);
			float result = 0;
			for (Integer k : adj) {
				if (isTarget(k))
					result += (1 / (float) depth);
				else
					result += heuristic(k, depth + 1, maxDepth);
			}
			return (result / (float) depth);
		}
	}

	public float heuristic(int index, int depth) {
		return heuristic(index, depth, Player.MAX_DEPTH);
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

	/**
	 * Return adjacent edges, sorted by priority
	 * 
	 * @param index
	 * @return
	 */
	public PriorityQueue<Edge> getEdges(int from) {
		List<Integer> neighbors = adj.get(from);
		PriorityQueue<Edge> q = new PriorityQueue<Edge>();

		for (Integer to : neighbors) {
			q.add(new Edge(from, to, heuristic(to, 1)));
		}
		return q;
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
	public float priority;

	Edge(int from, int to, float priority) {
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
			Edge other = (Edge) o;
			return (other.from == from && other.to == to);
		}
	}

	@Override
	public String toString() {
		return from + " -> " + to + "(" + priority + ")";
	}

	@Override
	public int compareTo(Edge o) {
		// System.err.println("Comparing " + to + "(" + priority + ") to " +
		// o.to + "(" + o.priority + ")");
		return (int) (o.priority - this.priority);
	}
}