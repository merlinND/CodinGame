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

class Wall {
	boolean[][] flags;
	Position max;
	int n;
	
	public Wall(Position max) {
		this.max = max;
		
		this.flags = new boolean[max.x + 1][max.y + 1];
		for(int x = 0; x <= max.x; x++) {
			for(int y = 0; y <= max.y; y++) {
				flags[x][y] = true;
				n++;
			}
		}
	}
	
	public void print() {
		for(int y = 0; y <= max.y; y++) {
			for(int x = 0; x <= max.x; x++) {
				if (flags[x][y])
					System.err.print("1");
				else
					System.err.print("0");
			}
			System.err.println(" ");
		}
	}
	
	public void eliminate(Position good, Position bad) {
		// Eliminate all windows that are closer to bad than to good
		for(int x = 0; x <= max.x; x++) {
			for(int y = 0; y <= max.y; y++) {
				Position there = new Position(x, y);
				if(distance(there, bad) < distance(there, good) && flags[x][y]) { //distanceToGood > d && 
					flags[x][y] = false;
					n--;
				}
			}
		}
	}
	
	public Position choose() {
		return choose(0.5);
	}
	
	public Position choose(double criterion) {
		Position lastSeen = new Position();
		int i = this.n;
		for(int x = 0; x <= max.x; x++) {
			for(int y = 0; y <= max.y; y++) {
				if(flags[x][y]) {
					i--;
					lastSeen.x = x;
					lastSeen.y = y;
					if(i <= Math.ceil(n * criterion)) {
						return lastSeen;
					}
				}
			}
		}
		return lastSeen;
	}
	
	public static double distance(Position a, Position b) {
		return Math.sqrt( Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) );
	}
}

class Player {
	
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
		
		Wall wall = new Wall(max);
		
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
            	wall.eliminate(good, bad);
            	
            	wall.print();
            }
            
            // Choose "center" window in remaining possibilities
            previous = current;
            current = wall.choose();
            
            // Avoid staying put
            if(current.equals(previous)) {
            	current = wall.choose(Math.random());
            	System.err.println("Shuffled to " + current);
            }
            
            // Write action to standard output
            System.out.println(current.x + " " + current.y);
        }
	}
}
