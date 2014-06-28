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
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Position))
			return false;
		Position other = (Position)obj;
		return (this.x == other.x && this.y == other.y);
	}
}

class Player {
	
	public static double distance(Position a, Position b) {
		return Math.sqrt( Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) );
	}
	
	public static void printTheWall(boolean[][] wall, Position max) {
		for(int y = 0; y <= max.y; y++) {
			for(int x = 0; x <= max.x; x++) {
				if (wall[x][y])
					System.err.print("1");
				else
					System.err.print("0");
			}
			System.err.println(" ");
		}
	}
	
	public static boolean[][] eliminate(boolean[][] wall, Position max, Position good, Position bad) {
		double d = distance(good, bad);

		// Eliminate all windows that are:
		// - at least at distance d from good
		// - AND closer to bad than to good
		for(int x = 0; x <= max.x; x++) {
			for(int y = 0; y <= max.y; y++) {
				Position there = new Position(x, y);
				double distanceToGood = distance(there, good);
				if(distanceToGood > d && distance(there, bad) <= distanceToGood) {
					wall[x][y] = false;
				}
			}
		}
		return wall;
	}
	
	public static Position choose(boolean[][] wall, Position max) {
		return choose(wall, max, 0.5);
	}
	
	public static Position choose(boolean[][] wall, Position max, double criterion) {
		int n = 0;
		Position lastSeen = new Position();
		for(int x = 0; x <= max.x; x++) {
			for(int y = 0; y <= max.y; y++) {
				if(wall[x][y])
					n++;
			}
		}
		int i = n;
		for(int x = 0; x <= max.x; x++) {
			for(int y = 0; y <= max.y; y++) {
				if(wall[x][y]) {
					i--;
					lastSeen.x = x;
					lastSeen.y = y;
					if(i <= n * criterion) {
						return lastSeen;
					}
				}
			}
		}
		return lastSeen;
	}
	
	/**
	 * MAIN
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		Position current = new Position();
		Position max = new Position();
		max.x = in.nextInt() - 1;
		max.y = in.nextInt() - 1;
		int n = in.nextInt();
		current.x = in.nextInt();
		current.y = in.nextInt();
		Position previous = current;
		
		boolean[][] wall = new boolean[max.x + 1][max.y + 1];
		for(int x = 0; x <= max.x; x++) {
			for(int y = 0; y <= max.y; y++) {
				wall[x][y] = true;
			}
		}
		
		System.err.println("Max is " + max);
		System.err.println("Starting at " + current);
		
		
        while (true) {
            // Read information from standard input
            String direction = in.next();
            
            System.err.println("Previous: " + previous);
            System.err.println("Current: " + current);
            System.err.println(direction);
            
            if(!direction.equals("UNKNOWN")) {
            	Position good, bad;
            	// TODO: check for corectness
            	if(direction.equals("WARMER")) {
            		good = current;
            		bad = previous;
            	}
            	else { // if(direction.equals("COLDER") || direction.equals("SAME"))
            		good = previous;
            		bad = current;
            	}
            	
            	// Eliminate windows
            	wall = eliminate(wall, max, good, bad);
            	printTheWall(wall, max);
            }
            
            // Choose "center" window in remaining possibilities
            previous = current;
            current = choose(wall, max);
            
            // Avoid staying put
            if(current.equals(previous)) {
            	current = choose(wall, max, Math.random());
            	System.err.println("Shuffled to " + current);
            }
            
            // Write action to standard output
            System.out.println(current.x + " " + current.y);
        }
	}
}
