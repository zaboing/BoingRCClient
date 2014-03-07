package gui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import client.UserSettings;

public class ProfileDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -1832308613230551091L;
	
	private JTextField profileName;
	private JTextField login;
	private JTextField pass;
	private JTextField nick;
	
	private JButton confirm;
	private JButton cancel;
	
	public ProfileDialog(Frame frame) {
		super(frame, "Add Profile", true);
		setLayout(new GridLayout(5, 2));
		add(new JLabel("Profile name:"));
		add(profileName = new JTextField());
		add(new JLabel("Login name:"));
		add(login = new JTextField());
		add(new JLabel("Password:"));
		add(pass = new JTextField());
		add(new JLabel("Nick name:"));
		add(nick = new JTextField());
		add(confirm = new JButton("Confirm"));
		add(cancel = new JButton("Cancel"));
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		pack();
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == confirm) {
			new UserSettings(profileName.getText(), pass.getText(), nick.getText(), login.getText());
			dispose();
		} else if (e.getSource() == cancel) {
			dispose();
		}
	}

}
