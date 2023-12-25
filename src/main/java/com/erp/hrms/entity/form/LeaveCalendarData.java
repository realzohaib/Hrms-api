package com.erp.hrms.entity.form;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveCalendarData {
	private LocalDate date;
	private int employeeCount;
	public List<LeaveEmployee> leaveEmployees;  

	public LeaveCalendarData(LocalDate date) {
		this.date = date;
		this.employeeCount = 0;
	}

	public void incrementEmployeeCount() {
		this.employeeCount++;
	}

}
