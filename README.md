
# self-taught-CS

This curriculum is my personal approach to learning computer science
from first principles, and it will be filled out over time as I self-learn new
concepts. This is not necessarily a full curriculum, but rather a reflection of
my interests, needs, and what I feel are gaps in my knowledge of computer science.
However, my goal is to build a curriculum that covers the whole compute stack
from transistors and assembly to high level languages, with each lesson building
off of the last lesson. As such, the sections below should form a solid computer
science curriculum.

### To-do: 
* Data Structures and Algorithms.

### Preface: Sand to Atoms to Compute

:white_check_mark: Overview notes on how a computer works the refining of sand 
into silicon ingots, silicone doping to create an electron imbalance, et cetera. 

:x: Notes on FPGAs, how they are built from transistors. Notes on ICs are a collection
of transistors in a reliable package. Notes on LUTs (lookup tables). Possibly
play with Verilator.

### Section 1: The Configuration of Hardware.

:x: Learn some Verilog, build a UART. Build some functional units. 

### Section 2: Processors

:x: Build an ARM7 CPU in Verilog. Start by building a pipeline with start, decode,
fetch, execute.

:x: Learn some RISC-V architecture.

### Section 3: Compilers

:x: Build a C compiler in Haskell. Consider the basics of compiler design. Write 
a parser, output ARM assembly which can then be run through the processor designed
in lesson 2. 

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

