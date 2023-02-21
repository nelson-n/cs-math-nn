
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






