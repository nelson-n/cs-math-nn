# Repo overview

## Purpose

`cs-math-nn` is Nelson's personal, self-directed curriculum for learning computer science, math, statistics, ML/deep learning, and quant finance from first principles. The stated goal is to cover "the whole compute stack from transistors and assembly to the math underpinning transformer models."

It is a **notes + exercises repo, not a software project**. There's no build, no package, no test suite, and no dependency manifest. "Code" means study notes written as heavily commented source files, Jupyter notebooks, and small from-scratch implementations (for example, NumPy neural nets or MLE in R).

**Code doesn't have to run.** This repo is for exploring and learning concepts. Some scripts are incomplete or have bugs, and that's fine. Don't fix, flag, or clean up existing code or repo hygiene (tracked `.DS_Store` files, binaries, logs) unless the owner asks.

- Remote: `github.com/nelson-n/cs-math-nn` (public). The README links use absolute GitHub URLs on `main`.
- History: first commit 2022-11-08. Last commit before agent work began was 2024-10-18 ("balance sheet notes"). About 370 commits, all on `main`, with informal messages.
- Authors in file headers: `nelson-n` and `lucius-verus-fan` (both the same owner).

## Layout

Top-level folders follow the README section order, going roughly up the stack:

| Folder | Contents |
|---|---|
| `Assorted/` | `atoms2bits.pdf` (the "preface": sand → transistors → CPU → OS → languages), Git and LaTeX cheatsheets, `Assets/RepoLogo.png` |
| `CompilersOperatingSystems/` | `Unix/UnixCheatsheet.md` only. The README lists large to-dos (C compiler, OS, filesystem). |
| `DataStructuresAlgorithms/` | Java notes, one file per topic under `DataStructuresAlgorithmsNotes/` (linked lists, trees, graphs, hashing, …), a cheatsheet `.md`, `BinaryTreeCheatsheet.java`, and the `CompareRecursion/` exercise (factorial timed in Py/R/Julia/Java/C/C++ via `CompareTime.sh`) |
| `ComputerLanguages/` | Cheatsheets and notes per language: `Python/` (incl. *Fluent Python* notes, `Scratch.py`), `R/` (incl. a very large `AdvancedR.R`), `Java/`, `Julia/`, `SQL/`, plus `ProgrammingParadigms.md` |
| `Math/` | Notebooks: `MathNotes` (Brownian motion, Itô), `Calculus`, `LinearAlgebra` (**Julia kernel**), plus `MathSymbolsCheatsheet.md`; `Lean/` (`SimpleDemo.lean`: 2+2=4; `LeanDemo.lean`: longer Lean 4 intro, core only) |
| `Statistics/` | Notebooks `Statistics`, `Regression`, `SVD_PCA`; exercises `MLE.R`, `MetropolisHastings.py`; `NoteFiles/` images |
| `MachineLearning/` | `MachineLearningPrinciples.ipynb` (theory, mostly embedded images of handwritten/slide notes), `MachineLearningCode.ipynb` (sklearn-style implementations), and `NoteFiles/ML*.png` |
| `DeepLearning/` | `DeepLearningPrinciples.ipynb` (theory + `NoteFiles/DL*.png`), `DeepLearningCode/DeepLearningCode.ipynb` (PyTorch plus TensorBoard `log/`), `TorchNotes.py`, from-scratch NumPy nets in `FeedforwardNN/` and `RecurrentNN/`, `NNProblems.py`, and `data/` (MNIST `.gz`, `WarAndPeace.txt`) |
| `QuantitativeFinance/` | `QuantFinance.ipynb` (valuation, duration and convexity, credit, yield curves, papers, and most recently balance sheet notes) plus `NoteFiles/` |
| `AdditionalTopics/` | `NetworkInternet/internet.pdf` only. Parallel computing, databases, crypto, and info theory are README placeholders. |

Every top-level folder has its own `README.md` describing each file's contents. The root `README.md` is the curriculum index. It has links to every notes file, a per-section `:bangbang: to-do` list, and a global To-Do List at the bottom.

## Languages and tooling in use

Python (NumPy, PyTorch, scikit-learn, TensorBoard), R, Java, Julia, C/C++, Lean 4, and bash, plus Jupyter for notebooks. Nothing is pinned, so assume a local environment with these installed. Notebooks are committed with outputs.

## Direction and roadmap (per README)

The highest-signal open threads are listed below. See the root README for the full list.

- **Deep learning:** build paradigmatic models from scratch in order. FFNN and RNN are done. CNN → ResNet → Transformer → Diffusion come next (Karpathy lectures, tinygrad).
- **Hardware/compilers/OS:** logic gates → CPU in Verilog → C compiler → toy OS/filesystem. None of this is started.
- **Math/Stats/Quant:** calculus and stats fundamentals, differential equations and PDEs, Poisson/Cox processes, CIR and affine term structure, Merton/Nelson-Siegel/Black-Scholes/jump diffusion.
- **Crypto:** SHA256 from scratch, then use it to build a hash table.
