package AutoCoureur;

import java.util.Scanner;

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
        Client client = new Client();

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

        client.close();
    }
}