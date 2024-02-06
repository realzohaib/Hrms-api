package com.erp.hrms.academiccalender.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.hrms.academiccalender.entity.AcademicCalendar;

public interface CalendarRepository extends JpaRepository<AcademicCalendar, Long> {

	@Query("SELECT h FROM AcademicCalendar h WHERE h.countryName = :countryName")
	public List<AcademicCalendar> findByCountry(@Param("countryName") String countryName);

	public AcademicCalendar findByHolidayId(Long id);

	@Query("SELECT h FROM AcademicCalendar h WHERE YEAR(h.startHolidayDate) = :year AND h.countryName = :countryName")
	public List<AcademicCalendar> findByYearAndCountry(@Param("year") int year,
			@Param("countryName") String countryName);

	@Query("SELECT COALESCE(SUM(h.numberOfHoliday), 0) " + "FROM AcademicCalendar h "
			+ "WHERE YEAR(h.startHolidayDate) = :year " + "AND MONTH(h.endHolidayDate) = :month ")
	BigDecimal calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(@Param("year") int year,
			@Param("month") int month);

	List<AcademicCalendar> findByCountryNameAndStartHolidayDateBetween(String countryName, LocalDate startHolidayDate,
			LocalDate endHolidayDate);

	List<AcademicCalendar> findByCountryNameAndEndHolidayDateBetween(String countryName, LocalDate startHolidayDate,
			LocalDate endHolidayDate);

}
