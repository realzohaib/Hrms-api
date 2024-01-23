package com.erp.hrms.approver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.hrms.approver.entity.LeaveApprover;

public interface LeaveApproverRepo extends JpaRepository<LeaveApprover, Long> {

	List<LeaveApprover> findByEndDateIsNull();

	List<LeaveApprover> findByFirstApproverEmpId(Long firstApproverEmpId);

}
