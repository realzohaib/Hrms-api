package com.erp.hrms.payroll.service;

import com.erp.hrms.payroll.entity.PayRoll;
import com.erp.hrms.payroll.entity.PayRollResponse;

public interface IPayRollService {

	public PayRollResponse getPayRollByEmpId(long empId, int year, int month);

	public PayRollResponse findPayrollForEmployeePage(long empId, int year, int month);

	public void savePayRoll(PayRoll payroll);

}
