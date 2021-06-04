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
        while(true){       
            Car car = new Car();

            long t = System.currentTimeMillis();
            long end = t+10000;
            while(System.currentTimeMillis() < end){
                String incomingString = car.recvProperties();
                // System.out.println(incomingString);
                String controlString = car.control(incomingString);
                // System.out.println(controlString);
                car.sendControls(controlString);
            }

            car.close();
        }
    }
}