import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client 
{
	
	public static void main(String[] args) throws IOException
	{
		int portNumber = 0;
		String hostName = null;
		
		Scanner scan = new Scanner(System.in);
		
		byte [] byte1		= new byte[1];		// 1 byte
		byte [] bytes64 	= new byte[32];		// 64 bytes
		byte [] bytes512 	= new byte[512];
		byte [] bytes256 	= new byte[256];
		byte [] bytes1024 	= new byte[1024];
		byte [] ack		 	= new byte[1];
		
		System.out.println("Host name: " + hostName);
		System.out.println("Port number: " + portNumber);
		
		
		// -------------- TESTING:  LATENCY and THROUGHPUT ----------------------------- //
		// -------------- TCP ---------------------------------------------------------- //
		
		System.out.println("\n\n//-------------- TESTING:  LATENCY and THROUGHPUT -----------------------------//");
		System.out.println("//-------------- TCP ----------------------------------------------------------//");
		
		System.out.print("IP Address of server\t\t: ");
		hostName = scan.nextLine();
		
		System.out.print("Port number on server\t\t: ");
		portNumber = scan.nextInt();
		
		//192.168.0.2
		
		try
		{
			Socket socket = new Socket(hostName, portNumber);
			System.out.println("Server connected.");
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			String fromServer;
			fromServer = in.readLine();
			System.out.println("first size: " + fromServer.length());
			
			while(fromServer.compareTo("")  != 0)
			{
				//Receive from server
	
				if(fromServer.length() == 1)
					out.println(byte1);
				
				if(fromServer.length() == 64)
					out.println(bytes64);
				
				if(fromServer.length() == 1024)				
					out.println(bytes1024);

				
				fromServer = in.readLine();
			}
			
			socket.close();
			System.out.println("SOCKET CLOSED");
			
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		
		
		
		// -------------- TESTING:  LATENCY  ------------------------------------------- //
		// -------------- UDP ---------------------------------------------------------- //
		
		Scanner scan2 = new Scanner(System.in);
		
		System.out.println("\n\n//-------------- TESTING:  LATENCY  -------------------------------------------//");
		System.out.println("//-------------- UDP ----------------------------------------------------------//");
		
		System.out.print("Port number on local machine\t: ");
		portNumber = scan.nextInt();
		
		System.out.print("IP Address of server\t\t: ");
		hostName = scan2.nextLine();
		
		System.out.print("Port number on server\t\t: ");
		int serverPortNumber = scan2.nextInt();
		
		System.out.println(hostName + "\t" + serverPortNumber + "\t" + portNumber);
		
		try
		{
			byte [] buffer = new byte [1024];
			
			DatagramSocket datagramSocket = new DatagramSocket(portNumber);
			System.out.println("Socket created");
			DatagramPacket acknowledgePacket = new DatagramPacket(buffer, buffer.length);
			DatagramPacket acknowledger = new DatagramPacket(byte1, byte1.length, InetAddress.getByName(hostName), serverPortNumber);
			DatagramPacket receiver = new DatagramPacket(buffer, buffer.length);
			
			System.out.println("READY TO RECEIVE");
			
			datagramSocket.receive(acknowledgePacket);
			System.out.println("Acknowledger received");
			
			int 		serverPort 		= acknowledgePacket.getPort();
			InetAddress serverAddress 	= acknowledgePacket.getAddress();
			
			System.out.println("Server port \t\t: " + serverPort);
			System.out.println("Server address \t\t: " + serverAddress);
			
			datagramSocket.send(acknowledger);
			
			//Begin receiving 1 byte packets for throughput analysis
			
			System.out.println("Receiving 1 byte packets....");
			
			DatagramPacket byte1Packet = new DatagramPacket(byte1, byte1.length, serverAddress, serverPort); 
			
			for(int count = 0; count < 10; count++)
			{
				datagramSocket.receive(receiver);
				datagramSocket.send(byte1Packet);
			}
			
			//Begin receiving 64 byte packets for throughput analysis
			
			System.out.println("Receiving 64 byte packets....");
			
			DatagramPacket bytes64Packet = new DatagramPacket(bytes64, bytes64.length, serverAddress, serverPort); 
			
			for(int count = 0; count < 10; count++)
			{
				datagramSocket.receive(receiver);
				datagramSocket.send(byte1Packet);
			}
			
			//Begin receiving 1024 byte packets for throughput analysis
			System.out.println("Receiving 1024 byte packets....");
			
			DatagramPacket bytes1024Packet = new DatagramPacket(bytes1024, bytes1024.length, serverAddress, serverPort); 
			
			for(int count = 0; count < 10; count++)
			{
				datagramSocket.receive(receiver);
				datagramSocket.send(byte1Packet);
			}
			
			datagramSocket.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		
		// -------------- TESTING:  message size VS number of messages ----------------- //
		// -------------- TCP ---------------------------------------------------------- //	
		
		System.out.println("\n\n//-------------- TESTING:  message size VS number of messages -----------------//");
		System.out.println("//-------------- TCP ----------------------------------------------------------//");
		
		scan = new Scanner(System.in);
		
		System.out.print("IP Address of server\t\t: ");
		hostName = scan.nextLine();
		
		System.out.print("Port number on server\t\t: ");
		portNumber = scan.nextInt();
		
		try
		{
			Socket socket = new Socket(hostName, portNumber);
			System.out.println("Server connected.");
			
			PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
			//Exchange acknowledgement
			inFromServer.readLine();
			outToServer.println(ack);
			
			//Send 1024 byte sized messages 1024 times to server
			for(int i = 0; i < 1024; i++)
			{
				inFromServer.readLine();
				outToServer.println(bytes1024);
			}	
			
			//Exchange acknowledgement
			inFromServer.readLine();
			outToServer.println(ack);
			
			//Send 512 byte sized messages 2048 times to server
			for(int i = 0; i < 2048; i++)
			{
				inFromServer.readLine();
				outToServer.println(bytes512);
			}
			
			//Exchange acknowledgement
			inFromServer.readLine();
			outToServer.println(ack);
			
			//Send 256 byte sized messages 4096 times to server
			for(int i = 0; i < 4096; i++)
			{
				inFromServer.readLine();
				outToServer.println(bytes256);
			}
			
			socket.close();
			
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		
		// -------------- TESTING:  message size VS number of messages ----------------- //
		// -------------- UDP ---------------------------------------------------------- //
		
		System.out.println("\n\n//-------------- TESTING:  message size VS number of messages -----------------//");
		System.out.println("//-------------- UDP ----------------------------------------------------------//");

		try
		{
			
			byte [] buffer = new byte[1024];
			
			scan = new Scanner(System.in);
			scan2 = new Scanner(System.in);
			
			System.out.print("Port number on local machine\t: ");
			portNumber = scan.nextInt();
			
			System.out.print("IP Address of server\t\t: ");
			hostName = scan2.nextLine();
			
			System.out.print("Port number on server\t\t: ");
			serverPortNumber = scan2.nextInt();
			
			System.out.println(hostName + "\t" + serverPortNumber + "\t" + portNumber);
			
			DatagramSocket datagramSocket = new DatagramSocket(portNumber);
			DatagramPacket acknowledger = new DatagramPacket(byte1, byte1.length, InetAddress.getByName(hostName), serverPortNumber);
			DatagramPacket receiver = new DatagramPacket(buffer, buffer.length);
			
			datagramSocket.receive(receiver);
			datagramSocket.send(acknowledger);
			
			serverPortNumber = receiver.getPort();
			InetAddress serverAddress = receiver.getAddress();
			
			DatagramPacket bytes1024Packet = new DatagramPacket(bytes1024, bytes1024.length, serverAddress, serverPortNumber);
			DatagramPacket bytes512Packet = new DatagramPacket(bytes512, bytes512.length, serverAddress, serverPortNumber);
			DatagramPacket bytes256Packet = new DatagramPacket(bytes256, bytes256.length, serverAddress, serverPortNumber);

			System.out.println("Server port \t\t: " + serverPortNumber);
			System.out.println("Server address \t\t: " + serverAddress);
			
			datagramSocket.receive(receiver);
			datagramSocket.send(acknowledger);
			
			for(int count = 0; count < 1024; count++)
			{
				datagramSocket.receive(receiver);
				datagramSocket.send(bytes1024Packet);
			}
			
			datagramSocket.receive(receiver);
			datagramSocket.send(acknowledger);
			
			for(int count = 0; count < 2048; count++)
			{
				datagramSocket.receive(receiver);
				datagramSocket.send(bytes512Packet);
			}
			
			datagramSocket.receive(receiver);
			datagramSocket.send(acknowledger);
			
			for(int count = 0; count < 4096; count++)
			{
				datagramSocket.receive(receiver);
				datagramSocket.send(bytes256Packet);
			}
			
			datagramSocket.close();
			
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(1);
		}
		System.out.println("Sockets closed!");
	}
}
