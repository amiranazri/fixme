package router;

import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*;
import java.util.concurrent.*;

public class Router 
{
	public ServerSocket marketSocket; //for market server
    public Socket marketClientSocket;
    public ServerSocket brokerSocket; //for broker server
    public Socket brokerClientSocket;
    public PrintWriter printWriter;
    public int brokerid = 100000 + (int)(Math.random() * ((499999 - 100000) + 1));
    public int marketid = 500000 + (int)(Math.random() * ((999999 - 500000) + 1));
    public static void main( String[] args )
    {
        System.out.println( "Client and Server are now connected." );
        new Router().startServer();
    }
    public void startServer() {

        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    marketSocket = new ServerSocket(5000);
                    System.out.println("Broker is trying to connect to port 5000, please wait.");
                    while (true) {
                        marketClientSocket = marketSocket.accept();
                        clientProcessingPool.submit(new ClientTask(marketClientSocket , brokerClientSocket));

                    }
                } catch (IOException e) {
                    System.err.println("Error: client request denied.");
                    e.printStackTrace();
                }
            }
        };

        Runnable serverTask1 = new Runnable() {
            @Override
            public void run() {
                try {
                    brokerSocket = new ServerSocket(5001);
                    System.out.println("Market is trying to connect to port 5001, please wait.");
                    while (true) {
                        brokerClientSocket = brokerSocket.accept();
                        clientProcessingPool.submit(new ClientTask1(marketClientSocket , brokerClientSocket));

                    }
                } catch (IOException e) {
                    System.err.println("Error: client request denied.");
                    e.printStackTrace();
                }
            }
        };

        Thread serverThread = new Thread(serverTask);
        serverThread.start();
        Thread serverThread1 = new Thread(serverTask1);
        serverThread1.start();
    }

    private class ClientTask implements Runnable {

        private ClientTask(Socket test, Socket test2) {
        }

        @Override
        public void run() {
            System.out.println("Market connected.");
            idgen(marketid, marketClientSocket);
            try {
                while(true)
                {
                        InputStreamReader in = new InputStreamReader(marketClientSocket.getInputStream());
                        BufferedReader bf = new BufferedReader(in);
                        String str = bf.readLine();
                        System.out.println("Market["+marketid+"] :" + str);
                        printWriter = new PrintWriter(brokerClientSocket.getOutputStream());
                        printWriter.println(str);
                        printWriter.flush();
                 }
                } catch (IOException e) {
                    System.err.println("Error: client request denied.");
                    e.printStackTrace();
                }
            try {
                marketClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class ClientTask1 implements Runnable {

        private ClientTask1(Socket test, Socket test2) {
        }

        @Override
        public void run() {
            System.out.println("Broker connected");
            idgen(brokerid, brokerClientSocket);
            try {
                while(true)
                {
                        InputStreamReader in = new InputStreamReader(brokerClientSocket.getInputStream());
                        BufferedReader bf = new BufferedReader(in);
                        String str = bf.readLine();
                        System.out.println("Broker["+brokerid+"] :" + str);
                        printWriter = new PrintWriter(marketClientSocket.getOutputStream());
                        printWriter.println(str);
                        printWriter.flush();
                 }
                } catch (IOException e) {
                    System.err.println("Error: client request denied.");
                    e.printStackTrace();
                }
            try {
                brokerClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void idgen (int str, Socket test){
        try {
        printWriter = new PrintWriter(test.getOutputStream());
            printWriter.println("MarketID="+marketid+"|BrokerID="+brokerid+"");
            printWriter.flush();
            } catch (IOException e) {
                    System.err.println("Error: client request denied.");
                    e.printStackTrace();
                }
    }
}