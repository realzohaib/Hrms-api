package com.erp.hrms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class Designation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dId;
	private Long employeeId;
	private String designationName;
	private String level;
	private String startDate;
	private String endDate;

	@ManyToOne
	@JoinColumn(name = "p_id")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
