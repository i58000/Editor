package CSCW;

import java.net.Socket;

public abstract class CSCW {
	//whichever server or client, it needs two threads:
	private SenderThread senderThread = null;
    private ReceiverThread receiverThread = null;
    
    protected CSCWManager cscwManager; 
    
    public CSCW(CSCWManager cscwManager) {
    	this.cscwManager = cscwManager;
    }
	
	public SenderThread getSenderThread(){
		return senderThread;
	}
	public ReceiverThread getReceiverThread(){
		return receiverThread;
	}
	
	public void close(){
		if(senderThread != null)
			senderThread.close();
		if(receiverThread != null)
			receiverThread.close();
	}
	
	//start CSCW
	public void startCooperation(Socket socket){
		senderThread = new SenderThread(socket, cscwManager);
		receiverThread = new ReceiverThread(socket, cscwManager);
		
		senderThread.start();
		receiverThread.start();
	}

}
