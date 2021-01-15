package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;
import ca.kbnt.ems.EmployeeManager.FTEmployeeData;
import ca.kbnt.ems.EmployeeManager.PTEmployeeData;

public class EmployeePane extends JPanel {
	private static final long serialVersionUID = 7611825201239502843L;
	
	public static interface EditListener extends EventListener {
		public void editMade(EmployeeData data, boolean canSave);
	}
	
	EmployeeManager db;
	Employee employee;
	EmployeeData data;
	
	public EmployeePane(EmployeeManager db, Employee employee) {
		super();
		this.db = db;
		this.employee = employee;
		this.data = employee.getData();
		
		this.editListeners = new ArrayList<EditListener>();
		
		build();
	}
	
	protected void build() {
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
		
		buildCommon(con, con2);
		
		/*
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
			
		} else {
			this.add(new JLabel("ECLS"), con);
			this.add(new JLabel(employee.getClass().getName()), con2);
			
			this.add(new JLabel("DCLS"), con);
			this.add(new JLabel(data.getClass().getName()), con2);
		}
		*/
		
		this.add(new JLabel("Net salary"), con);
		
		this.add(new JLabel(Double.toString(data.calcAnnualNetIncome())), con2);

		
		this.add(new JLabel("Gross salary"), con);
		
		this.add(new JLabel(Double.toString(data.calcAnnualGrossIncome())), con2);
	}
	
	JTextField firstName;
	JTextField lastName;
	JComboBox<Gender> gender;
	JTextField location;
	JTextField deductRate;
	
	protected void buildCommon(GridBagConstraints con, GridBagConstraints con2) {
		this.add(new JLabel("First name"), con);
		
		firstName = new JTextField(data.getFirstName());
		firstName.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				notifyEditListeners(true);
			}
			public void removeUpdate(DocumentEvent e) {
				// Don't allow saving if field is blank
				notifyEditListeners(e.getDocument().getLength() > 0);
			}
			public void changedUpdate(DocumentEvent e) {
				notifyEditListeners(true);
			}
		});
		this.add(firstName, con2);
		
		
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
	}
	
	private ArrayList<EditListener> editListeners;
	
	public void addEditListener(EditListener l) {
		editListeners.add(l);
	}
	public void removeEditListener(EditListener l) {
		editListeners.remove(l);
	}
	
	private void notifyEditListeners(boolean canSave) {
		editListeners.forEach((EditListener l) -> {
			l.editMade(data, canSave);
		});
	}
	
	public void saveChanges() {
		employee.setData(data);
		notifyEditListeners(false);
	}
	
}
