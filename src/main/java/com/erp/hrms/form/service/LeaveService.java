package com.erp.hrms.form.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.entity.RoleEntity;
import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.utill.ERole;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.entity.form.LeaveType;
import com.erp.hrms.exception.LeaveRequestApprovalException;
import com.erp.hrms.exception.LeaveRequestNotFoundException;
import com.erp.hrms.form.repository.ILeaveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveService implements ILeaveService {

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

	@Autowired
	private ILeaveRepository iLeaveRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserServiceForLeaveNotification userService;

	@Autowired
	EntityManager entityManager;

//	This method for send the leave request to manager and send email to manager and admin
	@Override
	public void createLeaveApproval(String leaveApproval, MultipartFile medicalDocumentsName) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		LeaveApproval leaveApprovalJson = mapper.readValue(leaveApproval, LeaveApproval.class);

		LeaveType leaveType = entityManager.find(LeaveType.class, leaveApprovalJson.getLeaveType().getLeaveTypeId());
		leaveApprovalJson.setLeaveType(leaveType);

		// Fetch admin and manager email addresses based on roles
		String adminEmail = null;
		String managerEmail = null;

		List<UserEntity> userList = userService.getUsers();
		for (UserEntity user : userList) {
			Set<RoleEntity> roles = user.getRoles();
			for (RoleEntity role : roles) {
				if (role.getName() == ERole.ROLE_ADMIN) {
					adminEmail = user.getEmail();
				} else if (role.getName() == ERole.ROLE_MANAGER) {
					managerEmail = user.getEmail();
				}
				// If both adminEmail and managerEmail are found, you can break out of the loop.
				if (adminEmail != null && managerEmail != null) {
					break;
				}
			}
			if (adminEmail != null && managerEmail != null) {
				break;
			}
		}

		String uniqueIdentifier = UUID.randomUUID().toString();
		String originalFileName = medicalDocumentsName.getOriginalFilename();
		String fileNameWithUniqueIdentifier = uniqueIdentifier + "_" + originalFileName;

		Path fileNameAndPath = Paths.get(uplaodDirectory, fileNameWithUniqueIdentifier);
		Files.write(fileNameAndPath, medicalDocumentsName.getBytes());
		leaveApprovalJson.setMedicalDocumentsName(fileNameWithUniqueIdentifier);

		iLeaveRepository.createLeaveApproval(leaveApprovalJson);

//		 Send emails to admin and manager
		sendLeaveRequestEmail(adminEmail, "Leave Request from Employee", leaveApprovalJson);
		sendLeaveRequestEmail(managerEmail, "Leave Request from Employee", leaveApprovalJson);

		// Send an email to the employee who requested the leave
		String employeeEmail = leaveApprovalJson.getEmail();
		sendLeaveRequestEmail(employeeEmail, "Leave Request Confirmation", leaveApprovalJson);
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
			existingApproval.setJobLevel(leaveApprovalJson.getJobLevel());
			existingApproval.setLocation(leaveApprovalJson.getLocation());
			existingApproval.setEmergencyContactNumber(leaveApprovalJson.getEmergencyContactNumber());
			existingApproval.setRequestDate(leaveApprovalJson.getRequestDate());
			existingApproval.setLeaveType(leaveApprovalJson.getLeaveType());
			existingApproval.setStartDate(leaveApprovalJson.getStartDate());
			existingApproval.setEndDate(leaveApprovalJson.getEndDate());
			existingApproval.setNumberOfDaysRequested(leaveApprovalJson.getNumberOfDaysRequested());
			existingApproval.setApprovalStatus(leaveApprovalJson.getApprovalStatus());
			existingApproval.setApprovingManagerName(leaveApprovalJson.getApprovingManagerName());
			existingApproval.setApprovalRemarks(leaveApprovalJson.getApprovalRemarks());
			existingApproval.setManagerEmail(leaveApprovalJson.getManagerEmail());

			// Fetch admin and manager email addresses based on roles
			String adminEmail = null;
			String managerEmail = leaveApprovalJson.getManagerEmail();

			List<UserEntity> userList = userService.getUsers();
			for (UserEntity user : userList) {
				Set<RoleEntity> roles = user.getRoles();
				for (RoleEntity role : roles) {
					if (role.getName() == ERole.ROLE_ADMIN) {
						adminEmail = user.getEmail();
					}
				}
				if (adminEmail != null) {
					break;
				}
			}

			// Send emails to admin
			sendLeaveRequestEmailApproved(adminEmail, "Leave Request status by the manager", leaveApprovalJson);
//			 Send email to manager who approve or deny the leave request
			sendLeaveRequestEmailApproved(managerEmail, "Leave Request status by the manager", leaveApprovalJson);

			// Send an email to the employee
			String employeeEmail = leaveApprovalJson.getEmail();
			sendLeaveRequestEmailApproved(employeeEmail, "Leave Request status by the manager", leaveApprovalJson);

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

//	This method for calculate total number of leave days with employee id  
	@Override
	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployee(Long employeeId) {
		return iLeaveRepository.calculateTotalNumberOfDaysRequestedByEmployee(employeeId);
	}

//	This method for calculate total number of leave days with employee id and leave name
	@Override
	public BigDecimal calculateTotalSpecificNumberOfDaysRequestedByEmployee(Long employeeId, String leaveName) {
		return iLeaveRepository.calculateTotalSpecificNumberOfDaysRequestedByEmployee(employeeId, leaveName);
	}

//	This method for send email send to admin and manager and employee.
	private void sendLeaveRequestEmail(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText("Leave Request Details:\n" + "Employee Name: " + leaveApproval.getNameOfEmployee() + "\n"
				+ "Leave Name: " + leaveApproval.getLeaveType().getLeaveName() + "\n" + "Start Date: "
				+ leaveApproval.getStartDate() + "\n" + "End Date: " + leaveApproval.getEndDate() + "\n" + "Reason: "
				+ leaveApproval.getLeaveReason() + "\n");
		mailSender.send(message);
	}

//	This method for send email send to admin and manager and employee when manager accepted or rejected  
	private void sendLeaveRequestEmailApproved(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText("Your Leave Request status :\n" + "Employee Name: " + leaveApproval.getNameOfEmployee() + "\n"
				+ "Leave Type: " + leaveApproval.getLeaveType() + "\n" + "Start Date: " + leaveApproval.getStartDate()
				+ "\n" + "End Date: " + leaveApproval.getEndDate() + "\n" + "Reason: " + leaveApproval.getLeaveReason()
				+ "\n" + "Manager Name : " + leaveApproval.getApprovingManagerName() + "\n" + "Status :"
				+ leaveApproval.getApprovalStatus() + "\n" + "Manager remark :" + leaveApproval.getApprovalRemarks()
				+ "\n" + "\n");
		mailSender.send(message);
	}

}