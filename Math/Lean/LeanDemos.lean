
-- lean Math/Lean/LeanDemos.lean

#eval 2 + 2
#check 2 + 2

--==============================================================================
-- Derivative of a Polynomial
--==============================================================================

-- Represent a polynomial by its list of coefficients, lowest power first:
--   [5, 3, 2]  means  5 + 3x + 2x²
abbrev Poly := List Int

-- Evaluate a polynomial at x (Horner's method): a₀ + x(a₁ + x(a₂ + ...)).
def Poly.eval (p : Poly) (x : Int) : Int :=
  p.foldr (fun c acc => c + x * acc) 0

-- Power rule: the term cₙxⁿ becomes n·cₙxⁿ⁻¹. Walk the list, multiplying each
-- coefficient by its power n.
def derivAux : Nat → Poly → Poly
  | _, []      => []
  | n, c :: cs => (n * c) :: derivAux (n + 1) cs

-- The constant term disappears, and the remaining terms start at power 1.
def Poly.deriv : Poly → Poly
  | []      => []
  | _ :: cs => derivAux 1 cs

-- d/dx (5 + 3x + 2x²) = 3 + 4x
#eval Poly.deriv [5, 3, 2]                -- [3, 4]
#eval (Poly.deriv [5, 3, 2]).eval 10      -- 3 + 4·10 = 43

-- Proofs about specific polynomials, checked by computation.
example : Poly.deriv [5, 3, 2] = [3, 4] := rfl
example : Poly.deriv [0, 0, 0, 1] = [0, 0, 3] := rfl   -- d/dx x³ = 3x²

-- A proof for ALL constants c: the derivative of a constant is zero
-- (the empty polynomial).
theorem deriv_const (c : Int) : Poly.deriv [c] = [] := rfl
