package cyou.keithhacks.ems.query;

import ca.kbnt.ems.EmployeeManager.EmployeeData;

public abstract class QueryClause {
	
	public abstract boolean matches(EmployeeData e);
	
	public static enum QueryField {
		ID("ID"),
		FirstName("First name"),
		LastName("Last name"),
		Gender("Gender"),
		Location("Location"),
		DeductRate("Deduct rate"),
		GrossSalary("Gross salary"),
		NetSalary("Net salary");
		
		private String label;
		public String toString() {
			return label;
		}
		private QueryField(String label) {
			this.label = label;
		}
	}
	
	public static Class<? extends QueryClause> getQueryTypeForField(QueryField field) {
		switch (field) {
		case ID:
			return IDQueryClause.class;
		case FirstName:
		case LastName:
		case Location:
			return TextQueryClause.class;
		case DeductRate:
		case GrossSalary:
		case NetSalary:
			return DoubleQueryClause.class;
		case Gender:
			return GenderQueryClause.class;
		}
		return null;
	}
	
}
