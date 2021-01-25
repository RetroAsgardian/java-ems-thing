package cyou.keithhacks.ems.query;

import java.util.ArrayList;

import ca.kbnt.ems.EmployeeManager.EmployeeData;

public class Query {
	
	boolean matchAny;
	ArrayList<QueryClause> clauses;
	
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
