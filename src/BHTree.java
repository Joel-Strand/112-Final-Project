/*
 * Authors: Joel Strand
 * Date Last Updated: 11/27/23
 */

// Implementation of a Barnes-Hut Tree (BHTree) for a QuadTree interface
public class BHTree implements QuadTree {

    // Constant representing the critical ratio for Barnes-Hut approximation
    private static final double theta = 0.5;

    // Variables for storing body, quad, and tree divisions
    private Body body;
    private Quad quad;
    private BHTree NorthEast, NorthWest, SouthEast, SouthWest;

    // Constructor for BHTree with a given quad
    public BHTree(Quad q) {
        this.quad = q;
        this.body = null;
        this.NorthEast = null;
        this.NorthWest = null;
        this.SouthEast = null;
        this.SouthWest = null;
    }

    // Method to insert a body into the BHTree
    @Override
    public void insert(Body b) {

        // If current node empty, insert b
        if (this.body == null) {
            this.body = b;
            return;
        }

        // If current node is internal, add masses and continue recursion with b
        if (!isExternal()) {
            this.body = this.body.plus(b); // Combine masses
            recurse(b); // Recurse to continue search
        } 
        
        // If current node is external, create children and recurse with BOTH this.body and b, then add masses
        else {
        
            subdivide(); // Create children

            // Recurse with both bodies to replace in appropriate new sub-quadrants
            recurse(this.body);
            recurse(b);

            this.body = this.body.plus(b); // Combine masses
        }
    }

    // Method to check if the node is external (has no children)
    @Override
    public boolean isExternal() {
        return (this.NorthWest == null && this.NorthEast == null && this.SouthWest == null && this.SouthEast == null);
    }

    // Method to assign the quadrant for a body
    private void recurse(Body b) {
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

    // Method to subdivide the tree into quadrants
    private void subdivide() {
        this.NorthEast = new BHTree(this.quad.NorthEast());
        this.NorthWest = new BHTree(this.quad.NorthWest());
        this.SouthEast = new BHTree(this.quad.SouthEast());
        this.SouthWest = new BHTree(this.quad.SouthWest());
    }

    // Method to update the forces acting on a body
    @Override
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
                // Recurse through every quad
                NorthWest.updateForce(b);   
                NorthEast.updateForce(b);
                SouthWest.updateForce(b);
                SouthEast.updateForce(b);
            }
        }
    }

    // Method to retrieve the quad associated with the tree node
    @Override
    public Quad getQuad() {
        return this.quad;
    }
}
