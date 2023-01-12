
//------------------------------------------------------------------------------
// Tenets of Java
//------------------------------------------------------------------------------

// The key that allows Java to solve both security and portability problems 
// is that the output of a Java compiler is not executable code, but rather
// it is bytecode.

// Bytecode is a highly optimized set of instructions designed to be executed
// by the Java Virtual Machine (JVM). The JVM is an interpreter for bytecode.

// Because the execution of every Java program is under the control of the JVM, 
// the JVM contains the program and prevents it from generating any side effects
// outside of the system.

//------------------------------------------------------------------------------
// Basics of OOP
//------------------------------------------------------------------------------

// Encapsulation

// Encapsulation is a progamming mechanism that binds code and the data that it
// manipulates together, keeping both safe from outside interference. An object
// contains both code and data, and it the key to encapsulation.

// Java's basic unit of encapsulation is the class. A class defines the form of
// an object, it specifies both the data and code that will operate on the data.
// Objects are instances of a class, thus a class is essentially a set of plans
// that specify how to build an object. 

// The data defined by the class are referred to as member variables. The code
// that operates on the data is referred to as member methods or just methods.
// Method is a term for a subroutine. 

// Polymorphism

// "One interface, multiple methods." This means that it is possible to design
// a generic interface to a group of related activities. For example, you might
// have a program that requires three different stacks, one for integer values,
// one for floating point values, and one for characters. In a polymorphism 
// approach, the algorithm that implements each stack is the same, even though
// the underlying data is different. It is the compiler's job to select the 
// specific action (method) as it applies to each situation.


// Inheritance 

// Inheritance is the process by which one object can acquire the properties of
// another object. Without inheritance, each new object would have to explicitly
// define all of its characteristics. Using inheritance, an object only needs to 
// define the qualities that make it unique, and it can inherit all other qualities
// from classes above it. I.e. a square is a rectangle with some additional 
// properties.

//------------------------------------------------------------------------------
// Compiling and Running Programs
//------------------------------------------------------------------------------

// Simple hello world program.

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!"); 
    }
}

// Note that in order to compile the above class, the class would need to be
// written in a file titled: Main.java

// The file can then be compiled with the command: javac Main.java
// And then run with the command: java Main

// When Java source code is compiled, each individual class is put into its 
// own output file named after the name of the compiled .class file.

//------------------------------------------------------------------------------
// Basic Java Program Structure
//------------------------------------------------------------------------------

// All programs begin with a class assertion:
class Main {}

// The class is Java's basic unit of encapsulation. The elements between the 
// two {} of the class are the members of the class.

// This line begins the "main" method. All Java applications begin execution by
// calling main()
public static void main(String[] args) {}

// The public keyword is an access specifier, the specifier identifies how other
// parts of the program can access members of the class. When a class member is
// preceded by public, then that member can be accessed by code outside of the 
// class in which it is declared.

// main() must be declared as public since it must be called by code outside of
// its class when the program starts.

// The keyword static allows main() to be called before an object of the class
// has been created.

// The keyword void tells the compiler that main() does not return a value.

// System is a pre-defined class that provides access to the system and System.out
// is an object that encapsulates console output.
System.out.println("Hello, World!"); 

// All statements in Java end with ; and lines that do not end with ; are 
// technically not statements.

//------------------------------------------------------------------------------
// Assigning Variables
//------------------------------------------------------------------------------

class Example2 {
    public static void main(String args[]) {
        int var1; // this declares a variable
        int var2; // this declares another variable
        var1 = 1024; // this assigns 1024 to var1
        System.out.println("var1 contains " + var1);
        var2 = var1 / 2;
        System.out.print("var2 contains var1 / 2: ");
        System.out.println(var2);
    }
}

// In Java, all variables must be declared before they and typed before they
// are used.

// Java defines two floating-point types: float and double.
// These represent single and double precision numbers respectively, with double
// being the most commonly used. For single precision 32 bits are used to represent
// the number, for double precision 64 bits are used.

class Example3 {
    public static void main(String args[]) {
        float var1;
        double var2;

        var1 = 10.2;
        var2 = 22.4652;
    }
}

// Simple program converting gallons to liters.
class GalToLit {
    public static void main(String args[]) {
        double gallons; // holds the number of gallons
        double liters; // holds conversion to liters
        gallons = 10; // start with 10 gallons
        liters = gallons * 3.7854; // convert to liters
        System.out.println(gallons + " gallons is " + liters + " liters.");
    }
}

//------------------------------------------------------------------------------
// Control Statements
//------------------------------------------------------------------------------

// Java operators:
// <, >, <=, >=, ==, !=

// Sample if statement:
if(a == b) System.out.println("you won't see this");

// For loop form:
// for(initialization; condition; iteration) statement;

// The following for loop iterates five times:
int count;
for(count = 0; count < 5; count = count + 1)
    System.out.println("This is count: " + count);

// In Java you will seldom see statements like count = count + 1

// This can be replaced with count++
for(count = 0, count < 5, count++)

// The negative analog of this operations is count--


// Left off on page 54.



 
