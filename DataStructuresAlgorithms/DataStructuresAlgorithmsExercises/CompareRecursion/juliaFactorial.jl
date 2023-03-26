
# Recursively compute factorials:
factorial(n) = n == 0 ? 1 : n * factorial(n-1)

for i in 1:100
    factorial(i)
end
