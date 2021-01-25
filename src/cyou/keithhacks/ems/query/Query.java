package cyou.keithhacks.ems.query;

import ca.kbnt.ems.EmployeeManager.EmployeeData;

public abstract class Query {
	
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
	
	public static Class<? extends Query> getQueryTypeForField(QueryField field) {
		switch (field) {
		case ID:
			return IDQuery.class;
		case FirstName:
		case LastName:
		case Location:
			return TextQuery.class;
		case DeductRate:
		case GrossSalary:
		case NetSalary:
			return DoubleQuery.class;
		case Gender:
			return GenderQuery.class;
		}
		return null;
	}
	
}
