
#!/bin/bash

echo
echo 'Python'
time python3 pythonFactorial.py

echo
echo 'R'
time Rscript RFactorial.R

echo
echo 'Julia'
time julia juliaFactorial.jl

echo
echo 'Java'
time java javaFactorial

echo
echo echo 'C'
time ./CFactorial

echo
echo echo 'C++'
time ./CppFactorial.out

echo
