package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ca.kbnt.ems.DataFiles.FileData;
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
		this.setSize(getSize().width, getSize().height + 16);
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
			chooser.setAcceptAllFileFilterUsed(true);
			chooser.setFileFilter(dbFilter);
			
			int result = chooser.showSaveDialog(this);
			if (result != JFileChooser.APPROVE_OPTION)
				return;
			
			File file = chooser.getSelectedFile();
			
			if (file.exists()) {
				app.addWindow(new InfoDialog(app, "Error", "Cannot overwrite an existing database."), true);
				return;
			}
			
			// save the initial db
			try {
				file.createNewFile();
				FileOutputStream fh = new FileOutputStream(file, false);
				FileData.writeData(fh, db);
				fh.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				app.addWindow(new InfoDialog(app, "Error", "Unable to create database."), true);
				return;
			}
			
			app.addWindow(new MainWindow(app, db, file));
			this.doDefaultCloseAction();
		});
		this.add(createNew, con);

		openExisting = new JButton("Open an existing database...");
		openExisting.addActionListener((ActionEvent e) -> {
			// select a file to open
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Open database...");
			
			FileNameExtensionFilter dbFilter = new FileNameExtensionFilter("JavaEMS databases (*.edb)", "edb");
			chooser.addChoosableFileFilter(dbFilter);
			chooser.setAcceptAllFileFilterUsed(true);
			chooser.setFileFilter(dbFilter);
			
			int result = chooser.showOpenDialog(this);
			if (result != JFileChooser.APPROVE_OPTION)
				return;
			
			File file = chooser.getSelectedFile();
			
			// open the db
			EmployeeManager db;
			try {
				file.createNewFile();
				FileInputStream fh = new FileInputStream(file);
				db = new EmployeeManager(FileData.loadData(fh));
				fh.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				app.addWindow(new InfoDialog(app, "Error", "Unable to open database."), true);
				return;
			}
			
			app.addWindow(new MainWindow(app, db, file));
			this.doDefaultCloseAction();
		});
		this.add(openExisting, con);
	}
	
}
