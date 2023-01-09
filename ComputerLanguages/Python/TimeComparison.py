
#-------------------------------------------------------------------------------
# TimeComparison.py
#-------------------------------------------------------------------------------

# General script for comparing the speed to various Python functions. 

import time

# General timer function that takes in a function and computes the computation
# time using perf_counter() which uses relative clock time.
def timer(function):

    start = time.perf_counter()
    function()
    end = time.perf_counter()

    elapsed = end - start
    print("Elapsed Time: " + str(elapsed))
    return elapsed

#===============================================================================
# Comparing the Speed of Lookup Using the dict, set, and list Classes
#===============================================================================

import random

# Generate a haystack of size `num_floats` and a list of needles of size 
# `num_needles`. Half of the needles exist and the haystack and half do not.
# Then compare the speeds of various methods for finding the needles in the haystack.
num_floats = int(1e6)
num_needles = int(1e3)

# Generate double precision floats.
floats = [random.random() for i in range(num_floats)]

# Randomly select needles in the float list and generate needles outside of the
# float list to create final needles list.
needles_pos = [random.randint(0, num_floats) for i in range(int(num_needles/2))]
needles_in = [floats[needle_pos] for needle_pos in needles_pos]
needles_out = [random.random() for i in range(int(num_needles/2))]
needles = needles_in + needles_out
random.shuffle(needles)

## Lookup using a simple for loop.
def for_lookup():
    found = 0
    for n in needles:
        if n in floats:
            found += 1

timer(for_lookup)

## Lookup using standard list comprehension.
def comprehension_lookup():
    sum([needle in floats for needle in needles])

timer(comprehension_lookup)

## Lookup using a set.
floats_set = set(floats)
needles_set = set(needles)

def set_lookup():
    len(floats_set & needles_set)

timer(set_lookup)

## Lookup using a dictionary.
floats_dict = {floats[i] : i for i in range(0, len(floats))}

def dict_lookup():
    sum([needle in floats_dict for needle in needles])

timer(dict_lookup)
