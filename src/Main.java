/*
 * Authors: Joel Strand, Liam Davis, Aiden Taghinia
 * Date Last Updated: 11/27/23
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import javax.swing.JPanel;

// Main class for program execution
public class Main extends JPanel {

    // Variables for simulation parameters
    private final static double deltaTime = 0.1;
    private static int numBodies;
    private static double radius;
    private static ArrayList<Body> bodies;

    // Main method
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in); // Scanner for reading file input

        numBodies = s.nextInt(); // Number of bodies in universe
        radius = s.nextDouble(); // Radius of universe
        bodies = new ArrayList<>();

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
    private static void parseInfo(ArrayList<Body> bodies, int numBodies, Scanner s) {
        for (int i = 0; i < numBodies; i++) {
            // Read parameters for each body (position, velocity, mass)
            double sx = s.nextDouble();
            double sy = s.nextDouble();
            double vx = s.nextDouble();
            double vy = s.nextDouble();
            double m = s.nextDouble();
            int r = s.nextInt();
            int g = s.nextInt();
            int b = s.nextInt();
            Color c = new Color(r, g, b);

            // Create a new Body object with the parsed information and add it to the array
            Body body = new Body(new Pair(sx, sy), new Pair(vx, vy), m, c);
            bodies.add(body);
        }
    }

    // Method to simulate the universe based on gravitational interactions between
    // bodies
    private static void simUniverse(ArrayList<Body> bodies, double deltaTime, double radius) {
        while (true) {
            if (StdDraw.mousePressed()) {
                Random rand = new Random();
                Pair pos = new Pair(StdDraw.mouseX(), StdDraw.mouseY());
                Pair velo = new Pair(rand.nextDouble() * 10000, rand.nextDouble() * 10000);
                double mass = factorial(15);
                Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
                Body b = new Body(pos, velo, mass, c);
                bodies.add(b);
            }

            
            // Create quad, Barnes-Hut tree for force calculation
            Quad q = new Quad(0, 0, radius * 2);
            BHTree tree = new BHTree(q);

            // Insert bodies into the tree
            for (Body b : bodies) {
                if (b.inQuad(q)) {
                    tree.insert(b);
                }
            }

            // Reset and update forces acting on every body, then update position
            for (Body b : bodies) {
                b.resetForce();
                tree.updateForce(b);
                b.update(deltaTime);  
            }

            // Draw the bodies
            StdDraw.clear(StdDraw.BLACK);
            for (Body b : bodies) {
                    b.draw();
            }
            StdDraw.show(10);
        }
    }   

    private static double factorial(int factor) {
        int res = 1;
        for (int i = 1; i < factor; i++) {
            res *= factor;
        }
        return res;
    }
}