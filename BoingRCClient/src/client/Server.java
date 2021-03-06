package client;

import gui.ChannelFrame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	public String ip;
	public int port;
	public UserSettings settings;

	public Socket socket;
	public BufferedWriter writer;
	public BufferedReader reader;
	public Thread receiveThread;

	public List<Channel> channels = new ArrayList<Channel>();

	public boolean connect() throws Exception {
		if (socket != null) {
			disconnect();
		}
		try {
			Proxy proxy = Proxy.NO_PROXY; 
			/*if (useProxy) {
				SocketAddress address = new InetSocketAddress("87.200.196.60", 80);
				proxy = new Proxy(Proxy.Type.SOCKS, address);
			}*/
			socket = new Socket(proxy);
			socket.connect(new InetSocketAddress(ip, port));
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			writer.write("PASS " + settings.pass);
			writer.newLine();
			writer.write("NICK " + settings.nick);
			writer.newLine();
			writer.write("USER " + settings.user + " 8 * : Java IRC Client by zaboing");
			writer.newLine();
			writer.flush();

			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if (line.indexOf("004") >= 0) {
					socket.setSoTimeout(5000);
					receiveThread = new Thread(this);
					receiveThread.setDaemon(true);
					receiveThread.start();
					return true;
				} else if (line.indexOf("433") >= 0) {
					System.out.println("Nickname is already in use.");
					return false;
				}
			}

			return false;
		} catch (Exception e) {
			throw e;
		}
	}

	public void run() {
		try {
			while (!socket.isClosed()) {
				String line;
				try {
					line = reader.readLine();
				} catch (SocketTimeoutException ste) {
					continue;
				}
				if (line == null) {
					break;
				}
				System.out.println(line);
				if (line.toLowerCase().startsWith("ping ")) {
					writer.write("PONG " + line.substring(5));
					writer.newLine();
					writer.flush();
				} else {
					int sender = 0;
					int type = line.indexOf(' ') + 1;
					int receiver = line.indexOf(' ', type) + 1;
					int message = line.indexOf(' ', receiver) + 1;
					String msgType = line.substring(type, receiver - 1);
					if ("PRIVMSG".equalsIgnoreCase(msgType)) {
						String channel = line.substring(receiver, message - 1);
						String userName = line.substring(sender + 1,
								line.indexOf('!'));
						String msg = line.substring(message + 1);
						getChannel(channel).addMessage(
								User.getUser(userName, channel), msg);
					} else if ("JOIN".equalsIgnoreCase(msgType)) {
						String channel = line.substring(receiver);
						String username = line.substring(sender + 1,
								line.indexOf('!'));
						getChannel(channel).addUser(
								User.getUser(username, channel));
					} else if ("PART".equalsIgnoreCase(msgType)) {
						String channel = line.substring(receiver);
						ChannelFrame cf = IRCClient.window.getChannel(line
								.substring(receiver));
						if (cf != null) {
							String username = line.substring(sender + 1,
									line.indexOf('!'));
							cf.channel.removeUser(User.getUser(username,
									channel));
						}
					} else if ("MODE".equalsIgnoreCase(msgType)) {
						String channel = line.substring(receiver, message - 1);
						String action = line.substring(message,
								line.indexOf(' ', message));
						String userName = line.substring(line.indexOf(' ',
								message) + 1);
						User user = User.getUser(userName, channel);
						if (action.equals("+o")) {
							user.prefix = '@';
						} else if (action.equals("-o")) {
							user.prefix = '\0';
						}
						ChannelFrame cf = IRCClient.window.getChannel(channel);
						if (cf != null) {
							cf.channel.markUsersDirty();
						}
					} else {
						System.out.println(line);
					}
				}
			}
		} catch (IOException e) {

		}
	}

	public void disconnect() {
		if (socket != null && socket.isConnected() && !socket.isClosed()) {
			if (writer != null) {
				try {
					writer.write(":tmi.twitch.tv QUIT :disconnected");
					writer.newLine();
					writer.flush();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (receiveThread != null) {
				try {
					receiveThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				receiveThread = null;
			}
			socket = null;
			writer = null;
			reader = null;
		}
	}

	public Channel join(String channel) {
		if (socket == null) {
			return null;
		}
		try {
			writer.write("JOIN " + channel);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Channel c = new Channel(this, channel);
		synchronized (channels) {
			channels.add(c);
		}
		return c;
	}

	public Channel getChannel(String channel) {
		synchronized (channels) {
			for (Channel c : channels) {
				if (c.channel.equals(channel)) {
					return c;
				}
			}
		}
		return join(channel);
	}
}
