package com.erp.hrms.payroll.entity;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import com.erp.hrms.attendence.service.AttendenceResponse;

import lombok.Data;

@Data
public class PayRollResponse {
	
	private PayRoll payroll;
	private AttendenceResponse attendence;
	private BigDecimal totalleaves;

}
