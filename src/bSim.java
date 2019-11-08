// Comments with *** in front them are taken from the ECSE 202 Assignment 1, 2 and 3 Instructions

import acm.graphics.*;
import acm.graphics.GOval;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import acm.gui.*;


/**
 * The simulation class that extends GraphicsProgram and sets up the screen to bounce the balls from aBall and put them
 * in the bTree
 */

public class bSim extends GraphicsProgram {
    // Parameters used in this program
    private static final int WIDTH = 1200; //*** n.b. screen coordinates
    private static final int HEIGHT = 600; //***
    private static final int OFFSET = 200; //***
    private static final double SCALE = HEIGHT/100; // pixels per meter //***
    private static final int NUMBALLS = 60; // # balls to simulate //***
    private static final double MINSIZE = 1.0; // Minimum ball radius (meters ) //***
    private static final double MAXSIZE = 7.0; // Maximum ball radius (meters) //***
    private static final double EMIN = 0.2; // Minimum loss coefficient //***
    private static final double EMAX = 0.6; // Maximum loss coefficient //***
    private static final double VoMIN = 40.0; // Minimum velocity (meters/sec) //***
    private static final double VoMAX = 50.0; // Maximum velocity (meters/sec) //***
    private static final double ThetaMIN = 80.0; // Minimum launch angle (degrees) //***
    private static final double ThetaMAX = 100.0; // Maximum launch angle (degrees) //***
    private RandomGenerator rgen = RandomGenerator.getInstance(); //***
    private boolean simEnable = false;
    private JPanel inputPanel = new JPanel(new TableLayout(9,2));
    private JPanel chooseBoxPanel = new JPanel();
    private JPanel tracePanel = new JPanel();
    private JComboBox bSimC = new JComboBox();
    // Creating instance of bTree class
    private bTree myTree = new bTree(); //***

    public void init() {
        //…other code…

        add(inputPanel, EAST);
        //inputPanel.setLayout(new TableLayout(9,2));
        //setLayout(new TableLayout(9, 2));

        inputPanel.add((new JLabel("NUMBALLS")));
        IntField numballsField = new IntField( 1,  255);
        inputPanel.add((numballsField));
        numballsField.addActionListener(this);

        inputPanel.add((new JLabel("MIN SIZE")));
        DoubleField minSizeField = new DoubleField( 1.0,  25.0);
        inputPanel.add((minSizeField));
        minSizeField.addActionListener(this);

        inputPanel.add((new JLabel("MAX SIZE")));
        DoubleField maxSizeField = new DoubleField( 1.0,  25.0);
        inputPanel.add((maxSizeField));
        maxSizeField.addActionListener(this);

        inputPanel.add((new JLabel("LOSS MIN")));
        DoubleField lossMinField = new DoubleField( 0.0,  1.0);
        inputPanel.add((lossMinField));
        lossMinField.addActionListener(this);

        inputPanel.add((new JLabel("LOSS MAX")));
        DoubleField lossMaxField = new DoubleField( 0.0,  1.0);
        inputPanel.add((lossMaxField));
        lossMaxField.addActionListener(this);

        inputPanel.add((new JLabel("MIN VEL")));
        DoubleField minVelField = new DoubleField( 1.0,  200.0);
        inputPanel.add((minVelField));
        minVelField.addActionListener(this);

        inputPanel.add((new JLabel("MAX VEL")));
        DoubleField maxVelField = new DoubleField( 1.0,  200.0);
        inputPanel.add((maxVelField));
        maxVelField.addActionListener(this);

        inputPanel.add((new JLabel("TH MAX")));
        DoubleField thetaMinField = new DoubleField( 1.0,  180.0);
        inputPanel.add((thetaMinField));
        thetaMinField.addActionListener(this);

        inputPanel.add((new JLabel("TH MAX")));
        DoubleField thetaMaxField = new DoubleField( 1.0,  180.0);
        inputPanel.add((thetaMaxField));
        thetaMaxField.addActionListener(this);

        add(chooseBoxPanel, NORTH);

        chooseBoxPanel.add(bSimC);
        bSimC.addItem("bSim");
        bSimC.addItem("Run");
        bSimC.addItem("Stack");
        bSimC.addItem("Clear");
        bSimC.addItem("Stop");
        bSimC.addItem("Quit");
        bSimC.setEditable(false);

        add(tracePanel, SOUTH);
        tracePanel.add(new JButton("Trace"));



//
//        // Sets the value of a field.
//        numballsField.setValue(4);
//        double d = numballsField.getValue();
//        numballsField.addActionListener(this);
//
//
//
//
//
//        fahrenheitField = new IntField(32);
//        fahrenheitField.setActionCommand("F -> C");
//        fahrenheitField.addActionListener(this);
//        celsiusField = new IntField(0);
//        celsiusField.setActionCommand("C -> F");
//        celsiusField.addActionListener(this);
//        add((new JLabel("Degrees Fahrenheit")),NORTH);
//        add(fahrenheitField);
//        add(new JButton("F -> C"));
//        add(new JLabel("Degrees Celsius"));
//        add(celsiusField);
//        add(new JButton("C -> F"));
//        addActionListeners();
//
            while(true) {
                pause(200);
                if (simEnable) { // Run once, then stop
                    doSim();
                    bSimC.setSelectedIndex(0);
                    simEnable = false;
                }
            }

    }

//    /* Listens for a button action */
//    public void actionPerformed(ActionEvent e) {
//        String cmd = e.getActionCommand();
//        if (cmd.equals("F -> C")) {
//            int f = fahrenheitField.getValue();
//            int c = GMath.round((5.0 / 9.0) * (f - 32));
//            celsiusField.setValue(c);
//        } else if (cmd.equals("C -> F")) {
//            int c = celsiusField.getValue();
//            int f = GMath.round((9.0 / 5.0) * c + 32);
//            fahrenheitField.setValue(f);
//        }
//    }
//
    public void itemStateChanged(ItemEvent e) {
        JComboBox source = (JComboBox)e.getSource();
        if (source == bSimC) {
            if (bSimC.getSelectedIndex() == 1) {
                System.out.println("Running simulation");
                doSim();
            }
            else if (bSimC.getSelectedIndex() == 2) {
                System.out.println("Stacking balls");
                doStack();
            }
            else if (bSimC.getSelectedIndex() == 3) {
                System.out.println("Clearing");
                clear();
            }
            else if (bSimC.getSelectedIndex() == 4) {
                System.out.println("Stopping");
                stop();
            }
            else if (bSimC.getSelectedIndex() == 2) {
                System.out.println("Qutiing");
                quit();
            }
           // etc…
        }
    }

    public void doSim() {

        int numBalls = numballsField.this.getValue();
        double minSize =;
        double maxSize =;
        double lossMin =;
        double lossMax =;
        double minVel =;
        double maxVel =;
        double thMin =;
        double thMax =;
        // for loop to randomize and create 60 different balls
        for (int i = 1; i <= NUMBALLS; i++) {
            // Randomizing the different aBall parameters with boundaries
            double bSize = rgen.nextDouble(minSize, maxSize); //***
            Color bColor = rgen.nextColor(); //***
            double bLoss = rgen.nextDouble(lossMin, lossMax); //***
            double bVel = rgen.nextDouble(minVel, maxVel); //***
            double theta = rgen.nextDouble(thMin, thMax); //***

            // Creating the ball with the previously randomly generate parameters
            aBall iBall = new aBall((WIDTH/2)/SCALE,bSize,bVel,theta,bSize,bColor,bLoss,this); // Adding the ball // Null for no trace, this for trace
            add(iBall.getBall()); //*** Getting the ball object
            myTree.addNode(iBall); //*** Adding balls to a bTree
            iBall.start(); // Starting the ball simulation
            while (myTree.isRunning()) {} //*** Loop to block following code from running
            GLabel label1 = new GLabel("Simulation completed", WIDTH/2, HEIGHT/2); // Message to click to stack balls
            label1.setFont("SansSerif-24");
            label1.setColor(Color.RED);
            add(label1);
            //label1.setVisible(false); // Making the first message invisible (found from looking through Glabel methods)

        }

    }

    public void doStack() {
        myTree.stackBalls(); //*** Lay out balls in order
//        GLabel label2 = new GLabel("All Stacked!", WIDTH/2, HEIGHT/2); // Message that balls are stacked
//        label2.setFont("SansSerif-36");
//        label2.setColor(Color.RED);
//        add(label2);
//
    }

    public void clear() {
    }
    public void stop() {
    }
    public void quit() {
    }

//    /* Private instance variables */
//    private IntField fahrenheitField;
//    private IntField celsiusField;
//
//
//    public void run() {
//        this.resize(WIDTH, HEIGHT + OFFSET); //*** size display window
//
//        // Ground plane
//        GRect rect = new GRect(0, HEIGHT, 1200, 3);
//        rect.setFilled(true);
//        add(rect);
//
//        // Set seed for randomness
//        rgen.setSeed((long) 424242); //***
//
//
//
//        // for loop to randomize and create 60 different balls
//        for (int i = 1; i <= NUMBALLS; i++) {
//            // Randomizing the different aBall parameters with boundaries
//            double bSize = rgen.nextDouble(MINSIZE, MAXSIZE); //***
//            Color bColor = rgen.nextColor(); //***
//            double bLoss = rgen.nextDouble(EMIN, EMAX); //***
//            double bVel = rgen.nextDouble(VoMIN, VoMAX); //***
//            double theta = rgen.nextDouble(ThetaMIN, ThetaMAX); //***
//
//            // Creating the ball with the previously randomly generate parameters
//            aBall iBall = new aBall((WIDTH/2)/SCALE,bSize,bVel,theta,bSize,bColor,bLoss,this); // Adding the ball // Null for no trace, this for trace
//            add(iBall.getBall()); //*** Getting the ball object
//            myTree.addNode(iBall); //*** Adding balls to a bTree
//            iBall.start(); // Starting the ball simulation
//
//        }
//
//        while (myTree.isRunning()) {} //*** Loop to block following code from running
//        GLabel label1 = new GLabel("Click mouse to continue", WIDTH/2, HEIGHT/2); // Message to click to stack balls
//        label1.setFont("SansSerif-24");
//        label1.setColor(Color.RED);
//        add(label1);
//        //Code to wait for a mouse click // Wait
//        this.waitForClick(); // Wait for user to click to continue program (found from looking through Glabel methods)
//        label1.setVisible(false); // Making the first message invisible (found from looking through Glabel methods)
//        myTree.stackBalls(); //*** Lay out balls in order
//        GLabel label2 = new GLabel("All Stacked!", WIDTH/2, HEIGHT/2); // Message that balls are stacked
//        label2.setFont("SansSerif-36");
//        label2.setColor(Color.RED);
//        add(label2);
//
//
//    }
}
