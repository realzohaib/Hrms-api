package com.erp.hrms.form.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.entity.form.LeaveRecords;
import com.erp.hrms.form.repository.LeaveRecordsRepository;

@Service
public class LeaveRecordsServiceImpl implements ILeaveRecordsService {
	
	@Autowired
	LeaveRecordsRepository leaveRecordsRepository;

	@Override
	public LeaveRecords saveLeaveRecords(LeaveRecords leaveRecords) {
		
		return leaveRecordsRepository.save(leaveRecords);
	}

//	@Override
//	public LeaveRecords findByIdLeaveRecords(Long id) {
//		
//		return leaveRecordsRepository.findByLeaveRecordsId(id);
//	}

	@Override
	public List<LeaveRecords> findAllLeaveRecords() {
		
		return leaveRecordsRepository.findAll();
	}

}
