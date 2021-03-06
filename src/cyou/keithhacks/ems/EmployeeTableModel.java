package cyou.keithhacks.ems;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;
import cyou.keithhacks.ems.query.Query;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.EmployeeManager.DataChangedEvent;

public class EmployeeTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 5537523222788811095L;

	public static final String[] allColumns = { "ID", "First name", "Last name", "Gender", "Location", "Gross salary", "Net salary", "Deduct Rate" };
	private static final Class<?>[] columnClasses = { Integer.class, String.class, String.class, Gender.class, String.class, Double.class, Double.class, Double.class };

	protected boolean[] columnMask = { true, true, true, false, true, true, false, false };

	public void setColumnVisibility(int column, boolean visible) {
		if (column >= columnMask.length || column < 0)
			return;

		columnMask[column] = visible;
		enabledColumns = getEnabledColumns();
		this.fireTableStructureChanged();
	}

	public boolean getColumnVisibility(int column) {
		if (column >= columnMask.length || column < 0)
			return false;

		return columnMask[column];
	}

	protected EmployeeManager db;

	protected int[] enabledColumns;

	protected ArrayList<Integer> ids;

	protected Query query;

	public EmployeeTableModel(EmployeeManager db) {
		this.db = db;
		this.query = null;

		enabledColumns = getEnabledColumns();
		refreshIDs();

		db.addDataChangedListener((DataChangedEvent e) -> {
			refresh();
		});
	}

	public void refresh() {
		refreshIDs();
		this.fireTableDataChanged();
	}

	protected void refreshIDs() {
		ids = new ArrayList<Integer>();

		if (query == null)
			for (Employee e : db) {
				ids.add(e.getID());
			}
		else
			for (Employee e : db) {
				if (query.matches(e.getData()))
					ids.add(e.getID());
			}
	}
	
	public void clearQuery() {
		this.query = null;
		refresh();
	}
	
	public void setQuery(Query q) {
		this.query = q;
		refresh();
	}
	
	protected int[] getEnabledColumns() {
		ArrayList<Integer> columns = new ArrayList<Integer>();

		for (int i = 0; i < allColumns.length; i++) {
			if (columnMask[i])
				columns.add(Integer.valueOf(i));
		}

		// manual toArray because java is dumb
		int[] array = new int[columns.size()];
		for (int i = 0; i < columns.size(); i++) {
			array[i] = columns.get(i).intValue();
		}
		return array;
	}

	@Override
	public int getRowCount() {
		return ids.size();
	}

	@Override
	public int getColumnCount() {
		return enabledColumns.length;
	}

	public String getColumnName(int column) {
		return allColumns[enabledColumns[column]];
	}

	/* Things that could have been
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return !("ID".equals(allColumns[enabledColumns[columnIndex]]));
	}
	 */

	public Class<?> getColumnClass(int column) {
		return columnClasses[enabledColumns[column]];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		EmployeeData e = db.getEmployee(getIDAt(rowIndex)).getData();

		if (columnIndex >= enabledColumns.length)
			return null;

		if ("ID".equals(allColumns[enabledColumns[columnIndex]]))
			return Integer.valueOf(e.getID());
		else if ("First name".equals(allColumns[enabledColumns[columnIndex]]))
			return e.getFirstName();
		else if ("Last name".equals(allColumns[enabledColumns[columnIndex]]))
			return e.getLastName();
		else if ("Gender".equals(allColumns[enabledColumns[columnIndex]]))
			return e.getGender();
		else if ("Location".equals(allColumns[enabledColumns[columnIndex]]))
			return e.getLocation();
		else if ("Gross salary".equals(allColumns[enabledColumns[columnIndex]]))
			return e.calcAnnualGrossIncome();
		else if ("Net salary".equals(allColumns[enabledColumns[columnIndex]]))
			return e.calcAnnualNetIncome();
		else if ("Deduct Rate".equals(allColumns[enabledColumns[columnIndex]]))
			return e.getDeductRate() * 100;

		return null;
	}

	public int getIDAt(int row) {
		return ids.get(row).intValue();
	}

}
