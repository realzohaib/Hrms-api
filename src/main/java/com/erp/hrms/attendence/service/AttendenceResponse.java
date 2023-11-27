package com.erp.hrms.attendence.service;

import java.util.List;

import com.erp.hrms.attendence.entity.Attendence;
import com.erp.hrms.shift.entity.ShiftAssignment;

import lombok.AllArgsConstructor;
import lombok.Data;
//this class is an response class which sends overall response of employee for the month
@Data
public class AttendenceResponse {
	
	 private List<Attendence> Attendence;

	private int totalWorkigDaysInMonth;
	
	private int totalDaysPresentInMonth;
	
	private int totalHalfDaysInMonth;
	
	private long totalOvertimeHoursInMonth;
	
	private Integer noOfDaysWorkedRegularHours;
	
	private ShiftAssignment shift;
	

}
