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

    private boolean isExternal() {
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

    private void updateTree(BHTree tree) {
        if (!tree.isExternal()) {
            if (tree.NorthWest != null) {
                insert(tree.NorthWest.body);
                updateForce(tree.NorthWest.body);
                updateTree(tree.Northwest);
            }

            if (tree.NorthEast != null) {
                insert(tree.NorthEast.body);
                updateForce(tree.NorthEast.body);
                updateTree(tree.NorthEast);
            }

            if (tree.SouthWest != null) {
                insert(tree.SouthWest.body);
                updateForce(tree.SouthWest.body);
                updateTree(tree.SouthWest);
            }

            if (tree.SouthEast != null) {
                insert(tree.SouthEast.body);
                updateForce(tree.SouthEast.body);
                updateTree(tree.SouthEast);
            }
        } else {
            // render
            tree.body.draw();
        }
        
    }
}