import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import java.awt.*;

public class aBall extends Thread {
    /**
     * The constructor specifies the parameters for simulation. They are
     *
     * @param Xi double The initial X position of the center of the ball
     * @param Yi double The initial Y position of the center of the ball
     * @param Vo double The initial velocity of the ball at launch
     * @param theta double Launch angle (with the horizontal plane)
     * @param bSize double The radius of the ball in simulation units
     * @param bColor Color The initial color of the ball
     * @param bLoss double Fraction [0,1] of the energy lost on each bounce
     */

    public aBall (double Xi, double Yi, double Vo, double theta,
                  double bSize, Color bColor, double bLoss) {

        this.Xi = Xi; // Get simulation parameters
        this.Yi = Yi;
        this.Vo = Vo;
        this.theta = theta;
        this.bSize = bSize;
        this.bColor = bColor;
        this.bLoss = bLoss;

    }
    public GOval getBall() {
        return myBall;
    }

    /**
     * The run method implements the simulation loop from Assignment 1.
     * Once the start method is called on the aBall instance, the
     * code in the run method is executed concurrently with the main
     * program.
     * @param void
     * @return void
     */

    public void run() {
// Simulation goes here...

        boolean running = true;

        // Simulation loop
        while (running) {
            // Update parameters
            double X = Vox * Vt / g * (1 - Math.exp(-g * time / Vt)) + Xo; // X position
            double Y = bSize + Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time; // Y position
            double Vx = (X - Xlast) / TICK; // Estimate Vx from difference
            double Vy = (Y - Ylast) / TICK; // Estimate Vy from difference

            // Detecting collision with ground
            if (Vy < 0 && Y <= bSize) {
                double KEx = 0.5 * Vx * Vx * (1 - loss); // Kinetic energy in X direction after collision
                double KEy = 0.5 * Vy * Vy * (1 - loss); // Kinetic energy in y direction after collision
                Vox = Math.sqrt(2 * KEx); // Resulting horizontal velocity
                Voy = Math.sqrt(2 * KEy); // Resulting vertical velocity

                // Reset variables
                Xo = X;
                time = 0;
                Y = bSize;

                // running = false;  // TEST FOR A1.pdf TO STOP LOOP AFTER ONE FLOOR COLLISION

                // When to stop program (If either Vx or Vy < ETHR)
                if ((KEx <= ETHR) || (KEy <= ETHR)) {
                    running = false;
                }
            }

            try { // pause for 50 milliseconds
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



public class Bounce extends GraphicsProgram {
    // parameters related to the display screen
    private static final int WIDTH = 600;  // defines the width of the screen in pixels
    private static final int HEIGHT = 600; // distance from top of screen to ground plane
    private static final int OFFSET = 200; // distance from bottom of screen to ground plane

    // parameters related to the simulation expressed in simulation coordinates.
    private static final double g = 9.8;               // MKS gravitational constant 9.8 m/s^2
    private static final double Pi = 3.141592654;      // To convert degrees to radians
    private static final double Xinit = 5.0; 	       // Initial ball location (X)
    private static final double TICK = 0.1;            // Clock tick duration (sec)
    private static final double ETHR = 0.01;           // If either Vx or Vy < ETHR STOP
    private static final double XMAX = 100.0;          // Maximum value of X.
    private static final double PD = 1;                // Trace point diameter
    private static final double SCALE = HEIGHT / XMAX; // Pixels/meter
    private static final double k = 0.0016;            // Air resistance parameter
    private static final boolean TEST = true;          // Print if test true

    public void run() {
        this.resize(WIDTH, HEIGHT + OFFSET); // size display window

        // Code to read simulation parameters from user.
        double Vo = readDouble("Enter the initial velocity of the ball in meters/second [0, 100]: ");
        double theta = readDouble("Enter the launch angle in degrees [0, 90]: ");
        double loss = readDouble("Enter energy loss parameter [0, 1]: ");
        double bSize = readDouble("Enter the radius of the ball in meters [0.1, 5.0]: ");

        // Ground plane
        GRect rect = new GRect(0, HEIGHT, 600, 3);
        rect.setFilled(true);
        add(rect);

        // Ball
        double bPixelSize = bSize * SCALE;
        GOval myBall = new GOval(Xinit * SCALE, HEIGHT - bPixelSize, bPixelSize * 2, bPixelSize * 2);
        myBall.setFilled(true);
        myBall.setColor(Color.RED);
        add(myBall); // Create the ball

        // Initialize variables
        double Vt = g / (4 * Pi * bSize * bSize * k); // Terminal velocity
        double Vox = Vo * Math.cos(theta * Pi / 180); // Initial x velocity
        double Voy = Vo * Math.sin(theta * Pi / 180); // Initial y velocity
        double Xlast = 0; // Previous X position
        double Ylast = 0; // Previous Y position
        double time = 0;
        double Xo = Xinit; // Offset X

        boolean running = true;

        // Simulation loop
        while (running) {
            // Update parameters
            double X = Vox * Vt / g * (1 - Math.exp(-g * time / Vt)) + Xo; // X position
            double Y = bSize + Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time; // Y position
            double Vx = (X - Xlast) / TICK; // Estimate Vx from difference
            double Vy = (Y - Ylast) / TICK; // Estimate Vy from difference

            // Detecting collision with ground
            if (Vy < 0 && Y <= bSize) {
                double KEx = 0.5 * Vx * Vx * (1 - loss); // Kinetic energy in X direction after collision
                double KEy = 0.5 * Vy * Vy * (1 - loss); // Kinetic energy in y direction after collision
                Vox = Math.sqrt(2 * KEx); // Resulting horizontal velocity
                Voy = Math.sqrt(2 * KEy); // Resulting vertical velocity

                // Reset variables
                Xo = X;
                time = 0;
                Y = bSize;

                // running = false;  // TEST FOR A1.pdf TO STOP LOOP AFTER ONE FLOOR COLLISION

                // When to stop program (If either Vx or Vy < ETHR)
                if ((KEx <= ETHR) || (KEy <= ETHR)) {
                    running = false;
                }
            }

            // Display update (Screen coordinates)
            int ScrX = (int) ((X - bSize) * SCALE);
            int ScrY = (int) (HEIGHT - (Y + bSize) * SCALE);

            // The current values of X and Y will be the previous positions for the next loop
            Xlast = X;
            Ylast = Y;

            // Moving red ball and drawing trace points
            myBall.setLocation(ScrX, ScrY);  // Moving the red ball to the desired screen coordinates
            GOval tracePoint = new GOval(X * SCALE, HEIGHT - Y * SCALE, PD, PD); // Initializing the tracepoints
            tracePoint.setFilled(true);
            add(tracePoint); // Drawing the tracepoints on the screen

            // Time Update
            time += TICK;

            // Animation delay
            pause(30); // Pause for 0.1 seconds

            if (TEST) // Printing coordinates, velocities and time throughout the animation
                System.out.printf("t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n",
                        time, Xo + X, Y, Vx, Vy);




        }
    }
}


