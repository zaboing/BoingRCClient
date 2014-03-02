package gui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowServerDialog implements ActionListener {
	private Frame window;
	
	public ShowServerDialog(Frame window) {
		this.window = window;
	}
	
	public void actionPerformed(ActionEvent e) {
		new ServerDialog(window).setVisible(true);
	}

}
