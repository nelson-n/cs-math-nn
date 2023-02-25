
//------------------------------------------------------------------------------
// Heaps 
//------------------------------------------------------------------------------

// Heaps allow for quick access to the smallest or largest item in a set.

// A common problem is to find the smallest or largest item in a set of items.
// - One way to solve this is to keep the set sorted. 
// - However, this is expensive as it requires a O(n log n) re-sort with each
//   insertion or deletion.

// The better approach is to keep the largest (or smallest) item at a known
// location in the set.
// - Inserts and deletes should cause the new largest (or smallest) item to
// move to a known location. 
// - This is not expensive as you do not need to re-sort the entire set. You 
// just need to move the smallest or largest element which is O(log n).

// Heaps can also be used with heapsort. In heapsort the largest value is pulled
// off of the heap one at a time and placed into a sorted array. This is O(n log n).
// However, it is usually slower than mergesort by a constant factor.

// Heaps are implemented as binary trees with the rule that the child node is 
// always smaller than the parent. 
// - The root node is the largest value, this is called a top-heavy tree.
// - The root node can be the smallest value, this is called a bottom-heavy tree.

// Heaps are left-balanced trees. This means that the left side of the tree is
// always filled before the right side. This is because the left side is filled
// first and then the right side is filled in order.

// The nodes can actually be stored in an array. The root node is stored at index 0.
// - The left child of a node is stored at index 2n + 1.
// - The right child of a node is stored at index 2n + 2.
// - The parent of a node is stored at index (n - 1) / 2.

// This saves the overhead of the pointers in a linked list.

// Algorithm for inserting a new value into a heap:
// - Allocate a new position to the array.
// - Insert the new value into the array, compare to its parent node, and if it
// is greater than the parent node then swap the two values. Repeat this until
// the value finds its correct position.

// The worst case scenario for inserting a new value into a heap is O(log n).
// - In this scenario the value being inserted is the largest value and has to 
// percolate all the way up to the root (index 0). 
// - The height of the tree is the log of the number of elements, thus the big O
// for this operation is O(log n).

// For element removal, you can only remove the root from the heap.

// Implementation.
// Note* that it is the logic of the comparator that determines whether the heap
// goes smallest to largest or largest to smallest.

import java.util.Comparator;

public class Heap<E> {
    Object[] tree;  // Left-balanced binary tree

    private Comparator<? super E> comparator;

    public Heap(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public int getSize() {
        return tree == null ? 0 : tree.length;
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        // Grow tree by 1 to hold new node
        Object[] temp;
        if (tree == null) {
            temp = new Object[1];
        } else {
            temp = new Object[tree.length + 1];
            System.arraycopy(tree, 0, temp, 0, tree.length);
        }
        tree = temp;

        // Insert data as right-most node in last level
        tree[getSize() - 1] = data;

        // Push node upward to correct position
        int childIndex = getSize() - 1;
        int parentIndex = getParentIndex(childIndex);
        while (childIndex > 0
                && comparator.compare(
                        (E)tree[parentIndex],
                        (E)tree[childIndex]) < 0) {
            // Parent and child out of order, swap
            swapNodes(parentIndex, childIndex);

            // Move up one level in the tree
            childIndex = parentIndex;
            parentIndex = getParentIndex(parentIndex);
        }
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot extract from empty heap");
        }

        // Extract data at top of heap
        E extracted = (E)tree[0];

        // If extracting last node, set tree to empty and return extracted data
        if (getSize() == 1) {
            tree = null;
            return extracted;
        }

        // Save and remove right-most node from last level
        E save = (E)tree[tree.length - 1];
        Object[] temp = new Object[tree.length - 1];
        System.arraycopy(tree, 0, temp, 0, tree.length - 1);
        tree = temp;

        // Set saved node as the new root
        tree[0] = save;

        // Push root down to correct position
        int parentIndex = 0;
        while (true) {
            int leftChildIndex = getLeftChildIndex(parentIndex);
            int rightChildIndex = getRightChildIndex(parentIndex);

            // Determine whether parent, left child, or right child is largest
            int maxIndex = parentIndex;
            if (leftChildIndex < tree.length && comparator.compare(
                    (E)tree[leftChildIndex],
                    (E)tree[maxIndex]) > 0) {
                maxIndex = leftChildIndex;
            }
            if (rightChildIndex < tree.length && comparator.compare(
                    (E)tree[rightChildIndex],
                    (E)tree[maxIndex]) > 0) {
                maxIndex = rightChildIndex;
            }

            // If parent is largest, root has been pushed to correct position
            if (parentIndex == maxIndex) {
                break;
            } else {
                // Parent and a child out of order, swap with larger child
                swapNodes(parentIndex, maxIndex);

                // Move down one level in the tree
                parentIndex = maxIndex;
            }
        }

        return extracted;
    }

    private static int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private static int getLeftChildIndex(int parentIndex) {
        return parentIndex * 2 + 1;
    }

    private static int getRightChildIndex(int parentIndex) {
        return parentIndex * 2 + 2;
    }

    private void swapNodes(int index1, int index2) {
        Object temp = tree[index1];
        tree[index1] = tree[index2];
        tree[index2] = temp;
    }
}

//------------------------------------------------------------------------------
// Priority Queues 
//------------------------------------------------------------------------------





