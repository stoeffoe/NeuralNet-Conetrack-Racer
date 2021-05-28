package CarSimulator;

import java.io.IOException;
import java.util.Scanner;

public class Car {
    Client client;
    private Process python;

    /**
     * Set up a client connection to be able to use the car
     */
    public Car(){
        try{
            python = runPython("server.py");
            Thread.sleep(500);
        } catch(Exception e){
            e.printStackTrace();
        }
        client = new Client();
    }












    
    /**
     * Close the socket connection
     */
    public void close(){
        client.close();
        python.destroy();
    }

    /**
     * Run a python script
     * @param fileName the location of the python script relative to the current location
     * @return the process
     * @throws IOException
     */
    private Process runPython(String fileName) throws IOException{
        String dir = System.getProperty("user.dir") + "\\src\\main\\python\\CarSimulator\\";
        String cmd = "python " + dir + fileName;

        return Runtime.getRuntime().exec(cmd);
    }

    /**
     * Test function to test the connectivity with the socket server
     */
    public void test(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press q to quit or any other sentence to test the echo server:");
        String input;
        boolean quit = false;
        do{
            input = scanner.nextLine();
            switch (input) {
                case "q":
                    quit = true;
                    break;
                default:
                    client.send(input);
                    System.out.println(client.recv());
                    break;
            }
        } while (!quit);
        scanner.close();
    }
}
