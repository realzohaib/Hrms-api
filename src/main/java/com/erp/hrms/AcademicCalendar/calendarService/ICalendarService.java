package com.erp.hrms.AcademicCalendar.calendarService;

import java.math.BigDecimal;
import java.util.List;

import com.erp.hrms.AcademicCalendar.entity.Holiday;

public interface ICalendarService {

	public List<Holiday> getAllHolidays();

	public void addHoliday(Holiday holiday);

	public List<Holiday> getAllHolidaysWithCountry(String countryName);

	public Holiday updateHoliday(Long holidayId, Holiday holiday);

	public List<Holiday> getAllHolidaysWithYearAndCountry(int year, String countryName);

	public Holiday getHolidayByid(Long holidayId);

	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(int year, int month);
}
