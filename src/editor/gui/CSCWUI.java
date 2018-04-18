package editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import editor.RES;


public class CSCWUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3706647121358831507L;
	
	private JTabbedPane tabbedPane;
	private JPanel serverPanel;
	private JPanel clientPanel;
	private GUI gui;
	
//	private JButton btnConnect;

	public CSCWUI(GUI gui) {
		this.gui = gui;
		tabbedPane = new JTabbedPane();
		clientPanel = new JPanel();
		serverPanel = new JPanel();
		initComponents();
	}
	private void initComponents() {
		setTitle(RES.title_cscw);
		setSize(300,200);
	    setVisible(true);
	    setLocationRelativeTo(null);
	    
	    add(tabbedPane);
	    tabbedPane.add(RES.tabbedpane_as_server, serverPanel);
	    tabbedPane.add(RES.tabbedpane_as_client, clientPanel);
	    
	    initServerPanel();
	    initClientPanel();
	    
	    this.setAlwaysOnTop(true);
	   
	}
	
	//initial server panel
	private void initServerPanel(){
		JLabel portLabel = new JLabel(RES.label_port);
		JTextField textField = new JTextField(RES.default_port); //1024-65535
		JButton btn = new JButton(RES.btn_start);
		
		serverPanel.setLayout(null);
		serverPanel.add(portLabel);
		serverPanel.add(textField);
		serverPanel.add(btn);
		
		portLabel.setBounds(80, 30, 50, 20);
		textField.setBounds(120, 30, 50, 20);
		btn.setBounds(90, 80, 90, 30);
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
	        public void keyTyped(KeyEvent evt) {
				Object o = evt.getSource();
				if (o instanceof JTextField){
					char keyCh = evt.getKeyChar();
			        if ((keyCh < '0') || (keyCh > '9')){ //only accept number
			        	if (keyCh != '') //回车字符
			        		evt.setKeyChar('\0');
			        }
			    }
			}
		});
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port = Integer.parseInt(textField.getText()); // to int
				startServer(port);
			}
		});
	}
	
	//initial client panel
	private void initClientPanel(){
		JLabel IPLabel = new JLabel(RES.label_ip);
		JLabel portLabel = new JLabel(RES.label_port);
		JTextField portTextField = new JTextField(RES.default_port); //1024-65535
		JTextField IPTextField = new JTextField(RES.default_ip); //1024-65535
		JButton btn = new JButton(RES.btn_connect);
//		btnConnect = btn;
		
		clientPanel.setLayout(null);
		clientPanel.add(IPLabel);
		clientPanel.add(IPTextField);
		clientPanel.add(portLabel);
		clientPanel.add(portTextField);
		clientPanel.add(btn);
		
		IPLabel.setBounds(80, 15, 50, 20);
		IPTextField.setBounds(120, 15, 100, 20);
		portLabel.setBounds(80, 45, 50, 20);
		portTextField.setBounds(120, 45, 50, 20);
		btn.setBounds(90, 80, 90, 30);
		
		IPTextField.addKeyListener(new KeyAdapter() {
			@Override
	        public void keyTyped(KeyEvent evt) {
				Object o = evt.getSource();
				if (o instanceof JTextField){
					char keyCh = evt.getKeyChar();
			        if (((keyCh < '0') || (keyCh > '9')) && (keyCh != '.')){ //only accept number and dot
			        	if (keyCh != '') //回车字符
			        		evt.setKeyChar('\0');
			        }
			    }
			}
		});
		
		portTextField.addKeyListener(new KeyAdapter() {
			@Override
	        public void keyTyped(KeyEvent evt) {
				Object o = evt.getSource();
				if (o instanceof JTextField){
					char keyCh = evt.getKeyChar();
			        if ((keyCh < '0') || (keyCh > '9')){ //only accept number
			        	if (keyCh != '') //回车字符
			        		evt.setKeyChar('\0');
			        }
			    }
			}
		});
		
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port = Integer.parseInt(portTextField.getText()); //to int
				String IP = IPTextField.getText();
				connectServer(IP, port);
			}
		});
		
	}
	
	private void startServer(int port){
		if(port < 1024 || port > 65535){ //incorrect port 
			JOptionPane.showMessageDialog(null, RES.messagedialog_text_port_inputed_error, RES.messagedialog_title, JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(gui.getConnection().startServer(port)){ //if start server succeed
			this.setAlwaysOnTop(false);
			JOptionPane.showMessageDialog(null, RES.messagedialog_text_server_started, RES.messagedialog_title, JOptionPane.PLAIN_MESSAGE);
			gui.getMenubar().setToolOpenCSCWItemText(true);
			this.dispose();
		}
		else{
			this.setAlwaysOnTop(false);
			JOptionPane.showMessageDialog(null, RES.messagedialog_text_port_occupied, RES.messagedialog_title, JOptionPane.WARNING_MESSAGE); 
			this.setAlwaysOnTop(true);
		}
	}
	
	private void connectServer(String IP, int port){
		if(port < 1024 || port > 65535){ //incorrect port 
			JOptionPane.showMessageDialog(null, RES.messagedialog_text_port_inputed_error, RES.messagedialog_title, JOptionPane.WARNING_MESSAGE); 
			return;
		}
		if(gui.getConnection().startClient(IP, port)){ //if connect server succeed
			this.setAlwaysOnTop(false);
//			JOptionPane.showMessageDialog(null, RES.messagedialog_text_connect_succeed, RES.messagedialog_title, JOptionPane.PLAIN_MESSAGE);
			gui.getMenubar().setToolOpenCSCWItemText(true);
			this.dispose();
		}
		else{
			this.setAlwaysOnTop(false);
			JOptionPane.showMessageDialog(null, RES.messagedialog_text_connect_failed, RES.messagedialog_title, JOptionPane.WARNING_MESSAGE); 
			this.setAlwaysOnTop(true);
		}
		
	}
}
