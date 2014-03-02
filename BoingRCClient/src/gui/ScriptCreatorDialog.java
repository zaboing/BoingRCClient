package gui;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ScriptCreatorDialog extends JDialog implements ActionListener {
	
	private static final long serialVersionUID = 8250171230006750726L;

	private Frame frame;
	
	private JTextField scriptname;
	private JButton create;
	private JButton cancel;
	
	public ScriptCreatorDialog(Frame frame) {
		super(frame, true);
		this.frame = frame;
		setLayout(new GridLayout(2, 2));
		add(new JLabel("Script name:"));
		add(scriptname = new JTextField());
		add(create = new JButton("Create"));
		add(cancel = new JButton("Cancel"));
		create.addActionListener(this);
		cancel.addActionListener(this);
		pack();
		setLocationRelativeTo(null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == create) {
			dispose();
			new ScriptEditor(frame, scriptname.getText()).setVisible(true);		
		} else if (e.getSource() == cancel) {
			dispose();
		}
	}
}
