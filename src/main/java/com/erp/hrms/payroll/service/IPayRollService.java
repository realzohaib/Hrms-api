package com.erp.hrms.payroll.service;

import com.erp.hrms.payroll.entity.PayRoll;

public interface IPayRollService {
	
	PayRoll getPayRollByEmpId(long empId);
	
	public PayRoll findPayrollForEmployeePage(long id);
	
	public void savePayRoll(PayRoll payroll);

}
