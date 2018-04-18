package CSCW;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends CSCW implements Runnable {
	ServerSocket ss;
	public Server(CSCWManager cscwManager, int port, ServerSocket ss){
		super(cscwManager);
		this.ss = ss;
	}
	
	@Override
	public void run() {
		
		try {
	        Socket socket = ss.accept(); //wait for client
	        System.out.println("connect");
	        
	        startCooperation(socket);
	        
		} catch (IOException e) {
			System.out.println("socket closed");
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
