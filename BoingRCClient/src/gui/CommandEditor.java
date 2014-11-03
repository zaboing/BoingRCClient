package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CommandEditor implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		try {
			new ProcessBuilder(new String[] { "JSONEditor.exe",
					"#zaboing/cmds.json", "#zaboing/aliases.json" }).start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
