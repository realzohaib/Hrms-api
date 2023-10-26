package com.erp.hrms.shift.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.shift.entity.Shift;
import com.erp.hrms.shift.repo.IShiftRepo;

@Service
public class ShiftDaoImpl implements IShiftDao {

	@Autowired
	private IShiftRepo repo;

	@Override
	public void saveShift(Shift shift) {
		try {
			repo.save(shift);
		} catch (Exception e) {
			throw new RuntimeException(" invalid data ");
		}
	}

	@Override
	public List<Shift> getAllShifts() {
		return repo.findAll();
	}

	@Override
	public Shift updateShift(Shift shift) {
		Shift shift2 = new Shift();
		shift2.setShiftId(shift.getShiftId());
		shift2.setShiftName(shift.getShiftName());
		shift2.setShiftStartTime(shift.getShiftStartTime());
		shift2.setShiftEndTime(shift.getShiftEndTime());
		
		return repo.save(shift2);

	}

	

}
