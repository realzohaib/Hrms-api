package com.erp.hrms.entity.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaveCountResponse {

	private String startDate; // Adjust the type based on your actual entity
	private long count;

}