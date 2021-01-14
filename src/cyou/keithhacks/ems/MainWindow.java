package cyou.keithhacks.ems;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ca.kbnt.ems.EmployeeManager.EmployeeManager;

public class MainWindow extends JInternalFrame {
	
	private static final long serialVersionUID = -8094233755987088018L;
	
	Application app;
	
	EmployeeManager db;

	public MainWindow(Application app) {
		super("Employee Management System", true, false, true, true);
		this.app = app;
		
		db = new EmployeeManager();
		
		db.newFTEmployee();
		db.newFTEmployee();
		db.newPTEmployee();
		db.newPTEmployee();
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		// HACK gotta do this to make the table not be 0 pixels tall
		this.setSize(this.getSize().width, this.getSize().height + 120);
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	JButton addEmployeeBtn;
	JButton findByIDBtn;
	JButton searchBtn;
	JButton showAllBtn;
	
	JScrollPane tablePane;
	JTable table;
	
	void build() {
		buildMenuBar();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(4, 4, 4, 4);
		con.weightx = 0.0;
		con.weighty = 0.0;
		
		addEmployeeBtn = new JButton("Add employee...");
		this.add(addEmployeeBtn, con);
		
		findByIDBtn = new JButton("Find by ID...");
		findByIDBtn.addActionListener((ActionEvent e) -> {
			app.addWindow(new TextInputDialog(app, "Find by ID", "Enter the desired employee's ID: ", (String str) -> {
				app.addWindow(new ActionDialog(app, "Debug", str, (ActionEvent e1) -> {}), true);
			}), true);
		});
		this.add(findByIDBtn, con);
		
		searchBtn = new JButton("Search...");
		this.add(searchBtn, con);
		
		showAllBtn = new JButton("Show all");
		this.add(showAllBtn, con);
		
		con.gridx = 0;
		con.gridwidth = GridBagConstraints.REMAINDER;
		con.fill = GridBagConstraints.BOTH;
		con.weightx = 1.0;
		con.weighty = 1.0;
		
		table = new JTable();
		
		tablePane = new JScrollPane(table);
		tablePane.setPreferredSize(new Dimension(
				tablePane.getPreferredSize().width,
				table.getPreferredSize().height
		));
		this.add(tablePane, con);
	}
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu editMenu;
	JMenu viewMenu;
	JMenu columnsMenu;
	
	void buildMenuBar() {
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem("New"));
		fileMenu.add(new JMenuItem("Open..."));
		fileMenu.add(new JMenuItem("Save"));
		
		fileMenu.addSeparator();
		
		fileMenu.add(new JMenuItem("Export..."));
		
		editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		editMenu.add(new JMenuItem("Undo"));
		editMenu.add(new JMenuItem("Redo"));
		editMenu.add(new JMenuItem("Changelog..."));
		
		editMenu.addSeparator();

		editMenu.add(new JMenuItem("Add employee..."));
		editMenu.add(new JMenuItem("Find by ID..."));
		
		viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		
		columnsMenu = new JMenu("Columns");
		viewMenu.add(columnsMenu);
		
		buildColumnsMenu();
		
		viewMenu.addSeparator();
		
		viewMenu.add(new JMenuItem("Search..."));
		viewMenu.add(new JMenuItem("Show all"));
	}
	
	void buildColumnsMenu() {
		columnsMenu.removeAll();

		JCheckBoxMenuItem column;
		
		column = new JCheckBoxMenuItem("ID", true);
		columnsMenu.add(column);
		
		column = new JCheckBoxMenuItem("First name", true);
		columnsMenu.add(column);
		
		column = new JCheckBoxMenuItem("Last name", true);
		columnsMenu.add(column);
		
		column = new JCheckBoxMenuItem("Gender", false);
		columnsMenu.add(column);
		
		column = new JCheckBoxMenuItem("Location", true);
		columnsMenu.add(column);
		
		column = new JCheckBoxMenuItem("Gross salary", true);
		columnsMenu.add(column);
		
		column = new JCheckBoxMenuItem("Net salary", false);
		columnsMenu.add(column);
	}
	
}
