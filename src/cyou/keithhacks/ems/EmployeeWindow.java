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
import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangeOperation;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangedEvent;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangedEventListener;
import ca.kbnt.ems.EmployeeManager.HashTable.IDInUseError;
import cyou.keithhacks.ems.ActionDialog.Button;

public class EmployeeWindow extends JInternalFrame {

	private static final long serialVersionUID = 6509806823394837612L;

	protected Application app;
	EmployeeManager db;
	EmployeeData data;

	private class EmployeeListener implements DataChangedEventListener {
		EmployeeWindow win;

		public EmployeeListener(EmployeeWindow win) {
			this.win = win;
		}

		public void onChange(DataChangedEvent event) {
			if (event.operation == DataChangeOperation.Modified) {
				if (event.newData.getID() == data.getID()) {
					if (db.checkVacantID(data.getID())) {
						delete.setEnabled(false);
					} else {
						delete.setEnabled(true);
					}
					if (pane.getEdited())
						app.addWindow(new ActionDialog(app, "Employee modified",
								"<html>Employee " + Integer.toString(data.getID())
										+ " was modified while it was open.<br/>"
										+ "Reload data? <b>Your changes will be lost.</b></html>",
								(ActionEvent e) -> {
									data = event.newData.clone();
									pane.rebuild(data);
									save.setEnabled(false);
									win.pack();
									win.setMinimumSize(win.getSize());
								}, Button.Ok), true);
					else {
						data = event.newData.clone();
						pane.rebuild(data);
						save.setEnabled(false);
						win.pack();
						win.setMinimumSize(win.getSize());
					}
				}
			} else if (event.operation == DataChangeOperation.Removed) {
				if (event.oldData.getID() == data.getID()) {
					win.doDefaultCloseAction();
				}
			}
		}
	}

	public EmployeeWindow(Application app, EmployeeManager db, EmployeeData data) {
		super("Employee " + Integer.toString(data.getID()), true, true, true, true);
		this.data = data;
		this.app = app;
		this.db = db;

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

		pane = new EmployeePane(app, data);
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
			Employee employee;
			if (db.checkVacantID(data.getID())) {
				employee = new Employee(data);
				try {
					db.addEmployee(employee);
				} catch (IDInUseError e1) {
					// This should never happen
					e1.printStackTrace();
				}
			} else {
				employee = db.getEmployee(data.getID());
			}
			pane.saveChanges(employee);
		});
		bottom.add(save);

		delete = new JButton("Delete");

		if (db.checkVacantID(data.getID())) {
			delete.setEnabled(false);
		} else {
			delete.setEnabled(true);
		}

		delete.addActionListener((ActionEvent e) -> {
			app.addWindow(new ActionDialog(app, "Confirm deletion", "<html>Really delete employee "
					+ Integer.toString(data.getID()) + "? <b>This action cannot be undone.</b></html>",
					(ActionEvent e1) -> {
						db.removeEmployee(data.getID());
						this.doDefaultCloseAction();
					}, ActionDialog.Button.Cancel), true);
		});
		bottom.add(delete);

		pane.addEditListener((data, canSave) -> {
			save.setEnabled(canSave);
		});
	}

}
