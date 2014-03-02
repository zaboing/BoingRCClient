package client;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import scripting.ValidScript;

public class Channel {
	
	public String channel;
	public Server server;
	public Vector<User> users = new Vector<User>();
	public Vector<Object[]> messages = new Vector<Object[]>();
	public Queue<Object[]> newMessages = new ArrayDeque<Object[]>();
	
	public List<ValidScript> activeScripts = new ArrayList<ValidScript>();
	
	
	private boolean areUsersDirty = false;
	private boolean areMessagesDirty = false;
	
	public Channel(Server server, String channel) {
		this.server = server;
		this.channel = channel;
	}

	public void addUser(User user) {
		users.add(user);
		Collections.sort(users);
		markUsersDirty();
	}
	
	public void removeUser(User user) {
		users.remove(user);
		Collections.sort(users);
		markUsersDirty();
	}
	
	public void markUsersDirty() {
		areUsersDirty = true;
	}
	
	public boolean areUsersDirty() {
		return areUsersDirty;
	}
	
	public void cleanUsers() {
		areUsersDirty = false;
	}
	
	public void registerScript(ValidScript script) {
		activeScripts.add(script);
		script.onRegister(this);
	}
	
	public void unregisterScript(ValidScript script) {
		if (activeScripts.remove(script)) {
			script.onUnregister(this);
		}
	}
	
	public void addMessage(User user, String msg) {
		addMessage(user, msg, true);
	}
	
	public void addMessage(User user, String msg, boolean notifyScripts) {
		messages.add(new Object[] {user, msg});
		synchronized (newMessages) {
			newMessages.add(new Object[] {user, msg});
		}
		if (notifyScripts) {
			for (ValidScript activeScript : activeScripts) {
				activeScript.receiveMessage(this, user, msg);
			}
		}
		markMessagesDirty();
	}
	
	public void markMessagesDirty() {
		areMessagesDirty = true;
	}
	
	public boolean areMessagesDirty() {
		return areMessagesDirty;
	}
	
	public void cleanMessages() {
		areMessagesDirty = false;
	}
	
	public void sendMessage(String input) {
		if (input == null || input.isEmpty()) {
			return;
		}
		synchronized (server.writer) {
			System.out.println(server.socket.isClosed());
			try {
				if (input.startsWith("/")) {
					if (input.equals("/list")) {
						String s = ":tmi.twitch.tv NAMES " + channel;
						server.writer.write(s);
						System.out.println(s);
						server.writer.newLine();
					}
				} else {
					server.writer.write("PRIVMSG " + channel + " :"
							+ input);
					server.writer.newLine();
				}
				server.writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		addMessage(User.getUser(IRCClient.server.settings.nick, channel), input, false);
	}
	
	public String name() {
		return channel.substring(1);
	}
}
