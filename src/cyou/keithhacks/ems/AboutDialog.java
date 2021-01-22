package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class AboutDialog extends JInternalFrame {

	private static final long serialVersionUID = 7749287266703179108L;

	public AboutDialog() {
		super("About", false, true, false, false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		this.setVisible(true);
	}
	
	void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(8, 8, 8, 8);
		con.gridx = 0;
		con.gridwidth = GridBagConstraints.REMAINDER;
		
		this.add(new JLabel("JavaEMS"), con);
		this.add(new JLabel("By Keith and Kyle Benton"), con);
		this.add(new JLabel("Made in 2021"), con);
		this.add(new JLabel("Copyleft - all wrongs reserved"), con);
		/*
		this.add(new JLabel("By using this software, you certify that " +
							"you are not Anish Kapoor, affiliated with " +
							"Anish Kapoor, or using it on his behalf."), con);
		*/
	}
	
}
