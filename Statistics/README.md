# Statistics

## Notes

`Statistics.ipynb`
* Notes on topics in statistics covering:
    * Theorems
    * Interpretations of Linear Regression

`Regression.ipynb`
* Notes on regression covering:
    * Interpretations of Linear Regression
    * Application of Gradient Descent to Linear Regression
    * Estimating Linear Regression using Stochastic Gradient Descent
    * Logistic Regression
    * Estimating Logistic Regression using Stochastic Gradient Descent

'SVD_PCA.ipynb'
* Notes on singular value decomposition (SVD) and principal component analysis (PCA):
    * 

## Exercises

`MLE.R`

Estimates the properties of a Gaussian (normal) distribution using Maximum Likelihood Estimation (MLE). MLE is implemented from scratch using a density function, negative log likelihood function, and a naive optimization function. From-scratch results are then benchmarked against MLE performed with Nelder-Mead simplex optimization.

Given a statistical model of a probability density and a sample of independent observations, the likelihood of observing the whole sample is defined as the product of the probability densities of the individual values. MLE finds the parameters (mu and sigma) of the statistical model that maximizes the likelihood of observing the given sample. This is done by minimizing negative log likelihood (NLL) via an optimization function. MLE converges asymptotically to the true parameters of the population as the sample size increases.

Likelihood Equation: $L(x) = {\displaystyle \prod_{i=1}^{i=n} \frac{1}{\sqrt{2\pi\sigma^2}}e^{-\frac{(x_i-\mu)^2}{2\sigma^2}}}$

`MetropolisHastings.py`

Implements the Metropolis-Hastings method for obtaining a sequence of random samples from a distribution that is difficult to sample from (for example if you have observed some data that may be difficilt to observe and you want to know what parameters have given rise to this data). First implements the Metropolis-Hastings method using the canonical package PyMC3, then implements it from scratch.
