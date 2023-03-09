
#==============================================================================
# Fluent Python - Luciano Ramalho
#==============================================================================

#==============================================================================
# Chapter 1: The Python Data Model
#==============================================================================

# you can think of the python data model as the framework that formalizes the
# building blocks of the language itself: classes, iterators, functions, etc

# when coding in any language you spend a lot of time implementing methods that
# are called by the framework, the same happens in python
# the python interpreter invokes special methods to perform basic object 
# operations often triggered by the syntax __getitem__

# for example: to evaluate my_collection[key] the interpreter calls 
# my_collection.__getitem__(key)

#------------------------------------------------------------------------------
# A Pythonic Card Deck
#------------------------------------------------------------------------------

# the following demonstrates the power of two methods: __getitem__ and __len__
import collections

Card = collections.namedtuple('Card', ['rank', 'suit'])

class FrenchDeck:
    ranks = [str(n) for n in range(2, 11)] + list('JQKA')
    suits = 'spades diamonds clubs hearts'.split()

    def __init__(self):
        self._cards = [Card(rank, suit) for suit in self.suits
                                        for rank in self.ranks]

    def __len__(self):
        return len(self._cards)

    def __getitem__(self, position):
        return self._cards[position]

# collections.namedtuple constructs a simple class to represent individual cards

# the FrenchDeck class includes __len__ and __getitem__ methods which allows
# position and length to be queried
deck = FrenchDeck()
len(deck)
deck[0]
deck[42]

# should we create a method to pick a random card? No need, python already has a 
# function to get a random item from a sequence
from random import choice

choice(deck)

# we have just seen some of the advantages ot using special methods
# the user does not have to memorize arbitrary method names
# it is easier to benefit from the rich python standard library instead
# of reinventing the wheel as shown by the random.choice function

# because __getitem__ delegates to the [] operator of self._cards the deck
# automatically supports slicing

# here is how we look at the top 3 cards in the deck and then pick just the aces
# by starting on index 12 and skipping 13 cards at a time
deck[:3]
deck[12::13]

# just by implementing the __getitem__ special method the deck is iterable
for card in deck:
    print(card)

# the deck can also be iterated in reverse
for card in reversed(deck):
    print(card)

# iteration is often implicit, if a class has no __contains__ method, the in
# operator does a sequential scan, in works with the FrenchDeck class 
# because the class is iterable due to __getitem__
Card('Q', 'hearts') in deck

# as implemented so far, FrenchDeck cannot be shuffled because it is immutable
# in Chapter 11 this will be fixed by adding the __setitem__ method

#------------------------------------------------------------------------------
# How Special Methods are Used
#------------------------------------------------------------------------------

# special methods are meant to be called by the python interpreter, not by you
# you do not write my_object.__len__(), you write len(my_object)

# if my_object is an instance of a user-defined class, python calls the __len__
# instance method that you implemented

# for built in types like list, str, bytearray etc, the interpreter takes a 
# shortcut and the CPython implementation of len() is used which is faster

# often the special method call is implicit, the statement i in x causes
# an invocation of iter(x) which calls x.__iter__()

# code should not have many direct calls to special methods with the exception
# of the __init__ call which is used invoke the initializer of the superclass
# in your one __init__ implementation

#------------------------------------------------------------------------------
# Emulating Numeric Types
#------------------------------------------------------------------------------

# we will implement a class to represent 2d vectors (Euclidean vectors)
# eventually we want to be able to accomplish the following

# >>> v1 = Vector(2, 4)
# >>> v2 = Vector(2, 1)
# >>> v1 + v2
# Vector(4, 5)

# we will use the special methods __repr__, __abs__, __add__, __mul__

from math import hypot 

class Vector:

    def __init__(self, x=0, y=0):
        self.x = x
        self.y = y

    def __repr__(self):
        return 'Vector(%r, %r)' % (self.x, self.y) 

    def __abs__(self):
        return hypot(self.x, self.y) 

    def __bool__(self):
        return bool(abs(self))
    
    def __add__(self, other):
        x = self.x + other.x 
        y = self.y + other.y 
        return Vector(x, y)

    def __mul__(self, scalar):
        return Vector(self.x * scalar, self.y * scalar)

#------------------------------------------------------------------------------
# String Representation
#------------------------------------------------------------------------------

# the __repr__ special method is called by the repr built-in to get the string
# representation of an object for inspection

# the string returned by __repr__ should be unambiguous and match the source 
# code necessary to re-create the object being represented, that is why our
# chosen representatin looks like calling the constructor of the class:
# Vector(2, 2)

v1 = Vector(2, 2)
v1
repr(v1)

# contrast __repr__ with __str__ which is called by the str() constructor and
# implicitly used in the print function. __str__ should return a string suitable
# for display to end users

# if you only implement one of these special methods, choose __repr__ because 
# when no custom __str__ is available, python will fallback on __repr__

#------------------------------------------------------------------------------
# Arithmetic Operators
#------------------------------------------------------------------------------

# the example above uses two operators: + and * to show off the functionality
# of __add__ and __mul__

# note that in both cases the methods create and return a new intance of Vector,
# and do not modify either operand, self and the other operand are merely read

# this is the expected behavior of infix operators: to create new objects and
# not touch their operands

#------------------------------------------------------------------------------
# Boolean Value of a Custom Type
#------------------------------------------------------------------------------

# although python has a bool type, it accepts any object in a boolean context
# to determine whether a value x is truthy of falsy, python applies bool(x)
# which always returns True or False regardless of object type

# by default, instances of user-defined classes are considered truthy unless
# __bool__ or __len__ is implemented

# bool(x) calls x.__bool__() and uses the result
# if __bool__ is not implemented, Python tries to invoke x.__len__() and if 
# that returns zero, bool returns False, otherwise bool returns True

# in the example above the implementation of __bool__ is simple, it returns 
# False if the magnitude of the vector is 0, True otherwise

#------------------------------------------------------------------------------
# Overview of Special Methods
#------------------------------------------------------------------------------

# string / byte representation
__repr__
__str__
__format__
__bytes__

# conversion to number
__abs__
__bool__
__complex__
__int__
__float__
__hash__
__index__

# emulating collections
__len__
__getitem__
__setitem__
__delitem__
__contains__

# iteration
__iter__
__reversed__
__next__

# emulating callables
__call__

# context management
__enter__
__exit__

# instance creation and destruction
__new__
__init__
__del__

# attribute management
__getattr__
__getattribute__
__setattr__
__delattr__
__dir__

# attribute descriptors
__get__
__set__
__delete__

# class services
__prepare__
__instancecheck__
__subclasscheck__

# unary numeric operators
__neg__-
__pos__+
__abs__abs()

# rich comparison operators
__lt__<
__le__<=
__eq__==
__ne__!=
__gt__>
__ge__>=

# arithmetic operators
__add__+
__sub__-
__mul__*
__truediv__/
__floordiv__//
__mod__%
__divmod__divmod()
__pow__** or pow()
__round__round()

# reversed arithmetic operators
__radd__
__rsub__
__rmul__
__rtruediv__
__rfloordiv__
__rmod__
__rdivmod__
__rpow__

# augmented assignment arithmetic operators
__iadd__
__isub__
__imul__
__itruediv__
__ifloordiv__
__imod__
__ipow__

# bitwise operators
__invert__~
__lshift__<<
__rshift__>>
__and__&
__or__|
__xor__^

# reversed bitwise operators
__rlshift__
__rrshift__
__rand__
__rxor__
__ror__

# augmented assignment bitwise operators 
__ilshift__
__irshift__
__iand__
__ixor__
__ior__

# the reversed operators are fallbacks when the operands are swapped
# b * a instead of a * b

# augmented assignments are shortcuts combining an infix operator with 
# variable assignment (a = a * b becomes a *= b)

#==============================================================================
# Chapter 2: Data Structure (Sequence Types)
#==============================================================================

#------------------------------------------------------------------------------
# An Array of Sequences
#------------------------------------------------------------------------------

# The standard library offers a rich selection of sequence types implemented in 
# the C language.

# Container sequences: list, tuple, and collections.deque
# Flat sequences: str, bytes, bytearray, memoryview, array.array

# Container sequences hold references to the objects they contain which may be 
# of any type.

# Flat sequences physically store the value of each item within its own memory
# space. Flat sequences are thus more compact, but they are limited to holding
# primitive values like characters, bytes, and numbers.

# Another way of grouping sequences type is by mutability

# Mutable sequences: list, bytearray, array.array, collections.deque, memoryview
# Immutable sequences: tuple, str, bytes

#------------------------------------------------------------------------------
# List Comprehension and Generator Expressions
#------------------------------------------------------------------------------

# A quick way to build a sequence is to using list comprehension (if the target
# is a list) or a generator expression for all other types of sequences.

# listcomps = list comprehension
# genexps = generator expressions

# Syntax note:
# In python line breaks are ignored inside paris of [], {}, or () so you can 
# build multiline lists, listcomps, genexps, dictionaries without using the 
# ugly \ line continuation escape.

# List comprehension example:
x = "ABC"
dummy = [ord(x) for x in x]

# List comprehensions build lists from sequences or any other iterable type by
# filtering and transforming items. The filter and map built in functions can 
# be composed to do the same, but readability suffers.

#------------------------------------------------------------------------------
# Listcomps Versus map and filter
#------------------------------------------------------------------------------

# Listcomps do everything the map and filter functions do, without the contortions
# of the functionally challenged Python lambda

# Listcomp:
symbols = "ABDCVE"
beyond = [ord(s) for s in symbols if ord(s) > 127]

# Filter:
beyond = list(filter(lambda c: c > 127, map(ord, symbols)))

#------------------------------------------------------------------------------
# Cartesian Products
#------------------------------------------------------------------------------

# Example, imagine that you want to produce a list of t-shirts available in 
# two colors and three sizes, this can be created via a Cartesian product
# using list comprehension:

colors = ['black', 'white']
sizes = ['S', 'M', 'L']
tshirts = [(color, size) for color in colors for size in sizes] 

# the list can also be re-ordered with size given precedent
tshirts = [(color, size) for size in sizes for color in colors]

# Listcomps are a one trick pony: they build lists. To fill up other sequence
# types a genexp should be used. 

#------------------------------------------------------------------------------
# Generator Expressions
#------------------------------------------------------------------------------

# To initialize tuples, arrays, and other types of sequences you could begin
# with a listcomp and then convert to the desired type, but using a genexp
# will save memory as no conversion will be necessary.

# Genexps use the same syntax as listcomps, but are enclosed in parentheses
# rather than brackets.

symbols = 'ABdEFc'
tuple(ord(symbol) for symbol in symbols)

# If the generator expression is the only argument in a function (such as the 
# function tuple) than it does not need to be enclosed in parentheses.

import array
array.array('I', (ord(symbol) for symbol in symbols))

# If there are multiple arguments, then the generator expression must be 
# enclosed in parentheses. 

#------------------------------------------------------------------------------
# List Methods
#------------------------------------------------------------------------------

s.add() # s + s2 concatenation
s.iadd() # s += s2 in-place concatenation
s.append() # Append one element to the right (after the last element)
s.clear() # Delete all items
s.__contains__(e) # e in s
s.__copy__() # Shallow copy
s.count() # Count occurences of an elements
s.__delitem__(p) # Remove item at position p
s.__getitem__(p) # s[p] - get item at position p
s.index(e) # Find position of first occurence of e
s.insert(p, e) # Insert element e before the item at position p
s.__iter__() # Get iterator
s.__len()__ # Get number of items
s.__mul__(n) # s * n - repeated concatenation
s.__imul__(n) # s *= n - in place repeated concatenation
s.__rmul__(n) # n * s - reversed repeated concatenation
s.pop() # Remove and return last item
s.remove() # Remove first occurence of element e by value
s.reverse() # Reverse the order of items in place
s.__reversed__() # Get iterator to scan items from last to first 

#------------------------------------------------------------------------------
# Tuples Are Not Just Immutable Lists
#------------------------------------------------------------------------------

# Tuples hold records: each item in the tuple holds the data for one field and
# the position of the item gives its meaning.

# The following example shows tuples being used as records, note that in every
# expression sorting the tuple would destroy the information because the meaning
# of each data item is given by its position in the tuple.

lax_coordinates = (33.9425, -118.408056)
city, year, pop, chg, area = ('Tokyo', 2003, 32450, 0.66, 8014) 
traveler_ids = [('USA', '31195855'), ('BRA', 'CE342567'), ('ESP', 'XDA205856')]

for passport in sorted(traveler_ids):
    print('%s/%s' % passport)

# The for loop knows how to retrieve items in the tuple seperately, this is 
# called "unpacking", the second item gets assigned a dummy variable _ here
for country, _ in traveler_ids:
    print(country)

#------------------------------------------------------------------------------
# Tuple Unpacking
#------------------------------------------------------------------------------

# The most visible form of tuple unpacking is parallel assignment: assigning
# items from an iterable tuple of variables
lax_coordinates = (33.9425, -118.408056)
latitude, longitude = lax_coordinates # note how the order of the tuple matters
latitude
longitude

# Tuple unpacking allows functions to return multiple values in a way that is
# convenient to the caller
import os
_, filename = os.path.split('/home/nelson/test/testfile.txt')
filename

# Sometimes we only care about certain parts of a tuple when unpacking, in 
# these cases a dummy variables such as _ is used.

# Defining function parameters with *args to grab arbitrary excess arguments
# is a classic Python feature

# In Python 3 this idea was extended to tuple unpacking
a, b, *rest = range(5)
rest

#------------------------------------------------------------------------------
# Nested Tuple Unpacking
#------------------------------------------------------------------------------

# The tuple to receive an expression to unpack can have nested tuples such as:
# (a, b, (c, d))

metro_areas = ['Tokyo', 'JP', 36.933, (35.6872, 139.61527)]

#------------------------------------------------------------------------------
# Named Tuples
#------------------------------------------------------------------------------

# One missing feature of tuples is that when using them as records it can 
# be desirable to name the fields.

# The collections.namedtuple function is a function factory that produces 
# subclasses of tuple enhanced with field names and a class name

from collections import namedtuple

# Two parameters are required, a class name and a list of field names
City = namedtuple('City', 'name country population coordinates')
tokyo = City('Tokyo', 'JP', 36.933, (35.689722, 139.691667))

tokyo
tokyo.population

#------------------------------------------------------------------------------
# Tuples as Immutable Lists
#------------------------------------------------------------------------------

# When using tuples as an immutable variation of list, it helps to know how
# similar they are.

# Tuple supports all list methods that do not involve adding or removing items

# Methods and attributes available for both lists and tuples:
s.__add__(s2) # s + s2 - concatenation
s.__contains__(e) # e in s
s.count(e) # count occurrences of an element
s.__getitem__(e) # s[p] - get item at position
s.index(e) # find position of first occurrence of e
s.__iter__() # get iterator
s.__len__() # len(s) - number of items
s.__mul__(n) # s * n - repeated concatenation
s.__rmul__(n) # n * s - reversed repeated concatenation

# List only methods and attributes
s.__iadd__(s2) # s += s2 - in place concatenation
s.append(e) # append one element after last
s.clear() # delete all items
s.__delitem__(p) # remove item at position p
s.extend(it) # append items from iterable it
s.insert(p, e) # insert element e before the item at position p
s.__imul__(n) # s *= n - in place repeated concatenation
s.pop([p]) # remove and return last item at position p 
s.remove(e) # remove first occurrence of element e by value
s.reverse() # reverse the order of items in place
s.__reversed__() # get iterator to scan items from last to first
s.__setitem__(p, e) # s[p] = e - put e in position p, overwriting exising item
s.sort([key], [reverse]) # sort items in place with optional keyword arguments

#------------------------------------------------------------------------------
# Slicing
#------------------------------------------------------------------------------

# All sequence types in Python support slicing

#------------------------------------------------------------------------------
# Why Slices and Range Exclude the Last Item
#------------------------------------------------------------------------------

# Convention of excluding the last item in slices and ranges works well with
# the zero based indexing used in Python and C.

# It is easy to see the length of a slice / range when the stop position is 
# given, range(3) and my_list[:3] both produce 3 items.

# It is easy to compute the length of a slice or range when start or stop are 
# given, just subtract stop - start

# It is easy to split a sequences in two parts at any index x without overlapping,
# simply get my_list[:x] and my_list[x:].

#------------------------------------------------------------------------------
# Slice Objects
#------------------------------------------------------------------------------

# s[a:b:c] can be used to specify a stride or step c, causing the resulting
# slice to skip items
s = 'bicycle'
s[::3]

# the stride can also be negative, returning items in reverse
s[::-1]

# to evaluate the expression seq[start:stop:step] Python calls 
# seq.__getitem__(slice(start, stop, step))

#------------------------------------------------------------------------------
# Multidimensional Slicing and Ellipsis
#------------------------------------------------------------------------------

# The [] operator can take multiple indexes or slices seperated by commas, this
# is used for instance in NumPy where items of a 2d numpy.ndarray can be 
# fetched using the syntax a[i, j] and a 2d slice obtained with a[m:n, k:l].

# In the above case, the __getitem__ and __setitem__ special methods that 
# handle the [] operator simply receive the indices in a[i, j] as a tuple,
# to evaluate a[i, j] Python calls a.__getitem__((i, j)).

# In slicing, the ellipsis (...) is a used as a shortcut for multiple arguments.
# Example: x[i, ...] is a shortcut for x[i, :, :, :]

#------------------------------------------------------------------------------
# Assigning to Slices
#------------------------------------------------------------------------------

# Mutable sequences can be modified in place using slice notation on the left
# hand side of the argument.
l = list(range(10))
l[2:5] = [20, 30]
del l[5:7]
l[3::2] = [11, 22]

#------------------------------------------------------------------------------
# Using + and * with Sequences
#------------------------------------------------------------------------------

# Both + and * create new objects and never change the operands

l = [1, 2, 3]
l * 5
5 * 'abcd'

#------------------------------------------------------------------------------
# Building Lists of Lists
#------------------------------------------------------------------------------

# Sometimes lists need to be initialized with a number of nested lists, for
# example to represent squares on a game board. The best way of doing this
# is with list comprehension.

board = [['_'] * 3 for i in range(3)]
board[1][2]

#------------------------------------------------------------------------------
# Augmented Assignment with Sequences
#------------------------------------------------------------------------------

# The augmented assignment operators += and *= behave very differently 
# depending on the first operand. 

# The special method that makes += work is __iadd__ for "in-place addition",
# however, if __iadd__ is not implemented Python falls back on calling __add__.
# For mutable sequences __iadd__ will typically be implemented, but for 
# immutable sequences it will not.

# Example of *= with a mutable and immutable sequences, notice how with the 
# mutable sequence there is copy in place.

# mutable list
l = [1, 2, 3]
id(l)

l *= 2
id(l)

# immutable tuple
l = (1, 2, 3)
id(l)
l *= 2
id(l)

#------------------------------------------------------------------------------
# list.sort and the sorted Built-In Function
#------------------------------------------------------------------------------

# The list.sort method sorts a list in place without making a copy. Functions
# and methods that change an object in place should return None to make it clear
# to the caller that the object itself was changed and no new object was 
# created. 

# In contrast, the built in function 'sorted' creates a new list and returns it.

# Both list.sort and sorted take two optional keyword only arguments: 
# reverse = if True items are returned in descending order (default = False)
# key = a one-argument function that will be applied to each item to produce
# its sorting key, for example, key = len will sort strings by character length.

fruits = ['grape', 'raspberry', 'apple', 'banana']
sorted(fruits, key = len, reverse = True)

#------------------------------------------------------------------------------
# Managing Ordered Sequences with Bisect
#------------------------------------------------------------------------------

# The bisect module offers two main functions - bisect and insort - that use 
# the binary search algorithm to quickly find and insert items in any sorted
# sequence.

bisect(needle, haystack) # Does a binary search for needle in haystack which 
# must be a sorted sequence to locate the position where needle can be inserted
# while maintaining haystack in ascending order. 

import bisect

def grade(score, breakpoints = [60, 70, 80, 90], grades = 'FDCBA'):
        i = bisect.bisect(breakpoints, score)
        return(grades[i])

[grade(score) for score in [33, 99, 77, 86, 45, 68]]

# Sorting is expensive, so once you have a sorted sequence it is a good idea
# to keep it that way. That is why bisect.insort was created.

insort(seq, item) # inserts item into seq so as to keep seq in ascending order.

#------------------------------------------------------------------------------
# When a List is Not the Answer
#------------------------------------------------------------------------------

# List is flexible and easy to use, but depending on the application there 
# may be better alternatives. For example, if you need to store 10m floating
# point values, an array is much more efficient because array does not 
# actually hold full-fledged float objects, but only the packed bytes representing
# their machine values - just like an array in the C language.

# If your code does a lot of containment checks (ie item in my_collection) 
# then consider a set. Sets are optimized for fast membership checking but they
# are not sequences (their content is unordered).

#------------------------------------------------------------------------------
# Arrays
#------------------------------------------------------------------------------

# If the list will only contain numbers, array.array is more efficient than list.
# It supports all mutable sequence operations (.pop, .insert, .extend etc) and 
# additional methods for fast loading such as .frombytes and .tofile.

# A python array is as lean as a C array. When creating an array, you provide
# a typecode letter to determine the underlying C type used to store each item
# in the array. For example b is the typecode for a signed char. If you create
# an array('b') then each item will be stored in a single byte and interpreted
# as an integer from -128 to 127. Python will not let you put any number that
# does not match the type for the array.

# Creating a large array of floats.
from array import array
from random import random
floats = array('d', (random() for i in range(10**7))) # 10m floating points.
floats[10]

# Arrays can be saved and loaded quickly. Another fast and flexible way of
# saving numeric data is the pickle module for object serialization. Saving
# an array of floats with pickle.dump is almost as fast as array.tofile,
# however, pickle handles complex data types like numbers and nested collections.

# For the specific case of numeric arrays representing binary data such as 
# raster images, Python has bytes and bytearray.

#------------------------------------------------------------------------------
# Memory Views
#------------------------------------------------------------------------------

# The built in memoryview class is a shared memory sequence type that lets you
# handle slices of arrays without copying bytes. It was inspired by the NumPy
# library. A memoryview is essentially a generalized NumPy array structure in 
# Python itself (without the math). It allows you to share memory between data
# structures (things like PIL images, SQLite databases, NumPy arrays, etc) 
# without first copying. This is very important for large data sets.

# The memoryview.cast method lets you change the way multiple bytes are read or
# written as units moving bits around (just like the C cast operator). 

# memoryview.cast returns yet another memoryview object, always sharing the 
# same memory. 

import array
numbers = array.array('h', [-2, -1, 0, 1, 2])
memv  = memoryview(numbers)

numbers
memv

len(memv)
memv[1]

# Convert memv to typecode 'B' (unsigned char)....
memv_oct = memv.cast('B')
memv_oct.tolist()

# Change portion of memv_oct and notice how numbers changes.
memv_oct[0] = 255
numbers

#------------------------------------------------------------------------------
# NumPy and SciPy
#------------------------------------------------------------------------------

# NumPy implements multi-dimensional homogenous arrays and matrix types that
# hold not only numbers but also user-defined records and provides efficient
# elementwise operation. 

# SciPy is a library written on top of NumPy that offers scientific computing
# algorithms for linear algebra, numeric calculus, and statistics. SciPy 
# leverages the widely used C and Fortran codebase from the Netlib Repository. 

import numpy
a = numpy.arange(12)
a

type(a)
a.shape()
a.shape = 3, 4
a

a[2]
a[2, :]
a[2, 1]
a[:, 1]

a.transpose()

#------------------------------------------------------------------------------
# Deques and Other Queues
#------------------------------------------------------------------------------

# The .append and .pop methods make list objects usable as a stack or queue. If
# you use .append and .pop() you get LIFO (last in first out) behavior. 

# Inserting and removing from the left-end (front) of the list is costly because
# the entire list must be shifted.

# The collections.deque class is a thread-safe double-ended queue designed for
# fast inserting and removing from both ends. This can be used to keep a list
# (cache) of "last seen items" because the deque can be bounded (created with
# a maximum length, then when the stack is full and an item is added with 
# .append, it automatically discards an item from the opposite end).

from collections import deque

dq = deque(range(10), maxlen = 10)

dq.rotate(3)
dq.append(4)
dq.appendleft(5)
dq.extendleft([10, 11, 12])

# Alternative standard Python packages for implementing queues.
queue
multiprocessing
asyncio
heapq

#------------------------------------------------------------------------------
# Python Sequences Chapter Summary
#------------------------------------------------------------------------------

# Sequences in Python can be categorized in two ways: mutable vs. immutable and
# container sequences vs. flat sequences.

# Container sequences hold references to the objects they contain which may be 
# of any type.

# Flat sequences physically store the value of each item within its own memory
# space. Flat sequences are thus more compact, but they are limited to holding
# primitive atomic values like characters, bytes, and numbers.

# Container sequences: list, tuple, and collections.deque
# Flat sequences: str, bytes, bytearray, memoryview, array.array

# Mutable sequences: list, bytearray, array.array, collections.deque, memoryview
# Immutable sequences: tuple, str, bytes

#==============================================================================
# Chapter 3: Dictionaries and Sets
#==============================================================================

# "Any running Python program has many dictionaries active at the same time, 
# even if the user’s program code doesn’t explicitly use a dictionary.""
# - A.M. Kuchling

# The dict type is a fundamental part of the Python language. The built in 
# dict functions are stored in __builtins__.__dict__. Dicts in Python are
# highly optimized and hash tables are the engine behind this efficient
# implementation.

#------------------------------------------------------------------------------
# Dictionary Basics
#------------------------------------------------------------------------------

# Missing keys in a dictionary produce KeyErrors.

# Searching for whether a value exists in the keys of a dictionary is efficient
# in Python, not matter the size of the dictionary. A search like k in dict.keys()
# returns a view which is similar to a set and then a containment check for the
# set can be run which are very fast.

# In Python, an object is hashable if it has a hash value which never changes
# during its lifetime, it has a __hash__() method, and it has a __eq__() 
# method so that it may be compared to other objects. Hashable objects which
# compare equal must have the same hash value.

# The atomic immutable types (str, bytes, numeric types) are all hashable. A
# tuple is hashable only if its items are hashable.

tup = (1, 2, (20, 30))
hash(tup)

# User defined types/classes are hashable by default.

# A number of different syntaxes can be used to build dictionaries. Equals
# arguments, curly brackets, zipped lists, or a list of tuples.
a = dict(one=1, two=2, three=3)
b = {'one': 1, 'two': 2, 'three': 3}
c = dict(zip(['one', 'two', 'three'], [1, 2, 3]))
d = dict([('two', 2), ('one', 1), ('three', 3)])
e = dict({'three': 3, 'one': 1, 'two': 2})
a == b == c == d == e

# Dictionary comprehension can also be used to build dictionaries.
DIAL_CODES = [(86, 'CHINA'), (91, 'INDIA'), (1, 'UNITED STATES')]

country_code = {country: code for code, country in DIAL_CODES}

#------------------------------------------------------------------------------
# Dictionary Methods
#------------------------------------------------------------------------------

d.clear() # Remove all items
d.__contains__(k) # k in d
d.copy() # Shallow copy
d.__delitem__(k) # del d[k] - remove item with key k
d.fromkeys(it, [initial]) # New mapping from keys in iterable 
d.get(k, [default]) # Get item with key k, return default or None if missing
d.__getitem__(k) # d[k] - get item with key k
d.items() # Get view over items - (key, value) pairs
d.__iter__() # Get iterator over keys
d.keys() # Get view over keys
d.__len__() # Number of items - len(d)
d.__missing__(k) # Called when __getitem__ cannot find the key
d.move_to_end(k, [last]) # Move k to first or last position (last by default)
d.pop(k, [default]) # Remove and return value at k 
d.popitem() # Remoge and return an arbitrary (key, value) item
d.setdefault(k, [default]) # If k in d, return d[k], else set d[k] to default
d.__setitem__(k, v) # d[k] = v, put v at k
d.update(m, [**kargs]) # Update with items from mapping or iterable of (key, value) pairs
d.values() # Get view over values

#------------------------------------------------------------------------------
# Dictionary Variations
#------------------------------------------------------------------------------

collections.OrderedDict # Instead of being unordered, keys are ordered which 
# allows iteration over the keys in a predictable order, also allows for the
# usage of pop.

collections.Counter # Holds an integer count for each key, updating an existing
# key adds to its count. Can be used to count instances of a hashable object.

#------------------------------------------------------------------------------
# Set Theory
#------------------------------------------------------------------------------

# A set is a collection of unique objects, a basic use case is removing
# duplication.
l = ['spam', 'spam', 'eggs', 'spam']
set(l)
list(set(l))

# Set elements must be hashable.
# The set type also implements infix operators so given two sets a and b, 
# a | b returns the union of the sets, a & b computes the intersection of the
# sets, and a - b calculates the difference.
a = set([1, 2, 4, 5, 6])
b = set([2, 4, 7, 8, 9])

a & b
a | b
a - b

# Sets can add clarity and save computation time. Consider if you have a list
# of email addresses (the haystack) and a set of emails you are interested in
# (the needles). If you want to figure out how many of the needles exist in the
# haystack, you could iterate through the whole haystack with a for loop and 
# count the number of instances, but it would be clearer and faster to leverage
# set operation to find the number of overlaps.
found = len(needles & haystack)
found = len(set(needles).intersection(haystack)) # Alternative code.

# The extremely fast membership test is powered by the hash table that underlies
# the set.

# Set syntax. 
# Sets can be constructed using {}.
y = {1, 4, 5, 6, 7}

# Set Comprehension.
# Sets can also be constructed using comprehension.
myset = {x for x in range(10)}

#------------------------------------------------------------------------------
# Set Methods
#------------------------------------------------------------------------------

s & z 
s.__and__(z) # Intersection of s and z

s &= z 
s.__iand__(z) # s updated with the intersection of s and z

s | z
s.__or__(z) # Union of s and z

s |= z
s.__ior__(z) # s updated with the union of s and z 

s - z
s.__sub__(z) # Difference between sets s and z

s -= z 
s.__isub__(z) # s updated with the difference between s and z

s ^ z 
s.__xor__(z) # Symmetric difference (the complement of the intersection of s & z)

e in s
s.__contains__(e) # Element e is a member of s

s <= z 
s.__le__(z) # s is a subset of the z set

s < z
s.__lt__(z) # s is a propoer subset of the z set

s >= z 
s.__ge__(z) # s is a superset of the z set

s > z 
s.__gt__(z) # s is a proper superset of the z set

s.pop() # Remove and return an element from set s
s.remove(e) # Remove element e from set s

#------------------------------------------------------------------------------
# Hash Tables in Dictionaries
#------------------------------------------------------------------------------

# The following is a set of high level notes on how hash tables are used in 
# Python dictionaries. A hash table is a sparse array (an array with cells that
# are always empty). The cells in a hash table are called "buckets". In a 
# dictionary hash table there is a bucket for each item and it contains two fields, 
# a reference to the key and a reference to the value of the item. Because all 
# buckets have the same size, access to an individual bucket is done by offset. 

# Python tries to keep 1/3 of the buckets empty, if the hash table becomes too 
# crowded it is copied to a new location with room for more buckets.

# To put an item in a hash table, the first step is to calculate the hash value 
# of the item key which is done with the built-in hash() function.

# The hash() function works with built-in Python types and calls __hash__. If two 
# objects compare equal, then they must produce the same hash.
hash(1) == hash(1.0)

# Hash values should also scatter around the index space as much as possible. 
# Ideally, objects that are similar but not equal should have hash values that
# differ widely.
hash(1.0001)
hash(1.0002)

# The hash table algorithm.
# To fetch the value at mydict[search_key], Python calls hash(search_key) to 
# obtain the hash value of the search_key and uses the least significant bits
# of that number as an offset to look up a bucket in the hash table (the number
# of bits used depends on the current size of the table). If the found bucket
# is empty than KeyError is raised, if not then Python checks whether
# found_key == search_key and if so found_value is returned. However, if 
# search_key and found_key do not match than a hash collision has occurred and
# a larger number of bits from the hash is used to search again until the correct
# key is found.

# As the hash table grows, so does the number of hash bits used as bucket offsets
# which keeps the collision rate low.

#------------------------------------------------------------------------------
# Consequences of how Hash Tables Work
#------------------------------------------------------------------------------

# Keys must be hashable objects.

# Dictionaries have significant memory overhead.
# Because dictionaries use hash tables internally and hash tables are sparese,
# they are not space efficient. 

# Key search is very fast.
# Dictionaries are an example of the classic tradeoff between space and time.
# Hash tables are not space efficient, but lookup is very fast.

# Adding items to a dictionary may change the order of existing keys.
# The Python interpreter may decide at any time to build a bigger hash table 
# which can result in the order of existing keys in the dictionary changing. For
# this reason it is a bad idea to modify the contents of a dictionary while 
# iterating through it. 

# Implications are similar for sets:
# Set elements must be hashable.
# Sets have significant memory overhead.
# Membership testing is very efficient. 
# Adding elements to a set may change the order of the set.

#==============================================================================
# Chapter 4: Text versus Bytes
#==============================================================================

# "Humans use text, computers speak bytes."

# Python 3 introduced a sharp distinction between strings of human text and
# sequences of raw bytes. Implicit conversion of byte sequences to Unicode
# text is a thing of the past. This chapter covers Unicode strings, binary
# sequences, and the encodings used to convert between them.

#------------------------------------------------------------------------------
# Character Issues
#------------------------------------------------------------------------------

# The concept of a string is simple: a string of characters. However, the concept
# of character is harder to define. Python 3 uses Unicode characters, and the 
# Unicode standard explicitly seperates the identity of characters from specific
# byte representations.

# The identity of a character (its code point) is a number from 0 to 1,114,111
# shown in the Unicdoe standard as 4 to 6 hexadecimal digits with a "U+" prefix.

# The code point for the letter A is U+0041 and the music symbol G clef is 
# assigned to the code point U+1D11E. Only 10% of valid code points in the 
# Unicode architecture currently have characters assigned to them. 

# The actual bytes that represent a character depend on the encoding in use. An 
# encoding is an algorithm that converts code points to byte sequences and vice
# versa. 

# To encode and decode characters.
s = 'cafe'
b = s.encode
b'caf\xc3\xa9'
b.decode('utf8')

#------------------------------------------------------------------------------
# Byte Essentials
#------------------------------------------------------------------------------

# Python 3 has two main byte structures, the bytes type which is immutable and
# the bytearray type which is mutable. 

# Each item in bytes or bytearray is an integer from 0 to 255.
cafe = bytes('café', encoding='utf_8')
print(cafe)
cafe[0]
cafe[1]

cafe_arr = bytearray(cafe)

# Although binary sequences are really sequences of integers, their printed notation
# as a string with characters in it reflects the fact ASCII characters are often
# embedded in the sequence of integers. However, if you slice the byte object
# you will see the actual integer.
print(cafe)
cafe[0]

#------------------------------------------------------------------------------
# Structs and Memory Views
#------------------------------------------------------------------------------

# The struct module provides functions to parse packed bytes into a tuple of 
# fields of different types and perform the opposite conversion. 

# The memoryview class provides shared memory access to slices of data from other
# binary sequences, packed arrays, and buffers without copying the bytes. Slicing
# a memoryview returns a new memoryview. 
memoryview(cafe)

#------------------------------------------------------------------------------
# Basic Encoders / Decoders
#------------------------------------------------------------------------------

# The Python distribution bundles more than 100 encoders/decoders for text to 
# byte conversion and vice versa. Each encoder has a name like 'utf_8' and often
# aliases such as 'utf8' and 'U8' which can be used as the decoding argument in
# functions like open(), str.encode(), bytes.encode() and so on. 

# The following example shows a string encoded with three different encoders, 
# each producing a different byte sequence.
for codec in ['latin_1', 'utf_8', 'utf_16']:
    print(codec, 'El Niño'.encode(codec), sep='\t')

# utf-8 is the most common 8 bit encoding on the Web by far and is backward
# compatible with ASCII. utf-8 is widely deployed on GNU/Linux and OSX 
# operating systems. 

city = 'New York'
city.encode('utf_8')

# When handling text files, the best practice is to decode from bytes to 
# strings as early as possible (i.e. when opening a file for reading), process 
# the text exclusively using strings, then encode strings to bytes on output.

# The .read() and .write() methods in Python take care of this workflow.

# The most important encoding setting is that returned by locale.getpreferredencoding():
# it is the default for opening text files and for sys.stdout/stdin/ stderr when 
# they are redirected to files.

# In general the world is adopting Unicode, 80% of websites already use UTF-8.
# UTF-8 encodes Unicode characters into a sequence of 8-bit bytes.

# Find the default encoding of your Python instance.
import sys
sys.getdefaultencoding() 

# ASCII is the standard for representing text in computers, but it only contains
# 95 printable characters.

# How strings are represented in RAM: in memory Python 3 stores each string as 
# a sequence of code points using a fixed number of bytes per code point to allow
# efficient direct access to any character or slice. If a string contains only 
# characters that are in the latin1 range (a, b, c, d, etc) then that string 
# will use just one byte of memory per code point (character). Otherwise, 2 or
# 4 bytes per code point may be used.

#==============================================================================
# Chapter 5: First Class Functions
#==============================================================================

# Functions in python are first class.
# - Created at runtime.
# - Assigned to a variable or element in a data structure.
# - Passed as an argument to a function.
# - Returned as the result of a function.

# For example, you can assign functions to variables.
def printInt(X):
    print(X)

newFunctionName = printInt

#------------------------------------------------------------------------------
# Higher-Order Functions
#------------------------------------------------------------------------------

# One of the hallmarks of functional programming is the use of higher-order
# functions. A function that takes a function as an argument and returns a 
# function is a higher order function. 

# Examples of higher order functions are:
map 
sorted 

# In functional programming, some of the best known higher order functions are:
# map, reduce, apply, filter

# The apply functions was deprecated in python 3 because instead of writing:
apply(fn, args, kwargs)

# You can just write:
fn(*args, **key, word)

# Map, filter, and reduce higher order functions are still available, but there
# are better alternatives for most use cases. 

# List comprehension generally does the job of map or filter with a cleaner 
# syntax. Example, calculating the factorials of 0 to 6.
list(map(Factorial, range(6)))
[Factorial(n) for n in range(6)]
 
#------------------------------------------------------------------------------
# Anonymous Functions
#------------------------------------------------------------------------------

# The lambda keyword is used to create anonymous functions in python.
# The syntax of python limits the body of lambda functions to be pure expressions:
# the body of a lambda statement cannot make any assignments or use any other
# python statements such as while, or try.
sorted(fruits, key=lambda, word: word[::-1])

add = lambda x, y: x + y

#------------------------------------------------------------------------------
# The Seven Flavors of Callable Objects
#------------------------------------------------------------------------------

# The call object () may be applied to other objects beyond user-defined functions.
# To determine if an object is callable use the callable() built-in function.

# The Python data model lists 7 callable types:

# User defined functions.
# - Created with def or lambda statements.

# Built-in functions.
# - Methods implemented in C like dict.get.

# Methods.
# - Functions defined in the body of a class.

# Classes
# - When a class runs its __new__ method is used to create an instance and then
# __init__ is used to initialize it, finally, the instance is returned to the
# caller. Because there is no new operator in python, calling a class is like
# calling a function. 

# Class instances
# - If a class defines a __call__ method then its instance may be invoked as 
# a function.

# Generator functions
# - Functions or methods that use the yield keyword. When called, generator
# functions return a generator object. 

#------------------------------------------------------------------------------
# User-defined Callable Types
#------------------------------------------------------------------------------

# Not only are python functions real objects, but arbitrary python objects can
# be made to behave like functions by implementing a __call__ instance. 

import random 

class BingoCage:

    def __init__(self, items):
        self._items = list(items) 
        random.shuffle(self._items)

    def pick(self): 
        return self._items.pop() 

    def __call__(self):
        return self.pick()

# In the above example, running the command bingo() will produce the same result
# as running the command bingo.pick() because using call we have created a shortcut
# from self to self.pick().

# A class that implements call() is an easy way to create function-like objects
# that are able to carry some internal state. An example of this is a decorator,
# decorators must be functions but it is sometimes convenient to remember things
# between function calls (memoization).

# In the above example, as items are popped off the list with bingo() the object
# will remember what has been popped off of the list.

#------------------------------------------------------------------------------
# Function Introspection
#------------------------------------------------------------------------------

# Functions have many attributes beyond __doc__ which can be seen with the 
# command dir(function). However, user-defined functions have some unique
# attributes.
__annotations__ # dict, parameter and return annotations
__call__ # method wrapper, implementation of the () operator, aka the callable object protocol
__closure__ # tuple, the function binding for free variables (often None)
__code__ # code, function metadata and function body compiled into bytecode
__defaults__ # tuple, default values for the formal parameters
__get__ # method-wrapper, implementation of the read-only descriptor protocol
__globals__ # dict, global variables of the module where the function is defined
__kwdefaults__ # dict, default values for the keyword only formal patterns
__name__ # str, the function name
__qualname__ # str, the qualified function name

# __defaults__, __code__, and __annotations__ will be described later. 

#------------------------------------------------------------------------------ 
# Function Annotations
#------------------------------------------------------------------------------

def clip(text:str, max_len:'int > 0'=80) -> str:

# Each argument in the function may have an annotation expression preceded by :
# and the return value can be annotated with ->

# No processing is done with the annotations, they are merely stored in 
# __annotation__ and can be accessed

#------------------------------------------------------------------------------
# Packages for Functional Programming in Python 
#------------------------------------------------------------------------------

# Even though Python is not a functional programming language, there is 3rd party
# support for writing in a functional manner. An example of this the implementation
# of arithmetic operators in packages like functools and operator which allows
# for writing in a functional style. 

# For example, say you want to calculate factorials without using recursion:
from functools import reduce
from operator import mul

def fact(n):
    return reduce(mul(range(1, n+1)))

#------------------------------------------------------------------------------
# Chapter Summary
#------------------------------------------------------------------------------

# You can assign functions to variables, pass them to other functions, store
# them in data structures, access function attributes, and allow frameworks
# and tools to act on that information. 

# Higher order functions, a staple of functioal programming are common in Python.
# - for example: map, filter, reduce, sum, all any

# Callables come in many flavors and can be created with def, lambda, and __call__.

# Is Python a functional language? Even if not intended, by endowing Python with
# first class functions, Python opened the door to functional programming.

# map, filter, and reduce were motivation for adding lambda to Python.
# - map, filter, and reduce first appeared in Lisp which was the original 
# functional programming language, however, Lisp does not limit what can be 
# done inside a lambda statement because everything in Lisp is an expression. 
# - Python instead uses a statement oriented syntax in which expressions cannot
# contain statements. 

# Ironically, stealing the list comprehension syntax from another functional 
# language - Haskell - significantly diminishes the need for map, filter, and lambda.

# In conclusion, Python is not a functional language, it just borrows good ideas
# from functional languages. 

#==============================================================================
# Chapter 7: Function Decorators and Closures
#==============================================================================

# "There’s been a number of complaints about the choice of the name “decorator” 
# for this feature. The major one is that the name is not consistent with its use
# in the GoF book. The name decorator probably owes more to its use in the compiler
# area — a syntax tree is walked and annotated." - PEP 318

# Function decorators let us "mark" functions in the source code to improve their
# behavior in some way. This is powerful, but it requires understanding closures.

#-------------------------------------------------------------------------------
# Decorators 101
#-------------------------------------------------------------------------------

# A decorator is a callable that takes another function as an argument (the 
# function that is being decorated). The decorator may perform some processing
# with the decorated function, and returns it or replaces it with another
# function or callable object.

# Assuming an existing decorator named @decorate:
@decorate
def target():
    print("running target()")

# Has the same effect as:
def target():
    print("running target()")

target = decorate(target)

# The end result is the same, after the @decorator is attached the function 
# target does not refer to the original target function, but to whatever function
# is returned by decorate(target). 

# Strictly speaking, decorators are just syntactic sugar. 

# A key feature of decorators is that they run right after the decorated function
# is defined. That is usually at import time when a module is loaded into Python.

#-------------------------------------------------------------------------------
# Variable Scope Rules
#-------------------------------------------------------------------------------

# Consider the following scoping example:
b = 6
def f2(a):
    print(a)
    print(b)
    b = 9 

f2(3)
# >>> error 

# The error here is surprisingly because even though b is defined in the function
# after print(b) is called, b is defined as a global variable above the function.

# This occurs because when Python compiles the body of the function, it decides
# that b is a local variable because it is assigned within the function. The
# generated bytecode reflects that and it will try to grab b from the local environment.

# In fact, if we had not defined b = 9 inside the function than it would have
# run without issue. 

# This is a design choice: Python does not require you to declare variables but
# it assumes that a variable assigned in the body of a function is a local variable. 

# If we wanted the function to treat b as a global variable, this can be done
# with a global declaration.
def f3(a):
    global b
    print(a)
    print(b)
    b = 9

# To compare the difference in bytecode between functions above, the dis module
# provides an easy way to disassemble the bytecode of Python functions.
from dis import dis
dis(f1)

#-------------------------------------------------------------------------------
# Closures
#-------------------------------------------------------------------------------

# Closures are often confused with anonymous functions. The reason is historic:
# defining functions inside functions is not common until you start using 
# anonymous functions, and closures only matter when you have nested functions.

# A closure is a function with an extended scope that encompasses nonglobal
# variables referenced in the body of the function but not defined there.
# - It does not matter whether the function is anonymous or not, what matters
# is that it can access nonglobal variables defined outside of its body.

# Consider an avg function used to computed the mean of an ever-increasing
# series of values. For example, consider a series of commodity prices that grows
# every day. 

# We first create a class to calculate a running average.
class Averager():

    def __init__(self):
        self.series = []

    def __call__(self, new_value):
        self.series.append(new_value)
        total = sum(self.series)
        return total / len(self.series)
    
avg = Averager()
avg(10)
avg(11)
avg(12)

# Now consider implementing the same averager in a functional manner.
def make_averager():
    series = []

    def averager(new_value):
        series.append(new_value)
        total = sum(series)
        return total / len(series)

# When invoked, make_averager() returns an averager function object. Each time
# an averager is called it appends the passed argument to the series and computes
# the current average. 
avg = make_averager()
avg(10)
avg(11)
avg(12)

# In both the Averager class and make_averager() function approach a callable object named
# avg is created that will update the historical series and return the mean.

# It is obvious in the Averager class where the history is kept: self.series, 
# but where does the avg function in the second example find the series?

# Note* that series is a local variable of make_averager() because of the 
# initialization series = [] happens in the body of the function.
# - Within averager, series is a free variable, meaning that it is not bound
# by the local scope.

# To summarize: a closure is a function that retains the binding of the free
# variables that exist when the function is defined, so that they can be used
# later when the function is invoked and the defining scope is no longer 
# available. 
# - Note that this only occurs with nested functions.

#-------------------------------------------------------------------------------
# The nonlocal Declaration
#-------------------------------------------------------------------------------

# The previous implementation of the recursive function call make_averager() is
# inefficient as all values of the historical series are stored. It would be
# more efficient to simply store the sum and the number of items in the series,
# and then compute the mean from this information. 

# However, this is not possible with the current implementation because the
# series variable is local to the make_averager() function.

# To fix this, we can use the nonlocal declaration to indicate that the variable
# is not local.

def make_averager():
    count = 0
    total = 0

    def averager(new_value):
        nonlocal count, total
        count += 1
        total += new_value
        return total / count

#-------------------------------------------------------------------------------
# Memoization with functools.lru_cache()
#-------------------------------------------------------------------------------

# Left off on p. 200







