
// Sorting and Searching

//------------------------------------------------------------------------------
// Sorting Properties
//------------------------------------------------------------------------------

/*

Comparison Sorts
- Rely on comparing elements to place them in the correct order.
- Requires a comparison function to compare elements.
- Fastest possible time: O(n log n)

Linear Time Sorts
- Cannot always be applied, requires that data has characteristics that allow
for linear time sorting.
- Fastest possible time: O(n)

In-Place Sorts
- Do not require additional space.

Extra Storage Sorts
-  Require additional space during the sort.

Unstable Sort
- Elements that compare as equal may be reordered during the sort.

Stable Sort
- Elements considered equal will not be re-ordered during the sort.

Uses or Sorting
- Order statistics, i.e. find the smallest element in an array.
- Binary search, an efficient search method that requires data to be sorted.
- Databases, storing data in sorted order allows for fast reads and writes.
- Spreadsheets, spellcheckers, many more.

 */

//------------------------------------------------------------------------------
// Comparable Interface
//------------------------------------------------------------------------------

// Special interface in Java that allows for the comparison of objects.

// Example circle class.
public class Circle implements Comparable<Circle> {
    private int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int compareTo(Circle other) {
        if (radius < other.radius) {
            return -1;
        }
        if (radius > other.radius) {
            return 1;
        }
        return 0;
    }
}

// Comparable is a generic interface of type T, where T is the object that you
// would like to compare.

// The interface provides one method, compareTo, which allows you to compare
// objects of the same type.

// Comparable expects an integer to be returned that is either positive, negative,
// or zero. Ordering will occur depending on whether it is negative (before) or
// positive (after) or zero (equal). Because of this the above program can be
// simplified so that the circle radius is just subtracted from the other circle.

public class Circle implements Comparable<Circle> {
    private int radius;

    public Circle(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int compareTo(Circle other) {
        return radius - other.radius;
    }
}

// The limitation of the Comparable interface is that it only allows for 
// comparison in one way: compareTo

//------------------------------------------------------------------------------
// Comparator Interface
//------------------------------------------------------------------------------

// The Comparator interface allows for the comparison of objects in multiple ways.

// Also allows for the comparison of objects of different types.

// For example, take a Circle class that in addition to radius has fields for x
// and y coordinates.
public class Circle implements Comparable<Circle> {
    private int radius;
    private int x;
    private int y;

    public Circle(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int compareTo(Circle other) {
        return radius - other.radius;

    }
}

// import java.util.Comparator;

// Compare two circles using radius
public class RadiusComparator implements Comparator<Circle> {
    public int compare(Circle c1, Circle c2) {
        if (c1.getRadius() < c2.getRadius()) {
            return -1;
        }
        if (c1.getRadius() > c2.getRadius()) {
            return 1;
        }
        return 0;
    }
}
// Compare two circles using radius descending
public class RadiusDescendingComparator implements Comparator<Circle> {
    public int compare(Circle c1, Circle c2) {
        if (c1.getRadius() < c2.getRadius()) {
            return 1;   // Flip result from non-descending comparator
        }
        if (c1.getRadius() > c2.getRadius()) {
            return -1;  // Flip result from non-descending comparator
        }
        return 0;
    }
}

// Compare two circles using distance from origin
public class DistanceFromOriginComparator implements Comparator<Circle> {
    public int compare(Circle c1, Circle c2) {
        if (c1.getDistanceFromOrigin() < c2.getDistanceFromOrigin()) {
            return -1;
        }
        if (c1.getDistanceFromOrigin() > c2.getDistanceFromOrigin()) {
            return 1;
        }
        return 0;
    }
}

// Below is a two level sort, first by distance from origin is compared and then
// if they are equal, the radius is compared.

// Compare two circles using distance from origin then radius descending
public class DistanceFromOriginThenRadiusDescendingComparator implements Comparator<Circle> {
    public int compare(Circle c1, Circle c2) {
        int result;

        // Compare first using distance from origin
        DistanceFromOriginComparator dfoComparator =
                new DistanceFromOriginComparator();
        result = dfoComparator.compare(c1, c2);
        if (result != 0) {
            return result;
        }

        // If equal distance from origin, compare using radius descending
        RadiusDescendingComparator rdComparator =
                new RadiusDescendingComparator();
        return rdComparator.compare(c1, c2);
    }
}

// Anonymous comparators can be used to define type agnostic comparators.
// The following is an example of comparing two circles "on the fly" using 
// an in-class comparator call.

public class Circle {
    private double radius;
    private double x;
    private double y;

    public Circle(double radius, double x, double y) {
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public double getRadius() {
        return radius;
    }

    // The method we will use to sort the circles.
    public double getDistanceFromOrigin() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }
}

public class Main {
    public static void main(String[] args) {
        Circle c1 = new Circle(5, 1, 2);
        Circle c2 = new Circle(6, 2, 1);

        // Instantiate anonymous Comparator and call compare on it, this allows
        // for easy, in-line comparison.
        System.out.println("Radius comparison (c1, c2): "
                + new Comparator<Circle>() {
                    public int compare(Circle c1, Circle c2) {
                        if (c1.getRadius() < c2.getRadius()) {
                            return -1;
                        }
                        if (c1.getRadius() > c2.getRadius()) {
                            return 1;
                        }
                        return 0;
                    }
                }.compare(c1, c2));
    }
}

//------------------------------------------------------------------------------
// Insertion Sort
//------------------------------------------------------------------------------

// Simple sorting algorithm where you insert the next unsorted element into
// a sorted sequence.

// Inefficient for larger sets of data, but can be performed in-place since you
// are just moving elements from the unsorted to the sorted pile, so you do 
// not need additional space.

// Good algorithm for inserting new data into an already sorted set.
// Also works well with small amounts of data and doubly linked lists.

// In the worst case the list starts in reverse order and you have to search all
// the way through the list at each step: O(n/2) * O(n) = O(n^2)
// - n steps backwards as you move j positions through the list, thus n*j = O(n^2)
// - Put another way, you have to search through the list once for each element, thus n*n = O(n^2)

// Inserting a new value into an already sorted list is O(n) since you only have
// to search through the list once.

// Insertion sort implementation:

// import java.util.Comparator;

// Note that the user passes in an array of values and a comparator that they 
// have defined for comparing the objects.

public class InsertionSort {
    public static <T> void insertionSort(T[] array, Comparator<? super T> comparator) {

        // Repeatedly insert the next unsorted element (the key) into the
        // sorted elements
        for (int j = 1; j < array.length; ++j) {
            T key = array[j];

            // Move backward through sorted elements, shifting each one up
            // until we find the location where the key will be inserted
            int i = j - 1;
            while (i >= 0 && comparator.compare(array[i], key) > 0) {
                array[i + 1] = array[i];
                --i;
            }

            // Insert the key
            array[i + 1] = key;
        }
    }
}

//------------------------------------------------------------------------------
// Quicksort
//------------------------------------------------------------------------------

// Widely regarded as the best general sort.

// Divide and conquer algorithm.    
// You find the median value and continually split the list into two halves to sort.

// Worst case performance is O(n^2) if the median values are chosen poorly.

// If it can be guaranteed that good median values will be chosen, then you 
// can guarantee average case performance which is O(n log n).
// - The key to average performance is selecting median values that results
// in a balanced partition.

// To guarantee good median values, select 3 values randomly from the pile and
// select the median value of the partition. This virtually guarantees that
// average case performance.

// This random selection makes quicksort an unstable algorithm as it might swap
// the position of two equal elements. 

// The order of operations is: quicksort the array, then split the array at the
// partitions and quicksort the two halves of the array, then split again until
// the entire array is broken in two arrays of two that are fully sorted.

// The first partition is order n, you need to move through the whole array.
// The number of times we have to run partitions of order n is equal to log n.
// Thus you end up with log n number of partitions, each costing log n results
// in O(n log n).

// For example, array length 8 results in 3 partitions to arrays of length 4, 2, and 1.
// The number of partitions if the partitions are divided correctly is 8 log base 2.
// i.e. 3^2 = 8. For an array of length 32 we can expect 2^5 partitions. This log 
// base two is where the O(n log n) comes from.

// It can be shown mathematically that the "median of three" method of selecting
// medians guarantess good partitions and n log n performance.

// Sorting occurs in-place.

// Works well with arrays due to the need for random access in the algorithm.
// Good for medium to large datasets.

// Does not work if you cannot load all of the data into memory at once.




// Quicksort Implementation:

// import java.util.Comparator;
// import java.util.Random;

public class QuickSort {
    public static <T> void quickSort(T[] array, Comparator<? super T> comparator) {
        // Simply calling quicksort on the entire way.
        quickSortRecursive(array, 0, array.length - 1, comparator); 
    }

    // This function will recursively call itself until the array is fully partitioned
    // and sorted.
    private static <T> void quickSortRecursive(
            T[] array,
            int i,
            int k,
            Comparator<? super T> comparator) {

        // Stop the recursion when it is not possible to partition further
        if (i >= k) {
            return;
        }

        // Determine where to partition the elements
        int j = partition(array, i, k, comparator);

        // Sort the left partition
        quickSortRecursive(array, i, j, comparator);

        // Sort the right partition
        quickSortRecursive(array, j + 1, k, comparator);
    }

    // Partition algorithm.
    private static <T> int partition(
            T[] array,
            int i,
            int k,
            Comparator<? super T> comparator) {

        // Use the median-of-three method to find the partition value
        T p = medianOfThree(array, i, k, comparator);

        // Create two partitions around the partition value
        --i;
        ++k;
        while (true) {
            // Move left until an element is found in the wrong partition
            do {
                --k;
            } while (comparator.compare(array[k], p) > 0);

            // Move right until an element is found in the wrong partition
            do {
                ++i;
            } while (comparator.compare(array[i], p) < 0);

            if (i >= k) {
                // Stop partitioning when the left and right indices cross
                break;
            } else {
                // Swap the elements at the indices
                T temp = array[i];
                array[i] = array[k];
                array[k] = temp;
            }
        }

        return k;
    }

    private static <T> T medianOfThree(
            T[] array,
            int i,
            int k,
            Comparator<? super T> comparator) {

        // Get 3 random values from the array
        Random random = new Random();
        T a = array[random.nextInt(k - i + 1) + i];
        T b = array[random.nextInt(k - i + 1) + i];
        T c = array[random.nextInt(k - i + 1) + i];

        // Return the median of the 3 values
        if (comparator.compare(a, b) > 0) {             // a > b
            if (comparator.compare(b, c) > 0) {         // a > b > c
                return b;
            } else if (comparator.compare(a, c) > 0) {  // a > c > b
                return c;
            } else {                                    // c > a > b
                return a;
            }
        } else {                                        // b > a
            if (comparator.compare(a, c) > 0) {         // b > a > c
                return a;
            } else if (comparator.compare(b, c) > 0) {  // b > c > a
                return c;
            } else {                                    // c > b > a
                return b;
            }
        }
    }
}

//------------------------------------------------------------------------------
// Merge Sort
//------------------------------------------------------------------------------

// Divide and conquer recursive algorithm.

// Performs as well as quicksort, but requires twice the space of the unsorted
// data and has higher constant costs than quicksort.

// Merge sort is stable.

// Works with data that is too large to fit in memory because it divides the 
// data into predictable sizes that can be loaded and unloaded from RAM.

// Works by dividing the original pile into halves until you have individual elements,
// then merging the piles back together in sorted order.
// - When merging the piles back together you need to allocate memory for a new
// merged array, which is whcy it requires twice the space of the unsorted data.

// At each level of the mergesort the number of operations is equal to the number
// of elements, so that gives O(n) already for each level. The questions then becomes
// how many levels of sorting are there, and just like quicksort there it is log 
// base 2 of n levels. Thus the total number of operations is O(n log n).
// - Note that it is log base 2 because each level you are splitting in half.

// Works with arrays, singly linked lists, and doubly linked lists.

// You should prefer to use quicksort unless your data is too big to fit in memory
// or you need a stable merge. 

// import java.util.Comparator;

public class MergeSort {

    // The mergeSort function just splits arrays recursively down to individual
    // elements, then the merge function joins the elements back together in order.

    public static <T> void mergeSort(
            T[] array,
            Comparator<? super T> comparator) {

        // Recursive funcion that will ultimately sort the entire array.
        mergeSortRecursive(array, 0, array.length - 1, comparator);
    }

    private static <T> void mergeSortRecursive(
            T[] array,
            int i,
            int k,
            Comparator<? super T> comparator) {

        // Stop the recursion when no more divisions can be made
        // When everything has been split to single elements
        if (i >= k) {
            return;
        }

        // Determine where to divide the elements
        int j = (i + k - 1) / 2;

        // Sort the left division
        mergeSortRecursive(array, i, j, comparator);

        // Sort the right division
        mergeSortRecursive(array, j + 1, k, comparator);

        // Merge the two sorted divisions
        merge(array, i, j, k, comparator);
    }

    // See the method implementation for an explanation as to why we're using
    // the SuppressedWarnings annotation.
    @SuppressWarnings("unchecked")
    private static <T> void merge(
            T[] array,
            int i,
            int j,
            int k,
            Comparator<? super T> comparator) {

        // Create array to hold merged results.  In java we cannot
        // create an array of generic objects (e.g. new T[]) so we must
        // create an array of Objects and cast the elements later when
        // we're ready to use them.
        Object[] merged = new Object[k - i + 1];

        // Continue while either sorted sequence has elements to merge
        int pos1 = i;       // Position in first sequence
        int pos2 = j + 1;   // Position in second sequence
        int posM = 0;       // Position in merged sequence
        while (pos1 <= j || pos2 <= k) {
            if (pos1 > j) {
                // The first sequence has no more elements to merge,
                // merge all remaining elements from the second sequence
                while (pos2 <= k) {
                    merged[posM] = array[pos2];
                    ++posM;
                    ++pos2;
                }
            } else if (pos2 > k) {
                // The second sequence has no more elements to merge,
                // merge all remaining elements from the first sequence
                while (pos1 <= j) {
                    merged[posM] = array[pos1];
                    ++posM;
                    ++pos1;
                }
            } else {
                // Both sequences have more elements to merge
                if (comparator.compare(array[pos1], array[pos2]) < 0) {
                    // Next element from sequence 1 is smaller, merge it
                    merged[posM] = array[pos1];
                    ++posM;
                    ++pos1;
                } else {
                    // Next element from sequence 2 is smaller, merge it
                    merged[posM] = array[pos2];
                    ++posM;
                    ++pos2;
                }
            }
        }

        // Copy the merged sequence into the original array
        for (int index = 0; index < merged.length; ++index) {
            // This cast from Object to T results in an unchecked warning.
            // We know this is safe since we stored T's in the merged
            // array.  Therefore we suppress this warning with the
            // @SuppressWarning annotation at the method level.
            array[i + index] = (T)merged[index];
        }
    }
}

//------------------------------------------------------------------------------
// Counting Sort
//------------------------------------------------------------------------------

// Linear time sort.
// Counts how many times an element of a set occurs to see how it should be ordered.
// - Does not require comparisons.
// Improves upon O(n log n) upper bound of comparison sort algorithms.
// Requires that elements in the set are integral types.
// We also need to know what the largest value that can occur is.
// Stable sort.

// Cost: O(n + k) where n is the number of elements and k is the max integer in the set + 1.
// If the range of the data is small (i.e. 0-10), even if there are a 1m values in
// the set (n) then k acts as a constant and the big O is O(n).

// Space: Takes twice the space of the original list + space for the integer counts.

// Works with arrays, singly linked lists, and doubly linked lists.

// Good when sorting integral types with a small range (small maximum values).

// Implementation: 

public class CountingSort {
    public static void countingSort(int[] array, int maxElementValue) {

        // Create array to hold the counts (set counts = to 0)
        int[] counts = new int[maxElementValue + 1];
        for (int i = 0; i <= maxElementValue; ++i) {
            counts[i] = 0;
        }

        // Count the occurrences of each element, increment the index of the count
        // array by 1 for each occurrence
        for (int value : array) {
            ++counts[value];
        }

        // Sum up the counts by position
        for (int i = 1; i <= maxElementValue; ++i) {
            counts[i] = counts[i] + counts[i - 1];
        }

        // Use the counts to position each element where it belongs
        int[] sorted = new int[array.length];
        for (int i = array.length - 1; i >= 0; --i) {
            sorted[counts[array[i]] - 1] = array[i];
            --counts[array[i]];
        }

        // Replace the original array with the sorted array
        System.arraycopy(sorted, 0, array, 0, array.length);
    }
}

//------------------------------------------------------------------------------
// Radix Sort
//------------------------------------------------------------------------------

// Linear time sort.

// Sorts data in pieces called digits, from the digit in the least significant
// position to the digit in the most significant position.

// Example sorting radix-10 numbers:
// Unsorted: {15, 12, 49, 16, 36, 40}
// After sorting least significant digit (1s place): {40, 12, 15, 16, 36, 49}
// After sorting most significant digit (10s place): {12, 15, 16, 36, 40, 49}

// Sort each place, digit-wise.

// The sorting algorithm used in this approach must be stable because when 
// you are sorting the 10s place for example (10, 12), and you have two 1s,
// you want to keep the order of the 10s place the same (10, 12) and not
// reverse the numbers.

// Can only be applied to integers, but most data types such as strings can
// be converted to integers.

// The Big O is the same as counting sort, but you need to apply counting sort 
// for each digits (0s place, 10s place, 100s place, etc). 

// Cost: O(pn + pk)
// p = number of passes (number of digits)
// n = number of elements
// k = range of values for each digit (10 for radix-10)

// Radix is the number of digits utilized in a positional number system before
// "rolling over" to the next digit's place.
// - For example in a base 10 number system the digits used are 0-9, so the radix is 10.

// Can only be used with arrays.

// Implementation:
public class RadixSort {
    public static void radixSort(
            int[] array,
            int numPositions,
            int radix) {

        // Create array to hold the counts
        int[] counts = new int[radix + 1];

        // Create array to hold the sorted elements
        int[] sorted = new int[array.length];

        // Sort once for each position from least to most significant
        for (int position = 0; position < numPositions; ++position) {
            // Initialize the counts
            for (int i = 0; i <= radix; ++i) {
                counts[i] = 0;
            }

            // Calculate the position's power (e.g. 10^0, 10^1, 10^2)
            int positionValue = (int)Math.pow(radix, position);

            // Count the occurrences of each digit in that position
            for (int value : array) {
                int index = (value / positionValue) % radix;
                ++counts[index];
            }

            // Adjust each count to reflect the counts before it
            for (int i = 1; i <= radix; ++i) {
                counts[i] = counts[i] + counts[i - 1];
            }

            // Use the counts to position each element where it belongs
            for (int i = array.length - 1; i >= 0; --i) {
                int index = (array[i] / positionValue) % radix;
                sorted[counts[index] - 1] = array[i];
                --counts[index];
            }

            // Replace the original array with the sorted array
            System.arraycopy(sorted, 0, array, 0, array.length);

            // Move back up to the top of the for loop and sort again.

        }
    }
}

//------------------------------------------------------------------------------
// Searching Introduction
//------------------------------------------------------------------------------

/*
- Looking for a target value (key) in a set of data.
- Generally requires random access or having the data sorted in some way.
- There are basic searches for all data structures and generalized search algorithms
for specific data structures.

 */

//------------------------------------------------------------------------------
// Linear Search
//------------------------------------------------------------------------------

// Linear Search: O(n)
//    - Iterate through and array and compare the value you are looking for to each
//    value until you find the value that you are looking for.
//    - Slowest possible search.

public class LinearSearch {
    public static <T> int linearSearch(T[] array, T key) {

        // Scan through all elements of the array
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(key)) {
                // Key found
                return i;
            }
        }

        // Key not found
        return -1;
    }
}

// If you need to search unsorted data once, linear search works fine. If you
// need to repeatedly search then it may be better to sort the data.

//------------------------------------------------------------------------------
// Binary Search
//------------------------------------------------------------------------------

// Improvement over linear search: O(log n)
// Divides the sorted array down the middle, then compares the key to the middle
// value and depending on whether it is > or <, it then looks at that half of 
// the array, then divides those values in half and continues until the key is 
// found.

// As with all repeatedly divide by 2 algorithms, the Big O portion of the 
// divide by 2 is log n.
// - Each division reduces the problem size by half.
// - Repeatedly dividing by two is logarithmic base 2.
// - i.e. 64 requires log base 2 of 64 = 6 steps to get to 1 element.

// import java.util.Comparator;

public class BinarySearch {
    public static <T> int binarySearch(
            T[] sortedArray,
            T key,
            Comparator<? super T> comparator) {

        // Continue searching until the left and right indices cross
        int left = 0;
        int right = sortedArray.length - 1;
        while (left <= right) {
            int middle = (left + right) / 2;

            int compareResult = comparator.compare(key, sortedArray[middle]);
            if (compareResult > 0) {
                // Key larger than middle, continue search above middle
                left = middle + 1;
            } else if (compareResult < 0) {
                // Key smaller than middle, continue search below middle
                right = middle - 1;
            } else {
                // Key found at middle, return middle index
                return middle;
            }
        }

        // Key not found
        return -1;
    }
}
