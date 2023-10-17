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

@Service
public class AttendenceServiceImpl implements IAttendenceService {
	@Autowired
	IAttendencerepo repo;

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
			// Handle the exception or log it
			e.printStackTrace(); // You should replace this with proper logging
			throw new AttendencenotRegistered("Attendence not recorded" + e);
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
	            long calculateTotalDurationInMillis = calculateBreakDurationInMillis(punchInTime, punchOutTime);
	            attendence.setWorkingHours(calculateTotalDurationInMillis);

	            // Constants for better readability and maintainability
	            final long FULL_SHIFT_DURATION = 39600000; // 11 hours in milliseconds
	            final double HALF_DAY_THRESHOLD = 0.85;

	            if (calculateTotalDurationInMillis < HALF_DAY_THRESHOLD * FULL_SHIFT_DURATION) {
	                attendence.setHalfDay(true);
	            }

	            if (calculateTotalDurationInMillis > FULL_SHIFT_DURATION) {
	                long overtime = calculateTotalDurationInMillis - FULL_SHIFT_DURATION;
	                attendence.setOverTime(overtime);
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
		     List<Attendence> list = repo.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
		     
		     int workingDays = calculateWorkingDays(year, month);
		     int daysPresnt=0;
		     int halfDays=0;
		     long totalOvertimehrsInMont=0;
		     
		     Attendence attendence = new Attendence();
		     
		     for(Attendence singleAttendence : list) {
		    	 Timestamp punchInTime = singleAttendence.getPunchInTime();
		    	 Timestamp punchOutTime = singleAttendence.getPunchOutTime();
		    	 
	    		 if( (punchInTime != null && punchOutTime != null) ) daysPresnt++;

		    	 if(singleAttendence.isHalfDay()) halfDays++;
		    	 
		    	 long overTime = singleAttendence.getOverTime();
		    	 totalOvertimehrsInMont+=overTime;
		    	 
		    	 attendence.setDate(singleAttendence.getDate());
		    	 attendence.setDay(singleAttendence.getDay());
		    	 attendence.setPunchInTime(punchInTime);
		    	 attendence.setPunchOutTime(punchOutTime);
		    	 attendence.setWorkingHours(singleAttendence.getWorkingHours());
		    	 if(singleAttendence.isHalfDay()) {
		    		 attendence.setHalfDay(true);
		    	 }else {
		    		 attendence.setHalfDay(false);
		    	 }
		    	 attendence.setOverTime(singleAttendence.getOverTime());
		    	
		     }
		     
		     attendence.setTotalWorkigDaysInMonth(workingDays);
	    	 attendence.setTotalDaysPresentInMonth(daysPresnt);
	    	 
		     
		     return null;
		   // return repo.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
	}

	@Override
	public int calculateWorkingDays(int year, int month) {
	    int workingDays = 0;
	    LocalDate date = LocalDate.of(year, month, 1);

	    while (date.getMonthValue() == month) {
	        DayOfWeek dayOfWeek = date.getDayOfWeek();

	        // Check if the day is a working day based on your criteria
	        if (isWorkingDay(dayOfWeek)) {
	            workingDays++;
	        }

	        date = date.plusDays(1);
	    }

	    return workingDays;
	}

	private boolean isWorkingDay(DayOfWeek dayOfWeek) {
	    // Implement your organization's working day criteria here
	    return dayOfWeek != DayOfWeek.SUNDAY;
	}


}
