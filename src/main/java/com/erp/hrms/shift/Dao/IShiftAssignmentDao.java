package com.erp.hrms.shift.Dao;

import java.time.LocalDate;
import java.util.List;

import com.erp.hrms.shift.entity.Shift;
import com.erp.hrms.shift.entity.ShiftAssignment;

import net.bytebuddy.asm.Advice.Local;

public interface IShiftAssignmentDao {
	
	public void saveAssignedShift( ShiftAssignment shiftAssignment);
	
	public List<ShiftAssignment> getAlEmployeeShifr();
	
	public List<ShiftAssignment> getAllEmployeeShiftById(long id);
	
	public ShiftAssignment currentShftById(long id);
	
	public ShiftAssignment updateShift( ShiftAssignment asign , long id);
	
	public List<ShiftAssignment> findByDate(LocalDate date);
	
	public List<ShiftAssignment> findshiftById(long id , LocalDate date);
	
	List<ShiftAssignment> findByShift_ShiftNameAndStartDate(String shiftName, LocalDate date);
;

}
