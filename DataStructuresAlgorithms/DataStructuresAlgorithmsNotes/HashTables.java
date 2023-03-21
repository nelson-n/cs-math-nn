
//------------------------------------------------------------------------------
// Hash Tables
//------------------------------------------------------------------------------

/*

- Very fast inserts, deletes, and searches.
    - O(1) inserts, deletes, and lookups.
- Data stored as an array and accessed internally via a key.
    - The key can be any data type, but it will be converted to an integer representation.
    - The key is hashed to an integer, and the integer is used as an index into the array.
- Collisions occur when a hash function generates the same hash for different keys.
    - Typically the number of keys is small relative to the universe of possible keys
    and collisions are rare. 
    - A good hash function also minimizes collisions.
- Load factor = how heavily loaded the hash table is.
    - Load factor calculated as number of elements in the array divided by the
    number of buckets (indexes).
    - Load factor is a measure of how full the hash table is.
    - A low load factor is required to maintain constant time lookups.
    - 20 items in array / 10 buckets = load factor of 2.

- Applications:
- Symbol tables: when a compiler is keeping track of the names and functions
in a program that it is compiling the symbol information needs to be accessed
quickly and works well in a hash table.
- Data dictionary: hash table is a particularly effective way to implement
a data dictionary.

 */

//------------------------------------------------------------------------------
// Hash Functions
//------------------------------------------------------------------------------

// Maps keys to hash code.
// The goal is uniform distribution of hash codes, when we map a data type like
// a string to its integer index, we want these integers to be uniformly distributed.

// Hash example:

// Person.java

public class Person {
    private String firstName;
    private String lastName;
    private double height;
    private double weight;
    private double age;

    public Person(
            String firstName,
            String lastName,
            double height,
            double weight,
            double age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (Double.compare(person.height, height) != 0) return false;
        if (Double.compare(person.weight, weight) != 0) return false;
        if (Double.compare(person.age, age) != 0) return false;
        if (!firstName.equals(person.firstName)) return false;
        return lastName.equals(person.lastName);
    }

    @Override
    // hashCode java base implementation:
    // The hash code below incorporates parts from each of the fields of the 
    // person object to help identify them.
    public int hashCode() {
        int result;
        long temp;
        result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(age);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

// Another example, creating a hash from two values (x and y). 
public class Point {
    private short x;
    private short y;

    public Point(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;

    }

    // Note that to create the hash, the x and y value are not simply added together,
    // x is multiplied by 31 and then added to y. This is to help ensure that the
    // hash is distributed uniformly.
    // For example, if you just added the two then 2x + y would be the same as
    // x + 2y. Multiplying x by 31 helps to ensure that the hash is distributed.
    @Override
    public int hashCode() {
        int result = (int) x;
        result = 31 * result + (int) y;
        return result;
    }
}

// Demoing the above hash function.

/**
 * Program output:
    Axel's hash code: 1586346684
    Kaipo's hash code: 1490009985
    point1's hash code: 33
    point2's hash code: 63
 */
public class Main {
    public static void main(String args[]) {
        Person person1 = new Person("Axel", "Wil", 64.32, 122.39, 24.78);
        Person person2 = new Person("Kaipo", "Valens", 64.32, 122.39, 24.78);

        System.out.println("Axel's hash code: " + person1.hashCode());
        System.out.println("Kaipo's hash code: " + person2.hashCode());

        Point point1 = new Point((short)1, (short)2);
        Point point2 = new Point((short)2, (short)1);

        System.out.println("point1's hash code: " + point1.hashCode());
        System.out.println("point2's hash code: " + point2.hashCode());
    }
}

// If you created two people that differed only slightly in one attributed,
// they would still generate different hash codes. 

//------------------------------------------------------------------------------
// Mapping Hash Codes to Buckets
//------------------------------------------------------------------------------

// Now that data has been hashed to a unique integer, that integer needs to be
// coerced from a key to a value that can represent an index in the array.

// For example, if we have a hash table with 10 buckets, and we hash a key to
// the value 15, we need to map that value to an index in the array between
// 0 and 9.

// There are two common ways to do this:
// 1. Modulo hashing (division method)
// 2. Multiplication hashing

// Modulo hashing (division method):
// - The hash code is divided by the number of buckets and then take the remainder.
// - Formally: bucket = code % m where m is the number of buckets.
// - Example: 
// m = 1699 (prime between 2^10 and 2^11)
// code = 25,657
// bucket = 25,657 % 1,699 = 172
// - We want to avoid powers of 2 for the number of buckets because the code
// will just map to a lower order of bits which will destroy some of the
// uniqueness of the hash code.
// Best to use a number of buckets equal to a prime number that is not close 
// to 2.

// Multiplication hashing:
// - The hash code is multiplied by a constant A, which is between 0 and 1.
// - Typically A is set to sqrt(5) - 1 / 2, which is 0.6180339887.
// - Does not suffer from the need for the number of buckets to be a prime.
// Procedure:
// - multiply hash code by 0.618, then take just the fractional part.
// Example:
// m (num buckets) = 2000, k (hash code) = 6341
// 6341 * 0.618 = 3912.418, then take the fractional part 3912.418 - 3912 = 0.418
// 0.418 * 2000 = 836.36, then round down to 836 (the bucket index)
// This is the preferred method for mapping to a bucket.

//------------------------------------------------------------------------------
// Chained Hash Table
//------------------------------------------------------------------------------

// Consists of an array of linked lists, each list forms a bucket.

// Collision resistance: keys mapped to the same bucket are simply added to the
// linked list at that bucket.

// Inserting an element:
// - Hash the key.
// - Map the hash code to a bucket.
// - Add the key to the head of the list at the bucket index.

// Removing an element:
// - Hash the key.
// - Map the code to the appropriate bucket (index).
// - Search the list at the bucket index for the key.
// - If found, remove it from the list.

// Everything in the above operations is O(1) except for the search, which is
// O(n) where n is the number of elements in the list. Thus the overall time
// is a function of the load factor. If the number of buckets are small and 
// each bucket contains three items in the list (high load factor), than the 
// big O increases.

// The number of buckets must be specified when the hash table is initialized.
// Thus the number of buckets needs to be predicted ex ante.

// Implementation:

// import java.util.Iterator;
// import java.util.NoSuchElementException;

public class ChainedHashTable<K, V> {
    // Table of buckets
    private SinglyLinkedList<KeyValuePair<K, V>>[] table;

    private int size;

    public ChainedHashTable() {
        this(997);  // A prime number of buckets
    }

    @SuppressWarnings("unchecked")
    public ChainedHashTable(int buckets) {
        // Create table of empty buckets
        table = new SinglyLinkedList[buckets];
        for (int i = 0; i < table.length; ++i) {
            table[i] = new SinglyLinkedList<KeyValuePair<K, V>>();
        }

        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return getSize() == 0;
    }

    public void insert(K key, V value) throws
            IllegalArgumentException,
            DuplicateKeyException {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }
        if (contains(key)) {
            throw new DuplicateKeyException();
        }

        getBucket(key).insertHead(new KeyValuePair<K, V>(key, value));
        ++size;
    }

    public V remove(K key) throws
            IllegalArgumentException,
            NoSuchElementException {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        // If empty bucket
        SinglyLinkedList<KeyValuePair<K, V>> bucket = getBucket(key);
        if (bucket.isEmpty()) {
            throw new NoSuchElementException();
        }

        // If at head of bucket
        SinglyLinkedList<KeyValuePair<K, V>>.Element elem = bucket.getHead();
        if (key.equals(elem.getData().getKey())) {
            --size;
            return bucket.removeHead().getValue();
        }

        // Scan rest of bucket
        SinglyLinkedList<KeyValuePair<K, V>>.Element prev = elem;
        elem = elem.getNext();
        while (elem != null) {
            if (key.equals(elem.getData().getKey())) {
                --size;
                return bucket.removeAfter(prev).getValue();
            }
            prev = elem;
            elem = elem.getNext();
        }

        throw new NoSuchElementException();
    }

    public V lookup(K key) throws
            IllegalArgumentException,
            NoSuchElementException {
        if (key == null) {
            throw new IllegalArgumentException("key must not be null");
        }

        // Scan bucket for key
        SinglyLinkedList<KeyValuePair<K, V>>.Element elem =
                getBucket(key).getHead();
        while (elem != null) {
            if (key.equals(elem.getData().getKey())) {
                return elem.getData().getValue();
            }
            elem = elem.getNext();
        }

        throw new NoSuchElementException();
    }

    public boolean contains(K key) {
        try {
            lookup(key);
        } catch (IllegalArgumentException ex) {
            return false;
        } catch (NoSuchElementException ex) {
            return false;
        }

        return true;
    }

    private SinglyLinkedList<KeyValuePair<K, V>> getBucket(K key) {
        // Division method
        return table[Math.abs(key.hashCode()) % table.length];
    }

    private class KeysIterator implements Iterator<K> {
        private int remaining;  // Number of keys remaining to iterate
        private int bucket;     // Bucket we're iterating
        private SinglyLinkedList<KeyValuePair<K, V>>.Element elem;
                                // Position in bucket we're iterating

        public KeysIterator() {
            remaining = ChainedHashTable.this.size;
            bucket = 0;
            elem = ChainedHashTable.this.table[bucket].getHead();
        }

        public boolean hasNext() {
            return remaining > 0;
        }

        public K next() {
            if (hasNext()) {
                // If we've hit end of bucket, move to next non-empty bucket
                while (elem == null) {
                    elem = ChainedHashTable.this.table[++bucket].getHead();
                }

                // Get key
                K key = elem.getData().getKey();

                // Move to next element and decrement entries remaining
                elem = elem.getNext();
                --remaining;

                return key;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public Iterable<K> keys() {
        return new Iterable<K>() {
            public Iterator<K> iterator() {
                return new KeysIterator();
            }
        };
    }
}

//------------------------------------------------------------------------------
// Hash Table Demo Usage
//------------------------------------------------------------------------------

/**
 * Program determines whether an unsorted array contains duplicates.
 *
 * Our first approach might be to loop over array and for each element
 * loop over remaining elements to see if they match.  This would
 * be O(n^2).
 *
 * A better approach would be to sort the array (O(n lg n)) then
 * loop over the array to see if there are consecutive duplicates
 * O(n).  Overall time is O(n lg n).
 *
 * The solution we implement here loops over the elements in the
 * array, inserting each into a hashtable (O(n)).  If any duplicates
 * are detected we have our answer.  Worst case there are no duplicates
 * and we've inserted everything into the hashtable.  Overall time
 * is O(n).
 */
public class Main {
    public static void main(String args[]) {
        
        // A large array
        Integer data[] = { 1236, 1265, -1262, 32, /* ... */ 1662 };

        // Load array into hashtable O(n)
        ChainedHashTable<Integer, Integer> table =
                new ChainedHashTable<Integer, Integer>(data.length);
        boolean hasDuplicates = false;
        for (Integer i : data) {
            try {
                table.insert(i, i);
            } catch (DuplicateKeyException ex) {
                hasDuplicates = true;
                break;
            }
        }

        if (hasDuplicates) {
            System.out.println("Duplicates found");
        } else {
            System.out.println("No duplicates found");
        }
    }
}

//------------------------------------------------------------------------------
// Hash Sets
//------------------------------------------------------------------------------

// Many sets use linked lists to store data internally.
// A hash set uses a hash table to store data internally.

// The benefit is that inserts, lookups, and removals are O(1).

// The drawbacks is that it requires a data type that can be hashed.

// Implementation:
// import java.util.Iterator;
// import java.util.NoSuchElementException;

public class HashSet<E> implements Iterable<E> {
    private ChainedHashTable<E, E> table;

    public HashSet() {
        this(997);  // A prime number of buckets
    }

    public HashSet(int buckets) {
        table = new ChainedHashTable<E, E>(buckets);
    }

    public int getSize() {
        return table.getSize();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    public void insert(E data) throws
            IllegalArgumentException,
            DuplicateElementException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        // Insert data into the table
        try {
            table.insert(data, data);
        } catch (DuplicateKeyException ex) {
            throw new DuplicateElementException();
        }
    }

    public E remove(E data) throws
            IllegalArgumentException,
            NoSuchElementException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        return table.remove(data);
    }

    public boolean isMember(E data) throws
            IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        return table.contains(data);
    }

    public E getMember(E data) throws
            IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        return table.lookup(data);
    }


    public HashSet<E> union(HashSet<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }

        HashSet<E> result = new HashSet<E>(getSize() + other.getSize());

        // Add all items from this
        for (E data : this) {
            try {
                result.insert(data);
            } catch (DuplicateElementException ex) {
                // Ignore duplicate failures
            }
        }

        // Add all items from other
        for (E data : other) {
            try {
                result.insert(data);
            } catch (DuplicateElementException ex) {
                // Ignore duplicate failures
            }
        }

        return result;
    }

    public HashSet<E> intersection(HashSet<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }

        HashSet<E> result = new HashSet<E>(getSize() + other.getSize());

        // Add elements from this that are in other
        for (E data : this) {
            if (other.isMember(data)) {
                try {
                    result.insert(data);
                } catch (DuplicateElementException ex) {
                    // Ignore duplicate failures
                }
            }
        }

        return result;
    }

    public HashSet<E> difference(HashSet<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }

        HashSet<E> result = new HashSet<E>(getSize() + other.getSize());

        // Add elements from this that are not in other
        for (E data : this) {
            if (!other.isMember(data)) {
                try {
                    result.insert(data);
                } catch (DuplicateElementException ex) {
                    // Ignore duplicate failures
                }
            }
        }

        return result;
    }

    public boolean isSubset(HashSet<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }

        // If this has more elements than other, this can't be a subset
        if (getSize() > other.getSize()) {
            return false;
        }

        // If any member in this is not in other, this is not a subset
        for (E data : this) {
            if (!other.isMember(data)) {
                return false;
            }
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HashSet))
            return false;

        HashSet<E> other = (HashSet<E>)o;

        // If sets equal size and one is subset of other, then they're equal
        return getSize() == other.getSize()
                && isSubset(other);
    }

    public Iterator<E> iterator() {
        return table.keys().iterator();
    }
}

