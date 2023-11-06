package com.erp.hrms.attendence.service;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendenceRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private LocalDate startDate;
	private LocalDate endODate;

}