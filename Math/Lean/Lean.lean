--------------------------------------------------------------------------------
-- LeanDemo.lean written by nelson-n 2026-09-30
--
-- A first look at Lean 4 as a proof assistant. Uses only Lean core (no
-- Mathlib), so it can be checked directly with: lean LeanDemo.lean
--------------------------------------------------------------------------------

-- Lean is both a functional programming language and a proof assistant. Under
-- the Curry-Howard correspondence a proposition is a type, and a proof of that
-- proposition is a value (term) of that type. Checking a proof is therefore
-- just type checking. If the file compiles, every theorem in it is proven.

-- Commands that start with # are for interacting with Lean:
--   #eval  runs an expression and prints the result.
--   #check prints the type of an expression.
-- In VS Code (with the lean4 extension) the output appears in the infoview
-- panel. From the command line it is printed when the file is checked.

-- ## Sections
-- - Evaluating Expressions and Checking Types
-- - Defining Functions
-- - Proofs by Computation
-- - Propositional Logic
-- - Existential Statements
-- - Proofs by Induction
-- - Exercises

--==============================================================================
-- Evaluating Expressions and Checking Types
--==============================================================================

#eval 2 + 3                        -- 5
#eval [1, 2, 3].map (· * 2)        -- [2, 4, 6]
#eval "Lean" ++ " 4"               -- "Lean 4"

#check 2 + 3                       -- 2 + 3 : Nat
#check Nat.add_comm                -- ∀ (n m : Nat), n + m = m + n

-- Nat.add_comm is a theorem from the standard library. Its type IS the
-- statement being proven, which shows that proofs are ordinary values.

--==============================================================================
-- Defining Functions
--==============================================================================

def double (n : Nat) : Nat := n + n

#eval double 21                    -- 42

-- Functions can be defined by pattern matching and recursion. Lean requires
-- every function to terminate, which is needed for proofs to be sound.
def sumTo : Nat → Nat
  | 0     => 0
  | n + 1 => (n + 1) + sumTo n

#eval sumTo 10                     -- 55

--==============================================================================
-- Proofs by Computation
--==============================================================================

-- `rfl` (reflexivity) proves an equality when both sides compute to the same
-- value. `example` is an unnamed theorem.
example : 2 + 2 = 4 := rfl
example : double 21 = 42 := rfl
example : sumTo 10 = 55 := rfl

-- `decide` proves decidable statements about concrete values by evaluating them.
example : 17 < 42 := by decide
example : 10 % 3 = 1 := by decide

--==============================================================================
-- Propositional Logic
--==============================================================================

-- Proofs can be written as terms (term mode) or as a sequence of tactics
-- (tactic mode, started with `by`). Tactics transform the current goal until
-- nothing is left to prove.

-- Term mode: a proof of p ∧ q is a pair ⟨proof of p, proof of q⟩, so we swap
-- the components of the pair.
theorem and_swap (p q : Prop) (h : p ∧ q) : q ∧ p :=
  ⟨h.right, h.left⟩

-- Tactic mode: the same theorem, step by step.
theorem and_swap' (p q : Prop) : p ∧ q → q ∧ p := by
  intro h          -- assume h : p ∧ q, goal becomes q ∧ p
  constructor      -- split the goal into two goals: q, and p
  · exact h.right
  · exact h.left

-- Proof by cases: if p ∨ q holds, consider each case separately.
theorem or_swap (p q : Prop) : p ∨ q → q ∨ p := by
  intro h
  cases h with
  | inl hp => exact Or.inr hp
  | inr hq => exact Or.inl hq

-- Implication is function application: given a proof of p → q and a proof of
-- p, applying one to the other yields a proof of q (modus ponens).
theorem modus_ponens (p q : Prop) (hpq : p → q) (hp : p) : q :=
  hpq hp

--==============================================================================
-- Existential Statements
--==============================================================================

-- To prove ∃ n, P n, supply a witness n together with a proof of P n.
example : ∃ n : Nat, n * n = 16 := ⟨4, rfl⟩

--==============================================================================
-- Proofs by Induction
--==============================================================================

-- A recursive definition of doubling, then a proof that it agrees with 2 * n
-- for ALL natural numbers (not just the ones we test with #eval).
def doubleRec : Nat → Nat
  | 0     => 0
  | n + 1 => doubleRec n + 2

theorem doubleRec_eq (n : Nat) : doubleRec n = 2 * n := by
  induction n with
  | zero => rfl                    -- base case: doubleRec 0 = 0 = 2 * 0
  | succ n ih =>                   -- ih : doubleRec n = 2 * n
    simp only [doubleRec]          -- goal: doubleRec n + 2 = 2 * (n + 1)
    omega                          -- linear arithmetic closes it, using ih

-- `simp` can use lemmas from the standard library automatically.
example (xs ys : List Nat) : (xs ++ ys).length = xs.length + ys.length := by
  simp

--==============================================================================
-- Exercises
--==============================================================================

-- `sorry` marks an unfinished proof. The file still compiles, with a warning.
-- Try replacing each `sorry` with a real proof.

-- Hint: `exact` with an anonymous constructor ⟨_, _⟩.
theorem and_intro_ex (p q : Prop) (hp : p) (hq : q) : p ∧ q := by
  sorry

-- Hint: induction on n, as in doubleRec_eq.
theorem double_eq_doubleRec (n : Nat) : double n = doubleRec n := by
  sorry
