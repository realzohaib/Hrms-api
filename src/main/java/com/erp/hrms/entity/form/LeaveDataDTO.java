package com.erp.hrms.entity.form;

import java.util.List;

import lombok.Data;

@Data
public class LeaveDataDTO {

	  private List<LeaveApproval> leaveApprovals;
	    private long employeeIdCount;

	    public LeaveDataDTO(List<LeaveApproval> leaveApprovals, long employeeIdCount) {
	        this.leaveApprovals = leaveApprovals;
	        this.employeeIdCount = employeeIdCount;
	    }
}
