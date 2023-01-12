
//------------------------------------------------------------------------------
// Data Structures & Algorithms Fundamentals
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
// Overview of Data Structures
//------------------------------------------------------------------------------

/* 

Overview of Data Structures
- Basic Types
    - int, long, double, character, etc.
- Struct
    - Grouping of objects of different data types.
    - Stored contiguously in memory.
    - Cannot grow or shrink.
    - Used to group a set of known attributes.
    - Stored internally as bits arranged in a specific way.
- Arrays
    - Grouping of object of homogenous data type.
    - Stored contiguously in memory (each elements is stored immediately after
    the previous element).
    - Cannot grow or shrink.
    - Fast random access.
    - Slow inserts as inserting requires making a new array.
    - Slow deletes, also requires making a new array.
    - Good for problems that require many random reads.
- Lists
    - Stored non-contiguously in memory (items are stored randomly in memory)
    - Slow random access because data is stored non-contiguously.
    - Fast inserts and deletes because there is no need to shift all the elements
    after and before the place you are inserting and deleting from (another result
    of having non-contiguous representation in memory).
    - Good for problems that require lots of inserts and deletes.
- Graphs
    - General structure used in many applications.
- Classes / Fields
    - Classes themselves can be thought of as data structures that contain fields 
    which are also data.
    - With arrays it does not matter if it has 10 elements or 10m elements, accessing
    members of the array takes the same amount of time. However, inserts and deleters
    are very slow.

Objectives of Data Structures
- Efficiency
    - Algorithms should perform quickly for their intended task.
- Abstraction
    - Interfaces should be clearly and simply defined, complexity of how operations
    are actually carried out should be hidden under the surface.
- Reusability
    - The data structure should work regardless of the underlying data type.
    - Generic implementation allows one implementation to be used with multiple
    types.

*/

//------------------------------------------------------------------------------
// Overview of Algorithms
//------------------------------------------------------------------------------




