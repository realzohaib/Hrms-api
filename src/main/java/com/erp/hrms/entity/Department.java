package com.erp.hrms.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "departmentName") })
public class Department implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DepartmentIdGenerator")
	@SequenceGenerator(name = "DepartmentIdGenerator", sequenceName = "Department_Id", allocationSize = 1, initialValue = 11)
	private Long departmentId;
	private String departmentName;

	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PersonalInfo> personalInfos;

	public Department(String departmentName) {
		this.departmentName = departmentName;
	}

	public Department(long departmentId, String departmentName) {
		this.departmentId = departmentId;
		this.departmentName = departmentName;
	}

	public Department() {
	}
}
