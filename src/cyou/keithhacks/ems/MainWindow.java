package cyou.keithhacks.ems;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class MainWindow extends JInternalFrame {
	
	private static final long serialVersionUID = -8094233755987088018L;

	public MainWindow() {
		super("Employee Management System", true, false, true, true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	JButton newEmployeeBtn;
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
		
		newEmployeeBtn = new JButton("New employee...");
		this.add(newEmployeeBtn, con);
		
		findByIDBtn = new JButton("Find by ID...");
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
	JMenu viewMenu;
	JMenu columnsMenu;
	
	void buildMenuBar() {
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		viewMenu = new JMenu("View");
		menuBar.add(viewMenu);
		
		columnsMenu = new JMenu("Columns");
		viewMenu.add(columnsMenu);
		
		this.setJMenuBar(menuBar);
	}
	
}
