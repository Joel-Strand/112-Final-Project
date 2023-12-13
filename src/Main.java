/*
 * Authors: Joel Strand, Liam Davis, Aiden Taghinia
 * Date Last Updated: 12/6/23
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import javax.swing.JPanel;

// Main class for program execution
public class Main extends JPanel {

    // Variables for simulation parameters
    private final static double deltaTime = 0.1;
    private static int numBodies;
    private static double radius;
    private static ArrayList<Body> bodies;
    private static boolean paused;

    // Main method
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in); // Scanner for reading file input
        double theta = 0;
        try {
            theta = Double.parseDouble(args[0]);

            System.out.println(theta);
            numBodies = s.nextInt(); // Number of bodies in universe
            radius = s.nextDouble(); // Radius of universe
            bodies = new ArrayList<>();

            // Enable animation mode and recalibrate coords
            StdDraw.show(0);
            StdDraw.setXscale(-radius, +radius);
            StdDraw.setYscale(-radius, +radius);

            paused = false;
            parseInfo(bodies, numBodies, s);
            simUniverse(bodies, theta, deltaTime, radius);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: Please provide a value for theta -> cmd: java Main [theta] [filepath]");
            e.printStackTrace();
        }
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
        for (double dt = 0; dt < Double.MAX_VALUE; dt += deltaTime) {
            if (!paused) {
                String s = "Time Elapsed: " + dt + "ms";
                StdDraw.text(radius - (.25 * radius), 0, s);
                StdDraw.show(0);
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
                    Pair velo = new Pair(0, 0);
                    Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));

                    if (StdDraw.isKeyPressed(KeyEvent.VK_SHIFT)) {
                        mass *= 1000000000;
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

                if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) {
                    double mouseX = StdDraw.mouseX();
                    double mouseY = StdDraw.mouseY();
                    double blackHoleMass = Math.pow(10, 23);

                    // Create black hole body at the mouse position
                    Body blackHole = new Body(new Pair(mouseX, mouseY), new Pair(0, 0), blackHoleMass, Color.BLACK);

                    // Apply gravitational attraction from the black hole to all bodies
                    bodies.add(blackHole);
                }

                // Check for function key presses
                if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)) {
                    // Slow time by 25%
                    deltaTime *= .25;
                } else if (StdDraw.isKeyPressed(KeyEvent.VK_UP)) {
                    // Speed up time by 25%
                    deltaTime *= 1.25;
                } else if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
                    deltaTime = -0.2;
                } else {
                    deltaTime = 0.1;
                }
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
    // Random rand = new Random();
    // double Mx = StdDraw.mouseX();
    // double My = StdDraw.mouseY();
    // double mass = factorial(rand.nextInt(50));
    // Pair pos = new Pair(Mx, My);
    // Pair velo = new Pair(0,0);
    // Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));

    // if (hasNextKeyTyped()) {
    // char c = StdDraw.nextKeyTyped();
    // velo = initVeloFromKeys(c);
    // } else {
    // velo = new Pair(rand.nextInt() * 10000, rand.nextInt() * 10000);
    // }
    // return new Body(pos, velo, mass, c);
    // }

    private static double factorial(int factor) {
        int res = 1;
        for (int i = 1; i <= factor; i++) {
            res *= factor;
        }
        return res;
    }
}
