package com.erp.hrms.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Entity
@Data
@Table(name = "previous_employee")
public class PreviousEmployee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long previousId;
	private String companyName;
	private String companyAddress;
	private String designation;
	private String description;
	private String dateFrom;
	private String dateTo;
	private String previousManagerName;
	private String previousManagerPhoneCode;
	private String previousManagerContact;
	private String previousHRName;
	private String previousHRPhoneCode;
	private String previousHRContact;
	private double lastWithdrawnSalary;

	private String payslipScan;

	@OneToMany(mappedBy = "previousEmployee")
	@Cascade(CascadeType.ALL)
	@JsonManagedReference
	private List<EmpAchievement> empAchievements;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;
}
