package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ca.kbnt.ems.EmployeeManager.EmployeeManager;

public class StartupWindow extends JInternalFrame {
	private static final long serialVersionUID = -5422348334631470506L;
	
	Application app;
	public StartupWindow(Application app) {
		super("Welcome", true, false, true, false);
		this.app = app;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	JButton createNew;
	JButton openExisting;
	
	void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(4, 4, 4, 4);
		con.gridwidth = GridBagConstraints.REMAINDER;
		con.weightx = 1.0;
		con.weighty = 0.0;
		
		this.add(new JLabel("Please select an option:"), con);
		
		createNew = new JButton("Create a new database...");
		createNew.addActionListener((ActionEvent e) -> {
			EmployeeManager db = new EmployeeManager();
			
			// select a file to save to
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Save database as...");
			
			FileNameExtensionFilter dbFilter = new FileNameExtensionFilter("JavaEMS databases (*.edb)", "edb");
			chooser.addChoosableFileFilter(dbFilter);
			chooser.setFileFilter(dbFilter);
			
			int result = chooser.showSaveDialog(this);
			if (result != chooser.APPROVE_OPTION)
				return;
			
			File file = chooser.getSelectedFile();
			
			app.addWindow(new MainWindow(app, db, file));
			this.doDefaultCloseAction();
		});
		this.add(createNew, con);

		openExisting = new JButton("Open an existing database...");
		this.add(openExisting, con);
	}
	
}
