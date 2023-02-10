
# Recursively compute factorials.
factorial <- function(n) {
  if (n == 0) {
    return(1)
  } else {
    return(n * factorial(n - 1))
  }
}

for (i in 1:100) {
  factorial(i)
}
