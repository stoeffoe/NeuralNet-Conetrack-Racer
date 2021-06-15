package NeuralNet.ActivationFunction;

public class Sigmoid implements ActivationFunction {

    @Override
    public double calculateActivation(double input) {
        return 1 / (1 + Math.exp(-input));
    }
    
}
