package gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScriptCreator implements ActionListener {

	private Frame frame;
	
	public ScriptCreator(Frame frame) {
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		new ScriptCreatorDialog(frame).setVisible(true);
	}
}
