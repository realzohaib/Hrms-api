package com.erp.hrms.payroll.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.hrms.payroll.entity.PayRoll;
import com.erp.hrms.payroll.entity.SalaryCerti;

public interface ISalaryCerti extends JpaRepository<SalaryCerti, Long>  {
	
	SalaryCerti findByEmployeeIdAndMonthAndYear(long employeeId, int month, int year);
}
