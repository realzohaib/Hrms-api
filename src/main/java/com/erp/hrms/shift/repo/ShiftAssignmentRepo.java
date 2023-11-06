package com.erp.hrms.shift.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.hrms.attendence.entity.Attendence;
import com.erp.hrms.shift.entity.Shift;
import com.erp.hrms.shift.entity.ShiftAssignment;

public interface ShiftAssignmentRepo extends JpaRepository<ShiftAssignment, Long> {

	boolean existsByEmployeeId(Long employeeId);

	List<ShiftAssignment> findByEmployeeId(long id);

	@Query("SELECT sa FROM ShiftAssignment sa WHERE sa.assignmentId = (SELECT MAX(sa2.assignmentId) FROM ShiftAssignment sa2 WHERE sa2.employeeId = :employeeId)")
	ShiftAssignment findShiftAssignmentWithMaxAssignmentIdByEmployeeId(@Param("employeeId") Long employeeId);

	List<ShiftAssignment> findByStartDate(LocalDate date);

	@Query("SELECT sa FROM ShiftAssignment sa WHERE sa.employeeId = :employeeId AND :targetDate BETWEEN sa.startDate AND sa.endDate")
	List<ShiftAssignment> findShiftAssignmentsForDate(@Param("employeeId") Long employeeId,
			@Param("targetDate") LocalDate targetDate);

	@Query("SELECT sa FROM ShiftAssignment sa WHERE sa.shift.shiftName = :shiftName AND sa.startDate = :date")
	List<ShiftAssignment> findByShift_ShiftNameAndStartDate(LocalDate date, String shiftName);

}
