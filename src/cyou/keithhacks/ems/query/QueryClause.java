package cyou.keithhacks.ems.query;

import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;

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
	
	public static QueryField getFieldForQuery(QueryClause clause) {
		if (clause instanceof IDQueryClause) {
			return QueryField.ID;
		} else if (clause instanceof TextQueryClause) {
			switch (((TextQueryClause) clause).field) {
			case FirstName:
				return QueryField.FirstName;
			case LastName:
				return QueryField.LastName;
			case Location:
				return QueryField.Location;
			}
		} else if (clause instanceof DoubleQueryClause) {
			switch (((DoubleQueryClause) clause).field) {
			case DeductRate:
				return QueryField.DeductRate;
			case GrossSalary:
				return QueryField.GrossSalary;
			case NetSalary:
				return QueryField.NetSalary;
			}
		} else if (clause instanceof GenderQueryClause) {
			return QueryField.Gender;
		}
		
		return null;
	}
	
	public static QueryClause convertQueryClause(QueryClause clause, QueryField field) {
		if (getQueryTypeForField(field) != clause.getClass()) {
			switch (field) {
			case ID:
				return new IDQueryClause(IDQueryClause.QueryType.Equals, 0);
			case FirstName:
				return new TextQueryClause(TextQueryClause.QueryField.FirstName, TextQueryClause.QueryType.Equals, "");
			case LastName:
				return new TextQueryClause(TextQueryClause.QueryField.LastName, TextQueryClause.QueryType.Equals, "");
			case Location:
				return new TextQueryClause(TextQueryClause.QueryField.Location, TextQueryClause.QueryType.Equals, "");
			case DeductRate:
				return new DoubleQueryClause(DoubleQueryClause.QueryField.DeductRate, DoubleQueryClause.QueryType.Equals, 0.0);
			case GrossSalary:
				return new DoubleQueryClause(DoubleQueryClause.QueryField.GrossSalary, DoubleQueryClause.QueryType.Equals, 0.0);
			case NetSalary:
				return new DoubleQueryClause(DoubleQueryClause.QueryField.NetSalary, DoubleQueryClause.QueryType.Equals, 0.0);
			case Gender:
				return new GenderQueryClause(GenderQueryClause.QueryType.Equals, Gender.Unknown);
			}
		} else if (clause instanceof TextQueryClause) {
			switch (field) {
			case FirstName:
				((TextQueryClause) clause).field = TextQueryClause.QueryField.FirstName;
				return clause;
			case LastName:
				((TextQueryClause) clause).field = TextQueryClause.QueryField.LastName;
				return clause;
			case Location:
				((TextQueryClause) clause).field = TextQueryClause.QueryField.Location;
				return clause;
			}
		} else if (clause instanceof DoubleQueryClause) {
			switch (field) {
			case DeductRate:
				((DoubleQueryClause) clause).field = DoubleQueryClause.QueryField.DeductRate;
				return clause;
			case GrossSalary:
				((DoubleQueryClause) clause).field = DoubleQueryClause.QueryField.GrossSalary;
				return clause;
			case NetSalary:
				((DoubleQueryClause) clause).field = DoubleQueryClause.QueryField.NetSalary;
				return clause;
			}
		}
		
		return clause;
	}
	
}
