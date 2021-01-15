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
public class PartTimeEmployee extends Employee {

	// <editor-fold desc="ATTRIBUTES">
	private PTEmployeeData data;
	// </editor-fold>

	// <editor-fold desc="CONSTRUCTORS">
	public PartTimeEmployee(int ID, String firstName, String lastName, Gender gender, double deductionRate,
			String location, double hourlyWage, double hoursPerWeek, double weeksPerYear) {
		super(ID, firstName, lastName, gender, deductionRate, location);
		data = new PTEmployeeData(ID, firstName, lastName, gender, deductionRate, location, hourlyWage, hoursPerWeek,
				weeksPerYear);
	}

	public PartTimeEmployee(int ID) {
		super(ID);
		data = new PTEmployeeData(ID);
	}

	public PartTimeEmployee(PTEmployeeData data) {
		super(data);
		this.data = new PTEmployeeData(data);
	}
	// </editor-fold>

	@Override
	public void setData(EmployeeData data) {
		this.data = new PTEmployeeData(data);
	}

	@Override
	public EmployeeData getData() {
		return new EmployeeData(this.data);
	}

	public static class PTEmployeeData extends EmployeeData {

		private double hourlyWage = 0;
		private double hoursPerWeek = 0;
		private double weeksPerYear = 52;

		public PTEmployeeData(int ID) {
			super(ID);
		}

		public PTEmployeeData(int ID, String firstName, String lastName, Gender gender, double deductRate,
				String location, double hourlyWage, double hoursPerWeek, double weeksPerYear) {
			super(ID, firstName, lastName, gender, deductRate, location);
			this.hourlyWage = hourlyWage;
			this.hoursPerWeek = hoursPerWeek;
			this.weeksPerYear = weeksPerYear;
		}

		public PTEmployeeData(PTEmployeeData data) {
			this((EmployeeData) data);
		}

		public PTEmployeeData(EmployeeData data) {
			super(data);
			if (data instanceof PTEmployeeData) {
				var ptdata = (PTEmployeeData) data;
				this.hourlyWage = ptdata.hourlyWage;
				this.hoursPerWeek = ptdata.hoursPerWeek;
				this.weeksPerYear = ptdata.weeksPerYear;
			}
		}

		// <editor-fold desc="GETTERS AND SETTERS">
		/**
		 * @return the yearlySalary
		 */
		// </editor-fold>
		@Override
		public double calcAnnualNetIncome() {
			return hourlyWage * hoursPerWeek * weeksPerYear;
		}
	}

}
