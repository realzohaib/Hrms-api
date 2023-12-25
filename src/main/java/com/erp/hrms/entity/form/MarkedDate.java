package com.erp.hrms.entity.form;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkedDate {
	private LocalDate date;
	private boolean marked;

	public MarkedDate(LocalDate date, boolean marked) {
		this.date = date;
		this.marked = marked;
	}
}
