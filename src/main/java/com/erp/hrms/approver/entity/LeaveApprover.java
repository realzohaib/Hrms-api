package com.erp.hrms.approver.entity;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.erp.hrms.Location.entity.Location;

import lombok.Data;

@Entity
@Data
public class LeaveApprover {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lAId;
	private Long firstApproverEmpId;
	private String firstApproverEmail;

	private Long secondApproverEmpId;
	private String secondApproverEmail;
	private String startDate;
	private String endDate;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Location> locations;

	@ElementCollection
	private List<String> approverLevels;
	
	
//	DTO
//	
//	Private leaveApprover;
//	private int empid
	
	
	//public list<DTO> apprioverAndEmpUnderSameL;
	
}
