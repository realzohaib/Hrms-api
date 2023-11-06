package com.erp.hrms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name = "Employee_Achievement")
public class EmpAchievement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String achievementRewardsName;

	private String achievementsRewardsDocs;
	
	@Transient
	private byte[] achievementsRewardsDocsData;

	@ManyToOne
	@JoinColumn(name = "previous_id")
	@JsonBackReference
	private PreviousEmployee previousEmployee;
}
