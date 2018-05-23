package editor.gui;

import com.petebevin.markdown.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
//import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
//import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

import editor.RES;

public class Editor extends javax.swing.JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -23652970309644038L;	
	
	private JTextArea editor;
	private JScrollPane editorScrollPane;
	private JScrollPane previewScrollPane;
	private JSplitPane split;
	private JEditorPane preview;
	private UndoManager undoManager = new UndoManager();
	private String path = "";
	private String CSSPath;
	private GUI gui;

	
	
	public Editor(GUI gui) {
		this.gui = gui;
		initComponents();		
	}

	private void initComponents() {
		this.setLayout(new BorderLayout());
		
		//split
		split = new JSplitPane();
		split.setDividerSize(1);
		split.setOneTouchExpandable(true);
		split.setContinuousLayout(true);
		split.setDividerLocation(400);
		split.setPreferredSize(new Dimension(800, 400));
		
		//editor
		editor = new JTextArea();
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 13);
		editor.setFont(font);
		editor.setTabSize(4);

		editorScrollPane = new JScrollPane();
		editorScrollPane.setViewportView(editor);
		split.setLeftComponent(editorScrollPane);
		
		//preview
		preview = new JEditorPane();
		preview.setEditable(false);
		preview.setBorder(null);
		preview.setContentType("text/html");
		preview.setEditorKit(new HTMLEditorKit());

		previewScrollPane = new JScrollPane();
		previewScrollPane.setViewportView(preview);
		
		split.setRightComponent(previewScrollPane);
		
		this.add(split, java.awt.BorderLayout.CENTER);
		
		// editor key press listener
		editor.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				preview(editor.getText());
				if(gui.getConnection().isRun()){
//					gui.getCSCWManager().sendUserAction(new UserAction(getText()));
				}
				syncPreviewPosition();
			}
		});
		
		// scroll pane adjustment listener
		editorScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent evt) {
				int ev = evt.getValue();
				int pm = previewScrollPane.getVerticalScrollBar().getMaximum();
				int em = editorScrollPane.getVerticalScrollBar().getMaximum();
				int h = editorScrollPane.getVerticalScrollBar().getHeight();
				int pv = (int) (ev * ((pm-h) * 1.0) / ((em-h) * 1.0));
				previewScrollPane.getVerticalScrollBar().setValue(pv);
			}
		});		
		
		// undo listener
		editor.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});

		//StructureList sync listener
		editor.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void changedUpdate(DocumentEvent evt) {
				gui.getStructureList().syncList();
			}

			@Override
			public void insertUpdate(DocumentEvent evt) {
				gui.getStructureList().syncList();
			}

			@Override
			public void removeUpdate(DocumentEvent evt) {
				gui.getStructureList().syncList();
			}
			
		});
		
		
		
		
		// initial CSS
		setCSS(new File(System.getProperty("user.dir"), RES.CSS_PATH + RES.DEFAULT_CSS_FILE));
		
		// close preview
		openPreview(false);
		
	}

	//set CSS with .css file
	public void setCSS(File CSSFile){
		HTMLEditorKit kit = new HTMLEditorKit();
		CSSOMParser parser = new CSSOMParser();
		try {
			InputSource inputSource = new InputSource(CSSFile.toURI().toURL().toString());
			CSSStyleSheet cssStyleSheet = parser.parseStyleSheet(inputSource, null, null);
			CSSRuleList rules = cssStyleSheet.getCssRules();
			int size = rules.getLength();
			for (int i = 0; i < size; i++) {
				CSSRule rule = rules.item(i);
				kit.getStyleSheet().addRule(rule.getCssText());
			}
			preview.setEditorKit(kit);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		preview(editor.getText());
		
		CSSPath = CSSFile.getAbsolutePath();
		syncTitle();
	}

	//sync the title of the editor window
	public void syncTitle() {
		String CSSName = CSSPath.substring(CSSPath.lastIndexOf("\\") + 1);
		gui.setTitle(RES.EDITOR_NAME + " - " + ((path == "") ? RES.NEW_FILE_NAME : path));
	}

	//get the path of file edited now
	public String getPath() {
		return path;
	}

	//set the path
	public void setPath(String path) {
		this.path = path;
		syncTitle();
	}
	
	//get the content of CSS file from CSSPath
	public String getCSS() {
		BufferedReader in = null;
		StringBuilder sb = null;
		try {
			in = new BufferedReader(new FileReader(CSSPath));  
			String s;
			sb = new StringBuilder();
        	while ((s = in.readLine()) != null)  
                sb.append(s + "\n");  
            
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
        	try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return sb.toString();
	}

	//get the text of the editor
	public String getText() {

		return this.editor.getText();
	}

	//set text with invokelater to update UI
	public void setText(String text) {
		Runnable runSetText = new Runnable() {
		    public void run() {
		    	editor.setText(text);
		    	preview(text);
		    }
		};
		SwingUtilities.invokeLater(runSetText);
		
//		this.editor.setText(text);
//		//set text and preview
//		preview(text);
	}

	public void undo() {
		if(undoManager.canUndo()){
			undoManager.undo();
		}
	}

	public void redo() {
		if (undoManager.canRedo()){
			undoManager.redo();
		}
		
	}

	//if syncEditor is true, jump to line of the editor both editor and preview
	//else only sync the preview
	public void pageJump(int line, boolean syncEditor){

		Runnable runPageJump = new Runnable() {
		    public void run() {
		    	int max = editor.getLineCount();
				
		    	if(syncEditor){
		    		int em = editorScrollPane.getVerticalScrollBar().getMaximum();
					int ev = (int) ((em * ((line-1) * 1.0 / max * 1.0))); //(line == 1) ? 0 :
					editorScrollPane.getVerticalScrollBar().setValue(ev);
		    	}
				int pm = previewScrollPane.getVerticalScrollBar().getMaximum();
				int pv = (int) ((pm * ((line-1) * 1.0 / max * 1.0)));
				previewScrollPane.getVerticalScrollBar().setValue(pv);
		    }
		};
		SwingUtilities.invokeLater(runPageJump);

	}
	
	//set content of the preview
	private void preview(String text) {
		String html = new MarkdownProcessor().markdown(text);
		this.preview.setText(html);
		syncPreviewPosition();
	}
	
	//sync the position of preview
	private void syncPreviewPosition(){
		try {
			Rectangle rec = editor.modelToView(editor.getCaretPosition());
			if(rec != null){
				int line = rec.y / rec.height + 1;
				pageJump(line, false);
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isBlank(String str){
		if (null == str) {
			return true;
		}

		char[] chars = str.toCharArray();

		for (char ch : chars) {
			if (!Character.isWhitespace(ch)) {
				return false;
			}
		}
		return true;
	}
	
	//show or hide the preview
	public void openPreview(boolean open) {
		int width = this.getWidth();
		this.split.setPreferredSize(new Dimension(width, this.getHeight()));
		this.previewScrollPane.setVisible(open);
		if(open){
			this.split.setDividerLocation(width/2);
		}else{
			this.split.setDividerLocation(width);
			this.split.setOneTouchExpandable(true);
			this.split.setContinuousLayout(true);
		}
	}
}
