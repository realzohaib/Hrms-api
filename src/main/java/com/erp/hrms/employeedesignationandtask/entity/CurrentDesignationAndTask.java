	package com.erp.hrms.employeedesignationandtask.entity;
	
	import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.erp.hrms.joblevelanddesignation.entity.Designations;
import com.erp.hrms.joblevelanddesignation.entity.Task;
import com.erp.hrms.locationentity.Location;

import lombok.Data;
	
	@Entity
	@Data
	public class CurrentDesignationAndTask {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "current_desigAndTsk_Id")
		private Integer currentDsAndTskId;
		private Long empId;
		private String startDate;
		private String endDate;
		private Integer levelId;
	
		@ManyToMany
		@JoinTable(name = "currentDsAnsdTask_location", joinColumns = @JoinColumn(name = "current_desigAndTsk_Id"), inverseJoinColumns = @JoinColumn(name = "location_Id"))
		private List<Location> location;
		
		@ManyToMany(cascade = CascadeType.ALL)
		@JoinTable(name = "currentDsAnsdTask_tASK", joinColumns = @JoinColumn(name = "current_desigAndTsk_Id"), inverseJoinColumns = @JoinColumn(name = "task_id"))
		private List<Task> task;	
	
		@ManyToMany(  cascade = CascadeType.ALL , fetch = FetchType.EAGER)
		@JoinTable(name = "currentDsAnsdTask_designation", joinColumns = @JoinColumn(name = "current_desigAndTsk_Id"), inverseJoinColumns = @JoinColumn(name = "designation_id"))
		private List<Designations> designation;
	}
