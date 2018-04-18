package editor.gui.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import editor.gui.Editor;
import editor.gui.GUI;


public class EditMenuActionListener implements ActionListener {
	
	private GUI gui;

	public EditMenuActionListener(GUI gui) {
		super();
		this.gui = gui;
	}

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("undo".equals(command)) {
			this.undo();
		} else if ("redo".equals(command)) {
			this.redo();
		}

	}

	/**
	 * redo
	 */
	private void redo() {
		Editor editor = this.gui.getEditor();
		if(null != editor){
			editor.redo();
		}
	}

	/**
	 * undo
	 */
	private void undo() {
		Editor editor = this.gui.getEditor();
		if(null != editor){
			editor.undo();
		}
	}

}
