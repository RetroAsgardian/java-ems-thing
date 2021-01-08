package cyou.keithhacks.ems.testdata;

import java.util.Random;

import cyou.keithhacks.ems.collections.OpenBTreeHashTable;

public class DataThingy {
	
	public OpenBTreeHashTable<Integer, EmployeeInfo> employees;
	
	protected DataThingy() {
		employees = new OpenBTreeHashTable<Integer, EmployeeInfo>(32);
		
		Random random = new Random();
		
		for (int i = 0; i < 10; i++) {
			int id;
			do {
				id = random.nextInt(999999);
			} while (employees.get(id) != null);
			
			employees.put(id, new EmployeeInfo(id, "Test", "Person", "No", 0, 0.1));
		}
	}
	
	protected static DataThingy instance;
	public static DataThingy get() {
		if (instance == null)
			instance = new DataThingy();
		return instance;
	}
	
}
