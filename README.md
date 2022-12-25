# Computer Science - Math - Neural Networks 
<a href='https://github.com/nelson-n/cs-math-nn/blob/main/Preface/Assets/RepoLogo.png'><img src='/Preface/Assets/RepoLogo.png' align="right" height="160" /></a>

This curriculum is my personal approach to learning topics in computer science, mathematics, and neural networks from first principles. This repository is not necessarily a full curriculum, but rather a reflection of my interests, needs, and gaps in my knowledge. However, my goal is to build a curriculum that covers the whole compute stack from transistors and assembly to the math underpinning neural networks. As such, the lessons I compile below should form a solid computer science, math, and deep learning curriculum.

## Sections

[Hardware / Processors](#hardware--processors)

[Compilers / Operating Systems](#compilers--operating-systems)

[Data Structures / Algorithms](#data-structures--algorithms)

[Computer Languages](#computer-languages)

[Statistics](#statistics)

[Mathematics](#mathematics)

[Neural Networks](#neural-networks)

[Extra Curricular](#extra-curricular)
* [Networking / Internet](#networking--internet)
* [Parallel Computing](#parallel-computing)
* [Databases](#databases)
* [Hashing Functions / Cryptography](#hashing-functions--cryptography) 
* [Information Theory / Entropy](#information-theory--entropy)
* [Assorted](#assorted)

## Preface: Atoms to Bits 

`/Preface/atoms2bits.pdf`

The preface gives an overview of the complete compute stack: the refining of sand into silicon ingots, the doping of silicon to create differences in valence shell electrons, how doped silicon is used to build transistors, how transistors form logic gates, CPU architecture and operation, memory caches, instruction set architecture, operating systems, and higher level languages. This preface gives an overview of concepts that will be covered in detail in later sections.

#### :bangbang: to-do
* Add notes on FPGAs, how FPGAs are built from transistors, notes on how ICs are a collection of transistors in a reliable package, notes on LUTs (lookup tables).

## Hardware / Processors

#### :bangbang: to-do
* Implement AND, NAND, XOR, and other logic gates from scratch, use these logic gates to build functional units such as a 1-bit adder, multiplexer, sequential logic, SRAM, etc. Use logic gates to create simple implementations of various memory units: L1 cache, DRAM, etc. The functional units built in this section will be used later when building a model instruction set.
* Build a simple ARM7 CPU in Verilog or with another infrastructure.
    * Start by building a pipeline with simple start, decode, fetch, and exectute commands. Then build a simple register/memory unit to push and pull data, a simple ALU (arithmetic logic unit) that can perform basic arithmetic and logic operations, and a simple CU (control unit) for finding instructions and directing operations. These should be built on top of the functional units constructed in the hardware lesson. 
    * Additional: Write basic arithmetic instructions, branch instructions, and memory instructions. Allow for instruction out-of-order, basic parallelism, use dependency graphs for instructions. Set up a memory hierarchy with Registers, L1 cache, L2 cache, L3 cache, and DRAM.
* Extra-curricular: Build a UART in Verilog, GPU basics.

## Compilers / Operating Systems

#### :bangbang: to-do
* Create a set of general notes on operating systems (linux in particular). Add notes on the basic linux filesystem format from [here](https://www.linuxfoundation.org/blog/blog/classic-sysadmin-the-linux-filesystem-explained).
* Build a C compiler in Haskell. Consider the basics of compiler design, write a parser, output ARM assembly which can then be run through the simple processor designed in the processors lesson.
    * Build functions for converting binary to numbers and ASCII characters.
* Learn RISC-V architecture, contrast with x86, ARM, and other instruction set architectures.
* Build a UNIX-ish operating system in C or C++ with simple abilities like open, read, write, close, init, cat, ls, rm.
* Build a filesystem like FAT in C or C++.

## Data Structures / Algorithms

#### :bangbang: to-do
* Learn the low-level data structures behind the base data types in Python such as: list, array, tuple, et cetera. Build versions of these data types in C++ and then port to Python with a package like `pybind11`.
* Add a lesson on optimal algorithms.
* Add a lesson on the properties of different number systems: hexadecimal system, 32-bit numbers, 128-bit numbers, how with a 32-bit number system you can generate the numbers 0-65535, etc.

## Computer Languages

`/ComputerLanguages`

This section contains notes and exercises on both high level and low level programming languages. Includes notes from popular books such as [Fluent Python](https://github.com/hiddenJuliet/pythondocument/blob/master/Fluent%20Python.pdf) and [Advanced R](https://adv-r.hadley.nz/), cheatsheets with useful base commands, and scripts for benchmarking.

Languages currently covered:
* Python
* R

#### :bangbang: to-do
* Add notes on C, C++, potentially Go or Rust.
* Replicate lower level Python functions such as len(), dictionary, hash table, a sorting algorithm, slicing, indexing, set inclusion, et cetera in C++ to learn how these functions work. Then port these functions to Python with `pybind11`. Interface these functions with the data structures built from scratch in the data structures lesson.
* Overview of object-oriented-programming (OOP).

## Statistics

#### :bangbang: to-do
* Upload pre-existing statistics exercises.
* Do some statistics work in Julia.

## Mathematics

#### :bangbang: to-do
* Learn: differential equations, partial differential equations. Work on these problems in Julia or Python.
* Review multivariable calculus, gradient descent, formalize pre-existing notes and upload them.

## Neural Networks

#### :bangbang: to-do
* Build the following models from scratch in NumPy: Feedforward neural network -> recurrent neural network -> convolution neural network -> ResNet -> Transformer, all from scratch. 
    * Verify the correct order of the models so that all paradigmatic neural network models are built in-order.

## Extra Curricular

### Networking / Internet

Contains notes on computer networking and how the internet protocol stack works.

#### :bangbang: to-do
* Manually create and send TCP/IP packets: [tutorial](https://inc0x0.com/tcp-ip-packets-introduction/tcp-ip-packets-3-manually-create-and-send-raw-tcp-ip-packets/).

`/NetworkingInternet`
### Parallel Computing 

### Databases

### Hashing Functions / Cryptography

#### :bangbang: to-do
* Write a hashing function from scratch. Use the hashing function to build a hash table function. 
* Upload pre-existing crptography notes.

### Information Theory / Entropy

### Assorted

#### Git

#### :bangbang: to-do
* Add Git notes here, link to useful Git book.

## Acknowledgements

[Self-learning-Computer-Science - PKUFlyingPig](https://github.com/PKUFlyingPig/Self-learning-Computer-Science/blob/main/README.md)
Open-source CS curriculum that inspired the structure and content of this curriculum.

[From the Transistor to the Web Browser - George Hotz](https://github.com/geohot/fromthetransistor)
Proposed CS curriculum that inspired many of the low-level CS lessons on computer hardware, chips, and compilers that are in this curriculum.

### To-Do List
List 1, Porting in Pre-Existing Work:
* Add notes on UNIX that I already have.
* **Machine Learning / AI** Port in pre-existing work from scientia.
* **Statistics / Mathematics** Port in pre-existing work from scientia..

List 2, DSA + C++ + Low Level Python:
* **Higher Level Languages** Finish Fluent Python.
* **Data Structures** Learn data structures.
* **Algorithms** Learn algorithms.
* **Data Structures** Replicate low-level data structures behind base Python types such as list, array, tuple, dictionary, set. For example, build a low-level hash table function and then use this to build a dictionary type in Python (before being able to do this, a hashing function must be built from scratch). Build in C++ and then port to Python. Use `timeit` to compare the speed of lookup using the standard python dictionary/set type to lookup using my custom built data type.
* **Lower Level Languages** Learn C++ basics.
* **Lower Level Languages** Replicate lower level Python functions such as len() in C++, then port to Python and interface with low-level data structures that were already built from scratch.
* Build a dictionary method using C++ and an R dictionary interface. Then publish an R dictionary class to CRAN.

List 3, Neural Network Work:
* **Machine Learning / AI** Work on karpathy lectures, tinygrad tutorial, building paradigmatic models in order from scratch until Transformer model is understood.
