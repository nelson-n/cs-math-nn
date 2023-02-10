
import random

# Generate a random list of numbers.
def generateRandomList(n):
    return [random.randint(0, 100) for i in range(n)]

randomList = generateRandomList(int(1e7))

