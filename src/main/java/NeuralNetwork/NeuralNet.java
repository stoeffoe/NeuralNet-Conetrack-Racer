/*
Authors
- Stefan Beenen 0963586
- Ruben Hiemstra 0924010
- Jordy Weijgertse 0974347
*/
package NeuralNetwork;

public class NeuralNet {

    private double[][][] edges;


    public NeuralNet(int[] layers) {

        edges = new double[layers.length - 1][][];
        double initEdgeWeight = 1;

        for (int i = 0; i < layers.length - 1; i++) {
            int input = layers[i];
            int output = layers[i + 1];

            edges[i] = new double[output][input];

            setEdgeWeight(i, initEdgeWeight);
        }
    }

    void setEdgeWeight(int layer, double initEdgeWeight ){
        for (int row = 0; row < edges[layer].length; row++) {
            for (int col = 0; col < edges[layer][row].length; col++) {
                edges[layer][row][col] = initEdgeWeight;
            }
        }
    }

    public double[][] predict(double[] inputValues) {
        double[][] input = MatMath.fromList(inputValues);
        double[][] output = input;

        for (int layer = 0; layer < edges.length; layer++) {
            input = output;
            output = MatMath.multiply(edges[layer], input);
            output = MatMath.sigmoid(output);
        }
        return MatMath.norm(output);
    }

}
