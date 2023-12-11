/*
 * Authors: Joel Strand
 * Date Last Updated: 11/23/23
 */

import java.awt.Color;

// Class representing a body in gravitational simulation
public class Body implements IBody {

    // Gravitational constant: g = 6.67e-11 [m3 kg-1 s-2]
    private static final double g = 0.0000000000667;

    // Fields defining the position, velocity, force, mass, and color of the body
    public Pair s, v, f;
    private double mass;
    private Color color;

    // Constructor to initialize a Body with position, velocity, and mass
    public Body(Pair pos, Pair velo, double mass, Color c) {
        this.s = pos;
        this.v = velo;
        this.f = new Pair(0, 0);
        this.mass = mass;
        this.color = c;
    }

    // Method to calculate the distance between this body and another body 'b'
    @Override
    public double distanceBetween(Body b) {
        double dx = b.s.x - this.s.x ;
        double dy = b.s.y - this.s.y;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    // Method to compute the gravitational force acting on this body due to another
    // body 'b'
    @Override
    public void addForce(Body b) {
        double soft = 75; // Softening factor to prevent extreme forces when bodies are close
        double dx = b.s.x - this.s.x;
        double dy = b.s.y - this.s.y;
        double dist = distanceBetween(b); // Calculate Euclidean Distance
        double force = (g * b.mass * this.mass) / ((dist * dist) + (soft * soft));
        this.f.x += (force * dx) / dist;
        this.f.y += (force * dy) / dist;
    }

    // Method to update the body's position based on the current velocity and
    // deltaTime
    @Override
    public void update(double deltaTime) {
        this.v.x += (deltaTime * this.f.x) / this.mass;
        this.v.y += (deltaTime * this.f.y) / this.mass;
        this.s.x += deltaTime * this.v.x;
        this.s.y += deltaTime * this.v.y;
    }

    // Method to compute a new body by combining this body with another body 'b'
    @Override
    public Body plus(Body b) {
        double m = this.mass + b.mass;

        // Calculate new center of mass
        double comX = ((this.s.x * this.mass) + (b.s.x * b.mass)) / m;
        double comY = ((this.s.y * this.mass) + (b.s.y * b.mass)) / m;

        return new Body(new Pair(comX, comY), new Pair(this.v.x, b.v.x), m, this.color);
    }

    // Method to check if the body is within a specified Quad 'q'
    @Override
    public boolean inQuad(Quad q) {
        return q.contains(this.s.x, this.s.y);
    }

    // Method to reset the force acting on the body to zero
    @Override
    public void resetForce() {
        this.f = new Pair(0, 0);
    }

    // Method to draw the body as a small red oval on the graphics object 'g'
    @Override
    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.point(s.x, s.y);
    }

    // Method to represent the body as a formatted string
    @Override
    public String toString() {
        return String.format("%10E %10E %10E %10E %10E %10E %10E", s.x, s.y, v.x, v.y, f.x, f.y, mass);
    }

    // Getter methods to access the properties of the body
    @Override
    public Pair getPos() {
        return this.s;
    }

    @Override
    public Pair getVelo() {
        return this.v;
    }

    @Override
    public Pair getForce() {
        return this.f;
    }

    @Override
    public double getMass() {
        return this.mass;
    }

    @Override
    public Color getColor() {
        return this.color;
    }
}
