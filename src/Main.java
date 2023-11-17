/*
 * Authors: Joel Strand
 * Date Last Updated: 11/11/23
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
    private BHTree tree;
    private ArrayList<Body> bodies;

    class Thread1 extends Thread {
        @Override
        public void run() {
            while (true) {

                repaint();
                try {
                    Thread.sleep(1000 / _FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public Main(String filePath) {
        this.setPreferredSize(new Dimension(_WIDTH, _HEIGHT));
        readFromFile(filePath);
        Thread Thread1 = new Thread(); // need new Thread(new BHTree(new Quad()))??
        Thread1.start();
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("SPACE!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Main mainInstance = new Main(args[0]); // java Main < [filePath]
        frame.setContentPane(mainInstance);
        frame.pack();
        frame.setVisible(true);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, _WIDTH, _HEIGHT);

        // update here
        updateTree(tree, g);
    }

    private void readFromFile(String filePath) {
        File file = new File(filePath);
        try {
            Scanner s = new Scanner(file);
            double uniWidth = s.nextDouble();
            this.tree = new BHTree(new Quad(0, 0, uniWidth));
            while (s.hasNextLine()) {
                String[] temp = s.nextLine().split(" ");

                Pair pos = new Pair(parseNum(temp[0]), parseNum(temp[1]));
                Pair vel = new Pair(parseNum(temp[2]), parseNum(temp[3]));
                double mass = parseNum(temp[4]);

                Body body = new Body(pos, vel, mass);
                this.tree.insert(body);
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found @ " + filePath);
            e.printStackTrace();
        }
    }

    private double parseNum(String num) {
        String base = "";
        String exp = "";

        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            if (c == 'E') {
                exp += num.charAt(i+1);
                if (num.charAt(i+2) != ' ') {
                    exp += num.charAt(i + 2);
                }
            } else {
                base += c;
            }
        }

        double b = Double.parseDouble(base);
        double e = Double.parseDouble(exp);
        double ret = b;
        for (int i = 0; i < e; i++) {
            ret *= b;
        }

        return ret;
    }

    private void updateTree(BHTree tree, Graphics g) {
        if (!tree.isExternal()) {
            if (tree.NorthWest != null) {
                insert(tree.NorthWest.body);
                updateForce(tree.NorthWest.body);
                updateTree(tree.Northwest);
            }

            if (tree.NorthEast != null) {
                insert(tree.NorthEast.body);
                updateForce(tree.NorthEast.body);
                updateTree(tree.NorthEast);
            }

            if (tree.SouthWest != null) {
                insert(tree.SouthWest.body);
                updateForce(tree.SouthWest.body);
                updateTree(tree.SouthWest);
            }

            if (tree.SouthEast != null) {
                insert(tree.SouthEast.body);
                updateForce(tree.SouthEast.body);
                updateTree(tree.SouthEast);
            }
        } else {
            // render
            tree.body.draw(g);
        }
    }
}