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
import lombok.ToString;

@Entity
@Data
@Table(name = "Employee_Achievement")
@ToString
public class EmpAchievement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String achievementRewardsName;

	private String achievementsRewardsDocs;

	@Transient
	private byte[] achievementsRewardsDocsData;
	
	 public void setAchievementsRewardsDocsData(byte[] achievementsRewardsDocsData) {
	        validateAndSetData(achievementsRewardsDocsData, "Achievements Rewards Docs Data");
	    }

	    private void validateAndSetData(byte[] data, String dataType) {
	        if (data != null && data.length <= 100 * 1024) {
	            if ("Achievements Rewards Docs Data".equals(dataType)) {
	                this.achievementsRewardsDocsData = data;
	            }
	        } else {
	            throw new IllegalArgumentException(dataType + " size exceeds the allowed limit (100 KB)");
			}
		}

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;
}
