package com.erp.hrms.entity.form;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LeaveType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leaveTypeId;
	private String leaveName;
	private Double leaveDays;

	public LeaveType() {
		super();
	}

	public LeaveType(String leaveName, Double leaveDays) {
		super();
		this.leaveName = leaveName;
		this.leaveDays = leaveDays;
	}

}
