package client;

import gui.IRCWindow;

import javax.swing.JFrame;
import javax.swing.UIManager;

import scripting.ScriptManager;

public class IRCClient {
	
	public static String nick;
	
	public static IRCWindow window;

	public static Server server;

	public static ScriptManager scriptManager = new ScriptManager();
	
	public static void main(String[] args) {
		createGui();
	}
	
	public static void connectTo(String ip, int port, UserSettings settings) throws Exception {
		if (server != null) {
			stop();
		}
		server = new Server();
		server.ip = ip;
		server.port = port;
		server.settings = settings;
		server.connect();
	}
	
	public static void stop() {
		if (server != null) {
			server.disconnect();
		}
		server = null;
	}

	private static void createGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		window = new IRCWindow();
		window.setVisible(true);
//		window.addChannel("#zaboing");
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	/*
		server = new Server();
		server.ip = "irc.twitch.tv";
		server.port = 6667;
		server.settings = new UserSettings("oauth:aibwpwcv31op3luc8ws9rkjxfdxz6x", "zaboting", "zaboting");
		server.connect();

	 */
}