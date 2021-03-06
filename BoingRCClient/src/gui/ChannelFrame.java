package gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import scripting.InvalidScript;
import scripting.Script;
import scripting.ValidScript;
import client.Channel;
import client.IRCClient;
import client.User;

public class ChannelFrame extends JInternalFrame implements ActionListener,
		Runnable, Comparator<User>, HyperlinkListener {
	private static final long serialVersionUID = 2148429019002341497L;

	private IRCWindow ircWindow;
	private JEditorPane messages;
	private JScrollPane history;
	private JTextArea userList;
	private JTextField inputField;
	private JMenuBar menuBar;
	private JMenu tools;
	private JMenu scripts;
	private Thread communicationThread;

	public Channel channel;

	public User bot;

	private static final String urlRegex = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
	private static final Pattern urlPattern = Pattern.compile(urlRegex);
	
	public ChannelFrame(IRCWindow ircWindow, Channel channel) {
		super(channel.channel);
		menuBar = new JMenuBar();
		tools = new JMenu("Tools");
		scripts = new JMenu("Scripts");

		tools.add(scripts);
		menuBar.add(tools);
		setJMenuBar(menuBar);
		this.ircWindow = ircWindow;
		this.channel = channel;
		this.messages = new JEditorPane();
		this.history = new JScrollPane(messages);
		this.userList = new JTextArea();
		this.inputField = new JTextField();
		this.setSize(640, 480);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.setIconifiable(true);
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.messages.setContentType("text/html");
		this.messages.setEditable(false);
		this.messages.setText(getDefaultHTMLTemplate());
		this.messages.setFont(new Font("Monospaced", Font.PLAIN, 13));
		this.messages.addHyperlinkListener(this);
		this.history.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(history);
		this.userList.setEditable(false);
		this.userList.setFont(new Font("Monospaced", Font.PLAIN, 13));
		this.userList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(userList);
		this.inputField.addActionListener(this);
		this.inputField.setFont(new Font("Monospaced", Font.PLAIN, 13));
		this.inputField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(inputField);
		this.communicationThread = new Thread(this);
		this.communicationThread.start();
		this.bot = User
				.getUser(IRCClient.server.settings.nick, channel.channel);
		reshape();
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				reshape();
			}
		});
	}

	@Override
	public void run() {
		while (true) {
			synchronized (ircWindow) {
				if (!ircWindow.isVisible()) {
					break;
				}
			}
			if (channel.areUsersDirty()) {
				userList.setText("");
				for (User user : channel.users) {
					userList.append("  " + user.prefix + user.userName + "\r\n");
				}
				channel.cleanUsers();
			}
			if (channel.areMessagesDirty()) {
				synchronized (channel.newMessages) {
					Object[] msg;
					while ((msg = channel.newMessages.poll()) != null) {
						User user = (User) msg[0];
						appendLine("<" + (user.prefix == 0 ? "" : user.prefix)
								+ user.userName + "> " + msg[1]);
					}
				}
				channel.cleanMessages();
			}
		}
	}

	private void reshape() {
		int left = getInsets().left, right = getInsets().right, top = getInsets().top
				+ menuBar.getHeight(), bot = getInsets().bottom;
		this.history.setSize(getWidth() - left - right - 150, getHeight() - top
				- bot - 55);
		this.userList.setLocation(getWidth() - 150 - right - left, 0);
		this.userList.setSize(150, history.getHeight());
		this.inputField.setLocation(0, history.getHeight() + 5);
		this.inputField.setSize(getWidth() - left - right, 25);
	}

	private void appendLine(String line) {
		line = line.replace("&", "&amp;").replace("<", "&lt;")
				.replace(">", "&gt;").replace("\"", "&quot;");
		appendRawLine(line);
	}

	private void appendRawLine(String line) {
		final int oldValue = this.history.getHorizontalScrollBar().getValue();
		Matcher matcher = urlPattern.matcher(line);
		while (matcher.find()) {
			String found = matcher.group();
			line = line.replaceFirst(found, "<a href='" + found + "'>" + found + "</a>");
		}
		line = "<div align=\"left\">" + line + "</div></br>";
		HTMLDocument doc = (HTMLDocument) this.messages.getDocument();
		try {
			doc.insertBeforeEnd(doc.getElement(doc.getDefaultRootElement(),
					StyleConstants.NameAttribute, HTML.Tag.BODY), line);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.messages.setDocument(doc);
		this.messages.setCaretPosition(doc.getLength());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ChannelFrame.this.history.getHorizontalScrollBar().setValue(
						oldValue);
			}
		});
		reshape();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		channel.sendMessage(arg0.getActionCommand());
		inputField.setText(new String());
	}

	public static String getDefaultHTMLTemplate() {
		StringBuilder html = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"default_html.html"));
			char[] buf = new char[1024];
			int len;
			while ((len = br.read(buf)) != -1) {
				html.append(buf, 0, len);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return html.toString();
	}

	public int compare(User u1, User u2) {
		if (u1.equals(bot)) {
			return 1;
		}
		if (u2.equals(bot)) {
			return -1;
		}
		return u1.compareTo(u2);
	}

	public void refreshScripts() {
		scripts.removeAll();
		for (final Script script : IRCClient.scriptManager.scripts) {
			JMenuItem item = new JMenuItem(script.getName());
			if (script instanceof InvalidScript) {
				continue;
			} else {
				if (channel.activeScripts.contains(script)) {
					item.setEnabled(false);
				} else {
					item.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							channel.registerScript((ValidScript) script);
							appendRawLine("<div class='notification'>Enabled script "
									+ script.getName() + "</div>");
						}
					});
				}
			}
			scripts.add(item);
		}
	}

	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(e.getURL().toURI());
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
