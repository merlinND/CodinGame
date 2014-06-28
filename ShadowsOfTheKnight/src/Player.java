import java.util.Scanner;

class Position {
	int x, y;
	
	public Position() {
		this.x = this.y = 0;
	}
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Position(Position other) {
		this.x = other.x;
		this.y = other.y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

class Player {
	/*
	 * PROPERTIES
	 */
	
	
	/* 
	 * METHODS
	 */

	

	/*
	 * GETTERS & SETTERS
	 */
	
	
	
	/**
	 * MAIN
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		Position min = new Position();
		Position current = new Position();
		Position max = new Position();
		max.x = in.nextInt() - 1;
		max.y = in.nextInt() - 1;
		int n = in.nextInt();
		current.x = in.nextInt();
		current.y = in.nextInt();
		
		System.err.println("Max is " + max);
		System.err.println("Starting at " + current);
		
        while (true) {
            // Read information from standard input
            String direction = in.next();
            System.err.println(direction);

            // Write action to standard output
            System.out.println("0 0");
        }
	}
}
