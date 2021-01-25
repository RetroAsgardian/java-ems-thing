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

public class EmployeeData {

	public enum Gender {
		/**
		 * @deprecated This is a legacy gender, and will be removed in a future release.
		 */
		Male,
		/**
		 * @deprecated This is a legacy gender, and will be removed in a future release.
		 */
		Female,
		NonBinary("Non Binary"),
		SeaCaptain("Sea Captain"),
		Other,
		Yes,
		Definitely,
		MostLikely("Most Likely"),
		No,
		Maybe,
		ProbablyNot("Probably Not"),
		GenderHazyTryAgain("Gender Hazy Try Again"),
		AskAgainLater("Ask Again Later"),
		/**
		 * My dear sir, there are individuals roaming the streets of Fallen London at this
		 * very moment with the faces of squid! Squid! Do you ask them their gender? And
		 * yet you waste our time asking me trifling and impertinent questions about mine?
		 * It is my own business, sir, and I bid you good day.
		 */
		DoNotAskAgainEverYouHaveBeenWarned("Do Not Ask Again Ever You Have Been Warned"),
		/**
		 * You wouldn't download a gender
		 */
		Pirated,
		Unknown;

		private final String label;

		private Gender(String label) {
			this.label = label;
		}

		private Gender() {
			this.label = this.name();
		}

		public String toString() {
			return label;
		}
		
	}

	protected final int ID;
	protected String firstName;
	protected String lastName;
	protected Gender gender;
	protected double deductRate; // e.g. 0.21 for 21%
	protected String location;

	// public EmployeeData() {
	// ID = 0;
	// }

	public EmployeeData(int ID) {
		this.ID = ID;
		this.firstName = "";
		this.lastName = "";
		this.gender = Gender.Unknown;
		this.deductRate = 0;
		this.location = "";
	}

	public EmployeeData(int ID, String firstName, String lastName, Gender gender, double deductRate, String location) {
		this.ID = ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.deductRate = deductRate;
		this.location = location;
	}

	public EmployeeData(EmployeeData data) {
		this.ID = data.ID;
		this.firstName = data.firstName;
		this.lastName = data.lastName;
		this.gender = data.gender;
		this.deductRate = data.deductRate;
		this.location = data.location;
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

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	};

	// </editor-fold>

	public EmployeeData clone() {
		return new EmployeeData(this);
	}

	public double calcAnnualGrossIncome() {
		return calcAnnualNetIncome() * (1 - deductRate);
	}

	public double calcAnnualNetIncome() {
		return 0;
	}
}
