
# Data Structures Cheatsheet

### Dependency List
* `string` -> `array` -> `char`
* `stack` / `queue` / `set` -> `singly linked list`
    - A singly linked list is used to hold the contents of the stack, queue, or set.
* `hash table` -> `array`
* `chained hash table` -> `singly linked list` -> `array`
* `hash set` -> `hash table` -> `array`


### Primitive Types
* `byte` (1 byte) = Stores whole numbers from -128 to 127.
* `short` (2 bytes) = Stores whole numbers from -32,768 to 32,767.
* `int` (4 bytes) = Stores whole numbers from -2,147,483,648 to 2,147,483,647.
* `long` (8 bytes) = Stores whole numbers from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807.
* `float` (4 bytes) = Stores fractional numbers. Sufficient for storing 6 to 7 decimal digits.
* `double` (8 bytes) = Stores fractional numbers. Sufficient for storing 15 decimal digits.
* `boolean` (1 bit) = Stores true or false values.
* `char` (2 bytes) = Stores a single character/letter or ASCII values.

### Non-Primitive (Basic) Types
* `Arrays` = Grouping of objects of a homogenous data type.
    - Stored contiguously in memory as one long string of bytes. When you index the memory you know exactly how many bytes each element takes up because objects have the same type, so if you are storing 4 byte integers you can quickly return the element at index 5 by looking at bytes 4*5 = 20-24 in the string of bytes.
    - Cannot grow or shrink, a new array must be created to grow an array.
    - Fast indexing O(1), slow inserts and deletes O(n) as a new array must be created.
* `Lists` = Grouping of objects of heterogenous data types.
    - Items stored non-contiguously in memory (items are stored randomly in memory).
    - Fast inserts and deletes as prior elements do not need to be shifted in memory, slow random access.
* `Structs` = Grouping of objects of different data types.
    - Stored contiguously in memory as bits arranged in a specific way.
    - Used to store domain definitions or arguments.
* `Strings` = Array of characters.

### Linked Lists
* Fast O(1) inserts and deletes, slow O(n) indexing.
* Each element in the list has a reference to the next element, which is why values in the list can be spread randomly across memory instead of contiguously. When elements are inserted or deleted the references just need to be patched.
* `Singly Linked List` = each element links to the next element and can only be
traversed forward.
    - Fields = `head`, `tail`, `data`, `nextElement`.
* `Doubly Linked List` = each element links to the next and previous element, can
be traversed forward and backward. Does take up more memory because each element
has two references.
    - Fields = `head`, `tail`, `data`, `nextElement`, `prevElement`.
* `Circular Linked List` =  list wraps around on itself and has no beginning or end.
This can be implemented with either a single or doubly linked list. Useful for 
applications where you are continually looping through a list.

###  Stacks, Queues, Sets
* `Stack` = Last in first out (LIFO) data storage.
    - Supports three operations: `push`, `pop`, `peek` (look at the top of the stack).
* `Queue` = First in first out (FIFO) data storage.
    - Supports three operations: `enqueue` (add to the end of the queue), `dequeue` (remove from the front of the queue), `peek` (look at the front of the queue).
* `Set` = Unordered collection of items of the same type without any duplicates.
    - Supported operations: `equality`, `subset`, `union`, `intersection`, `difference`.
* A singly linked list is used to store the underlying data for all three of these data types. For example, to test whether a value is in a set, the underlying singly linked list is iterated through and tested for the presence of the value.

### Hash Tables
* Fast O(1) inserts, deletes, and searches, addressing the tradeoffs between arrays and lists.
* Data is stored as an array and accessed internally via a key.
    - Key can be any data type, but it will be hashed to an integer and this integer is used to index the underlying data in the array.
* Collisions can occur if the hash function generates the same integer index for different keys.
* Load factor = number of elements in the array / number of buckets. A low load factor is required to maintain constant time lookups.
* Data (i.e. attributes of a person) are hashed into a hash value (i.e. 20494824024) by combining the attributes of the person in a hashing equation. The output hash value is then converted to an integer representing an index in the array using Modulo Hashing (division method) or Multiplication Hashing.
* `Chained Hash Table` = array of linked lists where each list forms a bucket. This provides collision resistance as keys that are mapped to the same bucket are simply added to the tail of the linked list in that bucket.
* `Hash Sets` = Set that uses a hash table to store the members of the set internally. This results in set indexing that is faster than if the set had been implemented using a `Singly Linked List`.

### Trees













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




