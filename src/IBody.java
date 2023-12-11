/*
 * Author(s): Joel Strand
 * Date Last Updated: 12/11/23
 */

public interface IBody {
    double distanceBetween(Body b);
    void addForce(Body b);
    void update(double deltaTime);
    Body plus(Body b);
    boolean inQuad(Quad q);
    void resetForce();
    void draw();
    void toString();
    Pair getPos();
    Pair getVelo();
    Pair getForce();
    double getMass();
    Color getColor();
}


