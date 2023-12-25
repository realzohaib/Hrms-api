package com.erp.hrms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class Designation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dId;
	private String designationName;

	@ManyToOne
	@JoinColumn(name = "lid", nullable = false)
//	@JsonBackReference
	private Level level;

	public Designation(String designationName, Level level) {
		super();
		this.designationName = designationName;
		this.level = level;
	}

	public Designation() {
		super();
	}

	@OneToMany(mappedBy = "designation", cascade = CascadeType.ALL)
	private List<CurrentDesignation> currentDesignations;

}
