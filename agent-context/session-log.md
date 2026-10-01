# Session log

Newest first. Each entry gives the date, what was done, and open threads.

## 2026-09-30: Lean demo

- Added `Math/Lean/SimpleDemo.lean`, a minimal first test for the Lean install (`theorem two_plus_two : 2 + 2 = 4 := rfl`).
- Added `Math/Lean/LeanDemo.lean`, a core-only Lean 4 intro (no Mathlib, no lake project). It covers #eval/#check, recursive defs, rfl/decide, propositional logic, ∃, induction, and two `sorry` exercises. Linked it from `Math/README.md` and the root README.
- Lean was not installed on the machine, so neither file has been compile-checked yet.
- 2026-10-01: Owner installed Lean 4.34.1 and renamed the files to `Lean.lean` and `LeanDemos.lean`. Both check cleanly with `lean <file>`. A Mathlib Lake project was tried for derivative and integral examples, but the owner declined it because of the download size, so stay core-only. Real numbers, `deriv` and integrals are unavailable.

## 2026-09-30: agent-context bootstrap

- Read through the repo and created `agent-context/` (overview, conventions, this log) and a root `CLAUDE.md` pointing to it.
- No content or code was changed.
- Also wrote a `known-issues.md` listing bugs and hygiene problems. The owner said that isn't a concern for a learning repo, so it was removed and the "code doesn't have to run" rule was added to `repo-overview.md`.
