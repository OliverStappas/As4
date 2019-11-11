// Comments with *** in front them are taken from the ECSE 202 Assignment 1, 2, 3 and 4 Instructions

/**
 *  A binary tree class that processes balls with respect to their sizes
 */
public class bTree {

    // Instance variables

    private double X; //*** X offset
    private double Y; //*** Y offset
    private double lastSize; //*** Size of last ball that gets processed

    bNode root = null; //***

    /**
     * addNode method - adds a new node by descending to the leaf node
     *                  using a while loop in place of recursion.  Ugly,
     *                  yet easy to understand.
     */


    public void addNode(aBall iBall) { //***

        bNode current; //***

// Empty tree

        if (root == null) { //***
            root = makeNode(iBall); //***
        } //***

// If not empty, descend to the leaf node according to
// the input iBall.

        else { //***
            current = root; //***
            while (true) { //***
                if (iBall.getBSize() < current.data.getBSize()) { //***

// New iBall < iBall at node, branch left

                    if (current.left == null) {				//*** leaf node
                        current.left = makeNode(iBall);		//*** attach new node here
                        break;
                    }
                    else {									//*** otherwise
                        current = current.left;				//*** keep traversing
                    }
                }
                else {
// New iBall >= iBall at node, branch right

                    if (current.right == null) {			//*** leaf node
                        current.right = makeNode(iBall);		//*** attach
                        break;
                    }
                    else {									//*** otherwise
                        current = current.right;			//*** keep traversing
                    }
                }
            }
        }

    }

    /**
     * makeNode
     *
     * Creates a single instance of a bNode
     *
     * @param	int data   Data to be added
     * @return  bNode node Node created
     */

    bNode makeNode(aBall data) {
        bNode node = new bNode();							//***create new object
        node.data = data;									//*** initialize data field
        node.left = null;									//*** set both successors
        node.right = null;									//*** to null
        return node;										//*** return handle to new object
    }

    /**
     *
     * @param root Ball node in the bTree
     * traverseInOrderAndStack method - recursively traverses tree in order (LEFT-Root-RIGHT) and stacks the balls in the bTree.
     */
    private void traverseInOrderAndStack(bNode root) { //***
        if (root != null) {
            traverseInOrderAndStack(root.left); //***
            processBall(root); // Stacking the balls, using a separate method to make it more legible
            traverseInOrderAndStack(root.right); //***
        }
    }

    /**
     * Method used in the traverseInOrderAndStack method that updates X and Y offset values and stacks balls in previous
     * stacks or in new ones if necessary
     * @param root Ball node in the bTree
     */
    private void processBall(bNode root) {
        double currentSize = root.data.getBSize();
        double DELTASIZE = 0.1;
        if (lastSize == 0) { // Condition for first ball (no previous ball to compare to)
            X = Y = currentSize;
        }
        else if (currentSize - lastSize > DELTASIZE) { // condition for new stack
            X += 2 * currentSize;
            Y = currentSize;

        } else { // put on top of current stack (if the same size as previous ball)
            Y += 2 * currentSize;
        }
        root.data.moveTo(X, Y); // Move the ball where it belongs
        lastSize = currentSize; // Setting the lastSize to the current one for the next iteration
    }

    /**
     * @param root Ball node in the bTree
     * @return Returns whether or not all the balls in the bTree are still running (bouncing)
     */
    private boolean traverseRunning (bNode root) {
        if (root != null) {
            if (root.data.getRunningStatus() || traverseRunning(root.left) || traverseRunning(root.right)) { // If the current ball is still running
                return true;
            }
        }
        return false; // Returns false if all the balls have stopped running
    }

    /**
     * Method that performs traverseRunning (checks if all the balls are still running) on the root without needing a parameter
     * @return Returns whether all the balls in the bTree are still running or not
     */
    public boolean isRunning() { //method that performs traverseRunning on the root without needing a parameter
        return traverseRunning(root);
    }

    /**
     * Method that performs traverseInOrderAndStack (stacks the balls in the bTree) on the root without needing a parameter
     */
    public void stackBalls() { //method that performs traverseInOrderAndStack on the root without needing a parameter
        traverseInOrderAndStack(root);
        lastSize = 0;

    }

    /**
     * Method that traverses the ball tree and stops all the balls
     * @param root Ball node in the bTree
     */
    private void stopBallsTraversal(bNode root) {
        if (root.left != null) {
            stopBallsTraversal(root.left);
            root.left.data.setStatus(false);
        }
            root.data.setStatus(false);
        if (root.right != null) {
            stopBallsTraversal(root.right);
        }
    }

    /**
     * Method that stops all the balls in the tree without needing a parameter
     */
    public void stopBalls() {
        stopBallsTraversal(root);
    }

}

/**
 * A simple bNode class for use by bTree.  The "payload" can be
 * modified accordingly to support any object type.
 *
 * @author ferrie
 *
 */

class bNode { //***
    aBall data; //***
    bNode left; //***
    bNode right; //***
}


