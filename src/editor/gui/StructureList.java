package editor.gui;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StructureList extends javax.swing.JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4169895830038035864L;
	
	private static final int MAX_WIDTH = 200;
	private static final int SCROLLPANE_WIDTH = 25;
	private static final String LIST_ELEMENT_PREFIX = " ";
	
	private JScrollPane listScrollPane;
	private JList<String> list;
	private DefaultListModel<String> listMode;
	private int[] lineNum;
	
	private GUI gui;
	private Editor editor;
	
	private String[] tag = { "h1", "h2", "h3", "h4", "h5" }; //for <h?>
	
	StructureList(GUI gui){
		super();
		this.gui = gui;
		editor = this.gui.getEditor();
		listScrollPane = new JScrollPane();
		initComponents();
	}
	
	private void initComponents(){
		this.setLayout(new BorderLayout());
		
		listMode = new DefaultListModel<String>();
		list = new JList<String>(listMode);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		listScrollPane.setPreferredSize(new Dimension(0, 0));
		
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				if(evt.getValueIsAdjusting()){
					int[] index = list.getSelectedIndices();
					pageJump(lineNum[index[0]]);
				}
			}
            
        });
		
		this.add(listScrollPane);
	}
	
	public void syncList(){
		
		Document doc = Jsoup.parse(editor.getText());
		Elements tmp = new Elements();
		Elements elm = new Elements();
		
		for(int i = 0; i < tag.length; i++){
			tmp = doc.select(tag[i]);
			elm.addAll(tmp);
		}

		listMode.clear();
		for (int  i= 0; i < elm.size(); i++){
			listMode.addElement(LIST_ELEMENT_PREFIX + elm.get(i).text());	  
		}
		
		list.updateUI();
	
		//
		lineNum = getLineNum();
		
		//
		listScrollPane.setViewportView(list);
		int width = (listMode.isEmpty()) ? 0 : list.getPreferredSize().width + SCROLLPANE_WIDTH;
		listScrollPane.setPreferredSize(new Dimension((width > MAX_WIDTH) ? MAX_WIDTH : width, gui.getHeight()));
		listScrollPane.updateUI();
		list.setBackground(gui.getBackground());
		this.updateUI();
	}
	
	private void pageJump(int line){
		gui.getEditor().pageJump(line, true);
	}
	
	private int[] getLineNum(){
		String html = editor.getText();
		
		int[] tagidx = new int[listMode.size()];
		
		for(int i = 0; i < listMode.size(); i++){
			tagidx[i] = (i == 0) ? 0 : tagidx[i-1] + 1;
			
			int[] tmpidx = new int[tag.length];
			for(int j = 0; j < tag.length; j++){
				tmpidx[j] = html.substring(tagidx[i]).indexOf("<" + tag[j] + ">");
				
				if( tmpidx[j] == -1 ){
					tmpidx[j] = Integer.MAX_VALUE;
				}
				
			}
			tagidx[i] += getMin(tmpidx);
		}
		
		int[] tagline = new int[listMode.size()];
	
		for(int i = 0; i < listMode.size(); i++){
			String subhtml = html.substring(
					(i == 0) ? 0 : tagidx[i-1] , tagidx[i]);
	        int offset = 0;
	        while((offset = subhtml.indexOf("\n", offset)) != -1){
	            offset = offset + "\n".length();
	            tagline[i]++;
	        }
	        tagline[i] += (i == 0) ? 1 : tagline[i-1];
	        //System.out.println( tagline[i] );
		}
		return tagline;
	}
	
	private int getMin(int[] arr){
		int min = Integer.MAX_VALUE;
		for(int i = 0; i < arr.length; i++) {  
            if(arr[i] < min)  
                min = arr[i];  
        } 
        return min;  
	}

}
