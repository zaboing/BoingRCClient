package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.Border;

import scripting.Script;
import client.IRCClient;

public class IRCWindow extends JFrame implements ComponentListener,
		WindowListener {
	private static final long serialVersionUID = -4119499608301296668L;

	private JPanel toolPanel;
	private JPanel channelOverview;
	private JPanel mainPanel;
	private ArrayList<ChannelFrame> channels;
	private JMenuBar menuBar;
	private JMenu server;
	private JMenuItem addProfile;
	private JMenuItem connect;
	private JMenu channel;
	private JMenuItem addChannel;
	private JMenu scripts;
	private JMenuItem createScript;
	private JMenu editScript;
	private JMenuItem reloadScripts;

	public IRCWindow() {
		super("IRC Client");
		setLayout(null);
		toolPanel = new JPanel();
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		toolPanel.setBorder(border);
		toolPanel.setBackground(Color.WHITE);
		add(toolPanel);
		channelOverview = new JPanel();
		channelOverview.setBorder(border);
		channelOverview.setBackground(Color.WHITE);
		add(channelOverview);
		mainPanel = new JPanel();
		mainPanel.setBorder(border);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(null);
		add(mainPanel);
		channels = new ArrayList<ChannelFrame>();
		addComponentListener(this);

		menuBar = new JMenuBar();
		server = new JMenu("Server");
		addProfile = new JMenuItem("Add Profile");
		addProfile.addActionListener(new ShowProfileDialog(this));
		connect = new JMenuItem("Connect to Server");
		connect.addActionListener(new ShowServerDialog(this));
		channel = new JMenu("Channel");
		addChannel = new JMenuItem("Add Channel", KeyEvent.VK_A);
		addChannel.addActionListener(new ShowChannelDialog(this));
		scripts = new JMenu("Scripts");
		createScript = new JMenuItem("Create Script");
		createScript.addActionListener(new ScriptCreator(this));
		editScript = new JMenu("Edit Script");
		reloadScripts = new JMenuItem("Reload");
		reloadScripts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registerScripts();
			}
		});
		// registerScripts();
		server.add(addProfile);
		server.add(connect);
		channel.add(addChannel);
		scripts.add(createScript);
		scripts.add(editScript);
		editScript.add(reloadScripts);
		menuBar.add(server);
		menuBar.add(channel);
		menuBar.add(scripts);
		setJMenuBar(menuBar);

		addWindowListener(this);
	}

	private void registerScripts() {
		if (IRCClient.scriptManager.reload()) {
			editScript.removeAll();
			for (Script script : IRCClient.scriptManager.scripts) {
				JMenuItem item = new JMenuItem(script.getName());
				item.addActionListener(new ScriptOpener(this, script.getName()));
				editScript.add(item);
			}
			editScript.add(new JSeparator());
			editScript.add(reloadScripts);
			for (ChannelFrame channel : channels) {
				channel.refreshScripts();
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		toolPanel.setLocation(0, 0);
		toolPanel.setSize(getContentPane().getWidth(), 100);
		channelOverview.setLocation(0, 100);
		channelOverview.setSize(150, getContentPane().getHeight());
		mainPanel
				.setLocation(channelOverview.getWidth(), toolPanel.getHeight());
		mainPanel.setSize(
				getContentPane().getWidth() - channelOverview.getWidth(),
				getContentPane().getHeight() - toolPanel.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	public void addChannel(String channel) {
		ChannelFrame cf = new ChannelFrame(this,
				IRCClient.server.getChannel(channel));
		cf.setName(channel);
		mainPanel.add(cf);
		channels.add(cf);
	}

	public ChannelFrame getChannel(String channel) {
		for (ChannelFrame cf : channels) {
			if (cf.getName().equals(channel)) {
				return cf;
			}
		}
		return null;
	}

	public void printChannel(String channel, String message) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		IRCClient.stop();
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	// public static void main(String[] args) {
	// try {
	// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// IRCWindow window = new IRCWindow();
	// ChannelFrameCommunicator c;
	// window.addChannel("#zaboing", c = new ChannelFrameCommunicator() {
	//
	// @Override
	// public void userInput(String input) {
	// System.out.println(input);
	// }
	//
	// });
	// c.messageQueue.add("<za\"oing> Hello & Hi!");
	// window.setVisible(true);
	// window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	// window.setExtendedState(MAXIMIZED_BOTH);
	// }
}
