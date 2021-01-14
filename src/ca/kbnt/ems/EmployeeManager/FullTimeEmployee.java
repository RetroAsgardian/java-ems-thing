/*
 * Copyright (C) 2021 kbenton
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.kbnt.ems.EmployeeManager;

/**
 *
 * @author Kyle Benton
 */
public class FullTimeEmployee extends Employee {

    // <editor-fold desc="ATTRIBUTES">
    private FTEmployeeData data;
    // </editor-fold>

    // <editor-fold desc="CONSTRUCTORS">
    public FullTimeEmployee(int ID, String firstName, String lastName, Gender gender, double deductionRate, double yearlySalary) {
        super(ID, firstName, lastName, gender, deductionRate);
        data = new FTEmployeeData(ID, firstName, lastName, gender, deductionRate, yearlySalary);
    }

    public FullTimeEmployee(int ID) {
        super(ID);
        data = new FTEmployeeData(ID);
    }

    public FullTimeEmployee(FTEmployeeData data) {
        super(data);
        this.data = new FTEmployeeData(data);
    }
    // </editor-fold>
    
    @Override
    public void setData(EmployeeData data) {
        this.data = new FTEmployeeData(data);
    }
    
    @Override
    public  EmployeeData getData() {
        return new EmployeeData(this.data);
    }
    
    public static class FTEmployeeData extends EmployeeData {

        private double yearlySalary;

        public FTEmployeeData(int ID) {
            super(ID, "", "", Gender.Unknown, 0);
            yearlySalary = 0;
        }

        public FTEmployeeData(int ID, String firstName, String lastName, Gender gender, double deductRate, double yearlySalary) {
            super(ID, firstName, lastName, gender, deductRate);
            this.yearlySalary = yearlySalary;
        }

        public FTEmployeeData(FTEmployeeData data) {
            this((EmployeeData) data);
        }

        public FTEmployeeData(EmployeeData data) {
            super(data);
            this.yearlySalary = data.calcAnnualNetIncome();
//            if (data instanceof FTEmployeeData) {
//                var ftdata = (FTEmployeeData) data;
//                this.yearlySalary = ftdata.yearlySalary;
//            }
        }

        // <editor-fold desc="GETTERS AND SETTERS">
        /**
         * @return the yearlySalary
         */
        public double getYearlySalary() {
            return yearlySalary;
        }

        /**
         * @param yearlySalary the yearlySalary to set
         */
        public void setYearlySalary(double yearlySalary) {
            this.yearlySalary = yearlySalary;
        }

        // </editor-fold>
        @Override
        public double calcAnnualNetIncome() {
            return yearlySalary;
        }

    }

}
