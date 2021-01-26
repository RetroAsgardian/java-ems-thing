package cyou.keithhacks.ems;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;
import cyou.keithhacks.ems.ActionDialog.Button;
import cyou.keithhacks.ems.query.DoubleQueryClause;
import cyou.keithhacks.ems.query.Query;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.FTEmployeeData;
import ca.kbnt.ems.EmployeeManager.PTEmployeeData;

public class MainWindow extends JInternalFrame {

	private static final long serialVersionUID = -8094233755987088018L;

	Application app;

	EmployeeManager db;

	public MainWindow(Application app) {
		super("Employee Management System", true, false, true, true);
		this.app = app;

		db = new EmployeeManager();

		Employee emp = db.newEmployee();
		FTEmployeeData data = new FTEmployeeData(emp.getData());
		data.setFirstName("Firstname");
		data.setLastName("Lastname");
		data.setGender(Gender.No);
		data.setDeductRate(0.2);
		data.setYearlySalary(10000.0);
		emp.setData(data);

		emp = db.newEmployee();
		emp.setData(new PTEmployeeData(emp.getID()));

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
	EmployeeTableModel tableModel;

	void build() {
		tableModel = new EmployeeTableModel(db);
		table = new JTable(tableModel);

		buildMenuBar();

		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(4, 4, 4, 4);
		con.weightx = 0.0;
		con.weighty = 0.0;

		addEmployeeBtn = new JButton("Add employee...");
		addEmployeeBtn.addActionListener((ActionEvent e) -> {
			app.addWindow(new AddEmployeeDialog(app, db), true);
		});
		this.add(addEmployeeBtn, con);

		findByIDBtn = new JButton("Find by ID...");
		findByIDBtn.addActionListener((ActionEvent e) -> {
			doFindByID();
		});
		this.add(findByIDBtn, con);

		searchBtn = new JButton("Search...");
		searchBtn.addActionListener((ActionEvent e) -> {
			Query q = new Query();
			q.clauses.add(new DoubleQueryClause(DoubleQueryClause.QueryField.NetSalary, DoubleQueryClause.QueryType.GreaterOrEqual, 10000));
			q.clauses.add(new DoubleQueryClause(DoubleQueryClause.QueryField.NetSalary, DoubleQueryClause.QueryType.Less, 20000));
			tableModel.setQuery(q);
		});
		this.add(searchBtn, con);

		showAllBtn = new JButton("Show all");
		showAllBtn.addActionListener((ActionEvent e) -> {
			tableModel.clearQuery();
		});
		this.add(showAllBtn, con);

		con.gridx = 0;
		con.gridwidth = GridBagConstraints.REMAINDER;
		con.fill = GridBagConstraints.BOTH;
		con.weightx = 1.0;
		con.weighty = 1.0;

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
					int row = table.rowAtPoint(e.getPoint());
					if (row < 0)
						return;

					app.addWindow(new EmployeeWindow(app, db, db.getEmployee(tableModel.getIDAt(row))));
				}
			}
		});

		table.setAutoCreateRowSorter(true);

		tablePane = new JScrollPane(table);
		tablePane.setPreferredSize(new Dimension(tablePane.getPreferredSize().width, table.getPreferredSize().height));
		this.add(tablePane, con);
	}
	
	private void doFindByID() {
		app.addWindow(new TextInputDialog(app, "Find by ID", "Enter the desired employee's ID: ", (String str) -> {
			try {
				int ID = Integer.parseInt(str);
				Employee employee = db.getEmployee(ID);

				if (employee == null)
					app.addWindow(new InfoDialog(app, "Error", "Employee not found"), true);
				else
					app.addWindow(new EmployeeWindow(app, db, employee));
			} catch (NumberFormatException e1) {
				app.addWindow(new InfoDialog(app, "Error", "Invalid ID"), true);
			}
		}), true);
	}

	JMenuBar menuBar;
	JMenu fileMenu;
	JMenu editMenu;
	JMenu viewMenu;
	JMenu columnsMenu;

	void buildMenuBar() {
		JMenuItem item;
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem("New..."));
		fileMenu.add(new JMenuItem("Export..."));

		editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		
		item = new JMenuItem("Add employee...");
		item.addActionListener((ActionEvent e) -> {
			app.addWindow(new AddEmployeeDialog(app, db), true);
		});
		editMenu.add(item);
		
		item = new JMenuItem("Find by ID...");
		item.addActionListener((ActionEvent e) -> {
			doFindByID();
		});
		editMenu.add(item);

		viewMenu = new JMenu("View");
		menuBar.add(viewMenu);

		columnsMenu = new JMenu("Columns");
		viewMenu.add(columnsMenu);

		buildColumnsMenu();

		viewMenu.addSeparator();

		viewMenu.add(new JMenuItem("Search..."));
		
		item = new JMenuItem("Show all");
		item.addActionListener((ActionEvent e) -> {
			tableModel.clearQuery();
		});
		viewMenu.add(item);
	}

	void buildColumnsMenu() {
		columnsMenu.removeAll();

		JCheckBoxMenuItem column;

		for (int i = 0; i < EmployeeTableModel.allColumns.length; i++) {
			column = new JCheckBoxMenuItem(EmployeeTableModel.allColumns[i], tableModel.getColumnVisibility(i));
			final int n = i;
			column.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					tableModel.setColumnVisibility(n, e.getStateChange() == ItemEvent.SELECTED);
				}
			});
			columnsMenu.add(column);
		}
	}

}
