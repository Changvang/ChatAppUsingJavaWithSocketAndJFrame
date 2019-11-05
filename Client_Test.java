package ChatAppUsingJava;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Client_Test {
	
	static int ServerPort = 10000;
	private static String IP ;
	
	
	// tao port trong khoang 40000 -50000
	private static int PORT = (int)(Math.random()*((50000-40000)+1))+40000;
	
	public static void main(String[] args) throws IOException{
		
		// Lay Ip Tu Internet tu dong
		Socket socket = new Socket();
	    socket.connect(new InetSocketAddress("google.com", 80));
	    String Ip = socket.getLocalAddress().toString();
	    socket.close();
	    
	    
	    IP = Ip.substring(1, Ip.length());
		
	    // chu y thay doi Ipsever va server port de co the ket noi
		Socket new_client = new Socket(IP, ServerPort);
		//ServerSocket ss = new ServerSocket(SERVER_PORT, 1, InetAddress.getByName("10.70.171.78"));
		ServerSocket new_server = new ServerSocket(PORT, 1, InetAddress.getByName(IP));
		
		DataInputStream dis = new DataInputStream(new_client.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(new_client.getOutputStream());
        
        System.out.println(IP + ":" + PORT);
        // Login checking
        new Login(new_server,new_client, dis, dos, IP + ":" + PORT);
        //dos.writeUTF(name + ":12345"+ ":" + IP + ":" + PORT);
        //new ChatClient(new_client,new_server,name);
        
        //System.out.println("Successful Login");
        
        
//        Scanner scn = new Scanner(System.in);
//        System.out.println("Enter 'logout' to exit ! ");
//        dos.writeUTF(scn.nextLine());
        try {
        	new_client.close();
        	new_server.close();
        }catch(IOException e) {
        	System.out.println(e);
        }
	}

}
