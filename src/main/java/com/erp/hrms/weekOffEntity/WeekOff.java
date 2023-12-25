package com.erp.hrms.weekOffEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.erp.hrms.entity.PersonalInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class WeekOff {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long weekOffId;
		
	@Column(name = "individual_day_off")
    private String individualDayOff;
	
	private long employeeId;
	
	private long changedBy;
	
	private LocalDate changedOn;
			

}
