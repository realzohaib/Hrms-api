package com.erp.hrms.joblevelandDesignationEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.erp.hrms.EmpDesignation.ENTITY.CurrentDesignationAndTask;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class Task implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Task_id")
	private Integer taskId;
	private String TaskName;

	@ManyToMany(mappedBy = "task")
	@JsonBackReference
	private List<SubDuties> subduties = new ArrayList<SubDuties>();
//
//	@ManyToMany(mappedBy = "task")	
//	@JsonBackReference(value = "current")
//	private List<CurrentDesignationAndTask> current;

}
