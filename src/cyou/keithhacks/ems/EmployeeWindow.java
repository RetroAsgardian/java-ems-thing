package cyou.keithhacks.ems;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import ca.kbnt.ems.EmployeeManager.Employee;
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
	
	EmployeePane pane;
	
	void build() {
		this.setLayout(new BorderLayout());
		
		pane = new EmployeePane(db, employee);
		this.add(pane, BorderLayout.CENTER);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		this.add(bottom, BorderLayout.SOUTH);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		bottom.add(cancel);
		
		JButton save = new JButton("Save");
		save.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		bottom.add(save);
	}

}
