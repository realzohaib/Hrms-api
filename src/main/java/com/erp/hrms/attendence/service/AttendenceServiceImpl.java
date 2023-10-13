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
	
	//function to calculate time difference between two timestamp
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
	public Attendence punchout(long id) {
		Attendence attendence = repo.getById(id);
		Timestamp timestamp = time();
		attendence.setPunchOutTime(timestamp);
		
		Timestamp punchInTime = attendence.getPunchInTime();
		Timestamp punchOutTime = attendence.getPunchOutTime();
		
		long calculateTotalDurationInMillis = calculateBreakDurationInMillis(punchInTime, punchOutTime);
		attendence.setWorkingHours(calculateTotalDurationInMillis);
		
		return repo.save(attendence);
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
	        long breakDurationInMillis = calculateBreakDurationInMillis(breakToEnd.getBreakStart(), breakToEnd.getBreakEnd());
	        breakToEnd.setTotalBreak(breakDurationInMillis);

	        // Save the changes to the database
	        repo.save(data);
	    }

	    return data;
	}

	private Breaks findBreakToEnd(List<Breaks> breakList) {
	    // Implement your logic to find the break that needs to be ended.
	    // This may involve checking for a null 'breakEnd' property, or you may have other criteria.
	    // You can also add error handling if the logic doesn't find a break to end.

	    // Example: Find the first break with a null 'breakEnd' property
	    return breakList.stream()
	        .filter(breaks -> breaks.getBreakEnd() == null)
	        .findFirst()
	        .orElse(null);
	}

	@Override
	public List<Attendence> getAttendenceByDate(Long employeeId, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		return repo.findByEmployeeIdAndDateBetween(employeeId, startDate, endDate);
	}

}
