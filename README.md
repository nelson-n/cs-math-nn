
<!--- --------------------------------------------------------------------- --->

# Computer Science - Math - Deep Learning
<a href='https://github.com/nelson-n/cs-math-nn/blob/main/Assorted/Assets/RepoLogo.png'><img src='/Assorted/Assets/RepoLogo.png' align="right" height="160" /></a>

This curriculum is my personal approach to learning topics in computer science, mathematics, and deep learning (+ quant finance) from first principles. This repository is not necessarily a full curriculum, but rather a reflection of my interests, needs, and gaps in my knowledge. However, my goal is to cover the whole compute stack from transistors and assembly to the math underpinning transformer models. As such, the notes and exercises compiled below should form a solid computer science, math, and deep learning curriculum.

<!--- --------------------------------------------------------------------- --->

## Sections

* [Hardware / Processors](#hardware--processors)
* [Compilers / Operating Systems](#compilers--operating-systems)
* [Data Structures / Algorithms](#data-structures--algorithms)
* [Computer Languages](#computer-languages)
* [Mathematics](#mathematics)
* [Statistics](#statistics)
* [Machine Learning](#machine-learning)
* [Deep Learning](#deep-learning)
* [Quantitative Finance](#quantitative-finance)
* [Additional Topics](#additional-topics)
    * [Networking / Internet](#networking--internet) | [Parallel Computing](#parallel-computing) | [Databases](#databases) | [Cryptography](#cryptography) | [Information Theory / Entropy](#information-theory--entropy)
* [Assorted](#assorted)

## Language Cheatsheets
* [Unix Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/CompilersOperatingSystems/Unix/UnixCheatsheet.md)
* [Git Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/Assorted/GitCheatsheet.md)
* [Python Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/Python/PythonCheatsheet.py)
* [R Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/R/RCheatsheet.R)
* [R Plots and Tables Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/R/PlotsTables.R)
* [Java Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/Java/JavaCheatsheet.java)
* [SQL Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/SQL/SQLCheatsheet.md)

## Subject Notes
* [Mathematics Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Math/MathNotes.ipynb)
* [Calculus Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Math/Calculus.ipynb)
* [Linear Algebra Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Math/LinearAlgebra.ipynb)
* [Statistics Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Statistics/Statistics.ipynb) 
* [Machine Learning Notes](https://github.com/nelson-n/cs-math-nn/blob/main/MachineLearning/MachineLearningPrinciples.ipynb)
* [Deep Learning Notes](https://github.com/nelson-n/cs-math-nn/blob/main/DeepLearning/DeepLearningPrinciples.ipynb)
* [Quantitative Finance Notes](https://github.com/nelson-n/cs-math-nn/blob/main/QuantitativeFinance/QuantFinance.ipynb)

## General Cheatsheets
* [Data Structures and Time Complexity Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresCheatsheet.md)
* [Math Symbols Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/Math/MathSymbolsCheatsheet.md)

<!--- --------------------------------------------------------------------- --->

## Preface: Atoms to Bits 

`/Assorted/atoms2bits.pdf`

* This preface ([atoms2bits](https://github.com/nelson-n/cs-math-nn/blob/main/Assorted/atoms2bits.pdf)) gives an overview of the complete compute stack: the refining of sand into silicon ingots, the doping of silicon to create differences in valence shell electrons, how doped silicon is used to build transistors, how transistors form logic gates, CPU architecture and operation, memory caches, instruction set architecture, operating systems, and higher level languages. 

#### :bangbang: to-do
* In atoms2bits add notes on FPGAs, how FPGAs are built from transistors, notes on how ICs are a collection of transistors in a reliable package, notes on LUTs (lookup tables).

<!--- --------------------------------------------------------------------- --->

## Hardware / Processors

#### :bangbang: to-do
* Implement AND, NAND, XOR, and other logic gates from scratch, use these logic gates to build functional units such as a 1-bit adder, multiplexer, sequential logic, SRAM, etc. Use logic gates to create simple implementations of various memory units: L1 cache, DRAM, etc. The functional units built in this section will be used later when building a model instruction set.
* Build a simple ARM7 CPU in Verilog or with another infrastructure.
    * Start by building a pipeline with simple start, decode, fetch, and exectute commands. Then build a simple register/memory unit to push and pull data, a simple ALU (arithmetic logic unit) that can perform basic arithmetic and logic operations, and a simple CU (control unit) for finding instructions and directing operations. These should be built on top of the functional units constructed in the hardware lesson. 
    * Additional: Write basic arithmetic instructions, branch instructions, and memory instructions. Allow for instruction out-of-order, basic parallelism, use dependency graphs for instructions. Set up a memory hierarchy with Registers, L1 cache, L2 cache, L3 cache, and DRAM.
* Extra-curricular: Build a UART in Verilog, GPU basics.

<!--- --------------------------------------------------------------------- --->

## Compilers / Operating Systems

`/CompilersOperatingSystems`

* **Cheatsheets**:
    * [Unix Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/CompilersOperatingSystems/Unix/UnixCheatsheet.md): Covers useful Unix commands.

#### :bangbang: to-do
* Create a set of general notes on linux/unix, combine with notes in unix.pdf in local. Add notes on the basic linux filesystem format from [here](https://www.linuxfoundation.org/blog/blog/classic-sysadmin-the-linux-filesystem-explained).
* Build a C compiler in Haskell. Consider the basics of compiler design, write a parser, output ARM assembly which can then be run through the simple processor designed in the processors lesson.
    * Build functions for converting binary to numbers and ASCII characters.
* Learn RISC-V architecture, contrast with x86, ARM, and other instruction set architectures.
* Build a UNIX-ish operating system in C or C++ with simple abilities like open, read, write, close, init, cat, ls, rm.
* Build a filesystem like FAT in C or C++.

<!--- --------------------------------------------------------------------- --->

## Data Structures / Algorithms

`/DataStructuresAlgorithms`

* **Cheatsheets**:
    * [Data Structures and Time Complexity Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresCheatsheet.md): Covers dependencies, primitive types, basic types, fundamental data structures, sorting/searching algorithms, and time complexities of common algorithms.
    * [Binary Tree Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/BinaryTreeCheatsheet.java): Covers binary tree search, recursion, and implementation.

* **Notes on**:
    * [DSA Fundamentals](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/DataStructuresAlgorithmsFundamentals.java): Data structures and algorithms fundamentals.
    * [Linked Lists](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/LinkedLists.java): Linked lists, doubly linked lists, and circular linked lists.
    * [Sorting and Searching](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/SortingSearching.java): Sorting and searching algorithms. 
    * [Stacks Queues Sets](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/StacksQueuesSets.java): Stacks, queues, and sets.
    * [Hash Tables](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/HashTables.java): Hash tables and hash algorithms.
    * [Trees](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/Trees.java): Tree structures, properties, and algorithms.
    * [Heaps Priority Queues](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/HeapsPriorityQueues.java): Heaps and priority queues.
    * [Graphs](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/Graphs.java): Graph structures.
    * [Graph Algorithms](https://github.com/nelson-n/cs-math-nn/blob/main/DataStructuresAlgorithms/DataStructuresAlgorithmsNotes/GraphAlgorithms.java): Graph algorithms.

* **Exercises**:
    * Comparison of the speed of recursion in Python, R, Java, C, and C++.

#### :bangbang: to-do
* Learn the low-level data structures behind the base data types in Python such as: list, array, tuple, et cetera. Build versions of these data types in C++ and then port to Python with a package like `pybind11`.
* Add a lesson on the properties of different number systems: hexadecimal system, 32-bit numbers, 128-bit numbers, how with a 32-bit number system you can generate the numbers 0-65535, etc.

<!--- --------------------------------------------------------------------- --->

## Computer Languages

`/ComputerLanguages`

* **Cheatsheets**:
    * [Programming Paradigms Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/ProgrammingParadigms.md): Covers compiled vs. interpreted languages, imperative, declarative, functional, and object-oriented programming paradigms.

* **Notes on**:
    * [Python Notes](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/Python/FluentPython.py): Python
    * [Java Notes](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/Java/JavaNotes.java): Java
    * [R Notes](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/R/AdvancedR.R): R
    * [Julia Notes](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/Julia/JuliaNotes.jl): Julia 
    * [SQL Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/ComputerLanguages/SQL/SQLCheatsheet.md): SQL

#### :bangbang: to-do
* Add notes on C, C++, potentially Go or Rust.
* Replicate lower level Python functions such as len(), dictionary, hash table, a sorting algorithm, slicing, indexing, set inclusion, et cetera in C++ to learn how these functions work. Then port these functions to Python with `pybind11`. Interface these functions with the data structures built from scratch in the data structures lesson.
* Overview of object-oriented-programming (OOP).
* Build a dictionary method using C++ and an R dictionary interface. Then publish an R dictionary class to CRAN.

<!--- --------------------------------------------------------------------- --->

## Mathematics

`/Math`

* **Notes on**:
    * [Linear Algebra Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Math/LinearAlgebra.ipynb): General concepts of linear algebra. 
    * [Calculus Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Math/Calculus.ipynb): General concepts of calculus. 
    * [Math Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Math/MathNotes.ipynb): Assorted topics in mathematics.
    * [Math Symbols Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/Math/MathSymbolsCheatsheet.md)

<!--- --------------------------------------------------------------------- --->

## Statistics

`/Statistics`

* **Notes on**:
    * [Statistics Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Statistics/Statistics.ipynb): General topics in statistics.
    * [Regression Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Statistics/Regression.ipynb): Notes on regression, methods of estimation, and linear regression interpretations.
    * [SVD and PCA Notes](https://github.com/nelson-n/cs-math-nn/blob/main/Statistics/SVD_PCA.ipynb): Notes on Singular Value Decomposition and Principal Component Analysis.

* **Exercises**:
    * Implementation of Maximum Likelihood Estimation (MLE) from scratch that benchmarks against a canonical optimization function.
    * Implementation of the Metropolis-Hastings method from scratch.

#### :bangbang: to-do
* Poker exercises: implement a card deck class with operations such as draw X cards, calculate probabilites of different hands given different stages of a game, implement riffle shuffle, et cetera.

<!--- --------------------------------------------------------------------- --->

## Machine Learning

`/MachineLearning`

* **Notes on**:
    * [Machine Learning Principles](https://github.com/nelson-n/cs-math-nn/blob/main/MachineLearning/MachineLearningPrinciples.ipynb): Machine learning principles.
    * [Machine Learning Code](https://github.com/nelson-n/cs-math-nn/blob/main/MachineLearning/MachineLearningCode.ipynb): Machine learning code.

<!--- --------------------------------------------------------------------- --->

## Deep Learning

`/NeuralNetworks`

* **Notes on**:
    * [Deep Learning Principles](https://github.com/nelson-n/cs-math-nn/blob/main/DeepLearning/DeepLearningPrinciples.ipynb): Deep learning principles.

* **Exercises**:
    * Implementation of the following neural network architectures from scratch using NumPy. Neural networks are then trained and tested out-of-sample.
        * Feedforward Neural Network
        * Recurrent Neural Network
    * Solving simple neural network problems relating to finding optimas, gradient descent, et cetera.

#### :bangbang: to-do
* Build the following models from scratch in NumPy: Feedforward neural network -> recurrent neural network -> convolution neural network -> ResNet -> Transformer -> Diffusion Model, all from scratch. 
    * Verify the correct order of the models so that all paradigmatic neural network models are built in-order.

<!--- --------------------------------------------------------------------- --->

## Quantitative Finance

`/QuantitativeFinance`

* **Notes on**:
    * [Quantitative Finance Notes](https://github.com/nelson-n/cs-math-nn/blob/main/QuantitativeFinance/QuantFinance.ipynb): Notes on assorted topics in quantitative finance.
        * Sensitivity measures and derivations.
        * Bond measures.
        * General definitions.
        * Yield curve estimation.

<!--- --------------------------------------------------------------------- --->

## Additional Topics

<!--- --------------------------------------------------------------------- --->
### Networking / Internet

`/NetworkingInternet`

* **Notes on**:
    * [Network / Internet Notes](https://github.com/nelson-n/cs-math-nn/blob/main/AdditionalTopics/NetworkInternet/internet.pdf): Notes on computer networking and how the internet protocol stack works. 

#### :bangbang: to-do
* Manually create and send TCP/IP packets: [tutorial](https://inc0x0.com/tcp-ip-packets-introduction/tcp-ip-packets-3-manually-create-and-send-raw-tcp-ip-packets/)

<!--- --------------------------------------------------------------------- --->

### Parallel Computing 

<!--- --------------------------------------------------------------------- --->

### Databases

<!--- --------------------------------------------------------------------- --->

### Cryptography

#### :bangbang: to-do
* Write a hashing function from scratch. Use the hashing function to build a hash table function. 
* Upload pre-existing crptography notes.
* Write a script to generate public/private key pairs using SHA256 or another hashing function.

<!--- --------------------------------------------------------------------- --->

### Information Theory / Entropy

<!--- --------------------------------------------------------------------- --->

## Assorted

* **Cheatsheets**:
    * [Git Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/Assorted/GitCheatsheet.md): Covers useful commands for working with Git and GitHub.
    * [Latex Cheatsheet](https://github.com/nelson-n/cs-math-nn/blob/main/Assorted/LatexCheatsheet.md): Covers useful commands and packages for working in LaTeX.

<!--- --------------------------------------------------------------------- --->
## To-Do List
* Add notes to `Statistics` on Poisson processes, Cox Process / Doubly Stochastic Poisson Process.
* Add notes to `QuantitativeFinance` notes on Cox-Ingersoll-Ross process which is commonly used in term-structure modelling, add notes on Affine Term Structure Models.
    * Add notes on the difference between risk-free measures Q and physical measures P.
    * Reference ReducedFormModel.pdf for help.
* Work on `/Math` calculus fundamentals notes and `/Statistics` statistics fundamentals notes. 
    * Notes on the Taylor Series.
* Work on `/Math` notes on differential equations, partial differential equations.
    * Use 3B1B videos.
* Add `/QuantitativeFinance` notes on Merton (1974), Nelson-Siegel, Black-Scholes, and a Normal Jump Diffusion Model (GBM that allows for Poisson jumps in asset prices, see Merton 1976).
* Neural Network work.
    * Look at Karpathy lectures.
    * Look at tinygrad tutorial, play with tinygrad.
    * Build paradigmatic models in sequential order up to the Transformer model.
    * Read *Attention Is All You Need* paper.
* Add a quick study of BLAS and LAPACK routines.
    * https://docs.julialang.org/en/v1/stdlib/LinearAlgebra/#BLAS-functions
* Work on *Fluent Python* notes.
* Add LaTeX equations to pre-existing math/stats/neural network exercises and `/Assorted/EquationsCheetsheet.md` based off of this [tutorial](https://blmoistawinde.github.io/ml_equations_latex/#negative-loglikelihood).
* Work on poker exercises.
* Build a hashing function (SHA256) from scratch, then use it to build a hash table class from scratch.

<!--- --------------------------------------------------------------------- --->
