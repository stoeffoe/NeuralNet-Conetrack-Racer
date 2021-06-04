package AutoCoureur;

import CarSimulator.Car;

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

        while(true){
            String incomingString = car.recvProperties();
            // System.out.println(incomingString);
            String controlString = car.control(incomingString);
            // System.out.println(controlString);
            car.sendControls(controlString);
        }

        // car.close();
    }
}