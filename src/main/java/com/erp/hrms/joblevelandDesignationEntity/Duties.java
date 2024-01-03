package com.erp.hrms.joblevelandDesignationEntity;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Duties implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "duties_id")
	private Integer dutiesId;
	private String dutyName;

	@ManyToMany(mappedBy = "duties" , fetch = FetchType.EAGER)
	@JsonBackReference
	private List<Designations> designation = new ArrayList<Designations>();
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "duties_subduties",
	joinColumns = {@JoinColumn(name ="duties_id")},
	inverseJoinColumns = {@JoinColumn(name ="subDuty_Id")})
	@JsonManagedReference
	private List<SubDuties>subduties = new ArrayList<SubDuties>();

	public Duties(Integer dutiesId, String dutyName, List<Designations> designation) {
		super();
		this.dutiesId = dutiesId;
		this.dutyName = dutyName;
		this.designation = designation;
	}

	public Duties() {

	}

}
