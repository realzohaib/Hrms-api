package com.erp.hrms.entity.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveCountDTO {

	private String leaveName;
	private Double totalLeaveDays;

}
