package com.erp.hrms.joblevelandDesignationEntity;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Designations implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "designation_id")
	private Integer designationId;
	private String designationName;

//	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	//@JsonManagedReference
//	private List<Duties> duties;
	
	@ManyToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
	@JoinTable(name = "duties_designations", 
	           joinColumns = @JoinColumn(name = "designation_id"),
	           inverseJoinColumns = @JoinColumn(name = "duties_id"))
	@JsonManagedReference
	private List<Duties> duties;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "joblevel_id")
	@JsonBackReference
	private JobLevel joblevel;

	
	
	public Designations(Integer designationId, String designationName, List<Duties> duties, JobLevel joblevel) {
		super();
		this.designationId = designationId;
		this.designationName = designationName;
		this.duties = duties;
		this.joblevel = joblevel;
	}

}
