
//------------------------------------------------------------------------------
// Trees
//------------------------------------------------------------------------------

// Allows for data to be stored hierarchically. 
// Recursive data structure.
// Trees are organized into nodes that take a hierarchical form.
// Each node can have a parent and children.

// Root node = node at the top of the tree.
// Child node = node directly below another node.

// Branching factor = the number of children any node may have.
// - Determines how quickly the tree will fan out. 
// - Ex: a binary tree has a branching factor of 2.

// Descendent node = node distance below another node. 

// Parent node = node directly above another node.

// Sibling node = any node that is the same distance from the root as another
// node.

// Lead node = node with no children.

// Level of a node = number of nodes that must be traversed (including the root
// node) to reach the node.

// Height of the tree = level of the most distant leaf from the root. 

// Rotations = methods for keeping trees balanced.
// - A tree is balanced if there are only missing nodes in the last level of the tree.

// The performance of a tree is determined by the height of the tree.

// Applications:
// - Huffman coding: data compression algorithm that uses a binary tree to determine
// the best way to assign codes to symbols in the data. Symbols occurring frequently
// are assigned shorted codes.
// - GUI File Systems: each directory is a node.
// - Databases: trees are good for representing data that needs sequential access,
// random access, inserts, and deletes. 
// - Expression processing: performed by compilers, expressions can be represented
// naturally as trees where the operator is the parent node and the child nodes
// are operands.
// Machine learning: decision trees are used to classify data.
// Priority queues: trees are used to implement priority queues.

//------------------------------------------------------------------------------
// Binary Trees
//------------------------------------------------------------------------------

// Each node has three fields: last reference, right reference, and data reference.

public class BinaryTree<E> {
    public class Node {
        E data;
        Node left;
        Node right;

        // Only allow BinaryTree to create Nodes
        private Node(E data) {
            this.data = data;
        }

        public E getData() {
            return data;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public boolean hasLeft() {
            return left != null;
        }

        public boolean hasRight() {
            return right != null;
        }

        public boolean isLeaf() {
            return !hasLeft() && !hasRight();
        }

        public Node insertLeft(E data) throws IllegalStateException {
            if (hasLeft()) {
                throw new IllegalStateException(
                        "Cannot insert because left child already exists");
            }

            left = new Node(data);
            ++size;

            return left;
        }

        public Node insertRight(E data) throws IllegalStateException {
            if (hasRight()) {
                throw new IllegalStateException(
                        "Cannot insert because right child already exists");
            }

            right = new Node(data);
            ++size;

            return right;
        }

        public E removeLeft() throws IllegalStateException {
            if (!hasLeft()) {
                throw new IllegalStateException(
                        "Cannot remove because left child does not exist");
            }
            if (!left.isLeaf()) {
                throw new IllegalStateException(
                        "Cannot remove because left child is not a leaf");
            }

            E data = left.data;
            left = null;
            --size;

            return data;
        }

        public E removeRight() throws IllegalStateException {
            if (!hasRight()) {
                throw new IllegalStateException(
                        "Cannot remove because right child does not exist");
            }
            if (!right.isLeaf()) {
                throw new IllegalStateException(
                        "Cannot remove because right child is not a leaf");
            }

            E data = right.data;
            right = null;
            --size;

            return data;
        }
    }

    private int size;
    private Node root;

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Node getRoot() {
        return root;
    }

    public Node insertRoot(E data) throws IllegalStateException {
        if (!isEmpty()) {
            throw new IllegalStateException(
                    "Cannot insert root into non-empty tree");
        }

        root = new Node(data);
        ++size;

        return root;
    }

    public E removeRoot() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot remove root of empty tree");
        }
        if (!root.isLeaf()) {
            throw new IllegalStateException(
                    "Cannot remove because root is not a leaf");
        }

        E data = root.data;
        root = null;
        --size;

        return data;
    }

    public BinaryTree<E> merge(BinaryTree<E> other, E data) {
        BinaryTree<E> merged = new BinaryTree<E>();

        // Merge this and other into new tree with data as root
        merged.root = new Node(data);
        merged.root.left = this.root;
        merged.root.right = other.root;
        merged.size = 1 + this.size + other.size;

        // Remove all nodes from this and other
        this.root = null;
        this.size = 0;
        other.root = null;
        other.size = 0;

        return merged;
    }
}

//------------------------------------------------------------------------------
// Binary Tree Traversal
//------------------------------------------------------------------------------

// There are four methods for traversing trees:
// Pre-order, in-order, post-order, and level-order.

// Pre-order Traversal: 
// - Visit the root node, then recursively traverse the left subtree, then
// recursively traverse the right subtree.
// - Starts at the root node.
// - Also called depth-first search.

// In-order Traversal:
// - Recursively traverse the left subtree, then visit the root node, then
// recursively traverse the right subtree.
// - Starts at the left-most leaf node and then moves up the tree.

// Post-order Traversal:
// - Recursively traverse the left subtree, then recursively traverse the right
// subtree, then visit the root node.

// Level-order Traversal:
// - Visit nodes beginning at the root, then proceed downward visiting the nodes
// at each level from left to right.

// Implementation.

// Note* that visitor is just a functional interface that can be passed to the
// function. The visitor algorithm could print out the data in the node, or it
// could add the data to a sum, or perform any other operation.

public final class TreeAlgorithms {

    // Pre-order traversal

    // Note* that the public function that gets called from the outside works on
    // trees, but the private function that does the actual work works on nodes.
    public static <E> void traversePreOrder(
            BinaryTree<E> tree,
            Visitor<E> visitor) {
        doTraversePreOrder(tree.getRoot(), visitor);
    }

    private static <E> void doTraversePreOrder(
            BinaryTree<E>.Node node,
            Visitor<E> visitor) {
        if (node == null) {
            return;
        }

        visitor.visit(node.getData());
        doTraversePreOrder(node.getLeft(), visitor);
        doTraversePreOrder(node.getRight(), visitor);
    }

    // In-order traversal
    public static <E> void traverseInOrder(
            BinaryTree<E> tree,
            Visitor<E> visitor) {
        doTraverseInOrder(tree.getRoot(), visitor);
    }

    private static <E> void doTraverseInOrder(
            BinaryTree<E>.Node node,
            Visitor<E> visitor) {
        if (node == null) {
            return;
        }

        doTraverseInOrder(node.getLeft(), visitor);
        visitor.visit(node.getData());
        doTraverseInOrder(node.getRight(), visitor);
    }

    // Post-order traversal
    public static <E> void traversePostOrder(
            BinaryTree<E> tree,
            Visitor<E> visitor) {
        doTraversePostOrder(tree.getRoot(), visitor);
    }

    private static <E> void doTraversePostOrder(
            BinaryTree<E>.Node node,
            Visitor<E> visitor) {
        if (node == null) {
            return;
        }

        doTraversePostOrder(node.getLeft(), visitor);
        doTraversePostOrder(node.getRight(), visitor);
        visitor.visit(node.getData());
    }

    // Level-order traversal
    public static <E> void traverseLevelOrder(
            BinaryTree<E> tree,
            Visitor<E> visitor) {
        // Queue holds nodes that have been discovered and must be visited
        Queue<BinaryTree<E>.Node> queue = new Queue<BinaryTree<E>.Node>();

        // Start off with only root in queue
        if (!tree.isEmpty()) {
            queue.enqueue(tree.getRoot());
        }

        // While nodes remain to be visited in the queue
        while (!queue.isEmpty()) {
            // Visit the front node
            BinaryTree<E>.Node node = queue.dequeue();
            visitor.visit(node.getData());

            // Enqueue front node's children
            if (node.hasLeft()) {
                queue.enqueue(node.getLeft());
            }
            if (node.hasRight()) {
                queue.enqueue(node.getRight());
            }
        }
    }
}

// A note on recursion:

// Because trees fan out, the number of nodes will grow drastically as the
// number of levels grows slowly. 
// The number of nodes will grow at a logarithmic rate, while the number of
// levels will grow at a linear rate (you double the number of nodes at each level).
// Thus you will only end up with a number of recursive calls on the stack equal to
// the height of the tree, which is much smaller than the number of nodes in the
// tree.
// So if you have a balanced tree, you can store a lot of nodes in a tree without
// worrying too much about stack overflow.

// However, note that at the extreme, if you have an unbalanced tree you simply
// have a linked list, and you will have a stack overflow.

//------------------------------------------------------------------------------
// Binary Tree Example
//------------------------------------------------------------------------------

// Using a binary tree to evaluate and nicely print a mathematical expression 
// using an expression tree.

// Operators (+, /, -) are stored in parent nodes and operands (numbers) are
// stored in leaf nodes.

// Can be used to easily translate any mathematical expression into any of the
// three common expression representations: prefix, infix, postfix.

// Example prefix: (*(/(- 74 10) 32) + (23 17)
// - Pre-order traversal yields prefix notation.
// - A nice benefit of prefix notation is that you do not need parentheses to
// capture order-of-operations.

// Example postfix: (((74 10 - ) 32 / ) (23 17 + ) *)
// - Post-order traversal yields postfix notation.
// - Also does not require parentheses.

// Example infix: ((74 - 10) / 32) * (23 + 17)
// - In-order traversal yields prefix notation.
// - Requires parentheses to capture order-of-operations.

// Postfix notation can be easily evaluated by a computer using an abstract 
// stack machine. 
// The algorithm for this is as follows:
// - We are using post-order traversal so go down the left, then down the right,
// then finally to the node.
// - Perform post-order traversal, if the visited node is an operand (the leaf of
// a tree), push it onto the stack.
// - As you work up, if the visited node is an operator then pop the operands off
// of the stack, evaluate the operator with the operands, then push the results
// onto the stack.

/**
 * Parses prefix expressions into an ExpressionTree.  The supported operators
 * are +, -, *, and /.
 */

 // Function for parsing a tree into a binary tree.

public class PrefixExpressionParser {
    private String[] tokens;
    private int tokenIndex = 0;
    private BinaryTree<String> parseTree = new BinaryTree<String>();

    public static ExpressionTree parse(String expression) {
        PrefixExpressionParser parser = new PrefixExpressionParser(expression);
        return new ExpressionTree(parser.parseTree);
    }

    // Build the parse tree
    private PrefixExpressionParser(String expression) {
        if (expression.equals("")) {
            throw new IllegalArgumentException(
                    "Expression malformed: Empty expression");
        }

        tokens = expression.split(" ");

        // Insert first token as root
        String token = getNextToken();
        BinaryTree<String>.Node tokenNode = parseTree.insertRoot(token);

        // Build rest of tree recursively
        if (isOperator(token)) {
            insertLeftOperand(tokenNode);
            insertRightOperand(tokenNode);
        }

        if (tokenIndex != tokens.length) {
            throw new IllegalArgumentException(
                    "Expression malformed: Too many tokens");
        }
    }

    private boolean isOperator(String token) {
        return token.equals("+")
                || token.equals("-")
                || token.equals("*")
                || token.equals("/");
    }

    private void insertLeftOperand(BinaryTree<String>.Node operatorNode) {
        // Insert token as left child
        String token = getNextToken();
        BinaryTree<String>.Node tokenNode = operatorNode.insertLeft(token);

        if (isOperator(token)) {
            insertLeftOperand(tokenNode);
            insertRightOperand(tokenNode);
        }
    }

    private void insertRightOperand(BinaryTree<String>.Node operatorNode) {
        // Insert token as right child
        String token = getNextToken();
        BinaryTree<String>.Node tokenNode = operatorNode.insertRight(token);

        if (isOperator(token)) {
            insertLeftOperand(tokenNode);
            insertRightOperand(tokenNode);
        }
    }

    private String getNextToken() {
        if (tokenIndex == tokens.length) {
            throw new IllegalArgumentException(
                    "Expression malformed: Not enough tokens");
        }

        return tokens[tokenIndex++];
    }
}


// Function for converting a parsed binary tree into a specific notation and then
// evaluating the expression.

public class ExpressionTree {
    BinaryTree<String> parseTree;

    public ExpressionTree(BinaryTree<String> parseTree) {
        this.parseTree = parseTree;
    }

    public String toPrefixNotation() {
        StringBuffer buffer = new StringBuffer();
        doToPrefixNotation(parseTree.getRoot(), buffer);
        return buffer.toString().trim();
    }

    private void doToPrefixNotation(
            BinaryTree<String>.Node node,
            StringBuffer buffer) {
        if (node == null) {
            return;
        }

        buffer.append(node.getData()).append(' ');
        doToPrefixNotation(node.getLeft(), buffer);
        doToPrefixNotation(node.getRight(), buffer);
    }

    public String toPostfixNotation() {
        StringBuffer buffer = new StringBuffer();
        doToPostfixNotation(parseTree.getRoot(), buffer);
        return buffer.toString().trim();
    }

    private void doToPostfixNotation(
            BinaryTree<String>.Node node,
            StringBuffer buffer) {
        if (node == null) {
            return;
        }

        doToPostfixNotation(node.getLeft(), buffer);
        doToPostfixNotation(node.getRight(), buffer);
        buffer.append(node.getData()).append(' ');
    }

    public String toInfixNotation() {
        StringBuffer buffer = new StringBuffer();
        doToInfixNotation(parseTree.getRoot(), buffer);
        return buffer.toString().trim();
    }

    private void doToInfixNotation(
            BinaryTree<String>.Node node,
            StringBuffer buffer) {
        if (node == null) {
            return;
        }

        if (node.isLeaf()) {
            buffer.append(node.getData());
        } else {
            // Parentheses required to enforce precedence
            buffer.append("(");
            doToInfixNotation(node.getLeft(), buffer);
            buffer.append(" ").append(node.getData()).append(" ");
            doToInfixNotation(node.getRight(), buffer);
            buffer.append(")");
        }
    }

    public int evaluate() {
        Stack<Integer> stack = new Stack<Integer>();
        doEvaluate(parseTree.getRoot(), stack);
        return stack.pop();
    }

    /**
     * Execute a post-order traversal.  When a node is visited its value will
     * be computed and placed on the stack.  Leaf nodes will have their integer
     * value pushed directly on the stack.  Non-leaf nodes will be evaluated
     * by popping the top two values from the stack, applying the operator held
     * by the non-leaf node, then pushing the result onto the
     * stack.  When the traversal is finished the stack will contain one value
     * which is the result of the overall expression.
     */
    private void doEvaluate(
            BinaryTree<String>.Node node,
            Stack<Integer> stack) {
        if (node == null) {
            return;
        }

        // Evaluate parent
        if (node.isLeaf()) {
            // Leaf: Push value
            stack.push(Integer.parseInt(node.getData()));
        } else {
            doEvaluate(node.getLeft(), stack);
            doEvaluate(node.getRight(), stack);

            // Non-leaf: Pop two values, apply operator, push result
            int op2 = stack.pop();
            int op1 = stack.pop();

            switch (node.getData().charAt(0)) {
                case '+':
                    stack.push(op1 + op2);
                    break;
                case '-':
                    stack.push(op1 - op2);
                    break;
                case '*':
                    stack.push(op1 * op2);
                    break;
                case '/':
                    stack.push(op1 / op2);
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unknown operator '" + node.getData() + "'");
            }
        }
    }
}





