
## Sections
# - Helper Functions
# - General Notes
# - R Language Notes

#===============================================================================
# Helper Functions
#===============================================================================

# Date formatting template.
Date <- "06-14-2001"
as.Date(Date, format = "%m/%d/%Y")

# Date formatting list.
# %S = second
# %M = minute
# %p = am/pm
# %H = hour in 24 hour clock
# %a = day of the week, abbreviated (Mon-Sun)
# %A = day of the week, full (Monday-Sunday)
# %e = day of the month (1-31)
# %d = day of the month (01-31)
# %m = month, numeric (01-12)
# %b = month, abbreviated (Jan-Dec)
# %B = month, full (January-December)
# %y = year, without century (00-99)
# &Y = year, with century (0000-9999)

# Round all numeric columns in a data frame.
mutate_if(is.numeric, round, digits = 1)

#===============================================================================
# General
#===============================================================================

# Remove all variables from the Global Environment apart from specified variables.
rm(list = setdiff(ls(), "x"))

# Byte compile code for speed increase.
functionX_compiled <- compiler::cmpfun(functionX)

#===============================================================================
# Objects
#===============================================================================

# To understand computations in R, two slogans are helpful:
# - Everything that exists is an object.
# - Everything that happens is a function call

# Look up the memory address of an object.
lobstr::obj_addr()

# Trace when an object X is copied to a new memory address.
cat(tracemem(x), '\n')
untracemem(x)

# Print the memory address of each object in a list.
lobstr::ref(list1, list2)

# To find how much memory an object takes.
lobstr::obj_size(x)

# To see how many bytes of memory R is using.
lobstr::mem_used()

# To print a message to console every time that the garbage collector runs.
gcinfo(TRUE)

# To determine the type of a vector.
typeof()

# To display the structure of an object.
str()

# To check the attributes of an object.
attributes(a)

# To set attributes of objects individually.
attr(a, "x") <- "abcdef"

# To set and remove attributes en masse.
a <- structure(
   1:3,
   x = "abcdef",
   y = 4:6
)

# To remove the class from an object.
unclass()

# To set the names of an object.
x <- 1:3
names(x) <- c("a", "b", "c")

# To set the dimensions of an object.
z <- 1:6
dim(z) <- c(3, 2)

# Generate a random permutation of 1:n.
sample()

# Set operations.
union(x, y)
intersect(x, y)
setdiff(x, y)
setequal(x, y)

# To convert boolean representation to integer representation.
which()

#===============================================================================
# Control Flow and Environments
#===============================================================================

# To perform multiple if/else statements with switch.
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

# The last component of switch() should always throw an error, otherwise 
# unmatched inputs will invisibly return NULL. You can use switch with a
# numeric x, but it is reccomended to only be used with character statements.

# To exit a for loop with next or break.
for (i in 1:10) {
   if (i < 3)
      next
   print(i)
   if (i >= 5)
      break
}

# Additional control flow tools:
while(condition == TRUE) # performs action while condition is true
repeat(action) # repeats action forever until a break is encountered

# To find the formals, body, and environment of a function.
formals()
body()
environment()

# To call a function upon exiting a loop.
with_dir <- function(dir, code) {
   old <- setwd(dir)
   on.exit(setwd(old), add = TRUE)
}

# To find all unbound symbols within a function.
codetools::findGlobals()

# To generate a diagnostic message.
message()

# To prevent automatic printing.
invisible()

# To write an infix operator.
`%+%` <- function(a, b) paste0(a, b)

# Infix operators.
# %% = remainder operator
# %/% = integer division
# %*% = matrix multiplication
# %in% = matching operator

# To write a replacement function.
`second<-` <- function(x, value) {
   x[2] <- value
   x
}

# To get documentation on special functions.
`%%`
?`%%`

# To extract information about an environment.
env_print()

# To get a vector of environment bindings.
env_names()

# To check what the current environment is.
current_env()

# To find all parents of an environment.
env_parents()

# To modify variables in the parent environment with super assignment.
# <<-

# To see the present environments (and the order in which packages have been attached).
base::search()
rlang::search_envs()

# To find the environment of a function.
fn_env(f)
environment(f)

# To print out a call stack tree.
lobstr::cst()
traceback()

# To signal a condition in a function.
stop()
warning()
message()

# Stop a function if inputs are incorrect.
stopifnot()
stopifnot(is.integer(x))

# To ignore conditions in R.
# ignore errors with try()
# ignore warnings with suppressWarnings()
# ignore messages with suppressMessages()

#===============================================================================
# Functionals
#===============================================================================

# To apply a function to a vector and return a list.
purrr::map()
lapply()

# There are four variants of map that return an atomic vector of a specified type.
map_chr() # character vector
map_lgl() # logical vector
map_int() # integer vector
map_dbl() # double vector

# Returning atomic vectors in base R.
sapply()
vapply()

# Map inline function helpers.
map(somelist, function(x) x + 1)
map(somelist, ~ . + 1)

# Take a vector and reduce it to length 1 by calling a function with a pair of values.
# Example, find values in a list of vectors that occur in every list.
Reduce(list, intersect)

# Reduce and return intermediate elements as well.
accumulate(list, `+`)

# To apply a function over multiple dimensions such as a matrix.
apply()

# To cache computation, remember previous inputs and return cached results.
memoise::memoise()

#===============================================================================
# Vectorization
#===============================================================================

input <- c(1:10)

# Vectorization to return a list.

# for loop approach.
output <- vector(mode = "list", length = length(input))
for (i in input) output[[i]] <- input[[i]] * 2

# lapply approach.
output <- lapply(input, function(x) x * 2)

# purrr approach.
output <- purrr::map(input, function(x) x * 2)

# Vectorization to return a vector.

# for loop approach.
output <- vector(mode = "double", length = length(input))
for (i in input) output[[i]] <- input[[i]] * 2

# sapply approach.
output <- sapply(input, function(x) x * 2)

# vapply approach, specifying output type.
output <- vapply(input, function(x) x * 2, numeric(1))

# purrr approach.
output <- purrr::map_dbl(input, function(x) x * 2)

# Vectorization in parallel.
library(parallel)
cl <- makeCluster(detectCores(), type = "FORK")

parApply()
parSapply()
parLapply()

stopCluster(cl)

#===============================================================================
# OOP and Libraries
#===============================================================================

# To see paths to R libraries.
.libPaths()

# To see R libraries installed at .libPaths.
lapply(.libPaths(), list.dirs, recursive = FALSE, full.names = FALSE)

# To check whether the function uses a method or not.
base::mean

# To check what methods are used.
methods(base::mean)

# To check the source code of a given method.
base:::mean.default

# To find the OOP system of an object.
library(sloop)
sloop::otype(1:10)

# To see the methods of a generic or class.
sloop::s3_methods_generic("mean")
sloop::s3_methods_class("ordered")

# To check how a function dispatches to a specific method.
sloop::s3_dispatch(print("hello"))

# => method exists and is found by UseMethod()
# -> method exists and is used by NextMethod()
# * method exists but is not used
# Nothing (and greyed out in console): method does not exist

# To check the methods available to a class:
methods(class = "class")

#===============================================================================
# Metaprogramming 
#===============================================================================

# To pass a string that represents a variable as a function argument.
eval(rlang::sym(paste0("df_num", i)))

# Capture code as an expression.
rlang::expr(10 + 12 + 15)
rlang::expr(x)
rlang::sym("x")

# To capture code as an expression in a function.
f2 <- function(x) rlang::enexpr(x)
f2(a + b + c)

# To construct a function call from its components.
rlang::call2("f", 1, 2, 3)

# View the abstract syntax tree (AST) of a function call.
lobstr::ast(f(a, "b"))

# To parse an expression.
rlang::parse_expr("y <- x + 10")

# To turn inputs into a string.
cement <- function(...) {
   args <- rlang::ensyms(...)
   paste(purrr::map(args, as.character), collapse = " ")
}

cement(Good, morning, Hadley)

# Quasiquotation with bang bang operator !!.
name <- "Hadley"
time <- "morning"
cement(Good, !!time, !!name)

# To accept either symbols or strings as input.
f <- function(...) ensyms(...)
f(x)
f("x")

# Evaluate an expression in a specific environment (quosure).
q1 <- rlang::new_quosure(expr(x + y), env(x = 1, y = 10))
rlang::eval_tidy(q1)

#===============================================================================
# Debugging and Speed
#===============================================================================

# To pause a function evaluation insert browser() inside the function.
g <- function(b) {
   browser()
   h(b)
}

# To automatically insert and uninsert a browser() statement in functions.
debug()
undebug()

# To speed and memory profile a function or chunk of code.
profvis::profvis({
   
   x <- integer()
   for (i in 1:1e4) {
      x <- c(x, i)
   }
   
})

# To compare speed with microbenchmark.
x <- runif(100)
lb <- bench::mark(
   sqrt(x),
   x ^ 0.5
)
plot(lb)

#===============================================================================
# Growth Rates
#===============================================================================

# Period to period percent growth.
p2p_grw <- function(x) ((x - dplyr::lag(x, n = 1)) / dplyr::lag(x, n = 1)) * 100
p2p_grw <- function(x) (x / dplyr::lag(x, n = 1) - 1) * 100

# Annualized period to period percent growth. Assumes data is quarterly, i.e.
# there are 4 observations for each year.
ann_grw <- function(x) ((x / dplyr::lag(x, n = 1))^4 - 1) * 100

# Convert annual percent growth (i.e. 16.99%) to period to period percent growth (i.e. 4%).
ann2p2p_grw <- function(x) (1 + (x / 100))^(1/4) - 1

#===============================================================================
# Data Frequency Conversion
#===============================================================================

# Convert dates in character format to Date objects.
# Note* use ?strptime to find a list of character formats.
as.Date(date_vec, format = "%m/%d/%Y")

# Convert "2020 Q1" vector of dates to Date objects.
as.Date(as.yearqtr(date_vec, "%Y Q%q"), frac = 1)

# Create quarterly tis series based on a vector of data.
tis(
   data = c(5.2, 5.6, 5.5, 6.7, 5.3, 5.2, 6.1, 6.2),
   start = 20200331,
   end = 20211231,
   frequency = 4
)

# Convert higher frequency tis data to quarterly.
df %>%
   lapply(., function(x) convert(x, "quarterly", observed = "averaged", 
          ignore = TRUE, method = "discrete"))

# Convert higher frequency data to quarterly with dplyr.
df %>%
   group_by(date = dint::last_of_quarter(date)) %>%
   summarise(series_name = mean(series_name))

#===============================================================================
# Conditional Case Mutation
#===============================================================================

# Example, using dplyr::case_when() and data.table::fcase() to convert dates that
# fall on a weekend to the following Monday.

# dplyr::case_when()
df %>%
   mutate(weekday = weekdays(date)) %>%
   mutate(date = case_when(
      date == "Saturday" ~ date + days(2),
      date == "Sunday" ~ date + days(1),
      TRUE ~ date # Specify action for all other cases.
   ))

# data.table::fcase()
df[, weekday := weekdays(date)][
   , date := fcase(
      weekday == "Saturday", date + days(2),
      weekday == "Sunday", date + days(1),
      weekday != c("Saturday", "Sunday"), date
   )]

#===============================================================================
# group_by
#===============================================================================
          
# Calculate mean by group.
df <- df %>%
   group_by(id) %>%
   summarise(avg = mean(col1, na.rm = TRUE))
        
# Perform index-specific computation within a group.
df <- df %>%
   group_by(id) %>%
   summarize(diff1 = last(col1) - first(col1) %>%
   summarize(diff2 = nth(col1, 5) - first(col1)
             
df <- df %>%
   group_by(id) %>%
   summarize(diff1 = col1[4:10] - col1[[1]]) 
          
#===============================================================================
# Plotting
#===============================================================================

# Stock ggplot example.
plot <- ggplot(data = df, aes(x = date)) +
   
   geom_line(aes(y = temp_outside, colour = "temp_outside"), lwd = 1) +
   geom_line(aes(y = temp_inside, colour = "temp_inside"), lwd = 0.8) +
   
   scale_color_manual(values = c("blue", "red")) + 
   
   labs(
      title = "Outside Versus Inside Temperature",
      x = "Date",
      y = "Temperature",
      colour = NULL
   ) +
   
   coord_cartesian(ylim = c(40, 80)) +
   
   theme_light() +
   theme(legend.position = c(0.15, 0.85))

ggsave(
   filename = "temp_plot.pdf",
   plot = plot,
   device = "pdf",
   width = 9, height = 5, units = "in",
   path = paste0(dir)
)
