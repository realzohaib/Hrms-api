package com.erp.hrms.attendence.service;

import java.util.List;

import com.erp.hrms.attendence.entity.Attendence;

import lombok.Data;

@Data
public class AttendenceResponse {
	
	List<Attendence> Attendence;

	private int totalWorkigDaysInMonth;
	
	private int totalDaysPresentInMonth;
	
	private int totalHalfDaysInMonth;
	
	private long totalOvertimeHoursInMonth;
	

}
