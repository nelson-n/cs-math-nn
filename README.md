
# self-taught-CS

This curriculum is my personal approach to learning computer science
from first principles, and it will be filled out over time as I self-learn new
concepts. This is not necessarily a full curriculum, but rather a reflection of
my interests, needs, and what I feel are gaps in my knowledge of computer science.
However, my goal is to build a curriculum that covers the whole compute stack
from transistors and assembly to high level languages, with each lesson building
off of the last lesson. As such, the sections below should form a solid computer
science curriculum.

### Sections

[Preface: Atoms to Bits](#preface:-atoms-to-bits)

[The Configuration of Hardware](#the-configuration-of-hardware)

[Processors](#processors)

## Preface: Atoms to Bits 

**/preface**

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
*   Start by building a pipeline with simple start, decode, fetch, and exectute commands. Then build a simple register/memory unit to push and pull data, a simple ALU (arithmetic logic unit) that can perform basic arithmetic and logic operations, and a simple CU (control unit) for finding instructions and directing operations. These should be built on top of the functional units constructed in the hardware lesson. 
*   Additional: Write basic arithmetic instructions, branch instructions, and memory instructions. Allow for instruction out-of-order, basic parallelism, dependency graphs for instructions. Set up memory hierarchy with Registers, L1 cache, L2 cache, L3 cache, DRAM.

* :heavy_multiplication_x: Extra-curricular: GPU basics.

### Section 3: Compilers

* Learn RISC-V architecture, contrast with x86, ARM, and other instruction set
architectures.

:x: Build a C compiler in Haskell. Consider the basics of compiler design. Write 
a parser, output ARM assembly which can then be run through the processor designed
in lesson 2. 

* Build functions for converting binary to numbers and ASCII characters.

### Section 4: Operating System

:x: Build a UNIX-ish operating system in C or C++ with simple abilities like
open, read, write, close, init, cat, ls, rm.

:x: Build a filesystem like FAT in C or C++.

### Section: Networking

### Section: Data Structures

:x: Learn the low-level data structures behind the base data types in Python such
as: list, array, tuple, et cetera. Build versions of these data types in C++ and
then port to Python with a package such as `pybind11`.

### Section: Algorithms

### Section: Lower Level Languages

:x: C, C++, more. 

:x: Replicate lower level Python functions such as len(), dictionary, hash table, 
sorting algorithm, slicing, indexing, set inclusion, et cetera in C++ to learn 
how these functions work. Then port these functions to Python with `pybind11`.

### Section: Higher Level Languages

:x: Python, R, more.
* Fluent Python - Luciano Ramalho
* Advanced R - Hadley Wickham

:x: Overview of OOP.

### Section: Parallel Computing 

### Section: Databases.

### Section: The Internet

### Section: Machine Learning / AI

Port in work from scientia.

Go through Karpathy lectures. Build ResNet from scratch, build Transformer from
scratch (verify the correct order of models feedforward -> X -> Y) so that all
paradigmatic models are built from scratch.

### Extra Curricular Study

#### Hashing Functions
:x: Write a hashing function from scratch. Use the hashing function to build a
hash table function. 

#### Statistics

Port in work from scientia.

Do some statistical work in Julia.

#### Mathematics

Port in work from scientia.

Learn differential equations.

#### Information Theory and Entropy

#### Git
* Add Git notes here, link to useful Git book.

### Acknowledgements / Inspiration

[Self-learning-Computer-Science - PKUFlyingPig](https://github.com/PKUFlyingPig/Self-learning-Computer-Science/blob/main/README.md)

[From the Transistor to the Web Browser - George Hotz](https://github.com/geohot/fromthetransistor)

