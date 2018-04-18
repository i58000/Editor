package CSCW;

import java.net.Socket;


public class Client extends CSCW implements Runnable {
	private Socket socket;
    
	public Client(CSCWManager cscwManager, Socket socket){
		super(cscwManager);
		this.socket = socket;
	}
	
	@Override
	public void run() {
		startCooperation(socket);
	}

} 

