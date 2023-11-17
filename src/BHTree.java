/*
 * Authors: Joel Strand
 * Version: 0.2
 * Date Last Updated: 11/11/23
 */

public class BHTree {
    // Change to adjust distance approximation threshold.
    // 0 = brute force, 1.75+ = heavy appprox 
    private static final double theta = 0.5;

    private Body body;
    private Quad quad;
    public BHTree NorthEast, NorthWest, SouthEast, SouthWest;

    public BHTree(Quad q) {
        this.quad = q;
        this.body = null;
        this.NorthEast = null;
        this.NorthWest = null;
        this.SouthEast = null;
        this.SouthWest = null;
    }

    public void insert(Body b) {
        // if no bodies present in tree
        if (this.body == null) {
            this.body = b;
        }

        // if internal node
        if (!isExternal()) {
            this.body = this.body.plus(b);

            // recurse to insert b into correct quad
            findQuad(b);
        }

        // if external node
        else {
            // create children
            subdivide();

            // recurse to insert both bodies into correct quad
            findQuad(this.body);
            findQuad(b);

            this.body = body.plus(b);
        }
    }

    public boolean isExternal() {
        return (NorthWest == null && NorthEast == null && SouthWest == null && SouthEast == null);
    }

    private void findQuad(Body b) {
        if (b.inQuad(quad.NorthWest())) {
            NorthWest.insert(b);
        } else if (b.inQuad(quad.NorthEast())) {
            NorthEast.insert(b);
        } else if (b.inQuad(quad.SouthWest())) {
            SouthWest.insert(b);
        } else if (b.inQuad(quad.SouthEast())) {
            SouthEast.insert(b);
        }
    }

    private void subdivide() {
        this.NorthEast = new BHTree(this.quad.NorthEast());
        this.NorthWest = new BHTree(this.quad.NorthWest());
        this.SouthEast = new BHTree(this.quad.SouthEast());
        this.SouthWest = new BHTree(this.quad.SouthWest());
    }

    public void updateForce(Body b) {
        if (this.body == null || this.body.equals(b)) {
            return;
        }

        if (isExternal()) {
            b.addForce(this.body);
        } else {
            double width = this.quad.length();
            double dist = this.body.distanceBetween(b);

            if ((width / dist) < theta) {
                b.addForce(this.body);
            } else {
                NorthWest.updateForce(b);
                NorthEast.updateForce(b);
                SouthWest.updateForce(b);
                SouthEast.updateForce(b);
            }
        }
    }
}