package editor;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import editor.gui.GUI;


public class Main {

	public static void main(String[] args) {
		
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//"com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                new GUI().setVisible(true);
            }
        });
        
        
		
	}
}
