package com.erp.hrms.attendence.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
public class Breaks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long breakId;
	
	private Timestamp breakStart;
	
	private Timestamp breakEnd;
	
	private long totalBreak;
	
	@JoinColumn(name = "attendence_Id")
	@ManyToOne
	@JsonBackReference
	private Attendence attendence;

}
