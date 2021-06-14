package NeuralNet.ActivationFunction;

public class FastSigmoid implements ActivationFunction {

    @Override
    public double calculateActivation(double input) {
        return 0.5*(input/(1+Math.abs(input))+1);
    }
    
}
