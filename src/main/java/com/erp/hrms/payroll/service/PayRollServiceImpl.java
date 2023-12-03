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
import com.erp.hrms.payroll.entity.Deductions;
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

	private double leaveDayCut(AttendenceResponse fullAttendence, Double basicPay, PayRoll pl) {
		double leaveDayCutAmount = 0;
		int totalDaysPresentInMonthI = 20;
		double totalDaysPresentInMonth = totalDaysPresentInMonthI;
		int totalWorkigDaysInMonthI = 25;
		double totalWorkigDaysInMonth = totalWorkigDaysInMonthI;
		double totalcasualleavesApproved = 31;
		double totalmedicalleavesApproved = 15;
		double casualleaveinmonth = 2;
		double medicalleaveinmonth = 2;
		double totalcasualleavesprovidedinyear = 30;
		double totalmedicalleavesprovidedinyear = 15;
		double totalAllowances = calculateTotalAllowances(pl);

		// 1 day pay = (basic pay + allowances + benefits) / (30 - Sundays)
		// Benefits need to be added, details in onboarding module

		// For testing purposes
		double onedayamount = 500;

		double uninformedLeaves = totalWorkigDaysInMonth - totalDaysPresentInMonth
				- (casualleaveinmonth + medicalleaveinmonth); // 25 - 20 - (3) = 2

		if (uninformedLeaves > 0) {
			leaveDayCutAmount = uninformedLeaves * (onedayamount * 1.3); // Abhi 1.3 hardcoded hai, baad mai isse
		}

		if (totalcasualleavesApproved > totalcasualleavesprovidedinyear) {
			double daysexceeedingcasualleavesprovided = totalcasualleavesApproved - casualleaveinmonth;
			if (daysexceeedingcasualleavesprovided < totalcasualleavesprovidedinyear) {
				double noofdaysforamounttobededucted = totalcasualleavesApproved - totalcasualleavesprovidedinyear;
				leaveDayCutAmount += onedayamount * noofdaysforamounttobededucted;
			} else {
				leaveDayCutAmount += onedayamount * casualleaveinmonth;
			}
		}

		if (totalmedicalleavesApproved > totalmedicalleavesprovidedinyear) {
			double daysexceeedingmedicalleavesprovided = totalmedicalleavesApproved - medicalleaveinmonth;
			if (daysexceeedingmedicalleavesprovided < totalmedicalleavesprovidedinyear) {
				double noofdaysforamounttobededucted = totalmedicalleavesApproved - totalmedicalleavesprovidedinyear;
				leaveDayCutAmount += onedayamount * noofdaysforamounttobededucted;
			} else {
				leaveDayCutAmount += onedayamount * medicalleaveinmonth;
			}
		}

		return leaveDayCutAmount;
	}

	private double calculateTotalAllowances(PayRoll payRoll) {
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
		return totalAllowances;
	}

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
		double basicPay = payRoll.getBasicPay();
		double anySpecialRewardAmount = payRoll.getAnySpecialRewardAmount();
		double bonus = payRoll.getBonus();
		double monthyPerformancePay = payRoll.getMonthlyPerformancePay();
		double incentiveAmount = payRoll.getIncentiveAmount();
		double overtimePay = payRoll.getOvertimePayAmount();

		// Calculate total pay
		double totalPay = basicPay + totalAllowances + anySpecialRewardAmount + bonus + monthyPerformancePay
				+ incentiveAmount + overtimePay;

		return totalPay;
	}

	@Override
	public PayRollResponse getPayRollByEmpId(long empId, int year, int month) {
		PayRollResponse payRollResponse = new PayRollResponse();

		// Retrieve full attendance details
		AttendenceResponse fullAttendence = attendence.fullAttendence(empId, year, month);

		// Retrieve total leaves
		BigDecimal leaves = leave.calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(empId, year, month);

		PayRoll payroll = repo.findByEmployeeIdAndMonthAndYear(empId, month, year);
		if (payroll != null) {
			payRollResponse.setPayroll(payroll);
			payRollResponse.setAttendence(fullAttendence);
			payRollResponse.setTotalleaves(leaves);

			return payRollResponse;
		}

		// Retrieve employee details
		PersonalInfo employee = personalinfo.getPersonalInfoByEmployeeId(empId);

		// Initialize PayRoll, sDeductions and Allowances objects
		PayRoll payRoll = new PayRoll();
		Allowances allowances = new Allowances();
		Deductions deductions = new Deductions();

		// isko global variable rakha hai q ke iski value hume leaveday cut func mai
		// chahiye
		Double basicPay = 0.0;
		// Set job details from the employee's JobDetails list
		List<JobDetails> jobDetails = employee.getJobDetails();
		for (JobDetails job : jobDetails) {
			payRoll.setJobDesignation(job.getJobPostDesignation());
			payRoll.setJobLevel(job.getJobLevel());
			payRoll.setJobLocation(job.getPostedLocation());

			String string = job.getBasicPay();
			basicPay = Double.valueOf(string);
			payRoll.setBasicPay(basicPay);

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

		// method to calculate total Pay
		double totalPay = calculateTotalPay(payRoll);

		// Setting total Pay
		payRoll.setTotalPay(totalPay);

		// setting values of deduction
		double leaveDayCut = leaveDayCut(fullAttendence, basicPay, payRoll);
		deductions.setLeaveDayCut(leaveDayCut);

		payRoll.setDeductions(deductions);

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
