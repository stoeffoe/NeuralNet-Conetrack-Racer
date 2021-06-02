package CarSimulator;

import java.io.IOException;

public class SocketServerPython extends Thread{
    
    public SocketServerPython(){}

    @Override
    public void run() {
        try{
            String dir = System.getProperty("user.dir") + "\\src\\main\\python\\CarSimulator\\";
            String fileName = "lidar_socketpilot_world.py";
            String cmd = "python " + dir + fileName;

            Runtime.getRuntime().exec(cmd);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
