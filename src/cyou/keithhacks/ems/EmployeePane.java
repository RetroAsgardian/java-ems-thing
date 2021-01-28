package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentEvent;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;
import ca.kbnt.ems.EmployeeManager.FTEmployeeData;
import ca.kbnt.ems.EmployeeManager.PTEmployeeData;

public class EmployeePane extends JPanel {
	private static final long serialVersionUID = 7611825201239502843L;

	public static interface EditListener extends EventListener {
		public void editMade(EmployeeData data, boolean canSave);
	}

	Application app;
	EmployeeData data;
	boolean dataEdited = false;

	public EmployeePane(Application app, EmployeeData data) {
		super();
		this.app = app;
		this.data = data;
		this.editListeners = new ArrayList<EditListener>();

		build();
	}

	public void rebuild(EmployeeData data) {
		this.data = data;
		this.dataEdited = false;
		this.removeAll();
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

		if (data instanceof FTEmployeeData) {
			FTEmployeeData ftdata = (FTEmployeeData) data;

			this.add(new JLabel("Yearly salary"), con);

			JSpinner yearlySalary = new JSpinner(new SpinnerNumberModel(ftdata.getYearlySalary(), 0.0, null, 100));
			yearlySalary.addChangeListener((ChangeEvent e) -> {
				ftdata.setYearlySalary(((Double) yearlySalary.getModel().getValue()).doubleValue());
				notifyEditListeners(validateState());
			});

			this.add(yearlySalary, con2);

		} else if (data instanceof PTEmployeeData) {
			PTEmployeeData ptdata = (PTEmployeeData) data;

			this.add(new JLabel("Hourly wage"), con);

			JSpinner hourlyWage = new JSpinner(new SpinnerNumberModel(ptdata.getHourlyWage(), 0.0, null, 0.5));
			hourlyWage.addChangeListener((ChangeEvent e) -> {
				ptdata.setHourlyWage(((Double) hourlyWage.getModel().getValue()).doubleValue());
				notifyEditListeners(validateState());
			});

			this.add(hourlyWage, con2);

			this.add(new JLabel("Hours per week"), con);

			JSpinner hoursPerWeek = new JSpinner(new SpinnerNumberModel(ptdata.getHoursPerWeek(), 0.0, null, 1));
			hoursPerWeek.addChangeListener((ChangeEvent e) -> {
				ptdata.setHoursPerWeek(((Double) hoursPerWeek.getModel().getValue()).doubleValue());
				notifyEditListeners(validateState());
			});

			this.add(hoursPerWeek, con2);

			this.add(new JLabel("Weeks per year"), con);

			JSpinner weeksPerYear = new JSpinner(new SpinnerNumberModel(ptdata.getWeeksPerYear(), 0.0, null, 1));
			weeksPerYear.addChangeListener((ChangeEvent e) -> {
				ptdata.setWeeksPerYear(((Double) weeksPerYear.getModel().getValue()).doubleValue());
				notifyEditListeners(validateState());
			});

			this.add(weeksPerYear, con2);

		} else {
			// this.add(new JLabel("ECLS"), con);
			// this.add(new JLabel(employee.getClass().getName()), con2);

			// this.add(new JLabel("DCLS"), con);
			// this.add(new JLabel(data.getClass().getName()), con2);
		}

		this.add(new JLabel("Net salary"), con);

		netSalary = new JTextField(NumberFormat.getCurrencyInstance().format(data.calcAnnualNetIncome()));
		netSalary.setEditable(false);
		netSalary.setEnabled(false);

		this.add(netSalary, con2);

		this.add(new JLabel("Gross salary"), con);

		grossSalary = new JTextField(NumberFormat.getCurrencyInstance().format(data.calcAnnualGrossIncome()));
		grossSalary.setEditable(false);
		grossSalary.setEnabled(false);

		this.add(grossSalary, con2);

		this.addEditListener((data, canSave) -> {
			netSalary.setText(NumberFormat.getCurrencyInstance().format(data.calcAnnualNetIncome()));
			grossSalary.setText(NumberFormat.getCurrencyInstance().format(data.calcAnnualGrossIncome()));
		});

		this.addEditListener((data, canSave) -> {
			this.dataEdited = true;
		});
	}

	JTextField firstName;
	JTextField lastName;
	JComboBox<Gender> gender;
	JTextField location;
	JSpinner deductRate;

	JTextField netSalary;
	JTextField grossSalary;

	protected void buildCommon(GridBagConstraints con, GridBagConstraints con2) {
		this.add(new JLabel("First name"), con);

		firstName = new JTextField(data.getFirstName());
		firstName.getDocument().addDocumentListener(new SimpleDocListener((DocumentEvent e) -> {
			data.setFirstName(firstName.getText());
			notifyEditListeners(validateState());
		}));
		this.add(firstName, con2);

		this.add(new JLabel("Last name"), con);

		lastName = new JTextField(data.getLastName());
		lastName.getDocument().addDocumentListener(new SimpleDocListener((DocumentEvent e) -> {
			data.setLastName(lastName.getText());
			notifyEditListeners(validateState());
		}));
		this.add(lastName, con2);

		this.add(new JLabel("Gender"), con);

		gender = new JComboBox<Gender>(Gender.class.getEnumConstants());
		gender.setSelectedItem(data.getGender());
		gender.addItemListener((ItemEvent e) -> {
			if (e.getStateChange() != ItemEvent.SELECTED)
				return;

			data.setGender((Gender) gender.getSelectedItem());
			notifyEditListeners(validateState());
			/*
			if (data.getGender() == Gender.Male || data.getGender() == Gender.Female) {
				app.addWindow(
						new InfoDialog(app, "Warning",
								"Male and Female are deprecated genders, and will be removed in a future release."),
						true);
			}
			*/
		});
		this.add(gender, con2);

		this.add(new JLabel("Location"), con);

		location = new JTextField(data.getLocation());
		location.getDocument().addDocumentListener(new SimpleDocListener((DocumentEvent e) -> {
			data.setLocation(location.getText());
			notifyEditListeners(validateState());
		}));
		this.add(location, con2);

		this.add(new JLabel("Deduct rate"), con);

		JPanel deductRatePanel = new JPanel();
		deductRatePanel.setLayout(new GridBagLayout());
		this.add(deductRatePanel, con2);

		GridBagConstraints dcon = new GridBagConstraints();
		dcon.gridx = 0;
		dcon.gridy = 0;
		dcon.fill = GridBagConstraints.HORIZONTAL;
		dcon.weightx = 1.0;

		deductRate = new JSpinner(new SpinnerNumberModel(data.getDeductRate() * 100, 0, 100, 0.1));
		deductRate.addChangeListener((ChangeEvent e) -> {
			data.setDeductRate(((Double) deductRate.getModel().getValue()).doubleValue() / 100);
			notifyEditListeners(validateState());
		});
		deductRatePanel.add(deductRate, dcon);

		dcon = (GridBagConstraints) dcon.clone();
		dcon.gridx = 1;
		dcon.fill = GridBagConstraints.NONE;
		dcon.weightx = 0.0;
		deductRatePanel.add(new JLabel(" %"), dcon);
	}

	public boolean validateState() {
		if (firstName.getDocument().getLength() <= 0)
			return false;
		if (lastName.getDocument().getLength() <= 0)
			return false;

		if (location.getDocument().getLength() <= 0)
			return false;

		if (data instanceof PTEmployeeData) {
			if (((PTEmployeeData) data).getHourlyWage() < 15.0)
				return false;
		}

		return true;
	}

	public boolean getEdited() {
		return dataEdited;
	}

	protected ArrayList<EditListener> editListeners;

	public void addEditListener(EditListener l) {
		editListeners.add(l);
	}

	public void removeEditListener(EditListener l) {
		editListeners.remove(l);
	}

	protected void notifyEditListeners(boolean canSave) {
		editListeners.forEach((EditListener l) -> {
			l.editMade(data, canSave);
		});
	}

	public void saveChanges(Employee employee) {
		employee.setData(data);
		notifyEditListeners(false);
		this.dataEdited = false;
	}

}
