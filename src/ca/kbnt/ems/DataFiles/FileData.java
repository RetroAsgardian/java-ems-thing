package ca.kbnt.ems.DataFiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ca.kbnt.ems.DataFiles.protobuf.ProtoEmployeeList;
import ca.kbnt.ems.EmployeeManager.Employee;
import ca.kbnt.ems.EmployeeManager.EmployeeManager;
import ca.kbnt.ems.EmployeeManager.EmployeeData;
import ca.kbnt.ems.EmployeeManager.FTEmployeeData;
import ca.kbnt.ems.EmployeeManager.PTEmployeeData;
import ca.kbnt.ems.EmployeeManager.EmployeeData.Gender;

public class FileData {
	public static void writeData(OutputStream output, EmployeeManager mgr) throws IOException {

		var empList = ProtoEmployeeList.EmployeeList.newBuilder();

		for (var e : mgr.getEmployees()) {
			var data = e.getData();
			var builder = ProtoEmployeeList.Employee.newBuilder().setID(data.getID()).setFname(data.getFirstName())
					.setLname(data.getLastName()).setGender(data.getGender().name()).setDeductRate(data.getDeductRate())
					.setLocation(data.getLocation());

			if (data instanceof PTEmployeeData) {
				var ptdata = (PTEmployeeData) data;
				builder.setPte(ProtoEmployeeList.PTEmployee.newBuilder().setHourlyWage(ptdata.getHourlyWage())
						.setHoursPerWeek(ptdata.getHourlyWage()).setWeeksPerYear(ptdata.getWeeksPerYear()));
			} else if (data instanceof FTEmployeeData) {
				var ftdata = (FTEmployeeData) data;
				builder.setFte(ProtoEmployeeList.FTEmployee.newBuilder().setYearlySalary(ftdata.getYearlySalary()));
			}

			empList.addList(builder.build());
		}
		empList.build().writeTo(output);

	}

	public static EmployeeManager loadData(InputStream input) throws IOException {
		var empList = ProtoEmployeeList.EmployeeList.parseFrom(input);
		List<Employee> newList = new ArrayList<>();

		for (var e : empList.getListList()) {
			EmployeeData data;
			switch (e.getAdditionalDataCase()) {
				case PTE:
					var ptdata = new PTEmployeeData(e.getID());
					var pte = e.getPte();
					ptdata.setHourlyWage(pte.getHourlyWage());
					ptdata.setHoursPerWeek(pte.getHoursPerWeek());
					ptdata.setWeeksPerYear(pte.getWeeksPerYear());
					data = ptdata;
					break;

				case FTE:
					var ftdata = new FTEmployeeData(e.getID());
					ftdata.setYearlySalary(e.getFte().getYearlySalary());
					data = ftdata;
					break;
				case ADDITIONALDATA_NOT_SET:
				default:
					data = new EmployeeData(e.getID());
					break;
			}

			data.setFirstName(e.getFname());
			data.setLastName(e.getLname());
			data.setGender(Gender.valueOf(e.getGender()));
			data.setDeductRate(e.getDeductRate());
			data.setLocation(e.getLocation());
			newList.add(new Employee(data));
		}

		return new EmployeeManager(newList);
	}
}
