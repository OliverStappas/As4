import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import java.awt.*;

public class bSim extends GraphicsProgram {
    // Parameters used in this program
    private static final int WIDTH = 1200; // n.b. screen coordinates
    private static final int HEIGHT = 600;
    private static final int OFFSET = 200;
    private static final double SCALE = HEIGHT/100; // pixels per meter
    private static final int NUMBALLS = 100; // # balls to simulate
    private static final double MINSIZE = 1.0; // Minumum ball radius (meters)
    private static final double MAXSIZE = 10.0; // Maximum ball radius (meters)
    private static final double EMIN = 0.1; // Minimum loss coefficient
    private static final double EMAX = 0.6; // Maximum loss coefficient
    private static final double VoMIN = 40.0; // Minimum velocity (meters/sec)
    private static final double VoMAX = 50.0; // Maximum velocity (meters/sec)
    private static final double ThetaMIN = 80.0; // Minimum launch angle (degrees)
    private static final double ThetaMAX = 100.0; // Maximum launch angle (degrees)
    private static RandomGenerator rgen = new RandomGenerator();


    public void run() {
// Set up display, create and start multiple instances of
        this.resize(WIDTH, HEIGHT + OFFSET); // size display window

        // Ground plane
        GRect rect = new GRect(0, HEIGHT, 1200, 3);
        rect.setFilled(true);
        add(rect);

        // Set seed
        rgen.setSeed((long) 0.12345);

        for (int i = 1; i < NUMBALLS; i++) {

            double Vo = rgen.nextDouble(VoMIN, VoMAX);
            double theta = rgen.nextDouble(ThetaMIN, ThetaMAX);
            double bSize = rgen.nextDouble(MINSIZE, MAXSIZE);
            Color bColor = rgen.nextDouble(EMIN, EMAX);
            double bLoss = rgen.nextDouble(EMIN, EMAX);

        }

        // Test for one ball
        //aBall redBall = new aBall(10.0,100.0,1.0,30.0,6.0,Color.RED,0.25);
        aBall redBall = new aBall(5,1,40,85,1,Color.RED,0.4);
        add(redBall.getBall());
        redBall.start();

        // For example, to generate a random loss parameter, one would use an
        // instance of the RandomGenerator class as follows:
        //double iLoss = rgen.nextDouble(EMIN ,EMAX );
    }
}

