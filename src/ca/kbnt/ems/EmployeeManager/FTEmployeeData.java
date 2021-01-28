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

public class FTEmployeeData extends EmployeeData {

    private double yearlySalary;

    public FTEmployeeData(int ID) {
        super(ID);
        yearlySalary = 0;
    }

    public FTEmployeeData(int ID, String firstName, String lastName, Gender gender, double deductRate, String location,
            double yearlySalary) {
        super(ID, firstName, lastName, gender, deductRate, location);
        this.yearlySalary = yearlySalary;
    }

    public FTEmployeeData(FTEmployeeData data) {
        this((EmployeeData) data);
    }

    public FTEmployeeData(EmployeeData data) {
        super(data);
        this.yearlySalary = data.calcAnnualGrossIncome();
        // if (data instanceof FTEmployeeData) {
        // var ftdata = (FTEmployeeData) data;
        // this.yearlySalary = ftdata.yearlySalary;
        // }
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
    public FTEmployeeData clone() {
        return new FTEmployeeData(this);
    }

    @Override
    public double calcAnnualGrossIncome() {
        return yearlySalary;
    }

}