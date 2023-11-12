/*
 * Authors: Joel Strand
 * Version: 0.1
 * Date Late Updated: 11/11/23
 */

public class Pair {
    public double x, y;

    public Pair(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Pair add(double num) {
        return new Pair(this.x + num, this.y + num);
    }

    public Pair times(double num) {
        return new Pair(this.x * num, this.y * num);
    }
}