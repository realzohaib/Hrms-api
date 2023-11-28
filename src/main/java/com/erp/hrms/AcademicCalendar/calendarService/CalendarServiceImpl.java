package com.erp.hrms.AcademicCalendar.calendarService;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.erp.hrms.AcademicCalendar.Holiday;
import com.erp.hrms.AcademicCalendar.calendarRepository.HolidayRepository;

@Service
public class CalendarServiceImpl implements ICalendarService {

	@Autowired
	private HolidayRepository calendarReposiroty;

	@Override
	public List<Holiday> getAllHolidays() {
		try {
			return calendarReposiroty.findAll();
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retreving holidays.");
		}
	}

	@Override
	public void addHoliday(Holiday holiday) {
		try {
			calendarReposiroty.save(holiday);
		} catch (DataAccessException e) {
			throw new RuntimeException("An error occurred while saving the holiday.", e);
		}
	}

	@Override
	public List<Holiday> getAllHolidaysWithCountry(String countryName) {
		try {
			List<Holiday> findByCountry = calendarReposiroty.findByCountry(countryName);
			return findByCountry;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving holidays by country name: " + countryName);
		}
	}

	@Override
	public Holiday updateHoliday(Long holidayId, Holiday holiday) {
		Holiday existingHoliday = calendarReposiroty.findByHolidayId(holidayId);
		if (existingHoliday == null) {
			throw new EntityNotFoundException("Holiday not found with id: " + holidayId);
		}
		try {
			existingHoliday.setName(holiday.getName());
			existingHoliday.setStartHolidayDate(holiday.getStartHolidayDate());
			existingHoliday.setEndHolidayDate(holiday.getEndHolidayDate());
			existingHoliday.setNumberOfHoliday(holiday.getNumberOfHoliday());
			existingHoliday.setCountryName(holiday.getCountryName());

			return calendarReposiroty.save(existingHoliday);
		} catch (DataAccessException e) {
			throw new RuntimeException("An error occurred while updating the holiday.", e);
		}
	}

	@Override
	public List<Holiday> getAllHolidaysWithYearAndCountry(int year, String countryName) {
		try {
			return calendarReposiroty.findByYearAndCountry(year, countryName);
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving holidays by year and country.", e);
		}
	}

	@Override
	public Holiday getHolidayByid(Long holidayId) {
		try {
			Holiday findById = calendarReposiroty.findByHolidayId(holidayId);
			return findById;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving holidays by holiday Id: " + holidayId);
		}
	}
}
