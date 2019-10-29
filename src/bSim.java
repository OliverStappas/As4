import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import java.awt.*;


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


    public void run() {
        this.resize(WIDTH, HEIGHT + OFFSET); //*** size display window

        // Ground plane
        GRect rect = new GRect(0, HEIGHT, 1200, 3);
        rect.setFilled(true);
        add(rect);

        // Creating instance of bTree class
        bTree myTree = new bTree(); //***

        // Set seed for randomness
        rgen.setSeed((long) 424242); //***



        // for loop to randomize and create 100 different balls
        for (int i = 1; i <= NUMBALLS; i++) {
            // Randomizing the different aBall parameters with boundaries
            double bSize = rgen.nextDouble(MINSIZE, MAXSIZE); //***
            Color bColor = rgen.nextColor(); //***
            double bLoss = rgen.nextDouble(EMIN, EMAX); //***
            double bVel = rgen.nextDouble(VoMIN, VoMAX); //***
            double theta = rgen.nextDouble(ThetaMIN, ThetaMAX); //***

            // Creating the ball with the previously randomly generate parameters
            aBall iBall = new aBall((WIDTH/2)/SCALE,bSize,bVel,theta,bSize,bColor,bLoss,null); // Adding the ball (add
                                                                                                       // link instead of null if
                                                                                                       // you want tracepoints)
            add(iBall.getBall());
            myTree.addNode(iBall);
            iBall.start();

        }

        while (myTree.isRunning()) {
            // Block until termination
            try { // pause for 50 milliseconds  //***
                Thread.sleep(5);            //*** should be 50
            } catch (InterruptedException e) {  //***
                e.printStackTrace();            //***
            }
        }
        println("Death");
        GLabel label1 = new GLabel("Click mouse to continue", WIDTH/2, HEIGHT/2);
        label1.setFont("SansSerif-36");
        label1.setColor(Color.RED);
        add(label1);
        //Code to wait for a mouse click // Wait
        this.waitForClick(); // Wait for user to click to continue program
        label1.setVisible(false);
        myTree.stackBalls(); // Lay out balls in order
        GLabel label2 = new GLabel("All Stacked!", WIDTH/2, HEIGHT/2);
        label2.setFont("SansSerif-36");
        label2.setColor(Color.RED);
        add(label2);


        // Test for one ball
        // aBall redBall = new aBall(10.0,100.0,1.0,30.0,6.0,Color.RED,0.25,this); //***
        // aBall redBall = new aBall(5,1,40,85,1,Color.RED,0.4,this); //***
        // add(redBall.getBall()); //***
        // redBall.start(); //***

    }
}
