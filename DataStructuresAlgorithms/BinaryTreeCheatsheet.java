
// Cheatsheet and examples on how to query binary trees with recursion in Java.

// Covers:
// - Building a binary tree.
// - Counting the number of nodes in a binary tree.
// - Counting the number of leaves in a binary tree.
// - Counting the number of non-leaf nodes in a binary tree.
// - Finding the height of a binary tree.
// - Recursively removing leaves from a binary tree.
// - Pre-order, in-order, and post-order traversal of a binary tree.
// - Binary tree class implementation.

public class BinaryTreeCheatsheet {
    public static void main(String args[]) {

//------------------------------------------------------------------------------
// Building a Binary Tree
//------------------------------------------------------------------------------

// Note* that because the only field in the BinaryTree class that can be accessed
// externally is the root node, all additions to the tree must be built from 
// the root node.

        // Build an example tree.
        BinaryTree<Integer> tree1;
        
        tree1 = new BinaryTree<Integer>();
        tree1.insertRoot(1);
        tree1.getRoot().insertLeft(2);
        tree1.getRoot().insertRight(3);
        tree1.getRoot().getLeft().insertLeft(4);
        tree1.getRoot().getRight().insertLeft(5);
        tree1.getRoot().getRight().insertRight(6);
        tree1.getRoot().getLeft().getLeft().insertLeft(7);
        tree1.getRoot().getRight().getRight().insertRight(8);
        tree1.getRoot().getRight().getRight().getRight().insertRight(9);

//------------------------------------------------------------------------------
// Testing Recursive Methods
//------------------------------------------------------------------------------

        System.out.println("Tree 1 has " + TreeFunctions.countLeaves(tree1) + " leaves.");
        
        System.out.println("Tree 1 has " + TreeFunctions.countNonLeaves(tree1) + " non-leaf nodes.");

        System.out.println("Tree 1 has a height of " + TreeFunctions.getHeight(tree1) + ".");

        System.out.println("Tree 1 elements in pre-order:");
        TreeFunctions.printPreOrder(tree1);

        System.out.println("Tree 1 elements in in-order:");
        TreeFunctions.printInOrder(tree1);

        System.out.println("Tree 1 elements in post-order:");
        TreeFunctions.printPostOrder(tree1);

        TreeFunctions.removeLeaves(tree1);
        System.out.println("Tree 1 elements in pre-order after removing leaves:");
        TreeFunctions.printPreOrder(tree1);

    }

//------------------------------------------------------------------------------
// Tree Functions for Recursively Searching the Tree
//------------------------------------------------------------------------------

    public static class TreeFunctions {

        //----------------------------------------------------------------------
        // Node Counter
        //----------------------------------------------------------------------

        // Note that all functions have a public wrapper function which just calls
        // the private recursive function on the root node. This kicks off the 
        // recursion.
        public static int countNodes(BinaryTree<Integer> tree) {
            return doCountNodes(tree.getRoot());
        }

        // Private method that recursively counts the number of nodes.
        private static int doCountNodes(BinaryTree<Integer>.Node node) {

            // If you traverse to an end of the tree and find a null node, then
            // return 0 and continue.
            if (node == null) {
                return 0;
            }

            // The 1 counts the current node as a node, then recursively walks
            // down the left and right branches of the tree and adds the results.
            return 1 + doCountNodes(node.getLeft()) + doCountNodes(node.getRight());
        }

        //----------------------------------------------------------------------
        // Leaf Counter
        //----------------------------------------------------------------------

        public static int countLeaves(BinaryTree<Integer> tree) {
            return doCountLeaves(tree.getRoot());
        }

        private static int doCountLeaves(BinaryTree<Integer>.Node node) {

            if (node == null) {
                return 0;
            }

            // If you find a leaf node return 1 which will add to the total count,
            // then continue.
            if (node.isLeaf()) {
                return 1;

            // If you find a non-leaf node, then recursively walk down the left
            // and right branches of the tree and add the results together.
            } else {
                return doCountLeaves(node.getLeft()) + doCountLeaves(node.getRight());
            }
        }

        //----------------------------------------------------------------------
        // Non Leaf Counter
        //----------------------------------------------------------------------

        public static int countNonLeaves(BinaryTree<Integer> tree) {
            return doCountNonLeaves(tree.getRoot());
        }

        private static int doCountNonLeaves(BinaryTree<Integer>.Node node) {

            if (node == null) {
                return 0;
            }

            if (node.isLeaf()) {
                return 0;
            // For all non-leaf nodes, add 1 to represent the current node, then
            // recursively walk down the left and right branches of the tree and
            // add the results together.
            } else {
                return 1 + doCountNonLeaves(node.getLeft()) + doCountNonLeaves(node.getRight());
            }
        }

        //----------------------------------------------------------------------
        // Height of Tree
        //----------------------------------------------------------------------

        public static int getHeight(BinaryTree<Integer> tree) {
            return doGetHeight(tree.getRoot());
        }

        private static int doGetHeight(BinaryTree<Integer>.Node node) {

            if (node == null) {
                return 0;
            }

            // If you reach a leaf, add 1 to the counter.
            if (node.isLeaf()) {
                return 1;
            // For all non-leaf nodes, add 1 to represent the current node, then
            // recursively walk down the left and right branches of the tree and
            // add to the counter. Take the max of whatever branch is tallest, 
            // left or right.
            } else {
                return 1 + Math.max(doGetHeight(node.getLeft()), doGetHeight(node.getRight()));
            }
        }

        //----------------------------------------------------------------------
        // Remove Leaves
        //----------------------------------------------------------------------

        // Because we can only remove nodes from the perspective of the parent node,
        // we have to perform the recursive operations from the perspective of the
        // parent node instead of the current node which makes things tricky.

        // - For one, we need to perform operations independently on the left and
        // right nodes as a parent may have two children nodes. This is reflected
        // in the .left and .right if statements.

        // - Secondly, we need to check if the current parent node whose perspective
        // we are looking at even has children, otherwise we will get a null pointer
        // when we try to run .isLeaf() on the child node. This check is reflected in the
        // in the first if (node.left != null) statement.

        // Once we have set up seperate operations for the left and right nodes
        // and checked that the parent node has children, we can write normal
        // recursive code from the perspective of the parent node. In this case,
        // we check if the child node is a leaf, and if so we remove it. If not,
        // we recursively call the function on the child node.

        public static void removeLeaves(BinaryTree<Integer> tree) {
            doRemoveLeaves(tree.getRoot());
        }

        private static void doRemoveLeaves(BinaryTree<Integer>.Node node) {

            // If you get to the end of the tree, do not do anything and continue.
            if (node == null) {
                return;
            }

            if (node.left != null) {
                if (node.left.isLeaf()) {
                    node.removeLeft();
                } else {
                    doRemoveLeaves(node.getLeft());
                }
            }

            if (node.right != null) {
                if (node.right.isLeaf()) {
                    node.removeRight();
                } else {
                    doRemoveLeaves(node.getRight());
                }
            }
        }

        //----------------------------------------------------------------------
        // Pre-order Traversal
        //----------------------------------------------------------------------

        // Pre-order Traversal (depth-first-search): root, left sub-tree, right sub-tree.

        public static void printPreOrder(BinaryTree<Integer> tree) {
            doPrintPreOrder(tree.getRoot());
        }

        private static void doPrintPreOrder(BinaryTree<Integer>.Node node) {

            if (node == null) {
                return;
            }

            // Print the current node (the root node on the first iteration), 
            // then recursively walk down the left and right branches of the tree.
            System.out.print(node.getData() + " ");
            doPrintPreOrder(node.getLeft());
            doPrintPreOrder(node.getRight());
        }

        //----------------------------------------------------------------------
        // In-order Traversal
        //----------------------------------------------------------------------

        // In-order Traversal: left sub-tree, root, right sub-tree.

        public static void printInOrder(BinaryTree<Integer> tree) {
            doPrintInOrder(tree.getRoot());
        }

        private static void doPrintInOrder(BinaryTree<Integer>.Node node) {

            if (node == null) {
                return;
            }

            doPrintInOrder(node.getLeft());
            System.out.print(node.getData() + " ");
            doPrintInOrder(node.getRight());
        }

        //----------------------------------------------------------------------
        // Post-order Traversal
        //----------------------------------------------------------------------

        // Post-order Traversal: left sub-tree, right sub-tree, root.

        public static void printPostOrder(BinaryTree<Integer> tree) {
            doPrintPostOrder(tree.getRoot());
        }

        private static void doPrintPostOrder(BinaryTree<Integer>.Node node) {

            if (node == null) {
                return;
            }

            doPrintPostOrder(node.getLeft());
            doPrintPostOrder(node.getRight());
            System.out.print(node.getData() + " ");
        }


    }

//--------------------------------------------------------------------------
// Binary Tree Implementation
//--------------------------------------------------------------------------

// There is a master class (BinaryTree) which contains the following fields.
// - size = the number of nodes in the tree.
// - root = the root node of the tree.

// There is a nested private class (Node) which contains the following fields.
// - data = the data stored in the node.
// - left = the left child of the node.
// - right = the right child of the node.

// BinaryTree class methods:
// - getSize() = returns the number of nodes in the tree.
// - isEmpty() = returns true if the tree is empty.
// - getRoot() = returns the root node of the tree.
// - insertRoot(E data) = inserts a new node with the given data as the root of the tree.
// - removeRoot() = removes the root node of the tree.
// - merge() = merges two trees into one.

// Node class methods:
// - getData() = returns the data stored in the node.
// - getLeft() = returns the left child of the node.
// - getRight() = returns the right child of the node.
// - hasLeft() = returns true if the node has a left child.
// - hasRight() = returns true if the node has a right child.
// - isLeaf() = returns true if the node is a leaf.
// - insertLeft(E data) = inserts a new node with the given data as the left child of the node.
// - insertRight(E data) = inserts a new node with the given data as the right child of the node.
// - removeLeft() = removes the left child of the node.
// - removeRight() = removes the right child of the node.

    public static class BinaryTree<E> {

        // Each node contains only a data field, a left child, and a right child.
        public class Node {
            E data;
            Node left;
            Node right;

            // Only allow the BinaryTree class to create Nodes.
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

            // A leaf node occurs when a node has no children.
            public boolean isLeaf() {
                return !hasLeft() && !hasRight();
            }

            public Node insertLeft(E data) throws IllegalStateException {
                if (hasLeft()) {
                    throw new IllegalStateException(
                            "Cannot insert because left child already exists");
                }

                left = new Node(data);
                ++size; // size tracks the number of nodes in the tree.

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

            // Note* that because nodes have no parent references, we cannot remove
            // a node without knowing its parent. This is why there are only methods
            // for removing children nodes. Also, we can only remove leaf nodes or 
            // else there will be a break in the tree.
            public E removeLeft() throws IllegalStateException {
                if (!hasLeft()) {
                    throw new IllegalStateException(
                            "Cannot remove because left child does not exist");
                }
                if (!left.isLeaf()) {
                    throw new IllegalStateException(
                            "Cannot remove because left child is not a leaf");
                }

                // Pop off the data of the node that is about to be removed and return
                // it to the user.
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

        // The BinaryTree contains a root node and a size field.
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

        // Because there are no parent references and we can only remove child nodes,
        // there needs to be a special method for removing the root.
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

        // Method for merging two trees into one. Takes a pre-existing tree, a new 
        // tree, and a new root. The new root gets sets and the two trees are added
        // to it as a left and right child. 
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

}
