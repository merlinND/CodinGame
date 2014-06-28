import java.util.*;
import java.io.*;
import java.math.*;

class Point {
    public int x, y;
    
    Point() {
        this(0, 0);
    }
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Returns the angle to upwards vertical
    // that a vector from this point to end point makes
    // (in degrees)
    public int angle(Point end) {
        double rad = (Math.PI/2) - Math.atan2(end.y - this.y, end.x - this.x);
        int deg = (int) (rad * (180d / Math.PI));
        return - deg;
    }
    
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}

class Move {
    public int rotation, power;
    
    Move() {
        this(0, 0);
    }
    Move (int r, int pow) {
        this.rotation = r;
        this.power = pow;
    }
    
    public void print() {
        System.out.println(this.rotation + " " + this.power);
    }
    public String toString() {
        return "Rotation: " + this.rotation + ", power: " + this.power;
    }
}

class MarsLander {
    protected static final int MAX_POWER = 4;
    protected static final int MAX_LANDING_VS = 40;
    
    protected Point position;;
    protected int hs, vs, fuel, rotation, power;
    
    MarsLander() {
        update(new Point(), 0, 0, 0, 0, 0);
    }
    
    public void update(Point p, int hs, int vs, int f, int r, int pow) {
        this.position = p;
        this.hs = hs;
        this.vs = vs;
        this.fuel = fuel;
        this.rotation = r;
        this.power = pow;
    }
    
    public Move moveToTarget(Point targetBegin, Point targetEnd) {
        Point middle = new Point(targetBegin.x + (targetEnd.x - targetBegin.x)/2, targetBegin.y);
        int angle = 0,
            power = 0;
        
        // 1. We need to be above the target zone
        if (this.position.x < targetBegin.x || this.position.x > targetEnd.x) {
            // Choose orientation and power to move towards target zone
            angle = - middle.angle(this.position);
            
            int distanceLeft = Math.abs(middle.x - this.position.x);
            power = MAX_POWER;
            if (distanceLeft < MAX_POWER) {
                power = distanceLeft;
            }
            return new Move(angle, power);
        }
        // 2. When above, go to vertical position and reduce vertical speed
        else {
            if (Math.abs(this.vs) > MAX_LANDING_VS)
                power = MAX_POWER;
            else {
                power = (int) (MAX_POWER * ((MAX_LANDING_VS - Math.abs(this.vs)) / (double)MAX_LANDING_VS));
            }
        }
        
        return new Move(angle, power);
    }
}

class Player {
    // Target zone start and end
    protected Point targetBegin, targetEnd;
    
    protected MarsLander lander;
    
    Player(Scanner in) {
        // We need to find the target zone
        int n = in.nextInt();
        Point previous = new Point(-1000, -1000);
        Point current;
        for (int i = 0; i < n; ++i) {
            current = new Point(in.nextInt(), in.nextInt());
            if (current.y == previous.y) {
                targetBegin = previous;
                targetEnd = current;
            }
            previous = current;
        }
        
        System.err.println("Target zone : " + targetBegin + "->" + targetEnd);
        
        // Create the Mars Lander
        this.lander = new MarsLander();
    }
    
    // At each new turn
    public void update(Point p, int hs, int vs, int f, int r, int pow) {
        this.lander.update(p, hs, vs, f, r, pow);
    }
    
    public Move getNextMove() {
        return lander.moveToTarget(targetBegin, targetEnd);
    }
    
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // INIT
        Player p = new Player(in);
        
        while (true) {
            // Read information from standard input
            Point position = new Point(in.nextInt(), in.nextInt());
            int hs = in.nextInt(),
                vs = in.nextInt(),
                f = in.nextInt(),
                r = in.nextInt(),
                pow = in.nextInt();
            p.update(position, hs, vs, f, r, pow);
        
            // Compute logic here
            Move nextMove = p.getNextMove();
            System.err.println(nextMove);

            // Write action to standard output
            nextMove.print();
        }
    }
}