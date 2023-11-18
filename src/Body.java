/*
 * Authors: Joel Strand
 * Version: 0.2
 * Date Last Updated: 11/17/23
 */

import java.awt.Graphics;
import java.awt.Color;

// Class representing a body in gravitational simulation
public class Body {

    // Gravitational constant: g = 6.67e-11 [m3 kg-1 s-2]
    private static final double g = 0.0000000000667;

    // Fields defining the position, velocity, force, and mass of the body
    private Pair s, v, f;
    private double mass;

    // Constructor to initialize a Body with position, velocity, and mass
    public Body(Pair pos, Pair velo, double mass) {
        this.s = pos;
        this.v = velo;
        this.f = new Pair(0, 0);
        this.mass = mass;
    }

    // Method to calculate the distance between this body and another body 'b'
    public double distanceBetween(Body b) {
        double dx = this.s.x - b.s.x;
        double dy = this.s.y - b.s.y;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    // Method to compute the gravitational force acting on this body due to another body 'b'
    public void addForce(Body b) {
        double soft = 75; // Softening factor to prevent extreme forces when bodies are close
        double dx = b.s.x - this.s.x;
        double dy = b.s.y - this.s.y;
        double dist = Math.sqrt((dx * dx) + (dy * dy)); // Calculate Euclidean Distance
        double force = (g * b.mass * this.mass) / ((dist * dist) + (soft * soft));
        this.f.x = (force * dx) / (dist);
        this.f.y = (force * dy) / (dist);
    }

    // Method to update the body's position based on the current velocity and deltaTime
    public void update(double deltaTime) {
        v.times(deltaTime / mass);
        s.times(deltaTime / mass);
    }

    // Method to compute a new body by combining this body with another body 'b'
    public Body plus(Body b) {
        double m = this.mass + b.mass;

        // Calculate new center of mass
        double comX = ((this.s.x * this.mass) + (b.s.x * b.mass)) / m;
        double comY = ((this.s.y * this.mass) + (b.s.y * b.mass)) / m;

        return new Body(new Pair(comX, comY), new Pair(this.v.x, b.v.y), m);
    }

    // Method to check if the body is within a specified Quad 'q'
    public boolean inQuad(Quad q) {
        return q.contains(this.s.x, this.s.y);
    }

    // Method to reset the force acting on the body to zero
    public void resetForce() {
        f.x = 0.0;
        f.y = 0.0;
    }

    // Method to draw the body as a small red oval on the graphics object 'g'
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.drawOval((int) s.x, (int) s.y, 2, 2);
    }

    // Method to represent the body as a formatted string
    public String toString() {
        return String.format("%10E %10E %10E %10E %10E", s.x, s.y, v.x, v.y, mass);
    }

    // Getter methods to access the properties of the body
    public Pair getPos() {
        return this.s;
    }

    public Pair getVelo() {
        return this.v;
    }

    public Pair getForce() {
        return this.f;
    }

    public double getMass() {
        return this.mass;
    }
}
