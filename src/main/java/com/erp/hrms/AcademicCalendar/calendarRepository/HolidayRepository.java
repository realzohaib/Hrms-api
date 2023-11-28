package com.erp.hrms.AcademicCalendar.calendarRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.erp.hrms.AcademicCalendar.entity.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	@Query("SELECT h FROM Holiday h WHERE h.countryName = :countryName")
	public List<Holiday> findByCountry(@Param("countryName") String countryName);

	public Holiday findByHolidayId(Long id);

	@Query("SELECT h FROM Holiday h WHERE YEAR(h.startHolidayDate) = :year AND h.countryName = :countryName")
	public List<Holiday> findByYearAndCountry(@Param("year") int year, @Param("countryName") String countryName);

}
