package AutoCoureur;

import java.io.IOException;
import java.util.Arrays;

import CarSimulator.Car;
import CarSimulator.Controls;
import CarSimulator.Properties;
import CarSimulator.CarData;
import NeuralNet.Data;
import NeuralNet.MatMath;
import NeuralNet.NeuralNet;

/**
 * Requirements:
 * 
 * 
 * Design:
 * 
 * 
 * Testing:
 * 
 * 
 */
public class App {
    private static int amountOfRays = 4;
    private static double minDistance = 0.5;
    private static double maxDistance = 5;
    private static double maxSteeringAngle = 45;
    private static int[] layers = {amountOfRays, 6, 4, 1};


    public static void main(String[] args){
        switch (args.length) {
            case 2:
                switch (args[0]) {
                    case "data":
                        getData(args[1]);
                        break;
                    
                    case "train":
                        train(args[1], null);
                        break;
                    
                    case "test":
                        test(args[1]);
                        break;

                    default:
                        System.out.println("Invalid argument specified!");
                        displayOptions();
                        break;
                }
                break;
        
            case 3:
                if(args[0].equals("train")){
                    train(args[1], args[2]);
                    break;
                }
        
            default:
                System.out.println("Invalid number of arguments specified!");
                displayOptions();
                break;
        }
    }

    /**
     * Get car data from the car by controlling it and saving all the input(properties)-output(controls)
     * @param dataSetFile The file where the car data needs to be saved
     */
    private static void getData(String dataSetFile){
        Car car = new Car();
        CarData carData = new CarData();
        UserInputControls uic = UserInputControls.getInstance();

        System.out.println("Use mouse to steer, press R to start/stop recording and press ESC to save the cardata and exit the program");

        boolean record = false;
        boolean previousRecordStatus = record;
        while(!uic.getQuitingStatus()){
            record = uic.getRecordStatus();
            if(!previousRecordStatus && record){
                System.out.println("Started recording");
            } else if(previousRecordStatus && !record){
                System.out.println("Stopped recording");
            }
            Properties properties = car.recvProperties();
            if(record){
                carData.addProperties(properties);
            }

            double steeringAngle = uic.getSteeringAngle();
            double targetVelocity = uic.getTargetVelocity();
            Controls controls = car.sendControls(steeringAngle, targetVelocity);
            if(record){
                carData.addControls(controls);
            }
            previousRecordStatus = record;
        }

        if(carData.getPropertiesList().isEmpty()){
            System.out.println("No data to save");
        } else{
            System.out.println("Saving data to file");
            carData.saveToJsonFile(dataSetFile);
        }

        car.close();
        System.exit(0);
    }

    /**
     * Train the neural net
     * @param dataSetFile A file with a raw dataset from the car
     * @param edgesFile A file where the weights of the edges are saved
     */
    private static void train(String dataSetFile, String edgesFile){
        CarData carData = CarData.loadFromJsonFile(dataSetFile);
        int datasetSize = carData.getPropertiesList().size()-1;
        Data[] dataSet = new Data[datasetSize];
        for(int indexDataset = 0; indexDataset < datasetSize; indexDataset++){
            Properties properties = carData.getFirstProperties();
            Controls controls = carData.getFirstControls();
            dataSet[indexDataset] = new Data(
                MatMath.normalize(properties.getRay(amountOfRays), minDistance, maxDistance), 
                new double[]{
                    MatMath.normalize(controls.getSteeringAngle(), -maxSteeringAngle, maxSteeringAngle)
                }
            );
        }

        NeuralNet nn = null;
        try {
            nn = NeuralNet.loadFromJsonFile(edgesFile);
        } catch (Exception e) {
            System.out.println("No file to init edges");
            nn = new NeuralNet(layers);
        }

        UserInputControls uic = UserInputControls.getInstance();
        System.out.println("NeuralNet.cores "+ NeuralNet.cores);
        System.out.println("press ESC to save the NN and exit the program");
        
        int count = 0;
        double weights = 1;
        double oldLowestError =0;
        while(!uic.getQuitingStatus()){             
            double LowestError = nn.fit(dataSet, weights, 100,count);
            double errorDiff = oldLowestError - LowestError;
            System.out.println("errorDiff "+  errorDiff );

            if(errorDiff ==0){
                weights/=10;
            }

            oldLowestError =  LowestError;
            count++;
        }

        nn.stopExecutor();

        if(edgesFile != null){
            nn.saveToJsonFile(edgesFile);
        }
        System.out.println(Arrays.deepToString(nn.getEdges()).replace("[", "{").replace("]", "}"));
        System.exit(0);
    }

    /**
     * Control the car used a trained neural net to test its performance
     * @param edgesFile A file where the weights of the edges are saved
     */
    private static void test(String edgesFile){
        NeuralNet neuralNet = null;
        try {
            neuralNet = NeuralNet.loadFromJsonFile(edgesFile);
        } catch (IOException e) {
            System.out.println("No file to init edges");
            System.exit(1);
        }
        Car car = new Car();

        while (true) {
            Properties properties = car.recvProperties();

            double[][] neuralNetInput = MatMath.fromList(MatMath.normalize(properties.getRay(amountOfRays), minDistance, maxDistance));

            double steeringAngle = MatMath.denormalize(neuralNet.predict(neuralNetInput)[0][0], -maxSteeringAngle, maxSteeringAngle);
            double targetVelocity = 0.9;    // default velocity, to be replaced by the neuralnet

            car.sendControls(steeringAngle, targetVelocity);
        }
    }


    /**
     * Display to possible arguments to run the app with
     */
    private static void displayOptions(){
        System.out.println("Possible options:");
        System.out.println("- data <dataset.json>");
        System.out.println("\t To gather data and save it to a file specified at <dataset.json>");
        System.out.println("- train <dataset.json>");
        System.out.println("\t To train a new neural net with a dataset specified at <dataset.json>");
        System.out.println("- train <dataset.json> <edges.json>");
        System.out.println("\t To train an existing neural net with a dataset specified at <dataset.json> and the edges specified at <edges.json>");
        System.out.println("- test <edges.json>");
        System.out.println("\t To test the neural net with edges specified at <edges.json>");
    }
}
