package com.erp.hrms.academiccalender.service;

import java.math.BigDecimal;
import java.util.List;

import com.erp.hrms.academiccalender.entity.AcademicCalendar;

public interface ICalendarService {

	public List<AcademicCalendar> getAllHolidays();

	public void addHoliday(AcademicCalendar academicCalendar);

	public List<AcademicCalendar> getAllHolidaysWithCountry(String countryName);

	public AcademicCalendar updateHoliday(Long holidayId, AcademicCalendar academicCalendar);

	public List<AcademicCalendar> getAllHolidaysWithYearAndCountry(int year, String countryName);

	public AcademicCalendar getHolidayByid(Long holidayId);

	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(int year, int month);

	public int getAllHolidaysWithYearAndMonthAndCountryName(int year, int month, String countryName);

}
