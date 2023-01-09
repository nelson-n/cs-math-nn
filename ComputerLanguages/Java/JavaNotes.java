
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






