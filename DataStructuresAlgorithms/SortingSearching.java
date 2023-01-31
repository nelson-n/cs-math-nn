
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