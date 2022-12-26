
#-------------------------------------------------------------------------------
# SVD_PCA.py written by nelson-n, 2022-08-11
#-------------------------------------------------------------------------------

root_dir = ''

import os
import gzip
import numpy as np
from matplotlib.image import imread
import matplotlib.pyplot as plt

#===============================================================================
# Testing SVD Theory on a Model Matrix
#===============================================================================

## Setting some definitions:
# X = input matrix
# U = U matrix (great definition I know)
# S = Sigma matrix (diagonal matrix of ranks)
# Vt = Transposed V matrix
# In theory, X = USVt

# Init model matrix.
X = np.matrix([
    [2, 4, 6],
    [5, 1, 3],
    [2, 9, 4],
    [7, 3, 1],
    [6, 3, 4]
])

# Compute SVD.
U, S, Vt = np.linalg.svd(X)

# Convert sigma matrix from array to diag matrix and if necessary add rows of
# zeroes to the sigma matrix (for the case that num rows X > num cols X) to
# create an S long (Sl) version of the sigma matrix.
S = np.diag(S)

if X.shape[0] > X.shape[1]:
    Sl = np.vstack([S, np.zeros([X.shape[0] - len(S), X.shape[1]])])

# Test that the SVD is not a farce! X = USVt
np.dot(U, np.dot(Sl, Vt))

# Now testing some of the intuition of the SVD.
# XtX = the correlation matrix among the columns of X, i.e. the inner products
# of the input data itself. Large values represent similarity and small values
# represent places that are more orthagonal (less similar). 
Xt = np.transpose(X)
XtX = np.dot(Xt, X)

# Given the definition of the SVD as X = USVt, we can plug USVt in for X in 
# the correlation matrix XtX and perform the following calculation.
# XtX = VSUtUSVt = VS^2Vt
np.dot(V, np.dot(np.square(S), Vt))

# VS^2Vt is the eigenvalue decomposition of the correlation matrix XtX where
# S^2 are the eigenvalues of XtX and V are the eigenvectors of XtX.

# S^2 = eigenvalues of XtX
np.square(S)
np.linalg.eig(XtX)[0]

# V = eigenvectors of XtX
V
np.linalg.eig(XtX)[1]

# Thus, in the singular value decomposition USVt, V is just the eigenvectors
# of the correlation matrix X and S (sigma) is the square root of the 
# eigenvalues of the same correlation matrix X.

#===============================================================================
# Testing Some Image Compression with SVD
#===============================================================================

# Set path to demo image.
data_dir = root_dir + 'Math/data/'

# Load image, convert to grayscale (1 channel), view image.
img = imread(data_dir + 'demo.jpeg')
X = np.mean(img, -1)

plt.imshow(X)
plt.show()

# Perform SVD on image X using the economy SVD indicated with the full_matrices
# argument. This cuts off the zero rows of the sigma matrix.
U, S, Vt = np.linalg.svd(X, full_matrices = False)
S = np.diag(S)

# Subset to the first 5 rows/cols of the U, S, Vt matrices and reconstruct a
# compressed version of X using the fancy @ matmul operator. Then view compressed image.
i = 5
X_comp = U[:, :i] @ S[0:i, :i] @ Vt[:i, :]

plt.imshow(X_comp)
plt.show()

# Subset to the first 20 rows/cols.
i = 20
X_comp = U[:, :i] @ S[0:i, :i] @ Vt[:i, :]

plt.imshow(X_comp)
plt.show()

# Subset to the first 100 rows/cols.
i = 100
X_comp = U[:, :i] @ S[0:i, :i] @ Vt[:i, :]

plt.imshow(X_comp)
plt.show()

# Plot the eigenvalues (square root technically) to see where the information
# content of the image starts to fall off (plotting sigma matrix).
plt.figure(1)
plt.semilogy(S)
plt.show()

#===============================================================================
# Testing PCA Theory on a Model Matrix
#===============================================================================

# PCA intuitive explanation: a hierarchical coordinate system that captures
# the directions in an input data matrix X that explain the most variance 
# in X.

# Using the same input X matrix as in the SVD theory section above. First 
# calculate a mean centered matrix B by substracting mean of the rows of X
# from X.
X_mean = np.mean(X, axis = 0)
B = X - X_mean

# Calculate covariance matrix.
cov = np.cov(B, rowvar = False)

# Compute eigenvalues and eigenvectors of the covariance matrix, then order
# them by the size of the eigenvalues.
eigval, eigvec = np.linalg.eigh(cov)
index = np.argsort(eigval)[::-1]
eigvec = eigvec[:, index]
eigval = eigval[index]

# This is equivalent to scaling down a large matrix using eigenvectors. If M 
# is a large matrix we can look for a vector o and a scalar n such that 
# M * o = n * o where o are eigenvectors and n are eigenvalues. By the same 
# logic, in the PCA case: cov * eigvec = eigval * eigvec
cov @ eigvec
eigval * eigvec

# In PCA, T = BV where T are the principal components and V are the eigenvectors
# (called the loadings in PCA).
T = B @ eigvec

# Putting it all together in a function that calculates PCA given an input
# matrix X.
def PCA(X):

	X_mean = np.mean(X, axis = 0)
	B = X - X_mean

	cov = np.cov(B, rowvar = False)

	eigval, eigvec = np.linalg.eigh(cov)
	index = np.argsort(eigval)[::-1]
	eigvec = eigvec[:, index]
	eigval = eigval[index]

	T = B @ eigvec

	return T

# Using SVD, one can compute B = USVt, which allows for the conversion between
# SVD values and PCA loadings.
U, S, Vt = np.linalg.svd(B, full_matrices = False)
S = np.diag(S)

# Plugging in USVt for B in the PCA equation: T = BV.
T_alt = U @ S @ Vt @ eigvec

#===============================================================================
# Using PCA on a Transformed Matrix with Known Properties
#===============================================================================

# We are going to take a matrix with normal gaussian properties centered at
# 0, 0 and then transform it to give it known properties, we will then test
# whether PCA is able to find these properties. This demo is inspired by
# Steve Brunton's series on PCA.

# Data will be centered at 2, 1.
center = np.array([2, 1])

# Data will be stretched by a factor of 2 in the x-direction and compressed
# by a factor of 0.5 in the y-direction.
stretch = np.array([2, 0.5])

# Data will be rotated by a factor of pi/3 using the rotation matrix.
theta = np.pi/3

rotate = np.array([[np.cos(theta), -np.sin(theta)],
                   [np.sin(theta), np.cos(theta)]]) 

# Create transformed matrix with 10,000 data points.
X = rotate @ np.diag(stretch) @ np.random.randn(2, 10000) + np.diag(center) @ np.ones([2, 10000])

# Plot X to make sure we did the transformations correctly.
fig = plt.figure()
fig.add_subplot(111)
plt.scatter(X[0,:], X[1,:], s=2)
plt.show()

# Calculate mean centered matrix B.
X_mean = np.mean(X, axis = 1)
B = X - np.tile(X_mean, (10000, 1)).T

# Check that mean centered matrix B looks correct.
fig = plt.figure()
fig.add_subplot(111)
plt.scatter(B[0,:], B[1,:], s=2)
plt.show()

# Find principal components via SVD.
U, S, Vt = np.linalg.svd(B / np.sqrt(10000), full_matrices = False)

# The resultant sigma matrix S has values of ~2 and ~0.5 which tells you that
# the first principal component has a lot of variance (the stretch of factor 
# 2 in the x-direction) and the second principal component has less variance
# the compression of factor 0.5 in the y-direction.

# The resultant U matrix tells us how the distribution is rotated. Note that
# the transformation induced by the rotate matrix is the same as the U matrix.
U
rotate

#===============================================================================
# Using SVD on MNIST Dataset
#===============================================================================

# Set path to MNIST images.
data_dir = root_dir + '/NeuralNetworks/data/MNIST/'

# Load MNIST images, the images are loaded as a [10000, 784] matrix with 
# 10000 images (rows) each with 28*28=784 pixels (images and black and white
# so there is only one channel, thus we get a nice 2D matrix).
imgs  = gzip.open(data_dir + 't10k-images-idx3-ubyte.gz', 'rb').read()
imgs = np.frombuffer(imgs, dtype=np.uint8, offset=16).reshape(-1, 28*28)/255

# Select first image in set and reshape to a [28, 28] matrix, then view the image.
img = imgs[1,:].reshape(28, 28)

plt.imshow(img)
plt.show()

# We want the handwritten digits to be columnwise, so transpose matrix.
X = np.transpose(imgs)

# Calculate economy SVD.
U, S, Vt = np.linalg.svd(X, full_matrices = False)

# Plot the sigmas. Note that the energy/information does not fall off until
# 650+ pixels, making the SVD or PCA of this dataset not particularly useful.
plt.figure(1)
plt.semilogy(S)
plt.show()
