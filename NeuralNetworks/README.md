# Neural Networks

`/NNProblems.py`

Solves small problems related to neural networks such as finding optimums and performing gradient descent.

`/FeedforwardNN/FeedforwardNN.py`

Implements a vanilla feedforward neural network from scratch using NumPy. The neural network is then tested and trained on the MNIST handwritten digits dataset.

## /RecurrentNN

`/RecurrentNN.py`

Implements a recurrent neural network from scratch using NumPy, then trains the neural network on token level text from Leo Tolstoy's War and Peace.

`/ProcessText.py` 

Reads .txt files, tokenizes the text, and constructs word index arrays. Used to construct token level input for the recurrent neural network.

`/CharacterRNN.py`

Trains a character level recurrent neural network on the text from Leo Tolstoy's War and Peace.

## /data

### /MNIST

* Dataset containing 28x28 grayscale handwritten digits and accompanying labels seperated into a training and test set. 

`/data/WarAndPeace.txt` 

Raw text version of Leo Tolstoy's War and Peace used to train a recurrent neural network.