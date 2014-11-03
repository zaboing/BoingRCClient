package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class ScriptEditor extends JDialog implements ActionListener {
	private static final long serialVersionUID = -1628247328221104094L;

	private String pluginHead;
	private String pluginTail;

	private JTextArea codeArea;
	private JButton apply;
	private String scriptName;

	public ScriptEditor(Frame frame, String scriptName) {
		super(frame, "Script Editor");
		this.scriptName = scriptName;
		this.codeArea = new JTextArea();
		this.setLayout(new BorderLayout(0, 0));
		this.add(codeArea, BorderLayout.CENTER);
		this.apply = new JButton("Apply");
		this.apply.addActionListener(this);
		this.add(apply, BorderLayout.SOUTH);
		insertScript();
		pluginHead = "package scripts;\r\n"
				+ "import client.*;\r\n"
				+ "import gui.*;\r\n"
				+ "import scripting.*;\r\n"
				+ "public class " + scriptName + " extends ValidScript {\r\n"
				+ "\tpublic " + scriptName + "() {\r\n"
				+ "\t\tsuper(\"" + scriptName + "\");\r\n"
				+ "\t}\r\n";
		pluginTail = "}";
	}

	private void insertScript() {
		File f = new File("scripts/" + scriptName + ".script");
		if (f.exists() && f.isFile()) {
			try {
				Reader reader = new BufferedReader(new FileReader(f));

				StringBuilder file = new StringBuilder();
				char[] charbuf = new char[1024];
				int len;
				while ((len = reader.read(charbuf)) != -1) {
					file.append(charbuf, 0, len);
				}
				codeArea.setText(file.toString());

				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveScript() {
		try {
			Writer writer = new BufferedWriter(new FileWriter("scripts/" + scriptName + ".script"));
			writer.write(codeArea.getText().toCharArray());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		File scriptDir = new File("scripts");
		scriptDir.mkdirs();
		saveScript();
		try {
			File javaSrcFile = new File(scriptDir, scriptName + ".java");
			Writer p = new FileWriter(javaSrcFile);
			String script = pluginHead + codeArea.getText() + pluginTail;
			p.write(script);
			p.close();
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler
					.getStandardFileManager(null, null, null);
			Iterable<? extends JavaFileObject> units;
			units = fileManager.getJavaFileObjectsFromFiles(Arrays
					.asList(javaSrcFile));
			CompilationTask task = compiler.getTask(null, fileManager, null,
					null, null, units);
			task.call();
			fileManager.close();
			javaSrcFile.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
