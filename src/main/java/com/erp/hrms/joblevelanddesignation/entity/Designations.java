package com.erp.hrms.joblevelanddesignation.entity;

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
import javax.persistence.ManyToOne;

import com.erp.hrms.employeedesignationandtask.entity.CurrentDesignationAndTask;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Designations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "designation_id")
	private Integer designationId;
	private String designationName;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "designation_duties", joinColumns = {
			@JoinColumn(name = "designation_id", referencedColumnName = "designation_id") }, inverseJoinColumns = {
					@JoinColumn(name = "duties_id", referencedColumnName = "duties_id") })
	@JsonManagedReference
	private List<Duties> duties = new ArrayList<Duties>();

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
