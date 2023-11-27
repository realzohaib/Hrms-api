package com.erp.hrms.payroll.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.payroll.entity.PayRoll;

@Repository
public interface IPayRollRepo extends JpaRepository<PayRoll, Long>{
	
	PayRoll findByemployeeId(long id);

}
