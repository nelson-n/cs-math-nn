
# Data Structures Cheatsheet

### Data Structures Dependency List
* `array` -> No Dependencies
* `linked list` -> No Dependencies
* `binary tree` -> No Dependencies

<\br>

* `string` -> `array` -> `char`
    - Strings are represented as an array of characters.

* `stack` / `queue` / `set` -> `singly linked list`
    - A singly linked list is used to hold the contents of the stack, queue, or set.

* `hash table` -> `array`
    - An array is used to store the values that are saved and indexed in an underlying array.

* `chained hash table` -> `singly linked list` -> `array`
    - A singly linked list is used to represent the buckets of the hash table, and an array is used to store the underlying data.

* `hash set` -> `hash table` -> `array`
    - A hash table is used to store the contents of the set S = {}.

* `heap` -> `singly linked list` / `array`
    - A heap can be implemented with both a singly linked list or array. The array is more efficient as it saves the overhead of the pointers in a singly linked list.

* `priority queue` -> `heap` -> `singly linked list` / `array`
    - A heap is used to store the highest priority item at the top of the priority heap (queue).

* `graph` -> `hash set` -> `hash table` -> `array`
    - A hash set is used to store the set of edges going into and out from a given vertex in a graph.

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
* `Chained Hash Table` = Array of linked lists where each list forms a bucket. This provides collision resistance as keys that are mapped to the same bucket are simply added to the tail of the linked list in that bucket.
* `Hash Sets` = Set that uses a hash table to store the members of the set internally. This results in set indexing that is faster than if the set had been implemented using a `Singly Linked List`.

### Binary Trees
* Recursive data structure in which data is organized into nodes that take a hierarchical form.
* `Binary Tree` Fields = `data`, `left`, `right`.
* Four methods of `Binary Tree` traversal.
    - Pre-Order Traversal = Visit root node, recursively traverse left, recursively traverse right (depth-first search).
    - In-Order Traversal = Recursively traverse left, visit root node, recursively traverse right.
    - Post-Order Traversal = Recursively traverse left, recursively traverse right, visit root node.
    - Level-Order Traversal = Visit root node, then proceed downward visiting nodes at each level from left to right (breadth-first search).

### Heaps, Priority Queues
* `Heaps` allow for quick access to the smallest or largest item in a set.
* One approach would be to just keep a sorted set, but this is expensive as it requires a O(n log n) re-sort with each insertion to the set. 
* The heap approach is to keep the largest (or smallest) item at a known location in the set where inserts cause the item to move to a known location.
    - This is implemented as a `Binary Tree` where the child node is always smaller than the parent node, thus the root node is the largest value in the heap.
* `Heaps` can also be stored as `Arrays` where the root node is stored at index 0. This saves the overhead cost of pointers that are in a linked-list. 
    - The left child of a node is stored at index 2n + 1.
    - The right child of a node is stored at index 2n + 2.
    - The parent of a node is stored at index (n - 1) / 2.
* `Heapsort` = Algorithm where the largest value is pulled off the heap one at a time and placed in a sorted array, runs in O(n log n).
* Heap Insert Algorithm = Insert the new value into the tree, compare to its parent node, if greater then swap the two values, repeat until the value finds its correct position.
* `Priority Queue` = Structure to store the highest priority item at the front. A heap is used internally, but the concept of largest value is changed to represent highest priority using a custom comparator to determine the priority of items.
    - Allows quick access to the highest (or lowest) priority item in a set.

### Graphs
* `Graphs` consist of nodes and vertices with the formal representation G = (V, E).
    - Example: V = {1, 2, 3, 4}, E = {(1, 2), (2, 3), (3, 4), (4, 1), (1, 3), (2, 4)}
* `Graphs` can either be directed or undirected. In a directed graph you can only travel one direction along edges, in an undirected graph you can travel both directions.
* Adjacency List Representation = Most common way to represent graphs in a computer.
    - `Linked List` type representation where each member of the list contains the vertex and a list of vertices adjacent to the matrix.
    - In this structure edges logically exist from the vertex to each adjacent vertex.
    - In a directed graph implementation, each member of the list has three fields.
        - The vertex and any associated data.
        - A `Hash Set` of vertices that form the edges incident to (pointing towards) from the vertex.
        - A `Hash Set` of vertices that form the edges incident from (pointing away) from the vertex.
* `Breadth-First Search` = Starts at a given vertex and explores all vertices reachable from it.
    - Place starting vertex in a queue.
    - For each vertex adjacent to the vertex at the start of the queue, check if the vertex has been visited and if not visit it.
    - Dequeue the vertex at the front of the queue once all adjacent vertices have been visited.
    - Continue running the algorithm recursively until all reachable vertices have been visited.
    - The visited set will contain all vertices reachable from the start vertex. Once can also store the number of vertices traversed before reaching each vertex in the graph, this allows one to find the shortest path from the start vertex to any other vertex.
* `Depth-First Search` = Explore as far as possible in one direction, then backtrack and explore as far as possible in the next direction.
    - Select a starting vertex, if any undiscovered vertices are adjacent to the selected vertex then visit that vertex and repeat this traversal process until you cannot travel any further. Return to the start vertex and label it as finished, then move to the next univisited vertex and repeat the process recursively.
* `Minimum Spanning Tree (Prim's Algorithm)` = Tree that covers all vertices in a weighted, undirected graph at minimum cost.
    - Finds the cheapest way to connect all vertices. 
    - Prim's Algorithm works by adding selecting the cheapest connected edge at any given time. This makes it a greedy algorithm, but despite this it always generates the optimal solution.
* `Shortest Paths (Djikstra's Algorithm)` = Path that connects one vertex to another in a weighted, directed graph at minimum cost.
    - Works by creating a shortest paths tree where the root is the start vertex and the branches are the shortest paths to all other vertices. 
    - Resembles `Breadth-First Search`, the algorithm repeatedly selects a vertex and explores the edges incident to it, storing the path that looks best at the moment (greedy) into the shortest paths tree. Despite being greedy Djikstra's algorithm generates the optimal solution.
    - Each vertex maintains two values, whether it has been visited and the cost of the shortest path to that vertex. 
    - At the end of the algorithm, traversing the shortest paths tree gives the shortest path to each of the vertices in the graph.
