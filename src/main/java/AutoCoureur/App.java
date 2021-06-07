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
        int amountOfCars = 5;
        Car cars[] = new Car[amountOfCars];
        while(true){
            for(int i = 0; i < amountOfCars; i++){
                cars[i] = new Car();
            }

            long t = System.currentTimeMillis();
            long end = t+30000;
            while(System.currentTimeMillis() < end){
                for(int i = 0; i < amountOfCars; i++){
                    cars[i].sendControls(cars[i].control(cars[i].recvProperties()));
                }
                // String incomingString = car.recvProperties();
                // // System.out.println(incomingString);
                // String controlString = car.control(incomingString);
                // // System.out.println(controlString);
                // car.sendControls(controlString);
            }

            for(int i = 0; i < amountOfCars; i++){
                cars[i].close();
            }
        }
    }
}