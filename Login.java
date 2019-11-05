package ChatAppUsingJava;

import java.awt.event.*;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.sun.glass.events.WindowEvent;

public class Login extends JFrame implements ActionListener{
	
	DataInputStream dis ;
    DataOutputStream dos ;
    String IpandPort;
	
    ServerSocket server_peer;
    
	JPanel panel;
	JTextField User_name;
	JTextField Password;
	JTextField Loginstatus;
	JButton Login;
	private int login_status = 0;
	
	public Login(ServerSocket server_peer, Socket conn, DataInputStream dis, DataOutputStream dos,String IpandPort){
		
		// Create display screen
		panel = new JPanel();
		User_name = new JTextField();
		Password = new JTextField();
		Loginstatus = new JTextField();
		Login = new JButton("Login");
		this.setSize(400, 200);
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel.setLayout(null);
		this.add(panel);
		this.setTitle("LOGIN");
		User_name.setBounds(10, 20 , 250, 40);
		panel.add(User_name);
		Password.setBounds(10, 70 , 250, 40);
		panel.add(Password);
		Login.setBounds(300, 20 , 70 , 90);
		panel.add(Login);
		Loginstatus.setBounds(10, 120 , 250, 20);
		Loginstatus.setVisible(false);
		panel.add(Loginstatus);
		Login.addActionListener(this);
        
		// Solve socket problem
		this.server_peer = server_peer;
		this.dis = dis;
		this.dos = dos;
		this.IpandPort = IpandPort;
		
		// Receive login_status from server
		while (true) {
			try {
				String string = dis.readUTF();
				System.out.println(string);
				if(string.equals("Not found")) {
					JOptionPane.showMessageDialog(panel,"Wrong username or password!");
					//Loginstatus.setText("Login fail");
					System.out.println("Login Fail");
				}
				else {
					JOptionPane.showMessageDialog(panel,"Login succesful");
					//Loginstatus.setText("Login succesful");
					System.out.println("Login OK");
					this.dispose();
					new ChatClient(conn,server_peer,string);
				}
			} catch (Exception e1) {
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if ((e.getSource() == Login) && (User_name.getText() != "") && (Password.getText() != "")) {
			try {
				dos.writeUTF(User_name.getText() + ":" + Password.getText() + ":" + IpandPort);
		        
			} catch (Exception e1) {
				try {
					Thread.sleep(3000);
					System.exit(0);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
			User_name.setText("");
			Password.setText("");
		}
	}
	
	
}
