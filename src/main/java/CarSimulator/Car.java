package CarSimulator;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Car {
    private static final double finity = 1e20;
    private Client client;

    /**
     * Set up a client connection to be able to use the car
     */
    public Car(){
        client = new Client();

        while(true){
            try{
                control();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        // close();
    }

    public void control() throws ParseException, IOException{
        String sensorString = client.recv();
        // System.out.println (sensorString);

        JSONParser sensorParser = new JSONParser();
        JSONObject sensorObject = (JSONObject) sensorParser.parse(sensorString);
        JSONArray rawLidarDistances = (JSONArray) sensorObject.get("lidarDistances");
        double[] lidarDistances = new double [rawLidarDistances.size()];

        for (int distanceIndex = 0; distanceIndex < lidarDistances.length; distanceIndex++){
            lidarDistances [distanceIndex] = (double) rawLidarDistances.get(distanceIndex);
        }

        long lidarHalfApertureAngle = (long) sensorObject.get("lidarHalfApertureAngle");
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

        double steeringAngle = targetObstacleAngle;
        double targetVelocity = (90 - Math.abs (steeringAngle)) / 60;

        // ====== END of control algorithm

        client.send(toActuatorString(steeringAngle, targetVelocity));                
    }

    /**
     * Combine the steeringAngle and the targetVelocity to a JSON string
     * @param steeringAngle
     * @param targetVelocity
     * @return JSON string of the object
     * @throws IOException
     */
    public String toActuatorString(double steeringAngle, double targetVelocity) throws IOException{
        HashMap<String, Double> actuatorObject = new HashMap<String, Double>();
        actuatorObject.put("steeringAngle", steeringAngle);
        actuatorObject.put("targetVelocity", targetVelocity);

        StringWriter actuatorStringWriter = new StringWriter();
        (new JSONObject(actuatorObject)).writeJSONString(actuatorStringWriter);
        return actuatorStringWriter.toString();
    }
    
    /**
     * Close the socket connection
     */
    public void close(){
        client.close();
    }
}
