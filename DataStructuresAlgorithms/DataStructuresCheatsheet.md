
# Data Structures Cheatsheet

## Primitive Types
* byte (1 byte) = Stores whole numbers from -128 to 127.
* short	(2 bytes) = Stores whole numbers from -32,768 to 32,767.
* int (4 bytes) = Stores whole numbers from -2,147,483,648 to 2,147,483,647.
* long (8 bytes) = Stores whole numbers from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807.
* float	(4 bytes) = Stores fractional numbers. Sufficient for storing 6 to 7 decimal digits.
* double (8 bytes) = Stores fractional numbers. Sufficient for storing 15 decimal digits.
* boolean (1 bit) = Stores true or false values.
* char (2 bytes) = Stores a single character/letter or ASCII values.

## Non-Primitive (Basic) Types
* Arrays = Grouping of objects of a homogenous data type.
    - Stored contiguously in memory as one long string of bytes. When you index the memory you know exactly how many bytes each element takes up because objects have the same type, so if you are storing 4 byte integers you can quickly return the element at index 5 by looking at bytes 4*5 = 20-24 in the string of bytes.
    - Cannot grow or shrink, a new array must be created to grow an array.
    - Fast random access (indexing), slow inserts and deletes as a new array must be created.
* Lists = Grouping of objects of heterogenous data types.
    - Items stored non-contiguously in memory (items are stored randomly in memory).
    - Fast inserts and deletes as prior elements do not need to be shifted in memory, slow random access.
* Structs = Grouping of objects of different data types.
    - Stored contiguously in memory as bits arranged in a specific way.
    - Used to store domain definitions or arguments.
* Strings = Array of characters.


















## Data Structures Overview
- Basics: int, double, float, character, etc.
- Arrays
- Lists
- Singly Linked List
    - Stack
    - Queue
    - Set
- Doubly Linked List
- Hash Tables
    - Very fast inserts, deletes, and searches.
- Trees
    - Good for: sequential access, random access, inserts, and deletes.
- Binary Tree Traversal Methods
    - Pre-order Traversal (depth-first-search): root, left sub-tree, right sub-tree.
    - In-order Traversal: left sub-tree, root, right sub-tree.
    - Post-order Traversal: left sub-tree, right sub-tree, root.
    - Level-order Traversal (breadth-first-search): root, then proceed downward by level visiting nodes from left to right.
- Heaps
    - Implemented as a binary tree in array form.
    - Allow for quick access to the largest or smallest items in a set.
- Priority Queues
    - Implemented using a heap data structure and a comparator to determine priority.
    - Allow for quick access to the highest or lowest priority item in a set.




