package com.erp.hrms.payroll.service;

import java.util.List;

import com.erp.hrms.payroll.entity.PayRoll;
import com.erp.hrms.payroll.entity.PayRollResponse;
import com.erp.hrms.payroll.entity.SalaryCerti;

public interface IPayRollService {

	public PayRollResponse getPayRollByEmpId(long empId, int year, int month);

	public PayRollResponse findPayrollForEmployeePage(long empId, int year, int month);

	public void savePayRoll(PayRoll payroll);

	public SalaryCerti getSalaryCerti(long empId, int year, int month);
	
	public List<SalaryCerti> getAllRequest();
	
	public SalaryCerti updateSalaryCertiStatus(SalaryCerti certi);


}
