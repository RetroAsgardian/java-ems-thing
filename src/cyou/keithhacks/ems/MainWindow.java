package cyou.keithhacks.ems;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import cyou.keithhacks.ems.testdata.DataThingy;
import cyou.keithhacks.ems.testdata.EmployeeInfo;

public class MainWindow extends JInternalFrame {
	private static final long serialVersionUID = -7039777370572673794L;
	
	Random random;
	
	public MainWindow() {
		super("Employee Management System", true, false, true, true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		random = new Random();
		
		this.pack();
		// this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	JLabel employeeCount;
	
	JButton addEmployee;
	JButton createWindow;
	
	JScrollPane tableScroll;
	JTable table;
	
	void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(4, 4, 4, 4);
		con.gridx = 0;
		con.gridwidth = GridBagConstraints.REMAINDER;
		con.weightx = 0.0;
		con.weighty = 0.0;
		
		employeeCount = new JLabel("Employees: " + Integer.toString(DataThingy.get().employees.size()));
		this.add(employeeCount, con);
		
		con.gridwidth = 1;
		addEmployee = new JButton("Add employee");
		addEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// generate random id, ensure it's not in the DB
				int id;
				do {
					id = random.nextInt(999999);
				} while (DataThingy.get().employees.get(id) != null);
				
				DataThingy.get().employees.put(id, new EmployeeInfo(id, "Test", "Person", "No", 0, 0.1));
				
				// Update employee count
				employeeCount.setText("Employees: " + Integer.toString(DataThingy.get().employees.size()));
				
				// Update table
				((AbstractTableModel) table.getModel()).fireTableDataChanged();
			}
		});
		this.add(addEmployee, con);
		
		con.gridx = GridBagConstraints.RELATIVE;
		createWindow = new JButton("Dump to stdout");
		createWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO create window
				System.out.println(DataThingy.get().employees.toString());
			}
		});
		this.add(createWindow, con);
		
		con.fill = GridBagConstraints.BOTH;
		con.weightx = 1.0;
		con.weighty = 1.0;
		con.gridx = 0;
		con.gridwidth = GridBagConstraints.REMAINDER;
		
		table = new JTable(new AbstractTableModel() {
			// FIXME this is a huge mess
			ArrayList<Integer> keys = DataThingy.get().employees.keys();
			public void fireTableDataChanged() {
				keys = DataThingy.get().employees.keys();
				super.fireTableDataChanged();
			}
			
			Class<?>[] columnClasses = {int.class, String.class, String.class, String.class, int.class, double.class};
			String[] columnNames = {"ID", "First Name", "Last Name", "Gender", "Location", "Deduct Rate"};
			
			public int getColumnCount() {
				return columnClasses.length;
			}
			
			public Class<?> getColumnClass(int column) {
				if (column >= 0 && column < columnClasses.length)
					return columnClasses[column];
				return Object.class;
			}
			
			public String getColumnName(int column) {
				if (column >= 0 && column < columnNames.length)
					return columnNames[column];
				return "Column " + Integer.toString(column);
			}

			public int getRowCount() {
				return keys.size();
			}

			public Object getValueAt(int row, int column) {
				// TODO Auto-generated method stub
				EmployeeInfo e = DataThingy.get().employees.get(keys.get(row));
				
				switch (column) {
				case 0:
					return e.getID();
				case 1:
					return e.getFirstName();
				case 2:
					return e.getLastName();
				case 3:
					return e.getGender();
				case 4:
					return e.getLocationID();
				case 5:
					return e.getDeductRate();
				}
				
				return null;
			}
			
		});
		tableScroll = new JScrollPane(table);
		tableScroll.setPreferredSize(new Dimension(
				tableScroll.getPreferredSize().width,
				table.getPreferredSize().height
		));
		
		this.add(tableScroll, con);
	}
	
}
