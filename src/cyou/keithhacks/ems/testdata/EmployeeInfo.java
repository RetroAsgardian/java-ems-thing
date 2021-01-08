package cyou.keithhacks.ems.testdata;

/**
 * 
 * @author Keith
 * @deprecated Ignore this class
 */
public class EmployeeInfo {
	
	protected int id;
	protected String firstName;
	protected String lastName;
	protected String gender;
	protected int locationID;
	protected double deductRate;
	
	public EmployeeInfo(int id, String firstName, String lastName, String gender, int locationID, double deductRate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.locationID = locationID;
		this.deductRate = deductRate;
	}
	
	public int getID() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getGender() {
		return gender;
	}
	
	public int getLocationID() {
		return locationID;
	}
	
	public double getDeductRate() {
		return deductRate;
	}
	
	public int hashCode() {
		return id;
	}
	
}
