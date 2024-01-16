
#===============================================================================
# Disassembling Python Bytecode with dis
#===============================================================================

# dis allows for the analysis of CPython bytecode by disassembling it.
import dis

# The CPython bytecode is a sequence of bytes which is executed by the Python
# interpreter. The CPython virtual machine is a stack-based virtual machine,
# so operations such as LOAD and POP refer to the stack.

#-------------------------------------------------------------------------------
# Adding Two Numbers 
#-------------------------------------------------------------------------------

# Adding two numbers example.
def add(a, b):
    return a + b

dis.dis(add)

# Output:
#  2           0 LOAD_FAST                0 (a)
#              3 LOAD_FAST                1 (b)
#              6 BINARY_ADD
#              7 RETURN_VALUE

# Loads both a and b and then performs a binary addition on them before returning
# the result.

# 2 = The corresponding line number in the source code.
# 0/3/6/7 = The address in the bytecode which corresponds to the byte index. 
# LOAD_FAST/BINARY_ADD = The bytecode instruction.
# 0/1 = The index of the variable in the local namespace (a is at index 0, b is at index 1).
# a/b = The name of the variable in the local namespace.

# LOAD_FAST = Loads a variable from the local namespace.
# BINARY_ADD = Performs a binary addition on the top two items on the stack.
# RETURN_VALUE = Returns the top item on the stack.

#-------------------------------------------------------------------------------
# Assigning a Value to a Variable
#-------------------------------------------------------------------------------

def assign():
    x = 9

dis.dis(assign)

# Output:
#  2           0 LOAD_CONST               1 (9)
#              3 STORE_FAST               0 (x)
#              6 LOAD_CONST               0 (None)
#              9 RETURN_VALUE

# LOAD_CONST = Loads a constant from the code object's co_consts tuple.
# STORE_FAST = Stores a value in the local namespace.
# RETURN_VALUE = Returns the top item on the stack.

#-------------------------------------------------------------------------------
# Linear Search Function
#-------------------------------------------------------------------------------

def linear_search(list, value):
    for i in range(len(list)):
        if list[i] == value:
            return i
    return -1

dis.dis(linear_search)

# LOAD_GLOBAL = Loads a variable from the global namespace.
# LOAD_FAST = Loads a variable from the local namespace.
# PRECALL = Prepares the stack for a function call.
# CALL_FUNCTION = Calls a function.
# GET_ITER = Gets an iterator for the top item on the stack.
# FOR_ITER = Iterates over the top item on the stack.
# BINARY_SUBSCR = Performs a binary subtraction on the top two items on the stack.
# COMPARE_OP = Compares the top two items on the stack.
# POP_JUMP_IF_FALSE = Pops the top item on the stack and jumps to the address if it is false.
# SWAP = Swaps the top two items on the stack.
# LOAD_FAST = Loads a variable from the local namespace.
# JUMP_BACKWARD = Jumps to the address.

#-------------------------------------------------------------------------------
# Nested for Loop
#-------------------------------------------------------------------------------

def nested_for():
    for i in range(10):
        for j in range(10):
            print(i, j)

dis.dis(nested_for)

# POP_TOP = Pops the top item on the stack.

#===============================================================================
# Testing Contiguous and Non-Contiguous Memory Addresses
#===============================================================================

import sys
import ctypes

# Generate an array of 10 integers.
array = [i for i in range(10)]

# Get the memory address of the array, optionally convert it to hexadecimal. Then
# find the size of one integer.
id(array)
hex(id(array))
sys.getsizeof(array[0])

# Find how much space one entry in the array occupies in memory.
id(array[1]) - id(array[0]) # 32 bits

# Because the array is contiguous, we can find the element at index 4 by
# adding 4*32 to the memory address of the first element.
memAddress = id(array[0]) + 4*32
ctypes.cast(memAddress,ctypes.py_object).value

#===============================================================================
# Exploring the Walrus Operator
#===============================================================================

# The walrus operator (new in Python 3.8) allows allows you to assign variables
# in the middle of an expression.

# One unique feature of the walrus operator is that it returns the assigned value
# upon assignment. This is the main intent of the operator, to assign a value and
# also return it, making code clearer.
(walrus := True)

# Use case:
# The walrus operator can be used to shorted code by assigning and returning 
# values at the same time within a function.

# Without walrus operator. Note that we need to assign `num_length` and `num_sum`
# to be able to use them in the dictionary.
numbers = [2, 8, 0, 1, 1, 9, 7, 7]

num_length = len(numbers)
num_sum = sum(numbers)

description = {
    "length": num_length,
    "sum": num_sum,
    "mean": num_sum / num_length,
}

description

# With walrus operator we can assign the values and return them to the dictionary
# at the same time.
numbers = [2, 8, 0, 1, 1, 9, 7, 7]

description = {
    "length": (num_length := len(numbers)),
    "sum": (num_sum := sum(numbers)),
    "mean": num_sum / num_length,
}

description

#===============================================================================
# JIT in Python 
#===============================================================================

# JIT = compilation occurs on demand when the code is run instead of compiled beforehand.
# - The compiler emits machine code, in contrast to an ahead-of-time compiler
# such as the GNU C compiler or Java compiler which emits machine code and distributes
# it as a binary executable.

# Python code is compiled to bytecode which has the following properties:
# - Bytecode requires a bytecode interpreter loop to be executed by the CPU.
# - Bytecode is high level and can equate to 1000s of machine instructions.
# - Bytecode is type agnostic and platform agnostic.

# Consider a simple Python function f():
def func():
    a = 1
    return a 

# This function compiles to 5 bytecode instructions:
import dis
dis.dis(func)

# These 5 bytecode instructions are interpreted when the function is called
# by a massive loop written in C.

# Print the opname and argval for each instruction.
for instruction in dis.get_instructions(func):
    print(instruction.opname, instruction.argval)

# The following is a crude Python evaluation loop, similar to the one in C.
def interpret(func):
    stack = []
    variables = {}
    for instruction in dis.get_instructions(func):
        if instruction.opname == "LOAD_CONST": # LOAD_CONST = push the argval onto the stack, pushes 1 onto the stack.
            stack.append(instruction.argval) 
        elif instruction.opname == "LOAD_FAST": # LOAD_FAST = push the value of the local variable onto the stack, pushes "a" onto the stack in the variable tuple.
            stack.append(variables[instruction.argval])
        elif instruction.opname == "STORE_FAST": # STORE_FAST = pops the top of the stack (1) and stores it in the variable tuple, pops 1 off the stack and stores it with "a".
            variables[instruction.argval] = stack.pop()
        elif instruction.opname == "RETURN_VALUE": # RETURN_VALUE = pops the top of the stack (tuple with "a" and 1) and returns it.
            return stack.pop()

# Run demo Python interpreter.
print(interpret(func))

# This big switch/if-else statement is a simplified version of how the CPython interpreter loop works.

# Everytime you want to run the function, func it has to loop through each instruction and compare the 
# bytecode name (called the opcode) with each if-statement. Both this comparison and the loop itself add 
# an overhead to the execution. That overhead seems redundant if you run the function 10,000 times and the 
# bytecodes never change (because they are immutable). It would be more efficient to instead generate the code 
# in a sequence instead of a evaluating this loop every time you call the function - this is what JIT does.

# This version of JIT, proposed for Python 3.13 is called copy-any-patch JIT.

# The interpreter loop above did two things, it interpreted (looked up the bytecode)
# and then executed it (ran the instructions).

# A copy-and-patch JIT copies the instructions for each command and fills in the blanks (patches)
# the bytecode with the correct values. 

def copy_and_patch_interpret(func):
    code = 'def f():\n'
    code += '  stack = []\n'
    code += '  variables = {}\n'
    for instruction in dis.get_instructions(func):
        if instruction.opname == "LOAD_CONST":
            code += f'  stack.append({instruction.argval})\n'
        elif instruction.opname == "LOAD_FAST":
            code += f'  stack.append(variables["{instruction.argval}"])\n'
        elif instruction.opname == "STORE_FAST":
            code += f'  variables["{instruction.argval}"] = stack.pop()\n'
        elif instruction.opname == "RETURN_VALUE":
            code += '  return stack.pop()\n'
    code += 'f()'
    return code

# This metaprogramming produces the following code:
copy_and_patch_interpret(func)

def f():
  stack = []
  variables = {}
  stack.append(1)
  variables["a"] = stack.pop()
  stack.append(None)
  return stack.pop()

# This is sequential and does not require a loop to be evaluated. We can store
# the function output as a string and evaluate it without running through
# the interpreter loop over and over. In this sense we are compiling just 
# a portion of the whole program.
compiled_function = compile(copy_and_patch_interpret(func), filename="<string>", mode="exec")

print(exec(compiled_function))
print(exec(compiled_function))
print(exec(compiled_function))

# This technique of writing out the instructions for each bytecode and patching the values has
# upsides and downsides compared to a “full” JIT compiler. A full JIT compiler would normally
# compile high-level bytecodes like LOAD_FAST into lower level instructions in an IL (Intermediate Language).
# Because every CPU architecture has different instructions and features, it would be monumentally-complicated
# to write a compiler that converts high-level code directly to machine code and supports 32-bit and 64-bit CPUs,
# as well as Apple’s ARM architecture as well as all the other flavours of ARM. Instead most JIT’s compile first
# to an IL that is a generic machine-code-like instruction set. Those instructions are things like
# “PUSH A 64-bit integer”, “POP a 64-bit float”, “MULTIPLY the values on the stack”. The JIT can then compile
# IL into machine-code at runtime by emitting CPU-specific instructions and storing them in memory
# to be later executed (similar to how we did in our example).

# Summary: JIT takes little chunks of codes such as functions inside a Python program and compiles them
# so that if they are run over-and-over again they do not need to be interpreted each time by a large
# interpreter loop that takes in bytecode instructions and converts them to machine code. 


