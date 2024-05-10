
# Function for finding the sum of odd numbers in a list:
def sum_odd_numbers(numbers):
    total = 0
    for number in numbers:
        if number % 2 != 0:
            total += number
    return total

# Generate a list of integers:
numbers = range(1, 5001)

sum_odd_numbers(numbers)

#===============================================================================
# Assorted Problems
#===============================================================================

import math

def sigmoid(x):
    return 1 / (1 + math.exp(-x))

sigmoid(-10) * (1 - sigmoid(-10))
math.exp(-10) / (1 + math.exp(-10))**2 < 0.0001

round(sigmoid(-1) * (1 - sigmoid(-1)), 2)
math.exp(-1) / (1 + math.exp(-1))**2 

sigmoid(0) * (1 - sigmoid(0))
math.exp(0) / (1 + math.exp(0))**2 

sigmoid(1) * (1 - sigmoid(1))
math.exp(1) / (1 + math.exp(1))**2 

sigmoid(10) * (1 - sigmoid(10))


round(1 - ((1 - (1/100000))**10000), 3)

(1/100000)**10000

(1/100000)**10000





