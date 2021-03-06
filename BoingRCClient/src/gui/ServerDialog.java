package gui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.net.ConnectException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import client.IRCClient;
import client.UserSettings;

public class ServerDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7072007691900916624L;

	private JTextField ip;
	private JTextField port;
	private JComboBox<UserSettings> userSettings;

	private JButton connect;
	private JButton cancel;

	public ServerDialog(Frame parent) {
		super(parent, "Connect to Server", true);
		setLayout(new GridLayout(4, 2));
		add(new JLabel("Server ip:"));
		add(ip = new JTextField("irc.twitch.tv"));
		add(new JLabel("Server port:"));
		add(port = new JTextField("6667"));
		add(new JLabel("User profile:"));
		add(userSettings = new JComboBox<UserSettings>(
				UserSettings.allSettings()));
		add(connect = new JButton("Connect"));
		add(cancel = new JButton("Cancel"));
		connect.addActionListener(this);
		cancel.addActionListener(this);
		pack();
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connect) {
			dispose();
			try {
				IRCClient.connectTo(ip.getText(),
						Integer.parseInt(port.getText()),
						(UserSettings) userSettings.getSelectedItem());
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this.getParent(),
						"The port must be a number between 0 and 65535",
						"Invalid port", JOptionPane.PLAIN_MESSAGE);
			} catch (UnknownHostException uhe) {
				uhe.printStackTrace();
				JOptionPane.showMessageDialog(this.getParent(),
						"Failed to find the host", "Unknown Host",
						JOptionPane.PLAIN_MESSAGE);
			} catch (ConnectException ce) {
				ce.printStackTrace();
				JOptionPane.showMessageDialog(this.getParent(),
						"Failed to establish a connection to " + ip.getText()
								+ " : " + port.getText(), "Connection failed",
						JOptionPane.PLAIN_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this.getParent(),
						"Uncaught Exception", "Uncaught exception: " + ex,
						JOptionPane.PLAIN_MESSAGE);
			}
		} else if (e.getSource() == cancel) {
			dispose();
		}
	}
}
