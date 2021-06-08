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
            long end = t+5000;
            while(System.currentTimeMillis() < end){
                    car.recvProperties();

                    double[] lidarDistances = car.getProperties().getLidarDistances();
                    long lidarHalfApertureAngle = car.getProperties().getLidarHalfApertureAngle();
                    long lidarApertureAngle = 2 * lidarHalfApertureAngle;
            
                    // ====== BEGIN of control algorithm
            
                    double nearestObstacleDistance = 1e20;
                    double nearestObstacleAngle = 0.;
                    
                    double nextObstacleDistance = 1e20;
                    double nextObstacleAngle = 0.;
            
                    for (long lidarAngle = -lidarHalfApertureAngle; lidarAngle < lidarHalfApertureAngle; lidarAngle++) {
                        long distanceIndex = lidarAngle < 0 ? lidarAngle + lidarApertureAngle : lidarAngle;
                        double lidarDistance = lidarDistances [(int) distanceIndex];
                        
                        if (lidarDistance < nearestObstacleDistance) {
                            nextObstacleDistance = nearestObstacleDistance;
                            nextObstacleAngle = nearestObstacleAngle;
                            
                            nearestObstacleDistance = lidarDistance;
                            nearestObstacleAngle = lidarAngle;
                        }
                        else if (lidarDistance < nextObstacleDistance) {
                            nextObstacleDistance = lidarDistance;
                            nextObstacleAngle = lidarAngle;
                        }
                    }
                    
                    double targetObstacleDistance = (nearestObstacleDistance + nextObstacleDistance) / 2;
                    double targetObstacleAngle = (nearestObstacleAngle + nextObstacleAngle) / 2;
            
                    double steeringAngle = targetObstacleAngle;
                    double targetVelocity = (90 - Math.abs (steeringAngle)) / 60;
            
                    // ====== END of control algorithm
            
                    car.sendControls(steeringAngle, targetVelocity);
            }

            car.close();
        }
    }
}