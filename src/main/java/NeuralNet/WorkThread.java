package NeuralNet;

import java.util.concurrent.Callable;

import NeuralNet.ActivationFunction.ActivationFunction;

class WorkThread implements Callable<NNdata> {

    private double[][][] edges;
    private Data[] dataSet;

    public WorkThread(double[][][] edges, Data[] dataSet, ActivationFunction activationFunction ) {
        this.edges = edges;
        this.dataSet = dataSet;
    }

    @Override
    public NNdata call() throws Exception {
        return new NNdata(edges ,NeuralNet.calculateAverageError(this.edges ,dataSet));
    }
    
}
