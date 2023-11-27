package com.erp.hrms.payroll.service;

import com.erp.hrms.payroll.entity.PayRoll;

public interface IPayRollService {

	public PayRoll getPayRollByEmpId(long empId, int year, int month);

	public PayRoll findPayrollForEmployeePage(long empId, int year, int month);

	public void savePayRoll(PayRoll payroll);

}
