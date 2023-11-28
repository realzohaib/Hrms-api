package com.erp.hrms.AcademicCalendar.calendarControlelr;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.AcademicCalendar.Holiday;
import com.erp.hrms.AcademicCalendar.calendarService.ICalendarService;
import com.erp.hrms.api.security.response.MessageResponse;

@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarController {

	@Autowired
	private ICalendarService icalendarService;

	@GetMapping("/holidays")
	public ResponseEntity<?> getAllHolidays() {
		try {
			List<Holiday> allHolidays = icalendarService.getAllHolidays();
			if (allHolidays.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("No holidays found."));
			}
			return ResponseEntity.ok(allHolidays);
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Error occurred while accessing data. " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Unexpected error occurred: " + e.getMessage()));
		}
	}

	@PostMapping("/add-holiday")
	public ResponseEntity<?> addHoliday(@RequestBody Holiday holiday) {
		try {
			icalendarService.addHoliday(holiday);
			return new ResponseEntity<>(new MessageResponse("Your holiday day is created."), HttpStatus.CREATED);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(new MessageResponse("Error while creating holiday. " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Unexpected error while creating holiday."),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getholidays/{countryName}")
	public ResponseEntity<?> getAllHolidaysWithCountry(@PathVariable("countryName") String countryName) {
		try {
			List<Holiday> allHolidaysWithCountry = icalendarService.getAllHolidaysWithCountry(countryName);
			if (allHolidaysWithCountry.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MessageResponse("No holidays found for country: " + countryName));
			}
			return ResponseEntity.ok(allHolidaysWithCountry);
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Error occurred while accessing data: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Unexpected error occurred: " + e.getMessage()));
		}
	}

	@PutMapping("/update/holiday/{holidayId}")
	public ResponseEntity<?> updateHoliday(@PathVariable Long holidayId, @RequestBody Holiday holiday) {
		try {
			icalendarService.updateHoliday(holidayId, holiday);
			return new ResponseEntity<>(new MessageResponse("Your holiday updated"), HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			return new ResponseEntity<>(new MessageResponse("Holiday not found with id: " + holidayId),
					HttpStatus.NOT_FOUND);
		} catch (DataAccessException e) {
			return new ResponseEntity<>(
					new MessageResponse("Error while updating holiday. Data access issue: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageResponse("Unexpected error while updating holiday. " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getholidays/{year}/{countryName}")
	public ResponseEntity<?> getAllHolidaysWithYearAndCountry(@PathVariable("year") int year,
			@PathVariable("countryName") String countryName) {
		try {
			List<Holiday> holidays = icalendarService.getAllHolidaysWithYearAndCountry(year, countryName);
			if (holidays.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new MessageResponse("No holidays found for the year " + year + " and country: " + countryName));
			}
			return ResponseEntity.ok(holidays);
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Error occurred while accessing data: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Unexpected error occurred: " + e.getMessage()));
		}
	}

	@GetMapping("/getholiday/{holidayId}")
	public ResponseEntity<?> getHolidayWithHolidayId(@PathVariable("holidayId") Long holidayId) {
		try {
			Holiday holidayByid = icalendarService.getHolidayByid(holidayId);
			if (holidayByid == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MessageResponse("No data found for this holiday id: " + holidayId));
			}
			return ResponseEntity.ok(holidayByid);
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Error occurred while accessing data: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Unexpected error occurred: " + e.getMessage()));
		}
	}
}
