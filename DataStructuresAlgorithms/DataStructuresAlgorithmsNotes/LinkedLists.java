
//------------------------------------------------------------------------------
// Linked Lists
//------------------------------------------------------------------------------

/* 

- Not provided as a built-in data structure in Java, however they are available
in the standard library.
- While arrays have slow inserts and deletes, linked lists have O(1) inserts
and deletes (as the list grows, insert and delete time stays the same).
- However, while arrays have O(1) access, linked lists have slow O(n) access.

- Linked Lists have the following architecture.
    - Head = reference to the first element in the list.
    - Tail = reference to the last element in the list.
    - Each element in the list has a reference to the next element in the list and
    the location of the element in memory. For this reason the value of the list
    can be spread randomly across memory as opposed to being stored contiguously, 
    and when a new element is inserted there does not need to be a shift of any
    elements in memory, the memory references just need to be patched.

- Types of Linked Lists:
    - Singly linked list: each element links to the next element and can only be
    traversed forward.
    - Doubly linked list: each element links to the next and previous element, can
    be traversed forward and backward. Does take up more memory because each element
    has two references.
    - Circular linked list: list wraps around on itself and has no beginning or end.
    This can be implemented with either a single or doubly linked list. Useful for 
    applications where you are continually looping through a list.

- Linked List applications:
    - Useful when the order of elements matters.
    - When random access is not required.
    - When you do not know how many elements will go in the list. 

- Linked List use cases:
    - Heap management where elements of memory are inserted and deleted frequently.
    - Filesystems where large files are spread across multiple blocks of memory.
    - Other data structures such as stacks, queues, hash tables, and trees.

* Note that linked lists and arrays are the two fundamental data structures that
are used to implement all most other data structures.

- Linked lists have O(1) inserts and deletes, but O(n) access.
- Arrays have O(1) access, but O(n) inserts and deletes.

 */

//------------------------------------------------------------------------------
// Singly Linked Lists
//------------------------------------------------------------------------------

/* 

- Inserting into a singly linked list:
    - Allocate a new element on the heap with a desired value.
    - Find the beforeElement, the item of the list that will be before the new
    element in the linked list.
    - Find the element beforeElement current points to and point the new element
    to it.
    - Change the beforeElement to refer to the new element.

- Note that in the above scenario the insert operation only effects two elements,
the new element and the beforeElement. It does not matter how many items are in
the linked list, the insert operation will always take the same amount of time.
    - However, if we wanted to perform the insert in the middle of the list, we 
    would first have to random access the middle of the list which would take O(n) 
    time. 
    - For this reason it is more efficient to insert into the front or the back
    of the list.

*/

// Singly Linked List Implementation

// Attributes of an Element object:
// - E data = the data passed by the user.
// - Element next = pointer to the next element in the list.\

// Attributes of a SinglyLinkedList object:
// - Element head = reference to the first element in the list.
// - Element tail = reference to the last element in the list.
// - int size = the number of elements in the list.

import java.util.NoSuchElementException;

// Generic element parameter <E>, the type of element held by the linked list

public class SinglyLinkedList<E> {

    // Nested class representation of one element in the linked list.
    public class Element {

        // Defining what will be in the element.
        private E data; // The data E passed by the user.
        private Element next; // Pointer to the next element in the list.

        // Constructor for the element.
        private Element(E data) {
            this.data = data;
            this.next = null;
        }

        // Element getters.
        public E getData() {
            return data;
        }

        public Element getNext() {
            return next;
        }

        private SinglyLinkedList getOwner() {
            return SinglyLinkedList.this;
        }
    }

    // Defining what a list contains using the element class implemented above.
    // All lists will have a head, tail, and size.
    private Element head;
    private Element tail;
    private int size;

    // Singly linked list getters.
    public Element getHead() {
        return head;
    }

    public Element getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Method for inserting a new element at the head of the list.
    public Element insertHead(E data) {
        Element newElement = new Element(data); // Define new element being added.

        if (isEmpty()) {
            // Insert into empty list
            head = newElement;
            tail = newElement;
        }
        else {
            // Insert into non-empty list
            newElement.next = head;
            head = newElement;
        }

        ++size; // Increase size of the list.

        return newElement; // The new element is returned so that it can be operated on.
    }

    // Method for inserting new element at the tail of the list.
    public Element insertTail(E data) {
        Element newElement = new Element(data);

        if (isEmpty()) {
            // Insert into empty list
            head = newElement;
            tail = newElement;
        }
        else {
            // Insert into non-empty list
            tail.next = newElement;
            tail = newElement;
        }

        ++size;

        return newElement;
    }

    // Method for inserting new element after a given element.
    public Element insertAfter(Element element, E data)
            throws IllegalArgumentException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        // Insert new element
        Element newElement = new Element(data);
        if (tail == element) {
            // Insert new tail
            element.next = newElement;
            tail = newElement;
        }
        else {
            // Insert into middle of list
            newElement.next = element.next;
            element.next = newElement;
        }

        ++size;

        return newElement;
    }

    public E removeHead() throws NoSuchElementException {
        // Check pre-conditions
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }

        // Remove the head
        Element oldHead = head;
        if (size == 1) {
            // Handle removal of the last element
            head = null;
            tail = null;
        }
        else {
            head = head.next;
        }

        --size;

        return oldHead.data;
    }

    // Note that there is no removeTail.  This cannot be implemented
    // efficiently because it would require O(n) to scan from head until
    // reaching the item _before_ tail.

    public E removeAfter(Element element)
            throws IllegalArgumentException, NoSuchElementException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }
        if (element == tail) {
            throw new IllegalArgumentException(
                    "Argument 'element' must have a non-null next element");
        }

        // Remove element
        Element elementToRemove = element.next;
        if (elementToRemove == tail) {
            // Remove the tail
            element.next = null;
            tail = element;
        }
        else {
            // Remove from middle of list
            element.next = elementToRemove.next;
        }

        --size;

        return elementToRemove.data;
    }

    // Method for checking if a singly linked list is equal to another singly
    // linked list.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SinglyLinkedList<?> that = (SinglyLinkedList<?>) o;

        if (this.size != that.size) return false;

        // Return whether all elements are the same
        SinglyLinkedList<?>.Element thisElem = this.getHead();
        SinglyLinkedList<?>.Element thatElem = that.getHead();
        while (thisElem != null && thatElem != null) {
            if (!thisElem.getData().equals(thatElem.getData())) {
                return false;
            }
            thisElem = thisElem.getNext();
            thatElem = thatElem.getNext();
        }

        return true;
    }
}

// Singly linked list example. Create two linked lists containing strings in 
// alphabetical order, then merge the two lists and sort into alphabetical order.

/**
  
 * Program output:
     List
     ----
     crocodile
     lizard
     snake
     turtle
     List
     ----
     finch
     owl
     parrot
     penguin
     List
     ----
     crocodile
     finch
     lizard
     owl
     parrot
     penguin
     snake
     turtle
 */

public class Main {
    public static void main(String[] args) {

        // Craete list of sorted reptiles.
        SinglyLinkedList<String> sortedReptiles =
                new SinglyLinkedList<String>();
        sortedReptiles.insertTail("crocodile");
        sortedReptiles.insertTail("lizard");
        sortedReptiles.insertTail("snake");
        sortedReptiles.insertTail("turtle");
        outputList(sortedReptiles);

        // Create list of sorted birds.
        SinglyLinkedList<String> sortedBirds =
                new SinglyLinkedList<String>();
        sortedBirds.insertTail("finch");
        sortedBirds.insertTail("owl");
        sortedBirds.insertTail("parrot");
        sortedBirds.insertTail("penguin");
        outputList(sortedBirds);

        // List of sorted reptiles and birds merged together
        SinglyLinkedList<String> sortedAllAnimals =
                mergeLists(sortedReptiles, sortedBirds);
        outputList(sortedAllAnimals);
    }

    // Method for printing the contents of a singly linked list.
    private static void outputList(SinglyLinkedList<String> list) {
        System.out.println("List");
        System.out.println("----");
        SinglyLinkedList<String>.Element elem = list.getHead();
        while (elem != null) {
            System.out.println("\t" + elem.getData());
            elem = elem.getNext();
        }
    }

    // Method for merging and sorting singly linked lists.
    private static SinglyLinkedList<String> mergeLists(
            SinglyLinkedList<String> sortedList1,
            SinglyLinkedList<String> sortedList2) {
        SinglyLinkedList<String> merged = new SinglyLinkedList<String>();

        // Add all elements from both lists to the merged list while
        // maintaining sort order
        SinglyLinkedList<String>.Element elem1 = sortedList1.getHead();
        SinglyLinkedList<String>.Element elem2 = sortedList2.getHead();
        while (elem1 != null || elem2 != null) {
            String dataToInsert;

            // Determine which element should be added next
            if (elem1 == null) {
                // List 1 exhausted, next element from list 2 will be added
                dataToInsert = elem2.getData();
                elem2 = elem2.getNext();
            } else if (elem2 == null) {
                // List 2 exhausted, next element from list 1 will be added
                dataToInsert = elem1.getData();
                elem1 = elem1.getNext();
            } else {
                // List 1 & 2 contain more elements, determine which is smaller
                if (elem1.getData().compareTo(elem2.getData()) < 0) {
                    // List 1 element smaller, it will be added
                    dataToInsert = elem1.getData();
                    elem1 = elem1.getNext();
                } else {
                    // List 2 element smaller, it will be added
                    dataToInsert = elem2.getData();
                    elem2 = elem2.getNext();
                }
            }

            // Add element to merged list
            merged.insertTail(dataToInsert);
        }

        return merged;
    }
}

//------------------------------------------------------------------------------
// Doubly Linked Lists
//------------------------------------------------------------------------------

// Differences between double and singly linked lists:
// - Each element links to next and previous elements.
// - The last elements next reference points to null (there is an end).
// - The first elements previous reference points to null (there is a start).
// - Can be traversed from head to tail or tail to head.
// - Each element takes up more memory because it has two references.

// Implementation.

public class DoublyLinkedList<E> {

    // Internal class used to represent one element in the list. 
    public class Element {

        private E data;
        private Element next;
        private Element previous;

        // Function for constructing an element.
        private Element(E data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }

        // Element.getData() returns the internal element data E.
        public E getData() {
            return data;
        }

        // Element.getNext() returns the element object referenced by Element.next.
        public Element getNext() {
            return next;
        }

        public Element getPrevious() {
            return previous;
        }

        // Element.getOwner() returns the whole DoublyLinkedList object in which
        // the specific element is contained.
        public DoublyLinkedList getOwner() {
            return DoublyLinkedList.this;
        }
    }

    // Initialize the doubly linked list itself, the linked list contains its
    // own metadata: head (an Element), tail (an Element), and size (an int).
    private Element head;
    private Element tail;
    private int size;

    // Getters for returns doubly linked list metadata.
    public Element getHead() {
        return head;
    }

    public Element getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Insert a new element to the head of the list given a user supplied data E.
    public Element insertHead(E data) {
        Element newElement = new Element(data);

        if (isEmpty()) {
            // Insert into empty list
            head = newElement;
            tail = newElement;
        }
        else {
            // Insert into non-empty list
            newElement.next = head;
            head.previous = newElement;
            head = newElement;
        }

        ++size;

        return newElement;
    }

    // Insert a new element into the tail of the list.
    public Element insertTail(E data) {
        Element newElement = new Element(data);

        if (isEmpty()) {
            // Insert into empty list
            head = newElement;
            tail = newElement;
        }
        else {
            // Insert into non-empty list
            newElement.previous = tail; // Point to the previous tail.
            tail.next = newElement;
            tail = newElement;
        }

        ++size;

        return newElement;
    }

    public Element insertBefore(Element element, E data)
            throws IllegalArgumentException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        // Insert new element
        Element newElement = new Element(data);
        if (head == element) {
            // Insert new head
            newElement.next = element;
            element.previous = newElement;
            head = newElement;
        }
        else {
            // Insert into middle of list
            newElement.next = element; // Point next to user specified element.
            newElement.previous = element.previous; // Point previous to the previous of the user specified element.
            element.previous.next = newElement; // Now point previous to new element.
            element.previous = newElement; 
        }

        ++size;

        return newElement;
    }

    public Element insertAfter(Element element, E data)
            throws IllegalArgumentException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        // Insert new element
        Element newElement = new Element(data);
        if (tail == element) {
            // Insert new tail
            newElement.previous = element;
            element.next = newElement;
            tail = newElement;
        }
        else {
            // Insert into middle of list
            newElement.next = element.next;
            newElement.previous = element;
            element.next.previous = newElement;
            element.next = newElement;
        }

        ++size;

        return newElement;
    }

    public E removeHead() throws NoSuchElementException {
        // Check pre-conditions
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }

        // Remove the head
        Element oldHead = head;
        if (size == 1) {
            // Handle removal of the last element
            head = null;
            tail = null;
        }
        else {
            head = head.next;
            head.previous = null;
            if (head.next != null) {
                head.next.previous = head;
            }
        }

        --size;

        return oldHead.data;
    }

    public E removeTail() throws NoSuchElementException {
        // Check pre-conditions
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }

        // Remove the tail
        Element oldTail = tail;
        if (size == 1) {
            // Handle removal of the last element
            head = null;
            tail = null;
        }
        else {
            tail = tail.previous;
            tail.next = null;
            if (tail.previous != null) {
                tail.previous.next = tail;
            }
        }

        --size;

        return oldTail.data;
    }

    public E remove(Element element)
            throws IllegalArgumentException, NoSuchElementException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        // Remove element
        if (size == 1) {
            // Remove the last element
            head = null;
        }
        else if (element == head) {
            // Remove the head
            element.next.previous = null;
            head = element.next;
        }
        else if (element == tail) {
            // Remove the tail
            element.previous.next = null;
            tail = element.previous;
        }
        else {
            // Remove from middle of list, repair references.
            element.previous.next = element.next;
            element.next.previous = element.previous;
        }

        --size;

        return element.data;
    }
}

//------------------------------------------------------------------------------
// Circular Lists
//------------------------------------------------------------------------------

// Differences with respect to single and doubly linked lists.
// - Last element points to the head of the list.
// - First element points to the tail of the list.
// - There is no end of the list so there is no tail element.
// - As new elements get added to the list, they become the head of the list.
// In this sense the head serves as your current location if you are iterating
// through the list in a loop-esque fashion.
// - Can be implemeneted as either a singly or doubly linked list.

// Implementation.

public class CircularDoublyLinkedList<E> {

    // Element class, same as a singly or doubly linked list.
    public class Element {

        private E data;
        private Element next;
        private Element previous;

        // Constructor for an element object.
        private Element(E data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }

        public E getData() {
            return data;
        }

        public Element getNext() {
            return next;
        }

        public Element getPrevious() {
            return previous;
        }

        public CircularDoublyLinkedList getOwner() {
            return CircularDoublyLinkedList.this;
        }
    }

    // Fields of the circular doubly linked list, note that there is not tail.
    private Element head;
    private int size;

    public Element getHead() {
        return head;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // User internal insert before method but by default specifies the head 
    // as the element that is getting inserted before.
    public Element insertHead(E data) {
        return insertBeforeImpl(head, data);
    }

    public Element insertBefore(Element element, E data)
            throws IllegalArgumentException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        return insertBeforeImpl(element, data);
    }

    public Element insertAfter(Element element, E data)
            throws IllegalArgumentException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        return insertBeforeImpl(element.next, data);
    }

    public E removeHead() throws NoSuchElementException {
        // Check pre-conditions
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }

        return removeImpl(head);
    }

    public E remove(Element element)
            throws IllegalArgumentException, NoSuchElementException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        return removeImpl(element);
    }

    // Function for inserting into the circular list.
    private Element insertBeforeImpl(Element element, E data) {

        // Insert new element
        Element newElement = new Element(data);
        if (element == null) {
            // Insert into empty list
            newElement.next = newElement; // Point back around to the head.
            newElement.previous = newElement;
            head = newElement;
        }
        else if (element == head) {
            // Insert new head
            newElement.next = element; // Patch pointers and set head equal to the new element.

            newElement.previous = element.previous;
            element.previous.next = newElement;
            element.previous = newElement;
            head = newElement;
        }
        else {
            newElement.next = element;
            newElement.previous = element.previous;
            element.previous.next = newElement;
            element.previous = newElement;
        }

        ++size;

        return newElement;
    }

    private E removeImpl(Element element) {
        // Remove element
        if (size == 1) {
            // Handle removal of last element
            head = null;
        }
        else if (element == head) {
            // Handle removal of head, patch pointers.
            element.previous.next = element.next;
            element.next.previous = element.previous;
            head = head.next;
        }
        else {
            element.previous.next = element.next;
            element.next.previous = element.previous;
        }

        --size;

        return element.data;
    }
}








