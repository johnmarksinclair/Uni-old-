package assignment2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Terminal {

	TerminalPanel panel;
	public String name;

	public class TerminalPanel extends JPanel implements ActionListener {

		private static final long serialVersionUID = -4404241756143559030L;
		protected JTextField textField;
		protected JTextArea textArea;
		private final static String newline = "\n";
		private JLabel label;
		private String input;

		public TerminalPanel() {
			super(new GridBagLayout());
			
			textField = new JTextField(30);
			textField.addActionListener(this);

			textArea = new JTextArea(10, 30);
			textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(textArea);

			GridBagConstraints c1 = new GridBagConstraints();
			c1.gridx = 0;
			c1.gridy = 0;
			c1.gridwidth = GridBagConstraints.REMAINDER;
			c1.fill = GridBagConstraints.BOTH;
			c1.weightx = 1.0;
			c1.weighty = 1.0;
			add(scrollPane, c1);
			
			label = new JLabel("");
			GridBagConstraints c2 = new GridBagConstraints();
			c2.gridx = 0;
			c2.gridy = 1;
			add(label, c2);
			
			GridBagConstraints c3 = new GridBagConstraints();
			c3.fill = GridBagConstraints.HORIZONTAL;
			c3.gridy = 1;
			c3.gridx = 1;
			add(textField, c3);
		}

		public synchronized void actionPerformed(ActionEvent evt) {
			input = textField.getText();
			textField.selectAll();
			textField.setText("");
			textArea.setCaretPosition(textArea.getDocument().getLength());		
			notify();
		}

		public void setPrompt(String prompt) {
			label.setText(prompt);
		}
		
		public void print(String output) {
			textArea.append(output);
		}
		
		public void println(String output) {
			textArea.append(output + newline);
		}
		
		public synchronized String read() {
			textField.setEditable(true);
			try {
				wait();
			}
			catch(Exception e) {e.printStackTrace();}
			textField.setEditable(false);
			return input;
		}
	}

	public Terminal(String name) {
		this.name = name;
		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel= new TerminalPanel());
		frame.setVisible(true);
		frame.pack();
	}	
	
	public void print(String output) {
		panel.print(output);
	}
	
	public void println(String output) {
		panel.println(output);
	}
	
	public synchronized String read(String prompt) {
		String input;
		panel.setPrompt(prompt);
		input = panel.read();
		panel.setPrompt("");
		return input;
	}
}
