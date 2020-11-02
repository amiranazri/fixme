package anazri.fixme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import anazri.fixme.*;

public class server {
    public static void main(String[] args) throws IOException{

        //step 1: connect to port 5000
        ServerSocket serverSocket = new ServerSocket(5000);
        Socket socket = serverSocket.accept();
    
        //Step 2: broker establishes connection
        System.out.println("\n>> User connected successfully.");
        serverSocket.close();

        InputStreamReader input = new InputStreamReader(socket.getInputStream());
        BufferedReader breader = new BufferedReader(input);

        String str = breader.readLine();
        System.out.println(">> Client: " + str + "\n");

        //Step 3: Router asigns new connection a unique 6 digit ID and communicates it to the Broker
        String ID = generateID(6);
        System.out.println(">> " + ID + ": " + "\n");
    }

    private static String generateID(int i) {
        return generateID(i);
    }
}