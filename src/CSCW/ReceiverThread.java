package CSCW;

import java.io.*;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ReceiverThread extends Thread {
	
    private Socket socket = null;
    private boolean run = true;
    
    private CSCWManager cscwManager;
    
    
	ReceiverThread(Socket socket, CSCWManager cscwManager){
		this.socket = socket;
		this.cscwManager = cscwManager;
	}
    
	public void run(){
		System.out.println("receiver run");
		
        ObjectOutputStream os = null;  
        ObjectInputStream is = null; 
        
        
        try {
        	is = new ObjectInputStream(
            		new BufferedInputStream(socket.getInputStream()));
        	while(run){
                
        		Object obj = is.readObject();
                
                if (obj != null) {
                	UserAction userAction = (UserAction)obj; 
                	recvUserAction(userAction);
                }
                
        	}
            
        } catch (ClassNotFoundException e) {
        	disconnect();
            e.printStackTrace();
        } catch (IOException e) {
        	disconnect();
			e.printStackTrace();
		} finally {
        	try {
        		if (os != null)
        			os.close();
                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
 
        }
	}

	//set run = false to end the loop
	public void close(){
		run = false;
	}
	
	private void recvUserAction(UserAction userAction){
		if(run){
			//System.out.println("receive:" + userAction);
			cscwManager.recvUserAction(userAction);
			//editor.updateUI();
		}
	}
	
	//when exception happened, close all connection
	private void disconnect(){
		System.out.println("disconnect");
		cscwManager.close();
		JOptionPane.showMessageDialog(null, "disconnect", "1111", JOptionPane.WARNING_MESSAGE); 
	}

}
