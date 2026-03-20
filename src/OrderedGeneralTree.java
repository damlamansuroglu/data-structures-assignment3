import java.util.ArrayList;
import java.util.List;

/**
 * Assignment 3: Ordered General Tree Implementation
 * * This class implements an Ordered General Tree where each node can have an arbitrary
 * number of children. The implementation follows the "Left-Child / Right-Sibling"
 * representation principle (Binary Tree Representation of a General Tree).
 * * In this representation:
 * 1. The 'left child' reference points to the node's first child in the general tree.
 * 2. The 'right child' reference points to the node's next sibling in the general tree.
 * * This class extends the AbstractTree class provided in the course materials to
 * utilize standard tree functionalities.
 * * @author Damla
 * @param <E> the type of elements stored in the tree.
 */
public class OrderedGeneralTree<E> extends AbstractTree<E> {

    //-----------------------------------------------------------------------
    // Nested Node Class
    //-----------------------------------------------------------------------

    /**
     * Internal Node class that implements the Position interface.
     * This node structure is specifically designed for the Left-Child/Right-Sibling representation.
     */
    protected static class Node<E> implements Position<E> {
        // The element stored at this position
        private E element;

        // Reference to the parent node
        private Node<E> parent;

        // Reference to the first child of this node (equivalent to "left" in binary tree)
        private Node<E> firstChild;

        // Reference to the next sibling of this node (equivalent to "right" in binary tree)
        private Node<E> nextSibling;

        /**
         * Constructs a new node with the specified parameters.
         * * @param e           the element to be stored
         * @param p           the parent node
         * @param firstChild  the first child node
         * @param nextSibling the next sibling node
         */
        public Node(E e, Node<E> p, Node<E> firstChild, Node<E> nextSibling) {
            this.element = e;
            this.parent = p;
            this.firstChild = firstChild;
            this.nextSibling = nextSibling;
        }

        // --- Implementation of the Position interface ---

        /**
         * Returns the element stored at this position.
         *
         * @return the stored element.
         * @throws IllegalStateException if position is no longer valid.
         */
        @Override
        public E getElement() throws IllegalStateException {
            if (nextSibling == this) // Sentinel check for a removed/defunct node
                throw new IllegalStateException("Position no longer valid");
            return element;
        }

        // --- Accessor and Mutator methods for internal links ---

        /**
         * Returns the parent of this node.
         * @return the parent node reference.
         */
        public Node<E> getParent() { return parent; }

        /**
         * Returns the first child of this node.
         * In the binary tree representation, this corresponds to the 'left child'.
         * @return the first child node reference.
         */
        public Node<E> getFirstChild() { return firstChild; }

        /**
         * Returns the next sibling of this node.
         * In the binary tree representation, this corresponds to the 'right child'.
         * @return the next sibling node reference.
         */
        public Node<E> getNextSibling() { return nextSibling; }

        /**
         * Sets the element stored at this node.
         * @param e the new element to be stored.
         */
        public void setElement(E e) { this.element = e; }

        /**
         * Sets the parent of this node.
         * @param parent the new parent node reference.
         */
        public void setParent(Node<E> parent) { this.parent = parent; }

        /**
         * Sets the first child of this node.
         * This updates the 'left child' pointer in the binary representation.
         * @param firstChild the new first child node reference.
         */
        public void setFirstChild(Node<E> firstChild) { this.firstChild = firstChild; }

        /**
         * Sets the next sibling of this node.
         * This updates the 'right child' pointer in the binary representation.
         * @param nextSibling the new next sibling node reference.
         */
        public void setNextSibling(Node<E> nextSibling) { this.nextSibling = nextSibling; }
    }
    //---------------- end of nested Node class ----------------

    // Instance variables for the tree
    private Node<E> root = null; // The root of the tree
    private int size = 0;        // The number of nodes in the tree

    /** * Constructs an empty Ordered General Tree.
     */
    public OrderedGeneralTree() {
        // Default constructor creates an empty tree
    }

    /**
     * Validates the position and casts it to a Node.
     * Checks if the position is null, is of the wrong type, or has been removed.
     * * @param p the position to validate
     * @return the Position cast to a Node
     * @throws IllegalArgumentException if the position is invalid
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node))
            throw new IllegalArgumentException("Not valid position type");
        Node<E> node = (Node<E>) p;
        if (node.getParent() == node) // Convention: if parent points to itself, the node is removed
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    //-----------------------------------------------------------------------
    // Accessor Methods (Implementing abstract methods from Tree/AbstractTree)
    //-----------------------------------------------------------------------

    /**
     * Returns the root Position of the tree (or null if tree is empty).
     * @return the root position
     */
    @Override
    public Position<E> root() {
        return root;
    }

    /**
     * Returns the Position of the parent of position p (or null if p is the root).
     * * @param p the position to find the parent of
     * @return the parent position
     * @throws IllegalArgumentException if p is not a valid position
     */
    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getParent();
    }

    /**
     * Returns an iterable collection of the children of position p.
     * This method traverses the "nextSibling" references to collect all children.
     * * @param p the position to find children of
     * @return iterable collection of children positions
     * @throws IllegalArgumentException if p is not a valid position
     */
    @Override
    public Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        List<Position<E>> snapshot = new ArrayList<>();

        // Start with the first child (left child in binary representation)
        Node<E> child = node.getFirstChild();

        // Iterate through the siblings (right child in binary representation)
        while (child != null) {
            snapshot.add(child);
            child = child.getNextSibling();
        }
        return snapshot;
    }

    /**
     * Returns the number of children of position p.
     * Calculated by iterating through the sibling chain.
     * * @param p the position to inspect
     * @return number of children
     * @throws IllegalArgumentException if p is invalid
     */
    @Override
    public int numChildren(Position<E> p) throws IllegalArgumentException {
        int count = 0;
        for (Position<E> child : children(p)) {
            count++;
        }
        return count;
    }

    /**
     * Returns the total number of nodes in the tree.
     * @return tree size
     */
    @Override
    public int size() {
        return size;
    }

    // Note: methods isEmpty(), isInternal(), isExternal(), isRoot()
    // are automatically inherited from AbstractTree.

    //-----------------------------------------------------------------------
    // Update Methods (As required by the Assignment)
    //-----------------------------------------------------------------------

    /**
     * Creates a new root node with element e and returns the node as a position.
     * If the tree is non-empty, this method returns null as per assignment specs.
     * * @param e the element to store in the root
     * @return the new root position, or null if tree is not empty
     */
    public Position<E> addRoot(E e) {
        if (!isEmpty()) {
            return null; // As specified in the assignment
        }
        root = new Node<>(e, null, null, null);
        size = 1;
        return root;
    }

    /**
     * Creates a new child node of position p containing element e.
     * The child nodes are linked in the order they are added.
     * * Logic:
     * 1. If p has no children, the new node becomes the 'firstChild'.
     * 2. If p has children, we traverse to the last sibling and attach the new node as 'nextSibling'.
     * * @param p the parent position
     * @param e the element to store
     * @return the newly created child position
     * @throws IllegalArgumentException if p is invalid
     */
    public Position<E> addChild(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> parent = validate(p);
        Node<E> newChild = new Node<>(e, parent, null, null);

        if (parent.getFirstChild() == null) {
            // Case 1: Parent has no children yet.
            // The new node becomes the first child (left child in binary rep).
            parent.setFirstChild(newChild);
        } else {
            // Case 2: Parent already has children.
            // We must traverse the sibling list to find the last child.
            Node<E> current = parent.getFirstChild();
            while (current.getNextSibling() != null) {
                current = current.getNextSibling();
            }
            // Append the new child to the end of the list (right child of the last sibling).
            current.setNextSibling(newChild);
        }

        size++;
        return newChild;
    }

    /**
     * Removes the position p and returns the element stored in p.
     * * CRITICAL REQUIREMENT:
     * If p has children, they become the children of p's parent, appearing
     * in the same order as p appeared.
     * * Logic Breakdown:
     * 1. Validate p. If p is root, do nothing and return null.
     * 2. Identify the chain of p's children (head and tail).
     * 3. Update the parent reference of all p's children to p's parent.
     * 4. Locate the predecessor of p (either p's parent if p is firstChild, or p's previous sibling).
     * 5. "Splice" the list of p's children into the spot where p was.
     * * @param p the position to remove
     * @return the element that was removed, or null if root
     * @throws IllegalArgumentException if p is invalid
     */
    public E remove(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);

        // Requirement: If p is the root, this method does nothing and returns null.
        if (isRoot(p)) {
            return null;
        }

        Node<E> parent = node.getParent();
        E temp = node.getElement();

        // --- Step 1: Identify the children of the node to be removed ---
        Node<E> firstChildOfNode = node.getFirstChild();
        Node<E> lastChildOfNode = null;

        // If the node has children, we need to update their parent pointers
        // and find the last child in the chain to connect it later.
        if (firstChildOfNode != null) {
            Node<E> walk = firstChildOfNode;
            while (walk != null) {
                walk.setParent(parent); // Adoption: Grandparent becomes parent
                if (walk.getNextSibling() == null) {
                    lastChildOfNode = walk;
                }
                walk = walk.getNextSibling();
            }
        }

        // --- Step 2: Find the predecessor of the node ---
        // We need to know who points to 'node' (is it the parent's firstChild link,
        // or a sibling's nextSibling link?).
        Node<E> prevSibling = null;
        if (parent.getFirstChild() == node) {
            // 'node' is the first child, so there is no previous sibling.
            prevSibling = null;
        } else {
            // 'node' is not the first child, search for the sibling immediately before it.
            prevSibling = parent.getFirstChild();
            while (prevSibling != null && prevSibling.getNextSibling() != node) {
                prevSibling = prevSibling.getNextSibling();
            }
        }

        // --- Step 3: Perform the "Splice" / Relinking ---

        // Scenario A: The node to remove has children (Adoption case)
        if (firstChildOfNode != null) {
            if (prevSibling == null) {
                // If node was the first child, its children become the new first children of parent.
                parent.setFirstChild(firstChildOfNode);
            } else {
                // If node had a previous sibling, link that sibling to node's first child.
                prevSibling.setNextSibling(firstChildOfNode);
            }
            // Finally, link the last child of the removed node to the removed node's next sibling.
            // This preserves the order of the rest of the siblings.
            lastChildOfNode.setNextSibling(node.getNextSibling());
        }
        // Scenario B: The node to remove is a leaf (Simple removal)
        else {
            if (prevSibling == null) {
                // Node was first child and has no children; parent points to node's next sibling.
                parent.setFirstChild(node.getNextSibling());
            } else {
                // Node was in the middle/end; bypass it.
                prevSibling.setNextSibling(node.getNextSibling());
            }
        }

        // --- Step 4: Cleanup ---
        // Nullify references to help Garbage Collection and prevent accidental use.
        node.setElement(null);
        node.setNextSibling(null);
        node.setFirstChild(null);
        node.setParent(node); // Standard convention for a defunct node
        size--;

        return temp;
    }

    //-----------------------------------------------------------------------
    // Display Method
    //-----------------------------------------------------------------------

    /**
     * Displays the tree nodes in preorder traversal, one node per line.
     * The element is preceded by a number of dots equal to its depth.
     * * Output format example:
     * A
     * . B
     * . . E
     */
    public void displayTree() {
        if (root == null) return;
        // We start the recursive display from the root at depth 0.
        // We use a helper method to handle the recursion properly.
        displayPreorderHelper(root);
    }

    /**
     * Recursive helper method for preorder display.
     * Uses the depth() method provided by AbstractTree to calculate indentation.
     * * @param p the current position being visited
     */
    private void displayPreorderHelper(Position<E> p) {
        int d = depth(p); // Method from AbstractTree

        // Print depth number of dots followed by a space
        for (int i = 0; i < d; i++) {
            System.out.print(". ");
        }
        // Print the element
        System.out.println(p.getElement());

        // Recursively visit all children (Preorder: Node -> Children)
        for (Position<E> child : children(p)) {
            displayPreorderHelper(child);
        }
    }
}