
#-------------------------------------------------------------------------------
# Julia Fundamentals
#-------------------------------------------------------------------------------

# To download a package.
using Pkg
Pkg.add("LinearAlgebra")
Pkg.add("BenchmarkTools")

#-------------------------------------------------------------------------------
# Julia Unique Features
#-------------------------------------------------------------------------------

# Julia has Unicode support.
π
√4

g(x,y) = √(x^2 + y^2)
g(3,4)

# Julia also allows for multiple comparison.
h(x) = begin
	if 0 < x < 3 # In many languages 0 < x < 3 would be an error.
		return x
	else
		return 0
	end
end

h(2)
h(-2)

# Julia also allows for function composition. The following command combines
# the functions g and h.
hg = h ∘ g

g(3,4) # Returns 5, so hg(3,4) will return 0 because 0 < 5 < 3 is false.
hg(3,4)

# Numeric coefficients may also be used to denote multiplication.
d = 2; e = 2d

# Julia also features a ternary operator which functions as a condensed ifelse.
3 < 4 ? "This is true" : "This is false"

# Julia supports expressions and macros. The quote command encapsulates the following
# block of text which can later be used as a variable or evaluated with eval.
q = quote
	if 3 < 4
		return true
	else
		return false
	end
end;

typeof(q)
eval(q)

# macros can be encapsulated with @, one example of this is a timing macro used
# to time the execution of a block of code.
begin
	inv([[1,0] [0,1]])
	@elapsed inv(rand(1000,1000))
end

# Note* that because Julia is a compiled language, the first time a function is
# called it will take longer to execute than subsequent calls.

# Multiple Dispatch:
# Julia allows for multiple dispatch, which means that a function can have
# multiple definitions depending on the types of the arguments. For example
# the inbuilt * operator can be used to multiply two numbers, two matrices,
# or two strings.
3 * 4
"3" * "4"
[1 2; 3 4] * [1 2; 3 4]

# You can also define your own functions with multiple dispatch. For example
# the following checks if the input is 4, regardless of whether it is an 
# integer or a string.

# Generic function with just one defined method.
isfour(x::Number) = (x == 4)

# Generic function now with two defined methods.
isfour(x::String) = (lowercase(x) ∈ ["4", "four"])

isfour(4)
isfour("four")


#-------------------------------------------------------------------------------
# Efficiency in Julia
#-------------------------------------------------------------------------------

# The goal of Julia is to be able to compile to machine code as quickly as C.

# Additionally, Julia has many inbuilt ways to exploit knowledge of properties 
# of the variables in order to increase the speed of execution such as with
# common linear algebra operations.

# Linear algebra specific operations are contained in the LinearAlgebra package,
# and differences in speed can be compared using the BenchmarkTools package.
using LinearAlgebra, BenchmarkTools

# As a demonstration of linear algebra optimization, compare the speed of computing
# the eigenvalues of a random matrix versus a random Hermitian matrix (Hermitian
# matrices have properties which can be exploited for speed).

# Eigenvalues of a 1000 x 1000 random matrix.
@belapsed eigvals(rand(ComplexF64,1000,1000))

# Eigenvalues of a 1000 x 1000 random Hermitian matrix.
@belapsed (eigvals ∘ Hermitian ∘ rand)(ComplexF64,1000,1000)











