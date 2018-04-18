package editor.gui.Listener;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;

import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
//import org.docx4j.org.apache.poi.poifs.filesystem.POIFSFileSystem;


import editor.RES;

import editor.gui.Editor;
import editor.gui.GUI;



public class FileMenuActionListener implements ActionListener {
	private GUI gui;
	
	public FileMenuActionListener(GUI gui) {
		super();
		this.gui = gui;
	}
	
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ("newFile".equals(command)) {
			this.newFile();
		} else if ("openFile".equals(command)) {
			this.openFile();
		} else if ("saveFile".equals(command)) {
			this.saveFile();
		} else if ("saveAs".equals(command)) {
			this.saveAs();
		} else if("exportAsDoc".equals(command)){
			this.exportAsDoc();
		} else if ("exit".equals(command)) {
			this.exit();
		}
	}
	
	/**
	 * export as doc or docx
	 */
	private void exportAsDoc() {
		Editor editor = gui.getEditor();
		
		String body = editor.getText();
		String css = editor.getCSS();
		//auto add body tag in order to generate a normal word file
		String content = "<html><head><style>" + css + "</style></head><body>" + body + "</body></html>";
		
		//file chooser
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle(RES.filechooser_saveas);
		String[] postfix = { "doc", "docx" };
		chooser.setFileFilter(new FileNameExtensionFilter("Word Files", postfix));
		int state = chooser.showSaveDialog(gui);
		if (state == JFileChooser.APPROVE_OPTION) {
			File chooseFile = chooser.getSelectedFile();
			
			String fname = chooser.getName(chooseFile);  
			//if the file name user input is not match the postfix, add the postfix  
		    if(!postfixMatch(fname, postfix)){
		    	chooseFile = new File(chooser.getCurrentDirectory(), fname + ".doc");
		    }
			
			if (!chooseFile.exists()) {
				try {
					chooseFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			exportAsDoc(content, chooseFile);
		}
	}
	
	@SuppressWarnings("deprecation")
	private void exportAsDoc(String content, File file) {
		try {
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

	        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
	        wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
	        ndp.unmarshalDefaultNumbering();

	        XHTMLImporterImpl xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
	        xHTMLImporter.setHyperlinkStyle("Hyperlink");

	        File tmp = File.createTempFile("pattern", ".suffix");
	        tmp.deleteOnExit();
	        saveFile(tmp, content);
	        wordMLPackage.getMainDocumentPart().getContent()
	                .addAll(xHTMLImporter.convert(tmp, null));//new File("C:/Users/ANJINSHUO/Documents/eclipse3/Editor/res/test.html")

	        System.out.println(XmlUtils.marshaltoString(wordMLPackage
	                .getMainDocumentPart().getJaxbElement(), true, true));

	        wordMLPackage.save(file);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Docx4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//byte b[] = content.getBytes();  
		//bais = new ByteArrayInputStream(b);  
		//poifs = new POIFSFileSystem();  
		//DirectoryEntry directory = poifs.getRoot();  
		//directory.createDocument("WordDocument", bais);
		//fos = new FileOutputStream(chooseFile);
		//poifs.writeFilesystem(fos);
		//fos.close();
		//bais.close();
		//poifs.close(); 
	}
	
	/**
	 * exit
	 */
	private void exit() {
		gui.dispose();
		System.exit(0);
	}


	/**
	 * save as
	 */
	private void saveAs() {
		Editor editor = this.gui.getEditor();		
		if (null == editor) {
			return;
		}
		String content = editor.getText();

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle(RES.filechooser_saveas);
		String[] postfix = { "md", "txt", "markdown", "mdown", "htm", "html" };
		chooser.setFileFilter(new FileNameExtensionFilter("Markdown/HTML Files", postfix));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", new String[] { "txt" }));
			
		int state = chooser.showSaveDialog(gui);
		if (state == JFileChooser.APPROVE_OPTION) {
			File chooseFile = chooser.getSelectedFile();
			
			String fname = chooser.getName(chooseFile);  
	        //if the file name user input is not match the postfix, add the postfix 
	        if(!postfixMatch(fname, postfix)){
	        	chooseFile = new File(chooser.getCurrentDirectory(), fname + ".md");
	        }
	        
			if (!chooseFile.exists()) {
				try {
					chooseFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			editor.setPath(chooseFile.getAbsolutePath());
			saveFile(chooseFile, content);
		}
	
	}

	/**
	 * save file
	 */
	private void saveFile() {
		Editor editor = this.gui.getEditor();
		if (null == editor) {
			return;
		}

		if (editor.isBlank(editor.getPath())) {
			// 还没有保存过文件
			saveAs();
		} 
		else {
			// 文件已经保存过
			File file = new File(editor.getPath());
			if (!file.exists()) { // 如果中途把文件已经删除则重新创建文件
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 获取文件内容
			saveFile(file, editor.getText());
		}
	}

	/**
	 * new file
	 */
	private void newFile() {
		if(!disposeFile()){
			return;
		}
		
		Editor editor = this.gui.getEditor();
		editor.setText("");
		editor.setPath("");
	}

	/**
	 * open file
	 */
	private void openFile() {
		
		if(!disposeFile()){
			return;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// 只能选择文件
		chooser.setDialogTitle(RES.filechooser_open);
		chooser.setFileFilter(new FileNameExtensionFilter("Markdown Files",
				new String[] { "md", "txt", "markdown", "mdown" }));
		chooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"Text Files", new String[] { "txt" }));
		
		int state = chooser.showOpenDialog(gui);
		if (state == JFileChooser.APPROVE_OPTION) {
			File chooseFile = chooser.getSelectedFile();
			String path = chooseFile.getAbsolutePath();
			Editor editor = gui.getEditor();
			
			if(!path.equals(editor.getPath())){ //is not the current file
				String content = readFile(chooseFile);
				editor.setText(content);
				editor.setPath(chooseFile.getAbsolutePath());
			}
			else{
				//is the current file
			}
		}

	}
	
	
	/**
	 * deal with the current opened file
	 */
	private boolean disposeFile(){ //save or delete current file
		Editor editor = this.gui.getEditor();

		if(editor.isBlank(editor.getPath()) && editor.isBlank(editor.getText())){ //new file and blank text
			return true;
		}
		else {  //ask whether save
			int state = JOptionPane.showConfirmDialog(null, RES.confirmdialog_text_savecurrent, RES.confirmdialog_title_savecurrent, JOptionPane.YES_NO_OPTION);
			if(state == JOptionPane.CLOSED_OPTION){
				return false;
			}
			else if(state == JOptionPane.OK_OPTION){
				saveFile();
			}
		}
		return true;
	
	}

	/**
	 * read file
	 */
	private String readFile(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			StringBuilder sb = new StringBuilder();
			String str;
			while ((str = reader.readLine()) != null) {
				sb.append(str);
				sb.append("\r\n");
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * save file
	 */
	private void saveFile(File file, String content) {
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file));
			writer.write(content);
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * whether the file name match the postfix
	 */
	private boolean postfixMatch(String name, String[] postfix){
		for(String pf : postfix){
			if(name.indexOf("."+pf) != -1){
				return true;
			}
		}
		return false;
		
	}
}
