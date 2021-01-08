package cyou.keithhacks.ems;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class TestWindow extends JInternalFrame {
	private static final long serialVersionUID = -4617887620012898176L;

	public TestWindow() {
		super("Test Window", true, true, true, true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	void build() {
		this.add(new JLabel("beep boop testing"));
	}
	
}
