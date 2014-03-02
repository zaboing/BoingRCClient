package client;

import java.util.HashSet;

public class User implements Comparable<User> {
	public String userName;
	public char prefix;
	public String channel;
	
	private static HashSet<User> users = new HashSet<User>();
	
	public boolean equals(Object o) {
		if (!(o instanceof User)) {
			return false;
		}
		User u = (User) o;
		if (this == u) {
			return true;
		}
		return this.userName.equals(u.userName) && this.channel.equals(u.channel);
	}

	public int compareTo(User u) {
		if (u.prefix != this.prefix) {
			return -(u.prefix - this.prefix) / Math.abs(u.prefix - this.prefix); 
		}
		return u.userName.compareTo(this.userName);
	}
	public static User getUser(String userName, String channel) {
		User user = new User();
		user.userName = userName;
		user.channel = channel;
		for (User u : users) {
			if (u.equals(user)) {
				return u;
			}
		}
		users.add(user);
		return user;
	}
}
