# Math

## Notes

`MathNotes.ipynb`
* Notes on assorted topics in math covering:
    * Math Fundamentals
        * Theorems and Approximations
        * Combinatorics/Permutations
    * Random Walk Processes
    * Brownian Motion
    * Geometric Brownian Motion
    * Itô's Lemma

`LinearAlgebra.ipynb`
* Notes on linear algebra covering:
    * Linear Algebra Notation
    * Central Theory of Linear Algebra
    * Vector Basics
    * Matrix Basics
    * Linear Transformations
    * Span and Linear Independence
    * Dot Product
    * Cross Product
    * Determinants
    * Matrix Multiplication
    * Systems of Linear Equations
    * Eigenvalues, Eigenvectors
    * Covariance Matrix
    * Spectral Theorem

`Calculus.ipynb`
* Notes on calculus covering:
    * General Definitions
    * Calculus Notation
    * Derivative Rules
    * Common Derivatives
    * Taylor Series

## Exercises

`/Lean/SimpleDemo.lean`

The simplest possible Lean 4 proof: 2 + 2 = 4 by `rfl`. Check it with `lean SimpleDemo.lean`; no output means the proof is correct.

`/Lean/LeanDemo.lean`

Introductory demo of the Lean 4 theorem prover using only Lean core (no Mathlib). Check it with `lean LeanDemo.lean`, or open it in VS Code with the lean4 extension. Covers:
* Evaluating expressions and checking types (#eval, #check).
* Defining functions by pattern matching and recursion.
* Proofs by computation (rfl, decide).
* Propositional logic in term mode and tactic mode.
* Existential statements.
* Proofs by induction.
* Exercises left as `sorry`.
