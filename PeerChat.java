package ChatAppUsingJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PeerChat extends JFrame implements ActionListener, Runnable {
	static ServerSocket server;
	static Socket send_socket;
	static Socket received_socket;
	JPanel panel;
	JTextField NewMsg;
	JTextArea ChatHistory;
	JButton Send;
	java.io.File file;
	private JFileChooser ChooseFile ;
    private JButton SendFile;
	String username = "You";
	String Partner_name;
	String Port_partner;
	String Ip_partner;
	static int PORT = 4000;
	static int PARTNER_PORT = 4001;
	InetAddress inetAddress = InetAddress.getLocalHost(); // cai nay dung de khi nao can truyen du ip cho server

	public PeerChat(ServerSocket serve, String Ip_partner, String Port_partner, String Name_partner) throws UnknownHostException, IOException {
		this.server = serve;
		this.Partner_name = Name_partner;
		this.Port_partner = Port_partner;
		this.Ip_partner = Ip_partner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ((e.getSource() == Send) && (NewMsg.getText() != "")) {

			ChatHistory.setText(ChatHistory.getText() + '\n' + "You > "
					+ NewMsg.getText());
			try {
				DataOutputStream dos = new DataOutputStream(
						send_socket.getOutputStream());
				dos.writeUTF(NewMsg.getText());
			} catch (Exception e1) {
				ChatHistory.setText(ChatHistory.getText() + '\n'
						+ "Message sending fail:Network Error");
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			NewMsg.setText("");
		}
	}
	
	@Override
	public void run() {
		panel = new JPanel();
		NewMsg = new JTextField();
		ChatHistory = new JTextArea();
		Send = new JButton("Send");
		this.setSize(500, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel.setLayout(null);
		this.add(panel);
		ChatHistory.setBounds(20, 20, 450, 340);
		panel.add(ChatHistory);
		NewMsg.setBounds(20, 400, 340, 30);
		panel.add(NewMsg);
		Send.setBounds(375, 400, 95, 30);
		panel.add(Send);
		Send.addActionListener(this);
		this.setVisible(true);
		// Xu li du lieu va cac soc ket
		
		// Hien thuc gui file
		
		// Setting sendFile
		
				final JFileChooser  fileDialog = new JFileChooser();
		        JButton showFileDialogButton = new JButton("Open File");
		        showFileDialogButton.setBounds(50, 365, 100, 30);
		        showFileDialogButton.addActionListener(new ActionListener() {
		           @Override
		           public void actionPerformed(ActionEvent e) {
		              int returnVal = fileDialog.showOpenDialog(null);
		              if (returnVal == JFileChooser.APPROVE_OPTION) {
		                 file = fileDialog.getSelectedFile();
		                 System.out.println("File Selected :" 
		                 + file.getName());
		              }
		              else{
		            	  System.out.println("Open command cancelled by user." );           
		              }      
		           }
		        });
		        panel.add(showFileDialogButton);
		        
		        SendFile = new JButton("SendFile");
		        SendFile.setBounds(200, 365, 100, 30);
		        SendFile.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		               if(file != null) {
		            	   ChatHistory.setText(ChatHistory.getText() + '\n' + "Me > SendFile:"
		       					+ file.getName());
		            	   try {
		       				DataOutputStream dos = new DataOutputStream(
		       						send_socket.getOutputStream());
		       				dos.writeUTF("SendFile:" + file.getName());
		       				
		       				// Open thread to sendfile include sendsocket, filename
		       				SendFile newsendfile = new SendFile(send_socket, file);
		       				Thread t1 = new Thread(newsendfile);
		       				t1.start();
		       				// Khoi dong lai socket de su dung boi vi sau khi gui file no da dong
		       				send_socket = new Socket(Ip_partner, Integer.parseInt(Port_partner));
		       				
		       			} catch (Exception e1) {
		       				ChatHistory.setText(ChatHistory.getText() + '\n'
		       						+ "Message sending fail:Network Error");
		       				try {
		       					Thread.sleep(3000);
		       					System.exit(0);
		       				} catch (InterruptedException e2) {
		       					// TODO Auto-generated catch block
		       					e2.printStackTrace();
		       				}
		       			}
		               }
		               file = null;
		            }
		         });
		        panel.add(SendFile);
				
		
		// waiting for another peer online
		boolean scanning=true;
		while(scanning)
		{
		    try
		    {
		    	//send_socket = new Socket(InetAddress.getLocalHost(), PARTNER_PORT);// using to send message
		    	this.send_socket = new Socket(Ip_partner, Integer.parseInt(Port_partner));
		        scanning=false;
		    }
		    catch(ConnectException e)
		    {
		    	ChatHistory.setText("Connect failed, waiting and trying again");
		        try
		        {
		            Thread.sleep(2000);//2 seconds
		        }
		        catch(InterruptedException ie){
		            ie.printStackTrace();
		        }
		    } catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		try {
			received_socket = server.accept();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} // using to received massage

		/*
		 * for remote pc use ip address of server with function
		 * InetAddress.getByName("Provide ip here")
		 * ChatHistory.setText("Connected to Server");
		 */

		ChatHistory.setText("Connected to " + Partner_name);
		this.setTitle(username+ " And " + Partner_name);
		while (true) {
			try {
				DataInputStream dis = new DataInputStream(received_socket.getInputStream());
				String string = dis.readUTF();
				ChatHistory.setText(ChatHistory.getText() + '\n' + Partner_name + " > "
						+ string);
				
				// if trong Another peer co SendFile thi mo thread bao gom received_socket, path_file(have name file) It stored in Downloads
				
				boolean isFound = string.indexOf("SendFile") !=-1? true: false; //true
				if(isFound && string.split(":")[0].contentEquals("SendFile")) {
					ReceivedFile newFile = new ReceivedFile(received_socket, string.split(":")[1]);
					Thread t = new Thread(newFile);
					t.start();
					// khoi dong lai socket vi sau khi gui file no buoc phai dong
					received_socket = server.accept();
					// Thread sleep de co the nhan thong qua port
					Thread.sleep(3000);
				}
			} catch (Exception e1) {
				ChatHistory.setText(ChatHistory.getText() + '\n'
						+ "Message sending fail:Network Error");
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}