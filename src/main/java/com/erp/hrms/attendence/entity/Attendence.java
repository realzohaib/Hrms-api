package com.erp.hrms.attendence.entity;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Attendence {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendence_Id")
	private Long attendenceId;
	
	private LocalDate date;
	
	@Column(name = "Day")
	private DayOfWeek day;
	
	@Column(name = "punch_In_Time")
	private Timestamp punchInTime;
	
	@Column(name = "punch_Our_Time")
	private Timestamp punchOutTime;
	
	@Column(name = ("working_Hours"))
	private long workingHours;
	
	private boolean halfDay;
	
	private long overTime;
	
	private String overtimeStatus;
	
	private long employeeId;

	@OneToMany(mappedBy = "attendence")
	@Cascade(CascadeType.ALL)
    @JsonManagedReference
	private List<Breaks>breaks;
	
	

}