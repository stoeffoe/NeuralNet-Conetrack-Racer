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
            double targetVelocity = 0.9;
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
        // get dataset out of file
        CarData carData = CarData.loadFromJsonFile(dataSetFile);
        
        // convert dataset to format for neuralnet

        int end = carData.getPropertiesList().size()-1;
        Data[] dataSet = new Data[end];

        for(int indexDataset = 0; indexDataset < end; indexDataset++){
            Properties properties = carData.getFirstProperties();
            Controls control = carData.getFirstControls();
            
            dataSet[indexDataset] = new Data(
                properties.getRay(120,8), 
                new double[]{
                    control.getSteeringAngle()
                }
            );
        }

        // create neural net
        int[] layers = { 8,6,4,1 };
        NeuralNet nn = null;
        
        // get startvalues of edges if necessary
        try {
            nn = NeuralNet.loadFromJsonFile(edgesFile);
        } catch (Exception e) {
            System.out.println("No file to init edges");
            nn = new NeuralNet(layers);
        }
                
        // train
        nn.fit(dataSet, 0.1, 10000);

        // save
        if(edgesFile != null){
            nn.saveToJsonFile(edgesFile);
        }
        System.out.println(Arrays.deepToString(nn.getEdges()).replace("[", "{").replace("]", "}"));
    }

    /**
     * Control the car used a trained neural net to test its performance
     * @param edgesFile A file where the weights of the edges are saved
     */
    private static void test(String edgesFile){
        // initialize objects
        NeuralNet neuralNet = null;
        try {
            neuralNet = NeuralNet.loadFromJsonFile(edgesFile);
        } catch (IOException e) {
            System.out.println("No file to init edges");
        }
        Car car = new Car();

        // test main loop
        while (true) {
            car.recvProperties();
            Properties carData = car.getProperties();

            double[][] neuralNetInput = MatMath.fromList(carData.getRay(120, 8));

            double steeringAngle = neuralNet.predict(neuralNetInput)[0][0];
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
