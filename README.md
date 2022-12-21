
# computer science - math - neural networks

This curriculum is my personal approach to learning topics in computer science, mathematics, and neural networks from first principles. This repository is not necessarily a full curriculum, but rather a reflection of my interests, needs, and gaps in my knowledge. However, my goal is to build a curriculum that covers the whole compute stack from transistors and assembly to the math underpinning neural networks. As such, the lessons I compile below should form a solid computer science, math, and deep learning curriculum.

### Sections

[Hardware / Processors](#hardware-processors)

[Compilers / Operating Systems](#compilers-operating-systems)

[Data Structures / Algorithms](#data-structures-algorithms)

[Computer Languages](#computer-languages)

[Statistics](#statistics)

[Mathematics](#mathematics)

[Neural Networks](#neural-networks)

[Extra Curricular](#extra-curricular-study)
* [Networking / The Internet](#networking-internet)
* [Parallel Computing](#parallel-computing)
* [Databases](#databases)
* [Hashing Functions / Cryptography](#hashing-functions-cryptography) 
* [Information Theory / Entropy](#information-theory-entropy)
* [Assorted](#assorted)

## Preface: Atoms to Bits 

`/Preface/atoms2bits.pdf`

The preface gives an overview of the complete compute stack: the refining of sand into silicon ingots, the doping of silicon to create differences in valence shell electrons, how doped silicon is used to build a transistor, how transistors form logic gates, CPU architecture and operation, memory caches, instruction set architecture, operating systems, and higher level languages. This gives an overview of concepts that will be covered in detail in later sections.

### ToDo :heavy_multiplication_x:
* Add notes on FPGAs, how FPGAs are built from transistors, notes on how ICs are a collection of transistors in a reliable package, notes on LUTs (lookup tables).

## Hardware / Processors
* :heavy_multiplication_x: Implement AND, NAND, other logic gates from scratch, use these logic gates to build functional units such as a 1-bit adder, multiplexer, sequential logic, SRAM, etc. Use logic gates to create simple implementations of various memory units: L1 cache, DRAM, etc. The functional units built in this section will be later when building a toy process and instruction set.

* :heavy_multiplication_x: Additional: Learn Verilog, build a UART.

* :heavy_multiplication_x: Build a simple ARM7 CPU in Verilog or with another infrastructure.
    * Start by building a pipeline with simple start, decode, fetch, and exectute commands. Then build a simple register/memory unit to push and pull data, a simple ALU (arithmetic logic unit) that can perform basic arithmetic and logic operations, and a simple CU (control unit) for finding instructions and directing operations. These should be built on top of the functional units constructed in the hardware lesson. 
    * Additional: Write basic arithmetic instructions, branch instructions, and memory instructions. Allow for instruction out-of-order, basic parallelism, dependency graphs for instructions. Set up memory hierarchy with Registers, L1 cache, L2 cache, L3 cache, DRAM.

* :heavy_multiplication_x: Extra-curricular: GPU basics.

## Compilers / Operating Systems

* :heavy_multiplication_x: Build a C compiler in Haskell. Consider the basics of compiler design, write a parser, output ARM assembly which can then be run through the simple processor designed in the processors lesson.
    * Build functions for converting binary to numbers and ASCII characters.

* :heavy_multiplication_x: Learn RISC-V architecture, contrast with x86, ARM, and other instruction set architectures.

* :heavy_multiplication_x: Build a UNIX-ish operating system in C or C++ with simple abilities like open, read, write, close, init, cat, ls, rm.

* :heavy_multiplication_x: Build a filesystem like FAT in C or C++.

## Data Structures / Algorithms

* :heavy_multiplication_x: Learn the low-level data structures behind the base data types in Python such as: list, array, tuple, et cetera. Build versions of these data types in C++ and then port to Python with a package such as `pybind11`.

## Computer Languages

`/ComputerLanguage`
This section contains notes and exercises on both high level and low level programming languages.

#### ToDo
* Notes on C, C++, potentially Go or Rust.
* Replicate lower level Python functions such as len(), dictionary, hash table, sorting algorithm, slicing, indexing, set inclusion, et cetera in C++ to learn how these functions work. Then port these functions to Python with `pybind11`. Interface these functions with the data structures built from scratch in the data structures lesson.
* Overview of object-oriented-programming (OOP).

## Statistics

* :heavy_multiplication_x: Do some statistics work in Julia.
## Mathematics

* :heavy_multiplication_x: Learn: differential equations, partial differential equations.
    * Work on math courses in Julia.

* :heavy_multiplication_x: Review multivariable calculus, gradient descent, formalize pre-existing notes and upload them.

## Neural Networks

* :heavy_multiplication_x: Feedforward neural network -> recurrent neural network -> convolution neural network -> ResNet -> Transformer, all from scratch. 
    * Verify the correct order of the models so that all paradigmatic neural network models are built in-order.

## Extra Curricular

### Networking / The Internet
### Parallel Computing 

### Databases

### Hashing Functions / Cryptography

* :heavy_multiplication_x: Write a hashing function from scratch. Use the hashing function to build a hash table function. 

### Information Theory / Entropy

### Assorted
#### Git
* Add Git notes here, link to useful Git book.

## Acknowledgements / Inspiration

[Self-learning-Computer-Science - PKUFlyingPig](https://github.com/PKUFlyingPig/Self-learning-Computer-Science/blob/main/README.md)
- Open-source CS curriculum that inspired the structure and content of this curriculum.

[From the Transistor to the Web Browser - George Hotz](https://github.com/geohot/fromthetransistor)
- Proposed CS curriculum that inspired many of the low-level CS lessons on computer hardware, chips, and compilers that are in this curriculum.



### To-Do List
List 1, Porting in Pre-Existing Work:
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
* Build a dictionary method using C++ and an R dictionary interface. Then publish an R dictionary class to CRAN.

List 3, Neural Network Work:
* **Machine Learning / AI** Work on karpathy lectures, tinygrad tutorial, building paradigmatic models in order from scratch until Transformer model is understood.
