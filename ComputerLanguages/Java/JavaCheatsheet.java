
// --- Classes and Functions ---

// Init a class, all code must exist within a class and the filename must match
// the name of the class (in this case JavaCheatsheet.java).
public class JavaCheatsheet {}

// Default method template for running code line-by-line.
public static void main(String[] args) {}

// Function that takes in an array of integers and two additional integer arguments.
static int[] insert(int[] array, int index, int value) {}

public // public members are visible to all other classes
static // static allows the method to be called before an object of the class has been created
final // non-access modifier which makes the value non-changeable 
void // tells the compiler that the method does not return a value
String[] args // an array of string, "John Doe" passes the array ["John", "Doe"] to the function

@Override // Allow a subclass to implement a method defined in its superclass.

// --- Types and Data Structures ---

BinaryTree<Integer> tree1; // Declare and then init a binary tree of integers.
tree1 = new BinaryTree<Integer>();

int var1 = 4; // define an integer
float var1 = 10.2; // define a float
double var1 = 22.4652 // define a double

int[] array = new int[10]; // create a new array of integers of length 10

// Java Generic Type Parameters
FunctionName <T> {} // Type
FunctionName <E> {} // Element
FunctionName <K> {} // Key
FunctionName <N> {} // Number
FunctionName <V> {} // Value

// --- Loops and Control Flow ---

// Loop through an array.
for(int i = 0; i < intArray.length; i++) {}

// Loop through a list.
for (String item : list) {}
for (Skill skill : skills) {}

// --- Random ---

System.out.print("Integer is: " + var1); // print without line break
System.out.println("Hello, World!"); // print with line break

// Profiling
long startTime = System.nanoTime();
long stopTime = System.nanoTime();
double timePerInsert = (double)(stopTime - startTime) / nanoDivisor;
