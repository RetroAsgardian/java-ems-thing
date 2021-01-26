package cyou.keithhacks.ems;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangeOperation;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangedEvent;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangedEventListener;
import cyou.keithhacks.ems.ActionDialog.Button;

public class EmployeeWindow extends JInternalFrame {

	private static final long serialVersionUID = 6509806823394837612L;

	protected Application app;
	EmployeeManager db;
	Employee employee;
	
	private class EmployeeListener implements DataChangedEventListener {
		EmployeeWindow win;
		public EmployeeListener(EmployeeWindow win) {
			this.win = win;
		}
		public void onChange(DataChangedEvent event) {
			if (event.operation == DataChangeOperation.Modified) {
				if (event.newData.getID() == employee.getID()) {
					if (save.isEnabled())
						app.addWindow(new ActionDialog(
								app,
								"Employee modified",
								"<html>Employee " + Integer.toString(employee.getID()) + " was modified while it was open.<br/>" +
								"Reload data? <b>Your changes will be lost.</b></html>",
								(ActionEvent e) -> {
									pane.rebuild();
								},
								Button.Ok
						), true);
					else
						pane.rebuild();
				}
			} else if (event.operation == DataChangeOperation.Removed) {
				if (event.oldData.getID() == employee.getID()) {
					win.doDefaultCloseAction();
				}
			}
		}
	}
	
	public EmployeeWindow(Application app, EmployeeManager db, Employee employee) {
		super("Employee " + Integer.toString(employee.getID()), true, true, true, true);
		this.app = app;
		this.db = db;
		this.employee = employee;

		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		EmployeeListener listener = new EmployeeListener(this);
		
		db.addDataChangedListener(listener);
		
		this.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosed(InternalFrameEvent e) {
				db.removeDataChangedListener(listener);
			}
		});

		build();

		this.pack();
		this.setMinimumSize(this.getSize());
		this.setVisible(true);
	}
	
	EmployeePane pane;
	
	JButton cancel;
	JButton save;
	JButton delete;
	
	void build() {
		this.setLayout(new BorderLayout());
		
		pane = new EmployeePane(app, db, employee);
		this.add(pane, BorderLayout.CENTER);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new FlowLayout());
		this.add(bottom, BorderLayout.SOUTH);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener((ActionEvent e) -> {
			this.doDefaultCloseAction();
		});
		bottom.add(cancel);
		
		save = new JButton("Save");
		save.setEnabled(false);
		save.addActionListener((ActionEvent e) -> {
			save.setEnabled(false);
			pane.saveChanges();
			this.doDefaultCloseAction();
		});
		bottom.add(save);
		
		delete = new JButton("Delete");
		delete.addActionListener((ActionEvent e) -> {
			app.addWindow(new ActionDialog(
					app,
					"Confirm deletion",
					"<html>Really delete employee " + Integer.toString(employee.getID()) + "? <b>This action cannot be undone.</b></html>",
					(ActionEvent e1) -> {
						db.removeEmployee(employee);
						this.doDefaultCloseAction();
					},
					ActionDialog.Button.Cancel
			), true);
		});
		bottom.add(delete);
		
		pane.addEditListener((data, canSave) -> {
			save.setEnabled(canSave);
		});
	}

}
