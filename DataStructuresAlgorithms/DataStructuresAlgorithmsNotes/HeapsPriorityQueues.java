
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

// Data structure used to store items that have priority.
// We want to retrieve the item with the highest priority from the queue.
// A heap is used internally to create a priority queue where the concept of 
// largest value is just changed to represent highest priority.

// Applications of priority queues:
// - Task scheduling performed by OS to determine what to run next on the CPU.
// - Event scheduling performed by GUIs to determine what to display next.
// - Network routing algorithms to determine which packet to send next.
// - Graph algorithms to determine which edge to traverse next.

// Implementation.
// Simply built on top of the heap class by using the heap's comparator to
// determine the priority of the items.

// import java.util.Comparator;

public class PriorityQueue<E> {
    Heap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Heap<E>(comparator);
    }

    public int getSize() {
        return heap.getSize();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        heap.insert(data);
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot extract from empty priority queue");
        }

        return heap.extract();
    }

    @SuppressWarnings("unchecked")
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot peek into empty priority queue");
        }

        // Default package-internal visibility of tree allows access here
        return (E)heap.tree[0];
    }
}

// Priority Queue Example.

public class Task {
    private int priority;   // Low value means high priority
    String Name;

    public Task(int priority, String name) {
        this.priority = priority;
        Name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return Name;
    }
}

// import java.util.Comparator;

/**
 * Simple task scheduler using a priority queue.
 *
 * Program output:
 *      Priority = 1, Name = Play audio
 *      Priority = 2, Name = Refresh screen
 *      Priority = 3, Name = Handle mouse move
 *      Priority = 4, Name = Swap process memory
 *      Priority = 5, Name = Write data to disk
 *      Priority = 6, Name = Close file
 *      Priority = 7, Name = Write log entry
 */
public class Main {
    public static void main(String args[]) {
        // Populate prioritized tasks
        PriorityQueue<Task> tasks = new PriorityQueue<Task>(
                new Comparator<Task>() {
                    public int compare(Task t1, Task t2) {
                        return t2.getPriority() - t1.getPriority();
                    }
                }
        );
        tasks.insert(new Task(3, "Handle mouse move"));
        tasks.insert(new Task(6, "Close file"));
        tasks.insert(new Task(5, "Write data to disk"));
        tasks.insert(new Task(2, "Refresh screen"));
        tasks.insert(new Task(4, "Swap process memory"));
        tasks.insert(new Task(1, "Play audio"));
        tasks.insert(new Task(7, "Write log entry"));

        // Output tasks in priority order
        while (!tasks.isEmpty()) {
            Task task = tasks.extract();

            System.out.println("Priority = " + task.getPriority()
                    + ", Name = " + task.getName());
        }
    }
}

//------------------------------------------------------------------------------
// Huffman Coding 
//------------------------------------------------------------------------------

// Method for encoding symbols using fewer bits than previously required.
// Commonly used for lossless compression. 

// The idea is that symbols that occur more frequently are encoded into fewer
// bits and those that occur less frequently are encoded into more bits.

// Encode steps:
// - Build a symbol frequency table.
// - Build a Huffman tree from the frequency table.
// - Build symbol encodings from the Huffman tree.
// - Serialize the Huffman tree as a header in encoded data.
// - Encode symbols into encoded data using encodings table.

// Decode steps:
// - Deserialize the Huffman tree from the header in encoded data.
// - Decode symbols from encoded data using the Huffman tree.

// Example case:
// - 274 input characters (n) @ 1 byte per character (ASCII).
// - 256 possible characters (m).
// - 274 bytes * 8 bits/byte = 2192 bits.

// First, count the occurrences of each character in the input to build the 
// symbol frequency table. This is done in O(n) time.
// - This can be done with a hash table or array of size m.

// Then we build the Huffman tree. This is done in O(m log m) time.
// - This consists of a tree of priority queues. 
// - Build a seperate binary tree for each symbol from the frequency table.
// - Each binary tree contains just a root node with the symbol and the number
// of occurrences of the symbol.
// - Then create a priority queue of all the binary trees.

// Building the trees is O(1).
// Inserting the trees into the priority queue is O(1).

// Then repeatedly merge the two trees with the lowest frequency until there is
// only one tree left. This is done in O(m log m) time.

// The original data took 8 bits per character. The compressed data takes 
// < 4 bits per character, a greater than 50% compression rate.




// Repeat




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

// Data structure used to store items that have priority.
// We want to retrieve the item with the highest priority from the queue.
// A heap is used internally to create a priority queue where the concept of 
// largest value is just changed to represent highest priority.

// Applications of priority queues:
// - Task scheduling performed by OS to determine what to run next on the CPU.
// - Event scheduling performed by GUIs to determine what to display next.
// - Network routing algorithms to determine which packet to send next.
// - Graph algorithms to determine which edge to traverse next.

// Implementation.
// Simply built on top of the heap class by using the heap's comparator to
// determine the priority of the items.

// import java.util.Comparator;

public class PriorityQueue<E> {
    Heap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Heap<E>(comparator);
    }

    public int getSize() {
        return heap.getSize();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        heap.insert(data);
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot extract from empty priority queue");
        }

        return heap.extract();
    }

    @SuppressWarnings("unchecked")
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot peek into empty priority queue");
        }

        // Default package-internal visibility of tree allows access here
        return (E)heap.tree[0];
    }
}

// Priority Queue Example.

public class Task {
    private int priority;   // Low value means high priority
    String Name;

    public Task(int priority, String name) {
        this.priority = priority;
        Name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return Name;
    }
}

// import java.util.Comparator;

/**
 * Simple task scheduler using a priority queue.
 *
 * Program output:
 *      Priority = 1, Name = Play audio
 *      Priority = 2, Name = Refresh screen
 *      Priority = 3, Name = Handle mouse move
 *      Priority = 4, Name = Swap process memory
 *      Priority = 5, Name = Write data to disk
 *      Priority = 6, Name = Close file
 *      Priority = 7, Name = Write log entry
 */
public class Main {
    public static void main(String args[]) {
        // Populate prioritized tasks
        PriorityQueue<Task> tasks = new PriorityQueue<Task>(
                new Comparator<Task>() {
                    public int compare(Task t1, Task t2) {
                        return t2.getPriority() - t1.getPriority();
                    }
                }
        );
        tasks.insert(new Task(3, "Handle mouse move"));
        tasks.insert(new Task(6, "Close file"));
        tasks.insert(new Task(5, "Write data to disk"));
        tasks.insert(new Task(2, "Refresh screen"));
        tasks.insert(new Task(4, "Swap process memory"));
        tasks.insert(new Task(1, "Play audio"));
        tasks.insert(new Task(7, "Write log entry"));

        // Output tasks in priority order
        while (!tasks.isEmpty()) {
            Task task = tasks.extract();

            System.out.println("Priority = " + task.getPriority()
                    + ", Name = " + task.getName());
        }
    }
}

//------------------------------------------------------------------------------
// Huffman Coding 
//------------------------------------------------------------------------------

// Method for encoding symbols using fewer bits than previously required.
// Commonly used for lossless compression. 

// The idea is that symbols that occur more frequently are encoded into fewer
// bits and those that occur less frequently are encoded into more bits.

// Encode steps:
// - Build a symbol frequency table.
// - Build a Huffman tree from the frequency table.
// - Build symbol encodings from the Huffman tree.
// - Serialize the Huffman tree as a header in encoded data.
// - Encode symbols into encoded data using encodings table.

// Decode steps:
// - Deserialize the Huffman tree from the header in encoded data.
// - Decode symbols from encoded data using the Huffman tree.

// Example case:
// - 274 input characters (n) @ 1 byte per character (ASCII).
// - 256 possible characters (m).
// - 274 bytes * 8 bits/byte = 2192 bits.

// First, count the occurrences of each character in the input to build the 
// symbol frequency table. This is done in O(n) time.
// - This can be done with a hash table or array of size m.

// Then we build the Huffman tree. This is done in O(m log m) time.
// - This consists of a tree of priority queues. 
// - Build a seperate binary tree for each symbol from the frequency table.
// - Each binary tree contains just a root node with the symbol and the number
// of occurrences of the symbol.
// - Then create a priority queue of all the binary trees.

// Building the trees is O(1).
// Inserting the trees into the priority queue is O(1).

// Then repeatedly merge the two trees with the lowest frequency until there is
// only one tree left. This is done in O(m log m) time.

// The original data took 8 bits per character. The compressed data takes 
// < 4 bits per character, a greater than 50% compression rate.





// Repeat





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

// Data structure used to store items that have priority.
// We want to retrieve the item with the highest priority from the queue.
// A heap is used internally to create a priority queue where the concept of 
// largest value is just changed to represent highest priority.

// Applications of priority queues:
// - Task scheduling performed by OS to determine what to run next on the CPU.
// - Event scheduling performed by GUIs to determine what to display next.
// - Network routing algorithms to determine which packet to send next.
// - Graph algorithms to determine which edge to traverse next.

// Implementation.
// Simply built on top of the heap class by using the heap's comparator to
// determine the priority of the items.

// import java.util.Comparator;

public class PriorityQueue<E> {
    Heap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Heap<E>(comparator);
    }

    public int getSize() {
        return heap.getSize();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        heap.insert(data);
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot extract from empty priority queue");
        }

        return heap.extract();
    }

    @SuppressWarnings("unchecked")
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot peek into empty priority queue");
        }

        // Default package-internal visibility of tree allows access here
        return (E)heap.tree[0];
    }
}

// Priority Queue Example.

public class Task {
    private int priority;   // Low value means high priority
    String Name;

    public Task(int priority, String name) {
        this.priority = priority;
        Name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return Name;
    }
}

// import java.util.Comparator;

/**
 * Simple task scheduler using a priority queue.
 *
 * Program output:
 *      Priority = 1, Name = Play audio
 *      Priority = 2, Name = Refresh screen
 *      Priority = 3, Name = Handle mouse move
 *      Priority = 4, Name = Swap process memory
 *      Priority = 5, Name = Write data to disk
 *      Priority = 6, Name = Close file
 *      Priority = 7, Name = Write log entry
 */
public class Main {
    public static void main(String args[]) {
        // Populate prioritized tasks
        PriorityQueue<Task> tasks = new PriorityQueue<Task>(
                new Comparator<Task>() {
                    public int compare(Task t1, Task t2) {
                        return t2.getPriority() - t1.getPriority();
                    }
                }
        );
        tasks.insert(new Task(3, "Handle mouse move"));
        tasks.insert(new Task(6, "Close file"));
        tasks.insert(new Task(5, "Write data to disk"));
        tasks.insert(new Task(2, "Refresh screen"));
        tasks.insert(new Task(4, "Swap process memory"));
        tasks.insert(new Task(1, "Play audio"));
        tasks.insert(new Task(7, "Write log entry"));

        // Output tasks in priority order
        while (!tasks.isEmpty()) {
            Task task = tasks.extract();

            System.out.println("Priority = " + task.getPriority()
                    + ", Name = " + task.getName());
        }
    }
}

//------------------------------------------------------------------------------
// Huffman Coding 
//------------------------------------------------------------------------------

// Method for encoding symbols using fewer bits than previously required.
// Commonly used for lossless compression. 

// The idea is that symbols that occur more frequently are encoded into fewer
// bits and those that occur less frequently are encoded into more bits.

// Encode steps:
// - Build a symbol frequency table.
// - Build a Huffman tree from the frequency table.
// - Build symbol encodings from the Huffman tree.
// - Serialize the Huffman tree as a header in encoded data.
// - Encode symbols into encoded data using encodings table.

// Decode steps:
// - Deserialize the Huffman tree from the header in encoded data.
// - Decode symbols from encoded data using the Huffman tree.

// Example case:
// - 274 input characters (n) @ 1 byte per character (ASCII).
// - 256 possible characters (m).
// - 274 bytes * 8 bits/byte = 2192 bits.

// First, count the occurrences of each character in the input to build the 
// symbol frequency table. This is done in O(n) time.
// - This can be done with a hash table or array of size m.

// Then we build the Huffman tree. This is done in O(m log m) time.
// - This consists of a tree of priority queues. 
// - Build a seperate binary tree for each symbol from the frequency table.
// - Each binary tree contains just a root node with the symbol and the number
// of occurrences of the symbol.
// - Then create a priority queue of all the binary trees.

// Building the trees is O(1).
// Inserting the trees into the priority queue is O(1).

// Then repeatedly merge the two trees with the lowest frequency until there is
// only one tree left. This is done in O(m log m) time.

// The original data took 8 bits per character. The compressed data takes 
// < 4 bits per character, a greater than 50% compression rate.




// Repeat




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

// Data structure used to store items that have priority.
// We want to retrieve the item with the highest priority from the queue.
// A heap is used internally to create a priority queue where the concept of 
// largest value is just changed to represent highest priority.

// Applications of priority queues:
// - Task scheduling performed by OS to determine what to run next on the CPU.
// - Event scheduling performed by GUIs to determine what to display next.
// - Network routing algorithms to determine which packet to send next.
// - Graph algorithms to determine which edge to traverse next.

// Implementation.
// Simply built on top of the heap class by using the heap's comparator to
// determine the priority of the items.

// import java.util.Comparator;

public class PriorityQueue<E> {
    Heap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Heap<E>(comparator);
    }

    public int getSize() {
        return heap.getSize();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        heap.insert(data);
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot extract from empty priority queue");
        }

        return heap.extract();
    }

    @SuppressWarnings("unchecked")
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot peek into empty priority queue");
        }

        // Default package-internal visibility of tree allows access here
        return (E)heap.tree[0];
    }
}

// Priority Queue Example.

public class Task {
    private int priority;   // Low value means high priority
    String Name;

    public Task(int priority, String name) {
        this.priority = priority;
        Name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return Name;
    }
}

// import java.util.Comparator;

/**
 * Simple task scheduler using a priority queue.
 *
 * Program output:
 *      Priority = 1, Name = Play audio
 *      Priority = 2, Name = Refresh screen
 *      Priority = 3, Name = Handle mouse move
 *      Priority = 4, Name = Swap process memory
 *      Priority = 5, Name = Write data to disk
 *      Priority = 6, Name = Close file
 *      Priority = 7, Name = Write log entry
 */
public class Main {
    public static void main(String args[]) {
        // Populate prioritized tasks
        PriorityQueue<Task> tasks = new PriorityQueue<Task>(
                new Comparator<Task>() {
                    public int compare(Task t1, Task t2) {
                        return t2.getPriority() - t1.getPriority();
                    }
                }
        );
        tasks.insert(new Task(3, "Handle mouse move"));
        tasks.insert(new Task(6, "Close file"));
        tasks.insert(new Task(5, "Write data to disk"));
        tasks.insert(new Task(2, "Refresh screen"));
        tasks.insert(new Task(4, "Swap process memory"));
        tasks.insert(new Task(1, "Play audio"));
        tasks.insert(new Task(7, "Write log entry"));

        // Output tasks in priority order
        while (!tasks.isEmpty()) {
            Task task = tasks.extract();

            System.out.println("Priority = " + task.getPriority()
                    + ", Name = " + task.getName());
        }
    }
}

//------------------------------------------------------------------------------
// Huffman Coding 
//------------------------------------------------------------------------------

// Method for encoding symbols using fewer bits than previously required.
// Commonly used for lossless compression. 

// The idea is that symbols that occur more frequently are encoded into fewer
// bits and those that occur less frequently are encoded into more bits.

// Encode steps:
// - Build a symbol frequency table.
// - Build a Huffman tree from the frequency table.
// - Build symbol encodings from the Huffman tree.
// - Serialize the Huffman tree as a header in encoded data.
// - Encode symbols into encoded data using encodings table.

// Decode steps:
// - Deserialize the Huffman tree from the header in encoded data.
// - Decode symbols from encoded data using the Huffman tree.

// Example case:
// - 274 input characters (n) @ 1 byte per character (ASCII).
// - 256 possible characters (m).
// - 274 bytes * 8 bits/byte = 2192 bits.

// First, count the occurrences of each character in the input to build the 
// symbol frequency table. This is done in O(n) time.
// - This can be done with a hash table or array of size m.

// Then we build the Huffman tree. This is done in O(m log m) time.
// - This consists of a tree of priority queues. 
// - Build a seperate binary tree for each symbol from the frequency table.
// - Each binary tree contains just a root node with the symbol and the number
// of occurrences of the symbol.
// - Then create a priority queue of all the binary trees.

// Building the trees is O(1).
// Inserting the trees into the priority queue is O(1).

// Then repeatedly merge the two trees with the lowest frequency until there is
// only one tree left. This is done in O(m log m) time.

// The original data took 8 bits per character. The compressed data takes 
// < 4 bits per character, a greater than 50% compression rate.





// Repeat





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

// Data structure used to store items that have priority.
// We want to retrieve the item with the highest priority from the queue.
// A heap is used internally to create a priority queue where the concept of 
// largest value is just changed to represent highest priority.

// Applications of priority queues:
// - Task scheduling performed by OS to determine what to run next on the CPU.
// - Event scheduling performed by GUIs to determine what to display next.
// - Network routing algorithms to determine which packet to send next.
// - Graph algorithms to determine which edge to traverse next.

// Implementation.
// Simply built on top of the heap class by using the heap's comparator to
// determine the priority of the items.

// import java.util.Comparator;

public class PriorityQueue<E> {
    Heap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Heap<E>(comparator);
    }

    public int getSize() {
        return heap.getSize();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        heap.insert(data);
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot extract from empty priority queue");
        }

        return heap.extract();
    }

    @SuppressWarnings("unchecked")
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot peek into empty priority queue");
        }

        // Default package-internal visibility of tree allows access here
        return (E)heap.tree[0];
    }
}

// Priority Queue Example.

public class Task {
    private int priority;   // Low value means high priority
    String Name;

    public Task(int priority, String name) {
        this.priority = priority;
        Name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return Name;
    }
}

// import java.util.Comparator;

/**
 * Simple task scheduler using a priority queue.
 *
 * Program output:
 *      Priority = 1, Name = Play audio
 *      Priority = 2, Name = Refresh screen
 *      Priority = 3, Name = Handle mouse move
 *      Priority = 4, Name = Swap process memory
 *      Priority = 5, Name = Write data to disk
 *      Priority = 6, Name = Close file
 *      Priority = 7, Name = Write log entry
 */
public class Main {
    public static void main(String args[]) {
        // Populate prioritized tasks
        PriorityQueue<Task> tasks = new PriorityQueue<Task>(
                new Comparator<Task>() {
                    public int compare(Task t1, Task t2) {
                        return t2.getPriority() - t1.getPriority();
                    }
                }
        );
        tasks.insert(new Task(3, "Handle mouse move"));
        tasks.insert(new Task(6, "Close file"));
        tasks.insert(new Task(5, "Write data to disk"));
        tasks.insert(new Task(2, "Refresh screen"));
        tasks.insert(new Task(4, "Swap process memory"));
        tasks.insert(new Task(1, "Play audio"));
        tasks.insert(new Task(7, "Write log entry"));

        // Output tasks in priority order
        while (!tasks.isEmpty()) {
            Task task = tasks.extract();

            System.out.println("Priority = " + task.getPriority()
                    + ", Name = " + task.getName());
        }
    }
}

//------------------------------------------------------------------------------
// Huffman Coding 
//------------------------------------------------------------------------------

// Method for encoding symbols using fewer bits than previously required.
// Commonly used for lossless compression. 

// The idea is that symbols that occur more frequently are encoded into fewer
// bits and those that occur less frequently are encoded into more bits.

// Encode steps:
// - Build a symbol frequency table.
// - Build a Huffman tree from the frequency table.
// - Build symbol encodings from the Huffman tree.
// - Serialize the Huffman tree as a header in encoded data.
// - Encode symbols into encoded data using encodings table.

// Decode steps:
// - Deserialize the Huffman tree from the header in encoded data.
// - Decode symbols from encoded data using the Huffman tree.

// Example case:
// - 274 input characters (n) @ 1 byte per character (ASCII).
// - 256 possible characters (m).
// - 274 bytes * 8 bits/byte = 2192 bits.

// First, count the occurrences of each character in the input to build the 
// symbol frequency table. This is done in O(n) time.
// - This can be done with a hash table or array of size m.

// Then we build the Huffman tree. This is done in O(m log m) time.
// - This consists of a tree of priority queues. 
// - Build a seperate binary tree for each symbol from the frequency table.
// - Each binary tree contains just a root node with the symbol and the number
// of occurrences of the symbol.
// - Then create a priority queue of all the binary trees.

// Building the trees is O(1).
// Inserting the trees into the priority queue is O(1).

// Then repeatedly merge the two trees with the lowest frequency until there is
// only one tree left. This is done in O(m log m) time.

// The original data took 8 bits per character. The compressed data takes 
// < 4 bits per character, a greater than 50% compression rate.




// Repeat




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

// Data structure used to store items that have priority.
// We want to retrieve the item with the highest priority from the queue.
// A heap is used internally to create a priority queue where the concept of 
// largest value is just changed to represent highest priority.

// Applications of priority queues:
// - Task scheduling performed by OS to determine what to run next on the CPU.
// - Event scheduling performed by GUIs to determine what to display next.
// - Network routing algorithms to determine which packet to send next.
// - Graph algorithms to determine which edge to traverse next.

// Implementation.
// Simply built on top of the heap class by using the heap's comparator to
// determine the priority of the items.

// import java.util.Comparator;

public class PriorityQueue<E> {
    Heap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Heap<E>(comparator);
    }

    public int getSize() {
        return heap.getSize();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        heap.insert(data);
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot extract from empty priority queue");
        }

        return heap.extract();
    }

    @SuppressWarnings("unchecked")
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot peek into empty priority queue");
        }

        // Default package-internal visibility of tree allows access here
        return (E)heap.tree[0];
    }
}

// Priority Queue Example.

public class Task {
    private int priority;   // Low value means high priority
    String Name;

    public Task(int priority, String name) {
        this.priority = priority;
        Name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return Name;
    }
}

// import java.util.Comparator;

/**
 * Simple task scheduler using a priority queue.
 *
 * Program output:
 *      Priority = 1, Name = Play audio
 *      Priority = 2, Name = Refresh screen
 *      Priority = 3, Name = Handle mouse move
 *      Priority = 4, Name = Swap process memory
 *      Priority = 5, Name = Write data to disk
 *      Priority = 6, Name = Close file
 *      Priority = 7, Name = Write log entry
 */
public class Main {
    public static void main(String args[]) {
        // Populate prioritized tasks
        PriorityQueue<Task> tasks = new PriorityQueue<Task>(
                new Comparator<Task>() {
                    public int compare(Task t1, Task t2) {
                        return t2.getPriority() - t1.getPriority();
                    }
                }
        );
        tasks.insert(new Task(3, "Handle mouse move"));
        tasks.insert(new Task(6, "Close file"));
        tasks.insert(new Task(5, "Write data to disk"));
        tasks.insert(new Task(2, "Refresh screen"));
        tasks.insert(new Task(4, "Swap process memory"));
        tasks.insert(new Task(1, "Play audio"));
        tasks.insert(new Task(7, "Write log entry"));

        // Output tasks in priority order
        while (!tasks.isEmpty()) {
            Task task = tasks.extract();

            System.out.println("Priority = " + task.getPriority()
                    + ", Name = " + task.getName());
        }
    }
}

//------------------------------------------------------------------------------
// Huffman Coding 
//------------------------------------------------------------------------------

// Method for encoding symbols using fewer bits than previously required.
// Commonly used for lossless compression. 

// The idea is that symbols that occur more frequently are encoded into fewer
// bits and those that occur less frequently are encoded into more bits.

// Encode steps:
// - Build a symbol frequency table.
// - Build a Huffman tree from the frequency table.
// - Build symbol encodings from the Huffman tree.
// - Serialize the Huffman tree as a header in encoded data.
// - Encode symbols into encoded data using encodings table.

// Decode steps:
// - Deserialize the Huffman tree from the header in encoded data.
// - Decode symbols from encoded data using the Huffman tree.

// Example case:
// - 274 input characters (n) @ 1 byte per character (ASCII).
// - 256 possible characters (m).
// - 274 bytes * 8 bits/byte = 2192 bits.

// First, count the occurrences of each character in the input to build the 
// symbol frequency table. This is done in O(n) time.
// - This can be done with a hash table or array of size m.

// Then we build the Huffman tree. This is done in O(m log m) time.
// - This consists of a tree of priority queues. 
// - Build a seperate binary tree for each symbol from the frequency table.
// - Each binary tree contains just a root node with the symbol and the number
// of occurrences of the symbol.
// - Then create a priority queue of all the binary trees.

// Building the trees is O(1).
// Inserting the trees into the priority queue is O(1).

// Then repeatedly merge the two trees with the lowest frequency until there is
// only one tree left. This is done in O(m log m) time.

// The original data took 8 bits per character. The compressed data takes 
// < 4 bits per character, a greater than 50% compression rate.





// Repeat





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

// Data structure used to store items that have priority.
// We want to retrieve the item with the highest priority from the queue.
// A heap is used internally to create a priority queue where the concept of 
// largest value is just changed to represent highest priority.

// Applications of priority queues:
// - Task scheduling performed by OS to determine what to run next on the CPU.
// - Event scheduling performed by GUIs to determine what to display next.
// - Network routing algorithms to determine which packet to send next.
// - Graph algorithms to determine which edge to traverse next.

// Implementation.
// Simply built on top of the heap class by using the heap's comparator to
// determine the priority of the items.

// import java.util.Comparator;

public class PriorityQueue<E> {
    Heap<E> heap;

    public PriorityQueue(Comparator<? super E> comparator) {
        this.heap = new Heap<E>(comparator);
    }

    public int getSize() {
        return heap.getSize();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public void insert(E data) {
        heap.insert(data);
    }

    @SuppressWarnings("unchecked")
    public E extract() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot extract from empty priority queue");
        }

        return heap.extract();
    }

    @SuppressWarnings("unchecked")
    public E peek() throws IllegalStateException {
        if (isEmpty()) {
            throw new IllegalStateException(
                    "Cannot peek into empty priority queue");
        }

        // Default package-internal visibility of tree allows access here
        return (E)heap.tree[0];
    }
}

// Priority Queue Example.

public class Task {
    private int priority;   // Low value means high priority
    String Name;

    public Task(int priority, String name) {
        this.priority = priority;
        Name = name;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return Name;
    }
}

// import java.util.Comparator;

/**
 * Simple task scheduler using a priority queue.
 *
 * Program output:
 *      Priority = 1, Name = Play audio
 *      Priority = 2, Name = Refresh screen
 *      Priority = 3, Name = Handle mouse move
 *      Priority = 4, Name = Swap process memory
 *      Priority = 5, Name = Write data to disk
 *      Priority = 6, Name = Close file
 *      Priority = 7, Name = Write log entry
 */
public class Main {
    public static void main(String args[]) {
        // Populate prioritized tasks
        PriorityQueue<Task> tasks = new PriorityQueue<Task>(
                new Comparator<Task>() {
                    public int compare(Task t1, Task t2) {
                        return t2.getPriority() - t1.getPriority();
                    }
                }
        );
        tasks.insert(new Task(3, "Handle mouse move"));
        tasks.insert(new Task(6, "Close file"));
        tasks.insert(new Task(5, "Write data to disk"));
        tasks.insert(new Task(2, "Refresh screen"));
        tasks.insert(new Task(4, "Swap process memory"));
        tasks.insert(new Task(1, "Play audio"));
        tasks.insert(new Task(7, "Write log entry"));

        // Output tasks in priority order
        while (!tasks.isEmpty()) {
            Task task = tasks.extract();

            System.out.println("Priority = " + task.getPriority()
                    + ", Name = " + task.getName());
        }
    }
}

//------------------------------------------------------------------------------
// Huffman Coding 
//------------------------------------------------------------------------------

// Method for encoding symbols using fewer bits than previously required.
// Commonly used for lossless compression. 

// The idea is that symbols that occur more frequently are encoded into fewer
// bits and those that occur less frequently are encoded into more bits.

// Encode steps:
// - Build a symbol frequency table.
// - Build a Huffman tree from the frequency table.
// - Build symbol encodings from the Huffman tree.
// - Serialize the Huffman tree as a header in encoded data.
// - Encode symbols into encoded data using encodings table.

// Decode steps:
// - Deserialize the Huffman tree from the header in encoded data.
// - Decode symbols from encoded data using the Huffman tree.

// Example case:
// - 274 input characters (n) @ 1 byte per character (ASCII).
// - 256 possible characters (m).
// - 274 bytes * 8 bits/byte = 2192 bits.

// First, count the occurrences of each character in the input to build the 
// symbol frequency table. This is done in O(n) time.
// - This can be done with a hash table or array of size m.

// Then we build the Huffman tree. This is done in O(m log m) time.
// - This consists of a tree of priority queues. 
// - Build a seperate binary tree for each symbol from the frequency table.
// - Each binary tree contains just a root node with the symbol and the number
// of occurrences of the symbol.
// - Then create a priority queue of all the binary trees.

// Building the trees is O(1).
// Inserting the trees into the priority queue is O(1).

// Then repeatedly merge the two trees with the lowest frequency until there is
// only one tree left. This is done in O(m log m) time.

// The original data took 8 bits per character. The compressed data takes 
// < 4 bits per character, a greater than 50% compression rate.
