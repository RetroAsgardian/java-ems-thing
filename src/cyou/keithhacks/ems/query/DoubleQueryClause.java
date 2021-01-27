package cyou.keithhacks.ems.query;

import ca.kbnt.ems.EmployeeManager.EmployeeData;

public class DoubleQueryClause extends QueryClause {
	
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
	
	public static enum QueryField {
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
	
	public QueryField field;
	public QueryType type;
	public double val;
	public DoubleQueryClause(QueryField field, QueryType type, double val) {
		this.field = field;
		this.type = type;
		this.val = val;
	}
	
	@Override
	public boolean matches(EmployeeData e) {
		double empVal;
		switch (field) {
		case DeductRate:
			empVal = e.getDeductRate() * 100;
			break;
		case GrossSalary:
			empVal = e.calcAnnualGrossIncome();
			break;
		case NetSalary:
			empVal = e.calcAnnualNetIncome();
			break;
		default:
			return false;
		}
		
		switch (type) {
		case Equals:
			return empVal == val;
		case NotEquals:
			return empVal != val;
		case Greater:
			return empVal > val;
		case Less:
			return empVal < val;
		case GreaterOrEqual:
			return empVal >= val;
		case LessOrEqual:
			return empVal <= val;
		}
		
		return false;
	}
	
}
