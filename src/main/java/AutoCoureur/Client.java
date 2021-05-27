package AutoCoureur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client{
    /**
     * The max length of the buffer which is used to send and receive to and from the server
     */
    private static final int maxMessageLength = 256;
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;

    /**
     * Set up a socket connection to the server
     */
    public Client(){
        try{
            socket = new Socket("", 50008);
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Send the message to the server
     * @param message The message which has to be sent with a maximum of {@value #maxMessageLength} bytes
     */
    public void send(String message){
        while(message.length() < maxMessageLength){
            message += " ";
        }
        try{
            byte[] sendBytes = message.getBytes("ASCII");
            outStream.write(sendBytes);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Wait until the client receives a message from the server
     * @return The message returned from the server
     */
    public String recv(){
        byte[] inBytes = new byte[maxMessageLength];
        String inString = "";
        try{
            inStream.readFully(inBytes, 0, maxMessageLength);
            inString = new String(inBytes, "ASCII");
        } catch(Exception e){
            e.printStackTrace();
        }
        return inString.trim();
    }

    /**
     * Close all connections to the server
     */
    public void close(){
        try{
            outStream.close();
            inStream.close();
            socket.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
