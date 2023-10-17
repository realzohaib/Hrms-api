package com.erp.hrms.attendence.service;

import java.time.LocalDate;
import java.util.List;

import com.erp.hrms.attendence.entity.Attendence;

public interface IAttendenceService {
	public Attendence punchIn(Attendence attendence) throws AttendencenotRegistered;
	
	public List<Attendence> getEmployeeAttendence(long employeeId);
	
	public Attendence punchout(long id) throws AttendencenotRegistered;
	
	public Attendence  breakStart(long Attendenceid);
	
	public Attendence  breakEnd(long Attendenceid);
	
	public List<Attendence> getAttendenceByDate(Long employeeId, LocalDate startDate, LocalDate endDate) ;

	public List<Attendence> getAttendanceForMonth(Long employeeId, int year, int month);
	
	public int calculateWorkingDays(int year, int month);



}
