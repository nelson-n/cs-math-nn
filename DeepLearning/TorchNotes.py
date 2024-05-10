
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


# Image tensor transformations used to perform data augmentation.
from torchvision import transforms 

transform = transforms.Compose([
    transforms.RandomHorizontalFlip(),  # Randomly flip the image horizontally.
    transforms.RandomRotation(20),       # Randomly rotate the image by up to 10 degrees.
    transforms.ColorJitter(brightness=0.8, contrast=0.8, saturation=0.8, hue=0.5),  # Adjust brightness, contrast, saturation, and hue.
    #transforms.RandomResizedCrop(224),  # Randomly crop the image and resize it to 224x224.
    transforms.ToTensor(),              # Convert the image to a PyTorch tensor.
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])  # Normalize the image.
])




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


# Learning rate schedulers.
scheduler = torch.optim.lr_scheduler.ReduceLROnPlateau(optimizer, mode = 'max', factor = 0.1, patience = 10, threshold=0.05)
scheduler = torch.optim.lr_scheduler.MultiStepLR(optimizer, [50, 70], gamma = 0.1)




#===============================================================================
# Neural Network Models
#===============================================================================

#-------------------------------------------------------------------------------
# Simple Linear Classifier
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
# Simple Convolutional Neural Network for Classification
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


#-------------------------------------------------------------------------------
# Complex Convolutional Neural Network for Classification 
#-------------------------------------------------------------------------------

# Features batch normalization, dropout, max pooling, and average pooling.

class CNNClassifier(torch.nn.Module):
    def __init__(self):
        super().__init__()

        # Convolution layers.
        self.conv1 = torch.nn.Conv2d(in_channels=3, out_channels=16, kernel_size=7, stride=1, padding=7//2)
        self.conv2 = torch.nn.Conv2d(in_channels=16, out_channels=32, kernel_size=3, stride=1, padding=3//2)
        self.conv3 = torch.nn.Conv2d(in_channels=32, out_channels=64, kernel_size=3, stride=1, padding=3//2)

        # Batch normalization layers.
        self.bn1 = torch.nn.BatchNorm2d(16)  
        self.bn2 = torch.nn.BatchNorm2d(32)  
        self.bn3 = torch.nn.BatchNorm2d(64)  

        # Convolution dropout layers.
        self.dropout = torch.nn.Dropout2d(p=0.35)

        # Linear dropout layers.
        self.fc_dropout = torch.nn.Dropout(p=0.35)

        # Activation layers.
        self.relu = torch.nn.ReLU()

        # Max pooling layers.
        self.max_pool = torch.nn.MaxPool2d(kernel_size=2, stride=2)

        # Average pooling layer.
        self.avg_pool = torch.nn.AvgPool2d(kernel_size=3, stride=1)  # Average pooling layer

        # Linear layer to be applied after convolution layers.
        self.fc1 = torch.nn.Linear(2304, 128)
        self.fc2 = torch.nn.Linear(128, 6)

    def forward(self, x):

        # Convolution layers.
        x = self.conv1(x)
        x = self.bn1(x)
        x = self.relu(x)
        x = self.max_pool(x)
        x = self.dropout(x)

        x = self.conv2(x)
        x = self.bn2(x)
        x = self.relu(x)
        x = self.max_pool(x)
        x = self.dropout(x)

        x = self.conv3(x)
        x = self.bn3(x)
        x = self.relu(x)
        x = self.max_pool(x)
        x = self.dropout(x)

        x = self.avg_pool(x)  

        x = x.view(x.size(0), -1)  # Flatten the feature map.
        x = self.fc1(x)  
        x = self.relu(x)
        x = self.fc_dropout(x)
        x = self.fc2(x) 

        return x


#-------------------------------------------------------------------------------
# Fully Convolutional Network
#-------------------------------------------------------------------------------

# Fully convolutional network for semantic segmentation. Takes in an image of 
# size (B,3,H,W) and outputs a tensor of size (B,5,H,W) where the 5 channels
# classify each pixel in the image into one of 5 classes.

class FCN(torch.nn.Module):
    def __init__(self):
        super().__init__()

        # Encoder block (downsampling).
        self.enc11 = torch.nn.Conv2d(3, 64, kernel_size=3, padding=1)
        self.enc12 = torch.nn.Conv2d(64, 64, kernel_size=3, padding=1)
        self.pool1 = torch.nn.MaxPool2d(kernel_size=2, stride=2)

        self.enc21 = torch.nn.Conv2d(64, 128, kernel_size=3, padding=1)
        self.enc22 = torch.nn.Conv2d(128, 128, kernel_size=3, padding=1)
        self.pool2 = torch.nn.MaxPool2d(kernel_size=2, stride=2)

        self.enc31 = torch.nn.Conv2d(128, 256, kernel_size=3, padding=1)
        self.enc32 = torch.nn.Conv2d(256, 256, kernel_size=3, padding=1)

        # Decoder block (upsampling).
        self.upconv1 = torch.nn.ConvTranspose2d(256, 128, kernel_size=2, stride=2)
        self.dec11 = torch.nn.Conv2d(256, 128, kernel_size=3, padding=1)
        self.dec12 = torch.nn.Conv2d(128, 128, kernel_size=3, padding=1)

        self.upconv2 = torch.nn.ConvTranspose2d(128, 64, kernel_size=2, stride=2)
        self.dec21 = torch.nn.Conv2d(128, 64, kernel_size=3, padding=1)
        self.dec22 = torch.nn.Conv2d(64, 64, kernel_size=3, padding=1)

        # Output layer (5 classes).
        self.outconv = torch.nn.Conv2d(64, 5, kernel_size=1)

        # Activation layers.
        self.relu = torch.nn.ReLU()

    def forward(self, x):

        # Record the original dimensions of the input image, if image is smaller
        # than 64 in either dim, pad it to 64.
        orig_dims = x.size()

        if x.size(2) < 64 or x.size(3) < 64:
            x = F.pad(x, (0, 64 - x.size(3), 0, 64 - x.size(2)))

        # Encoder.
        enc11 = self.relu(self.enc11(x))
        enc12 = self.relu(self.enc12(enc11))
        enc_pool1 = self.pool1(enc12)

        enc21 = self.relu(self.enc21(enc_pool1))
        enc22 = self.relu(self.enc22(enc21))
        enc_pool2 = self.pool2(enc22)

        enc31 = self.relu(self.enc31(enc_pool2))
        enc32 = self.relu(self.enc32(enc31))

        # Decoder.
        xu1 = self.upconv1(enc32)
        xu11 = torch.cat([xu1, enc22], dim=1)
        xd11 = self.relu(self.dec11(xu11))
        dec11 = self.relu(self.dec12(xd11))

        xu2 = self.upconv2(dec11)
        xu21 = torch.cat([xu2, enc12], dim=1)
        xd21 = self.relu(self.dec21(xu21))
        dec21 = self.relu(self.dec22(xd21))

        # Output layer.
        out = self.outconv(dec21)

        # If image was padded, crop the output to the original size.
        if orig_dims[2] < 64 or orig_dims[3] < 64:
            out = out[:, :, :orig_dims[2], :orig_dims[3]]

        return out


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


#-------------------------------------------------------------------------------
# Convolution Neural Network Training Loop
#-------------------------------------------------------------------------------

# Command line training flags.
if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser()
    parser.add_argument('--log_dir')

    # Put custom arguments here
    parser.add_argument('--run_name')
    parser.add_argument('-e', '--epochs', default = 5)
    parser.add_argument('-l', '--learningrate', default = 0.01)
    parser.add_argument('-c', '--continue_training', action='store_true')

    args = parser.parse_args()
    train(args)


def train(args):
    from os import path


    # --- Model Initialization -------------------------------------------------

    # Check if CUDA is available and specify device.
    if torch.cuda.is_available():
        # Select GPU device.
        device = torch.device("cuda")
        print("Using GPU:", torch.cuda.get_device_name(0))  # Print GPU name.
    else:
        # Fallback to CPU if GPU not available.
        device = torch.device("cpu")
        print("CUDA is not available. Using CPU.")

    # Initiate tensorboard logger if --log_dir is specified in the command line.
    train_logger, valid_logger = None, None
    if args.log_dir is not None:
        train_logger = tb.SummaryWriter(path.join(args.log_dir, 'train/', args.run_name), flush_secs=1)
        valid_logger = tb.SummaryWriter(path.join(args.log_dir, 'valid/', args.run_name), flush_secs=1)

    # Specify model.    
    model = CNNClassifier().to(device)

    # Load pre-existing model and continue training if --continue_training is specified in the command line.
    if args.continue_training:
        model.load_state_dict(torch.load(path.join(path.dirname(path.abspath(__file__)), 'cnn_model.th')))

    # Specify hyperparameters.
    num_epochs = int(args.epochs)
    learning_rate = float(args.learningrate)

    # Specify general parameters.
    print_frequency = 5

    # Define torch optimizer and loss function.
    optimizer = torch.optim.SGD(model.parameters(), lr = learning_rate, momentum=0.9)
    loss_function = torch.nn.CrossEntropyLoss()

    # Optional, learning rate schedulers.
    # scheduler = torch.optim.lr_scheduler.ReduceLROnPlateau(optimizer, mode = 'max', factor = 0.1, patience = 10, threshold=0.05)
    # scheduler = torch.optim.lr_scheduler.MultiStepLR(optimizer, [50, 70], gamma = 0.1)

    print('Number of epochs:', num_epochs)
    print('Initial learning rate:', learning_rate)


    # --- Training Loop -------------------------------------------------------

    # Record full training time.
    train_start_time = time.time()

    # Load training dataset and testing dataset.
    TrainDataLoader = load_data('./data/train')
    TestDataLoader = load_data('./data/valid')

    global_step = 0

    # Accuracy storage.
    train_avg_accs = []
    val_avg_accs = []

    # --- Epoch Iteration Begins -----------------------------------------------

    for epoch in range(num_epochs):
        print()
        model.train()

        iter_accs = []
        epoch_start_time = time.time()
        epoch_loss = 0.0

        # --- Batch Iteration Begins -------------------------------------------

        for batch_num, (inputs, targets) in enumerate(TrainDataLoader):

            # Move inputs and targets to device.
            inputs = inputs.to(device)
            targets = targets.to(device)

            # Zero out the gradients.
            optimizer.zero_grad()

            # Run forward pass of the model.
            model_output = model.forward(inputs)

            # Compute loss on the batch forward pass.
            loss = loss_function.forward(model_output, targets)

            if train_logger is not None:
                train_logger.add_scalar('loss', loss, global_step)

            # Compute accuracy on the batch forward pass.
            iter_acc = accuracy(model_output, targets)
            iter_accs.append(iter_acc.detach().cpu().numpy())

            # Backpass and optimize.
            loss.backward()
            optimizer.step()
            global_step += 1

            # Add batch loss to the epoch loss.
            epoch_loss += loss.item()

        # --- Batch Iteration Ends ---------------------------------------------


        # --- Epoch Logging ----------------------------------------------------

        # --- Compute Training Accuracy for the Epoch ---
        train_avg_acc = sum(iter_accs) / len(iter_accs)
        train_avg_accs.append(train_avg_acc)
        if train_logger:
            train_logger.add_scalar('accuracy', train_avg_acc, global_step)

        # --- Compute Validation Accuracy for the Epoch ---
        model.eval()
        with torch.no_grad():

            accs = []

            for inputs, targets in TestDataLoader:

                # Move inputs and targets to device.
                inputs = inputs.to(device)
                targets = targets.to(device)

                acc = accuracy(model.forward(inputs), targets)
                accs.append(acc.item())

            val_avg_acc = sum(accs) / len(accs)
            val_avg_accs.append(val_avg_acc)

        if valid_logger:
            valid_logger.add_scalar('accuracy', val_avg_acc, global_step)

        # --- Print Epoch Statistics to Console ---

        # Print the learning rate to console every few epochs.
        if (epoch + 1) % print_frequency == 0:
            print(f'Epoch [{epoch+1}/{num_epochs}], Learning Rate: {optimizer.param_groups[0]["lr"]:.4f}')

        # Print the epoch loss to console every few epochs.
        if (epoch + 1) % print_frequency == 0:
            print(f'Epoch Loss: {epoch_loss:.4f}')

        # Print the epoch train time every few epochs.
        epoch_end_time = time.time()
        if (epoch + 1) % print_frequency == 0:
            print(f'Epoch train time: {(epoch_end_time - epoch_start_time) / 60:.2f} minutes')

        # Print the average training accuracy every few epochs.
        if (epoch + 1) % print_frequency == 0:
                print('Training Set Accuracy:', train_avg_acc)

        # Print the average validation accuracy every few epochs.
        if (epoch + 1) % print_frequency == 0:
                print('Validation Set Accuracy:', val_avg_acc)


        # --- Post Epoch Updates ---

        # Update the learning rate if training accuracy does not decrease.
        # scheduler.step(train_avg_acc)


    # --- Epoch Iteration Ends -------------------------------------------------

    # Save model once trained.
    save_model(model)

    # Record full training time in minutes.
    train_end_time = time.time()
    print(f'Full training time: {(train_end_time - train_start_time) / 60:.2f} minutes')



