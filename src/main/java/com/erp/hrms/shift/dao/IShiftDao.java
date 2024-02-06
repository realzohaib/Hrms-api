package com.erp.hrms.shift.dao;

import java.util.List;

import com.erp.hrms.shift.entity.Shift;

public interface IShiftDao {
	public void saveShift(Shift shift);
	public List<Shift> getAllShifts();
	public Shift updateShift(Shift shift );

}
