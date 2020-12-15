package broker;

import java.io.*; 
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner; 
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.*;

public class Broker 
{
    public static String[] metals = {"Platinum", "Gold", "Silver", "Copper", "Iron", "Steel"};
	public static int[] inventory = {10, 10, 10, 10, 10, 10};
	public static int brokerOption;
	public static String material;
	public static int index;
	public static int biddingPrice;
	public static int quantity;
	public static int checksum;
	public static String assignid;
      public static String marketID;
      public static String brokerID;
	public static Socket socket;
	public static Scanner scanner;
	public static int bodyLength;
      public static String type;
      public static ZonedDateTime time;
	public static void main(String[] args) throws IOException 
	{ 
		time = ZonedDateTime.now(ZoneOffset.UTC);
		try
		{
			InetAddress ip = InetAddress.getByName("localhost"); 
			socket = new Socket(ip, 5001);
			InputStreamReader in = new InputStreamReader(socket.getInputStream());
			BufferedReader buff = new BufferedReader(in);
			scanner = new Scanner(System.in);
			Scanner scan = new Scanner(System.in); 
			assignid = buff.readLine();
                  String[] arr = assignid.split("\\|");
                  brokerID = arr[1].split("=")[1];
                  marketID = arr[0].split("=")[1];
			System.out.println("Broker ID: "+arr[1].split("=")[1]+"");
                  fixProtocol();
		}
		catch(Exception exception){ 
			exception.printStackTrace(); 
		} 

	}
      public static void brokerOption()
      {
            System.out.println("Please enter [1] for BUY, and [2] for SELL.");
            String notInt;
            while(true)
            {     try {
                        brokerOption = scanner.nextInt();
                        if (brokerOption == 1 || brokerOption == 2)
                              break;
                        else
                              System.out.println("BUY[1] OR SELL[2]");
                  }
                  catch(InputMismatchException e) {
                        notInt = scanner.next();
                        if (notInt.toLowerCase().equals("buy")) {
                              brokerOption = 1; break; }
                        else if (notInt.toLowerCase().equals("sell")) {
                              brokerOption = 2; break; }
                        else
                              System.out.println("BUY[1] OR SELL[2]");
                  }
            }
            System.out.println(brokerOption);
      }
      public static void material()
      {
            System.out.println("Select metal to purchase:\n");
            System.out.println("[0]Platinum\n" + "[1]Gold\n" + "[2]Silver\n" + "[3]Copper\n" + "[4]Iron\n" + "[5]Steel\n");            while(true)
            {
                  index = scanner.nextInt();
                  if (index <= 5 && index >= 0)
                        break;
                  else
                        System.out.println("Select metal to purchase:\n");
                        System.out.println("\n[0]Platinum\n" + "[1]Gold\n" + "[2]Silver\n" + "[3]Copper\n" + "[4]Iron\n" + "[5]Steel\n");
            }
            material = metals[index];
            System.out.println(material);
      }
      public static void Quantity()
      {
            if (brokerOption == 2)
                  System.out.println("Select Quantity To Sell [ Stocked: "+inventory[index]+"]");
            else
                  System.out.println("Select Quantity To Buy");
            while(true)
            {
                  try {
                        quantity = scanner.nextInt();
                        if (brokerOption == 2)
                        {
                              if (quantity >= 0 && quantity <= inventory[index])
                                    break;
                              else
                                    System.out.println("Select Quantity To Sell [ Stocked: "+ inventory[index] + "]");
                        }
                        else
                        {
                              if (quantity > 0 && quantity <= 10000)
                                    break;
                              else
                                    System.out.println("Please enter item quantity for purchase.");
                        }
                  } catch (InputMismatchException e) {
                        System.out.println("Please enter numbers only.");
                        scanner.next();
                  }
            }
      }
      public static void biddingPrice()
      {
            System.out.println("Select biddingPrice");
            while(true)
            {
                  biddingPrice = scanner.nextInt();
                  if (biddingPrice > 0 && biddingPrice <= 10000)
                        break;
                  else
                        System.out.println("Please enter bidding biddingPrice.");
            }
      }
      public static void Messaging()
      {
            String message = "35=D|49="+brokerID+"|56="+marketID+"|52="+time+"|55=D|54="+brokerOption+"|60=1|38="+quantity+"|40=1|44="+biddingPrice+"|64="+index+"|39=1";
            bodyLength = message.length();
            message = "8=FIX.4|9=" + bodyLength + "|" + message + "";
            int atoi;
            int checksum = 0;
            for (int i = 0; i < message.length(); i++)
            {
                  if (message.charAt(i) != '|')
                        atoi = (int)message.charAt(i);
                  else
                        atoi = 1;
                  checksum += atoi;
            }
            try
            {
                  message = "" + message + "|" + checksum + "";
                  System.out.println(message);
                  PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                  printWriter.println(message);
                  printWriter.flush();
            }catch (IOException exception)
            {
                  System.err.println("Unable to process client request");
                  exception.printStackTrace();
            }
      }

      public static void Receiving(String str)
      {
            String[] arr = str.split("\\|");
            if  (13 < arr.length)
            {
                        type = arr[0].split("=")[1];
                        brokerOption = Integer.parseInt(arr[8].split("=")[1]);
                        quantity = Integer.parseInt(arr[10].split("=")[1]);
                        biddingPrice = Integer.parseInt(arr[12].split("=")[1]);
                        index = Integer.parseInt(arr[13].split("=")[1]);
                  if (type.equals("Executed"))
                  {
                        System.out.println("Reflecting execution");
                        if (brokerOption == 1)
                              inventory[index] += quantity;
                        else if (brokerOption == 2)
                              inventory[index] -= quantity;
                  }
            }

      }
      public static void fixProtocol() throws IOException 
      { 
            try
            { 
                  final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);
                  Runnable check = new Runnable() {
                        @Override
                        public void run() {
                              try {
                                    while (true) {
                                          brokerOption();
                                          material();
                                          Quantity();
                                          biddingPrice();
                                          Messaging();
                                          InputStreamReader in = new InputStreamReader(socket.getInputStream());
                                          BufferedReader buff = new BufferedReader(in);
                                          String str = buff.readLine();
                                          System.out.println("Market["+marketID+"] :"+str+"");
                                          Receiving(str);
                                    }
                              } catch (IOException exception) {
                                    System.err.println("Unable to process client request");
                                   exception.printStackTrace();
                              }
                        }
                  };
      Thread surveyThread = new Thread(check);
      surveyThread.start();
            }catch(Exception exception){ 
                  exception.printStackTrace(); 
            } 
      }
}
