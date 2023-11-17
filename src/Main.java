/*
 * Authors: Joel Strand, Liam Davis, Aiden Taghinia
 * Date Last Updated: 11/17/23
 */

import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;


public class Main extends JPanel {

    private static final int _FPS = 60;
    private static final int _WIDTH = 1024;
    private static final int _HEIGHT = 768;
    private Body[] bodies;
    private double radius;
    private double deltaTime;
    private int numBodies;


    class Runner implements Runnable {
        public void run() {
            while (true) {
                paintComponent(getGraphics());
                simUniverse(bodies, deltaTime, radius, getGraphics());
                repaint();
                try {
                    Thread.sleep(1000 / _FPS);
                } catch (InterruptedException e) {
                }
            }

        }
    }
    
    public Main() {
        this.setPreferredSize(new Dimension(_WIDTH, _HEIGHT));
        Scanner s = new Scanner(System.in);

        this.numBodies = s.nextInt();
        this.radius = s.nextDouble();
        this.deltaTime = 0.1d;
        this.bodies = new Body[numBodies];

        parseInfo(bodies, numBodies, s);

        Thread t = new Thread(new Runner());
        t.start();
        System.out.println("running in Thread");
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("SPACE!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main mainInstance = new Main(); // java Main < [filePath]
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);
    
    }

    private void parseInfo(Body[] bodies, int numBodies, Scanner s) {
        for (int i = 0; i < numBodies; i++) {
            double sx = s.nextDouble();
            double sy = s.nextDouble();
            double vx = s.nextDouble();
            double vy = s.nextDouble();
            double m = s.nextDouble();

            Body b = new Body(new Pair(sx, sy), new Pair(vx, vy), m);
            bodies[i] = b;
        }
    }

    private void simUniverse(Body[] bodies, double deltaTime, double radius, Graphics g) {
        for (double i = 0; true; i += deltaTime) {
            Quad q = new Quad(0, 0, radius * 2);
            BHTree tree = new BHTree(q);

            for (int j = 0; j < bodies.length; j++) {
                Body b = bodies[j];
                if (b.inQuad(q)) {
                    tree.insert(b);
                }
            }

            for (int j = 0; j < bodies.length; j++) {
                Body b = bodies[j];
                b.resetForce();
                tree.updateForce(b);
                b.update(deltaTime);
            }

            for (Body b : bodies) {
                b.draw(g);
            }

        }
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, _WIDTH, _HEIGHT);
    }

    // private void readFromFile(String filePath) {
    //     File file = new File(filePath);
    //     try {
    //         Scanner s = new Scanner(file);
    //         double uniWidth = s.nextDouble();
    //         this.tree = new BHTree(new Quad(0, 0, uniWidth));
    //         while (s.hasNextLine()) {
    //             String[] temp = s.nextLine().split(" ");

    //             Pair pos = new Pair(parseNum(temp[0]), parseNum(temp[1]));
    //             Pair vel = new Pair(parseNum(temp[2]), parseNum(temp[3]));
    //             double mass = parseNum(temp[4]);

    //             Body body = new Body(pos, vel, mass);
    //             this.tree.insert(body);
    //         }
    //         s.close();
    //     } catch (FileNotFoundException e) {
    //         System.out.println("File Not Found @ " + filePath);
    //         e.printStackTrace();
    //     }
    // }

    // private double parseNum(String num) {
    //     String base = "";
    //     String exp = "";

    //     for (int i = 0; i < num.length(); i++) {
    //         char c = num.charAt(i);
    //         if (c == 'E') {
    //             exp += num.charAt(i+1);
    //             if (num.charAt(i+2) != ' ') {
    //                 exp += num.charAt(i + 2);
    //             }
    //         } else {
    //             base += c;
    //         }
    //     }

    //     double b = Double.parseDouble(base);
    //     double e = Double.parseDouble(exp);
    //     double ret = b;
        
    //     for (int i = 0; i < e; i++) {
    //         ret *= b;
    //     }

    //     return ret;
    // }

    private void updateTree(BHTree tree, Graphics g) {
        if (!tree.isExternal()) {
            Body b = tree.body;

            if (b.inQuad(tree.NorthWest.getQuad())) {
                tree.insert(b);
                tree.updateForce(b);
                updateTree(tree.NorthWest, g);
            }

            if (b.inQuad(tree.NorthEast.getQuad())) {
                tree.insert(b);
                tree.updateForce(b);
                updateTree(tree.NorthEast, g);
            }

            if (b.inQuad(tree.SouthWest.getQuad())) {
                tree.insert(b);
                tree.updateForce(b);
                updateTree(tree.SouthWest, g);
            }

            if (b.inQuad(tree.SouthEast.getQuad())) {
                tree.insert(b);
                tree.updateForce(b);
                updateTree(tree.SouthEast, g);
            }
        } else {
            // render
            tree.body.draw(g);
        }
    }
}