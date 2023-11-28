package com.erp.hrms.payroll.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.attendence.service.AttendenceResponse;
import com.erp.hrms.attendence.service.AttendenceServiceImpl;
import com.erp.hrms.entity.JobDetails;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.form.service.LeaveService;
import com.erp.hrms.payments.OverTimePay;
import com.erp.hrms.payments.OvertimePayService;
import com.erp.hrms.payroll.dao.IPayRollRepo;
import com.erp.hrms.payroll.entity.Allowances;
import com.erp.hrms.payroll.entity.PayRoll;
import com.erp.hrms.payroll.entity.PayRollResponse;

@Service
public class PayRollServiceImpl implements IPayRollService {

	@Autowired
	private IPayRollRepo repo;

	@Autowired
	private IPersonalInfoDAO personalinfo;

	@Autowired
	private OvertimePayService overtime;

	@Autowired
	private AttendenceServiceImpl attendence;

	@Autowired
	private LeaveService leave;

	private double calculateTotalPay(PayRoll payRoll) {
		// Convert String allowances to double
		double houseRentAmount = Double.parseDouble(payRoll.getAllowances().getHouseRentAmount());
		double foodAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getFoodAllowanceAmount());
		double vehicleAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getVehicleAllowanceAmount());
		double uniformAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getUniformAllowanceAmount());
		double travellingAllowancesAmount = Double.parseDouble(payRoll.getAllowances().getTravellingAllowancesAmount());
		double educationalAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getEducationalAllowanceAmount());
		double otherAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getOtherAllowanceAmount());

		// Sum up all amounts
		double totalAllowances = houseRentAmount + foodAllowanceAmount + vehicleAllowanceAmount + uniformAllowanceAmount
				+ travellingAllowancesAmount + educationalAllowanceAmount + otherAllowanceAmount;

		// Sum up other amounts
		double anySpecialRewardAmount = payRoll.getAnySpecialRewardAmount();
		double bonus = payRoll.getBonus();
		double monthyPerformancePay = payRoll.getMonthlyPerformancePay();
		double incentiveAmount = payRoll.getIncentiveAmount();
		double overtimePay = payRoll.getOvertimePayAmount();

		// Calculate total pay
		double totalPay = totalAllowances + anySpecialRewardAmount + bonus + monthyPerformancePay + incentiveAmount
				+ overtimePay;

		return totalPay;
	}

	@Override
	public PayRollResponse getPayRollByEmpId(long empId, int year, int month) {
		PayRollResponse payRollResponse = new PayRollResponse();

		// Retrieve full attendance details
		AttendenceResponse fullAttendence = attendence.fullAttendence(empId, year, month);

		// Retrieve total leaves
		BigDecimal leaves = leave.calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(empId, year, month);

		//PayRoll payroll = repo.findByemployeeId(empId);
		PayRoll payroll = repo.findByEmployeeIdAndMonthAndYear(empId, month, year);
		if (payroll != null) {
			payRollResponse.setPayroll(payroll);
			payRollResponse.setAttendence(fullAttendence);
			payRollResponse.setTotalleaves(leaves);

			return payRollResponse;
		}

		// Retrieve employee details
		PersonalInfo employee = personalinfo.getPersonalInfoByEmployeeId(empId);

		// Initialize PayRoll and Allowances objects
		PayRoll payRoll = new PayRoll();
		Allowances allowances = new Allowances();

		// Set job details from the employee's JobDetails list
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

		// Set other employee details
		payRoll.setEmployeeId(empId);
		payRoll.setName(employee.getFirstName() + " " + employee.getLastName());
		payRoll.setDepartment(employee.getDepartment().getDepartmentName());
		payRoll.setContact(employee.getPersonalContactNo());
		payRoll.setAddress(employee.getPermanentResidentialAddress());
		payRoll.setYear(year);
		payRoll.setMonth(month);

		// calculating OverTime Ammount
		OverTimePay overtimeAmount = overtime.getovertimeAmount();
		double amount = overtimeAmount.getOvertimeAmount();
		payRoll.setOvertimePayAmount(amount);
		long totalOvertimeHoursInMonth = fullAttendence.getTotalOvertimeHoursInMonth();
		// Convert milliseconds to hours
		double totalOvertimeHoursInMonthInHours = totalOvertimeHoursInMonth / (1000.0 * 60 * 60);
		// Calculate total overtime pay
		double totalOvertimePay = totalOvertimeHoursInMonthInHours * amount;

		// Set the total overtime pay in the PayRoll object
		payRoll.setOvertimePay(totalOvertimePay);

		//method to calculate total Pay
		double totalPay = calculateTotalPay(payRoll);

		// Setting total Pay
		payRoll.setTotalPay(totalPay);

		// Set the details in the PayRollResponse
		payRollResponse.setPayroll(payRoll);
		payRollResponse.setAttendence(fullAttendence);
		payRollResponse.setTotalleaves(leaves);

		return payRollResponse;
	}

	@Override
	public void savePayRoll(PayRoll payroll) {
		System.out.println(payroll);	
		repo.save(payroll);
	}

	@Override
	public PayRollResponse findPayrollForEmployeePage(long empId, int year, int month) {
		//PayRoll payroll = repo.findByemployeeId(empId);
		PayRoll payroll = repo.findByEmployeeIdAndMonthAndYear(empId, month, year);

		if (payroll == null) {
			return getPayRollByEmpId(empId, year, month);
		}

		// Retrieve full attendance details
		AttendenceResponse fullAttendence = attendence.fullAttendence(empId, year, month);

		// Calculate and set total leaves
		BigDecimal leaves = leave.calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(empId, year, month);

		PayRollResponse response = new PayRollResponse();
		response.setPayroll(payroll);
		response.setAttendence(fullAttendence);
		response.setTotalleaves(leaves);

		return response;
	}

}
