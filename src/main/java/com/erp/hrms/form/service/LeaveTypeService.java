package com.erp.hrms.form.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.form.LeaveType;
import com.erp.hrms.exception.LeaveTypeNotFoundException;
import com.erp.hrms.form.repository.ILeaveTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveTypeService implements ILeaveTypeService {

	@Autowired
	public ILeaveTypeRepository iLeaveTypeRepository;

	@Override
	public void predefinedLeaveType() {
		if (iLeaveTypeRepository.findAllLeaveType().isEmpty()) {
			List<LeaveType> initialLeaveType = new ArrayList<>();
			initialLeaveType.add(new LeaveType("Medical", 15.0));
			initialLeaveType.add(new LeaveType("Casual", 30.0));
			iLeaveTypeRepository.predefinedLeaveType(initialLeaveType);
		}
	}

	@Override
	public void createLeaveType(String leaveType) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		LeaveType leaveTypeJson = mapper.readValue(leaveType, LeaveType.class);
		iLeaveTypeRepository.createLeaveType(leaveTypeJson);
	}

	@Override
	public List<LeaveType> findAllLeaveType() {

		List<LeaveType> leaveTypes = iLeaveTypeRepository.findAllLeaveType();
		if (leaveTypes.isEmpty()) {
			throw new LeaveTypeNotFoundException(new MessageResponse("No leave type "));
		}
		return leaveTypes;
	}

	@Override
	public LeaveType findByLeaveTypeId(Long leaveTypeId) {
		LeaveType leaveType = iLeaveTypeRepository.findByLeaveTypeId(leaveTypeId);
		if (leaveType == null) {
			throw new LeaveTypeNotFoundException(
					new MessageResponse("Leave type with Id: " + leaveTypeId + " not found"));
		}

		return leaveType;
	}

}
