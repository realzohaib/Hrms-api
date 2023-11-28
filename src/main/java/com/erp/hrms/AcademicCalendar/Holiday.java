package com.erp.hrms.AcademicCalendar;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "holidays")
public class Holiday {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long holidayId;

	private String name;
	private LocalDate startHolidayDate;
	private LocalDate endHolidayDate;
	private Double numberOfHoliday;

	@Column(name = "country_name")
	private String countryName;
}