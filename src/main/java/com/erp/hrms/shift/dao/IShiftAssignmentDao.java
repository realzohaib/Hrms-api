package com.erp.hrms.shift.dao;

import java.time.LocalDate;
import java.util.List;

import com.erp.hrms.shift.entity.Shift;
import com.erp.hrms.shift.entity.ShiftAssignment;

import net.bytebuddy.asm.Advice.Local;

public interface IShiftAssignmentDao {
	
	public void saveAssignedShift( ShiftAssignment shiftAssignment);
	
	public List<ShiftAssignment> getAlEmployeeShifr();
	
	public List<ShiftAssignment> getAllEmployeeShiftById(long id);
	
	public ShiftAssignment currentShftById(long empid);
	
	public ShiftAssignment updateShift( ShiftAssignment asign);
	
	public List<ShiftAssignment> findByDate(LocalDate date);
	
	public List<ShiftAssignment> findshiftById(long id , LocalDate date);
	
	List<ShiftAssignment> findByShift_ShiftNameAndStartDate(String shiftName, LocalDate date);
	
	public void deleteShiftAllocation(long assignmentId);
;

}
