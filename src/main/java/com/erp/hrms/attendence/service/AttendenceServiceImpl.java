package com.erp.hrms.attendence.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.attendence.entity.Attendence;
import com.erp.hrms.attendence.entity.Breaks;
import com.erp.hrms.attendence.repo.IAttendencerepo;
import com.erp.hrms.helper.entity.monthCycle;
import com.erp.hrms.helper.service.IMonthCycleService;
import com.erp.hrms.shift.Dao.ShiftAssignmentDaoImpl;
import com.erp.hrms.shift.entity.ShiftAssignment;
import com.erp.hrms.weekOff.service.IweekOffService;
import com.erp.hrms.weekOffEntity.WeekOff;

@Service
public class AttendenceServiceImpl implements IAttendenceService {
	@Autowired
	private IAttendencerepo repo;

	@Autowired
	private ShiftAssignmentDaoImpl shift;

	@Autowired
	private IMonthCycleService monthcycle;
	
	@Autowired
	private IweekOffService weekoff;
	
	/*
	 * check admin notes in mobile
	 */	public static long totalshifthrs= 0 ;

	private boolean isWorkingDay(DayOfWeek dayOfWeek , long emp) {
		WeekOff getweekOff = weekoff.getweekOff(emp);
		String individualDayOff = getweekOff.getIndividualDayOff();
		DayOfWeek off = DayOfWeek.valueOf(individualDayOff);
		return dayOfWeek != off;
	}

	// function to calculate time difference between two timestamp
	public long calculateBreakDurationInMillis(Timestamp timestamp1, Timestamp timestamp2) {
		// Convert Timestamp to Instant
		Instant startInstant = timestamp1.toInstant();
		Instant endInstant = timestamp2.toInstant();

		// Calculate the duration between breakStart and breakEnd
		Duration duration = Duration.between(startInstant, endInstant);

		// Convert the Duration to milliseconds
		return duration.toMillis();
	}

	// function to generate timestamp
	public Timestamp time() {
		// Create a ZonedDateTime in GMT+04:00
		ZonedDateTime gmtPlus4 = ZonedDateTime.now(ZoneOffset.ofHours(4));

		// Convert the ZonedDateTime to a Timestamp without converting to Instant
		Timestamp timestamp = Timestamp.valueOf(gmtPlus4.toLocalDateTime());

		return timestamp;
	}

	@Override
	public Attendence punchIn(Attendence attendence) throws AttendencenotRegistered {
		try {
			// Extracting details from the request
			LocalDate date = attendence.getDate();
			long employeeId = attendence.getEmployeeId();

			// Retrieve the current shift assignment for the employee
			ShiftAssignment currentShiftByOfEmployee = shift.currentShftById(employeeId);
						

			// Extracting shift details
			LocalTime scheduledStartTime = currentShiftByOfEmployee.getShift().getShiftStartTime();
			
			// Extracting shift details
			LocalTime shiftEndTime = currentShiftByOfEmployee.getShift().getShiftEndTime();
			
			//it has no use here , it is being used in shift hrs calculation , it has implementation in punch out.
	        Duration duration = Duration.between(scheduledStartTime, shiftEndTime);
	        long millis = duration.toMillis();
	        totalshifthrs=millis;
	        
			// Adding a 20-minute buffer time
			LocalTime allowedStartTime = scheduledStartTime.plusMinutes(20);

			// Extracting the day of the week
			DayOfWeek dayOfWeek = date.getDayOfWeek();

			// Getting the current timestamp
			Timestamp timestamp = time();

			// Creating an Attendance object
			Attendence obj = new Attendence();
			obj.setDate(date);
			obj.setDay(dayOfWeek);
			obj.setPunchInTime(timestamp);
			obj.setEmployeeId(employeeId);

			// Checking for tardiness
			LocalTime actualStartTime = timestamp.toLocalDateTime().toLocalTime();
			obj.setTardyDay(actualStartTime.isAfter(allowedStartTime));

			// Saving the attendance record
			repo.save(obj);

			return obj;
		} catch (Exception e) {
			throw new AttendencenotRegistered("Attendance not recorded");
		}
	}

	@Override
	public List<Attendence> getEmployeeAttendence(long employeeId) {
		List<Attendence> list = repo.findByEmployeeId(employeeId);
		return list;
	}

	@Override
	public Attendence punchOut(long id) throws AttendencenotRegistered {
		try {
			Attendence attendence = repo.getById(id);
			Timestamp punchOutTime = time();
			attendence.setPunchOutTime(punchOutTime);

			Timestamp punchInTime = attendence.getPunchInTime();
			if (punchInTime != null) {
				long totalDurationInMillis = calculateBreakDurationInMillis(punchInTime, punchOutTime);
				attendence.setWorkingHours(totalDurationInMillis);
				

				// Constants for better readability and maintainability
//				final long FULL_SHIFT_DURATION = 12 * 60 * 60 * 1000; // 12 hours in milliseconds
				final long FULL_SHIFT_DURATION = totalshifthrs;
				final double HALF_DAY_THRESHOLD = 0.85;
				final double HALF_DAY_THRESHOLD2 = 0.45;
				final long ONE_HOUR = 60 * 60 * 1000;

				// Set half day flag
				if(totalDurationInMillis <= HALF_DAY_THRESHOLD * FULL_SHIFT_DURATION  && totalDurationInMillis >= HALF_DAY_THRESHOLD2 * FULL_SHIFT_DURATION)
				attendence.setHalfDay(true);
				
				if(totalDurationInMillis >= HALF_DAY_THRESHOLD2 * FULL_SHIFT_DURATION) {
					attendence.setCountable(true);					
				}else {
					attendence.setCountable(false);					
				}
				
				//isme 45 % se kam hua to leave cut karnui hai , usme kaam karna hai

				// Set normal working day flag
				if (totalDurationInMillis >= HALF_DAY_THRESHOLD * FULL_SHIFT_DURATION
						&& totalDurationInMillis < FULL_SHIFT_DURATION + ONE_HOUR) {
					attendence.setNormalWorkingDay(true);
				}

				// jab tak status apptoved nahi hai tab tak emp ke portal par nahi dikheaga
				// overtimr
				if (totalDurationInMillis > FULL_SHIFT_DURATION) {
					long overtime = totalDurationInMillis - (FULL_SHIFT_DURATION + ONE_HOUR);
					if (overtime > ONE_HOUR) {
						attendence.setOverTime(overtime);
						attendence.setOvertimeStatus("PENDING");
					}
				}
			}

			return repo.save(attendence);
		} catch (Exception e) {
			throw new AttendencenotRegistered("Attendance not recorded");
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Attendence breakStart(long Attendenceid) {
		Attendence data = repo.getById(Attendenceid);
		List<Breaks> breakList = data.getBreaks();
		Breaks breaks = new Breaks();
		Timestamp timestamp = time();
		breaks.setBreakStart(timestamp);

		breaks.setAttendence(data);
		breakList.add(breaks);
		data.setBreaks(breakList);

		repo.save(data);

		return data;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Attendence breakEnd(long Attendenceid) {
		Attendence data = repo.getById(Attendenceid);
		List<Breaks> breakList = data.getBreaks();

		// Find the break that needs to be ended
		Breaks breakToEnd = findBreakToEnd(breakList);

		if (breakToEnd != null) {
			Timestamp timestamp = time();
			breakToEnd.setBreakEnd(timestamp);

			// Calculate break duration
			long breakDurationInMillis = calculateBreakDurationInMillis(breakToEnd.getBreakStart(),
					breakToEnd.getBreakEnd());
			breakToEnd.setTotalBreak(breakDurationInMillis);

			// Save the changes to the database
			repo.save(data);
		}

		return data;
	}

	private Breaks findBreakToEnd(List<Breaks> breakList) {
		// Implement your logic to find the break that needs to be ended.
		// This may involve checking for a null 'breakEnd' property, or you may have
		// other criteria.
		// You can also add error handling if the logic doesn't find a break to end.

		// Example: Find the first break with a null 'breakEnd' property
		return breakList.stream().filter(breaks -> breaks.getBreakEnd() == null).findFirst().orElse(null);
	}

	@Override
	public List<Attendence> getAttendenceByDate(Long employeeId, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return repo.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
	}

	@Override
	public List<Attendence> getAttendanceForMonth(Long employeeId, int year, int month) {
		int startDay = 0;
		int endDay = 0;
		List<monthCycle> list = monthcycle.getmonthlycycle();

		for (monthCycle cycle : list) {
			startDay = cycle.getStartDate();
			endDay = cycle.getLastDate();
			break;
		}

		// Assuming the month cycle starts on the 27th of the last month
		LocalDate startDate = LocalDate.of(year, month, startDay).minusMonths(1);
		System.out.println("start date is " + startDate);

		// Ensure endDay is not greater than the maximum day of the month
		endDay = Math.min(endDay, startDate.plusMonths(1).lengthOfMonth());

		// Calculate the end date by adding one month
		LocalDate endDate = startDate.plusMonths(1);
		endDate = endDate.withDayOfMonth(endDay);

		System.out.println("end date is " + endDate);

		return repo.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
	}

	@Override
	public AttendenceResponse fullAttendence(Long employeeId, int year, int month) {
		int startDay = 0;
		int endDay = 0;
		List<monthCycle> list = monthcycle.getmonthlycycle();

		for (monthCycle cycle : list) {
			startDay = cycle.getStartDate();
			endDay = cycle.getLastDate();
			break;
		}

		LocalDate currentDate = LocalDate.now();
		// LocalDate currentDate = LocalDate.of(2023, 11, 25);

		if (currentDate.isAfter(currentDate.withDayOfMonth(endDay))) {
			month++;
		}
		List<Attendence> attendanceForMonth = getAttendanceForMonth(employeeId, year, month);

		AttendenceResponse attendenceResponse = new AttendenceResponse();
		int workingDays = calculateWorkingDays(year, month , employeeId);
		int daysPresent = 0;
		int halfDays = 0;
		long totalOvertimeHoursInMonth = 0;
		int noOfDaysWorkedRegularHours = 0;
		int tardyDays = 0;
		int countableDays=0;//ye sirf salary count ke liye use hoga , agar vo half day se pehle chala gaya to attendence ka record to rakhenge but uski leave maanninjayegi.

		for (Attendence atd : attendanceForMonth) {
			if (atd.isHalfDay()) {
				halfDays++;
			}

			if (atd.getPunchInTime() != null && atd.getPunchOutTime() != null) {
				daysPresent++;
			}

			if ("APPROVED".equals(atd.getOvertimeStatus()) || "UPDATED".equals(atd.getOvertimeStatus())) {
				totalOvertimeHoursInMonth += atd.getOverTime();
			}

			if (atd.isNormalWorkingDay()) {
				noOfDaysWorkedRegularHours++;
			}

			if (atd.isHalfDay()) {
				tardyDays++;
			}
			
			if(atd.isCountable()) {
				countableDays++;
			}
		}

		attendenceResponse.setAttendence(attendanceForMonth);
		attendenceResponse.setTotalWorkigDaysInMonth(workingDays);
		attendenceResponse.setTotalDaysPresentInMonth(daysPresent);
		attendenceResponse.setTotalHalfDaysInMonth(halfDays);
		attendenceResponse.setTotalOvertimeHoursInMonth(totalOvertimeHoursInMonth);
		attendenceResponse.setShift(shift.currentShftById(employeeId));
		attendenceResponse.setNoOfDaysWorkedRegularHours(noOfDaysWorkedRegularHours);
		attendenceResponse.setTotalTardyDays(tardyDays);
		attendenceResponse.setTotalSalaryDays(countableDays);

		return attendenceResponse;
	}

	@Override
	public int calculateWorkingDays(int year, int month , long employeeId) {
		int workingDays = 0;
		int startDay = 0;
		int endDay = 0;
		List<monthCycle> list = monthcycle.getmonthlycycle();

		for (monthCycle cycle : list) {
			startDay = cycle.getStartDate();
			endDay = cycle.getLastDate();
			break;
		}
		 LocalDate currentDate = LocalDate.now();
		//LocalDate currentDate = LocalDate.of(2023, 12, 03);

		LocalDate startDate = LocalDate.of(year, month, startDay).minusMonths(1);

		// Check if currentDate is after endDay, then update startDate to be the day
		// after endDay
		if (currentDate.isAfter(startDate.withDayOfMonth(endDay))) {
			startDate = startDate.withDayOfMonth(endDay).plusDays(1);
		}

		if (currentDate.isAfter(currentDate.withDayOfMonth(endDay)) || currentDate.getMonthValue() != month) {
			if (!(currentDate.isBefore(startDate.withDayOfMonth(startDate.lengthOfMonth()))
					|| currentDate.isEqual(startDate.withDayOfMonth(startDate.lengthOfMonth())))) {
				currentDate = currentDate.withDayOfMonth(endDay).withMonth(month);
			}

		}

		while (!currentDate.isBefore(startDate)) {
			DayOfWeek dayOfWeek = startDate.getDayOfWeek();

			// Check if the day is a working day based on your criteria
			if (isWorkingDay(dayOfWeek , employeeId)) {
				workingDays++;
			}

			startDate = startDate.plusDays(1);
		}

		return workingDays;
	}

	@Override
	// method to fetch all employee with pending request for overtime approval
	public List<Attendence> getEmployeeWithOverTimeStatusPending() {
		return repo.findByOvertimeStatus("PENDING");

	}

	// this method is to approve overtime request
	// id is not employee id , it is AttendenceId
	@Override
	@SuppressWarnings("deprecation")
	public void approveOverTime(Long id) {
		Attendence employee = repo.getById(id);
		employee.setOvertimeStatus("APPROVED");
		repo.save(employee);
	}

	/// this method is to deny overtime request
	// id is not employee id , it is AttendenceId
	@Override
	public void denyOverTime(Long id) {
		Attendence employee = repo.getById(id);
		employee.setOverTime(0);
		employee.setOvertimeStatus("DENIED");
		repo.save(employee);
	}

	// this method is to update overtime
	// id is not employee id , it is AttendenceId
	@Override
	public Attendence updateOverTime(Attendence attendence) {

		long id = attendence.getAttendenceId();
		Attendence atd = repo.getById(id);

		// updating Information
		atd.setOverTime(attendence.getOverTime());
		atd.setOvertimeStatus("UPDATED");
		// saving the object to dbs
		repo.save(atd);

		return atd;
	}

	@Override
	public Attendence getAttendenceId(Long attendenceId) throws IOException {
		try {
			Attendence attendanceid = repo.getById(attendenceId);
			return attendanceid;
		} catch (Exception e) {
			throw new RuntimeException(
					"An error occurred while retrieving attendence by attendence id: " + attendenceId);
		}

	}

}
