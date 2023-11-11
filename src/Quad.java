/*
 * Authors: Joel Strand
 * Version: 0.1
 * Date Last Updated: 11/11/23
 */

import java.io.LineNumberInputStream;

public class Quad {

    private double xMid, yMid, length;

    public Quad(double xMid, double yMid, double length) {
        this.xMid = xMid;
        this.yMid = yMid;
        this.length = length;
    }

    public double length() {
        return length;
    }

    public boolean contains(double x, double y) {
        if (x >= xMid - (length / 2) && x <= xMid + (length / 2) && y >= yMid - (length / 2)
                && y <= yMid + (length / 2)) {
            return true;
        }
        return false;
    }

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

    public String toString() {
        return null;
    }

}
