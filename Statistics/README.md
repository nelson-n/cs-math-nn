# Statistics

`MLE.R`

Estimates the properties of a Gaussian (normal) distribution using Maximum Likelihood Estimation (MLE). MLE is implemented from scratch using a density function, negative log likelihood function, and a naive optimization function. From-scratch results are then benchmarked against MLE performed with Nelder-Mead simplex optimization.

`MetropolisHastings.py`

Implements the Metropolis-Hastings method for obtaining a sequence of random samples from a distribution that is difficult to sample from (for example if you have observed some data that may be difficilt to observe and you want to know what parameters have given rise to this data). First implements the Metropolis-Hastings method using the canonical package PyMC3, then implements it from scratch.

`OLS_logit_SGD.py`

Estimates ordinary least squares (OLS) regression coefficients and logit coefficients using stochastic gradient descent (SGD). Then tests the accuracy of estimated coefficients out-of-sample.
