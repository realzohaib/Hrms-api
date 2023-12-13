package com.erp.hrms.payments;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ManyToAny;

import com.erp.hrms.payroll.entity.PayRoll;

import lombok.Data;

@Entity
@Data
public class Tax {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String TaxName;
	private double salaryPercentOfTax;
	
	@ManyToOne
    @JoinColumn(name = "payroll_id")
	private PayRoll payroll;

}
