package SSP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;

import javax.swing.JOptionPane;

import editor.RES;
import editor.gui.Editor;

public class Sender extends Thread {
	
	private Connection conn;
	private Editor editor;
	private String key;
	DatagramSocket socket = null;
	DatagramPacket dp;
	
	States sentStates;
	
	public Sender(Connection conn, DatagramSocket socket){
		this.conn = conn;
		this.socket = socket;
		this.editor = conn.getEditor();
		this.key = conn.getKey();
		sentStates = new States(); //warning
	}
	
	public void run() {
		boolean init_done = false;
		try {
			init_done = init();
			if(init_done){
				while (conn.isRun()) {
	            	if ( conn.remote_addr != null ) {
	            		
	            		//1. get current text and current timestamp
	            		String current = editor.getText();
	            		long now = System.currentTimeMillis();
	            		
	            		//2. if heard remote timeout
	            		boolean forced_resend = (now - conn.last_heard_time > Connection.TIMEOUT_HEARD);
	            		
	            		//3. if last_sent_first timeout, send all states in the sentStates
	            		if(now - conn.last_resend_time > Connection.TIMEOUT_RESEND){
	            			conn.last_resend_time = now;
	            			Iterator<SSP.State> iterator = sentStates.getAll().iterator();
	            			while (iterator.hasNext()) {
	            				Packet p = new Packet(iterator.next());
	            				send(p);
	            			}
	            		}
	            		
	            		//4. if has diff, append to sentStates and send diff
	            		if( sentStates.append(current, conn.last_rcvd_num, forced_resend))
	        			{
	                        Packet p = new Packet(sentStates.getLast());
	                        send(p);
	                        conn.last_sent_state = current;
	        			}
	        			else{ //send NOP
	    					Packet p = new Packet(conn.last_rcvd_num, sentStates.getFirst().num);
	        				send(p);
	        				
	        			}
	            		
	        			System.out.println( "send: " + sentStates);
	        		}
	            	updateLastHeardInfo();//status
	                sleep(Connection.INTERVAL_SEND);
	            }
			}
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
        	if(init_done)
        		close(); //send END
        }
	}
	
	private void close() {
		Packet p = new Packet();
		send(p);
		socket.close();
		conn.init();
	}
	
	private void send(Packet p) {
		/*
		 * simulator begin
		 */
		if(conn.sml_inProgress()){
			return;
		}
		/*
		 * simulator end
		 */
		dp = new DatagramPacket(new byte[0], 0, conn.remote_addr, conn.remote_port);
		byte[] arr = p.toByteArray(key);
        dp.setData(arr);//填充DatagramPacket
        try{
        	socket.send(dp);//发送
        } catch (IOException e) {
        	if(conn.isRun()){
        		conn.updateDelay();
        	}
//				e.printStackTrace();
        }
        
	}
	
	private boolean init() throws IOException, InterruptedException{
		if(conn.isServer()){ //server block, wait client
			while(conn.isRun()){
				if(conn.remote_addr != null) 
					break;
				sleep(Connection.INTERVAL_SERVER_CHECK_RUN);
			}
			
		}
		String current = editor.getText();
		sentStates.insert(new SSP.State(current));
		Packet p = new Packet(current);
        send(p);
        conn.last_sent_state = current;
    
        if(!conn.isServer()){ //client block, wait server
    		long start_connecting = System.currentTimeMillis();
        	while(!conn.isConnected()){
        		if(System.currentTimeMillis() - start_connecting > Connection.TIMEOUT_CONNECT){ //timeout
        			JOptionPane.showMessageDialog(null, RES.messagedialog_text_connect_failed, RES.messagedialog_title, JOptionPane.PLAIN_MESSAGE);
        			conn.close();
        			return false;
        		}
        		sleep(Connection.INTERVAL_CLIENT_CHECK_CONNECT);
        	}
        	//connected
        	JOptionPane.showMessageDialog(null, RES.messagedialog_text_connect_succeed, RES.messagedialog_title, JOptionPane.PLAIN_MESSAGE);
        }
        
        return true;
//        System.out.println("sender.init: " + sentStates + "/" + sentStates.getLast().diff);
	}
	
	//status
	private void updateLastHeardInfo(){
		conn.updateDelay();
	}
}
