package ChatAppUsingJava;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChatClient extends JFrame implements ActionListener{
	
	private Socket conn;
	private ServerSocket serve;
	static ArrayList<String> OnlineList;
	
	private final JPanel panel;
	private final JFrame f;
    private final JTextArea t;
    private JTextField Partner_name;
    private JButton Chat;

    public ChatClient(Socket conn, ServerSocket serve, String User) {
    	this.serve = serve;
    	this.conn = conn;
    	panel = new JPanel();
        f = new JFrame(User); //gui main window
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 500);
        t = new JTextArea("Waiting clients online...\n"); //to display the data
        t.setBounds(20, 20, 300, 300);
        panel.add(t);
        Partner_name = new JTextField();
        Partner_name.setBounds(20, 400, 200, 50);
        Chat = new JButton("Chat");
        Chat.setBounds(250, 400, 100 , 50);
        panel.add(Partner_name);
        panel.add(Chat);
        panel.setLayout(null);
        f.add(panel);
        f.setVisible(true);
        Chat.addActionListener(this);
        
        while (true) {
			try {
				DataInputStream dis = new DataInputStream(conn.getInputStream());
				String string = dis.readUTF();
				
				t.setText("Online : " + '\n');
				// chuyen chuoi string ve lai arraylist<String> de su dung
				OnlineList = new ArrayList(Arrays.asList(string.substring(1, string.length() - 1).replaceAll("\\s", "").split(",")));
				
				System.out.println(OnlineList.toString());
				
				for(Object str:OnlineList){
					if(!str.toString().split(":")[0].equals(User)) t.append(str.toString().split(":")[0] + '\n');
					} 
				
//				for(String i: OnlineList) {
//					if(!i.split(":")[0].equals(User)) t.append(i + '\n');
//				}
				
				//t.setText("Online" + string);
			} catch (Exception e1) {
				t.setText("fail to open");
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
		if ((e.getSource() == Chat) && (Partner_name.getText() != "")) {
			System.out.println(Partner_name.getText());
			
			int Canconnect = 0;
			String Ip_partner = "";
			String Port_partner = "";
			String Name_partner = "";
			
			for(Object str:OnlineList){
				if(str.toString().split(":")[0].equals(Partner_name.getText())) {
					Canconnect = 1;
					// Co the se can Thread de quan li cac ket noi
					Ip_partner = str.toString().split(":")[2];
					Port_partner = str.toString().split(":")[3];
					Name_partner = str.toString().split(":")[0];
				}
			} 
			
			if(Canconnect ==1 ) {
				//Open peer to peer
				//JOptionPane.showMessageDialog(f,"Connect with "+ Partner_name.getText());
				try {
					PeerChat peer = new PeerChat(serve, Ip_partner, Port_partner,Name_partner);
					Thread p = new Thread(peer);
					p.start();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else{
				JOptionPane.showMessageDialog(f,"Not found Partner!, please try again");
			}
			
			Partner_name.setText("");
		}
	}

}