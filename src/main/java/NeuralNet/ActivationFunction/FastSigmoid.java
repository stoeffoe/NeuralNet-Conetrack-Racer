package NeuralNet.ActivationFunction;

/** 
 * Faster alternative to the sigmoid.
 * Similar but not equal.
*/
public class FastSigmoid implements ActivationFunction {

    @Override
    public double calculateActivation(double input) {
        return 0.5*(input/(1+Math.abs(input))+1);
    }
    
}
