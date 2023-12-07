package com.erp.hrms.payroll.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
import com.erp.hrms.payments.Tax;
import com.erp.hrms.payments.TaxService;
import com.erp.hrms.payroll.dao.IPayRollRepo;
import com.erp.hrms.payroll.dao.ISalaryCerti;
import com.erp.hrms.payroll.entity.Allowances;
import com.erp.hrms.payroll.entity.Deductions;
import com.erp.hrms.payroll.entity.PayRoll;
import com.erp.hrms.payroll.entity.PayRollResponse;
import com.erp.hrms.payroll.entity.SalaryCerti;

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

	@Autowired
	private TaxService tax;

	@Autowired
	private ISalaryCerti certirepo;

	private double leaveDayCut(AttendenceResponse fullAttendence, Double basicPay, PayRoll pl) {
		double leaveDayCutAmount = 0;
		int totalDaysPresentInMonthI = 20;
		double totalDaysPresentInMonth = totalDaysPresentInMonthI;
		int totalWorkigDaysInMonthI = 25;
		double totalWorkigDaysInMonth = totalWorkigDaysInMonthI;
		// solving challenge 1
		// for now this is not totalcasualleaves APPROVED IN YEAR this is total casual
		// leaves taken till current month
		double totalcasualleavesTakenTillCurrentMonth = 30;
		// same as totalcasualleavesApproved
		double totalmedicalleavesTakenTillCurrentMonth = 12;
		double casualleaveinmonth = 2;
		double medicalleaveinmonth = 2;
		double totalcasualleavesprovidedinyear = 30;
		double totalmedicalleavesprovidedinyear = 15;
		double calculateOneDayAmount = calculateOneDayAmount(pl);

		// 1 day pay = (basic pay + allowances + benefits) / (30 - Sundays)
		// double onedayamount = calculateOneDayAmount / totalWorkigDaysInMonth;

		// For testing purposes , needs to be removed
		double onedayamount = 500;

		double uninformedLeaves = totalWorkigDaysInMonth - totalDaysPresentInMonth
				- (casualleaveinmonth + medicalleaveinmonth); // 25 - 20 - (3) = 2

		if (uninformedLeaves > 0) {
			leaveDayCutAmount = uninformedLeaves * (onedayamount * 1.3); // Abhi 1.3 hardcoded hai, baad mai isse
																			// dynamic karna hai
		}

		if (totalcasualleavesTakenTillCurrentMonth > totalcasualleavesprovidedinyear) {
			double daysexceeedingcasualleavesprovided = totalcasualleavesTakenTillCurrentMonth - casualleaveinmonth;
			if (daysexceeedingcasualleavesprovided < totalcasualleavesprovidedinyear) {
				double noofdaysforamounttobededucted = totalcasualleavesTakenTillCurrentMonth
						- totalcasualleavesprovidedinyear;
				leaveDayCutAmount += onedayamount * noofdaysforamounttobededucted;
			} else {
				leaveDayCutAmount += onedayamount * casualleaveinmonth;
			}
		}

		if (totalmedicalleavesTakenTillCurrentMonth > totalmedicalleavesprovidedinyear) {
			double daysexceeedingmedicalleavesprovided = totalmedicalleavesTakenTillCurrentMonth - medicalleaveinmonth;
			if (daysexceeedingmedicalleavesprovided < totalmedicalleavesprovidedinyear) {
				double noofdaysforamounttobededucted = totalmedicalleavesTakenTillCurrentMonth
						- totalmedicalleavesprovidedinyear;
				leaveDayCutAmount += onedayamount * noofdaysforamounttobededucted;
			} else {
				leaveDayCutAmount += onedayamount * medicalleaveinmonth;
			}
		}

		return leaveDayCutAmount;
	}

	private double calculateOneDayAmount(PayRoll payRoll) {
		// Convert String allowances to double
		double houseRentAmount = Double.parseDouble(payRoll.getAllowances().getHouseRentAmount());
		double foodAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getFoodAllowanceAmount());
		double vehicleAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getVehicleAllowanceAmount());
		double uniformAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getUniformAllowanceAmount());
		double travellingAllowancesAmount = Double.parseDouble(payRoll.getAllowances().getTravellingAllowancesAmount());
		double educationalAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getEducationalAllowanceAmount());
		double otherAllowanceAmount = Double.parseDouble(payRoll.getAllowances().getOtherAllowanceAmount());

		// Perquisites
		double vehicleCashAmount = payRoll.getVehicleCashAmount();
		double electricityAllocationAmount = payRoll.getElectricityAllocationAmount();
		double rentAllocationAmount = payRoll.getRentAllocationAmount();

		double basicPay = payRoll.getBasicPay();

		// Sum up all amounts
		double total = houseRentAmount + foodAllowanceAmount + vehicleAllowanceAmount + uniformAllowanceAmount
				+ travellingAllowancesAmount + educationalAllowanceAmount + otherAllowanceAmount + vehicleCashAmount
				+ electricityAllocationAmount + rentAllocationAmount + basicPay;

		return total;
	}

	private double calculateDeductions(Deductions de, double taxAmount) {
		return de.getLeaveDayCut() + de.getTardyDayCut() + de.getHalfDayCut() + taxAmount;
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
		double overtimePay = payRoll.getOvertimePay();

		// Perquisites
		double vehicleCashAmount = payRoll.getVehicleCashAmount();
		double electricityAllocationAmount = payRoll.getElectricityAllocationAmount();
		double rentAllocationAmount = payRoll.getRentAllocationAmount();

		// Calculate total pay
		double totalPay = vehicleCashAmount + electricityAllocationAmount + rentAllocationAmount + basicPay
				+ totalAllowances + anySpecialRewardAmount + bonus + monthyPerformancePay + incentiveAmount
				+ overtimePay;

		return totalPay;
	}

	private double taxCalculation(PayRoll pay) {
		List<Tax> taxcal = pay.getTax();
		double basicPay = pay.getBasicPay();
		double taxAmout = 0;
		for (Tax tax : taxcal) {
			double salaryPercentOfTax = tax.getSalaryPercentOfTax();
			taxAmout += (basicPay * salaryPercentOfTax) / 100;
		}
		return taxAmout;
	}

	private LocalDate getThirdLastWorkingDay(int month, int year) {
		LocalDate lastdayofmonth = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);

		LocalDate currentDay = lastdayofmonth;
		int workingDaysCount = 0;
		while (workingDaysCount < 3) {
			if (currentDay.getDayOfWeek() != DayOfWeek.SUNDAY) {
				workingDaysCount++;
			}
			currentDay = currentDay.minusDays(1);
		}
		return currentDay.plusDays(1);

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

			// Perquisites
			double vehicleCashAmount = Double.parseDouble(job.getCashAmount());
			payRoll.setVehicleCashAmount(vehicleCashAmount);

			double electricityAllocationAmount = Double.parseDouble(job.getElectricityAllocationAmount());
			payRoll.setElectricityAllocationAmount(electricityAllocationAmount);

			double rentAllocationAmount = Double.parseDouble(job.getRentAllocationAmount());
			payRoll.setRentAllocationAmount(rentAllocationAmount);

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
		payRoll.setMethodUsedForPayment("Cash");
		payRoll.setDateOfPayment(getThirdLastWorkingDay(month, year));

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

		// calculating tax
		List<Tax> taxlist = tax.getTax();
		payRoll.setTax(taxlist);

		// setting values of deduction
		double leaveDayCut = leaveDayCut(fullAttendence, basicPay, payRoll);
		deductions.setLeaveDayCut(leaveDayCut);
		double totalDeduction = calculateDeductions(deductions, taxCalculation(payRoll));
		deductions.setTotalDeductions(totalDeduction);
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

	@Override
	public SalaryCerti getSalaryCerti(long empId, int year, int month) {
		//need to add if condition
		SalaryCerti salaryCerti = new SalaryCerti();

		salaryCerti.setEmployeeId(empId);
		salaryCerti.setMonth(month);
		salaryCerti.setYear(year);
		salaryCerti.setManagerApprovalStatusForCerti("Pending");
		return certirepo.save(salaryCerti);

	}
	
	
	@Override
	public SalaryCerti updateSalaryCertiStatus(SalaryCerti certi) {
		SalaryCerti salaryCerti = certirepo.findByEmployeeIdAndMonthAndYear(certi.getEmployeeId(), certi.getMonth(), certi.getYear());
		salaryCerti.setManagerApprovalStatusForCerti(certi.getManagerApprovalStatusForCerti());
		salaryCerti.setHrApprovalStatusForCerti(certi.getHrApprovalStatusForCerti());
		return certirepo.save(salaryCerti);

	}

	@Override
	public List<SalaryCerti> getAllRequest() {
		return certirepo.findAll();
	}
	
	

}
