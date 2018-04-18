package SSP;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import editor.gui.Editor;
import editor.gui.GUI;

public class Connection {
	final static long TIMEOUT_CONNECT = 1000;
	final static long TIMEOUT_HEARD = 5000;
	final static long TIMEOUT_RESEND = 5000;
	final static long TIMEOUT_DISCONNECT = 60000;
	
	final static long INTERVAL_SEND = 500;//500
	final static long INTERVAL_CLIENT_CHECK_CONNECT = 100;
	final static long INTERVAL_SERVER_CHECK_RUN = 100;
	
	private Editor editor;
	private GUI gui;
	
	private boolean connected = false;
	private boolean run = false;
	private boolean is_server;
	
	Sender sender;
	Receiver receiver;
	private DatagramSocket socket = null;
	
	int port;
	InetAddress remote_addr;
	int remote_port;
	
	String last_sent_state;
	long last_rcvd_num;
	long last_heard_time;
	long last_resend_time;
	
	public Connection(GUI gui){
		this.gui = gui;
		this.editor = gui.getEditor();
	}
	
	public boolean startServer(int port){
		is_server = true;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("Server running");
		run = true;
		startCooperation();
		
		return true;
	}
	public boolean startClient(String IP, int port){
		is_server = false;
		try {
			remote_addr = InetAddress.getByName(IP);
			remote_port = port;
			socket = new DatagramSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("Client running : local port: " + socket.getLocalPort());
		run = true;
		startCooperation();
			
		return true;
	}
	
	public void startCooperation(){
		sender = new Sender(this ,socket);
		receiver = new Receiver(this, socket);
		sender.start();
		receiver.start();
	}
	
	// setter and getter
	public Editor getEditor(){
		return editor;
	}
	public boolean isServer(){
		return is_server;
	}
	public boolean isRun(){
		return run;
	}
	public void connected(){
		connected = true;
	}
	public boolean isConnected(){
		return connected;
	}
	
	//simulators
	private boolean sml_flag = false;
	public boolean sml_inProgress(){
		return sml_flag;
	}
	public void sml_disconnect(){
		sml_flag = true;
	}
	public void sml_reconnect(){
		sml_flag = false;
	}
	
	
	public void close(){
		run = false;
		connected = false;
		gui.getMenubar().setToolOpenCSCWItemText(false);
		gui.getStatusbar().init();
	}
	public void init(){
		port = 0;
		remote_addr = null;
		remote_port = 0;
		
		last_sent_state = null;
		last_rcvd_num = 0;
		last_heard_time = 0;
		last_resend_time = 0;
		
		sender = null;
		receiver = null;
		socket = null;
	}
	
	public void updateRemoteInfo(){
		String ip = remote_addr.getHostName();
		String port = Integer.toString(remote_port);
		gui.getStatusbar().setLeft(ip + ":" + port);
	}
	public void updateLastHeardInfo(){
		long delay = System.currentTimeMillis() - last_heard_time;
		gui.getStatusbar().setRight(delay);
		if(delay > TIMEOUT_DISCONNECT)
			close();
	}
}
