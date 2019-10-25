public class bTree {

    // Instance variables

    private double X;
    private double Y;
    private double lastSize;

    bNode root=null;

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
     * inorder method - inorder traversal via call to recursive method
     */

    public void inorder() {									// hides recursion from user
        traverse_inorder(root);
    }

    /**
     * traverse_inorder method - recursively traverses tree in order (LEFT-Root-RIGHT) and prints each node.
     */

    private void traverse_inorder(bNode root) {
        if (root.left != null) traverse_inorder(root.left);
        // processing for current node
        root.data.getBSize(); // Get size of ball at current node
        // Update values of X and Y to determine where to place it.
        /*Given a set of balls ordered from smallest to largest.
        2. For I = 1 to Number of balls in set
        3. If current size – last size > DELTASIZE
        4. Start a new stack
        5. Else
        6. Put current ball on top of last ball
        7. End*/
        for (int i = 1; i <= 60)
        //root.data.moveTo(X,Y); // to place the ball there
        //System.out.println(root.data);
        if (root.right != null) traverse_inorder(root.right);
    }

    public boolean isRunning() {
    }

    public void stackBalls() {

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


