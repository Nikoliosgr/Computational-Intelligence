import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MLP {
    // Define the number of inputs (d), number of categories (K), and number of neurons in hidden layers
    public static final int d = 2; // Number of inputs (x1, x2), based on the .cvs files
    public static final int K = 4; // Number of categories (C1, C2, C3, C4)
    public static final int H1 = 20; // Number of neurons in the first hidden layer
    public static final int H2 = 20; // Number of neurons in the second hidden layer
    public static final int H3 = 20; // Number of neurons in the third hidden layer (for PT3 only)

    // Define the activation function type (tanh or relu)
    public static final String ACTIVATION_FUNCTION = "relu";
    
    // Learning rate and stopping threshold
    public static final float LEARNING_RATE = 0.01f;
    public static final double STOPPING_THRESHOLD = 1e-4; // this shit does not work

    // Global variables for weights and biases
    public static float[][] W1 = new float[H1][d]; // Weights for the first hidden layer
    public static float[] b1 = new float[H1]; // Biases for the first hidden layer
    public static float[][] W2 = new float[H2][H1]; // Weights for the second hidden layer
    public static float[] b2 = new float[H2]; // Biases for the second hidden layer
    public static float[][] W3 = new float[H3][H2]; // Weights for the third hidden layer (for PT3 only)
    public static float[] b3 = new float[H3]; // Biases for the third hidden layer (for PT3 only)
    public static float[][] W_out = new float[K][H2]; // Weights for the output layer (for PT2)
    public static float[] b_out = new float[K]; // Biases for the output layer (for PT2)

    // Training and test data
    public static List<float[]> trainInputs = new ArrayList<>();
    public static List<float[]> trainTargets = new ArrayList<>();
    public static List<float[]> testInputs = new ArrayList<>();
    public static List<float[]> testTargets = new ArrayList<>();

    // Random number generator for weight initialization
    public static Random rand = new Random(); // set the seed to "12345L" for the same results.

    // Initialize weights and biases randomly in the range (-1, 1)
    public static void initializeWeightsAndBiases() {
        for (int i = 0; i < H1; i++) {
            for (int j = 0; j < d; j++) {
                W1[i][j] = rand.nextFloat() * 2 - 1; // Random value between -1 and 1
            }
            b1[i] = rand.nextFloat() * 2 - 1;
        }

        for (int i = 0; i < H2; i++) {
            for (int j = 0; j < H1; j++) {
                W2[i][j] = rand.nextFloat() * 2 - 1;
            }
            b2[i] = rand.nextFloat() * 2 - 1;
        }

        for (int i = 0; i < H3; i++) {
            for (int j = 0; j < H2; j++) {
                W3[i][j] = rand.nextFloat() * 2 - 1;
            }
            b3[i] = rand.nextFloat() * 2 - 1;
        }

        for (int i = 0; i < K; i++) {
            for (int j = 0; j < H2; j++) {
                W_out[i][j] = rand.nextFloat() * 2 - 1;
            }
            b_out[i] = rand.nextFloat() * 2 - 1;
        }
    }

    // Activation functions tanh and relu
    public static float tanh(float x) {
        return (float) Math.tanh(x);
    }

    public static float relu(float x) {
        return Math.max(0, x);
    }

    // Function to apply the activation function based on what was chosen type
    public static float activate(float x) {
        if (ACTIVATION_FUNCTION.equals("tanh")) {
            return tanh(x);
        } else if (ACTIVATION_FUNCTION.equals("relu")) {
            return relu(x);
        } else {
            throw new IllegalArgumentException("Invalid activation function type");
        }
    }

    // Load data from a CSV file
    public static void loadData(String filename, List<float[]> inputs, List<float[]> targets) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Parse input features (x1, x2)
                float x1 = Float.parseFloat(parts[0]);
                float x2 = Float.parseFloat(parts[1]);
                inputs.add(new float[]{x1, x2});

                // Parse the category (C1, C2, C3, C4)
                String category = parts[2];
                float[] target = new float[K];
                switch (category) {
                    case "C1":
                        target[0] = 1;
                        break;
                    case "C2":
                        target[1] = 1;
                        break;
                    case "C3":
                        target[2] = 1;
                        break;
                    case "C4":
                        target[3] = 1;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid category: " + category); // plz do not
                }
                targets.add(target);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Forward-pass function
    public static float[] forwardPass(float[] x, int d, float[] y, int K, boolean isPT3) {
        // Hidden layer 1
        float[] h1 = new float[H1];
        for (int i = 0; i < H1; i++) {
            float sum = 0;
            for (int j = 0; j < d; j++) {
                sum += W1[i][j] * x[j];
            }
            h1[i] = (float) Math.tanh(sum + b1[i]);
        }

        // Hidden layer 2
        float[] h2 = new float[H2];
        for (int i = 0; i < H2; i++) {
            float sum = 0;
            for (int j = 0; j < H1; j++) {
                sum += W2[i][j] * h1[j];
            }
            if(isPT3){
                h2[i] = (float) Math.tanh(sum + b2[i]);
            }else{
                h2[i] = activate(sum + b2[i]);
            }
        }

        // Hidden layer 3 (only for PT3)
        float[] h3 = new float[H3];
        if (isPT3) {
            for (int i = 0; i < H3; i++) {
                float sum = 0;
                for (int j = 0; j < H2; j++) {
                    sum += W3[i][j] * h2[j];
                }
                h3[i] = activate(sum + b3[i]);
            }
        }

        // Output layer
        for (int i = 0; i < K; i++) {
            float sum = 0;
            if (isPT3) {
                // Use h3 as input to the output layer for PT3
                for (int j = 0; j < H3; j++) {
                    sum += W_out[i][j] * h3[j];
                }
            } else {
                // Use h2 as input to the output layer for PT2
                for (int j = 0; j < H2; j++) {
                    sum += W_out[i][j] * h2[j];
                }
            }
            y[i] = sum + b_out[i]; // No activation function for the output layer (linear activation)
        }

        return y;
    }

    // Derivative of the activation function
    public static float activateDerivative(float x) {
        if (ACTIVATION_FUNCTION.equals("tanh")) {
            return (float) (1 - Math.pow(tanh(x), 2)); // Derivative of tanh
        } else if (ACTIVATION_FUNCTION.equals("relu")) {
            return x > 0 ? 1 : 0; // Derivative of ReLU
        } else {
            throw new IllegalArgumentException("Invalid activation function type");
        }
    }

    // Backpropagation function
    public static void backprop(float[] x, int d, float[] t, int K, boolean isPT3) {
        // Forward pass to compute activations
        float[] h1 = new float[H1];
        float[] h2 = new float[H2];
        float[] h3 = new float[H3];
        float[] y = new float[K];

        // Hidden layer 1
        for (int i = 0; i < H1; i++) {
            float sum = 0;
            for (int j = 0; j < d; j++) {
                sum += W1[i][j] * x[j];
            }
            h1[i] = (float) Math.tanh(sum + b1[i]);
        }

        // Hidden layer 2
        for (int i = 0; i < H2; i++) {
            float sum = 0;
            for (int j = 0; j < H1; j++) {
                sum += W2[i][j] * h1[j];
            }if(isPT3){
                h2[i] = (float) Math.tanh(sum + b2[i]);
            }else{
                h2[i] = activate(sum + b2[i]);
            }
        }

        // Hidden layer 3 (only for PT3)
        float[] deltaH3 = new float[H3]; // Declare deltaH3 here
        if (isPT3) {
            for (int i = 0; i < H3; i++) {
                float sum = 0;
                for (int j = 0; j < H2; j++) {
                    sum += W3[i][j] * h2[j];
                }
                h3[i] = activate(sum + b3[i]);
            }
        }

        // Output layer
        for (int i = 0; i < K; i++) {
            float sum = 0;
            if (isPT3) {
                for (int j = 0; j < H3; j++) {
                    sum += W_out[i][j] * h3[j];
                }
            } else {
                for (int j = 0; j < H2; j++) {
                    sum += W_out[i][j] * h2[j];
                }
            }
            y[i] = sum + b_out[i]; // No activation function for the output layer
        }

        // Compute error at the output layer
        float[] deltaOut = new float[K];
        for (int i = 0; i < K; i++) {
            deltaOut[i] = y[i] - t[i]; // Error = output - target
        }

        // Backpropagate error to the last hidden layer
        float[] deltaH2 = new float[H2];
        if (isPT3) {
            for (int i = 0; i < H3; i++) {
                float sum = 0;
                for (int j = 0; j < K; j++) {
                    sum += W_out[j][i] * deltaOut[j];
                }
                deltaH3[i] = sum * activateDerivative(h3[i]); // Use deltaH3 here
            }

            // Backpropagate error to the second hidden layer
            for (int i = 0; i < H2; i++) {
                float sum = 0;
                for (int j = 0; j < H3; j++) {
                    sum += W3[j][i] * deltaH3[j];
                }
                deltaH2[i] = sum * (float) (1 - Math.pow(tanh(h2[i]), 2));
            }
        } else {
            for (int i = 0; i < H2; i++) {
                float sum = 0;
                for (int j = 0; j < K; j++) {
                    sum += W_out[j][i] * deltaOut[j];
                }
                deltaH2[i] = sum * activateDerivative(h2[i]);
            }
        }

        // Backpropagate error to the first hidden layer
        float[] deltaH1 = new float[H1];
        for (int i = 0; i < H1; i++) {
            float sum = 0;
            for (int j = 0; j < H2; j++) {
                sum += W2[j][i] * deltaH2[j];
            }
            deltaH1[i] = sum * (float) (1 - Math.pow(tanh(h1[i]), 2));
        }

        // Update weights and biases for the output layer
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < (isPT3 ? H3 : H2); j++) {
                W_out[i][j] -= LEARNING_RATE * deltaOut[i] * (isPT3 ? h3[j] : h2[j]);
            }
            b_out[i] -= LEARNING_RATE * deltaOut[i];
        }

        // Update weights and biases for the third hidden layer (only for PT3)
        if (isPT3) {
            for (int i = 0; i < H3; i++) {
                for (int j = 0; j < H2; j++) {
                    W3[i][j] -= LEARNING_RATE * deltaH3[i] * h2[j];
                }
                b3[i] -= LEARNING_RATE * deltaH3[i];
            }
        }

        // Update weights and biases for the second hidden layer
        for (int i = 0; i < H2; i++) {
            for (int j = 0; j < H1; j++) {
                W2[i][j] -= LEARNING_RATE * deltaH2[i] * h1[j];
            }
            b2[i] -= LEARNING_RATE * deltaH2[i];
        }

        // Update weights and biases for the first hidden layer
        for (int i = 0; i < H1; i++) {
            for (int j = 0; j < d; j++) {
                W1[i][j] -= LEARNING_RATE * deltaH1[i] * x[j];
            }
            b1[i] -= LEARNING_RATE * deltaH1[i];
        }
    }

    // Compute mean squared error (MSE)
    public static float computeMSE(List<float[]> outputs, List<float[]> targets) {
        float mse = 0;
        for (int i = 0; i < outputs.size(); i++) {
            float[] output = outputs.get(i);
            float[] target = targets.get(i);
            for (int j = 0; j < K; j++) {
                float error = output[j] - target[j];
                mse += error * error;
            }
        }
        return mse / (outputs.size() * K);
    }

    public static void train(int B, int N, boolean isPT3) {
        int numEpochs = 0;
        float prevError = Float.MAX_VALUE;
        float currentError = 0;

        do {
            prevError = currentError; // Update prevError for the next iteration

            // Shuffle the training data for each epoch
            List<float[]> shuffledInputs = new ArrayList<>(trainInputs);
            List<float[]> shuffledTargets = new ArrayList<>(trainTargets);
            for (int i = 0; i < N; i++) {
                int j = rand.nextInt(N);
                float[] tempInput = shuffledInputs.get(i);
                float[] tempTarget = shuffledTargets.get(i);
                shuffledInputs.set(i, shuffledInputs.get(j));
                shuffledInputs.set(j, tempInput);
                shuffledTargets.set(i, shuffledTargets.get(j));
                shuffledTargets.set(j, tempTarget);
            }

            // Process mini-batches
            for (int i = 0; i < N; i += B) {
                int batchSize = Math.min(B, N - i);
                for (int j = 0; j < batchSize; j++) {
                    float[] input = shuffledInputs.get(i + j);
                    float[] target = shuffledTargets.get(i + j);
                    backprop(input, d, target, K, isPT3);
                }
            }

            // Compute total training error for the epoch
            List<float[]> outputs = new ArrayList<>();
            for (float[] input : trainInputs) {
                float[] output = new float[K];
                forwardPass(input, d, output, K, isPT3);
                outputs.add(output);
            }
            currentError = computeMSE(outputs, trainTargets);

            System.out.println("Epoch " + numEpochs + ", Training Error: " + currentError);

            // Check if stopping condition is met after at least 800 epochs
            if (numEpochs >= 800 && Math.abs(prevError - currentError) <= STOPPING_THRESHOLD) {
                System.out.println("Stopping training due to minimal loss improvement.");
                break;
            }

            numEpochs++;
        } while (numEpochs < 800 || Math.abs(prevError - currentError) > STOPPING_THRESHOLD);

        System.out.println("Training completed after " + numEpochs + " epochs.");
    }

    // Compute generalization accuracy on the test set
    public static float computeAccuracy(List<float[]> inputs, List<float[]> targets, boolean isPT3) {
        int correct = 0;
        for (int i = 0; i < inputs.size(); i++) {
            float[] input = inputs.get(i);
            float[] target = targets.get(i);
            float[] output = new float[K];
            forwardPass(input, d, output, K, isPT3);

            // Find the predicted category (index of the maximum output)
            int predicted = 0;
            for (int j = 1; j < K; j++) {
                if (output[j] > output[predicted]) {
                    predicted = j;
                }
            }

            // Find the true category (index of the 1 in the target vector)
            int trueCategory = 0;
            for (int j = 1; j < K; j++) {
                if (target[j] > target[trueCategory]) {
                    trueCategory = j;
                }
            }

            // Check if the prediction is correct
            if (predicted == trueCategory) {
                correct++;
            }

            // Print the test example with different styles based on correctness
            System.out.print("Input: [" + input[0] + ", " + input[1] + "], ");
            System.out.print("True Category: C" + (trueCategory + 1) + ", ");
            System.out.print("Predicted Category: C" + (predicted + 1) + "  ");
            if (predicted == trueCategory) {
                System.out.println("Correct (+)");
            } else {
                System.out.println("Incorrect (-)");
            }
        }

        return (float) correct / inputs.size();
    }

    public static void main(String[] args) {
        // Initialize weights and biases
        initializeWeightsAndBiases();

        // Load training data
        loadData("train_data.csv", trainInputs, trainTargets);
        System.out.println("Training data loaded. Number of samples: " + trainInputs.size());

        // Load test data
        loadData("test_data.csv", testInputs, testTargets);
        System.out.println("Test data loaded. Number of samples: " + testInputs.size());

        // Print the first training sample to verify
        System.out.println("First training sample:");
        System.out.println("Input: [" + trainInputs.get(0)[0] + ", " + trainInputs.get(0)[1] + "]");
        System.out.println("Target: [" + trainTargets.get(0)[0] + ", " + trainTargets.get(0)[1] + ", " +
                trainTargets.get(0)[2] + ", " + trainTargets.get(0)[3] + "]");

        // Test the forward-pass function for PT2
        float[] input = trainInputs.get(0); // Use the first training sample as input
        float[] outputPT2 = new float[K]; // Output vector for PT2
        forwardPass(input, d, outputPT2, K, false); // Pass false for PT2

        // Print the output of the forward-pass for PT2
        System.out.println("Output of forward-pass for PT2:");
        System.out.println("[" + outputPT2[0] + ", " + outputPT2[1] + ", " + outputPT2[2] + ", " + outputPT2[3] + "]");

        // Test the forward-pass function for PT3
        float[] outputPT3 = new float[K]; // Output vector for PT3
        forwardPass(input, d, outputPT3, K, true); // Pass true for PT3

        // Print the output of the forward-pass for PT3
        System.out.println("Output of forward-pass for PT3:");
        System.out.println("[" + outputPT3[0] + ", " + outputPT3[1] + ", " + outputPT3[2] + ", " + outputPT3[3] + "]");

        // Define mini-batch size (B) and number of training samples (N)
        int B = trainInputs.size() / 20; // Mini-batch size (N/20 or N/200)
        int N = trainInputs.size(); // Total number of training samples

        // Train PT2 (2 hidden layers)
        System.out.println("\nTraining PT2 (2 hidden layers):");
        boolean isPT3 = false; // Set to false for PT2
        train(B, N, isPT3);

        // Evaluate PT2 on the test set
        System.out.println("\nEvaluating PT2 on the test set:");
        float accuracyPT2 = computeAccuracy(testInputs, testTargets, isPT3);

        // Reinitialize weights and biases for PT3
        initializeWeightsAndBiases();

        // Train PT3 (3 hidden layers)
        System.out.println("\nTraining PT3 (3 hidden layers):");
        isPT3 = true; // Set to true for PT3
        train(B, N, isPT3);

        // Evaluate PT3 on the test set
        System.out.println("\nEvaluating PT3 on the test set:");
        float accuracyPT3 = computeAccuracy(testInputs, testTargets, isPT3);

        System.out.println("\nComparing PT2 and PT3 on Generalization Accuracy:");
        System.out.println("PT2 Generalization Accuracy: " + (accuracyPT2 * 100) + "%");
        System.out.println("PT3 Generalization Accuracy: " + (accuracyPT3 * 100) + "%");

    }

}