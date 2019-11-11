// Comments with *** in front them are taken from the ECSE 202 Assignment 1, 2, 3 and 4 Instructions

import acm.graphics.*;
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
    private RandomGenerator rgen = RandomGenerator.getInstance(); //***
    private boolean simEnable = false; //*** Whether or not to do bSim

    // Creating the three different panels on the display
    private JPanel inputPanel = new JPanel(new TableLayout(9,2));
    private JPanel chooseBoxPanel = new JPanel();
    private JPanel tracePanel = new JPanel();

    // Creating the combobox on the top of the screen
    private JComboBox bSimC = new JComboBox();

    // Creating instance of bTree class
    private bTree myTree = new bTree(); //***

    // Creating value fields for user input
    private IntField numballsField;
    private DoubleField minSizeField;
    private DoubleField maxSizeField;
    private DoubleField lossMinField;
    private DoubleField lossMaxField;
    private DoubleField minVelField;
    private DoubleField maxVelField;
    private DoubleField thetaMinField;
    private DoubleField thetaMaxField;

    private bSim traceStatus = null; // Status of whether or not to do trace
    private bSim doTrace = this; // The value that traceStatus will be equal to when we want the tracepoints

    // Creating the floor
    GRect rect = new GRect(0, HEIGHT, 1200, 3);

    /**
     * Running the ball and screen simulation
     */
    public void init() {

        this.resize(WIDTH + 152, HEIGHT + OFFSET + 100); //*** size display window

        // Filling and adding the floor to the screen
        rect.setFilled(true);
        add(rect);

        // Set seed for randomness
        rgen.setSeed((long) 424242); //***

        // Adding an input panel on the right of the screen
        add(inputPanel, EAST);

        // Adding the different int and double fields with mins and maxes, and titles
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

        inputPanel.add((new JLabel("TH MIN")));
        thetaMinField = new DoubleField( 80.0,1.0,  180.0);
        inputPanel.add((thetaMinField));

        inputPanel.add((new JLabel("TH MAX")));
        thetaMaxField = new DoubleField(100.0, 1.0,  180.0);
        inputPanel.add((thetaMaxField));

        // Adding the choosebox panel at the top of the screen
        add(chooseBoxPanel, NORTH);

        // Adding the choosebox to the choosebox panel
        chooseBoxPanel.add(bSimC);

        // Adding the different options names to the choosebox
        bSimC.addItem("bSim");
        bSimC.addItem("Run");
        bSimC.addItem("Stack");
        bSimC.addItem("Clear");
        bSimC.addItem("Stop");
        bSimC.addItem("Quit");
        bSimC.addItemListener(this); // Listening for user action

        // Adding the tracepanel at the bottom of the screen
        add(tracePanel, SOUTH);
        JToggleButton traceButton = new JToggleButton("Trace"); // Creating a trace button as a JToggleButton
        tracePanel.add(traceButton); // Adding the traceButton to the tracePanel

        // This portion of the code was taken from the java doc online. It makes the program trace if it the button is toggled, and do nothing otherwise
        traceButton.addItemListener(new ItemListener() {
            @Override
            /**
             *  Class to process the user toggling the trace button. If it's pressed: trace.
             */
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){ // If the button is presses
                    traceStatus = doTrace; // Trace the movement of the next balls
                }
                else {
                    traceStatus = null; // Don't trace

                }
            }
        });

    }

    /**
     * Class that processes which button the user presses in the combo box and then does the corresponding action
     * @param e An event (user clicking something)
     */
    public void itemStateChanged(ItemEvent e) { //***
        if(e.getStateChange() == ItemEvent.SELECTED) { //***
            JComboBox source = (JComboBox) e.getSource(); //***
            if (source == bSimC) { //*** If the user clicks on the combo box
                // If the user picks the following buttons in the combo box:
                if (bSimC.getSelectedIndex() == 1) { //*** Simulate the balls
                    System.out.println("Running simulation");
                    simEnable = true; //***

                } else if (bSimC.getSelectedIndex() == 2) { // Stack the balls
                    System.out.println("Stacking balls");
                    doStack(); //***
                    bSimC.setSelectedIndex(0); // Going back to the first button

                } else if (bSimC.getSelectedIndex() == 3) { // Clear the screen
                    System.out.println("Clearing");
                    clear();
                    bSimC.setSelectedIndex(0);

                } else if (bSimC.getSelectedIndex() == 4) { // Stop the ball simulation
                    System.out.println("Stopping");
                    stop();
                    bSimC.setSelectedIndex(0);

                } else if (bSimC.getSelectedIndex() == 5) { // Exit the program
                    System.out.println("Quitting");
                    System.exit(0);
                    bSimC.setSelectedIndex(0);

                }
            }
        }
    }


    /**
     * Starts a ball simulation based on the user input
     */
    public void doSim() {

        // Getting the values of the different entry fields
        int numBalls = numballsField.getValue();
        double minSize = minSizeField.getValue();
        double maxSize = maxSizeField.getValue();
        double lossMin = lossMinField.getValue();
        double lossMax = lossMaxField.getValue();
        double minVel = minVelField.getValue();
        double maxVel = maxVelField.getValue();
        double thMin = thetaMinField.getValue();
        double thMax = thetaMaxField.getValue();

        // for loop to randomize and create numBalls different balls
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

        }
//        if (!myTree.isRunning()) {
//            GLabel label1 = new GLabel("Simulation completed", WIDTH / 2, HEIGHT / 2); // Message to click to stack balls
//            label1.setFont("SansSerif-24");
//            label1.setColor(Color.RED);
//            add(label1);
//            //label1.setVisible(false); // Making the first message invisible (found from looking through Glabel methods)
//        }

    }

    /**
     * Stacks all the balls on the screen if they have all stopped moving
     */
    public void doStack() {
        if(!myTree.isRunning()) // Only stack if none of the balls are running
        myTree.stackBalls(); //*** Lay out balls in order
//        GLabel label2 = new GLabel("All Stacked!", WIDTH/2, HEIGHT/2); // Message that balls are stacked
//        label2.setFont("SansSerif-36");
//        label2.setColor(Color.RED);
//        add(label2);
//
    }

    /**
     * Clearing the display on the screen
     */
    public void clear() {
        myTree.stopBalls(); // Stop the balls from moving
        this.removeAll(); // Remove everything from the screen

        // Re-adding the floor on the screen
        rect.setFilled(true);
        add(rect);

        myTree.root = null; // Making the tree empty
    }

    public void stop() {
        myTree.stopBalls(); // Stop the simulation
    }

    /**
     * Loop that runs the screen simulation if the user presses that button in the combo box
     */
    public void run() {
        while(true) {
            pause(200);

            if (simEnable) { // Run once, then stop
                doSim(); // Do the ball simulation
                bSimC.setSelectedIndex(0); // Go back to the first button
                simEnable = false; // Don't run anymore
            }
        }
    }
}
