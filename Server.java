import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server
{
	private static int portNumber;
	private static BufferedWriter file;
	private static int count = 0;
	private static ArrayList<Double> allDurations = new ArrayList<Double>();
	private static int durationNumber = 0;
	private static Scanner scan = new Scanner(System.in);
	
	
	public static void main(String [] args) throws UnknownHostException
	{
		
		final int MILLI_SECOND_FACTOR 		= 1000000;
		final int SECOND_FACTOR				= 1000000000;
		
		byte byte1			=  1;				// 1 byte
		byte[] bytes64 		= new byte[64];		// 64 bytes
		byte[] bytes512 	= new byte[512];
		byte[] bytes256 	= new byte[256];
		byte[] bytes1024 	= new byte[1024]; 	//1024 bytes
		byte[] ack		 	= new byte[1];
		
		char[] bytes64b 		= new char[64];
		char[] bytes1024b 		= new char[1024]; 	//1024 bytes

		
		long duration		= 0;
		double durationMS	= 0;
		double durationSec	= 0;
		long timeSent		= 0;
		long timeReceived	= 0;
		
		String inputLine 	= null;
		
		byte[] kb1 			= new byte[1000];
		byte[] kb16			= new byte[16 * 1000];
		byte[] kb64			= new byte[64 * 1000];
		byte[] kb256 		= new byte[256 * 1000];
		byte[] mb1 			= new byte[1000000];
		
		double Mbps			= 0;
		
		System.out.println("\n---------------------------------- NETWORK DATA ----------------------------------");
		
		//Display basic network information
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println("Server IP Address\t: " + inetAddress.getHostAddress());
		System.out.println("Server name\t\t: " + inetAddress.getHostName());
		
		editHTMLFile(null);
		
		// -------------- TESTING:  LATENCY and THROUGHPUT ----------------------------- //
		// -------------- TCP ---------------------------------------------------------- //
				
		System.out.println("\n\n//-------------- TESTING:  LATENCY and THROUGHPUT -----------------------------//");
		System.out.println("//-------------- TCP ----------------------------------------------------------//");
		
		System.out.print("Port number on local machine\t\t: ");
		portNumber = scan.nextInt();
		
		try
		{
			ServerSocket serverSocket = new ServerSocket(portNumber);
			System.out.println("\nSocket created.");
			
			Socket clientSocket = serverSocket.accept();
			System.out.println("Client has connected.");
			
			PrintWriter 	outToClient 	= new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader 	inFromClient 	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			System.out.println("\n--------------------------- Measuring network latency (TCP) ---------------------------");
			
			System.out.println("\n---------------- Sending 1 byte ----------------\n");

			for(int count = 0; count < 10 ; count++)
			{
				//Send to client
				timeSent = System.nanoTime();
				outToClient.println(byte1);
				
				
				//Receive from client
				inputLine = inFromClient.readLine();
				timeReceived = System.nanoTime();
				
				System.out.println("Time sent was\t\t : " + timeSent);
				System.out.println("Time received was\t : " + timeReceived);
				
				
				duration = timeReceived - timeSent;
				durationMS = (double)duration / MILLI_SECOND_FACTOR;
				System.out.println("TCP duration\t\t : " + durationMS + "\n");
				collectData(durationMS);
			}
			
			System.out.println("\n--------------- Sending 64 bytes ---------------\n");
			for(int count = 0; count < 10 ; count++)
			{
				//Send to client
				timeSent = System.nanoTime();
				outToClient.println(bytes64b);
				
				
				//Receive from client
				inputLine = inFromClient.readLine();
				timeReceived = System.nanoTime();
				
				System.out.println("Time sent was\t\t : " + timeSent);
				System.out.println("Time received was\t : " + timeReceived);
				
				duration = timeReceived - timeSent;
				durationMS = (double)duration / MILLI_SECOND_FACTOR;
				System.out.println("TCP duration\t\t : " + durationMS + "\n");
				collectData(durationMS);
				
			}
			
			
			System.out.println("\n-------------- Sending 1024 bytes --------------\n");

			for(int count = 0; count < 10 ; count++)
			{
				//Send to client
				outToClient.println(bytes1024b);
				timeSent = System.nanoTime();
				
				//Receive from client
				inputLine = inFromClient.readLine();
				timeReceived = System.nanoTime();
				
				System.out.println("Time sent was\t\t : " + timeSent);
				System.out.println("Time received was\t : " + timeReceived);
				
				
				duration = timeReceived - timeSent;
				durationMS = (double)duration / MILLI_SECOND_FACTOR;
				System.out.println("TCP duration\t\t : " + durationMS + "\n");
				collectData(durationMS);	
			}
			
			
			System.out.println("\n--------------------------- Measuring network throughput (TCP) ---------------------------\n");
			
			System.out.println("------------------ Sending 1 KB ------------------");
			
			for(int count = 0; count < 10 ; count++)
			{
				timeSent = System.nanoTime();
				outToClient.println(kb1);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference: " + duration);
				
				duration = timeReceived - timeSent;
				durationSec = (double)duration / SECOND_FACTOR;
				
				Mbps = .001 / durationSec;
				System.out.println("Megabytes per second : " + Mbps + "\n");
				
				collectData(Mbps);

			}
			
			System.out.println("------------------ Sending 16 KB ------------------");
			
			for(int count = 0; count < 10 ; count++)	
			{
				timeSent = System.nanoTime();
				outToClient.println(kb16);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference\t\t: " + duration);

				duration = timeReceived - timeSent;
				durationSec = (double)duration / SECOND_FACTOR;
				
				Mbps = .016 / durationSec;
				System.out.println("Megabytes per second : " + Mbps + "\n");
				
				collectData(Mbps);
			}
			
			System.out.println("------------------ Sending 64 KB ------------------");
			
			for(int count = 0; count < 10 ; count++)
			{
				timeSent = System.nanoTime();
				outToClient.println(kb64);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference\t\t: " + duration);

				duration = timeReceived - timeSent;
				durationSec = (double)duration / SECOND_FACTOR;
				
				Mbps = .064 / durationSec;
				System.out.println("Megabytes per second : " + Mbps + "\n");
				
				collectData(Mbps);
			}
			
			System.out.println("------------------ Sending 256 KB ------------------");
			
			for(int count = 0; count < 10 ; count++)
			{
				timeSent = System.nanoTime();
				outToClient.println(kb256);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference\t\t: " + duration);
				duration = timeReceived - timeSent;
				durationSec = (double)duration / SECOND_FACTOR;
				
				Mbps = .256 / durationSec;
				System.out.println("Megabytes per second : " + Mbps + "\n");
				
				collectData(Mbps);
			}
			
			System.out.println("------------------ Sending 1 MB ------------------");
			
			for(int count = 0; count < 10 ; count++)
			{
				timeSent = System.nanoTime();
				outToClient.println(mb1);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference\t\t: " + duration);
				duration = timeReceived - timeSent;
				durationSec = (double)duration / SECOND_FACTOR;
				
				Mbps = 1 / durationSec;
				System.out.println("Megabytes per second : " + Mbps + "\n");
				
				collectData(Mbps);
				
			}
			
			outToClient.println();
			serverSocket.close();
			
		} catch(IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		

		// -------------- TESTING:  LATENCY -------------------------------------------- //
		// -------------- UDP ---------------------------------------------------------- // 
		
		System.out.println("\n\n// -------------- TESTING:  LATENCY -------------------------------------------- //");
		System.out.println("// -------------- UDP ---------------------------------------------------------- //");
		try {
			Scanner scan2 = new Scanner(System.in);
			
			System.out.print("Port number on local machine\t\t: ");
			portNumber = scan.nextInt();
			
			System.out.print("IP Address of client\t\t\t: ");
			String clientName = scan2.nextLine();
			
			System.out.print("Port number on client machine\t\t: ");
			int clientPortNumber = scan2.nextInt();
			
			System.out.println(clientName + "\t" + clientPortNumber + "\t" + portNumber);
			
			
			byte[] buffer = new byte[1024];
			DatagramSocket datagramSocket = new DatagramSocket(portNumber);
			System.out.println("Socket created.");
			
			DatagramPacket acknowledgePacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(clientName), clientPortNumber);
			DatagramPacket acknowledged = new DatagramPacket(buffer, buffer.length);
			
			datagramSocket.send(acknowledgePacket);
			System.out.println("Acknowledger sent");
			
			datagramSocket.receive(acknowledged);
			
			System.out.println("................................................HANDSHAKE COMPLETE");
			
			int 		clientPort 		= acknowledged.getPort();
			InetAddress clientAddress 	= acknowledged.getAddress();
			
			
			System.out.println("\n---------------- Sending 1 byte ----------------\n");
			
			byte [] byte1b = new byte[1];
			
			DatagramPacket byte1Packet = new DatagramPacket(byte1b, byte1b.length, clientAddress, clientPort);
			
			for(int count = 0; count < 10; count++)
			{
				timeSent = System.nanoTime();
				datagramSocket.send(byte1Packet);
					
				datagramSocket.receive(byte1Packet);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				durationMS = (double)duration / MILLI_SECOND_FACTOR;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference\t\t: " + duration);
				System.out.println(durationMS + "\t\tmilliseconds\n");
				
				collectData(durationMS);
			}
			
			System.out.println("\n---------------- Sending 64 bytes ----------------\n");
			
			DatagramPacket bytes64Packet = new DatagramPacket(bytes64, bytes64.length, clientAddress, clientPort);
			
			for(int count = 0; count < 10; count++)
			{
				timeSent = System.nanoTime();
				datagramSocket.send(bytes64Packet);
				
				datagramSocket.receive(bytes64Packet);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				durationMS = (double)duration / MILLI_SECOND_FACTOR;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference\t\t: " + duration);
				System.out.println(durationMS + "\t\tmilliseconds\n");
				
				collectData(durationMS);
			}
			
			System.out.println("\n--------------- Sending 1024 bytes ---------------\n");
			
			DatagramPacket bytes1024Packet = new DatagramPacket(bytes1024, bytes1024.length, clientAddress, clientPort);
			
			for(int count = 0; count < 10; count++)
			{
				timeSent = System.nanoTime();
				datagramSocket.send(bytes1024Packet);
				
				datagramSocket.receive(bytes1024Packet);
				timeReceived = System.nanoTime();
				
				duration = timeReceived - timeSent;
				durationMS = (double)duration / MILLI_SECOND_FACTOR;
				
				System.out.println("Time sent\t\t: " + timeSent);
				System.out.println("Time received\t\t: " + timeReceived);
				System.out.println("Difference\t\t: " + duration);
				System.out.println(durationMS + "\t\tmilliseconds\n");
				
				collectData(durationMS);
			}
			
			
			datagramSocket.close();
			
		} catch (SocketException e) {
			
			System.out.println(e);
			System.exit(1);
			
		} catch (IOException e) {
			
			System.out.println(e);
			System.exit(1);
		}
		
		
		// -------------- TESTING:  message size VS number of messages ----------------- //
		// -------------- TCP ---------------------------------------------------------- //
		
		System.out.println("\n\n// -------------- TESTING:  message size VS number of messages ----------------- //");
		System.out.println("// -------------- TCP ---------------------------------------------------------- //");
		System.out.print("Port number on local machine for TCP : ");
		portNumber = scan.nextInt();
		
		try
		{
			
			double [] times					= new double[3];
			ServerSocket 	serverSocket 	= new ServerSocket(portNumber);
			Socket 			clientSocket	= serverSocket.accept();
			
			PrintWriter 	outToClient 	= new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader 	inFromClient 	= new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		
			//Begin timer
			timeSent = System.nanoTime();
			
			//Exchange acknowledgment
			outToClient.println(ack);
			inFromClient.readLine();
			
			//Ping 1024 bytes to and back from Client
			for(int i = 0; i < 1024; i++)
			{
				outToClient.println(bytes1024);
				inFromClient.readLine();
			}
			
			//Stop timer
			timeReceived = System.nanoTime();
			
			duration = timeReceived - timeSent;
			durationMS = (double)duration / MILLI_SECOND_FACTOR;
			
			collectMessageData(durationMS);
			times[0] = durationMS;
			
			//Begin timer
			timeSent = System.nanoTime();
			
			//Exchange acknowledgement
			outToClient.println(ack);
			inFromClient.readLine();
			
			//Ping 512 bytes to and back from Client 2048 times
			for(int i = 0; i < 2048; i++)
			{
				outToClient.println(bytes512);
				inFromClient.readLine();
			}
			
			//Stop timer
			timeReceived = System.nanoTime();
			
			duration = timeReceived - timeSent;
			durationMS = (double)duration / MILLI_SECOND_FACTOR;
			
			collectMessageData(durationMS);
			times[1] = durationMS;
			
			//Begin timer
			timeSent = System.nanoTime();
			
			//Exchange acknowledgement
			outToClient.println(ack);
			inFromClient.readLine();
			
			//Ping 256 bytes to and back from Client 4096 times
			for(int i = 0; i < 4096; i++)
			{
				outToClient.println(bytes256);
				inFromClient.readLine();
				//System.out.println("Count\t\t:" + (i + 1));
			}
			
			//End timer
			timeReceived = System.nanoTime();
			
			duration = timeReceived - timeSent;
			durationMS = (double)duration / MILLI_SECOND_FACTOR;
			
			collectMessageData(durationMS);
			times[2] = durationMS;
			
			System.out.println("Time required to send 1024 - 1024 byte sized messages\t : " + times[0]);
			System.out.println("Time required to send 2048 - 512 byte sized messages\t : " + times[1]);
			System.out.println("Time required to send 4096 - 256 byte sized messages\t : " + times[2]);
		
			serverSocket.close();
			clientSocket.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		
		// -------------- TESTING:  message size VS number of messages ----------------- //
		// -------------- UDP ---------------------------------------------------------- //
		
		System.out.println("\n\n// -------------- TESTING:  message size VS number of messages ----------------- //");
		System.out.println("// -------------- UDP ---------------------------------------------------------- //");
		
		try
		{
			Scanner scan2 = new Scanner(System.in);
			
			double [] times = new double [3];
			
			System.out.print("Port number on local machine\t: ");
			portNumber = scan.nextInt();
			
			System.out.print("IP Address of client\t\t: ");
			String  clientName = scan2.nextLine();
			
			System.out.print("Port number on client\t\t: ");
			int clientPortNumber = scan.nextInt();
			
			System.out.println(clientName + "\t" + clientPortNumber + "\t" + portNumber);
			
			byte [] buffer = new byte [1024];
			
			DatagramSocket datagramSocket = new DatagramSocket(portNumber);
			DatagramPacket receiver = new DatagramPacket(buffer, buffer.length);
			DatagramPacket acknowledger = new DatagramPacket(ack, ack.length, InetAddress.getByName(clientName), clientPortNumber);
		
			datagramSocket.send(acknowledger);
			datagramSocket.receive(receiver);
			
			System.out.println("................................................. handshake COMPLETE");

			clientPortNumber = receiver.getPort();
			InetAddress clientAddress = receiver.getAddress();
			
			DatagramPacket bytes1024Packet = new DatagramPacket(bytes1024, bytes1024.length, clientAddress, clientPortNumber);
			DatagramPacket bytes512Packet = new DatagramPacket(bytes512, bytes512.length, clientAddress, clientPortNumber);
			DatagramPacket bytes256Packet = new DatagramPacket(bytes256, bytes256.length, clientAddress, clientPortNumber);
			
			System.out.println("Client port \t\t: " + clientPortNumber);
			System.out.println("Client address \t\t: " + clientAddress);
			
			//Begin timer
			timeSent = System.nanoTime();
			
			//Handshake
			datagramSocket.send(acknowledger);
			datagramSocket.receive(receiver);
			
			//Ping 1024 bytes to and back from Client
			for(int count = 0; count < 1024; count++)
			{
				datagramSocket.send(bytes1024Packet);
				datagramSocket.receive(receiver);
			}
			
			//End timer
			timeReceived = System.nanoTime();
			
			duration = timeReceived - timeSent;
			durationMS = (double)duration / MILLI_SECOND_FACTOR;
			
			collectMessageData(durationMS);
			times[0] = durationMS;
			
			//Begin timer
			timeSent = System.nanoTime();
			
			
			//Exchange acknowledgement
			datagramSocket.send(acknowledger);
			datagramSocket.receive(receiver);
			
			
			//Ping 512 byte sized messages to the client 2048 times
			for(int count = 0; count < 2048; count++)
			{
				datagramSocket.send(bytes512Packet);
				datagramSocket.receive(receiver);
			}
			
			//End timer
			timeReceived = System.nanoTime();
			
			duration = timeReceived - timeSent;
			durationMS = (double) duration / MILLI_SECOND_FACTOR;
			
			collectMessageData(durationMS);
			times[1] = durationMS;
			
			//Begin timer
			timeSent = System.nanoTime();
			
			//Exchange acknowledgement
			datagramSocket.send(acknowledger);
			datagramSocket.receive(receiver);
			
			//Ping 256 byte sized messages to the client 4096 times
			for(int count = 0; count < 4096; count++)
			{
				datagramSocket.send(bytes256Packet);
				datagramSocket.receive(receiver);
			}
			
			//End timer
			timeReceived = System.nanoTime();
			
			duration = timeReceived - timeSent;
			durationMS = (double)duration / MILLI_SECOND_FACTOR;
			
			collectMessageData(durationMS);
			times[2] = duration;
			
			datagramSocket.close();
			
			System.out.println("Time required to send 1024 - 1024 byte sized messages\t : " + times[0]);
			System.out.println("Time required to send 2048 - 512 byte sized messages\t : " + times[1]);
			System.out.println("Time required to send 4096 - 256 byte sized messages\t : " + times[2]);
			
		}
		
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		
		

		editHTMLFile(null);
		
	}
	
	private static void collectData(double duration)
	{	
		if(durationNumber < 10)
		{
			allDurations.add(duration);
			durationNumber++;
		}
		
		if(durationNumber == 10)
		{
			editHTMLFile(allDurations);
			
			allDurations.clear();
			
			durationNumber = 0;
			allDurations.add(duration);
		}
	}
	
	private static void collectMessageData(double duration)
	{	
		if(durationNumber < 3)
		{
			allDurations.add(duration);
			durationNumber++;
		}
		
		if(durationNumber == 3)
		{
			editHTMLFile(allDurations);
			
			allDurations.clear();
			
			durationNumber = 0;
			allDurations.add(duration);
		}
	}
	
	private static void editHTMLFile(ArrayList<Double> allDurations)
	{
		String fileContents = null;
		
		String preamble =  "<!DOCTYPE HTML>\r\n" + 
				"<html>\r\n" + 
				"<head>  \r\n" + 
				"<script>\r\n" + 
				"window.onload = function () {\r\n" + 
				"\r\n" + 
				"var chart = new CanvasJS.Chart(\"chartContainer\", {\r\n" + 
				"	animationEnabled: true,\r\n" + 
				"	title:{\r\n" + 
				"		text: \"Latency Performance (TCP)\"\r\n" + 
				"	},\r\n" + 
				"	axisX: {\r\n" + 
				"		title:\"Transmission Attempt\"\r\n" + 
				"	},\r\n" + 
				"	axisY:{\r\n" + 
				"		title: \"Round Trip Time (in ms)\"\r\n" + 
				"	},\r\n" + 
				"	data: [{\r\n" ;
		
		String post = "}\r\n" + 
				"</script>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"<script src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"></script>\r\n" + 
				"<div id=\"chartContainer\" style=\"height: 370px; width: 45%; display: inline-block\"></div>\r\n" + 
				"<div id=\"chartContainer2\" style=\"height: 370px; width: 45%; display: inline-block\"></div><br/><br/>\r\n" +
				"<div id=\"chartContainer3\" style=\"height: 370px; width: 45%; display: inline-block\"></div>\r\n" +
				"<div id=\"chartContainer4\" style=\"height: 370px; width: 45%; display: inline-block\"></div>\r\n" +
				"</body>\r\n" + 
				"</html>";
		
		try
		{
			if(count == 0)
			{ 
				file = new BufferedWriter(new FileWriter("results.html"));
				file.write(preamble);
				count++;
			}
			
			else if(count == 1)
			{
				fileContents = 	"		type: \"scatter\",\r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#4F81BC \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Round Trip Time:</b></span> {y} ms\",\r\n" + 
						"		name: \"1 byte\",\r\n" + 
						"		showInLegend: true,\r\n" + 
						"		dataPoints: [\r\n" + 
						"			{ x: 1, y: " + allDurations.get(0) + " },\r\n" + 
						"			{ x: 2, y: " + allDurations.get(1) + " },\r\n" + 
						"			{ x: 3, y: " + allDurations.get(2) + " },\r\n" + 
						"			{ x: 4, y: " + allDurations.get(3) + " },\r\n" + 
						"			{ x: 5, y: " + allDurations.get(4) + " },\r\n" + 
						"			{ x: 6, y: " + allDurations.get(5) + " },\r\n" + 
						"			{ x: 7, y: " + allDurations.get(6) + " },\r\n" + 
						"			{ x: 8, y: " + allDurations.get(7) + " },\r\n" + 
						"			{ x: 9, y: " + allDurations.get(8) + " },\r\n" + 
						"			{ x: 10, y: " + allDurations.get(9) + " }\r\n" + 
						"		]\r\n" + 
						"	}\r\n";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 2)
			{
				System.out.println("Claiming to be empty." + allDurations.get(10));
				fileContents = 	",{\r\n" + 
					"		type: \"scatter\",\r\n" + 
					"		name: \"64 bytes\",\r\n" + 
					"		showInLegend: true, \r\n" + 
					"		toolTipContent: \"<span style=\\\"color:#C0504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Round Trip Time:</b></span> {y} ms\",\r\n" + 
					"		dataPoints: [\r\n" + 
					"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
					"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
					"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
					"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
					"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
					"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
					"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
					"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
					"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
					"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
					"		]\r\n" + 
					"	}";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 3)
			{
				fileContents = 	",{\r\n" + 
						"		type: \"scatter\",\r\n" + 
						"		name: \"1024 bytes\",\r\n" + 
						"		showInLegend: true, \r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#C0504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Round Trip Time:</b></span> {y} ms\",\r\n" + 
						"		dataPoints: [\r\n" + 
						"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
						"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
						"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
						"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
						"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
						"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
						"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
						"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
						"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
						"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
						"		]\r\n" + 
						"	}" +
						"]\r\n" + 
						"});\r\n" + 
						"chart.render();\r\n";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 4)
			{
				fileContents = 	"var chart = new CanvasJS.Chart(\"chartContainer2\", {\r\n" + 
						"	animationEnabled: true,\r\n" + 
						"	title:{\r\n" + 
						"		text: \"Throughput Performance (TCP)\"\r\n" + 
						"	},\r\n" + 
						"	axisX: {\r\n" + 
						"		title:\"Transmission Attempt\"\r\n" + 
						"	},\r\n" + 
						"	axisY:{\r\n" + 
						"		title: \"Throughput (Mbps)\"\r\n" + 
						"	},\r\n" + 
						"	data: [{\r\n" +
						"		type: \"scatter\",\r\n" + 
						"		name: \"1 kilobyte\",\r\n" + 
						"		showInLegend: true, \r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#C0504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Throughput:</b></span> {y} bpms\",\r\n" + 
						"		dataPoints: [\r\n" + 
						"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
						"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
						"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
						"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
						"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
						"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
						"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
						"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
						"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
						"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
						"		]\r\n" + 
						"	}";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 5)
			{
				fileContents = ",{\r\n" +
					"		type: \"scatter\",\r\n" + 
					"		name: \"16 kilobytes\",\r\n" + 
					"		showInLegend: true, \r\n" + 
					"		toolTipContent: \"<span style=\\\"color:#BA504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Throughput:</b></span> {y} bpms\",\r\n" + 
					"		dataPoints: [\r\n" + 
					"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
					"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
					"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
					"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
					"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
					"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
					"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
					"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
					"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
					"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
					"		]\r\n" + 
					"	}";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 6)
			{
				fileContents = ",{\r\n" +
					"		type: \"scatter\",\r\n" + 
					"		name: \"64 kilobytes\",\r\n" + 
					"		showInLegend: true, \r\n" + 
					"		toolTipContent: \"<span style=\\\"color:#AA504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Throughput:</b></span> {y} bpms\",\r\n" + 
					"		dataPoints: [\r\n" + 
					"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
					"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
					"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
					"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
					"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
					"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
					"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
					"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
					"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
					"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
					"		]\r\n" + 
					"	}";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 7)
			{
				fileContents = ",{\r\n" +
					"		type: \"scatter\",\r\n" + 
					"		name: \"256 kilobytes\",\r\n" + 
					"		showInLegend: true, \r\n" + 
					"		toolTipContent: \"<span style=\\\"color:#DA504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Throughput:</b></span> {y} bpms\",\r\n" + 
					"		dataPoints: [\r\n" + 
					"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
					"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
					"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
					"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
					"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
					"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
					"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
					"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
					"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
					"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
					"		]\r\n" + 
					"	}";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 8)
			{
				fileContents = ",{\r\n" +
					"		type: \"scatter\",\r\n" + 
					"		name: \"1 megabyte\",\r\n" + 
					"		showInLegend: true, \r\n" + 
					"		toolTipContent: \"<span style=\\\"color:#FA504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Throughput:</b></span> {y} bpms\",\r\n" + 
					"		dataPoints: [\r\n" + 
					"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
					"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
					"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
					"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
					"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
					"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
					"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
					"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
					"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
					"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
					"		]\r\n" + 
					"	}]\r\n" +
					"});\r\n" +
					"chart.render();\r\n";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 9)
			{
				fileContents = 	"var chart = new CanvasJS.Chart(\"chartContainer3\", {\r\n" + 
						"	animationEnabled: true,\r\n" + 
						"	title:{\r\n" + 
						"		text: \"Latency Performance (UDP)\"\r\n" + 
						"	},\r\n" + 
						"	axisX: {\r\n" + 
						"		title:\"Transmission Attempt\"\r\n" + 
						"	},\r\n" + 
						"	axisY:{\r\n" + 
						"		title: \"Latency (ms)\"\r\n" + 
						"	},\r\n" + 
						"	data: [{\r\n" +
						"		type: \"scatter\",\r\n" + 
						"		name: \"1 byte\",\r\n" + 
						"		showInLegend: true, \r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#C0504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Latency:</b></span> {y} ms\",\r\n" + 
						"		dataPoints: [\r\n" + 
						"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
						"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
						"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
						"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
						"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
						"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
						"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
						"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
						"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
						"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
						"		]\r\n" + 
						"	}\r\n";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 10)
			{
				fileContents = ",{\r\n" +
						"		type: \"scatter\",\r\n" + 
						"		name: \"64 bytes\",\r\n" + 
						"		showInLegend: true, \r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#BA504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Latency:</b></span> {y} ms\",\r\n" + 
						"		dataPoints: [\r\n" + 
						"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
						"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
						"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
						"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
						"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
						"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
						"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
						"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
						"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
						"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
						"		]\r\n" + 
						"	}\r\n";
					
					file.append(fileContents);
					count++;
			}
			
			else if(count == 11)
			{
				fileContents = ",{\r\n" +
						"		type: \"scatter\",\r\n" + 
						"		name: \"1024 bytes\",\r\n" + 
						"		showInLegend: true, \r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#BA504E \\\"><b>{name}</b></span><br/><b> Transmission Attempt:</b> {x} <br/><b> Latency:</b></span> {y} ms\",\r\n" + 
						"		dataPoints: [\r\n" + 
						"			{ x: 1, y: " + allDurations.get(1) + " },\r\n" + 
						"			{ x: 2, y: " + allDurations.get(2) + " },\r\n" + 
						"			{ x: 3, y: " + allDurations.get(3) + " },\r\n" + 
						"			{ x: 4, y: " + allDurations.get(4) + " },\r\n" + 
						"			{ x: 5, y: " + allDurations.get(5) + " },\r\n" + 
						"			{ x: 6, y: " + allDurations.get(6) + " },\r\n" + 
						"			{ x: 7, y: " + allDurations.get(7) + " },\r\n" + 
						"			{ x: 8, y: " + allDurations.get(8) + " },\r\n" + 
						"			{ x: 9, y: " + allDurations.get(9) + " },\r\n" + 
						"			{ x: 10, y: " + allDurations.get(10) + " }\r\n" + 
						"		]\r\n" + 
						"	}]\r\n" +
						"});\r\n" +
						"chart.render();\r\n";
						
					
					file.append(fileContents);
					count++;
			}
			
			else if(count == 12)
			{
				fileContents = 	"var chart = new CanvasJS.Chart(\"chartContainer4\", {\r\n" + 
						"	animationEnabled: true,\r\n" + 
						"	title:{\r\n" + 
						"		text: \"Message Size and Number of Messages vs Time\"\r\n" + 
						"	},\r\n" + 
						"	axisX: {\r\n" + 
						"		title:\"Message Size\"\r\n" + 
						"	},\r\n" + 
						"	axisY:{\r\n" + 
						"		title: \"Time (ms)\"\r\n" + 
						"	},\r\n" + 
						"	data: [{\r\n" +
						"		type: \"scatter\",\r\n" + 
						"		name: \"TCP\",\r\n" + 
						"		showInLegend: true, \r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#C0504E \\\"><b>{name}</b></span><br/><b> Message Size:</b> {x} bytes <br/><b> Time:</b></span> {y} ms\",\r\n" + 
						"		dataPoints: [\r\n" + 
						"           { x: 256, y: " + allDurations.get(1) + " },\r\n" +  
						"			{ x: 512, y: " + allDurations.get(2) + " }, \r\n" + 
						"			{ x: 1024, y: " + allDurations.get(3) + " } \r\n" + 
						" 		] \r\n" +
						"     }\r\n";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 13)
			{
				fileContents = ",{\r\n" +
						"		type: \"scatter\",\r\n" + 
						"		name: \"UDP\",\r\n" + 
						"		showInLegend: true, \r\n" + 
						"		toolTipContent: \"<span style=\\\"color:#BA504E \\\"><b>{name}</b></span><br/><b> Message Size:</b> {x} bytes<br/><b> Time:</b></span> {y} ms\",\r\n" + 
						"		dataPoints: [\r\n" + 
						"			{ x: 256, y: " + allDurations.get(1) + " },\r\n" + 
						"			{ x: 512, y: " + allDurations.get(2) + " },\r\n" + 
						"			{ x: 1024, y: " + allDurations.get(3) + " }\r\n" + 
						"		]\r\n" + 
						"	}]\r\n" +
						"});\r\n" +
						"chart.render();\r\n";
				
				file.append(fileContents);
				count++;
			}
			
			else if(count == 14)
			{
				fileContents = post;
				file.append(post);
				file.close();
			}
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
}

