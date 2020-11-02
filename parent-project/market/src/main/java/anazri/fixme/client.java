package anazri.fixme;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import anazri.fixme.*;

public class client {
    public static void main(String[] args) throws IOException {
        String connectionID;
        Socket socket = new Socket("localhost", 5000);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        String clientID = uniqueID.generateID(6);
        printWriter.println(clientID);
        printWriter.flush();

        socket.close();

        this.connectionID = new uniqueID.generateID();
        System.out.println(connectionID);
    }
}