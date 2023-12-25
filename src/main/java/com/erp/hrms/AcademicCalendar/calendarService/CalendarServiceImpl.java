package com.erp.hrms.AcademicCalendar.calendarService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.erp.hrms.AcademicCalendar.calendarRepository.CalendarRepository;
import com.erp.hrms.AcademicCalendar.entity.AcademicCalendar;

@Service
public class CalendarServiceImpl implements ICalendarService {

	@Autowired
	private CalendarRepository calendarReposiroty;

	@Override
	public List<AcademicCalendar> getAllHolidays() {
		try {
			return calendarReposiroty.findAll();
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retreving holidays.");
		}
	}

	@Override
	public void addHoliday(AcademicCalendar academicCalendar) {
		try {
			calendarReposiroty.save(academicCalendar);
		} catch (DataAccessException e) {
			throw new RuntimeException("An error occurred while saving the holiday.", e);
		}
	}

	@Override
	public List<AcademicCalendar> getAllHolidaysWithCountry(String countryName) {
		try {
			List<AcademicCalendar> findByCountry = calendarReposiroty.findByCountry(countryName);
			return findByCountry;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving holidays by country name: " + countryName);
		}
	}

	@Override
	public AcademicCalendar updateHoliday(Long holidayId, AcademicCalendar academicCalendar) {
		AcademicCalendar existingHoliday = calendarReposiroty.findByHolidayId(holidayId);
		if (existingHoliday == null) {
			throw new EntityNotFoundException("Holiday not found with id: " + holidayId);
		}
		try {
			existingHoliday.setName(academicCalendar.getName());
			existingHoliday.setStartHolidayDate(academicCalendar.getStartHolidayDate());
			existingHoliday.setEndHolidayDate(academicCalendar.getEndHolidayDate());
			existingHoliday.setNumberOfHoliday(academicCalendar.getNumberOfHoliday());
			existingHoliday.setCountryName(academicCalendar.getCountryName());

			return calendarReposiroty.save(existingHoliday);
		} catch (DataAccessException e) {
			throw new RuntimeException("An error occurred while updating the holiday.", e);
		}
	}

	@Override
	public List<AcademicCalendar> getAllHolidaysWithYearAndCountry(int year, String countryName) {
		try {
			return calendarReposiroty.findByYearAndCountry(year, countryName);
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving holidays by year and country.", e);
		}
	}

	@Override
	public AcademicCalendar getHolidayByid(Long holidayId) {
		try {
			AcademicCalendar findById = calendarReposiroty.findByHolidayId(holidayId);
			return findById;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving holidays by holiday Id: " + holidayId);
		}
	}

	@Override
	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(int year, int month) {
		return calendarReposiroty.calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(year, month);
	}

//	is mujhe sir se puchna hai ki holiday ka data get karte waqt sunday dikana hai ya sirf holiday ka count
	@Override
	public int getAllHolidaysWithYearAndMonthAndCountryName(int year, int month, String countryName) {
		int totalHolidaysInMonth = 0;
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
		List<AcademicCalendar> list = calendarReposiroty.findByCountryNameAndStartHolidayDateBetween(countryName,
				firstDayOfMonth, lastDayOfMonth);
		if (list.isEmpty()) {
			list = calendarReposiroty.findByCountryNameAndEndHolidayDateBetween(countryName, firstDayOfMonth,
					lastDayOfMonth);
		}

		for (AcademicCalendar academicCalendar : list) {
			LocalDate startHolidayDate = academicCalendar.getStartHolidayDate();
			LocalDate endHolidayDate = academicCalendar.getEndHolidayDate();
			int startMonth = startHolidayDate.getMonthValue();
			if (startMonth != month) {
				startHolidayDate = startHolidayDate.withDayOfMonth(1).plusMonths(1);
			}

			int endMonth = endHolidayDate.getMonthValue();
			if (endMonth != month) {
				endHolidayDate = startHolidayDate.withDayOfMonth(startHolidayDate.lengthOfMonth());
			}

			int daysBetweenInclusive = (int) ChronoUnit.DAYS.between(startHolidayDate, endHolidayDate) + 1;
			totalHolidaysInMonth += daysBetweenInclusive;

			LocalDate currentDate = startHolidayDate;
			while (!currentDate.isAfter(endHolidayDate)) {
				DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
				if (dayOfWeek == DayOfWeek.SUNDAY) {
					totalHolidaysInMonth -= 1;
				}
				currentDate = currentDate.plusDays(1);
			}

		}
		return totalHolidaysInMonth;

	}

}
