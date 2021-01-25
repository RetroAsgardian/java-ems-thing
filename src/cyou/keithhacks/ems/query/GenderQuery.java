package cyou.keithhacks.ems.query;

import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;

public class GenderQuery extends Query {
	
	public static enum QueryType {
		Equals("equals"),
		NotEquals("does not equal");
		
		private String label;
		public String toString() {
			return label;
		}
		private QueryType(String label) {
			this.label = label;
		}
	}
	
	QueryType type;
	Gender val;
	public GenderQuery(QueryType type, Gender val) {
		this.type = type;
		this.val = val;
	}
	
	@Override
	public boolean matches(EmployeeData e) {
		switch (type) {
		case Equals:
			return e.getGender() == val;
		case NotEquals:
			return e.getGender() != val;
		}
		
		return false;
	}
	
}
