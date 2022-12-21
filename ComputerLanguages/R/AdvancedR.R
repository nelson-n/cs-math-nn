
# advanced R - Hadley Wickham

#-------------------------------------------------------------------------------
# 2 Names and values
#-------------------------------------------------------------------------------

# It is important to understand the distinction between an object and its name
# this chapter covers the distinction between names and values, and when R will
# copy an object.

# --- 2.2 binding basics -------------------------------------------------------

# attach lobstr, set of tools for inspecting data structures in R
library(lobstr)

# consider the code
x <- c(1, 2, 3)

# behind the scenes R is creating a vector of values c(1, 2, 3) and then it is
# binding that object to a name, x
# the object or value (vector in this case) doesn't have a name, it's the name 
# that has a value
# thus, you can think of a name as a reference to a value
y <- x 

# y <- x does not point y to x, it points y to the same value (vector) that
# x points to 

# you can see this by looking at the value memory address, the location in 
# memory where the values (vector) is stored
obj_addr(x)
obj_addr(y)

# both names, x and y, point to the same value

# redundant values end up being stored at different memory addresses
a <- 1:10
b <- 1:10
obj_addr(a)
obj_addr(b)

# thus you are using double the memory necessary in the above scenario

# functions are stored in the same place in memory regardless of how you 
# access them
obj_addr(mean)
obj_addr(base::mean)
obj_addr(get('mean'))

# --- 2.2.1 non-syntactic names ---

# R has strict rules about what constitues a valid name
# a syntactic name must consist of letters, digits, ., and _, but can't 
# begin with _ or a digit
# you also cannot use any reserved words such as TRUE, NULL, if, and function
# one can override these rules by using backticks
`_abc` <- 1

# --- 2.3 copy-on-modify -------------------------------------------------------

# consider the following code, it binds x and y to the same underlying value,
# then modifies y
x <- c(1, 2, 3)
y <- x
y[[3]] <- 4

# what happens to the shared binding when y is modified? 
# R creates a new object and then binds y to that new object
# this behavior is called copy-on-modify

# --- 2.3.1 tracemem() ---

# you can see when an object gets copied with base::tracemem()
# once you call tracemem() with an object you will get the objects current
# address, from then on whenever the object is copied tracemem() will print
# a message telling you which object was copied and its new address
x <- c(1, 2, 3)
cat(tracemem(x), "\n")

y <- x
y[[3]] <- 4L

# if you modify y again it won't get copied because the new object in memory
# only has a single name bound to it, thus R applies modify-in-place optimization
y[[3]] <- 5L

# to turning memory tracing off
untracemem(x)

# --- 2.3.2 function calls ---

# the same rules apply to function calls
f <- function(a) {
  a
}

x <- c(1, 2, 3)
cat(tracemem(x), "\n")

z <- f(x)

untracemem(x)

# there is no copy that occurs because while f() is running, the a inside 
# the function points to the same value as x does outside the function

# --- 2.3.3 lists ---

# it is not just names/variables that point to values, elements of lists do too
# instead of storing the values themselves, lists store references to the values
l1 <- list(1, 2, 3)
l2 <- l1
l2[[3]] <- 4

# like vectors, lists use copy-on-modify behaviour; the original list is left
# unchanged and R creates a modified copy

# however, this modified copy is a shallow copy: the list object and its bindings
# are copied, but the values pointed to by the bindings are not

# l1[[1]] and l2[[1]] still point to the same value in memory
# l1[[2]] and l2[[2]] still point to the same value in memory
# l1[[3]] and l2[[3]] now point to different values in memory

# the opposite of a shallow copy is a deep copy where the contents of every 
# reference are copied, prior to R 3.1.0 all copies were deep copies
# in a deep copy scenario l1[[2]] and l2[[1]] point to different values
# in memory despite the fact that they both = 1

# lobstr::ref() shows what values are shared across lists
# ref() prints the memory address of each object along with a local ID
ref(l1)
ref(l2)
ref(l1, l2)

# --- 2.3.4 data frames ---

# data frames are lists of vectors, so copy-on-modify has important consequences
# for modifying data frames
d1 <- data.frame(x = c(1, 5, 6), y = c(2, 4, 3))

# if you modify a column, only that column needs to be modified, the other
# will still point to their original references
# however, if you modify a row every column is modified, which means that 
# every column must be copied to a new vector in memory

# --- 2.3.5 character vectors ---

# R uses a global string pool where each element in a vector of strings is a 
# pointer to a unique string in the string pool
# ref() can show this by setting: character = TRUE
x <- c("a", "a", "abc", "d")
ref(x, character = TRUE)

# --- 2.4 object size ----------------------------------------------------------

# you can find how much memory an object takes with lobstr::obj_size()
obj_size(ggplot2::diamonds)

# since elemtents of lists are references to values, the size of a list is 
# smaller than one would expect
x <- runif(1e6)
obj_size(x)

y <- list(x, x, x)
obj_size(y)

# y is only 80 bytes bigger than x, which is the size of an empty list with
# three elements
obj_size(list(NULL, NULL, NULL))

# references also make it challenging to think about the size of individual 
# objects, obj_size(x) + obj_size(y) will only equal obj_size(x, y) if 
# there are no shared values
obj_size(x, y)

# because R uses a global string pool, repeating a vector 100 times does not
# take 100 times the memory
banana <- c("bananas", "bananas", "bananas")
obj_size(banana)

obj_size(rep(banana, 100))

# R 3.5.0 and later have ALTREP (alternative representation) which allows R to
# represent certain types of vectors compactly, for instance vectors that use :
# for the vector x = 1:1345 R just stores the first and last number, thus every
# vector is the same size no matter how large
obj_size(1:5)
obj_size(1:1345)

# --- 2.5 modify-in-place ------------------------------------------------------

# modifying R objects usually creates a copy but there are two exceptions
#   - objects with a single binding get special performance optimisation
#   - environments, a special type of object, are always modified-in-place
v <- c(1, 2, 3)
v[[3]] <- 4

# if an object has a single name bound to it (in this case v) then the object
# itself is changed instead of a copy of it being made

# for loops are notoriously slow in R because each iteration of the loop copies
# the data frame
x <- data.frame(matrix(runif(5 * 1e4), ncol = 5))
medians <- vapply(x, median, numeric(1))

cat(tracemem(x), "\n")

for (i in 1:5) {
   x[[i]] <- x[[i]] - medians[[i]]
 }

untracemem(x)

# each iteration copies the dataframe twice 
# we can reduce the number of copies by using a list instead of a data frame
# modifying a list uses internal C code so references are not incremented and 
# only a single copy is made
y <- as.list(x)

cat(tracemem(y), "\n")

for (i in 1:5) {
   y[[i]] <- y[[i]] - medians[[i]]
 }

untracemem(y)

# if you find yourself resorting to exotic tricks to avoid copies, it may
# be time to rewrite your function in C++

# --- 2.4.2 environments ---

# environments are always modified in place
# this property is described as reference semantics, because when you modify an
# environment all existing bindings to that environment continue to have the 
# same reference
e1 <- rlang::env(a = 1, b = 2, c = 3)
e2 <- e1

# if we change a binding the environment is modified in place so e2 gets 
# modified as well
e1$c <- 4
e2$c

# one consequence of this is that environments can contain themselves
e <- rlang::env()
e$self <- e
ref(e)

# this is a unique property of environments

# --- 2.6 unbinding and the garbage collector ----------------------------------

# consider the code:
x <- 1:3
x <- 2:4
rm(x)

# we created two objects, but when the code finishes neither object is bound 
# to a name, yet both take up memory
# the garbage collector (GC) frees memory by deleting R objects that are no
# longer used and requesting more memory from the OS if necessary
# GC runs automatically whenever R needs more memory to create a new object

# to print a message to console every time that the garbage collector runs
gcinfo(TRUE)

# GC can be forced with the command gc(), there is never a need to call gc()
# but you might want to so that R returns memory to your OS, gc() also tells
# you how much memory is being used

# lobstr::mem_used() is a wrapper around gc() that prints the total number
# of bytes used
mem_used()

#-------------------------------------------------------------------------------
# 3 Vectors
#-------------------------------------------------------------------------------

# --- 3.1 introduction & quiz --------------------------------------------------

# there are two types of vectors: atomic vectors and lists
# in atomic vectors all elements must have the same type
# in lists elements can have different types
# NULL is closely related to vectors and serves the role of a generic zero 
# length vector

# every vector can also have attributes which is a named list of arbitrary
# metadata
# two attributes are particularly important: the dimension attribute turns 
# vectors into matrices and arrays, the class attribute powers the S3
# objects system

# 1) What are the four common types of atomic vectors? What are the two rare types?
# 2) What are attributes? How do you get them and set them?
# 3) How is a list different from an atomic vectors? How is a matrix different
#    from a data frame?
# 4) Can you have a list that is a matrix? Can a data frame have a column that
#    is a matrix?
# 5) How do tibbles behave differently from data frames?

# --- 3.2 atomic vectors -------------------------------------------------------

# there are four primary types of atomic vectors:
# logical, integer, double, and character
# collectively, integer and double vectors are known as numeric vectors
 
# --- 3.2.1 scalars ---

# each of the four primary types has a special syntax to create an individual
# value, aka a scalar

# logicals can be written TRUE or FALSE or abbreviated T or F
# doubles can be specified in decimal (0.1234), scientific (1.23e4), or 
# hexadecimal (0xcafe) form, there are three special values unique to 
# doubles: Inf (infinity), -Inf, and NaN (not a number)
# integers are written the same as doubles but must be followed by an L (1234L)
# strings are surrounded by " or ', special characters are escaped with \ 
# ?Quotes gives details on what characters can be escaped with
?Quotes

# --- 3.2.2 making longer vectors with c() ---

# to create longer vectors from shorter ones, use c() for combines
lgc_var <- c(TRUE, FALSE)
int_var <- c(1L, 6L, 10L)
dbl_var <- c(1, 2.5, 4.5)
chr_var <- c("these are", "some strings")

# when inputs are atomic vectors, c() always creates another atomic vector
# ie it flattens
c(c(1, 2), c(3, 4))

# you can determine the type of vector with typeof()
typeof(dbl_var)

# --- 3.2.3 missing values ---
# R represents missing or unknown values with special sentinal value NA
# missing values tend to be infectious, most operations w/ a missing value
# will return a missing value
10 * NA
!NA

# there are only a few exceptions to this rule where the identity holds for 
# all possible inputs
NA^0
NA | TRUE
NA & FALSE

# is.na() is used to test for missingness

# --- 3.2.4 testing and coercion ---

# you can test if a vector is of a given types with is.*()
# is.logical(), is.integer(), is.double(), is.character()

# for atomic vectors, type is a property of the entire vector: all elements must
# be of the same type
# when you attemp to combine different types they will be coerced in a fixed 
# order: character -> double -> integer -> logical

# for example, combining a character and an integer yields a character
c("a", 1)

# coercion happens automatically, this is useful for logical vectors because 
# TRUE becomes 1 and FALSE becomes 0
x <- c(1, TRUE, TRUE, FALSE)

# --- 3.3 attributes -----------------------------------------------------------

# atomic vectors do not include important data structures like matrices, arrays,
# factors, or date-times
# these types are built on top of atomic vectors by adding attributes

# --- 3.3.1 getting and setting ---

# you can think of attributes as name-value pairs that attach metadata to an object
# individual attributes can be retrieved and modified with attr()
# attributes can be retrieved en masse with attributes()
# attributes can be set en masse with structure()

a <- 1:3
attr(a, "x") <- "abcdef"
attr(a, "x")

attr(a, "y") <- 4:6
attributes(a)

# or equivalently
a <- structure(
   1:3,
   x = "abcdef",
   y = 4:6
)
attributes(a)

# attributes should be though of as ephemeral, most attributes are lost by operations
attributes(a[1])

# there are only two attributes that are routinely preserved
#     names, a character vector giving each element a name
#     dim, short for dimensions, an integer vector used to turn vectors into 
#          matrices or arrays

# to preserve other attributes, you need to create your own S3 class

# --- 3.3.2 names ---

# you can name a vector in three ways

# when creating it
x <- c(a = 1, b = 2, c = 3)

# by assigning a character vector to names()
x <- 1:3
names(x) <- c("a", "b", "c")

# Inline with setNames()
x <- setNames(1:3, c("a", "b", "c"))

# names can be removed from a vector with:
x <- unname(x)
names(x) <- NULL

# --- 3.3.3 dimensions ---

# adding a dim attribute to a vector allows it to behave like a 2-dimensional
# matrix or a multi-dimensional array

# matrices and arrays can be created with matrix(), array(), or by using the 
# assignment form of dim()
x <- matrix(1:6, nrow = 2, ncol = 3)

# one vector argument to describe all dimensions
# c(2, 3, 2) = 2 rows, 3 cols, 2 arrays
y <- array(1:12, c(2, 3, 2))

# modify the list in place by setting dim()
z <- 1:6
dim(z) <- c(3, 2)

# many functions for working with vectors have generalizations for matrices
# and arrays

# vector
names()
length()
c()

# matrix
rownames()
colnames()
rbind() cbind()

# array
dimnames()
dim()
abind::abind()

# --- 3.4 S3 atomic vectors ----------------------------------------------------

# one of the most important vector attributes is class which underlies the S3
# object system
# having a class attribute turns an object into an S3 object, which means it will
# behave differently from a regular vector when passed to a generic function
# every S3 object is built on top of a base type and often stores additional
# information in other attributes

# four important S3 vectors used in base R
#     categorical data, where values come from a fixed set of levels recorded
#                       in factor vectors
#     dates, (with day resolution) which are recorded in Date vectors
#     date-times, (with second resolution) which are stored in POSIXct vectors
#     durations, which are stored in difftime vectors

# --- 3.4.1 ---

# a factor is a vector than can contain only predefined values, it is used to 
# store categorical data, factors are built on top of an integer vector with 
# two attributes: a class "factor" which makes it behave differently from 
# regular integer vectors and "levels" which defines the set of allowed values
x <- factor(c("a", "b", "b", "a"))
x

typeof(x)
attributes(x)

# --- 3.4.2 dates ---

# Date vectors are built on top of double vectors, they have class "Date" and
# no other attributes
today <- Sys.Date()
typeof(today)
attributes(today)

# the value of the double (which can be seen by stripping the class), represents
# the number of days since 1970-01-01
unclass(today)

# --- 3.4.3 date-times ---

# there are two ways of storing date-time information: POSIXct and POSIXlt
# POSIX = Portable Operating System Interface
# ct = calendar time, the time_t type in C
# lt - local time, the struct tm type in C
# POSIXct vectors are built on top of double vectors, where the value represents
# the number of seconds since 1970-01-01
now_ct <- as.POSIXct("2018-08-01 22:00", tz = "UTC")
now_ct

typeof(now_ct)
attributes(now_ct)

unclass(now_ct)

# the tzone attribute controls only how the date-time is formatted

# --- 3.4.4 durations ---

# durations represent the amount of time between pairs of dates or date-times,
# they are stored as difftimes and are built on top of doubles
one_week_1 <- as.difftime(1, units = "weeks")
one_week_1

typeof(one_week_1)
attributes(one_week_1)

# --- 3.5 lists ----------------------------------------------------------------

# lists are a step up in complexity from atomic vectors because each element
# can be any type
l1 <- list(1:3, "a", c(TRUE, FALSE, TRUE), c(2.3, 5.9))
typeof(l1)
str(l1)

# because elements of a list are references to values in memory, creating a 
# list does not use much memory
obj_size(l1)

# lists are sometimes called recursive vectors because a list can contain other lists
l3 <- list(list(list(1)))
str(l3)

# you can test for a list and coerce to a list with:
is.list()
as.list()

# you can turn a list into an atomic vector with unlist(), but the rules regarding
# this operation are complex and not always equivalent to what you would get with c()
x <- list(1, 3, 4, 5)
x <- unlist(x)

# --- 3.5.3 matrices and arrays ---

# with atomic vectors the dimension attribute is commonly used to create matrices
# with lists the dimension attribute can be used to create list-matrices or 
# list-arrays
l <- list(1:3, "a", TRUE, 1.0)
dim(l) <- c(2, 2)

# --- 3.6 data frames and tibbles ---

# the two most important S3 vectors built on top of lists are data frames and 
# tibbles 

# a data frame is a named list of vectors with attributes for column names, 
# row names, and it's class: "data.frame"
df1 <- data.frame(x = 1:3, y = letters[1:3])
typeof(df1)
str(df1)
attributes(df1)

# in contrast to a regular list, a data frame has an additional constraint:
# the length of each of its vectors must be the same

# tibbles attempt to be a replacement for data frames, fixing some of the 
# short comings of data frames
# tibbles are lazy and surly: they do less and complain more
# tibbles share the same structure as data frame, the only difference is that
# the class vector is longer and includes tbl_df
library(tibble)

df2 <- tibble(x = 1:3, y = letters[1:3])
typeof(df2)
str(df2)
attributes(df2)

# --- 3.6.1 tibble / data frame differences ---

# differences between tibbles and data frames
# tibbles never coerce inputs (ie strings are never coerced to factors, etc)
# data frames automatically transform non-syntactic names (ie 1 -> X1), tibbles
# do not and instead use syntactic names surrounded by ``
# tibbles allow you to refer to variables created during construction:
tibble(x = 1:3, y = x * 2)

# printing tibbles provides more information and is displayed better
# when subsetting columns from a data frame with df[ , vars] you will get
# a vector if vars selects one column, otherwise you get a data frame
# with a tibble [] always returns a tibble
# to extract a column as a vector in a tibble or data frame use df[["col"]] instead

# test and coerce to tibble with
is_tibble()
as_tibble()

# --- 3.7 NULL ---

# NULL is special because it has a unique type, is always length 0, and can't
# have any attributes
typeof(NULL)
length(NULL)
x <- NULL
attr(x, "y") <- 1

# there are two common uses of NULL
# to represent an empty vector 
c()

# to represent an absent vector, often used for function inputs

# ------------------------------------------------------------------------------
# 4 Subsetting
# ------------------------------------------------------------------------------

# --- 4.1 introduction ---

# there are six ways to subset atomic vectors
# there are three subsetting operators, [[]], [], and $
# subsetting operators interact differently with different vector types
# subsetting can be combined with assignment 

# subsetting is a natural complement to str(), while str() shows you all the 
# pieces of any objects (its structure), subsetting allows you to pull out the 
# pieces your interested in

# --- 4.2.1 atomic vectors ---

# initialize a vector, note that the number after the decimal points represents 
# the original position in the vector
x <- c(2.1, 4.2, 3.3, 5.4)

# there are six things that you can use to subset a vector

# POSITIVE INTEGERS return elements at the specified positions
x[c(3, 1)]
x[order(x)]

# duplicate indices will duplicate values
x[c(1, 1)]

# real numbers are silently truncated to integers
x[c(2.1, 2.9)]

# NEGATIVE INTEGERS exclude elements at the specified positions
x[-c(3, 1)]

# note that you cannot mix positive and negative integers in a subset
x[c(-1, 2)]

# LOGICAL VECTORS selects elements where the corresponding logical value is TRUE
# this is the most useful type of subsetting because you can write an expression
# that uses a logical vector
x[c(TRUE, TRUE, FALSE, FALSE)]
x[x > 3]

# NOTHING returns the original vector, this is not useful for 1D vectors but is 
# very useful for matrices, data frames, and arrays
x[]

# ZERO returns a zero length vector
x[0]

# --- 4.2 lists ---

# subsetting a list works the same as subsetting an atomic vector, [] returns 
# a list and [[]] / $ lets you pull out elements of a list

# --- 4.3 matrices and arrays ---

# you can subset higher dimension structures in three ways:
# with multiple vectors
# with a single vector
# with a matrix

# the most common way of subsetting matrices (2D) and arrays (>2D) is a simple
# generalisation of 1D subsetting: supply a 1D index for each dimension, 
# seperated by a comma, blank subsetting is now useful because it lets you keep 
# all rows or all columns 
a <- matrix(1:9, nrow = 3)
colnames(a) <- c("A", "B", "C")
a[1:2,]

a[c(TRUE, FALSE, TRUE), c("B", "A")]

a[0, -2]

# --- 4.2.4 data frames and tibbles ---

# data frames have characteristics of both lists and matrices
# when subsetting with a single index, they behave like lists and index the 
# columns so df[1:2] selects the first two columns
# when subsetting two indices they behave like matrices so df[1:3, ] selects
# the first three rows and all columns
df <- data.frame(x = 1:3, y = 3:1, z = letters[1:3])

df[df$x == 2, ]

# there are two ways to select columns from a data frame
# like a list
df[c("x", "z")]

# like a matrix
df[, c("x", "z")]

# --- 4.2.5 preserving dimensionality ---

# subsetting a matrix of data frames with a single number will return an object
# with lower dimensionality (ie subsetting one column / row from a data frame
# returns a list / vector)
a <- matrix(1:4, nrow = 2)
str(a[1,])

# to preserve dimensionality you must use drop = FALSE
str(a[1, , drop = FALSE])

# tibbles default to drop = FALSE and [] always returns another tibble

# --- 4.3 selecting a single elements ---

# there are two other subsetting operators: [[]] and $, [[]] is used for extracting
# single items, while x$y is useful shorthand for x[["y"]]

# [[]] is most important when working with lists because [] always returns a 
# smaller list
# if list x is a train carrying objects then x[[5]] is the object in car 5 and 
# x[4:6] is a train of cars 4-6

# while you must use [[]] when working with lists it is also a good habit to 
# use this when extracting single values from atomic vectors

# --- 4.4 subsetting and assignment --------------------------------------------

# subsetting and assignment may be combined
x <- 1:5
x[c(1, 2)] <- c(101, 102)

# x[[i]] <- NULL may be used to remove a component from a list
x <- list(a = 1, b = 2)
x[["b"]] <- NULL

# --- 4.5 applications ---------------------------------------------------------

# this section covers how principles of subsetting have been incorporated into
# functions such as subset(), merge(), dplyr::arrange() etc

# --- 4.5.1 lookup tables (character subsetting) ---
x <- c("m", "f", "u", "f", "f", "m", "m")
lookup <- c(m = "Male", f = "Female", u = NA)
lookup[x]

# --- 4.5.2 matching and merging by hand (integer subsetting) ---

# you can also have more complicated lookup tables with multiple columns of 
# information, for example suppose we have a vector of integer grades and a 
# table that describes their properties
grades <- c(1, 2, 2, 3, 1)

info <- data.frame(grade = 3:1, desc = c("Excellent", "Good", "Poor"),
                   fail = c(F, F, T))

# then say that we want to duplicate the info table to match each row in grades,
# an elegant way to do this is to combine match() and integer subsetting
id <- match(grades, info$grade)
info[id, ]

# --- 4.5.3 random samples and bootstraps (integer subsetting) ---

# you can use integer indices to randomly sample or bootstrap with sample(n)
# which generates a random permutation of 1:n
df <- data.frame(x = c(1, 2, 3, 1, 2), y = 5:1, z = letters[1:5])

# select 6 bootstrap replicates
df[sample(nrow(df), 6, replace = TRUE), ]

# --- 4.5.6 removing columns from data frames ---

# you can remove individual columns with a NULL statement
df$z <- NULL

# you can use set operations to keep all but specified columns
df[setdiff(names(df), "z")]

# --- 4.5.7 subsetting by logical condition ---

df[df$x == 5 & df$y == 7, ]

# De Morgan's laws simplify negations
# !(X & Y) is the same as !X | !Y
# !(X | Y) is the same as !X & !Y

# --- 4.5.8 boolean algebra v. sets (logical and integer) ---

# it's useful to be aware of the natural equivalence between set operations
# (integer subsetting) and Boolean algebra (logical subsetting)

# using set operations (integer subsetting) when you want to find the first
# (or last) TRUE
# or if you have very few TRUEs and many FALSEs, a set representation may be 
# faster and require less storage

# which() allows you to convert a Boolean representation to an integer representation
x <- sample(10) < 4
x
which(x)

# in general avoid switching from logical to integer subsetting unless you want,
# for example, to find the first and last TRUE value

# ------------------------------------------------------------------------------
# 5 control flow
# ------------------------------------------------------------------------------

# --- 5.1 introduction ---

# two primary tools for control flow: choices and loops
# choices like if statements and switch() calls allow you to run different code
# depending on the input

# loops like for and while allow you to repeatedly run code

# --- 5.2 choices ---

# if statements returns a value, so results may be assigned
x1 <- if (TRUE) 1 else 2
x2 <- if (FALSE) 1 else 2
c(x1, x2)

# when you use a single argument without an else statement, if invisibly returns
# NULL if the condition is false

# --- 5.2.2 vectorised if ---

# given that if statements only work with a single TRUE or FALSE, handling
# vectors of values is the job of ifelse()
x <- 1:10
ifelse(x %% 5 == 0, "XXX", as.character(x))
ifelse(x %% 2, "even", "odd")

# another vectorised equivalent is the more general dplyr::case_when() which
# uses special syntax to allow any number of condition-vector pairs
library(dplyr)
dplyr::case_when(
   x %% 5 == 0 ~ "fizz buzz",
   x %% 4 == 0 ~ "fizz",
   x %% 7 == 0 ~ "buzz",
   TRUE ~ as.character(x)
   )

# --- 5.2.3 switch() ---

# closely related to if, switch() is a compact special purpose equivalent that 
# allows you to replace if statements as follows:
x_option <- function(x) {
   if (x == "a") {
      "option 1"
   } else if (x == "b") {
      "option 2"
   } else {
      stop("Invalid x value")
   }
}

x_option <- function(x) {
   switch(x,
      a = "option 1",
      b = "option 2",
      c = "option 3",
      stop("Invalid x value")
          )
}

# the last component of switch() should always throw an error, otherwise 
# unmatched inputs will invisibly return NULL

# if multiple inputs have the same output you can leave the right hand side =
# empty and the input will "fall through" to the next values, this mimics
# the behaviors of the switch statement in C
legs <- function(x) {
   switch(x,
          cow = ,
          dog = 4,
          human = ,
          chicken = 2,
          stop("Unknown input"))
}
legs("cow")

# it is possible to use switch() with a numeric x, but it is harder to read and
# has undesirable failure modes, reccomended to only use switch() with
# character inputs

# --- 5.3 loops ---

# follow the basic form: for (item in vector) perform_action

# there are two ways to terminate a for loop early:
# next exits the current iteration
# break exits the entire for loop
for (i in 1:10) {
   if (i < 3)
      next
   print(i)
   if (i >= 5)
      break
}

# --- 5.3.1 common pitfalls ---

# if generating data, make sure to preallocate the output container, otherwise
# the loop will be very slow
# the vector() function is helpful here
means <- c(1, 50, 20)
out <- vector("list", length(means))
for (i in 1:length(means)) {
   out[[i]] <- rnorm(10, means[[i]])
}

# --- 5.3.2 ---

# related tools
# while(condition) action: performs action while condition is true
# repeat(action): repeats action forever until it encounters a break

# ------------------------------------------------------------------------------
# 6 functions
# ------------------------------------------------------------------------------

# --- 6.2 function fundamentals ------------------------------------------------

# functions can be broken down into 3 components: arguments, body, environment
# there are exceptions to every rule, and in this case there is a small selection
# of primitive base functions that are implemented purely in C

# --- 6.2.1 function components ---

# formals() = list of arguments that control how you call the function
# body() = code inside the function
# environment() = the data structure that determines how the function finds the
#                 values associated with the names

# formals and body are explicity specified, the environment is implicitly 
# specified based on where you defined the function
f02 <- function(x, y) {
   # A comment
   x + y
}

formals(f02)
body(f02)
environment(f02)

# --- 6.2.2 primitive functions ---

# there is one exception to the rule that a function has three components, 
# primitive functions like sum() and [] call C code directly
sum
`[`

# they have either type builtin or type special
typeof(sum)
typeof(`[`)

formals(sum)
body(sum)
environment(sum)

# primitive functions are found only in the base package, they have performance 
# advantages but are harder to write, for this reason R-core only uses C
# based functions when there is no other options

# --- 6.2.3 first class functions ---

# R functions are objects in their own right, a language property called
# "first-class functions"
# thus you can do normal operations with functions such as put them in a list
funs <- list(
   half = function(x) x / 2,
   double = function(x) x * 2
)
funs$double(10)

# --- 6.3 function composition ---

# base R provides two ways to compose multiple function calls
# for example imagine you want to compute population std. dev. using sqrt() and mean()
square <- function(x) x^2
deviation <- function(x) x - mean(x)

# you can nest function calls:
x <- runif(100)
sqrt(mean(square(deviation(x))))

# or save intermediate results as variables:
out <- deviation(x)
out <- square(out)
out <- mean(out)
out <- sqtr(out)

# the magrittr package provides a third option, the binary operator %>%
# which is called the pipe and is pronounced "and then"
library(magrittr)

x %>% deviation() %>% square() %>% mean() %>% sqrt()

# x %>% f() is equivalent to f(x); x %>% f(y) is equivalent to f(x, y)
# the pipe allows you to focus high level composition of functions rather than
# low level flow of data
# the focus is on whats being done (the verbs) rather than on what's being
# modified (the nouns)
# this style is common in Haskell and F# and is the default style in stack
# based programming languages like Forth and Factor

# --- 6.4 lexical scoping ------------------------------------------------------

# assignment = the act of binding a name to a value
# scoping = the act of finding the value associated with a name

# a deeper understanding of scoping allows you to write more advanced functional
# programming tools and eventually write tools that translate R code into 
# other langauges

# R uses lexical scoping: it looks up values of names based on how the function
# is defined, not how it is called

# R's lexical scoping follows four primary rules:
# name masking
# functions versus variables
# a fresh start
# dynamic lookup

# --- 6.4.1 name masking ---

# names defined inside a function mask names defined outside a function
x <- 10
y <- 20
g02 <- function() {
   x <- 1
   y <- 2
   c(x, y)
}
g02()

# if a name isn't defined inside a function, R looks one level up
x <- 2
g03 <- function() {
   y <- 1
   c(x, y)
}
g03()

# this still doesn't change the previous value of y
y

# the same rules apply if a function is defined inside another function
# first R looks inside the current function, then it looks where that function
# was defined, and so on all the way up to the global environment

# --- 6.4.3 fresh start ---

# what happens to values between invocations of a function?
g11 <- function() {
   if (!exists("a")) {
      a <- 1
   } else {
      a <- a + 1
   }
   a
}
g11()

# this function returns the same value each time, every time a function is called
# a new environment is created to host its execution, each invocation is completely
# independent, although there are ways to get around this

# --- 6.4.4 dynamic lookup ---

# lexical scoping deterimines where, but not when to look for values
# R looks for values when a function is run, not when the function is created
# thus output of a function can differ depending on objects outside the 
# function's environment
g12 <- function() x + 1
x <- 15
g12()

x <- 20
g12()

# to detect this possible issue
codetools::findGlobals()
# this function finds all external dependencies (unbound symbols) within a function

codetools::findGlobals(g12)
# the fact that "+" shows up in the output reveals why R has the behavior of looking
# outside of functions for variables, R uses lexical scoping for everything from
# mean() to + and {
# all R functions are using the same lexical scoping to find what values to 
# pass into the function

# --- 6.5 lazy evaluation ------------------------------------------------------

# in R function arguments are lazily evaluated: they are only evaluated if accessed

# for instance, the function below does not return an error because x is never used
h01 <- function(x) {
   10
}
h01(stop("This is an error!"))

# --- 6.5.1 promises ---

# lazy evaluation is powered by a data structure called a promise (or a thunk)
# this is one of the features that makes R unique

# a promise has 3 components:
#  an expression like x + y which gives rise to delayed computation
#  an environment where the expression should be evaluated
#  this makes sure that the following returns 11 instead of 101
y <- 10
h02 <- function(x) {
   y <- 100
   x + 1
}
h02(y)

#  a value is computed and cached the first time a promise is accessed when 
#  the expression is evaluated in a specified environment, this ensures that the 
#  promise is evaluated at most once, and is why you see "Calculating..." printed
#  only once in the following example
double <- function(x) {
   message("Calculating...")
   x * 2
}
h03 <- function(x) {
   c(x, x)
}

h03(double(20))

# you cannot manipulate promises with R code, promises are like a quantum state,
# any attempt to inspect them with R code will force an immediate evaluation,
# making the promise disappear

# x + y is a potential computation, any attempt to inspect will cause the 
# actual computation to be performed

# --- 6.5.2 default arguments ---

# thanks to lazy evaluation, default values can be defined in terms of other arguments,
# however, this is not reccomended
h04 <- function(x = 1, y = x*2, z = a + b) {
   a <- 10
   b <- 100
   c(x, y, z)
}
h04()

# --- 6.6 ... (dot-dot-dot) ----------------------------------------------------

# functions can have a special argument ... , with it a function can take any 
# number of additional arguments, in other programming languages this argument
# is often called varargs (variable arguments) and a function that uses it is 
# said to be variadic
# you can also use ... to pass those additional arguments on to another function

i01 <- function(y, z) {
   list(y = y, z = z)
}

i02 <- function(x, ...) {
   i01(...)
}

str(i02(x = 1, y = 2, z = 3))

# one can use list(...) which evaluates arguments and stores them in a list
i04 <- function(...) {
   list(...)
}
str(i04(a = 1, b = 2))
i04(a = 1, b = 2)

# --- 6.7 exiting a function ---------------------------------------------------

# most functions exit one of two ways: they return a value indicating success
# or they throw an error indicating failure

# --- 6.7.1 implicit versus explicit returns ---

# there are two ways that a function can return a value

# implicitly, where the last evaluated expression is the return value:
j01 <- function(x) {
   if (x < 10) {
      0
   } else {
      10
   }
}
j01(5)
j01(15)

# explicitly, by calling return()
j02 <- function(x) {
   if (x < 10) {
      return(0)
   } else {
      return(10)
   }
}
j02(5)
j02(15)

# --- 6.7.2 invisible values ---

# most functions return visibly: calling the function in an interactive context
# prints the result
j03 <- function() {}
j03()

# however, you can prevent automatic printing by applying invisible to the last value
j04 <- function() invisible()
j04()

# --- 6.7.4 exit handlers ---

# sometimes a function needs to make termporary changes to the global state, to 
# ensure that these changes are undone and that the global state is restored no
# matter how the function exits, use on.exit() to set up an exit handler
j06 <- function(x) {
   cat("Hello\n")
   on.exit(cat("Goodbye!\n"), add = TRUE)
   
   if (x) {
      return(10)
   } else {
      stop("error")
   }
}
j06(TRUE)
j06(FALSE)

# always set add = TRUE when using on.exit(), else each call to on.exit() will
# overwrite the previous exit handler

# example to change working directory in a function and then change it back on exit
with_dir <- function(dir, code) {
   old <- setwd(dir)
   on.exit(setwd(old), add = TRUE)
}

# --- 6.8 function forms -------------------------------------------------------

# to understand computations in R, two slogans are helpful:
#  everything that exists is an object
#  everything that happens is a function call

# while everything that happens in R is a result of a function call, not all calls
# look the same, function calls come in four varieties

# prefix = the function name comes before its arguments: foofy(a, b, c)
# infix = the function name comes in between its arguments: x + y
# replacement: functions that replace values by assignment: names(df) <- c("a", "b", "c")
# special: functions like [[]], if, and for

# any function can be written in prefix form

# --- 6.8.1 rewriting to prefix form ---

# examples of functions being rewritten to prefix form:
x + y
`+`(x, y)

names(df) <- c("x", "y", "z")
`names<-`(df, c("x", "y", "z"))

for (i in 1:10) print(i)
`for`(i, 1:10, print(i))

# the ability to rewrite fundamental functions such as `(` becomes useful
# when writing a domain specific language or translating to another language

# --- 6.8.2 prefix form ---

# arguments in prefix calls can be specified in 3 ways:
# by position: help(mean)
# by using partial matching: help(top = mean) **not recommended
# by name: help(topic = mean)

# --- 6.8.3 infix form ---

# R comes built in w/ a number of infix operators:
# :, ::, :::, $, @, ^, *, /, +, -, >, >=, <, <=, ==, !=, !, &, &&, |, ||, ~, <-, <<-

# you can create your own infix functions that start and end with %, base R uses
# this pattern to define: %%, %*%, %/%, %in%, %o%, %x%

# defining your own infix function is simple, you create a two argument function
# and bind it to a name that starts and ends with %
`%+%` <- function(a, b) paste0(a, b)
"new" %+% "string"

# all infix functions require two arguments except for + and -
-1
+10

# --- 6.8.4 replacement functions ---

# replacement functions act like they modify their arguments in place and have
# the special name xxx<-
# they must have arguments named x and value, and must return a modified object

# for example, the following functions modifies the second element of a vector
`second<-` <- function(x, value) {
   x[2] <- value
   x
}

# --- 6.8.5 special forms ---

# special forms such as `(` and `for` tend to be primitive functions (written in C)
# so there is little information that can be extracted about them from within R

# ------------------------------------------------------------------------------
# 7 Environments
# ------------------------------------------------------------------------------

# --- 7.1 introduction ---------------------------------------------------------

# the environment is the data structure that powers scoping

library(rlang)

# --- 7.2 environment basics ---------------------------------------------------

# an environment is similar to a named list with 4 important exceptions
#  every name must be unique
#  the names in an evironment are not ordered
#  an environment has a parent
#  environments are not copied when modified

# --- 7.2.1 basics ---

# to create an environment use rlang::env(), it works like a list() taking a set
# of name-value pairs
e1 <- env(
   a = FALSE,
   b = "a",
   c = 2.3,
   d = 1:3
)

# the job of an environment is to bind a set of names to a set of values

# env_print() can be used to get information about an environment
env_print(e1)

# env_names() can be used to a get a character vector the current bindings
env_names(e1)

# --- 7.2.2 important environments ---

# the current environment is the environment in which code is currently executing
current_env()

# when your computing interactively, that is usually the global environment or
# global_env(), this is sometimes called the "workplace" as it's where all 
# interactive (outside of a function) computation occurs
global_env()

# identical() must be used to check whether environments are equal
identical(current_env(), global_env())

# --- 7.2.3 parents ---

# every environment has a parent environment, the parent is what's used to 
# implement lexical scoping, if a name is not found in an environment then R
# will look in its parent and so on

# you can set the parent environment
e2a <- env(d = 4, e = 5)
e2b <- env(e2a, a = 1, b = 2)

# if you do not manually supply an environment, the parent environment defaults
# to the current environment
env_parent(e2b)
env_parents(e2b)

# by default env_parents() stops when it gets to the global environment, this is
# useful because the ancestors of the global environment include every attached package

# --- 7.2.4 super assignment ---

# regular assignments (<-) always creates a variable in the current environment,
# super assignment (<<-) never creates a variable in the current environment but
# instead modifies an existing variable found in the parent environment
x <- 0 
f <- function() {
   x <<- 1
}
f()
x

# *note: if <<- does not find an existing variable, it will create one in the 
# global environment

# --- 7.2.5 getting and setting ---

# you can get and set elements of an environment with $ and [[]]

# get(), assign(), exists(), and rm() are designed for use with the current
# environment, so using these functions in other environments is not reccomended

# --- 7.3 recursing over environments ------------------------------------------

# if you want to operate on every ancestor of an environment, it is often convenient
# to write a recursive function

# the where() function has two arguments, the name to look for (as a string) and
# the environment in which to start the search, we define the where function:
where <- function(name, env = caller_env()) {
   if (identical(env, empty_env())) {
      # Base case
      stop("Can't find ", name, call. = FALSE)
   } else if (env_has(env, name)) {
      # Success case
      env
   } else {
      # Recursive case
      where(name, env_parent(env))
   }   
}

x <- 5
where("x")

where("mean")

# --- 7.4 special environments -------------------------------------------------

# most environments are not created by you with env() but are instead created by R

# --- 7.4.1 package environments and the search path ---

# each package attached by library() or require() becomes one of the parents of
# the global environment, the immediate parent of the global environment is the 
# last package that you attached

# if you follow all parents back, you see the order in which every package has
# been attached, this is the search path
base::search()
rlang::search_envs()

# --- 7.4.2 the function environment ---

# a function binds the current environment when it is created, this is called
# the function environment and is used for lexical scoping
# across computer languages, functions that capture (or enclose) their environments
# are called closures
# you can get the function environment with fn_env()
y <- 1
f <- function(x) x + y
fn_env(f)
environment(f)

# --- 7.4.4 execution environment ---
h <- function(x) {
   a <- 2 
   x + a
}
y <- h(1)

# because of the fresh start principle, the function environment is ephemeral
# and resets every time

# --- 7.5 call stacks ----------------------------------------------------------

# the caller environment is accessed with rlang::caller_env()
caller_env()

# this provides the environment from which a function was called
# when executing a function there are two contexts

#  the execution environment is a child of the function environment which is 
#  determined by where the function was created

#  the call stack is the context created by where the function was called

# --- 7.5.1 simple call stacks ---
f <- function(x) {
   g(x = 2)
}
g <- function(x) {
   h(x = 3)
}
h <- function(x) {
   stop()
}

# the way you most commonly see a call stack in R is by looking at traceback()
# after an error has occured
f(x = 1)

# instead of traceback, one can use lobstr::cst() to print out a call stack tree
h <- function(x) {
   lobstr::cst()
}
f(x = 1)

# --- 7.5.3 frames ---

# each element of the call stack is a frame, also known as an evaluation context
# the frame is an extremely important internal data structure and R code can 
# access a small part of the data structure because tampering with it breaks R

# a frame has 3 key components

#  an expression labelled expr giving the function call, this is what traceback
#  prints out

#  an environment labelled env which is the execution environment of a function

# a parent, a previous call in the call stack

#-------------------------------------------------------------------------------
# 8 Conditions
#-------------------------------------------------------------------------------

# the condition system provides a paired set of tools that allow the author of
# a function to indicate that something unusual is happening
# the author signals conditions with stop(), warning(), message(), and the user
# can handle the errors with tryCatch() and withCallingHandlers()

# --- 8.2 signalling conditions ------------------------------------------------

# three conditions that can be signalled: errors, warnings, messages
stop("This is an error")
warning("This is a warning")
message("This is a message")

# --- 8.3 ignoring conditions --------------------------------------------------

# the simplest way of handling conditions in R is to simply ignore them
#  ignore errors with try()
#  ignore warnings with suppressWarnings()
#  ignore messages with suppressMessages()

f1 <- function(x) {
   log(x)
   10
}
f1("x")

f2 <- function(x) {
   try(log(x))
   10
}
f2("x")

# --- 8.4 handling conditions --------------------------------------------------

# two functions, tryCatch() and withCallingHandlers() allow us to register handlers,
# functions that take the signalled condition as their single argument
tryCatch(
   error = function(cnd) {
      # code to run when error is thrown
   },
   code_to_run_while_handlers_are_active
)

withCallingHandlers(
   warning = function(cnd) {
      # code to run when warning is signalled
   },
   message = function(cnd) {
      # code to run when message is signalled
   },
   code_to_run_while_handlers_are_active
)

# --- 8.4.1 condition objects ---

# so far we have just signalled conditions and not looked at the objects
# created behind the scenes
# the easiest way to see a condition object is to catch one from a signalled condition
# with rlang::catch_cnd()
cnd <- catch_cnd(stop("Error"))
str(cnd)

# built in conditions are lists with two elements, the message displayed to the 
# user and the call which triggered the condition

# to extract the call which triggered the condition
conditionCall(cnd)

# --- 8.4.2 exiting handlers ---

# tryCatch() registers exiting handlers and allows you to override default error
# behaviour
f3 <- function(x) {
   tryCatch(
      error = function(cnd) NA,
      log(x)
   )
}
f3("x")

# in the above example the error that would normally be induced by log("x") is
# overridden 

# handler functions are called with a single argument, the condition objects which 
# has been called cnd by convention
tryCatch(
   error = function(cnd) {
      paste0("--", conditionMessage(cnd), "--")
   },
   stop("Error")
)

# tryCatch has one other argument: finally, which specifies a block of code to run
# regardless of whether the initial expression succeeds or fails, this is functionally
# equivalent to on.exit()

# --- 8.5 custom conditions ----------------------------------------------------

# rlang::abort makes it easy to add metadata to conditions
# glue::glue in conjunction with rlang::abort allows for adaptive error messages
abort_bad_argument <- function(arg, must, not = NULL) {
   msg <- glue::glue("`{arg}` must {must}")
   if (!is.null(not)) {
      not <- typeof(not)
      msg <- glue::glue("{msg}; not {not}.")
   }
   
   abort("error_bad_argument",
         message = msg,
         arg = arg,
         must = must,
         not = not
         )
}

# we can now write a log function with good error aborting
my_log <- function(x, base = exp(1)) {
   if (!is.numeric(x)) {
      abort_bad_argument("x", must = "be numeric", not = x)
   }
}
my_log("a")

#-------------------------------------------------------------------------------
# functional programming
#-------------------------------------------------------------------------------

# R is a functional language, which means that it lends itself to a style of 
# problem solving centred on functions

# a functional style tends to create functions which can easily be analysed in 
# isolation (ie using only local information) and hence is much easier to 
# automatically optimise or parallelise

# functional programming is complementary to object oriented progamming

# every programming language has functions, so what makes a language functional?
# 1 functional languages have first class functions, functions that behave like any
# other data structure
# 2 functional languages require functions to be pure, a function is pure if:
#  the output only depends on the inputs
#  the function has no side effects like changing the value of a global variable

# --- functional style ---

# the functional style generally means dcomposing big problems into smaller pieces,
# then solving each piece with a function or combination of functions

#-------------------------------------------------------------------------------
# 9 Functionals
#-------------------------------------------------------------------------------

# a functional is a function that takes a function as an input and returns a 
# vector as an output
randomise <- function(f) f(runif(1e3))
randomise(mean)
randomise(sum)

# this chapter focuses on functionals provided by the purrr package
library(purrr)

# --- 9.2 map() ----------------------------------------------------------------

# the most fundamental functional is purrr:map(), it takes a vector and a 
# function, calls the function once for each element of the vector, and returns
# the results in a list
# map(1:3, f) is equivalent to list(f(1), f(2), f(3))
triple <- function(x) x*3
map(1:3, triple)

# in mathematics, map refers to an operation that associates each element of a 
# given set with one or more elements of a second set

# the implementation of map() is quite simple
simple_map <- function(x, f, ...) {
   out <- vector("list", length(x))
   for (i in seq_along(x)) {
      out[[i]] <- f(x[[i]], ...)
   }
   out
}

# the real purrr::map() is written in C to eke out every last iota of performance

# the base equivalent of map() is lapply()

# --- 9.2.1 producing atomic vectors ---

# map() returns a list which makes it the most general of the map family because
# you can put anything in a list, but it is inconvenient to return a list when a 
# data structure will suffice
# there are four variants of map that return an atomic vector of a specified type:
map_chr() # character vector
map_lgl() # logical vector
map_int() # integer vector
map_dbl() # double vector

# base R has two functions that can return atomic vectors
sapply()
vapply()

# --- 9.2.2 anonymous functions and shortcuts ---

# instead of using map() with an existing function, you can create an inline
# anonymous function that is not defined as a global variable
map_dbl(mtcars, function(x) length(unique(x)))

# anonymous functions are useful but the syntax is verbose, purrr supports
# a shortcut for using inline functions with map()
map_dbl(mtcars, ~ length(unique(.x)))

# you can see what is happening behind the scenes with as_mapper()
as_mapper(~ length(unique(.x)))

# --- 9.2.3 passing arguments with ... ---

# it is often convenient to pass along additional arguments to the function
# that you are calling
# for example, you might want to pass na.rm = TRUE along to mean()
x <- list(1:5, c(1:10, NA))

# because the maps functions pass ... along there is a simple form available
map_dbl(x, mean, na.rm = TRUE)

#-------------------------------------------------------------------------------
# Purrr style
#-------------------------------------------------------------------------------

# example of how to use multiple purrr functions to solve a moderately realistic
# problem: fitting a model to each subgroup and extracting a coefficient of the model
by_cyl <- split(mtcars, mtcars$cyl)

# now imagine we want to fit a linear model to each group then extract slope

# purrr solution
by_cyl %>% 
   map(~ lm(mpg ~ wt, data = .x)) %>%
   map(coef) %>%
   map_dbl(2)

# base R solution
models <- lapply(by_cyl, function(data) lm(mpg ~ wt, data = data))
vapply(models, function(x) coef(x)[[2]], double(1))

# for loop solution
slopes <- double(length(by_cyl))
for (i in seq_along(by_cyl)) {
   model <- lm(mpg ~ wt, data = by_cyl[[i]])
   slopes[[i]] <- coef(model)[[2]]
}
slopes

# --- 9.4 map variants ---------------------------------------------------------

# there are 23 primary variants of map(), but they can be captured with five
# main ideas

# output same type as input with modify()
# iterate over two inputs with map2()
# iterate with an index using imap()
# return nothing with walk()
# iterate over any number of inputs with pmap()

# there are four quantities:
# one argument, two arguments, one argument + index, N arguments

# and there are four output types
# list, atomic, same type, nothing

# one argument + list = map()
# one argument + list = map2()
# one argument & index + list = imap()
# N arguments + list = pmap()

# one argument + atomic = map_dbl()

# two arguments + same type = modify2()

# N arguments + nothing = pwalk()

# --- 9.4.1 same type of output as input ---

# suppose you want to double every column in a data frame, the problem with map()
# is that it returns a list
# you can use modify() which always returns the same type of output as input
df <- data.frame(x = 1:3, y = 6:4)
modify(df, ~ .x * 2)

# a basic implementation of modify is as follows:
simple_modify <- function(x, f, ...) {
   for (i in seq_along(x)) {
      x[[i]] <- f(x[[i]], ...)
   }
}

# --- 9.4.2 two inputs: map2() and friends ---

# how would you find a weighted mean when you have a list of observations
# and a list of weights?
xs <- map(1:8, ~ runif(10))
xs[[1]][[1]] <- NA

ws <- map(1:8, ~ rpois(10, 5) + 1)

# you can use map_dbl to compute the unweighted means:
map_dbl(xs, mean)

# but passing ws as an additional argument doesn't work because arguments after
# .f are not transformed

# to address this, we use map2() which is vectorised over two arguments, this means
# that both .x and .y are varied in each call to .f
map2_dbl(xs, ws, weighted.mean)

# additional arguments go after the two vector input arguments
map2_dbl(xs, ws, weighted.mean, na.rm = TRUE)

# --- 9.4.4 iterating over values and indices ---

# there are three basic ways to loop over a vector with a for loop:
#  loop over the elements: for (x in xs)
#  loop over the numeric indices: for (i in seq_along(xs))
#  loop over the names: for (nm in names(xs))

# the first form is analogous to the map() family
# the second and third are analogous to the imap() family which allows you to 
# iterate over values and the indices of a vector in parallel

# --- 9.4.5 any number of inputs: pmap() ---

# with pmap() you supply arguments in a single list

# using the weighted mean example from above
pmap_dbl(list(xs, ws), weighted.mean)

# --- 9.5 Reduce family --------------------------------------------------------

# after the map family the next most important family of functions is the reduce 
# family which is used less commonly and only has two main variants

# --- 9.5.1 basics ---

# reduce() takes a vector of length n and produces a vector of length 1 by calling
# a function with a pair of values at a time: reduce(1:4, f) is equivalent to
# f(f(f(1, 2), 3), 4)

# imagine you have a list of numeric vectors, and you want to find the values that
# occur in every element of the list
l <- map(1:4, ~ sample(1:10, 15, replace = T))
str(l)

# to solve this you would need to use intersect() repeatedly
out <- l[[1]]
out <- intersect(out, l[[2]])
out <- intersect(out, l[[3]])
out <- intersect(out, l[[4]])

# reduce() automates this solution
reduce(l, intersect)

# if we want to list all elements that appear in at least one entry
reduce(l, union)

# the essence of reduce() can be reduced to a simple wrapper around a for loop:
simple_reduce <- function(x, f) {
   out <- x[[1]]
   for (i in seq(2, length(x))) {
      out <- f(out, x[[i]])
   }
   out
}

# --- 9.5.2 accumulate ---

# the reduce() variant accumulate() is useful because it doesn't just return
# the final result, but all of the intermediate results as well
accumulate(l, intersect)

x <- c(4, 3, 10)
reduce(x, `+`)

accumulate(x, `+`)

# --- 9.6 predicate functionals ------------------------------------------------

# a predicate is a function that returns a single T or F like is.character(),
# is.null(), all()
# we say that a predicate matches a vector if it returns TRUE

# --- 9.6.1 basics ---

# purrr provides seven useful functions which come in 3 groups for evaluating
# predicates

# some(.x, .p) returns TRUE if any element matches
# every(.x, .p) returns TRUE if all elements match
# none(.x, .p) returns TRUE if no element matches

# detect(.x, .p) returns the value of the first match
# detect_index(.x, .p) returns the location of the first match

# keep(.x, .p) keeps all matching elements
# discard(.x, .p) drops all matching elements

df <- data.frame(x = 1:3, y =c("a", "b", "c"))
detect(df, is.factor)
str(keep(df, is.factor))

# --- 9.7 base functionals -----------------------------------------------------

# --- 9.7.1 matrices and arrays ---

# map() and friends are specialized to work with one-dimensional vectors, 
# base::apply() is specialized to work with two-dimensional and higher vectors
# apply() is essentially an operation that summarises a matrix or array by
# collapsing each row or column to a single value
# the MARGIN argument specifies whether to summarise over rows (1) or columns (2)

a2d <- matrix(1:20, nrow = 5)
apply(a2d, 1, mean)
apply(a2d, 2, mean)

# never use apply() with a data frame, it always coerces to a matrix which leads
# to undesirable results if you data frame contains anything other than numbers

#-------------------------------------------------------------------------------
# 10 Function Factories
#-------------------------------------------------------------------------------

# --- 10.1 introduction --------------------------------------------------------

# a function factory is a function that makes functions 
power1 <- function(exp) {
   function(x) {
      x^exp
   }
}

square <- power1(2)
cube <- power1(3)

# we have already learned about the individual components that make function
# factories possible
#  6.2.3 - functions in R are first-class functions
#  7.4.2 - functions capture (enclose) the environment in which they are created
#  7.4.4 - a functions creates a new execution environment every time it is run

# --- 10.2 factory fundamentals ------------------------------------------------

# the enclosed environment of the manufactured function is an execution environment
# of the function factory 
square
cube

# it is obvious where x comes from, but where does exp come from
library(rlang)
env_print(square)
env_print(cube)

# we see that the environments have the same parent, which is the enclosing environment
# of power(), the global environment

# looking at the function environment allows us to extract the value of exp
fn_env(square)$exp
fn_env(cube)$exp

# this is what makes manufactured functions behave differently from one another,
# names in the enclosing environment are bound to different values

# --- 10.2.4 stateful functions ---

# the usual assignment operator <- always creates a binding in the current
# environment, the super assignment operator <<- rebinds an existing name 
# found in a parent environment

# the following example shows how the super assignment operator can be used
# to count how many times it has been called
new_counter <- function() {
   i <- 0 
   
   function() {
      i <<- i + 1
      i
   }
   
}

counter_one <- new_counter()
counter_one()
counter_one()

#-------------------------------------------------------------------------------
# 11 Function Operators
#-------------------------------------------------------------------------------

# --- 11.1 introduction --------------------------------------------------------

# a function operator is a function that takes one (or more) functions as input 
# and returns a function as output
chatty <- function(f) {
   force(f)
   
   function(x, ...) {
      res <- f(x, ...)
      cat("Processing ", x, "\n", sep = "")
      res
   }
}
f <- function(x) x ^ 2
s <- c(3, 2, 1)

purrr::map_dbl(s, chatty(f))

# function operators are closely related to function factories; indeed they are 
# just a function factory that takes a function as input
# in python, decorators is just another name for functional operators

# --- 11.2 existing function operators -----------------------------------------

# there are two useful function operators that give a sense of what function 
# operators can do: purrr::safely() and memoise::memoise()
library(purrr)
library(memoise)

# --- 11.2.1 capturing errors with purrr::safely() ---

# one advantage of for-loops is that if one of the iteration fails, you can still
# access all the results up to the failure, if you do the same with a functional
# such as map you get no output which makes it more difficult to find where the
# problem is

x <- list(
   c(0.512, 0.165, 0.717),
   c(0.064, 0.781, 0.427),
   c(0.890, 0.785, 0.495),
   "oops"
)

out <- rep(NA_real_, length(x))
for (i in seq_along(x)) {
   out[[i]] <- sum(x[[i]])
}
out

# purrr::safely() provides a tool to help with this problem, safely() is a function
# operator that transforms a function to turn errors into data
out <- map(x, safely(sum))
str(out)

# the output is in a slightly inconvenient form where each list contains the 
# results and the error, we can make the output easier to use with 
# purrr::transpose() so that we get a list of results and errors
out <- transpose(map(x, safely(sum)))
out

# --- 11.2.2 caching computations with memoise::memoise() ---

# the memoise() function will remember previous inputs and return cached results,
# memoisation is an example of the classic computer science tradeoff of memory
# versus speed
# a memoised function can run much faster but because it stores all of the 
# previous inputs and outputs, it uses memory
slow_function <- function(x) {
   Sys.sleep(1)
   x * 10 * runif(1)
}

system.time(print(slow_function(1)))
system.time(print(slow_function(1)))

# with memoisation
fast_function <- memoise(slow_function)
system.time(print(fast_function(1)))
system.time(print(fast_function(1)))

# this is an example of dynamic programming, where a complex problem can be broken
# down into many overlapping subproblems, and remembering the results of a subproblem
# considerably improves performance

#-------------------------------------------------------------------------------
# Object-oriented Programming Introducion
#-------------------------------------------------------------------------------

# Object-oriented programming (OOP) is more challenging in R than in other 
# languages because there are multiple OOP systems to choose from

# the most important are S3, R6, and S4
# S3 and S4 are provided by base R
# R6 is provided by the R6 package and is similar to the Reference Class or RC
# from base R

# S3 and S4 use generic function OOP which is rather different than encapsulated
# OOP used by most languages today

# in R, functional programming is much more important that OOP because typically
# you want to solve complex problems by decomposing them into simple functions,
# not simple objects

# S3 allows your functions to return rich results with user-friendly display
# and programmer friendly internals. S3 is used throughout base R.

# R6 provides a standardised way to escape R's copy on modify semantics, this is 
# particularly important if you want to model objects that exist independently of
# R. A common need for R6 is to model data that comes from a web API and where
# changes come from inside or outside of R.

# S4 is a rigorous system that forces you to think carefully about program design.
# It's well-suited for building large systems that evolve over time and will 
# receive contributions from many programmers. 

# --- OOP systems --------------------------------------------------------------

# the main reason to use OOP is polymorphism (literally: many shapes), 
# polymorphism means that a developer can consider a function's interface
# seperately from its implementation, making it possible to use the same function
# form for different types of input
# this is closely related to the idea of encapsulation: the user doesn't need to
# worry about the details of an object because they are encapsulated behind a 
# standard interface

# polymorphism is what allows summary() to produce different outputs for numeric
# and factor variables
diamonds <- ggplot2::diamonds

summary(diamonds$carat)
summary(diamonds$cut)

# you can imagine summary() containing a series of if-else statements, but that
# would mean only the original author could add new implementations
# an OOP system makes it possible for any developer to extend the implementations
# for new types of input

# To be more precise, OOP systems call the type of an objects its class, and an 
# implementation for a specific class is called a method. Roughly speaking, a class
# defines what an object is and methods describe what an object can do. The class
# defines the fields, the data possessed by every instance of that class. Classes
# are organized in a hierarchy so that if a method does not exist for one class,
# it's parent method is used and the child is said to inherit behavior. 
# For example, in R an ordered factor inherits from a regular factor and a 
# generalised linear model inherits from a linear model. The process of finding the 
# correct method given the class is called method dispatch.

# There are two main paradigms of OOP which differ in how classes and methods
# are related.

# In encapsulated OOP, methods belong to objects or classes and method calls
# typically look like object.method(arg1, arg2). This is called encapsulated because
# the object encapsulated both data (with fields) and behavior (with methods),
# this is the paradigm found in most programming languages.

# In functional OOP, methods belong to generic functions and method calls look
# like ordinary function calls: generic(object, arg2, arg3). This is called
# functional because from the outside it looks like a regular function call.

# --- OOP in R -----------------------------------------------------------------

# S3 is R's first OOP system. S3 is an informal implementation of functional OOP
# and relies on common conventions rather than ironclad guarantees.

# S4 is a formal and rigorous rewrite of S3. It requires more upfront work than 
# S3 but in return provides more guarantees and greater encapsulation. S4 is 
# implemented in the base package.

# RC implements encapsulated OOP. RC objects are a special types of S4 objects 
# that are also mutable, instead of using R's usual copy-on-modify semantics, 
# they can be copies in place.

# R6 implements encapsulated OOP like RC, but solves some important issues.

# --- sloop --------------------------------------------------------------------

library(sloop)

# the sloop package ("sail the seas of OOP") provides a number of helpers that 
# are missing from base R
# sloos::otype() makes it easy to figure out what OOP system is being used
# by an object

otype(1:10)
otype(mtcars)

#-------------------------------------------------------------------------------
# 12 Base types
#-------------------------------------------------------------------------------

# --- 12.1 introduction --------------------------------------------------------

# R contains both Base objects and OO (object oriented) objects, and it is 
# important to distinguish between them

# --- 12.2 Base versus OO objects ----------------------------------------------

# a base object:
is.object(1:10)
otype(1:10)

# an OO objects
is.object(mtcars)
otype(mtcars)

# technically, the difference between base and OO objects is that OO objects have
# a class attribute
attr(1:10, "class")
attr(mtcars, "class")

# the class() function is safe to apply to S3 and S4 objects, but it returns 
# misleading results when applied to base objects

# it is safer to use sloop::s3_class() which returns the implicit class that the
# S3 and S4 systems will use to pick methods
x <- matrix(1:4, nrow = 2)
class(x)
s3_class(x)

# --- 12.3 Base types ----------------------------------------------------------

# while only OO objects have a class attribute, every object has a base type
typeof(1:10)
typeof(mtcars)

# Base types do not form an OOP system because functions that behave differently
# for different base types are primarily written in C code that uses switch
# statements (if else statement for each base type)

# R-core can create new types, but creating a new type is a lot of work because
# every switch statement needs to be modified to handle a new case, the most recent
# Base type was added in 2011

# in total there are 25 different base types, these types are most important
# in C code, so you will often see them called by their C type names which
# are included in parentheses

# --- vectors ---

# NULL (NILSXP)
typeof(NULL)

# logical (LGLSXP)
typeof(TRUE)

# integer (INTSXP)
typeof(1L)

# double (REALSXP)
typeof(2)

# complex (CPLXSXP)
typeof(1i)

# character (STRXP)
typeof("hi")

# list (VECSCP)
typeof(list(1, 2))

# raw (RAWSXP)

# --- functions ---

# closure (CLOSXP), regular R functions
typeof(mean)

# special (SPECIALSXP), internal functions
typeof(`[`)

# builtin (BUILTINSXP), primitive functions
typeof(sum)

# --- environments ---

# environment (ENVSXP)
typeof(globalenv())

# --- S4 ---

# S4 (S$SXP), used for S4 classes that don't inherit from an existing base type
mle_obj <- stats4::mle(function(x = 1) (x - 2) ^2)
typeof(mle_obj)

# --- language components ---

# symbol (SYMSXP)
typeof(quote(a))

# language (LANGSXP)
typeof(quote(a + 1))

# pairlist (LISTXP), used for function arguments
typeof(formals(mean))

# expression (EXPRSXP), special purpose type that is only returned by 
# parse() and expression()

# the remaining types are esoteric and rarely seen in R, they are important
# primarily in the C code: externalptr (EXTPTRSXP), wealred (WEAKREFSXP),
# bytecode (BCODESXP), promise (PROMSXP), ... (DOTSXP), any (ANYSXP)

# --- 12.3.1 numeric types ---

# *note: R uses numeric to refer to objects of type integer or double

#-------------------------------------------------------------------------------
# S3
#-------------------------------------------------------------------------------

# --- 13.1 introduction --------------------------------------------------------

# S3 is R's first and simplest OO system, S3 is informal and ad hoc, but there
# is a certain elegance in its minimalism: you can't take away any part of it and 
# still have a useful OO system

# for these reasons, you should use it unless you have a compelling reason to do
# otherwise

# S3 is the only OO system used in base and stats packages, and it is the most 
# commonly used system in CRAN packages

# S3 is very flexible which means it allows you to do things that are ill-advised
# this is in contrast to a strict environment such as Java
# the lack of constraints is important to the flexibility of S3
# the key is to apply constraints yourself based on a set of conventions that 
# you should always use

library(sloop)

# --- 13.2 basics --------------------------------------------------------------

# an S3 object is a base type with at least a class attribute
# for example take a factor, its base type is the integer vector, it has a class
# attribute of "factor", and a levels attribute that stores the possible levels:

f <- factor(c("a", "b", "c"))

typeof(f)
attributes(f)

# you can get the underlying base type by using unclass() which strips the class
# attribute, causing it to lose its special behaviour
unclass(f)

# an S3 object behaves differently from its underlying base type whenever its 
# passed to a generic function, the easiest way to tell if a function is generic
# is to use sloop::ftype() and look for the "generic" output
ftype(print)
ftype(str)
ftype(unclass)

# a generic function defines an interface which uses a different implementation 
# depending on the class of an argument (almost always the first argument)
# many base R functions are generic including print()
print(f)
print(unclass(f))

# beware that str() is generic, and some S3 classes use that generic to hide the 
# internal details, for example POSIXlt class used to represent date-time data
# is actually built on top of a list, a fact hidden by its str() method:
time <- strptime(c("2017-01-01", "2020-05-04 03:21"), "%Y-%m-%d")

typeof(time)
str(time)
str(unclass(time))

# the job of the generic is to define the interface (arguments) and then find
# the right implementation for the job

# the implementation for a specific class is called a method and the generic finds
# that method by performing method dispatch

# sloop:s3_dispatch() can be used to see the process of method dispatch
s3_dispatch(print(f))

# => method exists and is found by UseMethod()
# -> method exists and is used by NextMethod()
# * method exists but is not used
# Nothing (and greyed out in console): method does not exist

# methods follow the naming scheme: generic.class(), ex: print.factor()
# you should never call the method directly, but instead rely on the generic
# to find it for you 

# generally you can identify the presence of a method by seeing . in the name, 
# however, a number of important functions in base R were written before S3 and
# hence use . to join words such as data.frame

# unlike most functions, you can't see the source code for most S3 methods just 
# by typing their names, that is because S3 methods are not usually exported:
# they live only inside the package and are not available from the global environment
# instead you can use sloop::s3_get_method() 
weighted.mean.Date
s3_get_method(weighted.mean.Date)

# --- 13.3 classes -------------------------------------------------------------

# S3 has no formal definition of a class: to make an object an instance of a class
# you simply set the class attribute which can be done with structure() or after
# the fact with class <- ()

x <- structure(list(), class = "my_class")
class(x)

# the class name can be any string, but it is recommended to only use letters and
# _, avoid . so it is not confused with the . seperator between a generic name 
# and a class name

# when creating a class, it is reccomended to provide three functions: 

# a low level constructor, new_myclass() that efficiently creates new objects
# with the correct structure

# a validator, validate_myclass() that performs more computationally expensive
# check to ensure that the object has correct values

# a user friendly helper, myclass() that provides a convenient way for others to 
# create objects of your class

# you do not need a validator for very simple classes, and you can skip the 
# helper if the class is for internal use only, but you should always provide
# a constructor

# --- 13.3.1 constructors ---

# S3 doesn't provide a formal definition of class, so it has no built in way
# to ensure that all objects of a given class have the same structure (ie the
# same base type and the same attributes with the same types)

# instead you must enforce a consistent structure by using a constructor

# the constructor should follow three principles:
# be called new_myclass()
# have one argument for the base object and one for each attribute
# check the type of the base object and the types of each attribute

# ex of making a constructor for the simplest S3 class: Date
new_Date <- function(x = double()) {
   stopifnot(is.double(x))
   structure(x, class = "Date")
}

new_Date(c(-1, 0, 1))

# ex difftime constructor, difftime is built on a double as well, but it has a 
# units attribute that must take one of a small set of values:
new_difftime <- function(x = double(), units = "secs") {
   stopifnot(is.double(x))
   units <- match.arg(units, c("secs", "mins", "hours", "days", "weeks"))
   
   structure(x, class = "difftime", units = units)
   
}

new_difftime(c(1, 10, 3600), units = "secs")
new_difftime(52, "weeks")

# the constructor is a developer function, it will be called in many places
# by an experienced user so avoid time-consuming checks in the constructor

# --- 13.3.2 validators ---

# more complicated classes require more complicated checks for validity
# take factors for example, a constructor only checks that types are correct,
# making it possible to create malformed factors

new_factor <- function(x = integer(), levels = character()) {
   stopifnot(is.integer(x))
   stopifnot(is.character(levels))
   
   structure(x, levels = levels, class = "factor")
   
}

# this example does not work because there are 5 factors and only 1 level
new_factor(1:5, "a")

# this example does not work because there cannot be a 0 factor
new_factor(0:1, "a")

# rather than encumbering the constructor with complicated checks, it is better
# to put them in a seperate function, doing so allows you to cheaply create new
# objects when you know that the values are correct

validate_factor <- function(x) {
   values <- unclass(x)
   levels <- attr(x, "levels")
   
   if (!all(!is.na(values) & values > 0)) {
      stop(
         "All `x` values must be non-missing and greater than zero", call. = FALSE
      )
   }
   
   if (length(levels) < max(values)) {
      stop(
         "There must be at least as many `levels` as possible values in `x`",
         call. = FALSE
      )
   }
   
   x
}

validate_factor(new_factor(1:5, "a"))
validate_factor(new_factor(0:1, "a"))
   
# the validator function is called primarily for its side-effects, throwing an
# error is the object is invalid

# --- 13.3.3 helpers ---

# if you want users to construct objects from your class, you should also provide
# a helper method that makes their life as easy as possible

# a helper should always:
# have the same name as the class, ex myclass()
# finish by calling the constructor and validator if it exists
# create carefully crafted error messages tailored towards an end-user
# have a thoughtfully crafted user interface with carefully chosen default values
# and useful conversions

# ex, the code below shows a simple version of factor(): it takes a character
# vector and guesses that the levels should be the unique values
factor <- function(x = character(), levels = unique(x)) {
   ind <- match(x, levels)
   validate_factor(new_factor(ind, levels))
}

factor(c("a", "a", "b"))

# --- 13.4 Generics and methods ------------------------------------------------

# the job of an S3 generic is to perform method dispatch, find the specific 
# implementation for a class
# method dispatch is performed by UseMethod() which every generic calls
# UseMethod() takes two arguments: the name of the generic function (required),
# and the argument to use for method dispatch (optional)
# if the second argument is omitted it will dispatch based on the first argument
# which is almost always what is desired

# most functions consist of only a call to UseMethod(), take mean() for example
mean

# creating your own generic is similarly simple:
my_new_generic <- function(x) {
   UseMethod("my_new_generic")
}

# --- 13.4.1 method dispatch ---

# how does UseMethod() work? It creates a vector of method names,
# paste0("generic", ".", c(class(x)) "default)) and then looks for each potential
# method in turn

# sloop::s3_dispatch() gives a call to an S3 generic and lists all methods used
x <- Sys.Date()
sloop::s3_dispatch(print(x))

x <- matrix(1:10, nrow = 2)
sloop::s3_dispatch(mean(x))

# --- 13.4.2 finding methods ---

# sloop::s3_methods_generic() and sloop::s3_methods_class() allow you to find all
# methods defined for a generic or associated class
sloop::s3_methods_generic("mean")
sloop::s3_methods_class("ordered")

# --- 13.4.3 creating methods ---

# first, you should only ever write a method if you own the generic or the class,
# work with the author of either the generic or the class to add the method in
# their code

# a method must have the same arguments as its generic, this is enforced in packages
# by R CMD check

# --- 13.5 Object styles -------------------------------------------------------

# so far we have focused on vector style classes like Date and factor, these have
# the key property length(x) represents the number of observations in the vector

# there are three variants that do not have this property:

# 1, record style objects use a list of equal length vectors to represent 
# individual components of the object, the best example if POSIXlt which 
# underneath the hood is a list of 11 date-time components like year, month, day
# record style classes override length() and subsetting methods to conceal this
# implementation detail
x <- as.POSIXlt(ISOdatetime(2020, 1, 1, 0, 0, 1:3))

length(x)
length(unclass(x))

x[[1]] # the first date time
unclass(x)[[1]] # the first component, the number of seconds 

# 2, data frames are similar to record style objects in that both use lists of 
# equal length vectors, however, data frames are conceptually two dimensional and 
# the individual components are readily exposed to the user
# the number of observations is the number of rows, not the length
x <- data.frame(x = 1:100, y = 1:100)
length(x)
nrow(x)

# 3, scalar objects typically use a list to represent a single thing, for example 
# an lm object is a list of length 12 but it represents one model
mod <- lm(mpg ~ wt, data = mtcars)
length(mod)

# --- 13.6 Inheritance ---------------------------------------------------------

# S3 classes can share behaviour through a mechanism called inheritance, 
# inheritance is powered by three ideas:

# 1, the class can be a character vector, for example the ordered and POSIXct
# classes have two components in their class
class(ordered("x"))
class(Sys.time())

# ordered is a subclass of factor because it always appears before it in the 
# class vector, conversely, factor is a superclass of ordered

# 2, if a method is not found for the class in the first element of the vector,
# R looks for a method for the second class and so on
library(sloop)

s3_dispatch(print(ordered('x')))
s3_dispatch(print(Sys.time()))

s3_dispatch(print("hello"))

# 3, a method can delegate work by calling NextMethod(), note that s3_dispatch()
# reports delegation with ->
s3_dispatch(ordered("x")[1])

# --- 13.6.1 NextMethod() ---

# NextMethod() is the hardest part of inheritance to understand, so we will start
# with a conrete example of the most common use case: [

# we will start by creating a class "secret" that hides its output when printed
new_secret <- function(x = double()) {
   stopifnot(is.double(x))
   structure(x, class = "secret")
}

print.secret <- function(x, ...) {
   print(strrep("x", nchar(x)))
   invisible(x)
}

x <- new_secret(c(15, 1, 456))
x

# this works, but the default [ method does not preserve the class
s3_dispatch(x[1])
x[1]

# to fix this we need to provide a [.secret method
# one approach is to unclass the object
`[.secret` <- function(x, i) {
   x <- unclass(x)
   new_secret(x[i])
}

# this works but is inefficient because it creates a copy of x, a better approach
# is to use NextMethod() which concisely solves the problem of delegating to the 
# method that would have been called if [.secret did not exist
`[.secret` <- function(x, i) {
   new_secret(NextMethod())
}
x[1]

s3_dispatch(x[1])

# the => indicates that [.secret is called, but that NextMethod() delegates the 
# work to the underlying internal [ method as shown by ->

# --- 13.6.2 allowing subclassing ---

# when you create a class you need to decide if you want to allow subclasses
# because it requires some changes to the constructor and careful thought in 
# your methods

# to allow subclasses, the parent constructor needs to have ... and class 
# arguments
new_secret <- function(x, ..., class = character()) {
   stopifnot(is.double(x))
   
   structure(
      x,
      ...,
      class = c(class, "secret")
   )
   
}

# then the subclass constructor can call to the parent constructor with additional
# arguments as needed, for example, imagine we want to create a supersecret 
# class which also hides the number of characters
new_supersecret <- function(x) {
   new_secret(x, class = "supersecret")
}

print.supersecret <- function(x, ...) {
   print(rep("xxxxx", length(x)))
   invisible(x)
}

x2 <- new_supersecret(c(15, 1, 456))
x2

# to allow inheritance you also need to think carefully about your methods as 
# you can no longer use the constructor
# if you do, the method will always return the same class regardless of the input
# this forces whoever makes a subclass to do a lot of extra work
# example, we need to revise the [.secret method, currently it always returns a
# secret(), even when given a supersecret:
`[.secret` <- function(x, ...) {
   new_secret(NextMethod())
}

x2[1:3]

# we want to make sure that [.secret returns the same class as x even if it's a 
# subclass
# this cannot be solved in base R, but can be solved with vctrs::vec_restore() generic
library(vctrs)

`[.secret` <- function(x, ...) {
   vctrs::vec_restore(NextMethod(), x)
}
x2[1:3]

# --- 13.7 dispatch details ----------------------------------------------------

# --- 13.7.1 S3 and base types ---

# what happens when you call an S3 generic with a base objects, ie an object 
# with no class
class(matrix(1:5))

# dispatch occurs on the implicit class which has three components
# 1, the string "array" or "matrix" is the object has dimensions
# 2, the result of typeof()
# 3, the string "numeric" if the object is "integer" or "double"

# there is no base function that will compute the implicit class, but you can
# use sloop:s3_class()
s3_class(matrix(1:5))

# this is used by s3_dispatch()
s3_dispatch(print(matrix(1:5)))

# this means that the class() of an object does not uniquely determine its 
# dispatch:
x1 <- 1:5
class(x1)

s3_dispatch(mean(x1))

# --- 13.7.2 Internal generics ---

# Some base functions like [, sum(), and cbind() are called internal generics
# because they do not call UseMethod() but instead call the C functions 
# DispatchGroup() or DispatchOrEval()

# s3_dispatch() shows internal generics by including the name of the generic
# followed by (internal)
s3_dispatch(Sys.time()[1])

# --- 13.7.3 Group generics ---

# Group generics are the most complicated part of S3 method dispatch because 
# they involve both NextMethod() and internal generics. Like internal generics,
# they only exist in base R, and you cannot define your own group generic

# There are four group generics:

# Math: abs(), sign(), sqrt(), floor(), cos(), sin(), log(), see ?Math for the
# complete list

# Ops: +, -, *, /, ^, %%, %/%, &, |, !, ==, !=, <, <=, >=, >

# Summary: all(), any(), sum(), prod(), min(), max(), range()

# Complex: Arg(), Cong(), Im(), Mod(), Re()

# Defining a single group generic for your class overrides the default behaviour
# for all of the members of the group. Methods for group generics are looked for 
# only if the methods for the specific generic do not exist:
s3_dispatch(sum(Sys.time()))

# --- 13.7.4 Double dispatch ---

# Generics in the Ops group, which includes two argument arithmetic and Boolean
# operators - and & implement a special type of method dispatch. They dispatch
# on both of the arguments which is called double dispatch. This is necessary
# to preserve the commutative property of many operators, i.e. a + b should be 
# equal to b + a. Take the following example:
date <- as.Date("2017-01-01")
integer <- 1L

date + integer
integer + date

# If + dispatched only on the first argument, it would return different values
# for the two cases. R does two dispatches in cases like this. There are three
# possible outcomes of this lookup:

# The methods are the same, so it does not matter which method is used.
# The methods are different, and R falls back to the internal method with a warning.
# One method is internal, in which case R calls the other method.

#--- S3 Problems and solutions -------------------------------------------------

# Q1
# What class of object does the following code return? What base type is it
# built on? What attributes does it use.
x <- ecdf(rpois(100, 10))
x
typeof(x)
attributes(x)

# It returns an object of class ecdf with superclasses stepfun and function. The
# ecdf object is built on the base type closure (a function). 

# Q2
# What class of object does the following code return? What base type is it 
# built on? What attributes does it use?
x <- table(rpois(100, 5))
x
typeof(x)
attributes(x)

# Returns a table object which is built on the integer type. The attribute
# dimnames is used to name elements of the integer vector.

# Q3
# Write a constructor for data.frame objects.

# Data frames are built on named lists of vectors which all have the same
# length. Besides the class and column names (names), the row.names are the 
# only attribute. This must be a character vector the same length as the other
# vectors. 
new_data.frame <- function(x, n, row.names = NULL) {
   # Check if the underlying object is a list
   stopifnot(is.list(x))
   
   # Check all inputs are the same length 
   stopifnot(all(lengths(x) == n))
   
   if (is.null(row.names)) {
      # Use special row names helper from base R
      row.names <- .set_row_names(n)
   } else {
      # Otherwise check that they're a character vector with the correct length
      stopifnot(is.character(row.names), length(row.names) == n)
   }
   
   structure(
      x,
      class = "data.frame",
      row.names = row.names
   )
}

# Q4
# What generics does the table class have methods for?
sloop::s3_methods_class("table")

#===============================================================================
# 14, R6
#===============================================================================

# This chapter describes the R6 OOP system, R6 has two special properties:

# It uses the encapsulated OOP paradigm which means that methods belong to 
# objects, not generics, and you call them like object$method()

# R6 objects are mutable, which means that they are modified in place and hence
# have reference semantics.

# Because R6 is not built into base R, you need to install and load the R6 package
# to use it:
library(R6)

# --- 14.2 Classes and methods -------------------------------------------------

# R6 only needs a single function call to create the class and its methods
# R6::R6Class(), this is the only function from the package that you will
# ever use. 

# The following example shows the two most important arguments to R6Class():

# The first argument is classname. It is not strictly needed, but it improves
# error messages and makes it possible to use R6 objects with S3 generics. By
# convention, R6 classes have UpperCamelCase names.

# The second argument, public, supplies a list of methods (functions) and fields
# (anything else) that make up the public interface of the object. By convention,
# methods use snake_case. Methods can access the methods and fields of the current
# object via self$. 

Accumulator <- R6Class("Accumulator", list(
   sum = 0,
   add = function(x = 1) {
      self$sum <- self$sum + x
      invisible(self)
   }
))

# You should always assign the result of R6Class() into a variable with the same
# name as the class because R6Class() returns an R6 object that defines the class
Accumulator

# You can construct a new object from the class by calling the new() method. In
# R6 methods belong to objects, so you use $ to access new():
x <- Accumulator$new()

# You can then call methods and access fields with $:
x$add(4)
x$sum

# In this class the fields and methods are public, which means that you can get 
# or set the value of any field. Later, we will see how to use private fields and
# methods to prevent casual access to the internals of your class.

# The Accumulate class has the field $sum and the method $add.

# --- 14.2.1 Method chaining ---

# $add() is called primarily for its side-effect of updating $sum
# Side-effect R6 methods should always return self invisibly. This returns the
# "current" object and makes it possible to chain together multiple method calls
x$add(10)$add(10)$sum

# For readability you might put one method call on each line:
x$
   add(10)$
   add(10)$
   sum

# This technique is called method chaining and is commonly used in languages 
# like Python and JavaScript.
# Method chaining is deeply related to the pipe.

# --- 14.2.2 Important methods ---

# There are two important methods that should be defined for most classes:
# $initialize() and $print(), they are not required but providing them will
# make your class easier to use.

# $initialize() overrides the default behaviour of $new(). For example, the 
# following code defines a Person class with fields $name and $age. To ensure
# that $name is always a single string and $age is always a single number, I
# placed checks in intitialize().

Person <- R6Class("Person", list(
   name = NULL,
   age = NA,
   initialize <- function(name, age = NA) {
      stopifnot(is.character(name), length(name) == 1)
      stopifnot(is.numeric(age), length(age) == 1)
      
      self$name <- name
      self$age <- age
   }
))

hadley <- person$new("hadley", age = 38)

# If you have more expensive validation requirements, implement them in a 
# seperate $validate() and only call when needed

# Defining $print allows you to override the default printing behaviour, as with
# any R6 method called for its side effects, $print() should return invisible(self)

Person <- R6Class("Person", list(
   name = NULL,
   age = NA,
   initialize <- function(name, age = NA) {
      self$name <- name
      self$age <- age
   },
   print = function(...) {
      cat("Person: \n", self$name, "\n", self$age)
      invisible(self)
   }
))

hadley <- person$new("hadley", age = 38)
hadley

# --- 14.2.3 Adding methods after creation ---

# Instead of continuously creating new classes, it is also possible to modify the
# fields and methods of an existing class. Add new elements to an existing class 
# with $set(), supplying the visibility, the name, and the component.

Accumulator <- R6Class("Accumulator")
Accumulator$set("public", "sum", 0)
Accumulator$set("public", "add", function(x = 1) {
   self$sum <- self$sum + x
   invisible(self)
})

# --- 14.2.4 Inheritance ---

# To inherit behaviour from an existing class, provide the class object to the
# inherit argument.

AccumulatorChatty <- R6Class("AccumulatorChatty",
  inherit = Accumulator,
  public = list(
     add = function(x = 1) {
        cat("Adding ", x, "\n", sep = "")
        super$add(x = x)
     }
  ))

x2 <- AccumulatorChatty$new()
x2$add(10)$add(1)$sum

# $add() overrides the superclass implementation, but we can still delegate
# to the superclass implementation by using super$ (This is analogous to 
# NextMethod() in S3). Any methods which are not overridden will use the 
# implementation in the parent class. 

# --- 14.2.5 Introspection ---

# Every R6 object has an S3 class that reflects its hierarchy of R6 classes.
# This means that the easiest way to determine the class (and all classes it
# inherits from) is to use class()
class(x2)

# The S3 hierarchy includes the base "R6" class. This provides common behaviour
# including a print.R6() method which calls $print() as described above.

# You can list all methods and fields with names()
names(x2)

# --- 14.3 Controlling access --------------------------------------------------

# R6Class() has two other arguments that work similarly to public:

# private allows you to create fields and methods that are only available from
# within the class, not outside of it

# active allows you to use accessor functions to define dynamic or active fields

# --- 14.3.1 Privacy ---

# With R6 you can define private fields and methods, elements that can only be
# accessed from within the class, not from the outside. There are two things 
# that you need to know to take advantage of private elements. 

# The private argument to R6Class works in the same way as the public argument:
# you give it a named list of methods (functions) and fields (everything else).

# Fields and methods defined in private are available within the methods using
# private$ instead of self$, you cannot access private fields or methods outside
# of the class.

# In the following example, the $age and $name fields can only be set during 
# object creation and their values cannot be accessed from outside of the class

Person <- R6Class("Person", 
   public = list(
      initialize = function(name, age = NA) {
         private$name <- name
         private$age <- age
      },
      print = function(...) {
         cat("Person: \n")
         cat("  Name: ", private$name, "\n", sep = "")
         cat("  Age:  ", private$age, "\n", sep = "")
      }
   ),
   private = list(
      age = NA,
      name = NULL
   )
)

hadley3 <- Person$new("Hadley", 30)
hadley3

# Notice how the name cannot be accessed now
hadley3$name
hadley3$name <- "Hadler"

# The distinction between public and private fields is important when you create
# complex networks of classes and you want to make it clear what it is ok for
# others to access. Private methods tend to be less important in R compared to 
# other programming langauges because the object hierarchies in R tend to be
# simpler.

# --- 14.3.2 Active fields ---

# Active fields allow you to define components that look like fields from the
# outside, but are defined with functions, like methods. Active fields are 
# implemented using active bindings. Each active binding is a function that takes
# a single argument: value. If the argument is missing(), the value is being
# retrieved, otherwise it is being modified. 

# For example, you could make a field random that returns a different value 
# every time you access it:

Rando <- R6::R6Class("Rando", active = list(
   random = function(value) {
      if (missing(value)) {
         runif(1)  
      } else {
         stop("Can't set `$random`", call. = FALSE)
      }
   }
))
x <- Rando$new()
x$random
x$random

# Active fields are useful in conjunction with private fields because they make
# it possible to implement components that look like fields from the outside but 
# provide additional checks. 

# For example, we can use them to make a read-only age field and to ensure that 
# name is a length 1 character vector.

Person <- R6Class("Person", 
   private = list(
      .age = NA,
      .name = NULL
   ),
   active = list(
      age = function(value) {
         if (missing(value)) {
            private$.age
         } else {
            stop("`$age` is read only", call. = FALSE)
         }
      },
      name = function(value) {
         if (missing(value)) {
            private$.name
         } else {
            stopifnot(is.character(value), length(value) == 1)
            private$.name <- value
            self
         }
      }
   ),
   public = list(
      initialize = function(name, age = NA) {
         private$.name <- name
         private$.age <- age
      }
   )
)

hadley4 <- Person$new("Hadley", age = 38)
hadley4
names(hadley4)
hadley4$age

# Notice how name can only be set to character
hadley4$name <- 10

# Notice how age is read only and cannot be changed
hadley4$age <- 20

# --- Bank account example ---

# *Note: items in lists must be assigned with "=", not "<-"

Account <- R6Class("Account",

   # private fields
   private = list(
      name = NULL,
      id = NULL,
      balance = 0
   ),
   
   # functions that activate (fill) private fields
   active = list(
      
      name_activate = function(value)
         if (missing(value)) {
            stopifnot(is.character(value), length(value) == 1)
            private$name 
         } else {
            stop("`$name` is immutable")
         },
      
      id_activate = function(value)
         if (missing(value)) {
            stopifnot(is.numeric(value), length(value) == 1)
            private$id 
         } else {
            stop("`$id` is immutable")
         },
      
      balance_activate = function(value)
         if (missing(value)) {
            stopifnot(is.numeric(value), length(value) == 1)
            private$balance 
         } else {
            stop("`$balance` is immutable")
         }
   ),
   
   # public functions to initialize class and modify balance
   public = list(
      
      initialize = function(name_activate, id_activate, balance_activate) {
         private$name = name_activate
         private$id = id_activate
         private$balance = balance_activate
      },
      
      deposit = function(amount) {
         private$balance = private$balance + amount
         cat("New balance is: ", private$balance)
      },
      
      withdraw = function(amount) {
         if (private$balance - amount < 0) 
            stop("Account overdrawn, transaction cancelled")
         private$balance = private$balance - amount
         cat("New balance is: ", private$balance)
      },
      
      print = function(...) {
         cat("Name: ", private$name, "\n ID: ", private$id, "\n Balance: ", 
             private$balance)
      }
      
   )
)

# Initialize new object
account <- Account$new(name = "Dave", id = "8847", balance = 100)

# Notice the effect of overdrawing
account$withdraw(50)
account$withdraw(200)

# Notice the print function as work
account

# Notice how balance is inaccessable because it is private and we can't reset
# balance_activate because of our active field function
account$balance
account$balance_activate <- 10

# --- Username and password example ---

Login <- R6Class(classname = "Login",
                 
   private = list(
      .username = NULL,
      .password = NULL
   ),
   
   active = list(
      
      username = function(value) {
         if (missing(value)) {
            stopifnot(is.character(value), length(value) == 1)
            private$.username
         } else {
            stop("username is immutable")
         }
      },
      
      password = function(value) {
         if (missing(value)) {
            stopifnot(is.character(value), length(value) == 1)
            private$.password
         } else {
            stop("password is immutable")
         }
      }
   ),
   
   public = list(
      
      initialize = function(username, password) {
         private$.username = username
         private$.password = password
      },
      
      login = function(username, password) {
         if (username == private$.username & password == private$.password) {
            cat("Login Successful")
         } else {
            cat("Login Failed")
         }
      },
      
      print = function(...) {
         cat("Password is hidden")
      }
   )
)

account <- Login$new(username = "dave", password = "asdf1234")

# Notice that the password is hidden
account

# Successful and failed login examples
account$login(username = "dave", password = "asdf1234")
account$login(username = "dave", password = "asdf124")

# --- 14.4 Reference semantics -------------------------------------------------

# One of the big differences between R6 and most other objects is that they 
# have reference semantics. The primary consequence of reference semantics is
# that objects are not copied when modified:

y1 <- Accumulator$new()
y2 <- y1

# Notice that the output stays the same even though only y1 is modified
y1$add(10)
y1$sum
y2$sum

# If you want a copy, you will need to explicity $clone() the object
y1 <- Accumulator$new()
y2 <- y1$clone()

y1$add(10)
y1$sum
y2$sum

# There are three other less obvious consequences of reference semantics:

# It is harder to reason about code that uses R6 objects because you need to 
# understand more context.

# It makes sense to think about when an R6 object is deleted, and you can write
# a $finalize() to complement the $initialize().

# If one of the fields is an R6 object, you must create it inside $initialize(),
# not R6Class()

# --- 14.4.1 Reasoning ---

# R6 makes reasoning difficult, take the following example
x <- list(a = 1)
y <- list(b = 2)

z <- f(x, y)

# For the majority of functions you know that the final line modifies z

# Take a similar example using the imaginary List reference class
x <- List$new(a = 1)
y <- List$new(b = 2)

z <- f(x, y)

# If f() calls methods of x or y, it might modify them as well as z. This is the
# biggest potential downside of R6 and it should be avoided by writing functions
# that either return a value, modify their R6 inputs, or both.

# --- 14.4.2 Finalizer ---

# One useful property of reference semantics is that it makes sense to think
# about when an R6 object is finalized (i.e. when it is deleted). This does 
# not make sense for most objects because copy-on-modify semantics mean that there 
# may be many transient versions of an object. For example, the following creates 
# two factor objects: the second is created when the levels are modified, leaving
# the first to be destroyed by the garbage collector.

x <- factor(c("a", "b", "c"))
levels(x) <- c("c", "b", "a")

# Since R6 objects are not copied-on-modify they are only deleted once, and it
# makes sense to think of $finalize() as a complement to $initialize(). 
# Finalizers play a similar role to on.exit(), cleaning up any resources created
# by the initializer. For example, the following class wraps up a temporary
# file, automatically deleting it when the class is finalized.

TemporaryFile <- R6Class("TemporaryFile", list(
   path = NULL,
   initialize = function() {
      self$path <- tempfile()
   },
   finalize = function() {
      message("Cleaning up ", self$path)
      unlink(self$path)
   }
))

# The finalize method will be run when the object is deleted (or more precisely,
# by the first garbage collection after the object has been unbound from all names)
# or when R exits. 
tf <- TemporaryFile$new()
rm(tf)
#> Cleaning up /tmp/Rtmpk73JdI/file155f31d8424bd

# --- 14.5 Why R6? -------------------------------------------------------------

# R6 is very similar to a built in OO system called reference classes (RC).
# R6 is much simpler. Both R6 and RC are built on top of environments, but while
# R6 uses S3, RC uses S4. This means to fully understand RC, you need to 
# understand how the more complicated S4 works.
# R6 is much faster than RC. 

# --- R6 Example problems ------------------------------------------------------

# Q1
# Create an R6 class that represents a shuffled deck of cards. You should be able
# to draw cards from the deck with $draw(n), and return cards to the deck and
# reshuffle with $reshuffle().
suit <- c("SPADE", "HEARTS", "DIAMOND", "CLUB")
value <- c("A", 2:10, "J", "Q", "K")
cards <- paste(rep(value, 4), suit)

ShuffledDeck <- R6Class(
   classname = "ShuffledDeck",
   public = list(
      deck = NULL,
      initialize = function(deck = cards) {
         self$deck <- sample(deck)
      },
      reshuffle = function() {
         self$deck <- sample(cards)
         invisible(self)
      },
      n = function() {
         length(self$deck)
      },
      draw = function(n = 1) {
         if (n > self$n()) {
            stop("Only ", self$n(), " cards remaining.", .call = FALSE)
         }
         
         output <- self$deck[seq_len(n)]
         self$deck <- self$deck[-seq_len(n)]
         output
      }
   )
)

my_deck <- ShuffledDeck$new()
my_deck$draw(10)
my_deck$reshuffle()$draw(10)

# Q2
# Why can't you model a bank account or a deck of cards with an S3 class?
# Because S3 classes obey R's usual semantics of copy-on-modify, every time you
# deposit money into your bank account or draw a card from the deck, you would 
# get a new copy of the object.

# It is possible to combine S3 classes with an environment (which is how R6 works),
# but it is ill-advised to create an objects that looks like a regular R object 
# but has reference semantics.

#===============================================================================
# 15, S4
#===============================================================================

# S4 provides a formal approach to functional OOP. The underlying ideas are 
# similar to S3, but implementation is much stricter and makes use of 
# specialised functions for creating classes (setClass()), generics (setGeneric()),
# and methods (setMethod()). Additionally, S4 provides multiple inheritance
# (a class can have multiple parents) and multiple dispatch (method dispatch can
# use the class of multiple arguments). 

# An important concept of S4 is the slot, a named component of the object that
# is accessed using the subsetting operator @. The set of slots and their classes
# forms an important part of the definition of an S4 class. 

# Prerequisites
# All functions related to S4 live in the methods package. This package is always
# available in R, but may not be available when running R in batch mode, ie from
# Rscript. 
library(methods)

# --- 15.2 Basics --------------------------------------------------------------

# You define an S4 class by calling setClass() with the class name and a defenition
# of its slots, and the names, and the classes of the class data:
setClass("Person",
  slots = c(
     name = "character",
     age = "numeric"
  )
)

# Once the class is defined, you can construct new objects from it by calling
# new() with the name of the class and a value for each slot.
john <- new("Person", name = "John Smith", age = 12)

# Given an S4 object you can see its class with is() and access slots with @
# and slot()
is(john)
john@name
slot(john, "age")

# Generally you should only use @ in your methods. If you're working with someone
# else's class, look for accessor functions that allow you to safely set and 
# get slot values. As the developer of a class, you should provide you own
# accessor functions. Acessors are typically S4 generics allowing multiple 
# classes to share the same external interface.

# Here we create a setter and getter for the age slot by creating generics with
# setGeneric()
setGeneric("age", function(x) standardGeneric("age"))
setGeneric("age<-", function(x, value) standardGeneric("age<-"))

# And then defining methods with setMethod()
setMethod("age", "Person", function(x) x@age)
setMethod("age<-", "Person", function(x, value) {
   x@age <- value
   x
})

age(john) <- 50
age(john)

# If you're using an S4 class defined in a package, you can get help on it with
# class?Person. To get help for a method, put ? in front of a call (ie ?age(john))
# and ? will use the class of the arguments to figure out which help file you 
# need.

# Finally, you can use sloop functions to identify S4 objects and generics
# found in the wild.
library(sloop)

sloop::otype(john)
sloop::ftype(age)

# --- 15.3 Classes -------------------------------------------------------------

# To define an S4 class, call setClass() with three arguments:

# The class name. By convention S4 class names use UpperCamelCase.

# A named character vector that describes the names and classes of the slots
# (fields). For example, a person might be represented by a character name
# and a numeric age: c(name = "character", age = "numeric"). The pseudo
# class ANY allows a slot to accept objects of any type.

# A prototype, a list of default values for each slot. Technically the prototype
# is optional, but you should always provide it.

# The code below illustrates the three arguments by creating a Person class:
setClass("Person",
  slots = c(
     name = "character",
     age = "numeric"
  ),
  prototype = list(
     name = NA_character_,
     age = NA_real_
  )
)

me <- new("Person", name = "Hadley", age = 25)
str(me)

# --- 15.3.1 Inheritance ---

# There is one other important argument to setClass(): contains. This specifies
# a class (or classes) to inherit slots and behaviour from. For example, we can
# create an Employee class that inherits from the Person class, adding an extra
# slot that describes their boss.
setClass("Employee",
  contains = "Person",
  slots = c(
     boss = "Person"
  ),
  prototype = list(
     boss = new("Person")
  )
)

str(new("Employee"))

# --- 15.3.2 Introspection ---

# To determine what classes an object inherits from, us is().
is(new("Person"))
is(new("Employee"))
is(me, "Person")

# --- 15.3.3 Redefinition ---

# In most programming languages, class defenition occurs at compile-time and 
# object construction occurs later at run time. In R, both defenition and 
# construction occur at run time. When you call setClass() you are registering
# a class defenition in a (hidden) global variable.

# --- 15.3.4 Helper ---

# new() is a low level constructor suitable for use by the developer. 
# User-friendly classes should always be paired with a user-friendly helper. A
# helper should always:

# Have the same name as the class, ie myclass()

# Have a thoughtfully crafted user interface with carefully chosen default values
# and useful conversions.

# Create carefully crafted error messages tailored towards an end-user.

# Finish by calling methods::new()

Person <- function(name, age = NA) {
   age <- as.double(age)
    
   new("Person", name = name, age = age)
}

Person("Hadley")

# --- 15.3.5 Validator ---

# You will need to implement more complicated class validation checks yourself.
# For instance, @name and @age can currently be different lengths:
Person("Hadley", age = c(30, 38))

# To enforce additional constraints, a validator can be written with setValidity().
# It takes a class and a function and returns TRUE if the input is valid, 
# otherwise it returns a character vector describing the problem(s):
setValidity("Person", function(object) {
   if (length(object@name) != length(object@age)) {
      "@name and @age must be the same length"
   } else {
      TRUE
   }
})

Person("Hadley", age = c(30, 38))

# Note that the validity method is only called automatically by new(), so you
# can still create an invalid object by modifying it.
alex <- Person("Alex", age = 30)
alex@age <- 1:10

# You can explicitly check validity yourself by calling validObject():
validObject(alex)

# validObject() can be used to create accessors that can not create invalid 
# objects.

# --- 15.4 Generics and methods ------------------------------------------------

# The job of a generic is to perform method dispatch, ie find the specific
# implementation for the combination of classes passed to the generic.

# To create a new S4 generic call setGeneric() with a function that calls
# standardGeneric():
setGeneric("myGeneric", function(x) standardGeneric("myGeneric"))

# By convention new S4 generics should use lowerCamelCase.

# It is bad practice to use {} in the generic as it triggers a special case that
# is more expensive and best avoided.

# Don't do this!
setGeneric("myGeneric", function(x) {
   standardGeneric("myGeneric")
})

# --- 15.4.1 Signature ---

# Like setClass(), setGeneric() has many other arguments, but the only one you 
# need to know is signature. This allows you to control the arguments that are
# used for method dispatch. If signature is not supplied, all arguments (apart
# from ...) are used. It is occasionally useful to remove arguments from dispatch.
# This allows you to require that methods provide argument like verbose = TRUE
# or quiet = FALSE, but they do not take part in the dispatch.

setGeneric("myGeneric",
   function(x, ..., verbose = TRUE) standardGeneric("myGeneric"),
   signature = "x"
)

# --- 15.4.2 Methods ---

# A generic isn't useful without some methods, and in S4 you define methods with
# setMethod(). There are three important arguments: the name of the generic, 
# the name of the class, and the method itself.
setMethod("myGeneric", "Person", function(x) {
   # method implementation
})

# To list all the methods that belong to a generic, or that are associated with
# a class, use methods("generic") or methods(class = "class"). To find the 
# implementation of a specific method, use selectMethod("generic", "class").

# --- 15.4.3 Show method ---

# The most commonly defined S4 method that controls printing is show() which 
# controls how the object appears when it is printed. To define a method for 
# an existing generic, you must first determine the arguments. You can get 
# those from the documentation of by looking at the args() of the generic:
args(getGeneric("show"))

# Our show method needs to a have a single argument object:
setMethod("show", "Person", function(object) {
   cat(
      "Name: ", object@name, "\n",
      "Age: ", object@age, "\n"
   )
})

# --- 15.4.4 Accessors ---

# Slots should be considered an internal implementation detail: they can change
# without warning and user code should avoid accessing them directly. Instead,
# all user accessible slots should be accompanied by a pair of accessors. If 
# the slot is unique to the class, this can just be a function.
person_name <- function(x) x@name
person_name(alex)

# Typically, you will define a generic so that multiple classes can use the same
# interface.
setGeneric("name", function(x) standardGeneric("name"))
setMethod("name", "Person", function(x) x@name)

name(alex)

# If the slot is also writeable, you should provide a setter function. You 
# should always include validObject() in the setter to prevent the user from
# creating invalid objects.

# Note*: the second argument must be named value.
# Note*: you must return the object as the last line in the setMethod function.

setGeneric("name<-", function(x, value) standardGeneric("name<-"))
setMethod("name<-", "Person", function(x, value) {
   x@name <- value
   validObject(x)
   x
})

name(alex) <- "Jon Smythe"
name(alex)
name(alex) <- letters

#===============================================================================
# 16 OOP Trade-offs
#===============================================================================

# Default to S3, S3 is simple and widely used throughout base R and CRAN.

# --- 16.2 S4 versus S3 --------------------------------------------------------

# The underlying ideas in S3 and S4 are the same, S4 is just more formal, 
# more strict, and more verbose. The strictness and formality of S4 make it
# suited or large teams. S4 requires more upfront design than S3 and this 
# investment pays off on larger projects where greater resources are available.

# S4 is also a good fit for complex systems of interrelated objects, and it is 
# possible to minimise code duplication through careful implementation of methods.
# The best example of such a system is the Matrix package which efficiently
# stores and computes with different types of sparse and dense matrices. As of 
# version 1.3.2 it defines 102 classes, 21 generic functions, and 2005 methods.

# --- 16.3 R6 versus S3 --------------------------------------------------------

# R6 is profoundly different from S3 and S4 because it is built on encapsulated
# objects rather than generic functions. Additionally R6 objects have reference
# semantics which means that they can be modified in place. These big differences
# have a number of consequences.

# A generic function is a regular function so it lives in the global namespace. 
# An R6 method belongs to an object so it lives in a local namespace. This 
# influences how we think about naming.

# You invoke an R6 method using $, which is an infix operator. If you set up
# your methods correctly you can use chains of method calls as an alternative
# to the pipe.

# These are general trade-offs between functional and encapsulated OOP, so they
# serve as a discussion of system design in R versus Python.

# Creating an R6 method is very cheap. Most encapsulated OO languages encourage
# you to create many small methods, each doing one thing well with an evocative
# name. Creating a new S3 method is more expensive because you may also have to
# create a generic and think about naming issues.

# --- S4 Practice problems -----------------------------------------------------

# Q1
# lubridate::period() returns an S4 class. What slots does it have? What class
# is each slot?

# As a short example we create a period of 1 second, 2 minutes, 3 hours, 4 days,
# and 5 weeks.
example <- lubridate::period(
   c(1, 2, 3, 4, 5),
   c("second", "minute", "hour", "day", "week")
)

# This should add upt to 39 days, 3 hours, 2 minutes, 1 second
example
str(example)

# Q3
# create an advanced Person class
setClass("Person",
   slots = c(
      age = "numeric",
      given = "character",
      family = "character",
      role = "character",
      email = "character",
      comment = "character"
   ),
   prototype = list(
      age = NA_real_,
      given = NA_character_,
      family = NA_character_,
      role = NA_character_,
      email = NA_character_,
      comment = NA_character_
   )
)

# Helper to create instances of the Person class
Person <- function(given, family,
                   age = NA_real_,
                   role = NA_character_,
                   email = NA_character_,
                   comments = NA_character_) {
   age = as.double(age)
 
   new("Person",
       age = age,
       given = given, 
       family = family, 
       role = role, 
       email = email,
       comment = comment
    )
}

# Validator to ensure that each slot is of length one
setValidity("Person", function(object) {
   invalids <- c()
   if (length(object@age)     != 1 ||
       length(object@given)   != 1 ||
       length(object@family)  != 1 ||
       length(object@email)   != 1 ||
       length(object@comment) != 1) {
      invalids <- paste0("@name, @age, @given, @ family, @email,
                          @comment must be of length ")
   }
       
   known_roles <- c(
      NA_character_, "aut", "com", "cph", "cre", "ctb", "ctr", "dtc", "fnd"
   )

   if (!all(object@role %in% known_roles)) {
      paste(
         "@role(s) must be one of",
         paste(known_roles, collapse = ", ")
      )
   }
   
   if (length(invalids)) return(invalids)
   TRUE
})

# Q4
# Add age() accessors for the Person class. 

# To print the age slot.
setGeneric("age", function(x) standardGeneric("age"))
setMethod("age", "Person", function(x) x@age)

# To assign to the age slot.
setGeneric("age<-", function(x, value) standardGeneric("age<-"))
setMethod("age<-", "Person", function(x, value) {
   x@age <- value
   validObject(x)
   x
})

# In the defenition of the generic why is it necessary to repeat the name of the
# generic twice? Within setGeneric() the name (1st argument) is needed as the 
# name of the generic, then the name also explicitly incorporates method dispatch
# via standardGeneric() within the generic's body. This behaviour is similar to
# UseMethod() in S3. 

#===============================================================================
# Metaprogramming
#===============================================================================

# Introduction: metaprogramming is the idea that code is data that can be
# inspected and modified programmatically. At the most basic level it allows
# you to do things like library(purrr) instead of library("purrr") and enable
# plot(x, sin(x)) to automatically label the axes with x and sin x. At a deeper
# level it allows you to do things like y ~ x1 + x2 and translate 
# subset(df, x == y) into df[df$x == df$y, , drop = FALSE].

#-------------------------------------------------------------------------------
# 17 Big Picture
#-------------------------------------------------------------------------------

# Few modern programming languages have the level of metaprogramming that R 
# provides. 

# This chapter uses rlang for most big ideas and lobstr to explore the tree
# structure of code.
library(rlang)
library(lobstr)

# --- 17.2 Code is data --------------------------------------------------------

# Code is data, you can capture code and compute on it as you can with other
# types of data. The first way you can capture code is with rlang::expr(), you
# can think of expr() as returning exactly what you pass in:
expr(mean(x, na.rm = TRUE))
expr(10 + 100 + 1000)

# expr() lets you capture code that you have typed, to capture code passed to 
# a function you need a different tool, enexpr() (enriched expression)
capture <- function(x) {
   enexpr(x)
}
capture(a + b + c)

# Once you have captured an expression, you can inspect and modify it. Expressions
# behave much like lists, so you can modify with [[ and $.
f <- expr(f(x = 1, y = 2))

# Add a new argument.
f$z <- 3
f

# Or remove an argument.
f[[2]] <- NULL
f

# The first element of the call is the function to be called, which means the 
# first argument is in the second position. 

# --- 17.3 Code is a tree ------------------------------------------------------

# Behind the scenes almost every programming language represents code as a tree,
# often called an abstract syntax tree or AST for short. R is unusual in that you
# can actually inspect and manipulate this tree. 

# A conventient tool for understanding the tree structure is lobstr::ast(). Given
# some code this functios displays the underlying tree structure. Function calls
# form the branches of the tree and are shown by rectangles. The leaves of the
# tree are symbols like a and constants like "b".
lobstr::ast(f(a, "b"))

# Nested function calls create more deeply branching trees.
ast(f1(f2(a, b), f3(1, f4(2))))

f2 <- function(b = 2) {
   print(b)
}

ast(f1 <- function(a, ...) {
   print(a)
   f2(...)
})

# --- 17.4 Code can generate code ----------------------------------------------

# You can use code to generate new trees. There are two main tools: call2() and
# unquoting. 

# rlang::call2() constructs a function call from its components: the function to
# call and the arguments to call it with.
call2("f", 1, 2, 3)
call2("+", 1, call2("*", 2, 3))

# call2 is convenient for programming, but clunky for interactive use. An 
# alternative technique is to build complex code trees by combining simpler code
# trees via expr(), enexpr(), and the bang bang !! unquoting operator. 

# !!x inserts the code tree stored in x into the expression. 
xx <- expr(x + x)
yy <- expr(y + y)

expr(!!xx / !!yy)

# Unquoting becomes even more useful when you wrap it in a function, first using
# enexpr() to capture the user's expression, and then expr() and !! to create 
# a new expression using a template. 

# The example below computes the coefficient of variation. 
cv <- function(var) {
   var <- enexpr(var)
   expr(sd(!!var) / mean(!!var))
}

cv(x)
cv(x + y)

# --- 17.5 Evaluation runs code ------------------------------------------------

# You get a powerful set of tools when you evaluate (run) an expression. Evaluating
# an expression requires an environment, which tells R what the symbols in the 
# expression mean. 

# The primary tool for evaluating expression is base::eval() which takes an 
# expression and an environment. 
eval(expr(x + y), env(x = 1, y = 10))
eval(expr(x + y), env(x = 2, y = 100))

# If you omit the environment, eval uses the current environment.
x <- 10
y <- 100
eval(expr(x + y))

# One of the big advantages of evaluating code manually is that you can tweak
# the environment. The two main reasons to do this are:
# To temporarily override functions to implement a domain specific language.
# To add a data mask so you can refer to variables in a data frame as if they
# are variable in an environment. 

# The example below shows code evaluated in an environment where * and + have 
# been modified to work with strings instead of numbers. 
string_math <- function(x) {
   e <- env(
      caller_env(),
      `+` = function(x, y) paste0(x, y),
      `*` = function(x, y) strrep(x, y)
   )
   
   eval(enexpr(x), e)
}

name <- "Hadley"
string_math("Hello " + name)
string_math(("x" * 2 + "-y") * 3)

# dplyr takes this idea to the extreme, running code in an environment that 
# generates SQL code for execution in a remote database. 
library(dplyr)
con <- DBI::dbConnect(RSQLite::SQLite(), filename = ":memory:")
mtcars_db <- copy_to(con, mtcars)

mtcars_db %>%
   filter(cyl > 2) %>%
   select(mpg:hp) %>%
   head(10) %>%
   show_query()
#> <SQL>
#> SELECT `mpg`, `cyl`, `disp`, `hp`
#> FROM `mtcars`
#> WHERE (`cyl` > 2.0)
#> LIMIT 10

DBI::dbDisconnect(con)

# --- 17.7 Customising evaluation with data ------------------------------------

# Rebinding functions is very powerful, but it tends to require a lot of 
# investment. A more practical application is modifying evaluation to look for
# variables in a data frame instead of an environment. This idea powers the base
# subset() and transform() functions, as well as many tidyverse functions such
# as dplyr::mutate().

# rlang::eval_tidy() takes a data mask (typically a data frame) as well as an 
# expression and an environment. 
df <- data.frame(x = 1:5, y = sample(5))
eval_tidy(expr(x + y), df)

#-------------------------------------------------------------------------------
# 18 Expressions
#-------------------------------------------------------------------------------

# It is important to distinguish between an operation and its result. Take the 
# following code which does not run because the variable x is not defined.
y <- x * 10

# It would be nice to capture the intent of the code without executing it. How
# can we seperate our description of the action from the action itself. 

# One way is to use rlang::expr() and base::eval()
z <- rlang::expr(y <- x * 10)

x <- 4
eval(z)

# --- 18.2 Abstract syntax trees -----------------------------------------------

# Expressions are also called abstract syntax trees (AST).

# --- 18.2.1 Drawing ---

lobstr::ast(f(x, "y", 1))

# The branches of the tree are call objects, which represent function calls are
# orange rectangles. Symbols (such as the variable x) are drawn in purple and 
# constants are in black.

lobstr::ast(f(g(1, 2), h(3, 4, i())))

# --- 18.2.3 Infix calls ---

# Every call in R can be written in AST because even infix operators are truly
# functions under the hood. 

# The following functions are equivalent.
y <- x * 10
`<-`(y, `*`(x, 10))

lobstr::ast(y <- x * 10)

# --- 18.3 Expressions ---------------------------------------------------------

# The data structures present in AST are called expressions. An expression is 
# any member of the set of base types created by parsing code: constants, 
# scalars, symbols, call objects, and pairlists.

# --- 18.3.1 Constants ---

# Scalar constants are the simplest component of AST, more precisely a constant
# is either NULL or a length 1 atomic vector like TRUE, 1L, 2.5, "x".

# Constants are self-quoting in the sense that the expression used to represent
# a constant is the same constant.
identical(expr(2L), 2L)

# --- 18.3.2 Symbols ---

# A symbol represents the name of an object like x, mtcars, or mean. You can 
# create a symbol in two ways: by capturing code that references an object
# with expr() or turning a symbol into a string with rlang::sym().
expr(x)
sym("x")

# You can turn a symbol back into a string with as.character() or 
# rlang::as_string()
as_string(expr(x))

# --- 18.3.3 Calls ---

# A call object represents a captured function call. Call objects are a special
# type of list where the first component specifies the function to call (usually
# a symbol) and the remaining elements are arguments of that call. 
lobstr::ast(read.table("important.csv", row.names = FALSE))
x <- expr(read.table("important.csv", row.names = FALSE))
typeof(x)
is.call(x)

# --- 18.3.3.1 Subsetting ---

# Calls generally act like lists, so you can subset.
x[[1]]
x$row.names

# Extracting specific arguments from calls is challenging because of R's 
# flexible rules for argument matching. To work around this problem you can 
# use rlang::call_standardise()
rlang::call_standardise(x)

# Calls can be modified in the same way as lists.
x$header <- TRUE
x

# --- 18.3.3.3 Constructing ---

# You can construct call objects with rlang::call2.
call2("mean", x = expr(x), na.rm = TRUE)

# Infix calls can also be created this way.
call2("<-", expr(x), 10)

# --- 18.4 Parsing and grammar -------------------------------------------------

# The process by which a computer language takes a string and constructs an
# expression is called parsing, and is governed by a set of rules known as 
# grammar. 

# --- 18.4.1 Operator precedence ---

# One source of ambiguity from infix operators is what does 1 + 2 * 3 yield? 
# Which of two possible parse trees does R use?
lobstr::ast(1 + 2 * 3)

# Here we see that PEMDAS prevails! The function 2 * 3 gets calculated in its
# own function environment. Precendence for arithmetic operations is simple, but
# predicting the precedence of other operators is difficult. 

# There is one particularly surprising case in R: ! has much lower precedence 
# than one may expect.
lobstr::ast(!x %in% y)

# R has over 30 infix operators divided into 18 precedence groups.

# Another question is how 1 + 2 + 3 gets evaluated. Is 1 + 2 evaluated first or
# is 2 + 3 evaluated first?
lobstr::ast(1 + 2 + 3)

# In R most operators are left-associative, the operators on teh left are 
# evaluated first. 

# --- 18.4.3 Parsing and deparsing ---

# Most of the time you type code into the console and R takes care of turning
# the characters you have types into AST. However, occasionally you have code 
# stored in a string and you want to parse it yourself. You can do so with 
# rlang::parse_expr()
x1 <- "y <- x + 10"
rlang::parse_expr(x1)

# If you have multiple expressions seperated by ; or \n, you will need to use
# rlang::parse_exprs()
x3 <- "a <- 1; a + 1"
rlang::parse_exprs(x3)

# The base equivalent to parse_exprs() is parse(), it is a little harder to use
# because it is designed for parsing R code stored in files. 
as.list(parse(text = x1))

# The inverse of parsing is deparsing: given an expression you want the string
# that would generate it. This happens automatically when you print an expression,
# and you can get the string with rlang::expr_text()
z <- expr(y <- x + 10)
expr_text(z)

# --- 18.5 Walking AST with recursive functions --------------------------------

# We are now going to use everything that we have learned about ASTs to solve
# more complicated problems. The inspiration comes from the codetools package
# which provides two interesting functions:

# findGlobals() locates all global variables used by a function, useful to check
# if your function relies on variables defined in the parent environment.

# checkUsage() checks for a range of common problems including unused local
# variables, unused parameters, and partial argument matching.

# The underlying idea of these functions is recursion on the AST tree. 

# We first define two helper functions. The first, expr_type() returns "consant"
# for constants, "symbol" for symbols, "call" for calls, etc.
expr_type <- function(x) {
   if (rlang::is_syntactic_literal(x)) {
      "constant"
   } else if (is.symbol(x)) {
      "symbol"
   } else if (is.call(x)) {
      "call"
   } else if (is.pairlist(x)) {
      "pairlist"
   } else {
      typeof(x)
   }
}

expr_type(expr("a"))
expr_type(expr(x))
expr_type(expr(f(1, 2)))

# This is coupled with a wrapper around the switch function:
switch_expr <- function(x, ...) {
   switch(expr_type(x),
          ...,
          stop("Don't know how to handle type ", typeof(x), call. = FALSE)
   )
}

# With these two functions we can write a basic template for any function that 
# walks the AST using switch().
recurse_call <- function(x) {
   switch_expr(x,
               # Base cases
               symbol = ,
               constant = ,
               
               # Recursive cases
               call = ,
               pairlist =
   )
}

# --- 18.5.1 Finding T and F ---

# We start with a function to find whether the logical abbreviations T and F 
# are being used as this is considered poor coding. 
logical_abbr_rec <- function(x) {
   switch_expr(x,
               constant = FALSE,
               symbol = as_string(x) %in% c("F", "T")
   )
}

logical_abbr_rec(expr(TRUE))
logical_abbr_rec(expr(T))

#-------------------------------------------------------------------------------
# 19 Quasiquotation
#-------------------------------------------------------------------------------

# It is time to return to the fundamental ideas that make expr() and ast() work:
# quotation. In tidy evaluation, all quoting functions are quasiquoting fucntions
# because they also support unquoting. Where quotation is the act of capturing
# an unevaluated expression, unquotation is the ability to selectively evaluate
# parts of an otherwise quoted expression. Together this is called quasiquotation.

# Quasiquotation makes it easy to create functions that combine code written
# by the function's author with code written by the function's user.

library(rlang)
library(purrr)

# Quoting functions have deep connections to Lisp macros, but macros are usually
# at compile-time which does not exist in R, and they always input and output
# ASTs.

# --- 19.2 Motivation ----------------------------------------------------------

# Imagine creating a lot of strings by joining together words, instead of writing
# all of these quotes you want to use bare words.
paste("Good", "morning", "Hadley")

cement <- function(...) {
   args <- ensyms(...)
   paste(purrr::map(args, as_string), collapse = " ")
}

cement(Good, morning, Hadley)

# Formally this function quotes all of its inputs. This function is nice because 
# we no longer need to type quotation marks, the problem comes when we want to
# use variables.

# We need a way to explicitly unquote the input to tell cement() to remove the
# automatic quote marks. Quasiquotation gives us a standard tool to do so: !!,
# the bang bang operator. 
name <- "Hadley"
time <- "morning"
cement(Good, !!time, !!name)

# It is useful to compare cement() and paste(). paste() evaluates its arguments
# so we must quote where needed. cement() quotes its arguments, so we must 
# unquote where needed.

# --- 19.3 Quoting -------------------------------------------------------------

# The first part of quasiquotation is quotation: capturing an expression without
# evaluating it. 

# --- 19.3.1 Capturing expressions ---

# The most important function is rlang::expr()
expr(x + y)

# expr() is not useful inside of a function because it captures exactly the 
# arguments in expr(), this can be solved with enexpr() which captures what the
# caller supplied by looking at the internal promise object that powers lazy
# evaluation.
f2 <- function(x) enexpr(x)
f2(a + b + c)

# To capture all arguments in ..., use enexprs()
f <- function(...) enexprs(...)
f(x = 1, y = 10 * z)

# exprs() is used to make a list of expressions
exprs(x = x ^ 2, y = y ^ 3, z = z ^ 4)
# shorthand for
# list(x = expr(x ^ 2), y = expr(y ^ 3), z = expr(z ^ 4))

# --- 19.3.2 Capturing symbols ---

# Sometimes you only want the user to specify a variable name, not an arbitrary
# expression. In this case you can use rlang::ensym() or ensyms(). These are
# variants of enexpr() and enexprs() that check if the captured expression is 
# either a symbol or a string (strings are converted to symbols).
f <- function(...) ensyms(...)
f(x)
f("x")

# --- 19.3.3 With base R ---

# Each rlang function has an equivalent in base R, the primary difference is that
# the base equivalents do not support unquoting. This makes them quoting function,
# rather than quasiquoting functions.

# The base equivalent of expr() is quote()
quote(x + y)

# The base equivalent of enexpr() is substitute()
f3 <- function(x) substitute(x)
f3(x + y)

# The base equivalent of exprs() is alist()
alist(x = 1, y = x + 2)

# The equivalent of enexprs() is an undocumented feature of substitute()
f <- function(...) as.list(substitute(...()))
f(x = 1, y = 10 * z)

# --- 19.3.4 Substitution ---

# You will most often see substitute() used to capture unevaluated arguments. 
# However, as well as quoting, substitute() also does substitution. If you give
# it an expression rather than a symbol, it will substitute in the values of
# symbols defined in the current environment.
f4 <- function(x) substitute(x * 2)
f4(a + b + c)

# --- 19.3.5 Summary ---

# When quoting there are two important distinctions. Is is supplied by the 
# developer of the code or the user? Do you want to capture single or multiple
# expressions?

# This leads to a 2x2 table of functions depending on the use case.

# The rlang options are as follows:

# One expression, developer: expr()
# One expression, user: enexpr()
# Many expressions, developer: exprs()
# Many expressions, user: enexprs()

# The base options are as follows:

# One expression, developer: quote()
# One expression, user: substitute()
# Many expressions, developer: alist() 
# Many expressions, user: as.list(substitute(...()))

# --- 19.4 Unquoting -----------------------------------------------------------

# So far we have only seen small advantages of rlang quoting over base R quoting.
# The big difference is that rlang functions are quasiquotation because they
# can also unquote. 

# Unquoting allows you to selectively evaluate code inside expr() so that 
# expr(!!x) is equivalent to x. 
x <- expr(-1)
expr(f(!!x, y))

# !! also works with symbols and constants.
a <- sym("y")
b <- 1
expr(f(!!a, !!b))

# An example implementation of !!.
mean_rm <- function(var) {
   var <- ensym(var)
   expr(mean(!!var, na.rm = TRUE))
}
expr(!!mean_rm(x) + !!mean_rm(y))

# --- 19.4.5 Unquoting many arguments ---

# The !!! operators takes a list of expressiona and unquotes them all. 
xs <- exprs(1, a, -b)
expr(f(!!!xs, y))

# Or with names.
ys <- set_names(xs, c("a", "b", "c"))
expr(f(!!!ys, d = 4))

# Quoting functions in base R include ls(), rm(), library(), and base plotting
# functions. This allows both of the following to work.
library(rlang)
library("rlang")

# --- 19.6 dot dot dot ---------------------------------------------------------

# For example, what do you do if the elements you want to put in ... are already 
# stored in a list. Image you have a list of data frames that you want to
# rbind() together.
dfs <- list(
   a = data.frame(x = 1, y = 2),
   b = data.frame(x = 3, y = 4)
)

# One example is with quasiquotation.
dplyr::bind_rows(!!!dfs)

# When used in this context, the behaviour of !!! is know as "spatting" in Ruby,
# Go, PHP, and Julia. It is closely related to *args and **kwarg in Python which
# are sometimes called argument unpacking. 

# Side note on the := operator. It is recognized by R's parser, but it does not
# have any code associated with it. It looks like = but allows expressions on 
# either side, making it a more flexible alternative to =. It is used in 
# data.table for this reason. 

# -- 19.6.4 With base R ---

# Base R provides a swiss army knife to solve all of these problems: do.call()
# do.call() has two main arguments, the what gives a function to call and the
# second argument args is a list of arguments to pass to the function.
# Thus, do.call("f", list(x, y, z)) is equivalent to f(x, y, z).
do.call("rbind", dfs)

# --- 19.7 Case studies --------------------------------------------------------

# Some of the case studies use purrr, the combination of quasiquotation and 
# functional program can be elegant.
library(purrr)

# --- 19.7.2 Map-reduce to generate code ---

# Suppose you have a linear model specified by the following coefficients.
intercept <- 10
coefs <- c(x1 = 5, x2 = -4)

# How can this be converted to the expression: 10 + (x1 * 5) + (x2 * -4)
coef_sym <- syms(names(coefs))
coef_sym

summands <- map2(coef_sym, coefs, ~ expr((!!.x * !!.y)))
summands

summands <- c(intercept, summands)
summands

eq <- reduce(summands, ~ expr(!!.x + !!.y))
eq

# Example of all functions being compiled into a function.
linear <- function(var, val) {
   var <- ensym(var)
   coef_name <- map(seq_along(val[-1]), ~ expr((!!var)[[!!.x]]))
   
   summands <- map2(val[-1], coef_name, ~ expr((!!.x * !!.y)))
   summands <- c(val[[1]], summands)
   
   reduce(summands, ~ expr(!!.x + !!.y))
}

linear(x, c(10, 5, -4))

# --- 19.7.4 Creating functions ---

# rlang::new_function() can be used to generate functions by hand.
power <- function(exponent) {
   new_function(
      exprs(x = ), 
      expr({
         x ^ !!exponent
      }), 
      caller_env()
   )
}
power(0.5)

#-------------------------------------------------------------------------------
# 20 Evaluation
#-------------------------------------------------------------------------------

# --- 20.1 Introduction --------------------------------------------------------

# The user facing inverse of quotation is unquotation: it gives the user the 
# ability to selectively evaluate parts of an otherwise quoted argument.
# The developer facing complement of quotation is evaluation: it gives the
# developer the ability to evaluate quoted expressions in custom environments
# to achieve specific goals. There are two big ideas:

# The quosure: a data structure that captures an expression along with its 
# associated environment, as found in function arguments.

# The data mask, which makes it easier to evaluate an expression in the context
# of a data frame. 

# Together, quasiquotation, quosures, and data masks form tidy evaluation or
# tidy eval for short. Tidy eval porivdes a principled apporach to non-standard
# evaluation that makes it possible to use functions both interactively and 
# embedded with other functions. 

library(rlang)
library(purrr)

# --- 20.2 Evaluation basics ---------------------------------------------------

# eval() has two key arguments: expr and env. expr is the object to evaluate.
x <- 10
eval(expr(x))

# The second argument env gives the environment in which the expression should
# be evaluated, i.e. where to look for the values of x, y, and +.
x <- 10
y <- 2
eval(expr(x + y), env(x = 1000))

# --- 20.2.1 Application: local() ---

# Sometimes you want to perform a chunk of calculation that creates some 
# intermediate variables. The intermediate variables have no long term use 
# and may take up a lot of memory. One approach is to clean up with rm(),
# another is to wrap the code in a function and call it once. A more elegant
# approach is to use local.
rm(x, y)

foo <- local({
   x <- 10
   y <- 200
   x + y
})

foo
x
y

# The essence of local is simple. We create a new environment to evaluate an 
# expression. This effectively emulates running expr as if it was inside a 
# function (it is lexically scoped).

# --- 20.3 Quosures ------------------------------------------------------------

# Every use of eval() involved both an expression and an environment. This 
# coupling is so important that we need a data structure that can hold both 
# pieces. Base R does not have this structure, so rlang fills the gap with 
# quosure. 

# --- 20.3.1 Creating ---

# Use enquo() and enquos() to capture user supplied expressions. The vast 
# majority of quosures should be created this way.
foo <- function(x) enquo(x)
foo(a + b)

# new_quosure() creates a quosure from its components, an expression and an 
# environment. 
new_quosure(expr(x + y), env(x = 1, y = 10))

# --- 20.3.2 Evaluating ---

# Quosures are paired with a new evaluation function: eval_tidy() that takes
# a single quosure instead of an expression-environment pair. 
q1 <- new_quosure(expr(x + y), env(x = 1, y = 10))
eval_tidy(q1)

# For this example, eval_tidy() is a shortcut of: 
eval(get_expr(q1), get_env(q1))

# --- 20.3.3 Dots ---

# Quosures are typically a convenience to make code cleaner because you only have 
# one object to pass around instead of two. However, they are essential when 
# working with ... because it is possible for each argument passed to ... to 
# be associated with a different environment. 

# In the following example note that both quosures have the same expression, x,
# but a different environment. 
f <- function(...) {
   x <- 1
   g(..., f = x)
}
g <- function(...) {
   enquos(...)
}

x <- 0
qs <- f(global = x)
qs

# The means taht when you evaluate them, you get the correct results:
map_dbl(qs, eval_tidy)

# --- 20.4 Data masks ----------------------------------------------------------

# Data mask is a data frame where the evaluated code will look first for 
# variable defenitions. The data mask is the key idea that powers base functions
# like with() and subset(). It is also used throughout tidyverse packages like
# dplyr and ggplot2. 

# --- 20.4.1 Basics ---

# The data mask allows you to mingle variables from an environment and a data
# frame in a single expressions. You supply the data mask as the second argument
# to eval_tidy():
q1 <- new_quosure(expr(x * y), env(x = 100))
df <- data.frame(y = 1:10)

eval_tidy(q1, df)

# This code is hard to follow because every object is being created from scratch.
# It is easier to see what is occurring if a wrapper function is created.
with2 <- function(data, expr) {
   expr <- enquo(expr)
   eval_tidy(expr, data)
}

# The code above can then be rewritten as:
x <- 100
with2(df, x * y)

# --- 20.4.2 Pronouns ---

# Using a data mask introduces ambiguity. In the following example you do not 
# know whether x will come from the data mask or the environment.
with2(df, x)

# To resolve this issue, data mask provides two pronouns, .data and .env
# .data$x always refers to the x in the data mask
# .env$x always refers to x in the environment
x <- 1
df <- data.frame(x = 2)

with2(df, .data$x)
with2(df, .env$x)

# --- 20.4.3 Application: subset() ---

# base::subset() shows the power of tidy evaluation. subset(), like dplyr::filter()
# provides a convenient way for selecting rows of a data frame. 
sample_df <- data.frame(a = 1:5, b = 5:1, c = c(5, 3, 1, 4, 1))

# Shorthand for sample_df[sample_df$a >= 4, ]
subset(sample_df, a >= 4)

# The core of subset() is quite simple, it takes two arguments: a data frame (data),
# and an expression (rows). We evaluate rows using data as a data mask, then use
# the results to subset the data frame with [. 
subset2 <- function(data, rows) {
   rows <- enquo(rows)
   rows_val <- eval_tidy(rows, data)
   stopifnot(is.logical(rows_val))
   
   data[rows_val, , drop = FALSE]
}

subset2(sample_df, b == c)

# --- 20.4. Application: transform ---

# A more complicated situation is base::transform() which allows you to add 
# new variable to a data frame, evaluating their expressions in the context
# of missing variables. 
df <- data.frame(x = c(2, 3, 1), y = runif(3))
transform(df, x = -x, y2 = 2 * y)

# In our own version, we capture the unevaluated ... with enquos(...) and then
# evaluate each expression using a for loop. 
transform2 <- function(.data, ...) {
   dots <- enquos(...)
   
   for (i in seq_along(dots)) {
      name <- names(dots)[[i]]
      dot <- dots[[i]]
      
      .data[[name]] <- eval_tidy(dot, .data)
   }
   
   .data
}

transform2(df, x2 = x * 2, y = -y)

#-------------------------------------------------------------------------------
# 21 Translating R code
#-------------------------------------------------------------------------------

# --- 21.1 Introduction --------------------------------------------------------

# The combination of first-class environments, lexical scoping, and 
# metaprogramming offers a powerful toolkit for translating R code into other
# languages. One example is dbplyr which allows you to express data manipulation
# in R and automatically translate it to SQL.
library(dbplyr)

translate_sql(x^2)
translate_sql(x < 5 & !is.na(x))
translate_sql(!first %in% c("John", "Roger", "Robert"))

# Translating R to SQL is complex because of idiosyncracies in SQL. Here we will
# develop two simple domain specific languages (DSL), one to generate HTML and
# another to generate mathematical equations in LaTeX.

# --- 21.2 HTML ----------------------------------------------------------------

library(rlang)
library(purrr)

# HTML looks like the following:

# <body>
#    <h1 id='first'>A heading</h1>
#    <p>Some text &amp; <b>some bold text.</b></p>
#    <img src='myimg.png' width='100' height='100' />
# </body>

# The key component of HTML is tags. There are over 100 HTML tags, in this 
# chapter we focus on a handful. 

# <body> is the top-level tag that contains all content.
# <h1> defines a top level heading.
# <p> defines a paragraph.
# <b> emboldens text.
# <img> embeds an image.

# Because < and > have special meanings in HTML, you can’t write them directly.
# Instead you have to use the HTML escapes: &gt; and &lt;. And since those escapes
# use &, if you want a literal ampersand you have to escape it as &amp;

# Our goal is to be able to generate the HMTL above with an R functions that
# generally matches the structure of HTML.
with_html(
   body(
      h1("A heading", id = "first"),
      p("Some text &", b("some bold text.")),
      img(src = "myimg.png", width = 100, height = 100)
   )
)

# --- 21.2.2 Escaping ---

# Escaping is fundamental to translation. There are two related challenges with
# translating html.

# We need to automatically escape &, <, and >.
# We need to make sure that &, <, and > are not double-escaped.

# The easiest solution is to create an S3 class that distinguishes between 
# regular text that needs escaping and HTML.

html <- function(x) structure(x, class = "advr_html")

print.advr_html <- function(x, ...) {
   out <- paste0("<HTML> ", x)
   cat(paste(strwrap(out), collapse = "\n"), "\n", sep = "")
}

# --- 23.2.3 Basic tag functions ---

# Next we will write a one-tag function by hand, then figure out how to 
# generalise it so that we can generate a function for every tag. 

# We begin with <p>. HTML tags can have both attributes (id or class) and 
# children like <b> or <i>. We need a way to seperate these in the function call. 
# We use named and unnamed arguments to do this. 

# For example, a call to p() may look like: 
p("Some text. ", b(i("some bold italic text")), class = "mypara")

# We could list all possible attributes of the <p> tag, but that would be tedious
# so we resort to using ... and seperate components based on whether or not 
# they are named. 

# With this in mind we create a helper function that wraps around rlang::list2()
# and returns the named and unnamed components seperately.

library(rlang)

dots_partition <- function(...) {
   dots <- list2(...)
   
   if (is.null(names(dots))) {
      is_named <- rep(FALSE, length(dots))
   } else {
      is_named <- names(dots) != ""
   }
   
   list(
      named = dots[is_named],
      unnamed = dots[!is_named]
   )
}

# Example of finding the named and unnamed inputs.
str(dots_partition(a = 1, 2, b = 3, 4))

# We can now create out p() function. Note that there is one new function here:
# html_attributes(). This takes a named list and returns the HTML attribute 
# specification as a string. This deals with some of the idiosyncracies of HTML
# that we will not go into, but it can be sourced online.

source("dsl-html-attributes.r")

p <- function(...) {
   dots <- dots_partition(...)
   attribs <- html_attributes(dots$named)
   children <- map_chr(dots$unnamed, escape)
   
   html(paste0(
      "<p", attribs, ">",
      paste(children, collapse = ""),
      "</p>"
   ))
}

# --- 21.3 LaTeX ---------------------------------------------------------------

# Next we will convert R expressions into LaTeX math equivalents.

# --- 21.3.1 LaTeX mathematics ---

# To begin we will cover how formulas are expressed in LaTeX.

# Most simple mathematical equations are written in the same way that you would
# type them in R. x * y, z ^ 5. Subscripts are written using _ (e.g. x_1).

# Special characters start with a \: e.g. \pi.

# More complicated functions look like: \name{arg1}{arg2}. For example, to write
# a fraction you would use \frac{a}{b}, to write a square root you would use
# \sqrt{a}.

# --- 21.3.2 Goal ---

# The goal is to use these rules to automatically convert R expressions to 
# appropriate LaTeX representations. This will be accomplished in four stages.

# Convert known symbols: pi -> \pi
# Leave other symbols unchanged: x -> x, y -> y
# Convert known functions to their special form: sqrt(frac(a, b)) ->
#  \sqrt{\frac{a}{b}}

# --- 21.3.3 to_math() ---

# To begin, we need a wrapper function to convert R expressions into LaTeX 
# math expressions.

# We will begin with a basic to_math() function and a S3 class for printing
# LaTeX.

to_math <- function(x) {
   expr <- enexpr(x)
   out <- eval_bare(expr, latex_env(expr))
   
   latex(out)
}

latex <- function(x) structure(x, class = "advr_latex")
print.advr_latex <- function(x) {
   cat("<LATEX> ", x, "\n", sep = "")
}

# --- 21.3.4 Known symbols ---

# The first step is to create an environment that will convert the special LaTeX
# symbols used for Greek characters, e.g. pi to \pi. 

greek <- c(
   "alpha", "theta", "tau", "beta", "vartheta", "pi", "upsilon",
   "gamma", "varpi", "phi", "delta", "kappa", "rho",
   "varphi", "epsilon", "lambda", "varrho", "chi", "varepsilon",
   "mu", "sigma", "psi", "zeta", "nu", "varsigma", "omega", "eta",
   "xi", "Gamma", "Lambda", "Sigma", "Psi", "Delta", "Xi",
   "Upsilon", "Omega", "Theta", "Pi", "Phi"
)
greek_list <- set_names(paste0("\\", greek), greek)
greek_env <- as_environment(greek_list)

# We can then check the functionality:
latex_env <- function(expr) {
   greek_env
}

to_math(pi)
to_math(beta)

# --- 21.3.5 Unknown symbols ---


flat_map_chr <- function(.x, .f, ...) {
   purrr::flatten_chr(purrr::map(.x, .f, ...))
}

# If a symbol is not Greek, we want to leave it as is. This is tricky because
# we don't know in advance what symbols will be used and we cannot generate 
# them all. Instead we will walk the AST to find all symbols.
all_names_rec <- function(x) {
   switch_expr(x,
      constant = character(),
      symbol = as.character(x),
      call = flat_map_chr(as.list(x[-1]), all_names)
   )
}

all_names <- function(x) {
   unique(all_names_rec(x))
}

all_names(expr(x + y + f(a, b, c, 10)))

# We now want to take a list of symbols and convert it to an environment so that
# each symbol is mapped to its corresponding string representation, i.e. so 
# eval(quote(x), env) yields "x". We again use the pattern of converting a 
# named character to a list, then converting the list to an environment.
latex_env <- function(expr) {
   names <- all_names(expr)
   symbol_env <- as_environment(set_names(names))
   
   symbol_env
}

to_math(x)
to_math(pi)

# This works but we need to combine it with the Greek symbols environment. 
# Because we want to preference Greek over defaults, symbol_env needs to be
# the parent of greek_env. This gives us a function that can convert both Greek
# and unknown symbols. 
latex_env <- function(expr) {
   # Unknown symbols
   names <- all_names(expr)
   symbol_env <- as_environment(set_names(names))
   
   # Known symbols
   env_clone(greek_env, parent = symbol_env)
}

to_math(x)
to_math(pi)

# --- 21.3.6 Known functions ---

# Next we will add functions, starting with some helpers that make it easy to 
# add unary and binary operators. These functions only assemble strings.

unary_op <- function(left, right) {
   new_function(
      exprs(e1 = ),
      expr(
         paste0(!!left, e1, !!right)
      ),
      caller_env()
   )
}

binary_op <- function(sep) {
   new_function(
      exprs(e1 = , e2 = ),
      expr(
         paste0(e1, !!sep, e2)
      ),
      caller_env()
   )
}

unary_op("\\sqrt{", "}")
binary_op("+")

# Using these examples we can map a few illustrative examples of converting R
# to LaTeX. 
# Binary operators
f_env <- child_env(
   .parent = empty_env(),
   `+` = binary_op(" + "),
   `-` = binary_op(" - "),
   `*` = binary_op(" * "),
   `/` = binary_op(" / "),
   `^` = binary_op("^"),
   `[` = binary_op("_"),
   
   # Grouping
   `{` = unary_op("\\left{ ", " \\right}"),
   `(` = unary_op("\\left( ", " \\right)"),
   paste = paste,
   
   # Other math functions
   sqrt = unary_op("\\sqrt{", "}"),
   sin =  unary_op("\\sin(", ")"),
   log =  unary_op("\\log(", ")"),
   abs =  unary_op("\\left| ", "\\right| "),
   frac = function(a, b) {
      paste0("\\frac{", a, "}{", b, "}")
   },
   
   # Labelling
   hat =   unary_op("\\hat{", "}"),
   tilde = unary_op("\\tilde{", "}")
)

# We again modify latex_env() to include this functions environment.

latex_env <- function(expr) {
   
   # Known functions
   f_env
   
   # Default symbols
   names <- all_names(expr)
   symbol_env <- as_environment(set_names(names), parent = f_env)
   
   # Known symbols
   greek_env <- env_clone(greek_env, parent = symbol_env)
   
   greek_env
}

to_math(sin(x + pi))
to_math(log(x[i]^2))

#-------------------------------------------------------------------------------
# 22 Debugging 
#-------------------------------------------------------------------------------

# --- 22.3 Locating errors -----------------------------------------------------

# The most important tool for locating errors is traceback() which shows you the
# sequence of calls (the call stack) leading to the error. 
f <- function(a) g(a)
g <- function(b) h(b)
h <- function(c) i(c)
i <- function(d) {
   if (!is.numeric(d)) {
      stop("`d` must be numeric", call. = FALSE)
   }
   d + 10
}

f("a")

# In R Studio you can click "Show Traceback" or you can run:
traceback()

# --- 22.4 Interactive debugger ------------------------------------------------

# The interactive debugger allows you to pause the execution of a function and 
# interactively explore its state. 

# The easiest way to do this is with R Studio's "Rerun with Debug" feature. This
# reruns the command that created the error, pausing execution where the error
# occurred. 

# Otherwise you can insert a call to browser() where you want to pause and 
# re-run the function.
g <- function(b) {
   browser()
   h(b)
}
f(10)

# browser() is a regular function so you can use it in a if() statement.
g <- function(b) {
   if (b < 0) {
      browser()
   }
   h(b)
}

# You end up in an interactive environment inside the function where you can
# run arbitrary R code and explore the state. 

# --- 22.4.1 Breakpoints --- 

# In R Studio you can set a breakpoint by clicking to the left of the number 
# line or pressing shift + F9.

# Breakpoints operate the same as browser().

# --- 22.4.2 Debug ---

# debug() inserts a browser statement in the first line of a specified function.
f <- function(x) {
   x <- x + 5
   print(x)
}

f(5)
debug(f)
f(5)

# the browser statement is removed with undebug()
undebug(g)

# --- 22.5 Non-interactive debugging -------------------------------------------

# Debugging is most challenging when you cannot run the code interactively,
# for instance, if the code is run on another computer. 

# The following is a common checklist for non-interactive errors.

# Is the global environment different? Have you loaded different packages? Are
# objects left from previous sessions causing differences?

# Is the working directory different?

# Is the PATH environment variable which determines where external commands 
# (like git) are found, different?

# Is the R_LIBS environment variable, which determines where library() looks
# for packages different? 

#-------------------------------------------------------------------------------
# 23 Measuring Performance
#-------------------------------------------------------------------------------

# Before you make your code faster, you need to figure out what is making it
# slow. Instead of relying on intuition, you should profile you code: measure
# the run time of each line of code using realistic inputs. 

# We will use profvis for profiling and bench for microbenchmarking.
library(profvis)
library(bench)

# --- 23.2 Profiling -----------------------------------------------------------

# Across programming languages, the primary tool used to understand code 
# performance is a profiler. There are a number of different types of profilers
# but R uses a fairly simple type called a sampling or statistical profiler.
# A sampling profiler stops the execution of code every few milliseconds and
# records the call stack (i.e which function is currently executing, the function
# that called the function, and so on). 

# Consider the example of function f() below:
f <- function() {
   pause(0.1)
   g()
   h()
}
g <- function() {
   pause(0.1)
   h()
}
h <- function() {
   pause(0.1)
}

profvis(f())

# --- 23.2.2 Memory profiling ---

# There is a special entry in the profvis() display that does not correspond
# to the function: <GC>. This indicates whether the garbage collector is 
# running. If <GC> is taking up a lot of time that is an indication that you 
# are creating many short-lived objects. For example: 

x <- integer()
for (i in 1:1e4) {
   x <- c(x, i)
}

profvis::profvis({
   
   x <- integer()
   for (i in 1:1e4) {
      x <- c(x, i)
   }
   
})

# You will see that most of the time is spent in the garbage collector. 

# --- 23.3 Microbenchmarking ---------------------------------------------------

# A microbenchmark measures the performance of a very small piece of code. 
# Microbenchmarks are useful for comparing small snippets of code for specific
# tasks. 

# Be wary of generalising the results of microbenchmarks to real code: the observed
# differences between microbenchmarks will typically be dominated by higher 
# order effects in real code. A deep understanding of subatomic physics is not
# useful when baking. 

# The following example of the bench package compares two methods of computing
# the square root.
x <- runif(100)
(lb <- bench::mark(
   sqrt(x),
   x ^ 0.5
))

# mem_alloc tells you the amount of memory allocated on the first run and 
# n_gc tells you the total number of garbage collections over the run.
# Here we can see that sqrt() is faster than ^0.5

# You can visualize the distribution of execution time with plot().
plot(lb)

#-------------------------------------------------------------------------------
# 24 Improving Performance
#-------------------------------------------------------------------------------

library(bench)

# --- 24.2 Code organisation ---------------------------------------------------

# There are two traps that are easy to fall into when making code faster. 
# 1 Writing faster but incorrect code.
# 2 Writing code that you think is faster but is actually no better. 

# When trying to improve the speed of a bottleneck, you are likely to come up 
# with multiple approached. Write a function for each approach which encapsulates
# all behaviour, this makes it easier to check that each approach returns the
# correct result and the time it takes for the function to run. 

# To demonstrate the strategy we look at two ways to compute mean.
mean1 <- function(x) mean(x)
mean2 <- function(x) sum(x) / length(x)

# You want to generate a representative test case. A test case that is too large
# will take too long to run and make it difficult to compare. A test case that
# is too small may not scale up to the real problem. Here we choose 100,000
# test cases. 
x <- runif(1e5)

# Next use bench::mark() to compare the speed. 
bench::mark(
   mean1(x),
   mean2(x)
)[c("expression", "min", "median", "itr/sec", "n_gc")]

# --- 24.4 Do as little as possible --------------------------------------------

# The easiest way to make a function faster is to make it do less work. One way 
# to do this is to use a function more tailored to a specific problem. 

# rowSums(), colSums(), rowMeans(), and colMeans() are faster than equivalent
# invocations of apply() because they are vectorised. 

# vapply is faster than sapply because it pre-specifies the output type.

# If you want to see if a vector contains a single value, any(x == 10) is much
# faster than 10 %in% x because testing equality is simpler than testing
# set inclusion. 

# --- 24.4.1 Method dispatch ---

# Sometimes you can make a function faster by specifying method dispatch. 
x <- runif(1e2)

bench::mark(
   mean(x),
   mean.default(x)
)[c("expression", "min", "median", "itr/sec", "n_gc")]

# --- 24.5 Vectorise -----------------------------------------------------------

# Vectorising is about taking a whole object approach to a problem. It is about 
# thinking with vectors instead of scalars.

# Loops with vectorised functions are written in C instead of R. Loops in C are
# much faster because they have less overhead. 

# --- 24.6 Avoiding copies -----------------------------------------------------

# A pernicious source of slow R code is growing an object with a loop. Whenever 
# you use c(), append(), cbind(), rbind(), or paste() to create a bigger object, 
# R must first allocate space for the new object and then copy the old object
# to its new home. 

#-------------------------------------------------------------------------------
# Rewriting R code in C++
#-------------------------------------------------------------------------------

# Rewriting key functions in C++ is made possible by way of the Rcpp package.
# Rcpp makes it easy to connect C++ to R. While it is possible to write C or
# Fortran code for use in R, it is painful by comparison. Rcpp provides a clean
# and approachable API that lets you write high performance code, insulated from
# R's complexc C API. 

# Typical bottlenecks that C++ can address are: 

# Loops that cannot be vectorised because subsequent iterations depend on 
# previous ones. 

# Recursive functions or problems which involve calling a function millions of
# times. The overhead of calling a function in C++ is much lower than in R.

# We use Rcpp to call C++ from R.
library(Rcpp)

# --- 25.2 Getting started with C++ ---

# cppFunction() allows you to write C++ functions in R. 
cppFunction('int add(int x, int y, int z) {
  int sum = x + y + z;
  return sum;
}')

add
add(1, 2, 4)

# When you run this code Rcpp will compile the C++ code and construct an R 
# function that connects to the compiled C++ function. 

# --- 25.2.1 No inputs, scalar output ---

# Let's start with a simple function that has no inputs and returns a scalar.
one <- function() 1L

# The equivalent in C++ is:
# int one() {
#    return 1;
# }

# Compiling from C++ to R.
cppFunction('int one() {
  return 1;
}')

# This demonstrates some of the differences between R and C++

# The syntax to create a function looks like the syntax to call a function;
# you do not use assignment to create functions like in R.

# You must declare the type of output the function returns (in this case int which
# represents a scalar integer), the C++ classes for the most common types of 
# R vectors are: NumericVector, IntegerVector, CharacterVector, and LogicalVector.

# Scalars and vectors are different. The scalar equivalents of numeric, integer,
# character, and logical are double, int, String, bool.

# You must use an explicit return statement to return a value from a function.

# Every statement is terminated by ;.

# --- 25.2.2 Scalar input, scalar output ---

# The next example function implements a scalar version of the sign() function
# which returns 1 if the input is positive and -1 if its negative. 

signR <- function(x) {
   if (x > 0) {
      1
   } else if (x == 0) {
      0
   } else {
      -1
   }
}

cppFunction('int signC(int x) {
  if (x > 0) {
    return 1;
  } else if (x == 0) {
    return 0;
  } else {
    return -1;
  }
}')

# --- 25.2.3 Vector input, scalar output ---

# One big difference between R and C++ is that loops cost much less in C++. For
# example, we could implement a sum function in R using a loop. If you have
# been programming in R for a while your reaction to this may be visceral!
sumR <- function(x) {
   total <- 0
   for (i in seq_along(x)) {
      total <- total + x[i]
   }
   total
}

# In C++ loops have little overhead so its fine to use them. 
cppFunction('double sumC(NumericVector x) {
  int n = x.size();
  double total = 0;
  for(int i = 0; i < n; ++i) {
    total += x[i];
  }
  return total;
}')

# To find the length of the vector we use the .size() method which returns 
# an integer. C++ methods are called with . (a full stop).

# The for statement has a different syntax: for(init; check; increment). The 
# special prefix operator ++ increases the value of i by 1. 

# In C++ vector indices start at 0 which means that the last element is at 
# position n - 1.

# This is a good example of where C++ is much more efficient than R. sumC() is
# competitive with the built in and optimized sum() function and it is miles 
# ahead of our sumR() loop. 

x <- runif(1e3)
bench::mark(
   sum(x),
   sumC(x),
   sumR(x)
)[1:6]

# --- 25.2.4 Vector input, vector output ---

# Next we will create a function that computes the Euclidean distance between 
# a vector and a vector of values. 
pdistR <- function(x, ys) {
   sqrt((x - ys) ^ 2)
}

cppFunction('NumericVector pdistC(double x, NumericVector ys) {
  int n = ys.size();
  NumericVector out(n);

  for(int i = 0; i < n; ++i) {
    out[i] = sqrt(pow(ys[i] - x, 2.0));
  }
  return out;
}')

# --- 25.2.5 Using sourceCpp ---------------------------------------------------

# So far we have used inline C++ with cppFunction(). For real problems it is 
# usually easier to use stand alone C++ files and then source them into R using
# sourceCpp(). 

# Your stand alone C++ file should have the extension .cpp and needs to start 
# with:

# #include <Rcpp.h>
# using namespace Rcpp;

# And for each function you want available in R you must prefix it with: 
# // [[Rcpp::export]]

#-------------------------------------------------------------------------------
# The End!
#-------------------------------------------------------------------------------
