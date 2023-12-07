/*
 * Authors: Joel Strand, Liam Davis, Aiden Taghinia
 * Date Last Updated: 12/6/23
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.event.KeyEvent;

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
        double theta = Double.parseDouble(args[0]);
        System.out.println(theta);
        numBodies = s.nextInt(); // Number of bodies in universe
        radius = s.nextDouble(); // Radius of universe
        bodies = new ArrayList<>();

        // Enable animation mode and recalibrate coords
        StdDraw.show(0);
        StdDraw.setXscale(-radius, +radius);
        StdDraw.setYscale(-radius, +radius);

        parseInfo(bodies, numBodies, s);
        simUniverse(bodies, theta, deltaTime, radius);
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
    private static void simUniverse(ArrayList<Body> bodies, double theta, double deltaTime, double radius) {
        while (true) {
            // Create quad, Barnes-Hut tree for force calculation
            Quad q = new Quad(0, 0, radius * 2);
            BHTree tree = new BHTree(q);
            tree.setTheta(theta);

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

            // Add bodies to tree AFTER tree is created and updated
            // Guarantees these bodies will be inserted and added next frame.
            if (StdDraw.mousePressed()) {
                Random rand = new Random();
                double Mx = StdDraw.mouseX();
                double My = StdDraw.mouseY();
                double mass = factorial(rand.nextInt(50));
                Pair pos = new Pair(Mx, My);
                Pair velo = new Pair(0,0);
                Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));

                if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {         // Left arrow
                    velo.x -= rand.nextInt(100) * 100;
                } else if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {   // Up arrow
                    velo.y += rand.nextInt(100) * 100;
                } else if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {  // Right Arrow
                    velo.x += rand.nextInt(100) * 100;
                } else if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {  // Down Arrow
                    velo.y -= rand.nextInt(100) * 100;
                } else {
                    double d = Math.random();
                    if (d < 0.25) {
                        velo = new Pair(rand.nextInt(100) * 100, rand.nextInt(100) * 100);
                    } else if (d < 0.5 && d >= 0.25) {
                        velo = new Pair(rand.nextInt(100) * 100, -(rand.nextInt(100) * 100));
                    } else if (d < 0.75 && d >= 0.5) {
                        velo = new Pair(-(rand.nextInt(100) * 100), rand.nextInt(100) * 100);
                    } else {
                        velo = new Pair(-(rand.nextInt(100) * 100), -(rand.nextInt(100) * 100));
                    }
                }
                bodies.add(new Body(pos, velo, mass, c));
            }
        }
    }

    private static Pair initVeloFromKeys(char c) {
        Random rand = new Random();
        switch (c) {
            case 37: 
                return new Pair(-rand.nextInt() * 10000, 0);
            case 38: 
                return new Pair(0, rand.nextInt() * 10000);
            case 39: 
                return new Pair(rand.nextInt() * 10000, 0);
            case 40: 
                return new Pair(0, -rand.nextInt() * 10000);
            default: 
                break; 
        }
        return null;
    }

    // private static Body createBody() {
    //     Random rand = new Random();
    //     double Mx = StdDraw.mouseX();
    //     double My = StdDraw.mouseY();
    //     double mass = factorial(rand.nextInt(50));
    //     Pair pos = new Pair(Mx, My);
    //     Pair velo = new Pair(0,0);
    //     Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));

    //     if (hasNextKeyTyped()) {
    //         char c = StdDraw.nextKeyTyped();    
    //         velo = initVeloFromKeys(c);
    //     } else {
    //         velo = new Pair(rand.nextInt() * 10000, rand.nextInt() * 10000);
    //     }
    //     return new Body(pos, velo, mass, c);
    // }

    private static double factorial(int factor) {
        int res = 1;
        for (int i = 1; i <= factor; i++) {
            res *= factor;
        }
        return res;
    }
}