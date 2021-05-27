package AutoCoureur;

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

        client.send("Hello World");
        System.out.println(client.recv());

        client.close();
    }
}