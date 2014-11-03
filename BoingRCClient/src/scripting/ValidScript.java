package scripting;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

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
	
	public final boolean storeMap(String path, Map<String, String> map) {
		System.out.println("WRITING TO " + path);
		JSONObject json = new JSONObject(map);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
			writer.write(json.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public final boolean loadMap(String path, Result result) {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			StringBuilder builder = new StringBuilder();
			char[] buf = new char[2048];
			int len;
			while ((len = reader.read(buf)) != -1) {
				builder.append(buf, 0, len);
			}
			JSONObject json = new JSONObject(builder.toString());
			@SuppressWarnings("unchecked")
			Iterator<String> keys = json.keys();
			Map<String, String> map = new HashMap<String, String>();
			while (keys.hasNext()) {
				String key = keys.next();
				map.put(key, json.getString(key));
			}
			result.result = map;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
