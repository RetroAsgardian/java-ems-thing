package cyou.keithhacks.ems.query;

import java.util.regex.PatternSyntaxException;

import ca.kbnt.ems.EmployeeManager.EmployeeData;

public class TextQueryClause extends QueryClause {
	
	public static enum QueryType {
		Equals("equals"),
		NotEquals("does not equal"),
		Contains("contains"),
		NotContains("does not contain"),
		StartsWith("starts with"),
		NotStartsWith("does not start with"),
		EndsWith("ends with"),
		NotEndsWith("does not end with"),
		Regex("matches regex");
		
		private String label;
		public String toString() {
			return label;
		}
		private QueryType(String label) {
			this.label = label;
		}
	}
	
	public static enum QueryField {
		FirstName("First name"),
		LastName("Last name"),
		Location("Location");
		
		private String label;
		public String toString() {
			return label;
		}
		private QueryField(String label) {
			this.label = label;
		}
	}
	
	public QueryField field;
	public QueryType type;
	public String str;
	public TextQueryClause(QueryField field, QueryType type, String str) {
		this.field = field;
		this.type = type;
		this.str = str;
	}
	
	@Override
	public boolean matches(EmployeeData e) {
		String empStr;
		switch (field) {
		case FirstName:
			empStr = e.getFirstName();
			break;
		case LastName:
			empStr = e.getLastName();
			break;
		case Location:
			empStr = e.getLocation();
			break;
		default:
			return false;
		}
		
		switch (type) {
		case Equals:
			return empStr.equals(str);
		case NotEquals:
			return !empStr.equals(str);
		case Contains:
			return empStr.contains(str);
		case NotContains:
			return !empStr.contains(str);
		case StartsWith:
			return empStr.startsWith(str);
		case NotStartsWith:
			return !empStr.startsWith(str);
		case EndsWith:
			return empStr.endsWith(str);
		case NotEndsWith:
			return !empStr.endsWith(str);
		case Regex:
			try {
				return empStr.matches(str);
			} catch (PatternSyntaxException e1) {
				return false;
			}
		}
		
		return false;
	}
	
}
