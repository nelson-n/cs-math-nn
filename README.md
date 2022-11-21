
# self-taught-CS

This curriculum is my personal approach to learning computer science
from first principles, and it will be filled out over time as I self-learn new
concepts. This is not necessarily a full curriculum, but rather a reflection of
my interests, needs, and what I feel are gaps in my knowledge of computer science.
However, my goal is to build a curriculum that covers the whole compute stack
from transistors and assembly to high level languages, with each lesson building
off of the last lesson. As such, the sections below should form a solid computer
science curriculum.

### To-Do List
List 1, Porting in Pre-Existing Work:
* **Acknowledgements** Explain what each item in the awknowledgments section is and how it helped.
* **Higher Level Languages** Review and upload Python cheatsheets.
* **The Internet** Port in pre-existing notes on the internet.
* **Machine Learning / AI** Port in pre-existing work from scientia.
* **Statistics / Mathematics** Port in pre-existing work from scientia.

List 2, DSA + C++ + Low Level Python:
* **Higher Level Languages** Finish Fluent Python.
* **Data Structures** Learn data structures.
* **Algorithms** Learn algorithms.
* **Data Structures** Replicate low-level data structures behind base Python types such as list, array, tuple, dictionary, set. For example, build a low-level hash table function and then use this to build a dictionary type in Python (before being able to do this, a hashing function must be built from scratch). Build in C++ and then port to Python. Use `timeit` to compare the speed of lookup using the standard python dictionary/set type to lookup using my custom built data type.
* **Lower Level Languages** Learn C++ basics.
* **Lower Level Languages** Replicate lower level Python functions such as len() in C++, then port to Python and interface with low-level data structures that were already built from scratch.

List 3, Neural Network Work:
* **Machine Learning / AI** Work on karpathy lectures, tinygrad tutorial, building paradigmatic models in order from scratch until Transformer model is understood.

### Sections

[Preface: Atoms to Bits](#preface-atoms-to-bits)

[The Configuration of Hardware](#the-configuration-of-hardware)

[Processors](#processors)

[Compilers](#compilers)

[Operating Systems](#operating-systems)

[Networking](#networking)

[Data Structures](#data-structures)

[Algorithms](#algorithms)

[Lower Level Languages](#lower-level-languages)

[Higher Level Languages](#higher-level-languages)

[Parallel Computing](#parallel-computing)

[Databases](#databases)

[The Internet](#the-internet)

[Machine Learning / AI](#machine-learning--ai)

[Extra Curricular Study](#extra-curricular-study)
* [Hashing Functions / Cryptography](#hashing-functions--cryptography) 
* [Statistics](#statistics)
* [Mathematics](#mathematics)
* [Information Theory and Entropy](#information-theory-and-entropy)
* [Git](#git)

[Acknowledgments / Inspiration](#acknowledgements--inspiration)

## Preface: Atoms to Bits 

**/Preface**

The preface givens an overview of the complete compute stack: the refining of
sand into silicon ingots, the doping of silicon to create differences in valence shell electrons, how doped silicon is used to build a transistor, how transistors form logic gates, CPU architecture and operation, memory caches, instruction set architecture, operating systems, and higher level languages. This gives an overview of concepts that will be covered in detail in later sections, and also builds knowledge about concepts that cannot be covered in this curriculum such as building a transistor or building a solid-state-drive (SSD). 

**atoms2bits.pdf** 
* :heavy_check_mark: Notes covering: 1 Silicon, 2 Transistors, 3 CPU Production, 4 Logic Gate, 5 CPU Architecture and Operation, 6 GPU Architecture, 7 CPU Components, 8 Binary Code, 9 Instruction Set Architecture, 10 Memory and Motherboard, 11 BIOS, 12 Operating System, 13 Higher Level Languages.
* :heavy_multiplication_x: Add notes on FPGAs, how FPGAs are built from transistors, notes on how ICs are a collection of transistors in a reliable package, notes on LUTs (lookup tables).

## The Configuration of Hardware

* :heavy_multiplication_x: Implement AND, NAND, other logic gates from scratch, use these logic gates to build functional units such as a 1-bit adder, multiplexer, sequential logic, SRAM, etc. Use logic gates to create simple implementations of various memory units: L1 cache, DRAM, etc. The functional units built in this section will be later when building a toy process and instruction set.

* :heavy_multiplication_x: Additional: Learn Verilog, build a UART.

## Processors

* :heavy_multiplication_x: Build a simple ARM7 CPU in Verilog or with another infrastructure.
    * Start by building a pipeline with simple start, decode, fetch, and exectute commands. Then build a simple register/memory unit to push and pull data, a simple ALU (arithmetic logic unit) that can perform basic arithmetic and logic operations, and a simple CU (control unit) for finding instructions and directing operations. These should be built on top of the functional units constructed in the hardware lesson. 
    * Additional: Write basic arithmetic instructions, branch instructions, and memory instructions. Allow for instruction out-of-order, basic parallelism, dependency graphs for instructions. Set up memory hierarchy with Registers, L1 cache, L2 cache, L3 cache, DRAM.

* :heavy_multiplication_x: Extra-curricular: GPU basics.

## Compilers

* :heavy_multiplication_x: Build a C compiler in Haskell. Consider the basics of compiler design, write a parser, output ARM assembly which can then be run through the simple processor designed in the processors lesson.
    * Build functions for converting binary to numbers and ASCII characters.

* :heavy_multiplication_x: Learn RISC-V architecture, contrast with x86, ARM, and other instruction set architectures.

## Operating Systems

* :heavy_multiplication_x: Build a UNIX-ish operating system in C or C++ with simple abilities like open, read, write, close, init, cat, ls, rm.

* :heavy_multiplication_x: Build a filesystem like FAT in C or C++.

## Networking

## Data Structures

* :heavy_multiplication_x: Learn the low-level data structures behind the base data types in Python such as: list, array, tuple, et cetera. Build versions of these data types in C++ and then port to Python with a package such as `pybind11`.

## Algorithms

## Lower Level Languages

* :heavy_multiplication_x: Notes on C, C++, potentially Go or Rust.

* :heavy_multiplication_x: Replicate lower level Python functions such as len(), dictionary, hash table, sorting algorithm, slicing, indexing, set inclusion, et cetera in C++ to learn how these functions work. Then port these functions to Python with `pybind11`. Interface these functions with the data structures built from scratch in the data structures lesson.

## Higher Level Languages

* :heavy_multiplication_x: Notes on Julia.

* :heavy_multiplication_x: Overview of object-oriented-programming (OOP).

**/HigherLevelLanguages**

This section contains notes and exercises for learning multiple high level programming languages. This knowledge is derived mainly from a few excellent books.

**/Python/FluentPython.py** 
* :heavy_check_mark: Notes on the book *Fluent Python* by Luciano Ramalho. This 700 page book provides an in-depth understanding of Python 3 that covers all of the fundamental Python data types, the methods and OOP system that underpins Python, and the programming tradeoffs present in the Python language. These notes follow the text of the book closely. A pdf copy of *Fluent Python* may be found [here](https://github.com/hiddenJuliet/pythondocument/blob/master/Fluent%20Python.pdf).

**/R/AdvancedR.R**
* :heavy_check_mark: Notes on the book *Advanced R* by Hadley Wickham. This book focuses on advanced topics in the R language as well as how the R language works under the surface. Topics covered in the book include memory use, functional programming, the multiple OOP systems in R, and metaprogramming. After reading this book, one will have an advanced understanding of how code actually executes in R with knowledge of method dispatch, memory allocation, multithreading, and when the R language calls code written in C. The full book may be found [here](https://adv-r.hadley.nz/). 

## Parallel Computing 

## Databases

## The Internet

## Machine Learning / AI

* :heavy_multiplication_x: Feedforward neural network -> recurrent neural network -> convolution neural network -> ResNet -> Transformer, all from scratch. 
    * Verify the correct order of the models so that all paradigmatic neural network models are built in-order.

## Extra Curricular Study

### Hashing Functions / Cryptography

* :heavy_multiplication_x: Write a hashing function from scratch. Use the hashing function to build a hash table function. 

### Statistics

* :heavy_multiplication_x: Do some statistics work in Julia.

### Mathematics

* :heavy_multiplication_x: Learn: differential equations, partial differential equations.
    * Work on math courses in Julia.

* :heavy_multiplication_x: Review multivariable calculus, gradient descent, formalize pre-existing notes and upload them.

### Information Theory and Entropy

### Git
* Add Git notes here, link to useful Git book.

## Acknowledgements / Inspiration

[Self-learning-Computer-Science - PKUFlyingPig](https://github.com/PKUFlyingPig/Self-learning-Computer-Science/blob/main/README.md)

[From the Transistor to the Web Browser - George Hotz](https://github.com/geohot/fromthetransistor)

