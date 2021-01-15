package ca.kbnt.ems.EmployeeManager;

/**
 *
 * @author Kyle Benton
 */
public abstract class Employee implements HashTable.IHashable {

    @Override
    public int getID() {
        return this.data.getID();
    }

    // <editor-fold desc="STATIC ATTRIBUTES">
    public enum Gender {
    	/**
    	 * @deprecated This is a legacy gender, and will be removed in a future release.
    	 */
    	Male,
    	/**
    	 * @deprecated This is a legacy gender, and will be removed in a future release.
    	 */
    	Female,
    	NonBinary,
    	Other,
        Yes,
        Definitely,
        MostLikely,
        No,
        Maybe,
        ProbablyNot,
        ReplyHazyTryAgain,
        AskAgainLater,
        Unknown
    }
    //</editor-fold>

    // <editor-fold desc="ATTRIBUTES">
    private EmployeeData data;
    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">
    public Employee(int ID, String firstName, String lastName, Gender gender, double deductionRate) {
        data = new EmployeeData(ID, firstName, lastName, gender, deductionRate);
    }
    
    public Employee(int ID) {
        data = new EmployeeData(ID);
    }
    
    public Employee(EmployeeData data) {
        this.data = new EmployeeData(data);
    }
    // </editor-fold>

    
    public void setData(EmployeeData data) {
        this.data = new EmployeeData(data);
    }
    
    public EmployeeData getData() {
        return new EmployeeData(this.data);
    }
    
    public static class EmployeeData {

        protected final int ID;
        protected String firstName;
        protected String lastName;
        protected Gender gender;
        protected double deductRate; // e.g. 0.21 for 21%
        
//        public EmployeeData() {
//            ID = 0;
//        }
        
        public EmployeeData(int ID) {
            this.ID = ID;
            this.firstName = "";
            this.lastName = "";
            this.gender = Gender.Unknown;
            this.deductRate = 0;
        }
        
        public EmployeeData(int ID, String firstName, String lastName, Gender gender, double deductRate) {
            this.ID = ID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.deductRate = deductRate;
        }

        public EmployeeData(EmployeeData data) {
            this.ID = data.ID;
            this.firstName = data.firstName;
            this.lastName = data.lastName;
            this.gender = data.gender;
            this.deductRate = data.deductRate;
        }

        // <editor-fold desc="GETTERS AND SETTERS">
        /**
         * @return the ID
         */
        public int getID() {
            return ID;
        }

        /**
         * @return the firstName
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * @param firstName the firstName to set
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         * @return the lastName
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * @param lastName the lastName to set
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        /**
         * @return the gender
         */
        public Gender getGender() {
            return gender;
        }

        /**
         * @param gender the gender to set
         */
        public void setGender(Gender gender) {
            this.gender = gender;
        }

        /**
         * @return the deductRate
         */
        public double getDeductRate() {
            return deductRate;
        }

        /**
         * @param deductRate the deductRate to set
         */
        public void setDeductRate(double deductRate) {
            this.deductRate = deductRate;
        }
        // </editor-fold>

        public double calcAnnualGrossIncome() {
            return calcAnnualNetIncome() * (1-deductRate);
        }

        public double calcAnnualNetIncome() {
            return 0;
        };
    }
}
