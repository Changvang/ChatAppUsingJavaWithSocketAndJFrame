package ChatAppUsingJava;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
 
public class ReceivedFile implements Runnable{
  public final static int FILE_SIZE = 6022386; // file size temporary hard coded
                                               // should bigger than the file to be downloaded
  
  public static Socket receivesocket;
  public String File_name ;
  
  public ReceivedFile(Socket receivesocket, String File_name) {
	  this.receivesocket = receivesocket;
	  this.File_name = "C:\\Users\\Admin\\Downloads\\" + File_name;
  }
 
  @Override
  public void run() {
    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    Socket sock = null;
    InputStream is = null;
    try {
      sock = receivesocket;
      System.out.println("Connecting...");
 
      // receive file
      byte [] mybytearray  = new byte [FILE_SIZE];
      
	try {
		is = sock.getInputStream();
    	  System.out.println(File_name);
    	  fos = new FileOutputStream(File_name);
    	  bos = new BufferedOutputStream(fos);
    	  bytesRead = is.read(mybytearray,0,mybytearray.length);
    	  current = bytesRead;
 
      do {
         bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);
 
      bos.write(mybytearray, 0 , current);
      bos.flush();
      System.out.println("File " + File_name
          + " downloaded (" + current + " bytes read)");
      }catch(IOException e){
    	  System.out.print("Loi dinh dang file");
      }
    }
    finally {
    	try {
      
      System.out.print("Interrupt in 1 seconds");
	  Thread.currentThread().sleep(2000);
	  if (fos != null) fos.close();
      if (bos != null) bos.close();
      if(is != null) is.close();
	  Thread.currentThread().interrupt();
    	}
    	catch(Exception e){ }
    }
  }
 
}