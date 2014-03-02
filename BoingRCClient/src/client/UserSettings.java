package client;

import java.util.Vector;

public class UserSettings {
	public static Vector<UserSettings> allSettings = new Vector<UserSettings>();
	
	public String name;
	public String pass;
	public String nick;
	public String user;
	
	public UserSettings(String name, String pass, String nick, String user) {
		this.name = name;
		this.pass = pass;
		this.nick = nick;
		this.user = user;
		allSettings.add(this);
	}

	public String toString() {
		return name;
	}
	
	public static Vector<UserSettings> allSettings() {
		return allSettings;
	}
	
	static {
		new UserSettings("Bot", "oauth:aibwpwcv31op3luc8ws9rkjxfdxz6x", "zaboting", "zaboting");
	}
}
