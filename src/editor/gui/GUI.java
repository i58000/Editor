package editor.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

//import CSCW.CSCWManager;
import SSP.Connection;
import editor.RES;


public class GUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4895391909745067904L;
	
	private Editor editor;
	private StructureList structureList;
	private MenuBar menuBar;
	private StatusBar statusBar;
	
//	private CSCWManager cscwManager;
	private Connection conn;
	
	public GUI() {
		menuBar = new MenuBar(this);
		statusBar = new StatusBar(this);
		editor = new Editor(this);
		structureList = new StructureList(this);
//		cscwManager = new CSCWManager(this);
		conn = new Connection(this);
		
		
	    setIconImage(RES.icon);
	    
		initComponents();
	}
	private void initComponents() {
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		
		this.setJMenuBar(menuBar);

		editor.syncTitle();
		getContentPane().add(editor, java.awt.BorderLayout.CENTER);
        
		getContentPane().add(structureList, java.awt.BorderLayout.WEST);
		getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
	}
	
	public Editor getEditor(){
		return editor;
	}
	
	public StructureList getStructureList(){
		return structureList;
	}
	
	public MenuBar getMenubar(){
		return menuBar;
	}
	
//	public CSCWManager getCSCWManager(){
//		return cscwManager;
//	}
	public Connection getConnection(){
		return conn;
	}
	public StatusBar getStatusbar(){
		return statusBar;
	}

}
