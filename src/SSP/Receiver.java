package SSP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import editor.gui.Editor;

public class Receiver extends Thread {
	private Connection conn;
	private Editor editor;
	private String key;
	DatagramSocket socket = null;
	DatagramPacket dp;
	StateList rcvdStates;
	
	
	public Receiver(Connection conn, DatagramSocket socket){
		this.conn = conn;
		this.socket = socket;
		this.editor = conn.getEditor();
		this.key = conn.getKey();
		rcvdStates = new StateList();
	}
	
	public void run() {
        try {
        	while(conn.isRun()){
        		/*
        		 * simulate begin
        		 */
        		if(conn.sml_inProgress()){
					sleep(500);
					continue;
        		}
        		/*
        		 * simulate end
        		 */
        		byte[] buff = new byte[1024];
        		dp = new DatagramPacket(buff, buff.length);
        		socket.receive(dp);
        		byte[] arr = new byte[dp.getLength()];
        		System.arraycopy(dp.getData(), 0, arr, 0, dp.getLength());
        		Packet p = Packet.parseFrom(arr, key);
        		conn.last_heard_time = System.currentTimeMillis();
        		
        		updateRemoteInfo();
//        		updateLastHeardInfo();
        		
        		unpack(p);
        		
//        		System.out.println("recv : " + rcvdStates);
        	}
            
        } catch (IOException | InterruptedException | InvalidKeyException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            if(conn.isRun())
            	e.printStackTrace();
            //else the sender has sent the END and close this socket, so this receiver stop blocking and finish normally
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println("client key wrong!");
			run();
		}
	}
	
	private void unpack(Packet p){
		System.out.println(p.TYPE);
		
		switch(p.TYPE){
		case NEW:
			SSP.State rs = p.unpack();
			//1. ack
            process_acknowledgment_through(rs.ack);
    		//2. check whether existed
		   	if(rcvdStates.contains( rs.num )){
		   		System.out.println("<!> recv: exist");
		   		break;
		   	}
		   	//3. make sure we have the old state, ignoring out-of-order packet
		   	if(!rcvdStates.contains(rs.old)){
		   		System.out.println("<!> recv: not contains old:"+rs.old+"->"+rs.num+":"+rs.diff);
		   		break;
		   	}
		   	//4. throw away
		   	process_throwaway_until(rs.throwaway);
		   	//5. insert new state in sorted place
            rcvdStates.insert(rs);
		   	//6. apply
            String newText = rcvdStates.applyDiff();
            if(!newText.equals(conn.last_sent_state))
            	editor.setText( newText );
            //7. update conn info
            conn.last_rcvd_num = rs.num;
            break;
		case NOP:
			process_acknowledgment_through(p.ack);
			process_throwaway_until(p.throwaway);
			break;
		case END:
			conn.close();
			break;
		default: //INI?
			System.out.println("connected!");
			//update conn info
			conn.connected();
            conn.last_rcvd_num = p.num;
            updateRemoteInfo();
            
			SSP.State s = p.unpack();
			s.setText(editor.getText());
	        
	        if(!conn.isServer()){ //client
	            conn.sender.sentStates.initClient(s.text); //modify the sentStates[0]
	            String iniText = s.diffFrom("");
	            s.setText(iniText);
	        	editor.setText(iniText); //modify the rcvdStates[0]
	        }
	        rcvdStates.insert(s);
			break;
		}
	}
	
	private void process_acknowledgment_through(long ack){
		conn.sender.sentStates.process_acknowledgment_through(ack);
	}
	private void process_throwaway_until(long throwaway){
		rcvdStates.process_throwaway_until(throwaway);
	}
	
	private void updateRemoteInfo(){
		conn.remote_addr = dp.getAddress();
	    conn.remote_port = dp.getPort();
	    conn.updateRemoteInfo();
	}
//	private void updateLastHeardInfo(){
//		conn.updateDelay();
//	}
	
}
