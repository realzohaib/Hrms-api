package com.erp.hrms.financialYear;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class FinancialYear {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer financialYearId;
	
	private String financialYearStart;
	
	private String financialYearEnd;


}
