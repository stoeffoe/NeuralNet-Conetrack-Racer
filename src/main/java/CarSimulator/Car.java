package CarSimulator;

import com.google.gson.Gson;

public class Car {
    private static final double finity = 1e20;
    private Client client;
    private Gson gson = new Gson();

    /**
     * Set up a client connection to be able to use the car
     */
    public Car(){
        client = new Client();

        while(true){
            client.send(control(client.recv()));
        }

        // client.close();
    }

    /**
     * Receive data from the server, process this and send data back to the server
     */
    public String control(String sensorString){
        // String sensorString = client.recv();
        // System.out.println (sensorString);
        CarParameters carEnvironmentelParameters = gson.fromJson(sensorString, CarParameters.class);
        double[] lidarDistances = carEnvironmentelParameters.lidarDistances;
        long lidarHalfApertureAngle = carEnvironmentelParameters.lidarHalfApertureAngle;
        long lidarApertureAngle = 2 * lidarHalfApertureAngle;

        // ====== BEGIN of control algorithm

        double nearestObstacleDistance = finity;
        double nearestObstacleAngle = 0.;
        
        double nextObstacleDistance = finity;
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

        CarParameters carControlParameters = new CarParameters();
        carControlParameters.steeringAngle = targetObstacleAngle;
        carControlParameters.targetVelocity = (90 - Math.abs (carControlParameters.steeringAngle)) / 60;

        // ====== END of control algorithm

        // client.send(gson.toJson(carParameters));
        return gson.toJson(carControlParameters);
    }
}
