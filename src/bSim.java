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

public class bSim extends GraphicsProgram implements ItemListener {
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
    private IntField numballsField;
    private DoubleField minSizeField;
    private DoubleField maxSizeField;
    private DoubleField lossMinField;
    private DoubleField lossMaxField;
    private DoubleField minVelField;
    private DoubleField maxVelField;
    private DoubleField thetaMinField;
    private DoubleField thetaMaxField;
    private boolean traceOn = false;
    private bSim traceStatus = null;
    private bSim doTrace = this;
    GRect rect = new GRect(0, HEIGHT, 1200, 3);


    public void init() {
        //…other code

        this.resize(WIDTH + 152, HEIGHT + OFFSET); //*** size display window

        rect.setFilled(true);
        add(rect);

        // Set seed for randomness
        rgen.setSeed((long) 424242); //***

        add(inputPanel, EAST);
        //inputPanel.setLayout(new TableLayout(9,2));
        //setLayout(new TableLayout(9, 2));

        inputPanel.add((new JLabel("NUMBALLS")));
        numballsField = new IntField(60, 1,  255);
        inputPanel.add((numballsField));

        inputPanel.add((new JLabel("MIN SIZE")));
        minSizeField = new DoubleField(1.0, 1.0,  25.0);
        inputPanel.add((minSizeField));

        inputPanel.add((new JLabel("MAX SIZE")));
        maxSizeField = new DoubleField( 7.0,1.0,  25.0);
        inputPanel.add((maxSizeField));

        inputPanel.add((new JLabel("LOSS MIN")));
        lossMinField = new DoubleField( 0.2,0.0,  1.0);
        inputPanel.add((lossMinField));

        inputPanel.add((new JLabel("LOSS MAX")));
        lossMaxField = new DoubleField( 0.6,0.0,  1.0);
        inputPanel.add((lossMaxField));

        inputPanel.add((new JLabel("MIN VEL")));
        minVelField = new DoubleField( 40.0,1.0,  200.0);
        inputPanel.add((minVelField));

        inputPanel.add((new JLabel("MAX VEL")));
        maxVelField = new DoubleField( 50.0,1.0,  200.0);
        inputPanel.add((maxVelField));

        inputPanel.add((new JLabel("TH MAX")));
        thetaMinField = new DoubleField( 80.0,1.0,  180.0);
        inputPanel.add((thetaMinField));

        inputPanel.add((new JLabel("TH MAX")));
        thetaMaxField = new DoubleField(100.0, 1.0,  180.0);
        inputPanel.add((thetaMaxField));

        add(chooseBoxPanel, NORTH);

        chooseBoxPanel.add(bSimC);
        bSimC.addItem("bSim");
        bSimC.addItem("Run");
        bSimC.addItem("Stack");
        bSimC.addItem("Clear");
        bSimC.addItem("Stop");
        bSimC.addItem("Quit");
        bSimC.addItemListener(this);

        add(tracePanel, SOUTH);
        JToggleButton traceButton = new JToggleButton("Trace");
        tracePanel.add(traceButton);
        //This portion of the code was taken from the java doc online
        traceButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    traceStatus = doTrace;
                }
                else{
                    traceStatus = null;

                }
            }
        });
    }

    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            JComboBox source = (JComboBox) e.getSource();
            if (source == bSimC) {
                if (bSimC.getSelectedIndex() == 1) {
                    System.out.println("Running simulation");
                    doSim();
                    bSimC.setSelectedIndex(0);
                } else if (bSimC.getSelectedIndex() == 2) {
                    System.out.println("Stacking balls");
                    doStack();
                    bSimC.setSelectedIndex(0);

                } else if (bSimC.getSelectedIndex() == 3) {
                    System.out.println("Clearing");
                    clear();
                    bSimC.setSelectedIndex(0);

                } else if (bSimC.getSelectedIndex() == 4) {
                    System.out.println("Stopping");
                    stop();
                    bSimC.setSelectedIndex(0);

                } else if (bSimC.getSelectedIndex() == 5) {
                    System.out.println("Quitting");
                    //  quit();
                    bSimC.setSelectedIndex(0);

                }
            }
        }
    }



    public void doSim() {

        int numBalls = numballsField.getValue();
        double minSize = minSizeField.getValue();
        double maxSize = maxSizeField.getValue();
        double lossMin = lossMinField.getValue();
        double lossMax = lossMaxField.getValue();
        double minVel = minVelField.getValue();
        double maxVel = maxVelField.getValue();
        double thMin = thetaMinField.getValue();
        double thMax = thetaMaxField.getValue();

        // for loop to randomize and create 60 different balls
        for (int i = 1; i <= numBalls; i++) {
            // Randomizing the different aBall parameters with boundaries
            double bSize = rgen.nextDouble(minSize, maxSize); //***
            Color bColor = rgen.nextColor(); //***
            double bLoss = rgen.nextDouble(lossMin, lossMax); //***
            double bVel = rgen.nextDouble(minVel, maxVel); //***
            double theta = rgen.nextDouble(thMin, thMax); //***

            // Creating the ball with the previously randomly generate parameters
            aBall iBall = new aBall((WIDTH/2)/SCALE,bSize,bVel,theta,bSize,bColor,bLoss,traceStatus); // Adding the ball // Null for no trace, this for trace
            add(iBall.getBall()); //*** Getting the ball object
            myTree.addNode(iBall); //*** Adding balls to a bTree
            iBall.start(); // Starting the ball simulation
//            while (myTree.isRunning()) {} //*** Loop to block following code from running
//            GLabel label1 = new GLabel("Simulation completed", WIDTH/2, HEIGHT/2); // Message to click to stack balls
//            label1.setFont("SansSerif-24");
//            label1.setColor(Color.RED);
//            add(label1);
            //label1.setVisible(false); // Making the first message invisible (found from looking through Glabel methods)

        }

    }

    public void doStack() {
        if(!myTree.isRunning())
        myTree.stackBalls(); //*** Lay out balls in order
//        GLabel label2 = new GLabel("All Stacked!", WIDTH/2, HEIGHT/2); // Message that balls are stacked
//        label2.setFont("SansSerif-36");
//        label2.setColor(Color.RED);
//        add(label2);
//
    }

    public void clear() {
        myTree.stopBalls();
        this.removeAll();
        rect.setFilled(true);
        add(rect);
        myTree.root = null;
    }
//    public void stop() {
//    }
//    public void quit() {
//    }
    public void run() {
        while(true) {
            pause(200);
            if (simEnable) { // Run once, then stop
                doSim();
                bSimC.setSelectedIndex(0);
                simEnable = false;
            }
        }
    }


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
