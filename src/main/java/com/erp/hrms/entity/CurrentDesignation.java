package com.erp.hrms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
public class CurrentDesignation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cdId;
	private String startDate;
	private String endDate;
	private Long employeeId;

	@ManyToOne
	@JoinColumn(name = "lid", nullable = false)
//	@JsonBackReference
	private Level level;

	@ManyToOne
	@JoinColumn(name = "did", nullable = false)
//	@JsonBackReference
	private Designation designation;

}
