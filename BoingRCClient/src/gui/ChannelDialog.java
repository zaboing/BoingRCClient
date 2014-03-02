package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ChannelDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 732509695919926573L;

	private JTextField channel;
	private JButton add;
	private JButton cancel;
	private IRCWindow window;
	
	public ChannelDialog(IRCWindow frame) {
		super(frame, "Add Channel", true);
		this.window = frame;
		setLayout(new GridLayout(2, 2));
		add(new JLabel("Channel name"));
		add(channel = new JTextField());
		add(add = new JButton("Add Channel"));
		add(cancel = new JButton("Cancel"));
		add.addActionListener(this);
		cancel.addActionListener(this);
		pack();
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancel) {
			dispose();
		} else if (e.getSource() == add) {
			window.addChannel(channel.getText());
			dispose();
		}
	}
}
