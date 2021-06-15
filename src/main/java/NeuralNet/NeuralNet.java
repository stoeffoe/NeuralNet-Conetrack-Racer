/*
Authors
- Stefan Beenen 0963586
- Ruben Hiemstra 0924010
- Jordy Weijgertse 0974347
*/
package NeuralNet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import NeuralNet.ActivationFunction.*;

public class NeuralNet {
    private transient static final Gson gson = new Gson();

    private double[][][] edges;
    private ActivationFunction activationFunction ;

    /**
     * @return list of matrices containing the weights
     */
    public double[][][] getEdges() {
        return edges;
    }

    /**
      * initializes neuralnet with given layer sizes from input to output with optional hidden layers
      * @params layers is a list with the amount of nodes in each layer
      */
      
    public NeuralNet(int[] layers) {
        this.activationFunction = new FastSigmoid();

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
     * if you have already the weights of the edges  
     * @param edges 
     */
    public NeuralNet(double[][][] edges) {
        this.activationFunction = new FastSigmoid();
        this.edges = edges;
    }

    private void initEdgeWeights(int layer, double initEdgeWeight ){
        for (int row = 0; row < edges[layer].length; row++) {
            for (int col = 0; col < edges[layer][row].length; col++) {
                edges[layer][row][col] = initEdgeWeight;
            }
        }
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
            output = MatMath.multiply(edges[layer], input);
            output = MatMath.map(output, (x) -> activationFunction.calculateActivation(x));
        }
        return output;
    }


    private void train(Data[] dataSet, double weightChange) {
        int[] bestEdgeIndex = new int[3];
        double bestEdgeWeightChange =0;
        double lowestAvgError = calculateAverageError(dataSet);

        for (int layer = 0; layer < edges.length; layer++) {
            for (int row = 0; row < edges[layer].length; row++) {
                for (int col = 0; col < edges[layer][row].length; col++) {

                        int[] currentIndex = {layer, row, col};

                        double avgErrorPlus = calculateErrorEdgeChange(dataSet, currentIndex, weightChange);
                        double avgErrorMinus = calculateErrorEdgeChange(dataSet, currentIndex, -weightChange);
                
                        if (lowestAvgError < avgErrorPlus && lowestAvgError < avgErrorMinus ){
                            continue;
                        }

                        if (avgErrorMinus < avgErrorPlus){
                            lowestAvgError = avgErrorMinus ;  
                            bestEdgeWeightChange = -weightChange;
                        } else {
                            lowestAvgError = avgErrorPlus;
                            bestEdgeWeightChange = weightChange;
                        }

                        bestEdgeIndex = currentIndex;
                    }
                }
            }
        edges[bestEdgeIndex[0]][bestEdgeIndex[1]][bestEdgeIndex[2]] += bestEdgeWeightChange;
    }

    /**
     *  calculate error of the vector 
     * @param data Data
     * @return error as double 
     */
    private double calculateError(Data data) {
        double[][] target = data.getDesiredValue();
        double[][] output = predict(data.getMatrix());

        return MatMath.sumSquaredErrors(target, output);
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
     * chance the edge and chance them back
     * @param dataSet Data[]
     * @param edgeIndex int[]
     * @param weightChange double
     * @return the avrrage error of the dataset with the edge chance  
     */
    private double calculateErrorEdgeChange(Data[] dataSet, int[] edgeIndex, double weightChange) {
        edges[edgeIndex[0]][edgeIndex[1]][edgeIndex[2]] += weightChange;
        double avgError = calculateAverageError(dataSet);
        edges[edgeIndex[0]][edgeIndex[1]][edgeIndex[2]] -= weightChange;
        
        return avgError;
    }

    public void fit(Data[] dataSet, double weightChange, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            train(dataSet, weightChange);

            System.out.print("\t epoch: "+epoch);
            if(epoch%10==0){
                System.out.print("\n");
            }
        }
        System.out.print("\n");

    }


    /**
     * Save the lists within this object to a json file
     * @param fileName The location and name of the json file where the data needs to be saved to
     */
    public void saveToJsonFile(String fileName ){
        try{
            Writer writer = new FileWriter("./jsonFiles/edges/"+fileName);
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
     */
    public static NeuralNet loadFromJsonFile(String fileName){
        try {
            Reader reader = Files.newBufferedReader(Paths.get("./jsonFiles/edges/"+fileName));
            return new NeuralNet(gson.fromJson(reader, double[][][].class)) ;
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
