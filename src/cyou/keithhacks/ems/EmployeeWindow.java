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
import ca.kbnt.ems.EmployeeManager.Employee.Gender;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;

public class EmployeeWindow extends JInternalFrame {
	
	private static final long serialVersionUID = 6509806823394837612L;
	
	protected Application app;
	EmployeeManager db;
	Employee employee;
	
	public EmployeeWindow(Application app, EmployeeManager db, Employee employee) {
		super("Employee " + Integer.toString(employee.getID()), true, true, true, true);
		this.app = app;
		this.db = db;
		this.employee = employee;

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
		
		this.add(new JTextField(employee.getData().getFirstName()), con2);
		
		this.add(new JLabel("Last name"), con);

		this.add(new JTextField(employee.getData().getLastName()), con2);
		
		this.add(new JLabel("Gender"), con);
		
		JComboBox<Gender> gender = new JComboBox<Gender>(Gender.class.getEnumConstants());
		gender.setSelectedItem(employee.getData().getGender());
		this.add(gender, con2);
		
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
