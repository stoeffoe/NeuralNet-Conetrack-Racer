package CarSimulator;

import java.io.IOException;

import com.google.gson.Gson;

public class Car{
    private static final double finity = 1e20;
    private Client client;
    private Gson gson;
    public Properties properties;
    public Controls controls;
    private Process pythonWorld;

    /**
     * Set up a client connection to be able to use the car
     */
    public Car(){
        try{
            pythonWorld = Runtime.getRuntime().exec("cmd /c start pythonServer.bat");
            Thread.sleep(1000);
        } catch(Exception e){
            e.printStackTrace();
        }
        gson = new Gson();
        properties = new Properties();
        controls = new Controls();
        client = new Client();
    }

    /**
     * Process incoming properties and return controls
     */
    public String control(String sensorString){
        properties = gson.fromJson(sensorString, Properties.class);
        double[] lidarDistances = properties.getLidarDistances();
        long lidarHalfApertureAngle = properties.getLidarHalfApertureAngle();
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

        controls.setSteeringAngle(targetObstacleAngle);
        controls.setTargetVelocity((90 - Math.abs (controls.getSteeringAngle())) / 60);

        // ====== END of control algorithm

        return gson.toJson(controls);
    }

    /**
     * Receive the properties of the car from the world
     * @return String in json format with the properties
     */
    public String recvProperties(){
        return client.recv();
    }

    /**
     * Send the controls to the car in the world
     * @param controlString The controls in a string in json format
     */
    public void sendControls(String controlString){
        client.send(controlString);
    }

    public void close(){
        client.close();
        pythonWorld.descendants().forEach(s -> {
            try {
                Runtime.getRuntime().exec("taskkill /F /PID " + s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
