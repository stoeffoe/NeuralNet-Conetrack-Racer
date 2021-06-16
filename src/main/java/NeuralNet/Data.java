package NeuralNet;

public class Data {
    private double[][] inputValues;
    private double[][] outputValues;
    
    /**
     * 
     * @param inputValues input values as row vector
     * @param outputValues output values as row vector
     */
    public Data(double[] inputValues, double[] outputValues) {
        this.inputValues = MatMath.fromList(inputValues);
        this.outputValues = MatMath.fromList(outputValues);
    }

    /**
     * @return The input values as column vector
     */
    public double[][] getInputValues() {
        return inputValues;
    }

    /**
     * @return The output values as column vector
     */
    public double[][] getOutputValues() {
        return outputValues;
    }
}
