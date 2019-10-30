//Since the stackBalls() method in the bTree class does not need to reference any methods in the GraphicsProgram class,
// there is no need to include a link to bSim as the method argument.  Consequently the method signature for stackBalls
// is simply void stackBalls().

public class bTree {

    // Instance variables

    private double X; // X offset
    private double Y; // Y offset
    private double lastSize;
    private double DELTASIZE = 0.1;


    bNode root = null;

    /**
     * addNode method - adds a new node by descending to the leaf node
     *                  using a while loop in place of recursion.  Ugly,
     *                  yet easy to understand.
     */


    public void addNode(aBall iBall) {

        bNode current;

// Empty tree

        if (root == null) {
            root = makeNode(iBall);
        }

// If not empty, descend to the leaf node according to
// the input iBall.

        else {
            current = root;
            while (true) {
                if (iBall.getBSize() < current.data.getBSize()) {

// New iBall < iBall at node, branch left

                    if (current.left == null) {				// leaf node
                        current.left = makeNode(iBall);		// attach new node here
                        break;
                    }
                    else {									// otherwise
                        current = current.left;				// keep traversing
                    }
                }
                else {
// New iBall >= iBall at node, branch right

                    if (current.right == null) {			// leaf node
                        current.right = makeNode(iBall);		// attach
                        break;
                    }
                    else {									// otherwise
                        current = current.right;			// keep traversing
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
        bNode node = new bNode();							// create new object
        node.data = data;									// initialize data field
        node.left = null;									// set both successors
        node.right = null;									// to null
        return node;										// return handle to new object
    }

    /**
     * traverse_inorder method - recursively traverses tree in order (LEFT-Root-RIGHT) and prints each node.
     */

    private void traverse_inorder(bNode root) {
        if (root != null) {
            traverse_inorder(root.left);
            processBall(root);
            traverse_inorder(root.right);
        }
    }

    private void processBall(bNode root) {
        double currentSize = root.data.getBSize();
        if (lastSize == 0) { // Condition for first ball
            X = Y = currentSize;
        }
        else if (currentSize - lastSize > DELTASIZE) { // condition for new stack
            X += 2 * currentSize;
            Y = currentSize;

        } else { // put on top of current stack (if the same size as previous ball)
            Y += 2 * currentSize;
        }
        root.data.moveTo(X, Y);
        lastSize = currentSize;
    }


    private boolean traverseRunning (bNode root) {
        if (root != null) {
            if (root.data.getRunningStatus() || traverseRunning(root.left) || traverseRunning(root.right)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRunning() { //method based on the in-order traversal routine that scans the B-Tree and checks the status of each aBall,
        return traverseRunning(root);
    }

    public void stackBalls() {
        traverse_inorder(root);

    }

}

/**
 * A simple bNode class for use by bTree.  The "payload" can be
 * modified accordingly to support any object type.
 *
 * @author ferrie
 *
 */

class bNode {
    aBall data;
    bNode left;
    bNode right;
}


