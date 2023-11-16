/*
 * Authors: Joel Strand
 * Version: 0.2
 * Date Last Updated: 11/11/23
 */

import java.awt.Graphics;
import java.awt.Color;

public class Body {

    // Gravitational constant: g = 6.67e-11 [m3 kg-1 s-2]
    private static final double g = 0.0000000000667;

    private Pair s, v, f;
    private double mass;

    public Body(Pair pos, Pair velo, double mass) {
        this.s = pos;
        this.v = velo;
        this.f = new Pair(0, 0);
        this.mass = mass;
    }

    public double distanceBetween(Body b) {
        // return euclidean distance between this and b
        double dx = this.s.x - b.s.x;
        double dy = this.s.y - b.s.y;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public void addForce(Body b) {
        double soft = 75; // softening so forces don't become extreme when bodies are close
        double dx = b.s.x - this.s.x;
        double dy = b.s.y - this.s.y;
        double dist = Math.sqrt((dx * dx) + (dy * dy));
        double force = (g * b.mass * this.mass) / ((dist * dist) + (soft * soft));
        this.f.x = (force * dx) / (dist);
        this.f.y = (force * dy) / (dist);
    }

    public void update(double deltaTime) {
        // set velocity before position so position is accurate
        // to current frame velocity instead of previous frame.
        v.times(deltaTime / mass);
        s.times(deltaTime / mass);
    }

    public Body plus(Body b) {
        // combined mass
        double m = this.mass + b.mass;

        // center of mass for new body [Ma*Sa + Mb*Sb]/(Ma + Mb)
        double comX = ((this.s.x * this.mass) + (b.s.x * b.mass)) / m;
        double comY = ((this.s.y * this.mass) + (b.s.y * b.mass)) / m;

        return new Body(new Pair(comX, comY), new Pair(this.v.x, b.v.y), m);
    }

    public boolean inQuad(Quad q) {
        return q.contains(this.s.x, this.s.y);
    }

    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.drawOval((int) s.x, (int) s.y, 5, 5);
    }

    public String toString() {
        return String.format("%10E %10E %10E %10E %10E", s.x, s.y, v.x, v.y, mass);
    }
}