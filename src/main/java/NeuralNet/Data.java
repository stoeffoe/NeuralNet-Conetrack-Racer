package NeuralNet;

public class Data {
    private double[][] inputValues;
    private double[][] outputValue;
    
    /**
     * 
     * @param _matrix double[] vector of the input
     * @param _desiredValue double[] outputValue vector of the output
     */
    public Data(double[] inputValues, double[] outputValue) {
        this.inputValues = MatMath.fromList(inputValues);
        this.outputValue = MatMath.fromList(outputValue);
    }

    /**
     * 
     * @return double[]
     */
    public double[][] getInputValues() {
        return inputValues;
    }

    /**
     * 
     * @return double[][]
     */
    public double[][] getOutputValue() {
        return outputValue;
    }
}
