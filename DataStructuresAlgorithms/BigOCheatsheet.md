
# Big O Cheatsheet

## Data Structures
- Linked list inserts and deletes: O(1)
- Linked list random access: O(n)
- Array inserts and deletes: O(n)
- Array random access: O(1)

## Sorting and Searching
- Binary search: O(log n)
- Comparison sorts fastest possible time: O(n log n)
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
    - O(n+k) performance where k is the range of the elements being sorted (i.e. 0-10).
    - Requires twice the space of the unsorted input + an array of size k, stable sorting.
    - Only works on integral types, good when sorting data with a small range of values.


## Computation
- For loop: O(n)
- Nested for loop: O(n^2)