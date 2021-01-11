package cyou.keithhacks.ems;

import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MainWindow extends JInternalFrame {
	
	private static final long serialVersionUID = -8094233755987088018L;

	public MainWindow() {
		super("Employee Management System", true, false, true, true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		// this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu viewMenu;
	JMenu columnsMenu;
	
	void build() {
		menuBar = new JMenuBar();
		
		
	}
	
}
