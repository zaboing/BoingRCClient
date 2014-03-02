package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowChannelDialog implements ActionListener {

	private IRCWindow frame;
	
	public ShowChannelDialog(IRCWindow frame) {
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		new ChannelDialog(frame).setVisible(true);
	}

}
