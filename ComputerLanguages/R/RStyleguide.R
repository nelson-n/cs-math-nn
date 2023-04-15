
#===============================================================================
# 1 Files
#===============================================================================

# File names should be meaningful and end in .R. Use only numbers, letters, -, 
# and _.

# Good
# fit_models.R
# utility_functions.R

# Bad
# fit models.R
# foo.r
# stuff.r

# If files should be run in a particular order prefix with numbers, if more
# than 1- files are likely, left pad with zero.
# 00_download.R
# 01_explore.R
# ...
# 09_model.R
# 10_visualize.R

# Prefer file names that are all lowercase and never have names that differ 
# only in their capitalization.

# If your script uses add on packages, load them all at once at the beginning
# of the file, this is more transparent that library() calls throughout the
# code which may have hidden dependencies.

#===============================================================================
# Syntax
#===============================================================================

# 2.1 Object Names -------------------------------------------------------------

# "There are only two hard things in Computer Science, cache invalidation and 
# naming things." - Phil Karlton

# Variable and function names should use only lowercase letters, numbers, and _.
# Use underscores _ to seperate words within a name.

# Good
day_one
day_1

# Bad
DayOne
dayone

# Base R uses dota in functions names (data.frame) but it is better to reserve
# dots exclusively for the S3 object system. 

# If you find yourself attempting to cram data into variable names (e.g. 
# model_2018, model_2019, model_2020), consider using a list or data frame 
# instead.

# Generally, variable names should be nouns and function names should be verbs.
# Strive for names that are concise and meaningful.

# Good
day_one

#Bad
first_day_of_the_month
djm1

# Avoid re-using names of common functions and variables.

# Bad
T <- FALSE
c <- 10
mean <- function(x0) sum(x)

# 2.2 Spacing ------------------------------------------------------------------

# Always put a space after a comma, never before, just like regular English

# Good
x[, 1]

# Bad
x[,1]
x[ ,1]
x[ , 1]

# 2.2.2 Parentheses

# Do not put spaces inside or outside parentheses for regular functions calls

# Good
mean(x, na.rm = TRUE)

# Bad
mean (x, na.rm = TRUE)
mean( x, na.rm = TRUE )

# Place a space before and after () when used with if, for, or while

# Good 
if (debug) {
  show(x)
}

# Bad
if(debug) {
  show(x)
}

# Place a space after () used for function arguments

# Good
function(x) {}

# Bad
function (x) {}
function(x){}

# 2.2.4 Infix operators

# Most infix operators (==, +, -, <-, etc.) should be surrounded by spaces

# Good
height <- (feet * 12) + inches
mean(x, na.rm = TRUE)

# Bad
height<-feet*12+inches
mean(x, na.rm=TRUE)

# There are a few exceptions which should never be surrounded by spaces
# Operators with high precedence (::, :::, $, @, [, [[, ^)

# Good
sqrt(x^2 + y^2)
x <- 1:10

# Bad
sqrt(x ^ 2 + y ^ 2)
x <- 1 : 10

# 2.2.5 Extra spaces

# Adding extra spaces is ok if it improves the alignment of = or <-

# Good
list(
  total = a + b + c,
  mean  = (a + b + c) / n
)

# Also fine
list(
  total = a + b + c,
  mean = (a + b + c) / n
)

# Do not add extra spaces to places where space is not usually allowed

# 2.3 Functions calls ----------------------------------------------------------

# 2.3.1 Named arguments

# A function's arguments typically fall into two categories: one supplies the 
# data to compute on; the other controls the details of the computation. When
# calling a function typically omit the names of data arguments. If you override
# the default value of an argument, use the full name.

# Good
mean(1:10, na.rm = TRUE)

# Bad
mean(x = 1:10, , FALSE)

# 2.4 Control flow -------------------------------------------------------------

# 2.4.1 Code blocks

# Curly braces {} define the most important hierarchy of R code, to make the
# hierarchy easy to see: 
# { should be the last character on the line
# The contents should be indented by two spaces
# } should be the first character on th eline
if (y < 0 && debug) {
  message("y is negative")
}

# 2.4.2 Inline statements

# It is okay to drop the curly braces for very simple statements that fit on one
# line as long as they do not have side effects.

# Good
y <- 10
x <- if (y > 20) "Too low" else "Too high"

# Function calls that affect control flow like return(), stop(), and continue()
# should always go in their own {} block.

# Good
if (y < 0) {
  stop("Y is negative")
}

# Bad
if (y < 0) stop("Y is negative")

# 2.4.3 Implicit type conversion

# Avoid implicit type conversion (e.g. from numeric to logical) in if statements

# Good
if (length(x) > 0) {
  # do something
}

# Bad
if (length(x)) {
  # do something
}

# 2.5 Long Lines ---------------------------------------------------------------

# Limit code to 80 characters per line. If a function call is too long to fit on
# a single line, use one line each for the function names, each argument, and the
# closing ).

# Good
do_someting_very_complicated(
  something = "that",
  requires = many,
  argument = "some of which may be long"
)

# Bad
do_something_very_complicated("that", requires, many, arguments,
                              "some of which may be long"
                              )

# As described in the argument names section, you can omit the argument names
# for very common arguments, short unnamed arguments can also go on the same
# line as the function name, even if the whole function spans multiple lines.
map(x, f,
    extra_argument_a = 10,
    extra_argument_b = c(1, 43, 390, 210209)
    )

# You may also place several arguments on the same line if they are closely
# related to each other, e.g. strings in calls to paste() or stop()

# Good
paste0(
  "Requirement: ", requires, "\n",
  "Result: ", result, "\n"
)

# 2.7 Assignment ---------------------------------------------------------------

# Use <- and not = for assignment

# Good
x <- 5

# Bad
x = 5

# 2.8 Data ---------------------------------------------------------------------

# 2.8.1 Character vectors

# Use " and not ' for quoting text, the only exception is when the text already
# contains double quotes and no single quotes.

# Good
"Text"
'Text with "quotes"'

# 2.8.2 Logical vectors

# Prefer TRUE and FALSE to T and F

# 2.9 Comments -----------------------------------------------------------------

# Each line of comment should being with # and a single space

# In data analysis code, use comments to record important findings and analysis
# decisions. If you need comments to explain what your code is doing, consider
# rewriting your code to be clearer. If you discover that you have more comments
# than code, consider switching to R markdown.

#===============================================================================
# 3 Functions
#===============================================================================

# 3.1 Naming -------------------------------------------------------------------

# Strive to use verbs for functions names

# Good
add_row()
permute()

# Bad
row_adder()
permutation()

# 3.2 Long lines ---------------------------------------------------------------

# If a function defenition runs over multiple lines, indent the second line to 
# where the defenition starts

# Good
long_function_name <- function(a = "a long argument",
                               b = "another argument",
                               c = "another long argument") {
  # as usual code is indented two spaces
}

# 3.3 return() -----------------------------------------------------------------

# Only use return() for early returns, otherwise rely on R to return the result
# of the last evaluated expression.

# Good
find_abs <- function(x) {
  if (x > 0) {
    return(x)
  }
  x * -1
}

# Return statements should always be on their own line because they have 
# important effects on the control flow.

# If your function is called primarily for its side-effects (like printing,
# plotting, or saving to disk), it should return the first argument invisibly.
# This makes it possible to use the function as part of a pipe.

print.url <- function(x, ...) {
  cat("Url: ", build_url(x), "\n", sep = "")
  invisible(x)
}

# 3.4 Comments -----------------------------------------------------------------

# In code, use comments to explain the "why" not the "what" or the "how"

# Good

# Objects like data frames are treated as leaves
x <- map_if(x, is_bare_list, recurse)

# Bad

# Recurse only with bare lists
x <- map_if(x, is_bare_list, recurse)

# Comments should be in sentence case, and only end with a full stop if they 
# contain at least two sentences. Examples:

# Objects like data frames are treated as leaved

# Do not use 'is.list()'. Objects like data frames must be treated as leaves.

#===============================================================================
# 4 Pipes
#===============================================================================

# 4.1 Introduction -------------------------------------------------------------

# Use %>% to emphasise a sequence of actions, rather than the objects that the
# actions are being performed on.

# Avoid pipes when:
# You need to manipulate more than one object at a time, reserve pipes for a 
# sequences of steps.
# There are meaningful intermediate objects that could be given informative names

# 4.2 Whitespace ---------------------------------------------------------------

# %>% should always have a space before it and should usually be followed by a 
# new line. After the first step each line should be indented by two spaces.

# Good
iris %>%
  group_by(Species) %>%
  summarize_if(is.numeric, mean) %>%
  ungroup()
 
# 4.3 Long lines ---------------------------------------------------------------

# If the arguments to a function do not all fit on one line, put each argument
# on its own line and indent:

iris %>%
  group_by(Species) %>%
  summarise(
    Sepal.Length = mean(Sepal.Length),
    Sepal.Width = mean(Sepal.Width),
    Species = n_distinct(Species)
  )

# A one-step pipe can stay on one line, but you should consider rewriting it to
# a regular function call.

iris %>% arrange(Species)

arrange(iris, Species)

#===============================================================================
# 5 ggplot2
#===============================================================================

# 5.2 Whitespace ---------------------------------------------------------------

# + should always have a space before it, and should be followed by a new line.
# This is true even if your plot has only two layers. After the first step, each
# line should be indented by two spaces.

# If you are creating a ggplot off of a dplyr pipeline, there should only be
# one level of indentation.

iris %>%
  filter(Species == "setosa") %>%
  ggplot(aes(x = Sepal.width, y = Sepal.Length)) +
  geom_point()

# 5.3 Long lines ---------------------------------------------------------------

# If the arguments to a ggplot2 layer do not all fit on one line, put each 
# argument on its own line and indent.

ggplot(aes(x = Sepal.Width, y = Sepal.Length, color = Species)) +
  geom_point() +
  labs(
    x = "Sepal width, in cm",
    y = "Sepal length, in cm",
    title = "Sepal length vs. width or irises"
  )

#===============================================================================
# 6 Files (Packages)
#===============================================================================

# 6.1 Names --------------------------------------------------------------------

# If a file contains a single function, give the file the same name as the 
# function.

# If a file contains multiple related functions, give it a concise but 
# evocative name.

# Deprecated functions should live in a file with a deprec- prefix

# Compatability functions should live in a file with compat- prefix

# 6.2 Organisation -------------------------------------------------------------

# In a file that contains multiple functions, public functions and their 
# documentation should appear first, with private functions appearing after all 
# documented functions. If multiple public functions share the same documentation,
# they should all immediately follow the documentation block.

#===============================================================================
# 7 Documentation 
#===============================================================================

# 7.2 Title and description ----------------------------------------------------

# Use the first line of your function documentation to provide a concise title
# that describes the function, dataset, or class. Titles should use sentence case
# but not end with a full stop.

#' Combine values into a vector or list
#' 
#' This is a generic function which combines its arguments.
#'

# There is no need to use the explicit @title or @description tags, except in the
# case of the description if it is multiple paragraphs or includes more complex
# formatting like a bulleted list

#' Apply a function to each element of a vector
#'
#' @description
#' The map function transform the input, returning a vector the same length
#' as the input. 
#' 
#' * `map()` returns a list or a data frame
#' * `map_lgl()`, `map_int()`, `map_dbl()` and `map_chr()` return 
#'     vectors of the corresponding type (or die trying); 
#' * `map_dfr()` and `map_dfc()` return data frames created by row-binding 
#'    and column-binding respectively. They require dplyr to be installed.

# 7.3 Indents and line breaks --------------------------------------------------

# Always indent with one space after #'. If any description corresponding to a 
# roxygen tag spans over multiple lines, add another two spaces of indentation.

#' @param key The bare (unquoted) name of the column whose values will be used 
#'   as column headings. 

# Alternatively, tags that span over multiple lines (like @description, @examples,
# and @section) can have the corresponding tag on its own line and then subsequent
# lines do not need to be indented.

#' @examples
#' 1 + 1
#' sin(pi)

# Use line breaks before/after sections where needed

#' @section Tidy data:
#' When applied to a data frame, row names are silently dropped. To preserve,
#' convert to an explicit variable with [tibble::rownames_to_column()].
#'
#' @section Scoped filtering:
#' The three [scoped] variants ([filter_all()], [filter_if()] and
#' [filter_at()]) make it easy to apply a filtering condition to a
#' selection of variables.

# 7.4 Documenting parameters ---------------------------------------------------

# For most tags, like @param, @sealso, and @return, the text should be a sentence
# starting with a capital letter and ending with a full stop

#' @param key The bare (unquoted) name of the column whose values will be used 
#'   as column headings. 

# If some functions share parameters you can use @inheritParams to avoid 
# duplication of content in multiple places.

#' @inheritParams function_to_inherit_from

# 7.5 Capitalization and full stops --------------------------------------------

# For all bullets, enumerations, argument descriptions and the like, use sentence
# case and put a period at the end of each text elements, even if it is only a 
# few words. However, avoid capitalization of function names or packages since R
# is case sensitive. Use a colon before enumerations or bulleted lists.

#' @details 
#' In the following, we present the bullets of the list:
#' * Four cats are few animals.
#' * forcats is a package.

# 7.6 Cross-linking ------------------------------------------------------------

# Cross-referencing is encouraged, both within R's help file system as well as 
# to external resources. List closely related functions in @seealso. A single
# related function can be written as a sentence.

#' @seealso [fct_lump()] to automatically convert the rarest (or most common)
#'   levels to "other".

# More recomendations should be organised in a bulleted list.

#' @seealso
#' * [tibble()] constructs from individual columns.
#' * [enframe()] converts a named vector into a two-column tibble (names and 
#'   values).
#' * [name-repair] documents the details of name repair.

# If you have a family of related functions, you can use the @family tag to 
# automatically add appropriate lists and interlinks to the @seealso section.
# Family names are plural. In dplyr, the verbs arrange(), filter(), mutate(),
# slice(), and summarize() form the family of single table verbs.

#' @family single table verbs

# When linking to external resources either include the full url inlines with <>,
# or the suround prose and link text should make it extremely clear where the
# hyperlink goes. Avoid text like "click here".

#===============================================================================
# 9 Error messages
#===============================================================================

# An error message should start with a general statement of the problem, then
# give a concise description of what went wrong. Consistent use of punctuation 
# and formatting makes errors easier to parse.

# 9.1 Problem statement --------------------------------------------------------

# Every error message should start with a general statement of the problem that
# is consise but informative.

# Ideally each sentence should contain a single phrase, and should only mention
# one variable quantity. To avoid complex sentences it is preferred to lay out
# information in a bullet list. Start with a list of contextual information, and
# finish with a list of information about faulty user input. These lists should 
# be prefixed with ℹ and ✖ respectively if UTF-8 is available (and in blue and 
# red if colour is available), or the ASCII * character otherwise.

# If the cause of the problem is clear, use "must"

dplyr::nth(1:10, "x")
#> Error: `n` must be a numeric vector:
#> ✖ You've supplied a character vector.

dplyr::nth(1:10, 1:2)
#> Error: `n` must have length 1
#> ✖ You've supplied a vector of length 2.

# If you cannot state what was expected, use "can't"

mtcars %>% pull(b)
#> Error: Can't find column `b` in `.data`.

as_vector(environment())
#> Error: Can't coerce `.x` to a vector.

purrr::modify_depth(list(list(x = 1)), 3, ~ . + 1)
#> Error: Can't find specified `.depth` in `.x`.

# The problem statement should use sentence case and end with a full stops. The
# sentences should be short and bulleted.

vec_slice(letters, 100)
#> Must index an existing element:
#> ℹ There are 26 elements.
#> ✖ You've tried to subset element 100.

# Contextual information should be first.

# 9.2 Error location -----------------------------------------------------------

# Do your best to reveal the location, name, and content of the troublesome
# component, the goal is to make it as easy as possible for the user to find
# and fix the problem.

map_int(1:5, ~ "x")
#> Error: Each result must be a single integer:
#> ✖ Result 1 is a character vector.

# If the source of the error is unclear, avoid pointing the user in the wrong
# direction by giving an opinion about the source of the error. 

# If there are multiple issues, or an inconsistency revealed across several
# arguments, prefer a bulleted list

purrr::reduce2(1:4, 1:2, `+`)
#> Error: `.x` and `.y` must have compatible lengths:
#> ✖ `.x` has length 4
#> ✖ `.y` has length 2

# 9.3 Hints --------------------------------------------------------------------

# If the source of the error is clear and common, provide a hint prefixed 
# with ℹ

dplyr::filter(iris, Species = "setosa")
#> Error: Filter specifications must be named.
#> ℹ Did you mean `Species == "setosa"`?

# Hints should always end in a question mark. In general avoid writing a hint
# unless the problem is common and you can easily find a common pattern of 
# incorrect usage on StackOverflow

# 9.4 Punctuation --------------------------------------------------------------

# Errors should be written in sentence case, and should end with a full stop. 
# Bullets should be formatted similarly. 

# Prefer the singular in problem statements.

# Good
map_int(1:2, ~ "a")
#> Error: Each result must be coercible to a single integer:
#> ✖ Result 1 is a character vector.

# Bad
map_int(1:2, ~ "a")
#> Error: Results must be coercible to single integers: 
#> ✖ Result 1 is a character vector

# If you can detect multiple problems, list up to five. This allows the user
# to fix multiple problems and not be overwhelmed by the number of errors.

# BETTER
map_int(1:10, ~ "a")
#> Error: Each result must be coercible to a single integer:
#> ✖ Result 1 is a character vector
#> ✖ Result 2 is a character vector
#> ✖ Result 3 is a character vector
#> ✖ Result 4 is a character vector
#> ✖ Result 5 is a character vector
#> ... and 5 more problems

# Surround the names of arguments in backticks, e.g. `x`. Use "column" to 
# disambiguate columns and arguments: column `xxx`

#===============================================================================
# 10 News
#===============================================================================

# Each user-facing change in a package should be accompanied by a bullet in 
# NEWS.md. 

# 10.1 Bullets -----------------------------------------------------------------

# The goal of the bullet is to briefly describe the change so users of the 
# packages can understand what has changed. This can be similar to a commit
# message, but written with the user (not developer) in mind.

# New bullets shouldbe added to the top of the file (under the heading). 
# Organisation of bullets will happen later, during the release process.

# 10.1.1 General Style ---------------------------------------------------------

# Strive to place the name of the function as close to the beginning of the 
# bullet as possible. A consistent location makes the bullets easier to scan.

# Good
# * `ggsave()` now uses full argument names to avoid partial match warning (#2355).
  
# Bad
# * Fixed partial argument matches in `ggsave()` (#2355).

# Many news bullets will be a single sentence. This is typically adequate for 
# a bug fix or minor improvements, but more detail may be needed when describing
# a new features. For more complex features, include longer example in fenced 
# code blocks (```).

# Good
# * In `stat_bin()`, `binwidth` now also takes functions.

# Better
# * In `stat_bin()`, `binwidth` now also takes functions. The function is 
# called with the scaled `x` values, and should return a single number.
# This makes it possible to use classical binwidth computations with ggplot2.

# Best
# * In `stat_bin()`, `binwidth` now also takes functions. The function is 
# called with the scaled `x` values, and should return a single number.
# With a little work, this makes it possible to use classical bin size 
# computations with ggplot2.

```R
sturges <- function(x) {
  rng <- range(x)
  bins <- nclass.Sturges(x)
  
  (rng[2] - rng[1]) / bins
}
ggplot(diamonds, aes(price)) +
  geom_histogram(binwidth = sturges) + 
  facet_wrap(~cut)
```

# 10.1.2 Acknowledgement -------------------------------------------------------

# If the bullet is related to an issue, include the issue number. If the 
# contribution was a pull request, and the author is not a package author, 
# include their GitHub user name. Both items should be wrapped in parentheses
# that generally come before the final period.

# * `ggsave()` now uses full argument names to avoid partial match warnings 
# (@wch, #2355).

# 10.1.3 Cody style ------------------------------------------------------------

# Functions, arguments, and file names should be wrapped in backticks. Function
# names should include parentheses but omit arguments.

# * In `stat_bin()`, `binwidth` now also takes functions.

# 10.2.2 Release ---------------------------------------------------------------

# Each release should have a level 1 (#) heading that contains the package 
# name and version number. For smaller packages or patch releases this amount
# of organisation may be sufficient. For example, here is the NEWS for modelr 0.1.2:

# modelr 0.1.2

# * `data_grid()` no longer fails with modern tidyr (#58).
  
# * New `mape()` and `rsae()` model quality statistics (@paulponcet, #33).
                                                        
# * `rsquare()` use more robust calculation 1 - SS_res / SS_tot rather 
#   than SS_reg / SS_tot (#37).

# If there are many bullets, the version heading should be followed by issues
# grouped into related areas with level 2 headings (##).

# package 1.1.0

## Breaking changes

## New features

## Minor improvements and fixes

# Within a section, bullets should be ordered alphabetically by the first 
# function mentioned. If no functions is mentioned, place the bullet at the 
# top of the section. 

# 10.3 Blog post ---------------------------------------------------------------

# For all major and minor releases, the latest news should be turned into a 
# blog post highlighting major user-facing changes. Focus on new features and
# major improvements, including example showing the new features in action. 

#===============================================================================
# Git/GitHub                                                          
#===============================================================================
                                                       
# 11.1 Commit messages ---------------------------------------------------------

# Follow standard git commit message advice:
# The first line is the subject, and should summarise the changes in the commit
# in under 50 characters.

# If additional details are required, add a blank line, and then provide explanation 
# and context in paragraph format.

# If the commit fixed a GitHub issue include "Fixes #<issue-number>" which will
# ensure the issue is automatically closed when the commit is merged into master.
