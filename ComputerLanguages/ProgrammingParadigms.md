
# Programming Paradigms

### Compiled
* Code is converted directly into machine code that the CPU can execute.
* Generally faster than interpreted languages.
* Example languages: C, C++, Haskell, Rust, Go.

### Interpreted
* Instructions are run line by line without compiling the entire program into machine language.
* Instructions are not directly executed by the target machine, but instead read and executed by another program.
    * For example Python code compiles line by line into bytecode which is then run on the Python Virtual Machine.
* Generally slower than compiled languages due to the overhead of compiling the line before running it.
* Example languages: Ruby, Python, R, JavaScript.

### Just-in-Time Compilation 
* Code is compiled during the execution of the program instead of beforehand.
* JIT generally consists of bytecode translation to machine code, which is then executed directly.
* A JIT compiler analyses the code being executed and identifies the parts of the code where speedup gained from compilation or recompilation would outweigh the overhead of compiling that code.
* Example languages: Java (Java Virtual Machine), C#, Julia.

---

### Imperative
* Programming with an explicit sequence of commands that update state.
* Commands show how the computation takes place, step by step. Each step affects the global state. 
* Example languages: C, C++, Java, Fortran.
* Code example:

```
start:
    numPeople = length(people)
    if i >= numPeople goto finished
    p = people[i]
    nameLength = length(p.name)
    if nameLength <= 5 goto nextOne
    upperName = toUpper(p.name)
    addToList(result, upperName)
```

### Declarative Programming
* Programming by specifying the result you want, not how to get it.
    - The programmer states only what the result should look like, not how to obtain it.
* Developer does not create explicit computation instructions, instead identifying the conditions that should trigger execution processes.
* The compiler is left to decide the order of operations.
* Example languages: HTML, CSS, Prolog, SQL.

```
select upper(name)
from people
where length(name) > 5
order by name
```

### Structured Programming
* Type of imperative programming where control flow is defined by nested loops and subroutines.
* Variables are general local to blocks and have lexical scope.
* Example languages: C#, C++, Java, Python.

```
result = [];
for i = 0; i < length(people); i++ {
    p = people[i];
    if length(p.name)) > 5 {
        addToList(result, toUpper(p.name));
    }
}
return sort(result);
```

### Object Oriented Programming
* Member of the imperative language paradigm.
* OOP is based on the principle of sending messages to objects.
* Objects respond to messages by performing operations (methods) that can have arguments. 
* OOP objects are defined by classes (used to instantiate objects) and methods (class specific operations).
* Example languages: C++, Java, Python, Ruby.

```
result := List new.
people each: [:p |
  p name length greaterThan: 5 ifTrue: [result add (p name upper)]
]
result sort.
```

### Functional Programming
* Member of the declarative language paradigm.
* Control flow is defined by combining function calls, rather than assigning values to variables or modifying global state.
* The power of functional programming comes from passing functions to functions and returning functions from functions. 
* Example languages: Haskell, Lisp, OCaml, F#, Wolfram Language, JavaScript, Scala.

```
sorted(p.name.upper() for p in people if len(p.name) > 5)
```

---

### Paradigms Supported by Various Languages
* **C** = Imperative, procedural language that supports structured programming, lexical variable scope, and recursion, statically typed.
* **C++** = Procedural, imperative, functional, and object-oriented, statically typed.
* **C#** = Procedural, imperative, functional, and object-oriented, statically typed.
* **Java** = Object-oriented, functional, and imperative, statically typed.
* **JavaScript** = Object-oriented, functional, and procedural, dynamically typed.
* **Python** = Imperative, functional, and object-oriented, dynamically typed.
* **R** = Procedural, functional, and object-oriented, dynamically typed.
* **OCaml** = Functional, statically-typed.
* **Haskell** = Functional, statically-typed.
