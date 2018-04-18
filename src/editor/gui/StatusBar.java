package editor.gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import editor.RES;

public class StatusBar extends JToolBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5400753296020312276L;

//	private GUI gui;
	private JLabel left = new JLabel();
	private JLabel right = new JLabel();
	
	public StatusBar(GUI gui) {
		super();
//		this.gui = gui;	
		
		setFloatable(false);
		setBorderPainted(false);
		
		initComponents();
	}
	
	private void initComponents(){
		init();
		right.setHorizontalAlignment(SwingConstants.RIGHT);
		this.setLayout(new GridLayout(1,2,0,0));
		add(left);
		add(right);
	}
	public void updateRemoteInfo(String ipandport){
		left.setText(RES.statusbar_connected + ipandport);
	}
	public void updateDelay(long delay){
		if(delay < 1000){
			right.setText(RES.statusbar_delay_5);
		}
		else if(delay < 2000){
			right.setText(RES.statusbar_delay_4);
		}
		else if(delay < 3000){
			right.setText(RES.statusbar_delay_3);
		}
		else if(delay < 4000){
			right.setText(RES.statusbar_delay_2);
		}
		else if(delay < 5000){
			right.setText(RES.statusbar_delay_1);
		}
		else{
			right.setText(RES.statusbar_delay_0);
			left.setText(RES.statusbar_disconnected
					+ "，"
					+ (60 - delay/1000)
					+ RES.statusbar_disconnected_seconds);
		}
		
	}
	public void init(){
		left.setText(RES.statusbar_unconnected);
		right.setText("");
	}
}
