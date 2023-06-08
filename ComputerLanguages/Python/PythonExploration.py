
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
#
#===============================================================================








