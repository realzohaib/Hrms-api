package com.erp.hrms.payments;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class OverTimePay {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private LocalDate date;
	private double overtimeAmount;

}
