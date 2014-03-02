package gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScriptOpener implements ActionListener {

	private Frame parent;
	private String scriptname;
	
	public ScriptOpener(Frame parent, String scriptname) {
		this.parent = parent;
		this.scriptname = scriptname;
	}
	
	public void actionPerformed(ActionEvent e) {
		new ScriptEditor(parent, scriptname).setVisible(true);
	}

}
