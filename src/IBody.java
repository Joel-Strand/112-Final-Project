/*
 * Author(s): Joel Strand, Nate Pabis
 * Date Last Updated: 12/11/23
 */

import java.awt.Color;

public interface IBody {
    double distanceBetween(Body b);
    void addForce(Body b);
    void update(double deltaTime);
    Body plus(Body b);
    boolean inQuad(Quad q);
    void resetForce();
    void draw();
    String toString();
    Pair getPos();
    Pair getVelo();
    Pair getForce();
    double getMass();
    Color getColor();
}


