package cyou.keithhacks.ems;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;

public class EmployeeTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 5537523222788811095L;
	
	private static final String[] allColumns = {"ID", "First name", "Last name", "Gender", "Location", "Gross salary", "Net salary"};
	private static final Class<?>[] columnClasses = {Integer.class, String.class, String.class, Gender.class, String.class, Double.class, Double.class};
	
	protected boolean[] columnMask = {true, true, true, false, true, true, false};
	
	protected EmployeeManager db;
	
	protected int[] enabledColumns;
	
	public EmployeeTableModel(EmployeeManager db) {
		this.db = db;
		
		enabledColumns = getEnabledColumns();
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
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getColumnCount() {
		return enabledColumns.length;
	}
	
	public String getColumnName(int column) {
		return allColumns[enabledColumns[column]];
	}
	
	public Class<?> getColumnClass(int column) {
		return columnClasses[enabledColumns[column]];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		EmployeeData e = db.getEmployee(0).getData();
		
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
		
		return null;
	}

}
