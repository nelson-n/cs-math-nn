
# Big O Cheatsheet

### Data Structures 
* `Linked Lists`
    - Inserts and deletes: O(1)
    - Random access: O(n)
* `Arrays`
    - Inserts and deletes: O(n)
    - Random access: O(1)
* `Hash Tables`
    - Inserts, deletes, and random access: O(1)
* `Heaps`
    - Largest (smallest) item retrieval: O(log n)
* `Priority Queues`
    - Highest priority item retrieval: O(log n)

### Computation
- `For Loop`: O(n)
- `Nested For Loop`: O(n^2)

### Searching
* `Linear Search`: O(n)
    - Slowest possible search, list/array is iterated through until element is found.
    - Used when you only need to search the data once, and it is not worth sorting.

* `Binary Search`: O(log n)
    - Requires that data is sorted first, search occurs in-place.
    - Used when you need to repeatedly search data.

### Sorting
* `Insertion Sort`: O(n^2)
    - O(n) if inserting new data into an already sorted list.
    - Sorting performed in-place, stable sort.
    - Good for small amounts of data.

* `Quicksort`: O(n log n)
    - O(n^2) if partitions are incorrectly selected.
    - Sorting performance in-place, unstable sorting.
    - Considered the best general sorting algorithm.

* `Mergesort`: O(n log n)
    - Same performance as quicksort but has a slightly larger n constant.
    - Requires twice the space of the unsorted array, stable sorting.
    - Works on data that is too large to fit into memory.

* `Counting Sort`: O(n)
    - O(n + k) performance where k is the range of the elements being sorted (i.e. 0-10).
    - Requires twice the space of the unsorted input + an array of size k, stable sorting.
    - Only works on integer types, good when sorting data with a small range of values.

* `Radix Sort`: O(n)
    - O(pn + pk) where k is the range of elements and p is the number of digit positions (100 = 3).
    - Uses counting sort under the hood, so requires twice the space of the unsorted input + an array of size k, stable sorting.
    - Only works with integers.

### Traversal
* `Graph Shortest Path` (`Djikstra's Algorithm`): O(E log V)
    - O(E log V) where E is the number of edges and V is the number of vertices in the graph.
    - Only works with positive edge weights, for negative edge weights one must use the Bellman-Ford algorithm which runs in O(VE) time.
