package com.erp.hrms.entity.form;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class LeaveRecords {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long employeeid;
	private Double totalLeavesTaken;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<LeaveType> leaveTypes;
	
}
