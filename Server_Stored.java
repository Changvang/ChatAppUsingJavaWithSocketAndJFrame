package ChatAppUsingJava;

import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class Server_Stored {
	static int SERVER_PORT = 10000;
	
	// Vector to store active clients 
    static Vector<ClientHandler> ar = new Vector<>(); 
      
    // counter for clients 
    static int i = 0; 
    
    private static String[] Account_List = {"Test1:12345", "Test2:12345", "Test3:12345"};
    static ArrayList<String> Account_Online = new ArrayList<String>();
  
    public static void main(String[] args) throws IOException  
    { 
    	// Create Account
    	
    	// Lay Ip Tu Internet tu dong
    	Socket socket = new Socket();
    	socket.connect(new InetSocketAddress("google.com", 80));
    	String Ip = socket.getLocalAddress().toString();
    	socket.close();
    	
    	System.out.println(Ip);
    	
        // server is listening on port 1234 
        ServerSocket ss = new ServerSocket(SERVER_PORT, 1, InetAddress.getByName(Ip.substring(1, Ip.length())));
          
        Socket s; 
          
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            // Accept the incoming request 
            s = ss.accept(); 
  
            System.out.println("New client request received : " + s); 
              
            // obtain input and output streams 
            DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            
            System.out.println("Creating a new handler for this client..."); 
  
            // Create a new handler object for handling this request. 
            ClientHandler mtch = new ClientHandler(s, dis, dos,Account_List); 
  
            // Create a new Thread with this object. 
            Thread t = new Thread(mtch); 
              
            System.out.println("Adding this client to active client list"); 
  
            // add this client to active clients list 
            ar.add(mtch); 
  
            // start the thread. 
            t.start(); 
  
            // increment i for new client. 
            // i is used for naming only, and can be replaced 
            // by any naming scheme 
            i++; 
  
        } 
    } 
}