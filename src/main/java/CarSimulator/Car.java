package CarSimulator;

import java.io.IOException;

import com.google.gson.Gson;

public class Car{
    private static final double finity = 1e20;
    private static int socketPortCounter = 50012;

    private Gson gson;

    private Process pythonWorld;
    private Client client;

    private Properties properties;
    private Controls controls;

    /**
     * Set up a client connection to be able to use the car
     */
    public Car(){
        gson = new Gson();
        properties = new Properties();
        controls = new Controls();

        try{
            pythonWorld = Runtime.getRuntime().exec("cmd /c start pythonServer.bat " + socketPortCounter);
            Thread.sleep(2000);
        } catch(Exception e){
            e.printStackTrace();
        }
        client = new Client(socketPortCounter);

        socketPortCounter++;
    }

    /**
     * 
     * @return
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Receive the properties of the car from the world and set them in the properties
     */
    public Properties recvProperties(){
        String incomingString = client.recv();
        System.out.println(incomingString);
        properties = gson.fromJson(incomingString, Properties.class);
        return properties;
    }

    /**
     * 
     * @param steeringAngle
     * @param targetVelocity
     */
    public void sendControls(double steeringAngle, double targetVelocity){
        controls = new Controls(steeringAngle, targetVelocity);
        client.send(gson.toJson(controls));
    }

    /**
     * Close the socketconnection and the server
     */
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
