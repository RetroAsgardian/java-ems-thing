package cyou.keithhacks.ems;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.FTEmployeeData;
import ca.kbnt.ems.EmployeeManager.HashTable.IDInUseError;
import ca.kbnt.ems.EmployeeManager.PTEmployeeData;

public class AddEmployeeDialog extends JInternalFrame {
	private static final long serialVersionUID = 8799398506096026449L;

	Application app;
	EmployeeManager db;

	public AddEmployeeDialog(Application app, EmployeeManager db) {
		super("Create new...", false, true, false, false);

		this.app = app;
		this.db = db;

		build();

		this.pack();
		this.setVisible(true);
	}

	ButtonGroup group;
	JRadioButton partTime;
	JRadioButton fullTime;
	JCheckBox manualID;
	JSpinner manualIDField;

	JButton ok;
	JButton cancel;

	public void build() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.CENTER;
		con.fill = GridBagConstraints.HORIZONTAL;
		con.insets = new Insets(4, 4, 4, 4);
		con.gridx = 0;

		GridBagConstraints con2 = (GridBagConstraints) con.clone();
		con2.gridx = 1;

		group = new ButtonGroup();

		partTime = new JRadioButton("Part-time employee", false);
		fullTime = new JRadioButton("Full-time employee", true);

		group.add(partTime);
		group.add(fullTime);

		this.add(partTime, con);
		this.add(fullTime, con2);

		manualID = new JCheckBox("Manual ID", false);
		this.add(manualID, con);

		manualIDField = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
		this.add(manualIDField, con2);
		manualIDField.setEnabled(false);

		manualID.addChangeListener((ChangeEvent e) -> {
			manualIDField.setEnabled(manualID.isSelected());
		});

		ok = new JButton("OK");
		ok.addActionListener((ActionEvent e) -> {
			int id;
			if (manualID.isSelected())
				id = ((Integer) manualIDField.getValue()).intValue();
			else
				id = db.generateID();

			if (!db.checkVacantID(id)) {
				app.addWindow(new InfoDialog(app, "Error", "Employee ID is already taken"), true);
				return;
			}

			EmployeeData data;
			if (partTime.isSelected())
				data = new PTEmployeeData(id);
			else
				data = new FTEmployeeData(id);

			if (!db.checkVacantID(data.getID())) {
				app.addWindow(new InfoDialog(app, "Error", "Employee ID is already taken"), true);
				return;
			}

			app.addWindow(new EmployeeWindow(app, db, data));

			this.doDefaultCloseAction();
		});
		this.add(ok, con);

		cancel = new JButton("Cancel");
		cancel.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		this.add(cancel, con2);

		this.getRootPane().setDefaultButton(ok);
	}
}
