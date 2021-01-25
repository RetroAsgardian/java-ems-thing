package cyou.keithhacks.ems.query;

import ca.kbnt.ems.EmployeeManager.EmployeeData;

public class IDQueryClause extends QueryClause {
	
	public static enum QueryType {
		Equals("equals"),
		NotEquals("does not equal"),
		Greater("is greater than"),
		Less("is less than"),
		GreaterOrEqual("is greater than or equal to"),
		LessOrEqual("is less than or equal to");
		
		private String label;
		public String toString() {
			return label;
		}
		private QueryType(String label) {
			this.label = label;
		}
	}
	
	QueryType type;
	int val;
	public IDQueryClause(QueryType type, int val) {
		this.type = type;
		this.val = val;
	}
	
	@Override
	public boolean matches(EmployeeData e) {
		switch (type) {
		case Equals:
			return e.getID() == val;
		case NotEquals:
			return e.getID() != val;
		case Greater:
			return e.getID() > val;
		case Less:
			return e.getID() < val;
		case GreaterOrEqual:
			return e.getID() >= val;
		case LessOrEqual:
			return e.getID() <= val;
		}
		
		return false;
	}
	
}
