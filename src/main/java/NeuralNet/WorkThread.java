package NeuralNet;

import java.util.concurrent.Callable;

import NeuralNet.ActivationFunction.ActivationFunction;

class WorkThread implements Callable<NNdata> {



    private double[][][] edges;
    private Data[] dataSet;
    private ActivationFunction activationFunction;
    private int[] edge;

    public WorkThread(double[][][] edges, Data[] dataSet, int[] edge, ActivationFunction activationFunction ) {
        this.edges = edges;
        this.dataSet = dataSet;
        this.edge = edge;
        this.activationFunction = activationFunction;
    }
    /**
     * Passes the input values through the neural net
     * @param inputValues double input vector 
     * @return what the computer thinks is right 
     */
    public double[][] predict(double[][] input) {
        double[][] output = input;

        for (int layer = 0; layer < edges.length; layer++) {
            input = output;
            output = MatMath.multiplyAndActivate(edges[layer], input, activationFunction);
        }
        return output;
    }


    /**
     * calculate the error of eache datapoint 
     * @param dataSet Data[]
     * @return returns the average error
     */
    private double calculateAverageError(Data[] dataSet) {
        double errorSum = 0;
        for (Data data : dataSet) {
            errorSum += calculateError(data);
        }

        return errorSum / dataSet.length;
    }

    /**
     * Calculate the sum of squared error of the vector 
     * @param data Data object with the inputvalues and corresponding outputvalue
     * @return The sum of squared errors 
     */
    private double calculateError(Data data) {
        double[][] target = data.getOutputValues();
        double[][] output = predict(data.getInputValues());

        return MatMath.sumSquaredErrors(target, output);
    }

    @Override
    public NNdata call() throws Exception {
        double[] avgError = new double[1]; 
        avgError[0] = calculateAverageError(dataSet);
        return new NNdata(edges,edge ,avgError[0]);
    }
    
}
