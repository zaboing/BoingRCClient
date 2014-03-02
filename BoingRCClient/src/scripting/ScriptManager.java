package scripting;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import client.IRCClient;

public class ScriptManager {

	public File folder;
	public List<Script> scripts = new ArrayList<Script>();

	private long lastReload;

	public ScriptManager() {
		this("scripts");
	}

	public ScriptManager(String folder) {
		this(new File(folder));
	}

	public ScriptManager(File folder) {
		this.folder = folder;
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	public boolean reload() {
		if (folder.lastModified() != lastReload) {
			File[] fa = folder.listFiles();
			List<Script> scripts = new ArrayList<Script>();
			for (File f : fa) {
				if (f.isDirectory() || !f.getName().endsWith(".script")) {
					continue;
				}
				scripts.add(getScript(f));
			}
			lastReload = folder.lastModified();
			for (Script script : this.scripts) {
				if (script instanceof ValidScript) {
					((ValidScript) script).onUnload();
				}
			}
			this.scripts = scripts;
			for (Script script : this.scripts) {
				if (script instanceof ValidScript) {
					((ValidScript) script).onLoad();
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private static Script getScript(File f) {
		String name = f.getName();
		try {
			try {
				URL[] urls = new URL[] { f.getAbsoluteFile().getParentFile().getParentFile().toURI().toURL() };
				System.out.println(urls[0]);
				URLClassLoader classLoader = new URLClassLoader(urls, IRCClient.class.getClassLoader());
				
				Class<?> c = classLoader.loadClass("scripts."
						+ name.substring(0, name.lastIndexOf(".script")));
				classLoader.close();
				return (Script) c.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				return new InvalidScript(f.getName().substring(0,
						f.getName().lastIndexOf(".script")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new InvalidScript(f.getName());
		}
	}
}
