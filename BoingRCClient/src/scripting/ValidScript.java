package scripting;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import client.Channel;
import client.User;

public abstract class ValidScript implements Script {
	
	private final String name;
	
	public ValidScript(String name) {
		this.name = name;
	}
	
	public void receiveMessage(Channel channel, User user, String message) {
		
	}
	
	public void chat(Channel channel, String message) {
		channel.sendMessage(message);
	}
	
	public void onLoad() {
		
	}
	
	public void onUnload() {
		
	}
	
	public void onRegister(Channel channel) {
		
	}
	
	public void onUnregister(Channel channel) {
		
	}
	
	public final String getName() {
		return name;
	}
	
	public final boolean serialize(Serializable object, String path) {
		File file = new File(path);
		file.getParentFile().mkdirs();
		try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
			oos.writeObject(object);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public final boolean deserialize(String path, Result result) {
		try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)))) {
			result.result = ois.readObject();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
