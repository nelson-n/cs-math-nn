
// Stacks, Queues, and Sets

//------------------------------------------------------------------------------
// Stacks
//------------------------------------------------------------------------------

/*
- Stack = retrieve data in the opposite of the way it was stored.
- An example is the call stack, where the last function called is the first to
  return (the lowest level operation needs to conclude first).
- LIFO (last in first out), the last function called is the first out of the stack.
- Metaphor: a stack of plates, the last plate put on the stack is the first one
  taken off.
- A stack only has three operations:
    - Push = add to the top of the stack.
    - Pop = remove from the top of the stack.
    - Peek = look at the top of the stack.
- There are no operations to retrieve from the middle of the stack, insert into
the stack, etc.

- The call stack in a running program is implemented as a stack.
    - Each element on the stack is an activation record that holds all the information
    about the function call.
    - Activation records hold information about the function call, such as:
        - The function's name.
        - The function's parameters.
        - The function's local variables.
        - The return address.
        - The stack pointer.

*/

// Stack Implementation

// import java.util.NoSuchElementException;

public class Stack<E> {

    // Singly Linked List used to hold the contents of the stack.
    private SinglyLinkedList<E> list = new SinglyLinkedList<E>();

    // Just inserting into the list, inserts into lists are O(1) so this is O(1).
    public void push(E data) {
        list.insertHead(data);
    }

    public E pop() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return list.removeHead();
    }

    public E peek() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return list.getHead().getData();
    }

    public int getSize() {
        return list.getSize();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

//------------------------------------------------------------------------------
// Queues
//------------------------------------------------------------------------------

/*
- Allows you to enter elements and remove them in the same order that they were
  entered.
- Metaphor: a line of people waiting to get on a ride. The first person in line
  is the first person to get on the ride. The last person in line is the last 
  person to get on the ride.
    - FIFO: first in first out.
- Supports three operations:
    - Enqueue = add to the end of the queue.
    - Dequeue = remove from the front of the queue.
    - Peek = look at the front of the queue.

- In an OS, queues are used for event handling. As a series of events should as
mouse movements or keyboard clicks occurs, they are added to the queue and then
processed in the order they were received.
- Also used in semaphores: a semaphore is a variable that is used to control access
to a shared resource. It is used to ensure that only one process is accessing a
shared resource at a time. 

*/

// import java.util.NoSuchElementException;

public class Queue<E> {
    private SinglyLinkedList<E> list = new SinglyLinkedList<E>();

    public void enqueue(E data) {
        list.insertTail(data);
    }

    public E dequeue() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return list.removeHead();
    }

    public E peek() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return list.getHead().getData();
    }

    public int getSize() {
        return list.getSize();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}

//------------------------------------------------------------------------------
// Sets
//------------------------------------------------------------------------------

/*
- Unordered collection of like items.
- Duplicates not allowed.
- Example: {1, 2, 4, 6}
- Important part of discrete mathematics, an area relevant to computing.
- Used in computing to group data, allows for set correlation.

- Sets can be used for the following:
    - Data correlation: intersection, difference, union, etc.
    - Set covering optimazation.
    - Problems of combinatorics and resource selection.
    - Graphs.
    - Relational algebra (database queries).

- Supported operations:
    - Equality = two sets are equal if they contain the same elements.
    - Subset = a set is a subset of another set if all the elements of the first
      set are also in the second set.
    - Union = the union of two sets is a set that contains all the elements of
      both sets.
    - Intersection = the intersection of two sets is a set that contains only
    the elements that are in both sets.
    - Difference = the difference of two sets is a set that contains only the
    elements that are in the first set but not the second set.

 */

 // Implementation:

// import java.util.Iterator;
// import java.util.NoSuchElementException;

public class Set<E> implements Iterable<E> {
    private SinglyLinkedList<E> list = new SinglyLinkedList<E>();

    public int getSize() {
        return list.getSize();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void insert(E data) throws
            IllegalArgumentException,
            DuplicateElementException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        if (isMember(data)) {
            throw new DuplicateElementException();
        }

        // Insert data into the list
        list.insertTail(data);
    }

    public E remove(E data) throws
            IllegalArgumentException,
            NoSuchElementException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        // If empty list
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // Look for match at head
        if (list.getHead().getData().equals(data)) {
            return list.removeHead();
        }

        // Look for match after head
        SinglyLinkedList<E>.Element prevElem = list.getHead();
        while (prevElem.getNext() != null) {
            if (prevElem.getNext().getData().equals(data)) {
                return list.removeAfter(prevElem);
            }
            prevElem = prevElem.getNext();
        }

        throw new NoSuchElementException();
    }

    public boolean isMember(E data) throws
            IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        // Loop over all elems, return true if data found
        SinglyLinkedList<E>.Element elem = list.getHead();
        while (elem != null) {
            if (elem.getData().equals(data)) {
                return true;
            }
            elem = elem.getNext();
        }

        return false;
    }

    public E getMember(E data) throws
            IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        // Loop over all elems, return matching data
        SinglyLinkedList<E>.Element elem = list.getHead();
        while (elem != null) {
            if (elem.getData().equals(data)) {
                return elem.getData();
            }
            elem = elem.getNext();
        }

        throw new IllegalArgumentException("member does not exist");
    }

    public Set<E> union(Set<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        Set<E> result = new Set<E>();

        // Add all items from this
        SinglyLinkedList<E>.Element elem = list.getHead();
        while (elem != null) {
            result.insert(elem.getData());
            elem = elem.getNext();
        }

        // Add all items from other
        elem = other.list.getHead();
        while (elem != null) {
            if (!result.isMember(elem.getData())) {
                result.insert(elem.getData());
            }
            elem = elem.getNext();
        }

        return result;
    }

    public Set<E> intersection(Set<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        Set<E> result = new Set<E>();

        // For each element in this
        SinglyLinkedList<E>.Element elem = list.getHead();
        while (elem != null) {
            // If element also in other
            if (other.isMember(elem.getData())) {
                result.insert(elem.getData());
            }
            elem = elem.getNext();
        }

        return result;
    }

    public Set<E> difference(Set<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        Set<E> result = new Set<E>();

        // For each element in this
        SinglyLinkedList<E>.Element elem = list.getHead();
        while (elem != null) {
            // If element not in other
            if (!other.isMember(elem.getData())) {
                result.insert(elem.getData());
            }
            elem = elem.getNext();
        }

        return result;
    }

    public boolean isSubset(Set<E> other) throws
            IllegalArgumentException {
        if (other == null) {
            throw new IllegalArgumentException("data must not be null");
        }

        // If this has more elements than other, this can't be a subset
        if (getSize() > other.getSize()) {
            return false;
        }

        // If any member in this is not in other, this is not a subset
        SinglyLinkedList<E>.Element elem = list.getHead();
        while (elem != null) {
            // If element not in other
            if (!other.isMember(elem.getData())) {
                return false;
            }
            elem = elem.getNext();
        }

        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Set))
            return false;

        Set<E> other = (Set<E>)o;

        // If sets equal size and one is subset of other, then they're equal
        return getSize() == other.getSize()
                && isSubset(other);
    }

    private class SetIterator implements Iterator<E> {
        private SinglyLinkedList<E>.Element elem;

        public SetIterator() {
            elem = Set.this.list.getHead();
        }

        public boolean hasNext() {
            return elem != null;
        }

        public E next() {
            if (hasNext()) {
                E data = elem.getData();
                elem = elem.getNext();
                return data;
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public Iterator<E> iterator() {
        return new SetIterator();
    }
}

//------------------------------------------------------------------------------
// Set Usage
//------------------------------------------------------------------------------

// Set covering algorithm.
// - Optimization problem that models many problems in combinatorics and resource
//   allocation.
// - Given a set S and a set P of subsets A1, A2, ..., An of S (called set C), which
// is composed of one or more sets, P is said to cover S if every member of S is
// containted in at least one of the subsets C; in addition C contains as few sets
// from P as possible.
// - S contains attributes that must be covered.
// - P contains subsets of S that can be used to cover S.

// In the set covering algorithm, first select the subset S that covers the most
// nodes, then remove all nodes covered by S from the set of nodes to be covered,
// and repeat until all nodes are covered.
// - This is a gready algorithm, because it always selects the best option at the
// moment, without considering the future. Does not do any look-ahead.

// It is possible to implement a non-greedy, more optimal algorithm, but it is
// much more complex and has a much higher performance cost.

// Set covering example:
// Suppose a mission to Mars is being planned that requires a certain set of skills.
// Different astronauts have different skills, and the mission requires that
// all skills are covered.

// Class representing skills an astronaut can possess.
public class Skill {
    private String name;

    public Skill(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Skill))
            return false;

        Skill other = (Skill)o;

        return !(name != null ? !name.equals(other.name) : other.name != null);
    }
}

// Class representing an astronaut.
public class Astronaut {
    private String name;

    private Set<Skill> skills = new Set<Skill>();

    public Astronaut(String name, Skill... skills)
            throws DuplicateElementException {
        this.name = name;
        for (Skill skill : skills) {
            this.skills.insert(skill);
        }
    }

    public String getName() {
        return name;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Astronaut))
            return false;

        Astronaut other = (Astronaut)o;

        return !(name != null ? !name.equals(other.name) : other.name != null);
    }
}

public class CannotCoverException extends Exception {
    public CannotCoverException() {
    }
}

// Class solving the set covering problem. 
public class MissionToMars {
    public static void main(String[] args) {

        // Skills that must be covered
        System.out.println(
                "Skills that must be covered for a successful mission:");
        Set<Skill> skills = loadSkills();
        for (Skill skill : skills) {
            System.out.println("\t" + skill.getName());
        }
        System.out.println();

        // Astronauts from which to select
        System.out.println("Astronauts to select from:");
        Set<Astronaut> astronauts = loadAstronauts();
        for (Astronaut astronaut : astronauts) {
            System.out.println("\t" + astronaut.getName());
            for (Skill skill : astronaut.getSkills()) {
                System.out.println("\t\t" + skill.getName());
            }
        }
        System.out.println();

        // Determine the best set of astronauts to cover the skills
        System.out.println("-- Planning mission --");
        try {
            Set<Astronaut> missionAstronauts =
                    planMission(skills, astronauts);
            System.out.println("Astronauts selected for mission:");
            for (Astronaut astronaut : missionAstronauts) {
                System.out.println("\t" + astronaut.getName());
            }
        } catch (CannotCoverException ex) {
            System.out.println(
                    "Astronauts could not cover all required skills");
        }
    }

    private static Set<Skill> loadSkills() {
        Set<Skill> skills = new Set<Skill>();

        try {
            skills.insert(new Skill("cheerful"));
            skills.insert(new Skill("cook"));
            skills.insert(new Skill("engineer"));
            skills.insert(new Skill("fearless"));
            skills.insert(new Skill("leader"));
            skills.insert(new Skill("mechanic"));
            skills.insert(new Skill("navigator"));
            skills.insert(new Skill("physicist"));
            skills.insert(new Skill("pilot"));
            skills.insert(new Skill("strong"));
        } catch (DuplicateElementException ex) {
            assert false : "Should never happen because we're "
                    + "guaranteed to not be inserting duplicates";
        }

        return skills;
    }

    private static Set<Astronaut> loadAstronauts() {
        Set<Astronaut> astronauts = new Set<Astronaut>();

        try {
            astronauts.insert(new Astronaut("Anna Fisher",
                    new Skill("cheerful"),
                    new Skill("navigator")));
            astronauts.insert(new Astronaut("Boris Yegerov",
                    new Skill("cook"),
                    new Skill("mechanic")));
            astronauts.insert(new Astronaut("David Wolf",
                    new Skill("engineer"),
                    new Skill("strong"),
                    new Skill("cheerful")));
            astronauts.insert(new Astronaut("Gene Cernan",
                    new Skill("leader")));
            astronauts.insert(new Astronaut("Joseph Acaba",
                    new Skill("engineer"),
                    new Skill("physicist")));
            astronauts.insert(new Astronaut("Lisa Sterling",
                    new Skill("pilot"),
                    new Skill("navigator"),
                    new Skill("cook"),
                    new Skill("leader")));
            astronauts.insert(new Astronaut("Quan Chen",
                    new Skill("pilot"),
                    new Skill("mechanic"),
                    new Skill("cook"),
                    new Skill("fearless")));
            astronauts.insert(new Astronaut("Stephen Frick",
                    new Skill("navigator"),
                    new Skill("cook")));
            astronauts.insert(new Astronaut("Stephanie Wilson",
                    new Skill("cheerful"),
                    new Skill("cook"),
                    new Skill("strong")));
        } catch (DuplicateElementException ex) {
            assert false : "Should never happen because we're "
                    + "guaranteed to not be inserting duplicates";
        }

        return astronauts;
    }

    private static Set<Astronaut> planMission(
            Set<Skill> skills,
            Set<Astronaut> astronauts)
            throws CannotCoverException {
        Set<Astronaut> coveringAstronauts = new Set<Astronaut>();

        // As long as skills remain to be covered and astronauts are available
        while (!skills.isEmpty() && !astronauts.isEmpty()) {
            // Get the astronaut covering the most remaining skills
            Astronaut astronaut = findBestAstronaut(skills, astronauts);

            // If no astronaut was found, total cover is not possible
            if (astronaut == null) {
                throw new CannotCoverException();
            }

            // Add astronaut to set of covering astronauts
            try {
                coveringAstronauts.insert(astronaut);
            } catch (DuplicateElementException ex) {
                assert false : "Should never happen because we're "
                        + "guaranteed to not be inserting duplicates";
            }

            // Remove astronaut from set of remaining available astronauts
            astronauts.remove(astronaut);

            // Remove skills covered by the astronaut from the skills
            // that remain to be covered
            for (Skill skill : astronaut.getSkills().intersection(skills)) {
                skills.remove(skill);
            }
        }

        // If skills remain to be covered we got here because we ran out of
        // astronauts
        if (!skills.isEmpty()) {
            throw new CannotCoverException();
        }

        return coveringAstronauts;
    }

    private static Astronaut findBestAstronaut(
            Set<Skill> skills,
            Set<Astronaut> astronauts) {
        Astronaut bestAstronaut = null;

        // Loop over all astronauts selecting one that covers the most skills
        int bestCovered = 0;
        for (Astronaut astronaut : astronauts) {
            Set<Skill> skillsCovered =
                    skills.intersection(astronaut.getSkills());
            if (skillsCovered.getSize() > bestCovered) {
                bestCovered = skillsCovered.getSize();
                bestAstronaut = astronaut;
            }
        }

        // Will be null if no astronaut covered any of the remaining skills
        return bestAstronaut;
    }
}





