package com.erp.hrms.form.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.api.security.entity.RoleEntity;
import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.utill.ERole;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.entity.form.LeaveCalendarData;
import com.erp.hrms.entity.form.LeaveType;
import com.erp.hrms.entity.form.MarkedDate;
import com.erp.hrms.exception.LeaveRequestApprovalException;
import com.erp.hrms.exception.LeaveRequestNotFoundException;
import com.erp.hrms.form.repository.ILeaveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveService implements ILeaveService {

	private static final Logger logger = LoggerFactory.getLogger(LeaveApproval.class);

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

	@Autowired
	private ILeaveRepository iLeaveRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserServiceForLeaveNotification userService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private IPersonalInfoDAO iPersonalInfoDAO;

	@Autowired
	private FileService fileService;

//	This method for send the leave request to manager and send email to manager and admin
	@Override
	public void createLeaveApproval(String leaveApproval, MultipartFile medicalDocumentsName) throws IOException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			LeaveApproval leaveApprovalJson = mapper.readValue(leaveApproval, LeaveApproval.class);

			LeaveType leaveType = entityManager.find(LeaveType.class,
					leaveApprovalJson.getLeaveType().getLeaveTypeId());
			leaveApprovalJson.setLeaveType(leaveType);

			// Fetch admin and manager email addresses based on roles
			String hrEmail = null;
			String managerEmail = null;

			List<UserEntity> userList = userService.getUsers();
			for (UserEntity user : userList) {
				Set<RoleEntity> roles = user.getRoles();
				for (RoleEntity role : roles) {
					if (role.getName() == ERole.ROLE_HR) {
						hrEmail = user.getEmail();
					} else if (role.getName() == ERole.ROLE_MANAGER) {
						managerEmail = user.getEmail();
					}
				}
				// If both adminEmail and managerEmail are found, you can break out of the loop.
				if (hrEmail != null && managerEmail != null) {
					break;
				}
			}

			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				String uniqueIdentifier = UUID.randomUUID().toString();
				String originalFileName = medicalDocumentsName.getOriginalFilename();
				String fileNameWithUniqueIdentifier = uniqueIdentifier + "_" + originalFileName;

				Path fileNameAndPath = Paths.get(uplaodDirectory, fileNameWithUniqueIdentifier);
				Files.write(fileNameAndPath, medicalDocumentsName.getBytes());
				leaveApprovalJson.setMedicalDocumentsName(fileNameWithUniqueIdentifier);
			}

			iLeaveRepository.createLeaveApproval(leaveApprovalJson);

//		 Send emails to hr and manager
			// If both HR and manager emails are found, send emails to both
			if (hrEmail != null && managerEmail != null) {
				sendLeaveRequestEmail(hrEmail, "Leave Request from Employee", leaveApprovalJson);
				sendLeaveRequestEmail(managerEmail, "Leave Request from Employee", leaveApprovalJson);
			} else if (hrEmail != null) {
				// If only HR email is found, send the email to HR
				sendLeaveRequestEmail(hrEmail, "Leave Request from Employee", leaveApprovalJson);
			} else if (managerEmail != null) {
				// If only manager email is found, send the email to the manager
				sendLeaveRequestEmail(managerEmail, "Leave Request from Employee", leaveApprovalJson);
			} else {
				logger.warn("Neither HR nor manager email found. Leave request email not sent.");
			}

//		 Send an email to the employee who requested the leave
			String employeeEmail = leaveApprovalJson.getEmail();
			sendLeaveRequestEmail(employeeEmail, "Leave Request Confirmation", leaveApprovalJson);
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while send your request." + e);
		}
	}

//	This method for find the data of leave by leave request id
	@Override
	public LeaveApproval getleaveRequestById(Long leaveRequestId) throws IOException {
		try {
			LeaveApproval leaveApproval = iLeaveRepository.getleaveRequestById(leaveRequestId);
			if (leaveApproval == null) {
				throw new LeaveRequestNotFoundException(
						new MessageResponse("Leave request with ID " + leaveRequestId + " not found."));

			}
			String medicalDocumentsName = leaveApproval.getMedicalDocumentsName();
			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				byte[] fileData = fileService.getFileData(medicalDocumentsName);
				if (fileData != null) {
					leaveApproval.setMedicalDocumentData(fileData);
				}
			}
			return leaveApproval;
		} catch (Exception e) {
			throw new RuntimeException(
					"An error occurred while retrieving leave approval for leave request Id: " + leaveRequestId);
		}
	}

//	This method for find all the Leave Request by employeeId
	@Override
	public List<LeaveApproval> getLeaveRequestByEmployeeId(Long employeeId) throws IOException {
		List<LeaveApproval> leaveApprovals = iLeaveRepository.getLeaveRequestByEmployeeId(employeeId);
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(
					new MessageResponse("This Employee ID " + employeeId + " not found."));
		}
		for (LeaveApproval leaveApproval : leaveApprovals) {
			String medicalDocumentsName = leaveApproval.getMedicalDocumentsName();
			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				byte[] fileData = fileService.getFileData(medicalDocumentsName);
				if (fileData != null) {
					leaveApproval.setMedicalDocumentData(fileData);
				}
			}
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for find all leave request
	@Override
	public List<LeaveApproval> findAllLeaveApproval() throws IOException {
		List<LeaveApproval> leaveApprovals = null;
		leaveApprovals = iLeaveRepository.findAllLeaveApproval();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		for (LeaveApproval leaveApproval : leaveApprovals) {
			String medicalDocumentsName = leaveApproval.getMedicalDocumentsName();
			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				byte[] fileData = fileService.getFileData(medicalDocumentsName);
				if (fileData != null) {
					leaveApproval.setMedicalDocumentData(fileData);
				}
			}
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for update the leave request by the manager Accepted or Rejected with the help of leaveRequestId
	@Override
	public LeaveApproval approvedByManager(Long leaveRequestId, String leaveApproval,
			MultipartFile medicalDocumentsName) throws IOException {
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

			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				if (existingApproval.getMedicalDocumentsName() != null) {
					Path oldMedicalDocument = Paths.get(uplaodDirectory, existingApproval.getMedicalDocumentsName());
					Files.deleteIfExists(oldMedicalDocument);
				}
				String uniqueIdentifier = UUID.randomUUID().toString();
				String originalFileName = medicalDocumentsName.getOriginalFilename();
				String fileNameWithUniqueIdentifier = uniqueIdentifier + "_" + originalFileName;

				Path fileNameAndPath = Paths.get(uplaodDirectory, fileNameWithUniqueIdentifier);
				Files.write(fileNameAndPath, medicalDocumentsName.getBytes());
				leaveApprovalJson.setMedicalDocumentsName(fileNameWithUniqueIdentifier);
			}

			// Fetch hr and manager email addresses based on roles
			String hrEmail = null;
			String managerEmail = leaveApprovalJson.getManagerEmail();

			List<UserEntity> userList = userService.getUsers();
			for (UserEntity user : userList) {
				Set<RoleEntity> roles = user.getRoles();
				for (RoleEntity role : roles) {
					if (role.getName() == ERole.ROLE_HR) {
						hrEmail = user.getEmail();
					}
				}
				if (hrEmail != null) {
					break;
				}
			}

			// Send emails to hr
			sendLeaveRequestEmailApproved(hrEmail, "Leave Request status by the manager", leaveApprovalJson);
//			 Send email to manager who approve or deny the leave request
			sendLeaveRequestEmailApproved(managerEmail, "Leave Request status by the manager", leaveApprovalJson);

			// Send an email to the employee
			String employeeEmail = leaveApprovalJson.getEmail();
			sendLeaveRequestEmailApproved(employeeEmail, "Your leave request status", leaveApprovalJson);

			return iLeaveRepository.approvedByManager(leaveRequestId, leaveApprovalJson);
		} catch (Exception e) {
			throw new LeaveRequestApprovalException(new MessageResponse("Error while approving leave request." + e));
		}
	}

//	This method for update the leave request by the manager Accepted or Rejected with the help of leaveRequestId
	@Override
	public LeaveApproval approvedOrDenyByHR(Long leaveRequestId, String leaveApproval,
			MultipartFile medicalDocumentsName) throws IOException {
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
			existingApproval.setHrName(leaveApprovalJson.getHrName());
			existingApproval.setHrApprovalStatus(leaveApprovalJson.getHrApprovalStatus());
			existingApproval.setHrApprovalRemarks(leaveApprovalJson.getHrApprovalRemarks());
			existingApproval.setHrEmail(leaveApprovalJson.getHrEmail());

			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				if (existingApproval.getMedicalDocumentsName() != null) {
					Path oldMedicalDocument = Paths.get(uplaodDirectory, existingApproval.getMedicalDocumentsName());
					Files.deleteIfExists(oldMedicalDocument);
				}
				String uniqueIdentifier = UUID.randomUUID().toString();
				String originalFileName = medicalDocumentsName.getOriginalFilename();
				String fileNameWithUniqueIdentifier = uniqueIdentifier + "_" + originalFileName;

				Path fileNameAndPath = Paths.get(uplaodDirectory, fileNameWithUniqueIdentifier);
				Files.write(fileNameAndPath, medicalDocumentsName.getBytes());
				leaveApprovalJson.setMedicalDocumentsName(fileNameWithUniqueIdentifier);
			}

			// Fetch hr and manager email addresses based on roles
			String hrEmail = leaveApprovalJson.getHrEmail();
			String managerEmail = leaveApprovalJson.getManagerEmail();

			// Send emails to hr
			sendLeaveRequestEmailApprovedOrDenyByHR(hrEmail, "Leave Request status by the HR", leaveApprovalJson);
//			 Send email to manager who approve or deny the leave request
			sendLeaveRequestEmailApprovedOrDenyByHR(managerEmail, "Leave Request status by the HR", leaveApprovalJson);

			// Send an email to the employee
			String employeeEmail = leaveApprovalJson.getEmail();
			sendLeaveRequestEmailApprovedOrDenyByHR(employeeEmail, "Leave Request status by the HR", leaveApprovalJson);

			return iLeaveRepository.approvedOrDenyByHR(leaveRequestId, leaveApprovalJson);
		} catch (Exception e) {
			throw new LeaveRequestApprovalException(new MessageResponse("Error while approving leave request." + e));
		}
	}

//	This method for find all pending leave request 
	@Override
	public List<LeaveApproval> findAllLeaveApprovalPending() throws IOException {
		List<LeaveApproval> leaveApprovals = iLeaveRepository.findAllLeaveApprovalPending();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		for (LeaveApproval leaveApproval : leaveApprovals) {
			String medicalDocumentsName = leaveApproval.getMedicalDocumentsName();
			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				byte[] fileData = fileService.getFileData(medicalDocumentsName);
				if (fileData != null) {
					leaveApproval.setMedicalDocumentData(fileData);
				}
			}
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for find all accepted leave request 
	@Override
	public List<LeaveApproval> findAllLeaveApprovalAccepted() throws IOException {
		List<LeaveApproval> leaveApprovals = iLeaveRepository.findAllLeaveApprovalAccepted();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		for (LeaveApproval leaveApproval : leaveApprovals) {
			String medicalDocumentsName = leaveApproval.getMedicalDocumentsName();
			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				byte[] fileData = fileService.getFileData(medicalDocumentsName);
				if (fileData != null) {
					leaveApproval.setMedicalDocumentData(fileData);
				}
			}
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for find all rejected leave request 
	@Override
	public List<LeaveApproval> findAllLeaveApprovalRejected() throws IOException {

		List<LeaveApproval> leaveApprovals = iLeaveRepository.findAllLeaveApprovalRejected();
		if (leaveApprovals.isEmpty()) {
			throw new LeaveRequestNotFoundException(new MessageResponse("No leave request now "));
		}
		for (LeaveApproval leaveApproval : leaveApprovals) {
			String medicalDocumentsName = leaveApproval.getMedicalDocumentsName();
			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
				byte[] fileData = fileService.getFileData(medicalDocumentsName);
				if (fileData != null) {
					leaveApproval.setMedicalDocumentData(fileData);
				}
			}
		}
		leaveApprovals.sort((l1, l2) -> Long.compare(l2.getLeaveRequestId(), l1.getLeaveRequestId()));
		return leaveApprovals;
	}

//	This method for calculate total number of leave days with employee id  
	@Override
	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployee(Long employeeId) {
		return iLeaveRepository.calculateTotalNumberOfDaysRequestedByEmployee(employeeId);
	}

//	This method for calculate total number of leave days with employee id with leave name
	@Override
	public BigDecimal calculateTotalSpecificNumberOfDaysRequestedByEmployee(Long employeeId, String leaveName) {
		return iLeaveRepository.calculateTotalSpecificNumberOfDaysRequestedByEmployee(employeeId, leaveName);
	}

//	This method for send email send to hr and manager and employee.
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

//	This method for send email send to hr and manager and employee when manager accepted or rejected  
	private void sendLeaveRequestEmailApproved(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText("Your Leave Request status :\n" + "Employee Name: " + leaveApproval.getNameOfEmployee() + "\n"
				+ "Leave Type: " + leaveApproval.getLeaveType().getLeaveName() + "\n" + "Start Date: "
				+ leaveApproval.getStartDate() + "\n" + "End Date: " + leaveApproval.getEndDate() + "\n" + "Reason: "
				+ leaveApproval.getLeaveReason() + "\n" + "Manager Name : " + leaveApproval.getApprovingManagerName()
				+ "\n" + "Status :" + leaveApproval.getApprovalStatus() + "\n" + "Manager remark :"
				+ leaveApproval.getApprovalRemarks() + "\n" + "\n");
		mailSender.send(message);
	}

//	This method for send email send to hr and manager and employee when manager accepted or rejected  
	private void sendLeaveRequestEmailApprovedOrDenyByHR(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText("Your Leave Request status :\n" + "Employee Name: " + leaveApproval.getNameOfEmployee() + "\n"
				+ "Leave Type: " + leaveApproval.getLeaveType().getLeaveName() + "\n" + "Start Date: "
				+ leaveApproval.getStartDate() + "\n" + "End Date: " + leaveApproval.getEndDate() + "\n" + "Reason: "
				+ leaveApproval.getLeaveReason() + "\n" + "Manager Name : " + leaveApproval.getApprovingManagerName()
				+ "\n" + "Status :" + leaveApproval.getApprovalStatus() + "\n" + "Manager remark :"
				+ leaveApproval.getApprovalRemarks() + "\n" + "HR Name :" + leaveApproval.getHrName() + "\n"
				+ "Status :" + leaveApproval.getHrApprovalStatus() + "\n" + "HR remark :"
				+ leaveApproval.getHrApprovalRemarks() + "\n" + "\n");
		mailSender.send(message);
	}

//	Find all accepted leaves for calendar 
	@Override
	public List<LeaveApproval> getAllLeaveApprovalsAccepted() {
		return iLeaveRepository.findAllLeaveApprovalAccepted();
	}

//	This method is to calculate how many employees are on leave in a day.
	@Override
	public List<LeaveCalendarData> generateLeaveCalendar(List<LeaveApproval> leaveApprovals) {
		List<LeaveCalendarData> calendarData = new ArrayList<>();

		for (LeaveApproval approval : leaveApprovals) {
			LocalDate startDate = LocalDate.parse(approval.getStartDate());
			LocalDate endDate = LocalDate.parse(approval.getEndDate());

			List<LocalDate> leaveDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

			for (LocalDate date : leaveDates) {
				Optional<LeaveCalendarData> existingData = calendarData.stream()
						.filter(data -> data.getDate().equals(date)).findFirst();

				if (existingData.isPresent()) {
					existingData.get().incrementEmployeeCount();
				} else {
					LeaveCalendarData data = new LeaveCalendarData(date);
					calendarData.add(data);
				}
			}
		}

		return calendarData;
	}

//	This method is to mark the date on which more than 20% leave occurred and or will occur on the following day.
	@Override
	public List<MarkedDate> markCalendarDates() {
		try {
			List<LeaveApproval> leaveApprovals = getAllLeaveApprovalsAccepted();
			List<MarkedDate> markedDates = new ArrayList<>();

			Map<LocalDate, Integer> dateToEmployeeCount = new HashMap<>();
			List<PersonalInfo> employees = iPersonalInfoDAO.findAllPersonalInfo();

			// Calculate the number of employees on leave for each date
			for (LeaveApproval approval : leaveApprovals) {
				LocalDate startDate = LocalDate.parse(approval.getStartDate());
				LocalDate endDate = LocalDate.parse(approval.getEndDate());

				while (!startDate.isAfter(endDate)) {
					dateToEmployeeCount.put(startDate, dateToEmployeeCount.getOrDefault(startDate, 0) + 1);
					startDate = startDate.plusDays(1);
				}
			}

			int totalEmployees = getTotalEmployeeCount(employees);

			// Determine which dates should be marked
			for (LocalDate date : dateToEmployeeCount.keySet()) {
				int employeeCount = dateToEmployeeCount.get(date);

				if (employeeCount > 0.2 * totalEmployees) {
					markedDates.add(new MarkedDate(date, true));
				} else {
					markedDates.add(new MarkedDate(date, false));
				}
			}

			return markedDates;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while processing the request.");
		}
	}

	private int getTotalEmployeeCount(List<PersonalInfo> employees) {
		return employees.size();
	}

	@Override
	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(Long employeeId, int year,
			int month) {
		return iLeaveRepository.calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(employeeId, year, month);
	}

}
