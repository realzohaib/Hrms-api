package com.erp.hrms.payroll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.entity.JobDetails;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.payments.OverTimePay;
import com.erp.hrms.payments.OvertimePayService;
import com.erp.hrms.payroll.dao.IPayRollRepo;
import com.erp.hrms.payroll.entity.Allowances;
import com.erp.hrms.payroll.entity.PayRoll;

@Service
public class PayRollServiceImpl implements IPayRollService {

	@Autowired
	private IPayRollRepo repo;

	@Autowired
	private IPersonalInfoDAO personalinfo;

	@Autowired
	private OvertimePayService overtime;

	@Override
	public PayRoll getPayRollByEmpId(long empId) {
		PersonalInfo employee = personalinfo.getPersonalInfoByEmployeeId(empId);

		PayRoll payRoll = new PayRoll();
		Allowances allowances = new Allowances();

		List<JobDetails> jobDetails = employee.getJobDetails();
		for (JobDetails job : jobDetails) {
			payRoll.setJobDesignation(job.getJobPostDesignation());
			payRoll.setJobLevel(job.getJobLevel());
			payRoll.setJobLocation(job.getPostedLocation());

			allowances.setHouseRentAmount(job.getHouseRentAmount());
			allowances.setFoodAllowanceAmount(job.getFoodAllowanceAmount());
			allowances.setVehicleAllowanceAmount(job.getVehicleAllowanceAmount());
			allowances.setUniformAllowanceAmount(job.getUniformAllowanceAmount());
			allowances.setTravellingAllowancesAmount(job.getTravellingAllowancesAmount());
			allowances.setEducationalAllowanceAmount(job.getEducationalAllowanceAmount());
			allowances.setOtherAllowanceAmount(job.getOtherAllowanceAmount());

			payRoll.setAllowances(allowances);

		}
		payRoll.setEmployeeId(empId);
		payRoll.setName(employee.getFirstName() + " " + employee.getLastName());
		payRoll.setDepartment(employee.getDepartment().getDepartmentName());
		payRoll.setContact(employee.getPersonalContactNo());
		payRoll.setAddress(employee.getPermanentResidentialAddress());

		// calculating overtime hours - overtime hrs runtimer par generate hoga is loye
		// ammount send kar rahe of 1 hr
		// FE par is ammount ko multiply karna hoga total overtime hors se.

		OverTimePay getovertimeAmount = overtime.getovertimeAmount();
		long overtimeAmount = getovertimeAmount.getOvertimeAmount();
		payRoll.setOvertimePayAmount(overtimeAmount);

		return payRoll;
	}
	
	
	@Override
	public void savePayRoll(PayRoll payroll) {
		repo.save(payroll);
	}

	@Override
	public PayRoll findPayrollForEmployeePage(long id) {
		PayRoll payroll = repo.findByemployeeId(id);

		if (payroll == null) {
			return getPayRollByEmpId(id);
		}

		return payroll;
	}

}
