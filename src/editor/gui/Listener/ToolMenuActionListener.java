package editor.gui.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import SSP.Connection;
import editor.RES;
import editor.gui.CSCWUI;
import editor.gui.Editor;
import editor.gui.GUI;
import editor.gui.MenuBar;

public class ToolMenuActionListener implements ActionListener {
	
	private GUI gui;
	private boolean previewOpened = false;
	
	public ToolMenuActionListener(GUI gui) {
		super();
		this.gui = gui;
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch(command){
		case "openPreview":
			this.openPreview();
			break;
		case "importCSS":
			this.importCSS();
			break;
		case "selectCSS":
			this.selectCSS();
			break;
		case "about":
			this.about();
			break;
		case "openCSCW":
			this.openCSCW();
			break;
		case "simulate":
			this.simulate();
			break;
		}
	}

	private void simulate(){
		Connection conn = gui.getConnection();
		MenuBar menuBar = gui.getMenubar();
		if(conn.sml_inProgress()){ //reconnect
			conn.sml_reconnect();
			menuBar.setToolOpenSimulateItemText(false);
		} else { //disconnect
			conn.sml_disconnect();
			menuBar.setToolOpenSimulateItemText(true);
		}
	}
	private void openCSCW(){
		Connection conn = gui.getConnection();
		if(conn.isRun()){
			conn.close();
		}
		else
			new CSCWUI(gui);
	}
	
	private void openPreview(){
		Editor editor = gui.getEditor();
		MenuBar menuBar = gui.getMenubar();
		if(previewOpened){
			previewOpened = false;
			editor.openPreview(false);
			menuBar.setToolOpenPreviewItemText(false);
		}
		else{
			previewOpened = true;
			editor.openPreview(true);
			menuBar.setToolOpenPreviewItemText(true);
		}
		
	}
	
	private void importCSS() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//only file
		chooser.setDialogTitle(RES.filechooser_importcss);
		chooser.setFileFilter(new FileNameExtensionFilter("CSS Files", new String[] { "css" }));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", new String[] { "txt" }));
			
		int state = chooser.showSaveDialog(gui);
		if (state == JFileChooser.APPROVE_OPTION) {
			File chooseFile = chooser.getSelectedFile();
			File destFile = new File(System.getProperty("user.dir"), RES.CSS_PATH + chooseFile.getName());  
			if (destFile.exists()) {  
				int overlay = JOptionPane.showConfirmDialog(null, RES.confirmdialog_text_overlay,RES.confirmdialog_title_overlay, JOptionPane.YES_NO_OPTION);
				if(overlay == JOptionPane.NO_OPTION) {
					int selectTheExistFile = JOptionPane.showConfirmDialog(null, RES.confirmdialog_text_selectexist,RES.confirmdialog_title_selectexist, JOptionPane.YES_NO_OPTION);
					if (selectTheExistFile == JOptionPane.YES_OPTION) {
						selectCSS(destFile);
					}
					return;
				}
	        } 
			else { 
	            if (!destFile.getParentFile().exists()) { 
	                if (!destFile.getParentFile().mkdirs()) { 
	                	//JOptionPane.showMessageDialog(null, RES.messagedialog_importcssfailed); //tips
	                	//JOptionPane。showMessageDialog(null, "WARNING_MESSAGE", "WARNING_MESSAGE", JOptionPane.WARNING_MESSAGE);
	                	JOptionPane.showMessageDialog(null, RES.messagedialog_text_importcssfailed, RES.messagedialog_title_importcssfailed, JOptionPane.WARNING_MESSAGE);
	                	return;
	                }  
	            }
	        }
			
			int byteread = 0; // 读取的字节数  
		    InputStream in = null;  
		    OutputStream out = null;  
		  
		    try {  
	            in = new FileInputStream(chooseFile);  
	            out = new FileOutputStream(destFile);  
	            byte[] buffer = new byte[1024];  
	  
	            while ((byteread = in.read(buffer)) != -1) {  
	                out.write(buffer, 0, byteread);  
	            }
	        } catch (FileNotFoundException e) {
	        	e.printStackTrace();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        } finally {  
	            try {  
	                if (out != null) out.close();  
	                if (in != null) in.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }
		    
		    selectCSS(destFile);
		}
	}
	
	private void selectCSS() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// only file
		chooser.setCurrentDirectory(new File(System.getProperty("user.dir"), RES.CSS_PATH));
		chooser.setDialogTitle(RES.filechooser_selectcss);
		chooser.setFileFilter(new FileNameExtensionFilter("CSS Files", new String[] { "css" }));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", new String[] { "txt" }));
			
		int state = chooser.showSaveDialog(gui);
		if (state == JFileChooser.APPROVE_OPTION) {
			File chooseFile = chooser.getSelectedFile();
			selectCSS(chooseFile);
		}
	}
	
	private void selectCSS(File CSSFile) {
		Editor editor = gui.getEditor();
		editor.setCSS(CSSFile);
	}
	
	private void about() {
		JOptionPane.showMessageDialog(null, RES.messagedialog_text_about, RES.messagedialog_title_about, JOptionPane.PLAIN_MESSAGE);
	}
}
