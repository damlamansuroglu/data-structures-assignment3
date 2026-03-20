
/**
 * Assignment 3: Driver Class
 * * This class serves as the test harness for the OrderedGeneralTree implementation.
 * It is designed to verify the correctness of the tree operations, specifically:
 * 1. Building the tree structure as shown in Figure 1(a) of the assignment.
 * 2. Displaying the tree in preorder traversal with indentation.
 * 3. Testing the 'remove' method and verifying the child adoption logic (as seen in Figure 1(b)).
 * * @author Damla
 */
public class Main {

    /**
     * The main entry point for the application.
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {

        System.out.println("-------------------------------------------------------");
        System.out.println("TEST 1: Tree Construction (Figure 1a)");
        System.out.println("-------------------------------------------------------");

        // 1. Initialize an empty Ordered General Tree
        OrderedGeneralTree<String> tree = new OrderedGeneralTree<>();

        // 2. Add the root node 'A'
        // According to specifications, addRoot creates the root of the tree.
        Position<String> A = tree.addRoot("A");

        // 3. Add children to Root 'A'
        // The assignment specifies children are linked in the order they are added.
        // Expected order for A's children: B, C, D
        Position<String> B = tree.addChild(A, "B");
        Position<String> C = tree.addChild(A, "C");
        Position<String> D = tree.addChild(A, "D");

        // 4. Add children to Node 'B'
        // Expected order for B's children: E, F
        Position<String> E = tree.addChild(B, "E");
        Position<String> F = tree.addChild(B, "F");

        // 5. Add child to Node 'D'
        // Expected child: G
        Position<String> G = tree.addChild(D, "G");

        // 6. Display the constructed tree
        // Verification: The output should match the example in the assignment PDF.
        // Expected Output:
        // A
        // . B
        // . . E
        // . . F
        // . C
        // . D
        // . . G
        System.out.println("Displaying the constructed tree:");
        tree.displayTree();

        System.out.println("\n-------------------------------------------------------");
        System.out.println("TEST 2: Removal and Adoption (Figure 1b)");
        System.out.println("-------------------------------------------------------");

        // 7. Test the remove operation on Node 'B'
        // Scenario described in assignment:
        // "If node B is removed, its two children will be adopted by A (B's parent),
        // and after the removal of B, A's children must appear in the order E, F, C, D."
        System.out.println("Removing node 'B'...");
        String removedElement = tree.remove(B);
        System.out.println("Removed Element: " + removedElement);

        // 8. Verify the tree structure after removal
        // We expect E and F to have moved up to become children of A,
        // inserted exactly where B was.
        System.out.println("Displaying tree after removing 'B':");
        tree.displayTree();

        System.out.println("\n-------------------------------------------------------");
        System.out.println("Test Complete.");
        System.out.println("-------------------------------------------------------");
    }
}