
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

### General Commands
* `\bibliography{<filename>}` - add a `.bib` file to the document.
* `\bibliographystyle{<style>}` - add a bibliography style to the document.

### Packages

**`\usepackage{natbib}`**
* Citation package used to cite references in a latex document.
* `\setcitestyle{round}` - add round brackets around citation dates.
* `\cite` - cite a reference in-text with brackets around the entire citation.
* `\citet` - cite a reference in-text with brackets around the year only.

