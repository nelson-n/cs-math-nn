
# Deep Learning

## Notes

`/DeepLearningPrinciples.ipynb`

Notes on the principles of deep learning with an emphasis on theory. Covers:
* Network Design Principles
* Kernel Methods
* Neural Networks
* Linear Algebra and Probability for Deep Learning
* Linear Classification / Linear Regression Models
* Computational Graphs
* Stochastic Gradient Descent (SGD)
* Activation Functions
* Convolutions
* Optimization & Normalization
* Residual Networks
* Training Optimization
* Reinforcement Learning

`/DeepLearningCode/DeepLearningCode.ipynb`

Notes and implementations of deep learning algorithms. Covers:
* Tensors
* TensorBoard
* Linear Classification
* Gradient Descent
* Computational Graphs
* Gradient Descent on a Computational Graph
* Deep Networks in PyTorch
* Convolution Neural Networks in PyTorch
* Weights Initialization
* Normalization in PyTorch
* Learning Rate Schedules
* PreTrained Architectures
* Deep Network with Agent Action
* Imitation Learning
* DAgger Algorithm

`/TorchNotes.py`

Notes on PyTorch and PyTorch implementations. Covers:
* Tensor Transformations
* Activation Functions
* Loss Functions
* Optimizers
* Neural Network Models
* Training Loops

## Exercises

`/NNProblems.py`

Solves small problems related to neural networks such as finding optimums and performing gradient descent.

`/FeedforwardNN/FeedforwardNN.py`

Implements a vanilla feedforward neural network from scratch using NumPy. The neural network is then tested and trained on the MNIST handwritten digits dataset.

#### RecurrentNN

`/RecurrentNN//RecurrentNN.py`

Implements a recurrent neural network from scratch using NumPy, then trains the neural network on token level text from Leo Tolstoy's War and Peace.

`/RecurrentNN/ProcessText.py` 

Reads .txt files, tokenizes the text, and constructs word index arrays. Used to construct token level input for the recurrent neural network.

`/RecurrentNN/CharacterRNN.py`

Trains a character level recurrent neural network on the text from Leo Tolstoy's War and Peace.

#### /data

`/data/MNIST`

* Dataset containing 28x28 grayscale handwritten digits and accompanying labels seperated into a training and test set. 

`/data/WarAndPeace.txt` 

Raw text version of Leo Tolstoy's War and Peace used to train a recurrent neural network.
