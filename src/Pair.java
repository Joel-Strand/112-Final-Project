/*
 * Authors: Joel Strand
 * Version: 0.1
 * Date Late Updated: 11/17/23
 */

// Represents a pair of numeric values (x, y)
public class Pair {
    public double x, y;

    // Constructor to initialize a Pair with given x and y values
    public Pair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Method to add a numeric value to both x and y components of the Pair
    public Pair add(double num) {
        return new Pair(this.x + num, this.y + num);
    }

    // Method to multiply both x and y components of the Pair by a numeric value
    public Pair times(double num) {
        return new Pair(this.x * num, this.y * num);
    }
}
