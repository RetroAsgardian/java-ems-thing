package cyou.keithhacks.ems.query;

import java.util.ArrayList;

import ca.kbnt.ems.EmployeeManager.EmployeeData;

public class Query {
	
	public boolean matchAny;
	public ArrayList<QueryClause> clauses;
	
	public Query() {
		this.matchAny = false;
		this.clauses = new ArrayList<QueryClause>();
	}
	
	public void reset() {
		
	}
	
	public boolean matches(EmployeeData e) {
		if (matchAny) {
			for (QueryClause clause: clauses) {
				if (clause.matches(e))
					return true;
			}
			return false;
		} else {
			for (QueryClause clause: clauses) {
				if (!clause.matches(e))
					return false;
			}
			return true;
		}
	}
	
}
