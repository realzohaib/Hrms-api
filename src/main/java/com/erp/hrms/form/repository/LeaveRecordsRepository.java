package com.erp.hrms.form.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.form.LeaveRecords;

@Repository
public interface LeaveRecordsRepository extends JpaRepository<LeaveRecords, Long> {

	public LeaveRecords findByLeaveRecordsId(Long id);
}
