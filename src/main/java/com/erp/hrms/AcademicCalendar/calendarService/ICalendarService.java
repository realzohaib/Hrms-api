package com.erp.hrms.AcademicCalendar.calendarService;

import java.util.List;

import com.erp.hrms.AcademicCalendar.Holiday;

public interface ICalendarService {

	public List<Holiday> getAllHolidays();

	public void addHoliday(Holiday holiday);

	public List<Holiday> getAllHolidaysWithCountry(String countryName);

	public Holiday updateHoliday(Long holidayId, Holiday holiday);

	public List<Holiday> getAllHolidaysWithYearAndCountry(int year, String countryName);

	public Holiday getHolidayByid(Long holidayId);
}
