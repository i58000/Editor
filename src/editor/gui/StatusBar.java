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
	public void setLeft(String ipandport){
		left.setText(RES.statusbar_connected + ipandport);
	}
	public void setRight(long delay){
		if(delay < 2000){
			right.setText(RES.statusbar_delay + delay);
		}
		else{
			right.setText(RES.statusbar_disconnected + delay/1000);
		}
		
	}
	public void init(){
		left.setText(RES.statusbar_unconnected);
		right.setText("");
	}
}
