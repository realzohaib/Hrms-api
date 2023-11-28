package com.erp.hrms.payroll.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class PayRoll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payrollId;
    private long employeeId;
    private String name;
    private String jobDesignation;
    private String jobLevel;
    private String jobLocation;
    private String department;
    private String contact;
    private String address;
    
    private int month;
    private int year;
    
    private int leaveDays;
    private int tardyDays;
    
    @Embedded
    private Allowances allowances;
    
    private String incentivesName;
    private double incentiveAmount;
    
    private String anySpecialReward;
    private double anySpecialRewardAmount;
    private double bonus;

    private double overtimePayAmount;
    private double overtimePay;
    private double monthlyPerformancePay;
    private double totalPay;
}
