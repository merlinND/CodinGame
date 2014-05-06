package main;

import java.util.Scanner;

public class Player {
	/*
	 * PROPERTIES
	 */

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
		
		g.print();
		
		while (true) {
			// Read information from standard input
			int n = in.nextInt();

			// Compute logic here

			// System.err.println("Debug messages...");

			// Write action to standard output
			System.out.println("0 1");
		}
	}

	/*
	 * GETTERS & SETTERS
	 */
}
