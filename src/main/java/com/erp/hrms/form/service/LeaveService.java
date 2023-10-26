package com.erp.hrms.form.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.exception.LeaveRequestApprovalException;
import com.erp.hrms.exception.LeaveRequestNotFoundException;
import com.erp.hrms.form.repository.ILeaveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveService implements ILeaveService {

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

	@Autowired
	ILeaveRepository iLeaveRepository;

//	This method for send the leave request to manager
	@Override
	public void createLeaveApproval(String leaveApproval, MultipartFile medicalDocumentsName) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		LeaveApproval leaveApprovaljson = mapper.readValue(leaveApproval, LeaveApproval.class);

		String uniqueIdentifier = UUID.randomUUID().toString();
		String originalFileName = medicalDocumentsName.getOriginalFilename();
		String fileNameWithUniqueIdentifier = uniqueIdentifier + "_" + originalFileName;

		Path fileNameAndPath = Paths.get(uplaodDirectory, fileNameWithUniqueIdentifier);
		Files.write(fileNameAndPath, medicalDocumentsName.getBytes());
		leaveApprovaljson.setMedicalDocumentsName(fileNameWithUniqueIdentifier);

		iLeaveRepository.createLeaveApproval(leaveApprovaljson);

	}

//	This method for get the leave request by LeaveRequestId
	@Override
	public LeaveApproval getleaveRequestById(Long leaveRequestId) {
		LeaveApproval leaveApproval = iLeaveRepository.getleaveRequestById(leaveRequestId);
		if (leaveApproval == null) {
			throw new LeaveRequestNotFoundException(
					new MessageResponse("Leave request with ID " + leaveRequestId + " not found."));
		}
		return leaveApproval;
	}

//	This method for find all the Leave Request by employeeId
	@Override
	public List<LeaveApproval> getLeaveRequestByEmployeeId(Long employeeId) {
		List<LeaveApproval> leaveApprovals = iLeaveRepository.getLeaveRequestByEmployeeId(employeeId);
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(
					new MessageResponse("This Employee ID " + employeeId + " not found."));
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for find all leave request
	@Override
	public List<LeaveApproval> findAllLeaveApproval() {
		List<LeaveApproval> leaveApprovals = null;
		leaveApprovals = iLeaveRepository.findAllLeaveApproval();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for update the leave request by the manager Accepted or Rejected with the help of leaveRequestId
	@Override
	public LeaveApproval approvedByManager(Long leaveRequestId, String leaveApproval) throws IOException {
		LeaveApproval existingApproval = iLeaveRepository.getleaveRequestById(leaveRequestId);
		if (existingApproval == null) {
			throw new LeaveRequestNotFoundException(
					new MessageResponse("Leave request with ID " + leaveRequestId + " not found."));
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			LeaveApproval leaveApprovalJson = mapper.readValue(leaveApproval, LeaveApproval.class);
			existingApproval.setEmployeeId(leaveApprovalJson.getEmployeeId());
			existingApproval.setNameOfEmployee(leaveApprovalJson.getNameOfEmployee());
			existingApproval.setEmail(leaveApprovalJson.getEmail());
			existingApproval.setContactNumber(leaveApprovalJson.getContactNumber());
			existingApproval.setDesignation(leaveApprovalJson.getDesignation());
			existingApproval.setDepartment(leaveApprovalJson.getDepartment());
			existingApproval.setEmergencyContactNumber(leaveApprovalJson.getEmergencyContactNumber());
			existingApproval.setRequestDate(leaveApprovalJson.getRequestDate());
			existingApproval.setLeaveType(leaveApprovalJson.getLeaveType());
			existingApproval.setStartDate(leaveApprovalJson.getStartDate());
			existingApproval.setEndDate(leaveApprovalJson.getEndDate());
			existingApproval.setNumberOfDaysRequested(leaveApprovalJson.getNumberOfDaysRequested());
			existingApproval.setApprovalStatus(leaveApprovalJson.getApprovalStatus());
			existingApproval.setApprovingManagerName(leaveApprovalJson.getApprovingManagerName());
			existingApproval.setApprovalRemarks(leaveApprovalJson.getApprovalRemarks());

			return iLeaveRepository.approvedByManager(leaveRequestId, leaveApprovalJson);
		} catch (Exception e) {
			throw new LeaveRequestApprovalException(new MessageResponse("Error while approving leave request." + e));
		}
	}

//	This method for find all pending leave request 
	@Override
	public List<LeaveApproval> findAllLeaveApprovalPending() {
		List<LeaveApproval> leaveApprovals = iLeaveRepository.findAllLeaveApprovalPending();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for find all accepted leave request 
	@Override
	public List<LeaveApproval> findAllLeaveApprovalAccepted() {
		List<LeaveApproval> leaveApprovals = iLeaveRepository.findAllLeaveApprovalAccepted();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for find all rejected leave request 
	@Override
	public List<LeaveApproval> findAllLeaveApprovalRejected() {

		List<LeaveApproval> leaveApprovals = iLeaveRepository.findAllLeaveApprovalRejected();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}
}