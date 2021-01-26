package ca.kbnt.ems.EmployeeManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

	private final List<DataChangedEventListener> dataChangedListeners = new CopyOnWriteArrayList<>();

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
	private EmployeeData data;
	// </editor-fold>

	// <editor-fold desc="CONSTRUCTORS">

	public Employee(EmployeeData data) {
		this.data = data.clone();
	}
	// </editor-fold>

	public void setData(EmployeeData data) {
		var oldData = data;
		this.data = data.clone();
		notifyDataChangedListeners(new DataChangedEvent(data, oldData, DataChangeOperation.Modified));
	}

	public EmployeeData getData() {
		return this.data.clone();
	}

}
