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
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;

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
		con.gridwidth = GridBagConstraints.REMAINDER;
		
		group = new ButtonGroup();
		
		partTime = new JRadioButton("Part-time employee", false);
		fullTime = new JRadioButton("Full-time employee", true);
		
		group.add(partTime);
		group.add(fullTime);
		
		this.add(partTime, con);
		this.add(fullTime, con);
		
		manualID = new JCheckBox("Manual ID", false);
		this.add(manualID, con);
		
		manualIDField = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
		this.add(manualIDField, con);
		manualIDField.setEnabled(false);
		
		manualID.addChangeListener((ChangeEvent e) -> {
			manualIDField.setEnabled(manualID.isSelected());
		});
		
		ok = new JButton("OK");
		ok.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		this.add(ok, con);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		this.add(cancel, con);
		
		this.getRootPane().setDefaultButton(ok);
	}
}
