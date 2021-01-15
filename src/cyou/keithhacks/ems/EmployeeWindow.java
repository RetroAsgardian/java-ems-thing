package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.Employee.EmployeeData;
import ca.kbnt.ems.EmployeeManager.Employee.Gender;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.FullTimeEmployee.FTEmployeeData;
import ca.kbnt.ems.EmployeeManager.PartTimeEmployee.PTEmployeeData;

public class EmployeeWindow extends JInternalFrame {
	
	private static final long serialVersionUID = 6509806823394837612L;
	
	protected Application app;
	EmployeeManager db;
	Employee employee;
	EmployeeData data;
	
	public EmployeeWindow(Application app, EmployeeManager db, Employee employee) {
		super("Employee " + Integer.toString(employee.getID()), true, true, true, true);
		this.app = app;
		this.db = db;
		this.employee = employee;
		this.data = employee.getData();

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		build();
		
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.BOTH;
		con.insets = new Insets(4, 4, 4, 4);
		
		GridBagConstraints con2 = (GridBagConstraints) con.clone();
		
		con.gridx = 0;
		con.weightx = 0.0;
		con.fill = GridBagConstraints.NONE;
		con.anchor = GridBagConstraints.EAST; 
		
		con2.gridx = 1;
		con2.weightx = 1.0; 
		
		this.add(new JLabel("First name"), con);
		
		this.add(new JTextField(data.getFirstName()), con2);
		
		
		this.add(new JLabel("Last name"), con);

		this.add(new JTextField(data.getLastName()), con2);
		
		
		this.add(new JLabel("Gender"), con);
		
		JComboBox<Gender> gender = new JComboBox<Gender>(Gender.class.getEnumConstants());
		gender.setSelectedItem(data.getGender());
		this.add(gender, con2);
		
		
		this.add(new JLabel("Location"), con);
		
		this.add(new JTextField(data.getLocation()), con2);
		
		
		this.add(new JLabel("Deduct rate"), con);
		
		this.add(new JTextField(Double.toString(data.getDeductRate())), con2);
		
		
		if (data instanceof FTEmployeeData) {
			FTEmployeeData ftdata = (FTEmployeeData) data;
			
			this.add(new JLabel("Yearly salary"), con);
			
			this.add(new JTextField(Double.toString(ftdata.getYearlySalary())), con2);
			
		} else if (data instanceof PTEmployeeData) {
			PTEmployeeData ptdata = (PTEmployeeData) data;
			
			this.add(new JLabel("Hourly wage"), con);
			
			this.add(new JTextField(Double.toString(ptdata.getHourlyWage())), con2);
			
			
			this.add(new JLabel("Hours per week"), con);
			
			this.add(new JTextField(Double.toString(ptdata.getHoursPerWeek())), con2);
			
			
			this.add(new JLabel("Weeks per year"), con);
			
			this.add(new JTextField(Double.toString(ptdata.getWeeksPerYear())), con2);
			
		}
		
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		this.add(cancel, con);
		
		JButton save = new JButton("Save");
		save.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		this.add(save, con2);
	}
	
}
