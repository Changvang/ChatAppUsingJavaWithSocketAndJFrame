package ChatAppUsingJava;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

class ClientHandler implements Runnable  
{ 
    Scanner scn = new Scanner(System.in); 
    private String[] Account_List;
    private String[] User;
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin; 
      
    // constructor 
    public ClientHandler(Socket s,
                            DataInputStream dis, DataOutputStream dos, String[] Account_List) { 
        this.dis = dis; 
        this.dos = dos; 
        //this.name = name; 
        this.Account_List = Account_List;
        this.s = s; 
        this.isloggedin=false; 
    } 
  
    @Override
    public void run() { 
  
        String received = ""; 
        while (true)  
        { 
            try
            { 
            	//Using when you want get a another
            	//ObjectOutputStream objectOutput = new ObjectOutputStream(s.getOutputStream());
            	
                // try close socket when it close
            	if(dis == null) {
            		// set isloggedin = false de khong hien thi lai sau khi offline
            		this.isloggedin = false;
            		
            		Server_Stored.Account_Online.remove(received);
            		
            		// Gui lai thong tin cho tat ca cac client
            		for(ClientHandler mc: Server_Stored.ar) {
            			// If Peer is online
            			if(mc.isloggedin == true) {
            				System.out.println(Server_Stored.Account_Online.toString());
            				mc.dos.writeUTF(Server_Stored.Account_Online.toString());
            			}
            		}
            		
            		s.close();
            	}
            	// receive the string 
                received = dis.readUTF(); 
                User = received.split(":");
                  
                for(String i : Account_List) {
                	//System.out.println(i);
                	String[] check = i.split(":");
                	if(check[0].equals(User[0]) && check[1].equals(User[1])) {
                		
                		System.out.println("Can connect with user : " + User[0]);
                		isloggedin = true;
                	}
                }
                
                //System.out.println("Check_point");
                
                if(isloggedin) {
                	dos.writeUTF(User[0]);
                	
                	System.out.println(received);
                	
                	// Add new account already login to Account_Login
            		
            		Server_Stored.Account_Online.add(received);
            		
            		// Gui lai thong tin cho tat ca cac client
            		for(ClientHandler mc: Server_Stored.ar) {
            			// If Peer is online
            			if(mc.isloggedin == true) {
            				System.out.println(Server_Stored.Account_Online.toString());
            				mc.dos.writeUTF(Server_Stored.Account_Online.toString());
            			}
            		}
                }
                else {
                	
                	dos.writeUTF("Not found");
                }
            } catch (IOException e) { 
                try {
                	// catch when socket is closed 
                	// try close socket when it close
                	
                	// set isloggedin = false de khong hien thi lai sau khi offline
                	this.isloggedin = false;
               		
               		Server_Stored.Account_Online.remove(received);
               		
               		// Gui lai thong tin cho tat ca cac client
               		for(ClientHandler mc: Server_Stored.ar) {
               			// If Peer is online
               			if(mc.isloggedin == true) {
               				System.out.println(Server_Stored.Account_Online.toString());
               				mc.dos.writeUTF(Server_Stored.Account_Online.toString());
               			}
               		}
               		s.close();
               		break;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                e.printStackTrace(); 
            } 
              
        } 
    } 
} 
                	
                  
