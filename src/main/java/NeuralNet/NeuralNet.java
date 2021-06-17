package NeuralNet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private final ActivationFunction activationFunction = new FastSigmoid();

    private double[][][] edges;

    /**
     * if you have already the weights of the edges  
     * @param edges 
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
     * @return list of matrices containing the weights
     */
    public double[][][] getEdges() {
        return edges;
    }



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
        
        NNdata nnDataLowestError =  Collections.min(dataArrayList, new Comparator<NNdata>() {
            @Override
            public int compare(NNdata d1, NNdata d2) {
                return d1.error < d2.error ? -1 : (d1.error > d2.error) ? 1 : 0;
            }
        });

        this.edges =nnDataLowestError.nn;
        return nnDataLowestError.error;
    }

    private double[][][] changeEdge( double[][][] edges ,int[] edgeIndex,double weightChange) {
        edges[edgeIndex[0]][edgeIndex[1]][edgeIndex[2]] += weightChange;

        double[][][] newEdges = new double[edges.length][][];


        for (int layer = 0; layer < edges.length ;layer++) {
            for (int row = 0; row < edges[layer].length; row++) {
                newEdges[layer] = new double[edges[layer].length][edges[layer][0].length];
            }
        }

        newEdges = copyOf3Dim(edges, newEdges);

        edges[edgeIndex[0]][edgeIndex[1]][edgeIndex[2]] -= weightChange;
        return newEdges;
    } 

    private double[][][] copyOf3Dim(double[][][] array, double[][][]copy) {

        for (int x = 0; x < array.length; x++) {  
            for (int y = 0; y < array[x].length; y++) {  
                for (int z = 0; z < array[x][y].length; z++) {
                    copy[x][y][z] = array[x][y][z];  
                }  
            }  
        } 
        return copy;
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
     * @throws IOException
     */
    public static NeuralNet loadFromJsonFile(String fileName) throws IOException{
        Reader reader = Files.newBufferedReader(Paths.get("./jsonFiles/edges/"+fileName));
        return new NeuralNet(gson.fromJson(reader, double[][][].class)) ;
    }

    public void stopExecutor(){
        executor.shutdown();
    }
}
