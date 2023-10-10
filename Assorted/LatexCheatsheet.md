
## Latex Cheatsheet

### Compiling
* When compiling a latex document you need to run `latex` first to create `.aux`
file, then run `bibtex` to create `.bbl` file, then run `latex` again to update
the `.aux` file, and finally run `latex` one last time to update the `.pdf` file.
    * `latex <filename>.tex` - creates `.aux` file
    * `bibtex <filename>.aux` - creates `.bbl` file
    * `latex <filename>.tex` - updates `.aux` file
    * `latex <filename>.tex` - updates `.pdf` file
* Finally run `pdflatex` to create the `.pdf` file.
    * `pdflatex <filename>.tex` - creates `.pdf` file

* This entire workflow can be replaced in one step with the command `latexmk`.
    * `latexmk -pdf <filename>.tex` 

* **Note** in order to compile references and citations correctly you need a 
`.bib` file with the references and citations in it. The `.bib` file must be
referenced in the `.tex` file with the command `\bibliography{<filename>}`.

---

### General Commands
* `\bibliography{<filename>}` - add a `.bib` file to the document.
* `\bibliographystyle{<style>}` - add a bibliography style to the document.

---

### Packages

**`\usepackage{natbib}`**
* Citation package used to cite references in a latex document.
* `\setcitestyle{round}` - add round brackets around citation dates.
* `\cite` - cite a reference in-text with brackets around the entire citation.
* `\citet` - cite a reference in-text with brackets around the year only.

---

### Figures
* Example for embedding a figure in text.
    * To reference the figure in text use: `Figure \ref{fig:Figure1} displays ...`

```latex
\begin{figure}
    \centering
    \includegraphics[width=\linewidth]{<filename/figure.png>}
    \caption{<caption>}
    \label{fig:Figure1}
\end{figure}
```

---

### Bibliography
* Journal entries in .bib file should follow the form:

```latex
@article{DoeSmithPaper,
    author = {Doe, John and Smith, Jane},
    title = {Paper Title},
    journal = {Journal Name},
    year = {2020},
    volume = {1},
    pages = {1--10}
}
```

* The bibliography is embedded at the end of the document with the command: `\bibliography{<filename.bib>}`
* Additionally, a bibliography style can be added with the command: `\bibliographystyle{apalike}`
