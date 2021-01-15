package ca.kbnt.ems.EmployeeManager;

/**
 *
 * @author Kyle Benton
 */
public class Employee implements HashTable.IHashable {

	@Override
	public int getID() {
		return this.data.getID();
	}

	// <editor-fold desc="ATTRIBUTES">
	private ca.kbnt.ems.EmployeeManager.EmployeeData data;
	// </editor-fold>

	// <editor-fold desc="CONSTRUCTORS">

	public Employee(ca.kbnt.ems.EmployeeManager.EmployeeData data) {
		this.data = new ca.kbnt.ems.EmployeeManager.EmployeeData(data);
	}
	// </editor-fold>

	public void setData(ca.kbnt.ems.EmployeeManager.EmployeeData data) {
		this.data = data.clone();
	}

	public ca.kbnt.ems.EmployeeManager.EmployeeData getData() {
		return this.data.clone();
	}

}
