
# Big O Cheatsheet



# Djikstra's (Shortest Paths)
// - For a graph G = (V, E) runs in O(E lg V) time when implemented correctly.
// - Only works with positive edge weights, for negative edge weights one must
// use the Bellman-Ford algorithm which runs in O(VE) time.



## Data Structures Big O
- Linked list inserts and deletes: O(1)
- Linked list random access: O(n)
- Array inserts and deletes: O(n)
- Array random access: O(1)
- Hash table search, inserts, and deletes: O(1)
- Heap smallest / largest item retrieval: O(log n)
- Quicksort: O(n log n)
## Sorting and Searching
- Search slowest possible time: O(n)

- Linear search: O(n)
    - Data does not need to be sorted.
    - Used when you only need to search the data once, and it is not worth sorting.

- Binary search: O(log n)
    - Requires that data is sorted first, search occurs in-place.
    - Used when you need to repeatedly search data.

- Comparison sort fastest possible time: O(n log n)
- Linear sort fastest possibe time: O(n)

- Insertion Sort: O(n^2)
    - O(n) if inserting new data into an already sorted list.
    - Sorting performed in-place, stable sort.
    - Good for small amounts of data.

- Quicksort: O(n log n)
    - O(n^2) if partitions are incorrectly selected.
    - Sorting performance in-place, unstable sorting.
    - Considered the best general sorting algorithm.

- Mergesort: O(n log n)
    - Same O performance as quicksort but has a slightly larger constant.
    - Requires twice the space of the unsorted array, stable sorting.
    - Works on data that is too large to fit into memory.

- Counting Sort: O(n)
    - O(n + k) performance where k is the range of the elements being sorted (i.e. 0-10).
    - Requires twice the space of the unsorted input + an array of size k, stable sorting.
    - Only works on integral types, good when sorting data with a small range of values.

- Radix Sort: O(n)
    - O(pn + pk) where k is the range of elements and p is the number of digit positions (100 = 3).
    - Uses counting sort under the hood, so requires twice the space of the unsorted input + an array of size k, stable sorting.
    - Only works with 

## Computation
- For loop: O(n)
- Nested for loop: O(n^2)


