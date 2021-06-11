package AutoCoureur;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import CarSimulator.Car;
import CarSimulator.Properties;

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

    public static void main(String[] args) {
        Car car = new Car();
        KeyboardController kc = new KeyboardController();
        while (true) {
            Properties x = car.recvProperties();

            double a = kc.getSteeringAngle();
            double v = kc.getTargetVelocity();

            car.sendControls(a, v);


            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String text = "dataSet.add(new Data(new double[]" +  Arrays.toString(x.getLidarDistances()).replace("[", "{").replace("]", "}") + ",new double[] {" + a + "}" + "));"; // dataSet.add(new Data(c0, cross));//0;
            try {
                whenAppendToFileUsingFileWriter_thenCorrect("drive.txt", text);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void whenAppendToFileUsingFileWriter_thenCorrect(String fileName, String input) throws IOException {

        FileWriter fw = new FileWriter(fileName, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(input);
        bw.newLine();
        bw.close();

    }

}