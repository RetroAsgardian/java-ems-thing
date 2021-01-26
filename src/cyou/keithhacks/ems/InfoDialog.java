package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class InfoDialog extends JInternalFrame {

	private static final long serialVersionUID = 6509806823394837612L;

	protected Application app;

	String text;

	public InfoDialog(Application app, String title, String text) {
		super(title, false, true, false, false);
		this.app = app;
		this.text = text;

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
		con.gridwidth = 1;
		con.weighty = 0.0;

		con.gridx = 0;
		JButton ok = new JButton("OK");
		ok.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		this.add(ok, con);
		getRootPane().setDefaultButton(ok);

	}

}
