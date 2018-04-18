package editor.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu.Separator;

import editor.RES;
import editor.gui.Listener.EditMenuActionListener;
import editor.gui.Listener.FileMenuActionListener;
import editor.gui.Listener.ToolMenuActionListener;

public class MenuBar extends JMenuBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7307999110524299705L;
	
	private GUI gui;
	
	private JMenuItem openPreviewItem = new JMenuItem();
	private JMenuItem openCSCWItem = new JMenuItem();
	private JMenuItem simulateItem = new JMenuItem();
	
	public MenuBar(GUI gui) {
		super();
		this.gui = gui;	
		initComponents();
	}

	private void initComponents() {
		
		//file
		JMenu fileMenu = new JMenu();
		fileMenu.setMnemonic('F');
        fileMenu.setText(RES.menubar_file);
        
        //edit
		JMenu editMenu = new JMenu();
		editMenu.setMnemonic('E');
        editMenu.setText(RES.menubar_edit); 
        
        //tool
		JMenu toolMenu = new JMenu();
		toolMenu.setMnemonic('T');
        toolMenu.setText(RES.menubar_tool); 
        
		//listener
		FileMenuActionListener fileMenuActionListener = new FileMenuActionListener(this.gui);
		EditMenuActionListener editMenuActionListener = new EditMenuActionListener(this.gui);
		ToolMenuActionListener toolMenuActionListener = new ToolMenuActionListener(this.gui);
		

        //file
        JMenuItem newFileItem = new JMenuItem();
        JMenuItem openFileItem = new JMenuItem();
        JMenuItem saveFileItem = new JMenuItem(); 
        JMenuItem saveAsItem = new JMenuItem();
        JMenuItem exitItem = new JMenuItem();    
        JMenuItem exportAsDocItem = new JMenuItem();   
        
        newFileItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newFileItem.setText(RES.menubar_file_newdoc);
        newFileItem.setActionCommand("newFile");
        newFileItem.addActionListener(fileMenuActionListener);
        fileMenu.add(newFileItem);

        openFileItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openFileItem.setText(RES.menubar_file_opendoc); 
        openFileItem.setActionCommand("openFile");
        openFileItem.addActionListener(fileMenuActionListener);
        fileMenu.add(openFileItem);
        
        fileMenu.add(new Separator());

        saveFileItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveFileItem.setText(RES.menubar_file_savedoc);
        saveFileItem.setActionCommand("saveFile");
        saveFileItem.addActionListener(fileMenuActionListener);
        fileMenu.add(saveFileItem);

        saveAsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsItem.setText(RES.menubar_file_savedocto);
        saveAsItem.setActionCommand("saveAs");
        saveAsItem.addActionListener(fileMenuActionListener);
        fileMenu.add(saveAsItem);
        
        exportAsDocItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exportAsDocItem.setText(RES.menubar_file_exportasdoc); 
        exportAsDocItem.setActionCommand("exportAsDoc");
        exportAsDocItem.addActionListener(fileMenuActionListener);
        fileMenu.add(exportAsDocItem);
        
        fileMenu.add(new Separator());

        exitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitItem.setText(RES.menubar_file_exit); 
        exitItem.setActionCommand("exit");
        exitItem.addActionListener(fileMenuActionListener);
        fileMenu.add(exitItem);
        
        this.add(fileMenu);
        
        //edit
        JMenuItem undoItem = new JMenuItem();
        JMenuItem redoItem = new JMenuItem();
        
        undoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        undoItem.setText(RES.menubar_edit_undo); 
        undoItem.setActionCommand("undo");
        undoItem.addActionListener(editMenuActionListener);
        editMenu.add(undoItem);
        
        redoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        redoItem.setText(RES.menubar_edit_redo);
        redoItem.setActionCommand("redo");
        redoItem.addActionListener(editMenuActionListener);
        editMenu.add(redoItem);
        
        this.add(editMenu);
        
        //tool
        JMenuItem aboutItem = new JMenuItem();
        JMenuItem importCSSItem = new JMenuItem();
        JMenuItem selectCSSItem = new JMenuItem();
        //JMenuItem openCSCW = new JMenuItem();
        //JMenuItem openPreviewItem = new JMenuItem();
        //JMenuItem simulateItem = new JMenuItem();
        
        aboutItem.setText(RES.menubar_tool_about);
        aboutItem.setActionCommand("about");
        aboutItem.addActionListener(toolMenuActionListener);
        toolMenu.add(aboutItem);
        
        toolMenu.add(new Separator());
        
        openPreviewItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        openPreviewItem.setText(RES.menubar_tool_openpreview);
        openPreviewItem.setActionCommand("openPreview");
        openPreviewItem.addActionListener(toolMenuActionListener);
        toolMenu.add(openPreviewItem);
        
        toolMenu.add(new Separator());
        
        importCSSItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        importCSSItem.setText(RES.menubar_tool_importcss);
        importCSSItem.setActionCommand("importCSS");
        importCSSItem.addActionListener(toolMenuActionListener);
        toolMenu.add(importCSSItem);
        
        selectCSSItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        selectCSSItem.setText(RES.menubar_tool_selectcss); 
        selectCSSItem.setActionCommand("selectCSS");
        selectCSSItem.addActionListener(toolMenuActionListener);
        toolMenu.add(selectCSSItem);
        
        toolMenu.add(new Separator());
        
        openCSCWItem.setText(RES.menubar_tool_opencscw); 
        openCSCWItem.setActionCommand("openCSCW");
        openCSCWItem.addActionListener(toolMenuActionListener);
        toolMenu.add(openCSCWItem);
        
        
        simulateItem.setText(RES.menubar_tool_opensml); 
        simulateItem.setActionCommand("simulate");
        simulateItem.addActionListener(toolMenuActionListener);
        toolMenu.add(simulateItem);
        
        
        this.add(toolMenu);
	}
	
	public void setToolOpenPreviewItemText(boolean opened){
		if (opened){
			openPreviewItem.setText(RES.menubar_tool_closepreview);
		}
		else {
			openPreviewItem.setText(RES.menubar_tool_openpreview);
		}
		
	}
	
	public void setToolOpenCSCWItemText(boolean opened){
		if (opened){
			openCSCWItem.setText(RES.menubar_tool_closecscw);
		}
		else {
			openCSCWItem.setText(RES.menubar_tool_opencscw);
		}
		
	}
	
	public void setToolOpenSimulateItemText(boolean opened){
		if (opened){
			simulateItem.setText(RES.menubar_tool_closesml);
		}
		else {
			simulateItem.setText(RES.menubar_tool_opensml);
		}
	}

}
