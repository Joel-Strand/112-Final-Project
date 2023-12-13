/*
 * Author: Joel Strand
 * Date Last Updated: 11/17/23
 */

public interface QuadTree {
    boolean isExternal();
    void insert(Body b);
    Quad getQuad();
}
