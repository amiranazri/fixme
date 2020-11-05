package anazri.fixme;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import anazri.fixme.*;
import java.security.*;

public class client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);

        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());;
        printWriter.flush();

        socket.close();
    }
}