/*
 * Authors: Joel Strand
 * Date Last Updated: 11/17/23
 */

// Represents a square area used in spatial partitioning
public class Quad {

    // Properties defining the center coordinates and length of the quad
    private double xMid, yMid, length;

    // Constructor to initialize a Quad with given center coordinates and length
    public Quad(double xMid, double yMid, double length) {
        this.xMid = xMid;
        this.yMid = yMid;
        this.length = length;
    }

    // Method to get the length of the quad
    public double length() {
        return length;
    }

    // Method to check if a coordinate pair is contained within the quad
    public boolean contains(double x, double y) {
        if (x >= xMid - (length / 2) && x <= xMid + (length / 2) && y >= yMid - (length / 2)
                && y <= yMid + (length / 2)) {
            return true;
        }
        return false;
    }

    // Methods to create new quads representing different sub-quadrants
    public Quad NorthEast() {
        return new Quad(xMid + (.25 * length), yMid + (.25 * length), length / 2);
    }

    public Quad NorthWest() {
        return new Quad(xMid - (.25 * length), yMid + (.25 * length), length / 2);
    }

    public Quad SouthEast() {
        return new Quad(xMid + (.25 * length), yMid - (.25 * length), length / 2);
    }

    public Quad SouthWest() {
        return new Quad(xMid - (.25 * length), yMid - (.25 * length), length / 2);
    }

    public void draw() {
        StdDraw.rectangle(xMid, yMid, length / 2.0, length / 2.0);
    }

    // Method to represent the Quad as a string (Not implemented in the code)
    public String toString() {
        return null;
    }
}
