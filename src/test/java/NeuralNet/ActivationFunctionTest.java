package NeuralNet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import NeuralNet.ActivationFunction.*;


public class ActivationFunctionTest {
    
    @Test
    public void SigmoidTest() {
        ActivationFunction sigmoid = new Sigmoid();

        assertEquals(0.26894, sigmoid.calculateActivation(-1.0), 0.00001);
    }

    @Test
    public void FastSigmoidTest() {
        ActivationFunction fastSigmoid = new FastSigmoid();

        assertEquals(0.25000, fastSigmoid.calculateActivation(-1.0), 0.00001);
    }


}
