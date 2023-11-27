package com.erp.hrms.shift.Dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.shift.entity.ShiftAssignment;
import com.erp.hrms.shift.repo.ShiftAssignmentRepo;

@Service
public class ShiftAssignmentDaoImpl implements IShiftAssignmentDao {

	@Autowired
	private ShiftAssignmentRepo repo;

	@Override
	public void saveAssignedShift(ShiftAssignment shiftAssignment) {
		repo.save(shiftAssignment);

	}

	@Override
	public List<ShiftAssignment> getAlEmployeeShifr() {
		return repo.findAll();

	}

	@Override
	public List<ShiftAssignment> getAllEmployeeShiftById(long id) {
		return repo.findByEmployeeId(id);
	}

	@Override
	public ShiftAssignment currentShftById(long id) {
		return repo.findShiftAssignmentWithMaxAssignmentIdByEmployeeId(id);
	}

	@Override
	public ShiftAssignment updateShift(ShiftAssignment asign, long id) {
		asign.setEmployeeId(id);
		return repo.save(asign);
	}

	@Override
	public List<ShiftAssignment> findByDate(LocalDate date) {
		// TODO Auto-generated method stub
		return repo.findByStartDate(date);
	}

	@Override
	public List<ShiftAssignment> findshiftById(long id, LocalDate date) {
		// TODO Auto-generated method stub
		return repo.findShiftAssignmentsForDate(id, date);
	}

	@Override
	public List<ShiftAssignment> findByShift_ShiftNameAndStartDate(String shiftName, LocalDate date) {
		return repo.findByShift_ShiftNameAndStartDate(date, shiftName);
	}

}
