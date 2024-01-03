package com.erp.hrms.joblevelandDesignationEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class SubDuties implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subDuty_Id")
	private Integer subDutiesId;
	private String subDutyName;
	
	@ManyToMany(mappedBy = "subduties" , fetch =  FetchType.EAGER)
	@JsonBackReference
	private List<Duties> duties = new ArrayList<Duties>();

}
