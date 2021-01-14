package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TextInputDialog extends JInternalFrame {
	
	private static final long serialVersionUID = 6509806823394837612L;
	
	protected Application app;
	
	String text;
	
	Consumer<String> action;
	
	public TextInputDialog(Application app, String title, String text, Consumer<String> action) {
		super(title, false, true, false, false);
		this.app = app;
		this.text = text;
		this.action = action;

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		this.setVisible(true);
	}
	
	void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.BOTH;
		con.insets = new Insets(4, 4, 4, 4);
		con.gridwidth = GridBagConstraints.REMAINDER;
		con.weightx = 1.0;
		con.weighty = 1.0;
		con.ipady = 8;
		
		this.add(new JLabel(text), con);
		
		con.ipady = 0;
		con.insets = new Insets(0, 4, 4, 4);
		con.weighty = 0.0;
		
		con.gridx = 0;
		JTextField textField = new JTextField();
		this.add(textField, con);
		 
		con.gridwidth = 1;
		con.gridx = 0;
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		this.add(cancel, con);
		
		con.gridx = GridBagConstraints.RELATIVE;
		JButton ok = new JButton("OK");
		ok.addActionListener((ActionEvent e) -> {
			this.action.accept(textField.getText());
			this.doDefaultCloseAction();
		});
		this.add(ok, con);
	}
	
}
