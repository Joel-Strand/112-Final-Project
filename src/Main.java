/*
 * Authors: Joel Strand, Liam Davis, Aiden Taghinia
 * Date Last Updated: 11/25/23
 */

import java.util.Scanner;
import java.awt.Color;
import javax.swing.JPanel;
 
// Main class for program execution
public class Main extends JPanel {

    // Variables for simulation parameters
    private static int numBodies;
    private static double radius;
    private static double deltaTime;
    private static Body[] bodies;
 
    // Main method
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in); // Scanner for reading file input

        numBodies = s.nextInt(); // Number of bodies in universe
        radius = s.nextDouble(); // Radius of universe
        bodies = new Body[numBodies];

        // Enable animation mode and recalibrate coords
        StdDraw.show(0);
        StdDraw.setXscale(-radius, +radius);
        StdDraw.setYscale(-radius, +radius);

        parseInfo(bodies, numBodies, s);
        simUniverse(bodies, deltaTime, radius);
    }

    // Panel Focus
    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    // Method to parse information for each body from the input
    private static void parseInfo(Body[] bodies, int numBodies, Scanner s) {
        for (int i = 0; i < numBodies; i++) {
            // Read parameters for each body (position, velocity, mass)
            double sx = s.nextDouble();
            double sy = s.nextDouble();
            double vx = s.nextDouble();
            double vy = s.nextDouble();
            double m = s.nextDouble();
            int r = s.nextInt();
            int b = s.nextInt();
            int g = s.nextInt();
            Color c = new Color(r, g, b);

            // Create a new Body object with the parsed information and add it to the array
            Body body = new Body(new Pair(sx, sy), new Pair(vx, vy), m, c);
            bodies[i] = body;
        }
    }

    // Method to simulate the universe based on gravitational interactions between bodies
    private static void simUniverse(Body[] bodies, double deltaTime, double radius) {
        while (true) {
            // Create quad, Barnes-Hut tree for force calculation
            Quad q = new Quad(0, 0, radius * 2);
            BHTree tree = new BHTree(q);

            System.out.println("-------------");

            // Insert bodies into the tree
            for (Body b : bodies) {
                if (b.inQuad(q)) {
                    tree.insert(b);
                }
            }

            System.out.println("insertion complete");
            return;

            // // Update forces acting on each body within the tree
            // for (Body b : bodies) {
            //     System.out.println("BEFORE: " + b.toString());
            //     b.resetForce();
            //     tree.updateForce(b);
            //     System.out.println("UPDATE: " + b.toString());
            //     b.update(deltaTime);  
            //     System.out.println("AFTER: " + b.toString());  
            //     System.out.println("--------------");
            // }

            // // Draw the bodies
            // StdDraw.clear(StdDraw.BLACK);
            // for (Body b : bodies) {
            //      b.draw();
            // }
            // StdDraw.show(10);
            // System.out.println("-------------");
        }
    }
}
