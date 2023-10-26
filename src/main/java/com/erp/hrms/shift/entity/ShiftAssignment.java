package com.erp.hrms.shift.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class ShiftAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long assignmentId;  

    private LocalDate assignedOn;
    
    private LocalDate startDate;  
    private LocalDate endDate;

    private long employeeId;
    
    @ManyToOne
    private Shift shift;
}
