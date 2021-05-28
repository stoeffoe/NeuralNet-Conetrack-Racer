package CarSimulator;

import java.util.Scanner;

public class Car {
    Client client;
    /**
     * Set up a client connection to be able to use the car
     */
    public Car(){
        client = new Client();
    }

    /**
     * Close the socket connection
     */
    public void close(){
        client.close();
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
