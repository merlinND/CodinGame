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
    
    public void scale(int factor) {
        this.x *= factor;
        this.y *= factor;
    }
    public void translate(Point dv) {
        this.x += dv.x;
        this.y += dv.y;
    }
    
    
    public double length() {
		return Math.sqrt( this.x * this.x + this.y * this.y );
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
    protected static final int MAX_LANDING_HS = 20;
    protected static final int MARGIN = 5;
    
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
        int zoneLength = targetEnd.x - targetBegin.x;
        int angle = 0,
            power = 0;
        
        // 1. We need to be above the target zone
        Point target = new Point( targetBegin.x + (targetEnd.x - targetBegin.x) / 2, targetBegin.y );
        // If horizontal speed is not enough to reach target zone
        if ( !isEnough(targetBegin, targetEnd, zoneLength / 2) ) {
            System.err.println("Not enough horizontal speed !");
            // Choose orientation and power to move towards target zone
            angle = - target.angle(this.position);
            // TODO : make power relative to missing speed
            power = 4;
        }
        // If we're going to miss the zone, cancel some horizontal speed
        else if (isTooMuch(targetBegin, targetEnd)) {
            System.err.println("Too much horizontal speed !");
            // Choose orientation and power to cancel horizontal speed
            Point end = new Point(this.hs, this.vs);
            end.scale(-1);
            end.translate(this.position);
            angle = this.position.angle(end);
            // TODO : make power relative to excedent speed
            power = 4;
        }
        // 2. When inside the zone, cancel out all speed
        else if ( !isOutside(targetBegin, targetEnd)
                && ( Math.abs(this.hs) > (MAX_LANDING_HS - MARGIN) 
                    || Math.abs(this.vs) > (MAX_LANDING_VS - MARGIN) )
                ) {
            Move cancelMove = getCancelMove();
            angle = cancelMove.rotation;
            power = cancelMove.power;
        }
        // When on course, just try to limit vertical speed
        else {
            if (Math.abs(this.vs) > (MAX_LANDING_VS / 2)) {
                angle = 0;
                power = (int) ( 2 * MAX_POWER * (Math.abs(this.vs) / (double) MAX_LANDING_VS) );
            }
        }
        
        // Bound power
        if (power > MAX_POWER)
            power = MAX_POWER;
        else if (power < 0)
            power = 0;
        
        return new Move(angle, power);
    }
    
    protected boolean isOutside(Point targetBegin, Point targetEnd) {
        return (this.position.x < targetBegin.x 
                || this.position.x > targetEnd.x 
                || this.position.y < targetBegin.y);
    }
    
    // Returns the move that tries to cancel out the current speed
    protected Move getCancelMove() {
        int angle = 0, power = 0;
        // If horizontal speed is acceptable, go to vertical
        if (Math.abs(this.hs) < MAX_LANDING_HS)
            angle = 0;
        // Otherwise choose angle that cancels out speed vector
        else {
            Point end = new Point(this.hs, this.vs);
            end.scale(-1);
            end.translate(this.position);
            angle = this.position.angle(end);
        }
        
        Point speed = new Point(this.hs, this.vs),
            maxSpeed = new Point(MAX_LANDING_HS, MAX_LANDING_VS);
        power = (int) Math.round(MAX_POWER * (speed.length() / maxSpeed.length()));
        
        return new Move(angle, power);
    }
    
    
    // Predict if the horizontal speed is large enough to reach the target zone
    // before reaching the ground
    protected boolean isEnough(Point targetBegin, Point targetEnd, int epsilon) {
        if (!isOutside(targetBegin, targetEnd))
            return true;
        
        int distance1 = Math.abs(targetBegin.x - this.position.x),
            distance2 = Math.abs(targetEnd.x - this.position.x),
            distance = Math.min(distance1, distance2) + epsilon;
            
        if (distance <= 0)
            return true;
        if (Math.abs(this.hs) <= 0)
            return false;
            
        // Time left (in seconds) before we touch the ground
        // assuming vertical speed stays constant at its current value
        int altitude = Math.abs(this.position.y - targetBegin.y);
        double timeLeft = altitude / (double) Math.abs(this.vs);
        
        double eta = distance / (double) Math.abs(this.hs);
        return ( eta < timeLeft );
    }
    
    // Predict if the horizontal speed is too large to travel the given distance
    // before reaching the ground
    protected boolean isTooMuch(Point targetBegin, Point targetEnd) {
        int distance;
        if (isOutside(targetBegin, targetEnd)) {
            distance = Math.max ( Math.abs(this.position.x - targetBegin.x), 
                                    Math.abs(this.position.x - targetEnd.x) );
        }
        else {
            distance = Math.min ( Math.abs(this.position.x - targetBegin.x), 
                                    Math.abs(this.position.x - targetEnd.x) );
        }
        
        if (distance <= 0 && Math.abs(this.hs) > 0)
            return true;
        else {
            if (Math.abs(this.hs) <= 0)
                return false;
            
            // Time left (in seconds) before we touch the ground
            // assuming vertical speed stays constant at its current value
            int altitude = Math.abs(this.position.y - targetBegin.y);
            double timeLeft = altitude / (double) Math.abs(this.vs);
            return (Math.abs(this.hs) * timeLeft) > distance;
        }
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
            // Write action to standard output
            nextMove.print();
        }
    }
}