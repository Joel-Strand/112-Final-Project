/*
 * Authors: Joel Strand
 * Version: 0.1
 * Date Last Updated: 11/11/23
 */

public class BHTree {
    // Change to adjust distance approximation threshold.
    // 0 = brute force, 1.75+ = heavy appprox 
    private static final double theta = 0.5;

    private Body body;
    private Quad quad;
    private BHTree NorthEast, NorthWest, SouthEast, SouthWest;

    public BHTree(Quad q) {
        this.quad = q;
        this.body = null;
        this.NorthEast = null;
        this.NorthWest = null;
        this.SouthEast = null;
        this.SouthWest = null;
    }

    public void insert(Body b) {
        if (this.body == null) {
            this.body = b;
        }
}
}
