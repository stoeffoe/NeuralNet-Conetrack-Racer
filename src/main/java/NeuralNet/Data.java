/*
- Ruben Hiemstra 0924010
- Stefan Beenen 0963586
- Jordy Weijgertse 0974347
*/

package NeuralNet;

public class Data {
    private double[][] desiredValue;
    private double[] matrix;

    public Data(double[] _matrix, double[] _desiredValue) {
        this.matrix = _matrix;
        this.desiredValue = MatMath.fromList(_desiredValue);
    }

    public double[] getMatrix() {
        return matrix;
    }

    public double[][] getDesiredValue() {
        return desiredValue;
    }
}
