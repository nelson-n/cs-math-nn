
import torch

## Sections
# - Tensor Transformations
# - Activation Functions
# - Loss Functions
# - Optimizers
# - Neural Network Models
# - Training Loops



#===============================================================================
# Tensor Transformations
#===============================================================================

# Turn a length-d list of tensors into a single tensor with dimensionality d.
batch = torch.stack(data)







#===============================================================================
# Activation Functions
#===============================================================================

import torch.nn.functional as F

# Softmax activation.
F.log_softmax(x, dim=1)





#===============================================================================
# Loss Functions
#===============================================================================

import torch.nn.functional as F

# Negative log likelihood loss.
F.nll_loss(output, target)






#===============================================================================
# Optimizers
#===============================================================================

# SGD optimizer.
optimizer = torch.optim.SGD(model.parameters(), lr = learning_rate)







#===============================================================================
# Neural Network Models
#===============================================================================

#-------------------------------------------------------------------------------
# Linear Classifier
#-------------------------------------------------------------------------------

# Single linear layer classifier.
class LinearClassifier(torch.nn.Module):
    def __init__(self):
        super().__init__()

        # Define single linear layer.
        self.l1 = torch.nn.Linear(3*64*64, 6)

    def forward(self, x):
        """
        @x: torch.Tensor((B,3,64,64))
        @return: torch.Tensor((B,6))
        """

        # Flatten the input tensor.
        x = x.view(x.size(0), -1)

        # Apply the linear transformation.
        x = self.l1(x)

        return x

#-------------------------------------------------------------------------------
# Multilayer Perceptron Classifier
#-------------------------------------------------------------------------------

# Two linear layers with a ReLU activation function.
class MultilayerClassifier(torch.nn.Module):
    def __init__(self):
        super().__init__()

        HiddenLayerNodes = 10

        # Define layers.
        self.l1 = torch.nn.Linear(3*64*64, HiddenLayerNodes)
        self.relu = torch.nn.ReLU()
        self.l2 = torch.nn.Linear(HiddenLayerNodes, 6)

    def forward(self, x):
        """
        @x: torch.Tensor((B,3,64,64))
        @return: torch.Tensor((B,6))
        """

        # Flatten the input tensor
        x = x.view(x.size(0), -1)

        # Apply transformation.
        x = self.l1(x)
        x = self.relu(x)
        x = self.l2(x)

        return x



#-------------------------------------------------------------------------------
# Simple Convolutional Neural Network
#-------------------------------------------------------------------------------

class CNN(torch.nn.Module):
    def __init__(self):
        super().__init__()

        # Convolution layers.
        self.conv1 = torch.nn.Conv2d(in_channels=3, out_channels=16, kernel_size=7, stride=1, padding=1)
        self.conv2 = torch.nn.Conv2d(in_channels=16, out_channels=32, kernel_size=3, stride=1, padding=1)
        self.conv3 = torch.nn.Conv2d(in_channels=32, out_channels=64, kernel_size=3, stride=1, padding=1)

        # Dropout layers.
        self.dropout = torch.nn.Dropout2d(p=0.25)

        # Activation layers.
        self.relu = torch.nn.ReLU()

        # Linear layer to be applied after convolution layers.
        # Output after convolutions w/ 4 convolutions of stride 2 is: 48 channels * 64/(2^4) * 64/(2^4)
        self.fc = torch.nn.Linear(230400, 6)

    def forward(self, x):

        # Convolution layers.
        x = self.conv1(x)
        x = self.relu(x)
        x = self.dropout(x)
        x = self.conv2(x)
        x = self.relu(x)
        x = self.dropout(x)
        x = self.conv3(x)
        x = self.relu(x)
        x = self.dropout(x)

        # Flatten tensor to 1d.
        x = x.view(x.size(0), -1)

        # Linear layer.
        x = self.fc(x)

        return x











#===============================================================================
# Training Loops
#===============================================================================

#-------------------------------------------------------------------------------
# General Training Loop
#-------------------------------------------------------------------------------

def train(args):

    # Load model.
    model = MultilayerClassifier()

    # Hyperparameters.
    num_epochs = 10
    learning_rate = 0.01

    # Load training dataset and testing dataset.
    TrainDataLoader = DataLoader(dataset, num_workers=num_workers, batch_size=batch_size, shuffle=True, drop_last=False)
    TestDataLoader = DataLoader(dataset, num_workers=num_workers, batch_size=batch_size, shuffle=True, drop_last=False)

    # Define torch optimizer.
    optimizer = torch.optim.SGD(model.parameters(), lr = learning_rate)

    for epoch in range(num_epochs):

        epoch_loss = 0.0

        # Iterate through the batches of the dataloader.
        for batch_num, (inputs, targets) in enumerate(TrainDataLoader):

            # Zero out the gradients.
            optimizer.zero_grad()

            # Run forward pass of the model.
            logits = model.forward(inputs)

            # Compute loss using loss function on the forward pass.
            loss = ClassificationLoss().forward(logits, targets)

            # Backpass and optimize.
            loss.backward()
            optimizer.step()

            # Add batch loss to the epoch loss.
            epoch_loss += loss.item()
        
        # Print the epoch loss every few epochs.
        if (epoch + 1) % 5 == 0:
            print(f'Epoch [{epoch+1}/{num_epochs}], Loss: {epoch_loss:.4f}')

        # Compute accuracy on the test set every few epochs.
        if (epoch + 1) % 5 == 0:
            
            # Do not update gradient during testing.
            with torch.no_grad():

                accs = []

                for inputs, targets in TestDataLoader:
                    acc = accuracy(model.forward(inputs), targets)
                    accs.append(acc.item())

                avg_acc = sum(accs) / len(accs)
                print('Accuracy:', avg_acc)

    # Save model once trained.
    save_model(model)





