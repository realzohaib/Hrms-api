package com.erp.hrms.form.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.hrms.entity.form.RemaingLeaves;

public interface RemaingLeavesRepository extends JpaRepository<RemaingLeaves, Long> {
	
	boolean existsByEmployeeid(Long employeeid);

}
