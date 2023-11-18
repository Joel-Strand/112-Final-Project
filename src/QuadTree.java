public interface QuadTree {
    boolean isExternal();
    void insert(Body b);
    void updateForce(Body b);
    Quad getQuad();
}
