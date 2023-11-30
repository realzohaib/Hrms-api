package com.erp.hrms.entity.form;

import lombok.Data;

@Data
public class LeaveSummary {

	private String leaveName;
	private Double totalLeaveDays;

	public LeaveSummary(String leaveName, Double totalLeaveDays) {
		this.leaveName = leaveName;
		this.totalLeaveDays = totalLeaveDays;
	}
}
