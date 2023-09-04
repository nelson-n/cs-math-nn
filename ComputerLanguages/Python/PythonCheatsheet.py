
#------------------------------------------------------------------------------
# Random Notes
#------------------------------------------------------------------------------

# Python Built-in Collections
# List is a collection which is ordered and changeable. Allows duplicate members.
# Tuple is a collection which is ordered and unchangeable. Allows duplicate members.
# Set is a collection which is unordered, unchangeable*, and unindexed. No duplicate members.
# Dictionary is a collection which is ordered** and changeable. No duplicate members.

# Augmented assignment operators.
val += 1
val -= 1
val *= 1
val /= 1
val %= 1

# Various division options.
9/2 = 4.5 # True division, returns a float.
9//2 = 4  # Floor division, returns an int.
9%2 = 1   # Remainder, returns an int.

# Lambda functions.
add = lambda x, y: x + y

# Find the factorial of a number.
import math
math.factorial(3)

# Print the entirety of a dataframe.
with pd.option_context('display.max_rows', None, 'display.max_columns', None): 
    print(df)

#------------------------------------------------------------------------------
# Class Construction
#------------------------------------------------------------------------------

class Person:

  def __init__(self, name, age):
    self.name = name
    self.age = age

person1 = Person("John", 36)
print(person1.name)

#------------------------------------------------------------------------------
# Loops
#------------------------------------------------------------------------------

# Loop statements.
# while: Run whilst a condition is true.
# break: Exit the loop before completion.
# continue: Stop the current iteration of the loop and continue with the next iteration.
# else: Block of code to be executed when the loop finishes. Will not execute if a
     # break statement is reached.
# pass For loops cannot be empty, but if you have a for loop with no content, the
     # pass statement will stop an error from triggering.
# elif: Checks the next condition after if but before else.

# While and break statements, if a break statement is reached the loop ends.
counter = 0

while counter < 10:

    print(str(counter))
    counter = counter + 1

    if counter == 8:
        break

# Run an infinite loop until a breaking statement is met.
while True:
    if x:
        break

# Enumerate using an index and value.
for i, val in enumerate(mylist):
	...

#------------------------------------------------------------------------------
# Regex and String Operations
#------------------------------------------------------------------------------

string = "The rain in Spain"

# Split a string into a list of characters.
chars_list = [*string]

# Split a string into a list with every nth character.
[line[i:i+n] for i in range(0, len(line), n)]

# Collapse a string of split characters.
string = ''.join([chars for chars in chars_list])

# Index characters by position.
string[:3]

# Find the first index of a specified string, is the string does not exist
# -1 is returned.
string.find('rain')

# Regex commands.
import re

# Find indices of the first match. If 'rain' occurs multiple times in the string,
# only the first instance is returned. If there is no match then None is returned.
match = re.search('rain', string)

match.start()
match.span()
match.end()

# Find all occurences of a substring in a string.
re.findall('ai', string)

# Find the location of the first space.
re.search('\s', string).start()

# Split the string at each match, in this case at each space.
re.split('\s', string)

# Split the string, only at the first 2 occurences.
re.split('\s', string, 2)

# Replace the first two spaces with double spaces.
re.sub(' ' , '  ', string, 2)

#------------------------------------------------------------------------------
# Lists
#------------------------------------------------------------------------------

# List is a collection which is ordered and changeable. Allows duplicate members.

mylist = ['first', 'second', 'third', 'fourth', 'fifth', 'sixth']
mylist2 = [6, 2, 4, 3, 1, 5]

# --- Indexing Lists ---

# Indexing with pythonic 0 index.
mylist[:2] # first two items
mylist[2] # third item
mylist[2:] # third item to last item
mylist[-2] # second to last item
mylist[-2:] # second to last item to last item

# When indexing from the front (x), you must subtract 1, i.e to index the third
# item in the list it is mylist[2]. When indexing from the back (y) values are
# as normal. For example, to index the third and fourth item in the list is
# mylist[2:4]. 
mylist[x:y] 
mylist[2:4]

# Some features of 0 indexing:
# range(3) and mylist[:3] both return exactly 3 items.
# Lists can be split into two parts without overlap: mylist[:x] and mylist[x:]
# The length of a slice can be computed as stop - start: mylist[2:4] returns 4-2=2 items.

# Index last item.
mylist[-1]

# Index first two items.
mylist[:2]

# Index all but last two items.
mylist[:-2]

# Index third item to last item.
mylist[2:]

# Index third and fourth item.
mylist[2:4]

# Index odds or evens (every other item) starting at index 0 or 1.
mylist[0::2]
mylist[1::2]

# Reverse the list.
mylist[::-1]

# Find position of an item within a list.
mylist.index('third')

# --- Appending and Removing from a List ---

# Update a value in a list.
mylist[2] = 'third'

# Append value to end of list.
mylist.append('sixth')

# Insert value to the front a list.
mylist.insert(0, 'zero')

# Pop value off of the end of a list.
mylist.pop()

# Pop value off of the front of a list.
mylist.pop(0)

# Delete from list based on index.
del mylist[2:4]

# Remove from list based on value.
mylist.remove('sixth')

# --- Filtering Lists ---

mylist = [10, 20, 30, 40, 50]

# For loop approach.
filtered = []

for val in mylist:
    if val >= 30:
        filtered.append(val)

# Filter with lambda expression.
filtered = filter(lambda val: val >= 30, mylist)
list(filtered)

# Filter with list comprehension.
[val for val in mylist if val >= 30]

# Filter with a function.
def val_filter(val):
    if val >= 30: 
        return True
    else: 
        return False

filtered = filter(val_filter, mylist)
list(filtered)

# Filter based on a boolean list.
filterlist = [val >= 30 for val in mylist]
[i for i, j in zip(mylist, filterlist) if j == True]

# --- Creating Numeric Lists ---

# Create list from 0 to 9. The reason it only goes up to 9 is because the range
# function returns a list with 10 items.
list(range(10))

# Create a list from 1 to 10.
list(range(1, 10+1))

# Create a list of even numbers from 1 to 10.
list(range(2, 10+1, 2))

# Create a list from 10 to 1.
list(range(10, 1-1, -1))

# --- Random ---

# Reverse a list.
mylist.reverse()

# Concatenate lists.
list1 + list2

# Repeat a list x times.
list1 * 4

# Initiate a blank list of 4 zeroes.
[0] * 4

# Compare items in two lists.
list1 = [1, 2, 3, 4, 5]
list2 = [4, 2, 4, 4, 2]

set(list1).intersection(list2)
[i for i, j in zip(list1, list2) if i == j]

# Find the max or min in a list.
min(list1)
max(list1)

# Count the number of times an object occurs in a list.
list2.count(4)

# Zips and sorts.
mylist = ['first', 'second', 'third', 'fourth', 'fifth', 'sixth']
mylist2 = [6, 2, 4, 3, 1, 5]

# Return a sorted list.
sorted(mylist2)

# Sort values from largest to smallest in the mylist2 (i), then sort the
# values in mylist (j) based on the same order.
[j for i, j  in sorted(zip(mylist2, mylist), reverse=True)]

# Zip two lists together.
zipped = zip(mylist, mylist2)

# Turn zipped object into a list of tuples.
list(zipped)

# Unzip a zip object.
mylist, mylist2 = zip(*zipped)

# Sort list from lowest to highest. Modifies in place.
mylist2.sort()

# Sort list from highest to lowest. Modifies in place.
mylist2.sort(reverse=True)

# Find the unique values in a list by converting to a set.
mylist_unique = list(set(mylist))

# Create a shallow copy of a list, modifying values in the original list will
# not modify the copied list, but modifying child objects in the original list 
# (such as a list of lists) will modified child values in the copied list.
mylist_copy = mylist.copy()

# Create a deepcopy of a list. Modifying values in the original list will not
# modified the copied list or its child objects.
import copy
mylist_deepcopy = copy.deepcopy(mylist)

#------------------------------------------------------------------------------
# Sets
#------------------------------------------------------------------------------

# Set is a collection which is unordered, unchangeable*, and unindexed. No duplicate members.

# Initializing a set.
s = {1, 2, 3}
s = set([1, 2, 3])

# Set union, intersection, and difference.
s1 = {1, 2, 3, 3}
s2 = {3, 4, 5}

s1.union(s2)
s1.intersection(s2)
s1.difference(s2)

# Set comprehension.
{val + 1 for val in s1}
{val for val in s1 if val != 3}

#------------------------------------------------------------------------------
# Dictionaries 
#------------------------------------------------------------------------------

# Dictionary is a collection which is ordered and changeable. No duplicate members.
mydict = {
	'one': 1,
	'two': 2,
	'three': 3
}

# Access dictionary item via a key.
mydict["three"]

# Return the keys of the dictionary.
mydict.keys()

# Update existing dictionary entry.
mydict['three'] = 3

# Add new entry to the dictionary.
mydict['four'] = 4

# Remove dictionary entry.
del mydict['four']

# Remove all entires in a dictionary.
mydict.clear()

# Delete entire dictionary.
del mydict
