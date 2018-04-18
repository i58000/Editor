package CSCW;

import java.io.Serializable;


/**
 *   There is only one member(String text), this class is extensible
 */
public class UserAction implements Serializable {
	
	private static final long serialVersionUID = -8722307943986185830L;

	private String text;
	
	public UserAction(String t){
		text = t;
	}
	
	public UserAction() {
		// TODO Auto-generated constructor stub
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
