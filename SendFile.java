package ChatAppUsingJava;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
 
public class SendFile implements Runnable{
	
	public static Socket sock;
	public static File file;
 
	public SendFile(Socket sock, File file) {
		this.sock = sock;
		this.file = file;
	};
	
	
	
//  public final static int SOCKET_PORT = 13267;  // you may change this
//  public final static String FILE_TO_SEND = "D:\\School\\HK191\\ComputerNetwork\\ASS\\BTL_1.doc";  // you may change this
 
	@Override
  public void run() {
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    
    Socket sock = null;
    try {
      
    	// CHi gui nhan 1 lan duy nhat cho moi Thread
        System.out.println("Waiting...");
        try {
          sock = this.sock;
          System.out.println("Send Socket : " + sock);
          // send file
          File myFile = this.file;
          byte [] mybytearray  = new byte [(int)myFile.length()];
          fis = new FileInputStream(myFile);
          bis = new BufferedInputStream(fis);
          bis.read(mybytearray,0,mybytearray.length);
          os = sock.getOutputStream();
          System.out.println("Sending " + myFile.getName() + "(" + mybytearray.length + " bytes)");
          os.write(mybytearray,0,mybytearray.length);
          os.flush();
          System.out.println("Done.");
        }
        finally {
          
          try {
        	  System.out.print("Interrupt in 1 seconds");
        	  Thread.currentThread().sleep(2000);
        	  if (bis != null) bis.close();
        	  
        	  if(fis != null ) fis.close();
              if (os != null) os.close();
              if (sock!=null) sock.close();
//              DataOutputStream dos = new DataOutputStream(
//						sock.getOutputStream());
//				dos.writeUTF("Done send " + file.getName());
        	  Thread.currentThread().interrupt();
          }
          catch(Exception e) {
        	  
          }
        }
    }
    catch(IOException e) {
    	
    }
    
  }
}