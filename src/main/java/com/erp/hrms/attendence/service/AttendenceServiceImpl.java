package com.erp.hrms.attendence.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.attendence.entity.Attendence;
import com.erp.hrms.attendence.entity.Breaks;
import com.erp.hrms.attendence.repo.IAttendencerepo;
import com.erp.hrms.shift.Dao.ShiftAssignmentDaoImpl;

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
	public long calculateBreakDurationInMillis(Timestamp breakStart, Timestamp breakEnd) {
		// Convert Timestamp to Instant
		Instant startInstant = breakStart.toInstant();
		Instant endInstant = breakEnd.toInstant();

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
			// date from request
			LocalDate date = attendence.getDate();
			long employeeId = attendence.getEmployeeId();

			// day of week
			DayOfWeek dayOfWeek = date.getDayOfWeek();

			Timestamp timestamp = time();

			System.out.println(timestamp);

			Attendence obj = new Attendence();

			obj.setDate(date);
			obj.setDay(dayOfWeek);
			obj.setPunchInTime(timestamp);
			obj.setEmployeeId(employeeId);

			repo.save(obj);

			return obj;
		} catch (Exception e) {
			throw new AttendencenotRegistered("Attendence not recorded");
		}
	}

	@Override
	public List<Attendence> getEmployeeAttendence(long employeeId) {
		List<Attendence> list = repo.findByEmployeeId(employeeId);
		return list;
	}

	@Override
	public Attendence punchout(long id) throws AttendencenotRegistered {
		try {
			Attendence attendence = repo.getById(id);
			Timestamp timestamp = time();
			attendence.setPunchOutTime(timestamp);

			Timestamp punchInTime = attendence.getPunchInTime();
			Timestamp punchOutTime = attendence.getPunchOutTime();

			if (punchInTime != null && punchOutTime != null) {
// here the function name is calculateBreakDurationInMillis but we are calculating time difference between punchOut and punchIn
// calculateTotalDurationInMillis means total working hrs from punch in to punch out i	   	
				long calculateTotalDurationInMillis = calculateBreakDurationInMillis(punchInTime, punchOutTime);
				attendence.setWorkingHours(calculateTotalDurationInMillis);

				// Constants for better readability and maintainability
				final long FULL_SHIFT_DURATION = 39600000; // 11 hours in milliseconds
				final double HALF_DAY_THRESHOLD = 0.85;
				final double ONE_HOUR = 3600000;

				if (calculateTotalDurationInMillis > FULL_SHIFT_DURATION
						&& calculateTotalDurationInMillis < FULL_SHIFT_DURATION + ONE_HOUR + ONE_HOUR) {
					attendence.setNormalWorkingDay(true);
				}

				if (calculateTotalDurationInMillis < HALF_DAY_THRESHOLD * FULL_SHIFT_DURATION) {
					attendence.setHalfDay(true);
				}
//jab tak status apptoved nahi hai tab tak emp ke portal par nahi dikheaga overtime
// added extra one houre for break time inclusion			
				if (calculateTotalDurationInMillis > FULL_SHIFT_DURATION) {
					long overtime = calculateTotalDurationInMillis - FULL_SHIFT_DURATION
							+ new Double(ONE_HOUR).longValue();
					if (overtime > ONE_HOUR) {
						attendence.setOverTime(overtime);
						attendence.setOvertimeStatus("PENDING");
					} else {
						attendence.setOverTime(0);
					}
				}
			}

			return repo.save(attendence);
		} catch (Exception e) {
			throw new AttendencenotRegistered("Attendence not recorded");
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

		// Find the first break with a null 'breakEnd' property
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
		int daysPresnt = 0;
		int halfDays = 0;
		long totalOvertimehrsInMont = 0;
		int totalNormalWorkingDay = 0;

		for (Attendence atd : attendanceForMonth) {
			if (atd.isHalfDay() == true)
				halfDays++;

			if (atd.getPunchInTime() != null && atd.getPunchOutTime() != null)
				daysPresnt++;

			if ("APPROVED".equals(atd.getOvertimeStatus()) || "UPDATED".equals(atd.getOvertimeStatus())) {
				long overTime = atd.getOverTime();
				totalOvertimehrsInMont += overTime;
			}

			if (atd.isNormalWorkingDay() == true) {
				totalNormalWorkingDay++;
			}
		}

		attendenceResponse.setAttendence(attendanceForMonth);
		attendenceResponse.setTotalWorkigDaysInMonth(workingDays);
		attendenceResponse.setTotalDaysPresentInMonth(daysPresnt);
		attendenceResponse.setTotalHalfDaysInMonth(halfDays);
		attendenceResponse.setTotalOvertimeHoursInMonth(totalOvertimehrsInMont);
		attendenceResponse.setShift(shift.currentShftById(employeeId));
		attendenceResponse.setNoOfDaysWorkedRegularHours(totalNormalWorkingDay);

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

}
