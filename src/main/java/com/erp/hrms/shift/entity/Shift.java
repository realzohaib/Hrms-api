package com.erp.hrms.shift.entity;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import lombok.Data;
@Entity
@Data
public class Shift {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer shiftId;
	private String shiftName;
	private LocalTime shiftStartTime;
	private LocalTime shiftEndTime;
	
//	@OneToMany(mappedBy = "shift")
//	@Cascade(CascadeType.ALL)
//	private List<ShiftAssignment> assign;

}
