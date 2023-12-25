package com.erp.hrms.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
public class Level {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lId;
	private String levelName;

	@OneToMany(mappedBy = "level", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JsonManagedReference
	private List<Designation> designations;

//	public Level(String levelName, List<Designation> designations) {
//		super();
//		this.levelName = levelName;
//		this.designations = designations;
//	}

	public Level() {
		super();
	}

	public Level(String levelName) {
		super();
		this.levelName = levelName;
	}

}
