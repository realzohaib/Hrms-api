package com.erp.hrms.approver.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.approver.entity.LeaveApprover;
import com.erp.hrms.approver.repository.LeaveApproverRepo;

@Service
public class LeaveApproverServiceImpl implements LeaveApproverService {

	@Autowired
	private LeaveApproverRepo leaveApproverRepo;

	@Override
	public void createLeaveApprover(LeaveApprover leaveApprover) {
		leaveApproverRepo.save(leaveApprover);
	}

	@Override
	public Optional<LeaveApprover> findLeaveApproverById(Long lAId) {
		return leaveApproverRepo.findById(lAId);
	}

	@Override
	public List<LeaveApprover> findAllLeaveApprovers() {

		return leaveApproverRepo.findAll();
	}

	@Override
	public LeaveApprover updateLeaveApproverEndDate(Long lAId, LeaveApprover leaveApprover) {
		LeaveApprover existingLeaveApprover = leaveApproverRepo.findById(lAId)
				.orElseThrow(() -> new RuntimeException("LeaveApprover not found with id: " + lAId));
		existingLeaveApprover.setFirstApproverEmpId(leaveApprover.getFirstApproverEmpId());
		existingLeaveApprover.setSecondApproverEmpId(leaveApprover.getSecondApproverEmpId());
		existingLeaveApprover.setStartDate(leaveApprover.getStartDate());
		existingLeaveApprover.setEndDate(leaveApprover.getEndDate());

		return leaveApproverRepo.save(existingLeaveApprover);
	}

}
