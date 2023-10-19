package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name="Employee_Achievement")
public class EmpAchievement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String achievementRewardsName;
	


	@Column(length = 2147483647)
	@Lob
	private byte[] achievementsRewardsDocs;
	
	@ManyToOne
	@JoinColumn(name = "previous_id")
	@JsonBackReference
	private PreviousEmployee previousEmployee;
}
