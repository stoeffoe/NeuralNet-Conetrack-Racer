package NeuralNet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import NeuralNet.ActivationFunction.*;

public class NeuralNet {
    public final static int cores  =  Runtime.getRuntime().availableProcessors(); 
    private static ExecutorService executor = Executors.newFixedThreadPool(cores);

    private transient static final Gson gson = new Gson();
    private transient static final String directory = "./assets/NNmodels/";
    public static final ActivationFunction activationFunction = new FastSigmoid();

    private double[][][] edges;

    /**
     * Initialize the neural net with already known weights of the edges
     * @param edges 3D array of the wieghts of all the edges per layer from input to output
     */
    public NeuralNet(double[][][] edges) {
        this.edges = edges;
    }

    /**
     * initializes neuralnet with given layer sizes from input to output with optional hidden layers
     * @param layers is a list with the amount of nodes in each layer
     */
    public NeuralNet(int[] layers) {
        edges = new double[layers.length - 1][][];
        double initEdgeWeight = 1;

        for (int i = 0; i < layers.length - 1; i++) {
            int input = layers[i];
            int output = layers[i + 1];

            edges[i] = new double[output][input];

            initEdgeWeights(i, initEdgeWeight);
        }
    }

    /**
     * Init each edge within a layer
     * @param layer The layer where the edges needs to be initialized
     * @param initEdgeWeight The initialized weight of the edge
     */
    private void initEdgeWeights(int layer, double initEdgeWeight ){
        for (int row = 0; row < edges[layer].length; row++) {
            for (int col = 0; col < edges[layer][row].length; col++) {
                edges[layer][row][col] = initEdgeWeight;
            }
        }
    }

    /**
     * @return Array of the weights of all the edges per layer from input to output
     */
    public double[][][] getEdges() {
        return edges;
    }

    /**
     * Trains the neuralnet with the given epochs
     */
    public double fit(Data[] dataSet, double weightChange, int epochs,int run) {
        double error = 0; 
        for (int epoch = 0; epoch < epochs; epoch++) {
            error =  train(dataSet, weightChange);

            System.out.printf("epoch: %d \t",(epoch+(run*epochs)));
            if(epoch%10==0){
                System.out.print("\n");
            }
        }
        return error;
    }

    /**
     * Change one edge with a given weight change of + / - that will return the lowest error 
     */
    private double train(Data[] dataSet,  double weightChange) {
        
        List<Future<NNdata>> futureList = new ArrayList<Future<NNdata>>();
        List<WorkThread> workThreadList = new ArrayList<WorkThread>();

        for (int layer = 0; layer < edges.length; layer++) {
            for (int row = 0; row < edges[layer].length; row++) {
                for (int col = 0; col < edges[layer][row].length; col++) {
                    int[] currentEdge = new int[] {layer,row,col};
                    workThreadList.add(new WorkThread(changeEdge(this.edges, currentEdge, weightChange),dataSet,activationFunction));
                    workThreadList.add(new WorkThread(changeEdge(this.edges, currentEdge, -weightChange),dataSet,activationFunction));
                }
            }
        }

        for (WorkThread workThread : workThreadList) {
            futureList.add(executor.submit(workThread));
        }
        
        ArrayList<NNdata> dataArrayList = new ArrayList<NNdata>();
        for (Future<NNdata> future : futureList) {
            try {
                dataArrayList.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        NNdata nnDataLowestError =  Collections.min(dataArrayList);

        this.edges =nnDataLowestError.nn;
        return nnDataLowestError.error;
    }

    /**
     * calculate the error of eache datapoint 
     * @param dataSet Data[]
     * @return returns the average error
     */
    public static double calculateAverageError(double [][][] edges,Data[] dataSet) {
        double errorSum = 0;
        for (Data data : dataSet) {
            errorSum += NeuralNet.calculateError(edges ,data);
        }

        return errorSum / dataSet.length;
    }

    /**
     * Calculate the sum of squared error of the vector 
     * @param data Data object with the inputvalues and corresponding outputvalue
     * @return The sum of squared errors 
     */
    public static double calculateError(double[][][] edges,Data data) {
        double[][] target = data.getOutputValues();
        double[][] output = NeuralNet.predict( edges,data.getInputValues());

        return MatMath.sumSquaredErrors(target, output);
    }

    private double[][][] changeEdge( double[][][] edges ,int[] edgeIndex,double weightChange) {   
        double[][][] newEdges = new double[edges.length][][];

        newEdges = MatMath.copyOf3Dim(edges);
        newEdges[edgeIndex[0]][edgeIndex[1]][edgeIndex[2]] += weightChange;
        
        return newEdges;
    } 

    /**
    * Passes the input values through the neural net
     * @param edges the weights of Neuralnet 
     * @param input double input vector 
     * @return what the computer thinks is right 
     */
    public static double[][] predict(double[][][] edges,double[][] input) {
        double[][] output = input;

        for (int layer = 0; layer < edges.length; layer++) {
            input = output;
            output = MatMath.multiplyAndActivate(edges[layer], input, activationFunction);
        }
        return output;
    }

    public double[][] predict(double[][] input) {
        return NeuralNet.predict(this.edges, input);
    }

    /**
     * Save the lists within this object to a json file
     * @param fileName The location and name of the json file where the data needs to be saved to
     */
    public void saveToJsonFile(String fileName ){
        try{
            Writer writer = new FileWriter(directory+fileName);
            gson.toJson(edges, writer);
            writer.close();
        } catch(JsonIOException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Load a NeuralNet object from a json file
     * @param fileName The location and name of the file where the json info needs to be loaded from
     * @return A NeuralNet object with the lists in it
     * @throws IOException
     */
    public static NeuralNet loadFromJsonFile(String fileName) throws IOException{
        Reader reader = Files.newBufferedReader(Paths.get(directory+fileName));
        return new NeuralNet(gson.fromJson(reader, double[][][].class)) ;
    }

    public void stopExecutor(){
        executor.shutdown();
    }
}
