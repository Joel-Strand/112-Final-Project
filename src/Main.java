/*
 * Authors: Joel Strand, Liam Davis, Aiden Taghinia
 * Date Last Updated: 11/17/23
 */

import javax.swing.JFrame;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;

public class Main extends JPanel {

    // Constants for FPS, width, and height of the display
    private static final int _FPS = 60;
    private static final int _WIDTH = 1024;
    private static final int _HEIGHT = 768;

    // Variables for simulation parameters
    private int numBodies;
    private double radius;
    private double deltaTime;
    private Body[] bodies;

    // Inner class implementing Runnable for simulation loop
    class Runner implements Runnable {
        public void run() {
            // Continuous simulation loop
            while (true) {
                // Perform universe simulation based on defined parameters
                simUniverse(bodies, deltaTime, radius, getGraphics());
                try {
                    // Control FPS (frames per second) by introducing a delay
                    Thread.sleep(1000 / _FPS);
                } catch (InterruptedException e) {
                    // Exception handling for thread interruption
                }
            }
        }
    }
    
    // Constructor for the Main class
    public Main() {
        // Set the preferred size of the panel to the defined width and height
        this.setPreferredSize(new Dimension(_WIDTH, _HEIGHT));
        Scanner s = new Scanner(System.in);
        // Read input values for the number of bodies and the radius
        this.numBodies = s.nextInt();
        this.radius = s.nextDouble();
        this.deltaTime = 0.1d; // Set the time increment for simulation
        this.bodies = new Body[numBodies]; // Initialize an array to hold bodies

        // Parse information for each body from the input source
        parseInfo(bodies, numBodies, s);

        // Create a new thread for running the simulation loop and start it
        Thread t = new Thread(new Runner());
        t.start();
        System.out.println("Running in Thread"); // Output message for the console
    }

    // Main method for running the simulation
    public static void main(String[] args) {
        // Create a new JFrame for displaying the simulation
        JFrame frame = new JFrame("SPACE!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main mainInstance = new Main(); // Create an instance of the Main class
        frame.setContentPane(mainInstance); // Set the content pane to the Main instance
        frame.pack();
        frame.setVisible(true); // Set the frame to be visible
    }

    // Method to parse information for each body from the input
    private void parseInfo(Body[] bodies, int numBodies, Scanner s) {
        for (int i = 0; i < numBodies; i++) {
            // Read parameters for each body (position, velocity, mass)
            double sx = s.nextDouble();
            double sy = s.nextDouble();
            double vx = s.nextDouble();
            double vy = s.nextDouble();
            double m = s.nextDouble();

            // Create a new Body object with the parsed information and add it to the array
            Body b = new Body(new Pair(sx, sy), new Pair(vx, vy), m);
            bodies[i] = b;
        }
    }

    // Method to simulate the universe based on gravitational interactions between bodies
    private void simUniverse(Body[] bodies, double deltaTime, double radius, Graphics g) {
        while (true) {
            // Create quad, Barnes-Hut tree for force calculation
            Quad q = new Quad(0, 0, radius * 2);
            BHTree tree = new BHTree(q);

            // Insert bodies into the tree
            for (int j = 0; j < bodies.length; j++) {
                Body b = bodies[j];
                if (b.inQuad(q)) {
                    tree.insert(b);
                }
            }

            // Update forces acting on each body within the tree
            for (Body b : bodies) {
                b.resetForce();
                tree.updateForce(b);
                b.update(deltaTime);
            }

            // Draw the bodies
            for (Body b : bodies) {
                b.draw(g);
            }
            repaint(); // Redraw the panel
        }
    }

    // Panel Focus
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    // Override the paintComponent method to draw a black background for the panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, _WIDTH, _HEIGHT); // Fill the panel with a black color
    }
}
