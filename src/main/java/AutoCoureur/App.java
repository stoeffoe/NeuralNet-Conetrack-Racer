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
        KeyboardController kc = new KeyboardController();
        while(true){

            car.sendControls(kc.getSteeringAngle(), kc.getTargetVelocity());
            
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

   
}