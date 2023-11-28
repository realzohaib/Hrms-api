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
import com.erp.hrms.shift.Dao.ShiftAssignmentDaoImpl;
import com.erp.hrms.shift.entity.ShiftAssignment;

@Service
public class AttendenceServiceImpl implements IAttendenceService {
	@Autowired
	private IAttendencerepo repo;

	@Autowired
	private ShiftAssignmentDaoImpl shift;

	private boolean isWorkingDay(DayOfWeek dayOfWeek) {
		// Implement your organization's working day criteria here
		return dayOfWeek != DayOfWeek.SUNDAY;
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
				final long FULL_SHIFT_DURATION = 11 * 60 * 60 * 1000; // 11 hours in milliseconds
				final double HALF_DAY_THRESHOLD = 0.85;
				final long ONE_HOUR = 60 * 60 * 1000;

				// Set half day flag
				attendence.setHalfDay(totalDurationInMillis < HALF_DAY_THRESHOLD * FULL_SHIFT_DURATION);

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
		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = startDate.plusMonths(1).minusDays(1);
		return repo.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
	}

	@Override
	public AttendenceResponse fullAttendence(Long employeeId, int year, int month) {
		List<Attendence> attendanceForMonth = getAttendanceForMonth(employeeId, year, month);

		AttendenceResponse attendenceResponse = new AttendenceResponse();
		int workingDays = calculateWorkingDays(year, month);
		int daysPresent = 0;
		int halfDays = 0;
		long totalOvertimeHoursInMonth = 0;
		int noOfDaysWorkedRegularHours = 0;
		int tardyDays = 0;

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
		}

		attendenceResponse.setAttendence(attendanceForMonth);
		attendenceResponse.setTotalWorkigDaysInMonth(workingDays);
		attendenceResponse.setTotalDaysPresentInMonth(daysPresent);
		attendenceResponse.setTotalHalfDaysInMonth(halfDays);
		attendenceResponse.setTotalOvertimeHoursInMonth(totalOvertimeHoursInMonth);
		attendenceResponse.setShift(shift.currentShftById(employeeId));
		attendenceResponse.setNoOfDaysWorkedRegularHours(noOfDaysWorkedRegularHours);
		attendenceResponse.setTotalTardyDays(tardyDays);

		return attendenceResponse;
	}

	@Override
	public int calculateWorkingDays(int year, int month) {
		int workingDays = 0;
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = LocalDate.of(year, month, 1);

		while (!currentDate.isBefore(startDate) && currentDate.getMonthValue() == month) {
			DayOfWeek dayOfWeek = startDate.getDayOfWeek();

			// Check if the day is a working day based on your criteria
			if (isWorkingDay(dayOfWeek)) {
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
