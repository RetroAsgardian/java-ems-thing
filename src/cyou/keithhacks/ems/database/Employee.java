package cyou.keithhacks.ems.database;

/**
 * Employee record class
 * @author Keith
 */
public class Employee extends Record {
	
	@DBField(primaryKey = true)
	protected int id;
	
	@DBField
	protected String firstName;
	
	@DBField(alternateKey = true)
	protected String lastName;
	
	@DBField
	protected String gender;
	
	@DBField
	protected int location;
	
	@DBField
	protected double deductRate;
	
	@VirtualDBField
	public double grossIncome() {
		return 0.0;
	}
	
	@VirtualDBField
	public double netIncome() {
		double x = grossIncome();
		return x - (x * deductRate);
	}
	
}
