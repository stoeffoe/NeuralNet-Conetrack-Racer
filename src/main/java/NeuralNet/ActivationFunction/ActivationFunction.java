package NeuralNet.ActivationFunction;


/**
 * Neural network's activation function interface
 */
public interface ActivationFunction {
    
    /**
     * Calculates the Activation of the neuron based on it's input
     * @param input The summed edges
     * @return Activation value
     */
    public double calculateActivation(double input);

}
