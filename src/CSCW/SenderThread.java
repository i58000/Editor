package CSCW;

import java.io.*;
import java.net.Socket;

import javax.swing.JOptionPane;
 
public class SenderThread extends Thread {
 
    private Socket socket = null;
    private boolean run = true;
    private UserAction userAction;
    private CSCWManager cscwManager;
    
    public SenderThread(Socket socket, CSCWManager cscwManager){
        this.socket = socket;
        this.cscwManager = cscwManager;
    }
    
    public void run(){
    	System.out.println("sender run");
        ObjectOutputStream os = null;  
        ObjectInputStream is = null;  
        
        synchronized(this){
        try {
        	os = new ObjectOutputStream(
        			new BufferedOutputStream(socket.getOutputStream()));
        	os.flush();
        	while(run){
            	this.wait(); // wait the user action
                os.writeObject(userAction);  
                os.flush();
                
        	}
        } catch (IOException e) {
            e.printStackTrace();
        	disconnect();
        } catch (InterruptedException e) {
			e.printStackTrace();
        	disconnect();
		}finally {
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
        
        System.out.println("sender end");
    }

    //set run = false to end the loop
    public void close(){
    	run = false;
    }
    
    public void sendUserAction(UserAction userAction){
    	synchronized(this){
    		this.userAction = userAction;
        	this.notify();
    	}
    }
    
    //when exception happened, close all connection
	private void disconnect(){
		System.out.println("disconnect");
		cscwManager.close();
		JOptionPane.showMessageDialog(null, "disconnect", "1111", JOptionPane.WARNING_MESSAGE); 
	}
    

}