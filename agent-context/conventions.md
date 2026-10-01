# Conventions

These were inferred from the existing files. Follow them when adding content so new material reads like the old.

## Naming and placement

- Folders and files use **PascalCase** (`DataStructuresAlgorithms/`, `FeedforwardNN.py`, `SVD_PCA.ipynb`). A few older exercise files are lowercase (`pythonFactorial.py`); don't copy that.
- File name suffixes signal intent: `*Cheatsheet.*` is a quick reference, `*Notes.*` / `*Principles.ipynb` is theory, and `*Code.ipynb` / standalone scripts are implementations and exercises.
- Images embedded in notes go in the section's `NoteFiles/` folder. They're named with a sequential prefix (`ML1.png`, `DL142.png`) or descriptively (`BlackScholesContour.png`). Notebooks reference them as `![](./NoteFiles/<name>.png)`.
- Data lives in a `data/` or `Data/` subfolder of the section.

## Source-file style (Python / R / Julia)

Scripts start with a header block that gives the file name, author handle, and date, followed by a one-line purpose:

```python
#-------------------------------------------------------------------------------
# FeedforwardNN.py written by nelson-n 2021-06-26
#
# Creates feedforward neural network from scratch in numpy.
#-------------------------------------------------------------------------------
```

Major sections use `#===...===` banners (80 columns), and each banner has its title on its own line. Notes-style scripts (`TorchNotes.py`, cheatsheets) often open with a `## Sections` list. Comments are full sentences and explain the *why* and the math. The comments are the point of the file.

## Java notes

Topic files are prose-heavy. Each section opens with a `//----` banner and title, and the explanation sits in a `/* ... */` block of dash bullets, followed by example code.

## Notebooks

- Open with a `## Sections` list, then `##` for sections and `###`/`####` for subsections.
- Theory notebooks mix markdown/LaTeX with embedded `NoteFiles` images. Code notebooks interleave explanation with runnable cells.

## Markdown cheatsheets

`## <Title> Cheatsheet`, then `###` groups. Each item is a **bold question/description** followed by the command in backticks.

## Keeping READMEs in sync

When you add a notes file or exercise, update **both**:
1. The section's own `README.md`: add a backticked filename heading plus a bullet list of what it covers.
2. The root `README.md`: add a link under the section's **Notes on** / **Cheatsheets** / **Exercises** list (absolute `https://github.com/nelson-n/cs-math-nn/blob/main/...` URL). Add it to the top-level quick-link lists too if it's a primary notes file or cheatsheet. Remove the completed item from the relevant to-do list.

## Git

- Work on `main` (the owner's existing practice). Commit only when asked.
- Don't add new `.DS_Store` files, compiled binaries, or large generated artifacts in new commits.
