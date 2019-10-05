import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import java.awt.*;

public class aBall extends Thread  {

    private static final int WIDTH = 1200; // n.b. screen coordinates
    private static final int HEIGHT = 600;
    private static final int OFFSET = 200;
    private static final double SCALE = HEIGHT/100; // pixels per meter
    private static final double g = 9.8;               // MKS gravitational constant 9.8 m/s^2
    private static final double Pi = 3.141592654;      // To convert degrees to radians
    private static final double k = 0.0016;            // Air resistance parameter
    private static final double ETHR = 0.01;           // If either Vx or Vy < ETHR STOP
    private static final double PD = 1;                // Trace point diameter
    double TICK = 0.1;
    double Xi = 0;
    double Yi = 0;
    double Vo = 0;
    double theta = 0;
    double bSize = 0;
    Color bColor;
    double bLoss = 0;
    GOval myBall;

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

        // Ball
        double bPixelSize = this.bSize * SCALE;
        myBall = new GOval(this.Xi * SCALE, this.Yi * SCALE, bPixelSize * 2, bPixelSize * 2);
        myBall.setFilled(true);
        myBall.setColor(this.bColor);

    }
    public GOval getBall() {
        return myBall;
    }

    /**
     * The run method implements the simulation loop from Assignment 1.
     * Once the start method is called on the aBall instance, the
     * code in the run method is executed concurrently with the main
     * program.
     *
     * @return void
     */

    public void run() {
// Simulation goes here...
        double Vt = g / (4 * Pi * this.bSize * this.bSize * k); // Terminal velocity
        double Vox = this.Vo * Math.cos(theta * Pi / 180); // Initial x velocity
        double Voy = this.Vo * Math.sin(theta * Pi / 180); // Initial y velocity
        double Xlast = 0; // Previous X position
        double Ylast = 0; // Previous Y position
        double time = 0;
        double Xo = this.Xi; // Offset
        double KExLast = Double.POSITIVE_INFINITY;
        double KEyLast = Double.POSITIVE_INFINITY;
        double yOffset = this.Yi - this.bSize;

        boolean running = true;

        // Simulation loop
        while (running) {
            // Update parameters
            double X = Vox * Vt / g * (1 - Math.exp(-g * time / Vt)) + Xo; // X position
            double Y = Vt / g * (Voy + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time + yOffset; // Y position
            double Vx = (X - Xlast) / TICK; // Estimate Vx from difference
            double Vy = (Y - Ylast) / TICK; // Estimate Vy from difference

            // Detecting collision with ground
            if (Vy < 0 && Y <= this.bSize) {
                double KEx = 0.5 * Vx * Vx * (1 - this.bLoss); // Kinetic energy in X direction after collision
                double KEy = 0.5 * Vy * Vy * (1 - this.bLoss); // Kinetic energy in y direction after collision

                Vox = Math.sqrt(2 * KEx); // Resulting horizontal velocity
                Voy = Math.sqrt(2 * KEy); // Resulting vertical velocity

                if (theta > 90) {
                    Vox = -Vox;
                }

                // Reset variables
                Xo = X;
                time = 0;
                Y = this.bSize;
                yOffset = bSize;

                // When to stop program (If either Vx or Vy < ETHR)
                if ((KEx + KEy <= ETHR) || ((KEy + KEx) >= (KExLast + KEyLast))) {
                    running = false;
                }

                KExLast = KEx;
                KEyLast = KEy;

            }

            // Display update (Screen coordinates)
            int ScrX = (int) ((X - bSize) * SCALE);
            int ScrY = (int) (HEIGHT - (Y + bSize) * SCALE);

            // The current values of X and Y will be the previous positions for the next loop
            Xlast = X;
            Ylast = Y;

            // Moving red ball and drawing trace points
            myBall.setLocation(ScrX, ScrY);  // Moving the red ball to the desired screen coordinates
            //GOval tracePoint = new GOval(X * SCALE, HEIGHT - Y * SCALE, PD, PD); // Initializing the tracepoints
            //tracePoint.setFilled(true);
            //add(tracePoint); // Drawing the tracepoints on the screen

            // Time Update
            time += TICK;

            // Animation delay

            try { // pause for 50 milliseconds
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

