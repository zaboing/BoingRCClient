package gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowProfileDialog implements ActionListener {

	private Frame frame;
	
	public ShowProfileDialog(Frame frame) {
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		new ProfileDialog(frame).setVisible(true);
	}

}
