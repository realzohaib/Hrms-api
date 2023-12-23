package com.erp.hrms.form.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.form.LeaveApproval;

@Repository
public interface IleaveApprovalRepo extends JpaRepository<LeaveApproval, Long> {

	public List<LeaveApproval> findByEmployeeIdAndHrApprovalStatus(Long id, String hrApprovalStatus);

	List<LeaveApproval> findByEmployeeIdAndStartDateBetweenAndHrApprovalStatus(Long employeeId, String startDate,
			String endDate, String hrApprovalStatus);

	List<LeaveApproval> findByEmployeeIdAndEndDateBetweenAndHrApprovalStatus(Long employeeId, String startDate,
			String endDate, String hrApprovalStatus);

	@Query("SELECT la FROM LeaveApproval la WHERE la.startDate <= :date AND la.endDate >= :date")
	List<LeaveApproval> findByDate(@Param("date") String date);

	List<LeaveApproval> findByHrApprovalStatus(String hrApprovalStatus);

}
