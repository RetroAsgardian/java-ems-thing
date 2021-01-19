package ca.kbnt.ems.EmployeeManager;

import java.util.ArrayList;
import java.util.List;

import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangeOperation;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangedEvent;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangedEventListener;

/**
 *
 * @author Kyle Benton
 */
public class Employee implements HashTable.IHashable {

	@Override
	public int getID() {
		return this.data.getID();
	}

	// <editor-fold desc="EVENTS">
	// <editor-fold desc="DataChangedEvent">

	private final List<DataChangedEventListener> dataChangedListeners = new ArrayList<>();

	public void addDataChangedListener(DataChangedEventListener l) {
		dataChangedListeners.add(l);
	}

	public void removeDataChangedListener(DataChangedEventListener l) {
		dataChangedListeners.remove(l);
	}

	private void notifyDataChangedListeners(DataChangedEvent e) {
		dataChangedListeners.forEach(l -> {
			l.onChange(e);
		});
	}

	// </editor-fold>
	// </editor-fold>

	// <editor-fold desc="ATTRIBUTES">
	private ca.kbnt.ems.EmployeeManager.EmployeeData data;
	// </editor-fold>

	// <editor-fold desc="CONSTRUCTORS">

	public Employee(ca.kbnt.ems.EmployeeManager.EmployeeData data) {
		this.data = new ca.kbnt.ems.EmployeeManager.EmployeeData(data);
	}
	// </editor-fold>

	public void setData(ca.kbnt.ems.EmployeeManager.EmployeeData data) {
		var oldData = data;
		this.data = data.clone();
		notifyDataChangedListeners(new DataChangedEvent(data, oldData, DataChangeOperation.Modified));
	}

	public ca.kbnt.ems.EmployeeManager.EmployeeData getData() {
		return this.data.clone();
	}

}
