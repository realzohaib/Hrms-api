package com.erp.hrms.entity.form;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class RemaingLeaves {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double remaingMedicalLeave;
	private Double remaingCasualLeave;
	private long employeeid;
	

}
