package com.erp.hrms.Location.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.erp.hrms.EmpDesignation.ENTITY.CurrentDesignationAndTask;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long locationId;
	
	private Long ConcernedAuthorityEmpId;

	private String name;

	private String address;

	private String langitude;

	private String longitude;

	private boolean isMantenanceRequired;

	private String commentsForMaintenance;

//	@ManyToMany(cascade = { CascadeType.ALL }) 
//	@JoinTable(name = "countryLocation", joinColumns = @JoinColumn(name = "locationId"), inverseJoinColumns = @JoinColumn(name = "countryId"))
//	private List<CountryRoot> country;

	/**
	 * in-charge info
	 */
	private String inchargeInfo;
	
//	@ManyToMany(mappedBy = "location")
//	@JsonBackReference
//	private List<CurrentDesignationAndTask> current;

}
