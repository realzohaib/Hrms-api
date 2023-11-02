package com.erp.hrms.entity.form;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveCalendarData {
	private LocalDate date;
	private int employeeCount;

	public LeaveCalendarData(LocalDate date) {
		this.date = date;
		this.employeeCount = 1;
	}

	public void incrementEmployeeCount() {
		this.employeeCount++;
	}

}
