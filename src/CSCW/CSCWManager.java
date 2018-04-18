package CSCW;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


import editor.gui.Editor;
import editor.gui.GUI;

public class CSCWManager {
	private boolean run = false;
	private boolean is_server;
	private Server server;
	private Client client;
	private Editor editor;
	private GUI gui;
	private ServerSocket ss = null;
	
	public CSCWManager(GUI gui){
		this.gui = gui;
		this.editor = gui.getEditor();
	}
	
	//start CSCW as server
	public boolean startServer(int port){
		is_server = true;
		try {
			ss = new ServerSocket(port);
			System.out.println("Server running");
			setRun(true);
			server = new Server(this, port, ss);
			Thread thread = new Thread(server);
			thread.start();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
		
	}
	
	//start CSCW as client
	public boolean startClient(String IP, int port){
		is_server = false;
		Socket socket = null;
		try {
			socket = new Socket(IP, port);
			System.out.println("client port: " + socket.getLocalPort());
			setRun(true);
			client = new Client(this, socket);
			Thread thread = new Thread(client);
			thread.start();
			return true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//setRun(false);
			return false;
		}
	}
	
	//close all connection
	public void close(){
		if(is_server){
			try {
				if(ss != null)
					ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			server.close();
		}
		else
			client.close();		
		run = false;
		gui.getMenubar().setToolOpenCSCWItemText(false);
	}
	
	private void setRun(boolean r){
		run = r;
	}
	
	public boolean isRun(){
		return run;
	}
	
	//send the user action
	public boolean sendUserAction(UserAction userAction){
		if(run){
			System.out.println("send: " + userAction.getText());
			if(is_server)
				server.getSenderThread().sendUserAction(userAction);
			else
				client.getSenderThread().sendUserAction(userAction);
			return true;
		}
		else
			return false;
		
	}
	
	//receive the user action
	public boolean recvUserAction(UserAction userAction){
		if(run){
			System.out.println("recv: " + userAction.getText());
			if(!userAction.getText().equals(editor.getText()))
	    		editor.setText(userAction.getText());
			return true;
		}
		else
			return false;
	}
	
	public Server getServer(){
		return server;
	}
	public Client getClient(){
		return client;
	}
	
	public boolean isServer(){
		return is_server;
	}
}
