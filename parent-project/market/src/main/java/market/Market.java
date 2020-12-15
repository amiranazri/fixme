package market;

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
import java.util.concurrent.*;

public class Market 
{
	String[] metals = {"Platinum", "Gold", "Silver", "Copper", "Iron", "Steel"};
    public static final int[] inventory = {30, 60, 90, 120,150, 180};
	public static Socket checkSocket;
	public static Scanner scanner;
	public static void main(String[] args) throws IOException 
	{ 
		try
		{ 
			scanner = new Scanner(System.in);
			Scanner scan = new Scanner(System.in); 
			InetAddress ip = InetAddress.getByName("localhost"); 
			checkSocket = new Socket(ip, 5000); 
		final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

    	Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                    	PrintWriter printWriter = new PrintWriter(checkSocket.getOutputStream());
                    	String userName = scanner.nextLine();
                    	printWriter.println(userName);
                    	printWriter.flush();
                    }
                } catch (IOException exception) {
                    System.err.println("Unable to process client request");
                    exception.printStackTrace();
                }
            }
        };

        Runnable serverTask1 = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                    	InputStreamReader in = new InputStreamReader(checkSocket.getInputStream());
                    	BufferedReader bf = new BufferedReader(in);
                        String str = bf.readLine();
                        System.out.println("Broker :"+str+"");
                        doTheMath(str);
                    }
                } catch (IOException exception) {
                    System.err.println("Unable to process client request");
                    exception.printStackTrace();
                }
            }
        };

        Thread serverThread = new Thread(serverTask);
    	serverThread.start();
    	Thread serverThread1 = new Thread(serverTask1);
    	serverThread1.start();
		}catch(Exception exception){ 
			exception.printStackTrace(); 
		} 
	}
    public static void doTheMath (String str)
    {
        int brokerOption = 0;
        int quantity = 0;
        int price = 0;
        int index = 0;
        String[] arr = str.split("\\|");

        if (arr.length > 13) {
            brokerOption = Integer.parseInt(arr[7].split("=")[1]);
            quantity = Integer.parseInt(arr[9].split("=")[1]);
            price = Integer.parseInt(arr[11].split("=")[1]);
            index = Integer.parseInt(arr[12].split("=")[1]);
        }
        if (brokerOption == 1 || !(index < inventory.length)) // the ! is to allow index out of bounds through this part 
                                                                //so it can be rejected without wasting code
        {
            if (index < inventory.length && quantity <= inventory[index]) //index out of bounds rejected here
            {
                inventory[index] -= quantity;
                try {

                PrintWriter printWriter = new PrintWriter(checkSocket.getOutputStream());
                String userName = "Type=Executed|"+str+"";
                printWriter.println(userName);
                printWriter.flush();
                } catch (IOException exception) {
                    System.err.println("Unable to process client request");
                    exception.printStackTrace();
                }
                System.out.println(inventory[index]);
            }
            else
            {
                try {
                    PrintWriter printWriter = new PrintWriter(checkSocket.getOutputStream());
                    String userName = "Type=Rejected|"+str+"";
                    printWriter.println(userName);
                    printWriter.flush();
                } catch (IOException exception) {
                    System.err.println("Unable to process client request");
                    exception.printStackTrace();
                }
            }
        }
        else if (brokerOption == 2)
        {
            inventory[index] += quantity;
            try {

                PrintWriter printWriter = new PrintWriter(checkSocket.getOutputStream());
                String userName = "Type=Executed|"+str+"";
                printWriter.println(userName);
                printWriter.flush();
                } catch (IOException exception) {
                    System.err.println("Unable to process client request");
                    exception.printStackTrace();
                }
            System.out.println(inventory[index]);
        }
    }
}
