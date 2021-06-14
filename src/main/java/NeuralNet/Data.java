/*
- Ruben Hiemstra 0924010
- Stefan Beenen 0963586
- Jordy Weijgertse 0974347
*/

package NeuralNet;

public class Data {
    private double[][] outputValue;
    private double[] inputValues;
    
    /**
     * 
     * @param _matrix double[] vector of the input
     * @param _desiredValue double[] outputValue vector of the output
     */
    public Data(double[] inputValues, double[] outputValue) {
        this.inputValues = inputValues;
        this.outputValue = MatMath.fromList(outputValue);
    }

    /**
     * 
     * @return double[]
     */
    public double[] getMatrix() {
        return inputValues;
    }

    /**
     * 
     * @return double[][]
     */
    public double[][] getDesiredValue() {
        return outputValue;
    }
}
