package com.erp.hrms.form.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.academiccalender.dao.CalendarRepository;
import com.erp.hrms.academiccalender.entity.AcademicCalendar;
import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.approver.entity.LeaveApprover;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.entity.form.LeaveCalendarData;
import com.erp.hrms.entity.form.LeaveCountDTO;
import com.erp.hrms.entity.form.LeaveDataDTO;
import com.erp.hrms.entity.form.LeaveEmployee;
import com.erp.hrms.entity.form.LeaveType;
import com.erp.hrms.entity.form.MarkedDate;
import com.erp.hrms.exception.LeaveRequestApprovalException;
import com.erp.hrms.exception.LeaveRequestNotFoundException;
import com.erp.hrms.form.repository.ILeaveRepository;
import com.erp.hrms.form.repository.IleaveApprovalRepo;
import com.erp.hrms.locationdao.LocationRepository;
import com.erp.hrms.locationentity.Location;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LeaveService implements ILeaveService {

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

	@Autowired
	private ILeaveRepository iLeaveRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private IPersonalInfoDAO iPersonalInfoDAO;

	@Autowired
	private FileService fileService;

	@Autowired
	private IleaveApprovalRepo repo;

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private LocationRepository locationRepository;

//	This method for send the leave request to manager and send email to manager and admin
	@Override
//	public LeaveApprover createLeaveApproval(String leaveApproval, MultipartFile medicalDocumentsName)
//			throws IOException {
//		try {
//			LeaveApprover approver = null;
//			ObjectMapper mapper = new ObjectMapper();
//			LeaveApproval leaveApprovalJson = mapper.readValue(leaveApproval, LeaveApproval.class);
//			LeaveType leaveType = entityManager.find(LeaveType.class,
//					leaveApprovalJson.getLeaveType().getLeaveTypeId());
//			leaveApprovalJson.setLeaveType(leaveType);
//			leaveApprovalJson.setNoOfLeavesApproved(leaveApprovalJson.getNumberOfDaysRequested());
//			leaveApprovalJson.setApprovedStartDate(leaveApprovalJson.getStartDate());
//			leaveApprovalJson.setApprovedEndDate(leaveApprovalJson.getEndDate());
//
//			Long locationId = Long.parseLong(leaveApprovalJson.getLocation());
//			Location locations = locationRepository.findByLocationId(locationId);
//			if (locations == null) {
//				throw new RuntimeException("Location not found");
//			}
//			List<LeaveApprover> approvers = locations.getLeaveApprover();
//			if (!approvers.isEmpty()) {
//				approver = approvers.stream()
//						.filter(leaveApprover -> leaveApprover.getEndDate() == null
//								&& leaveApprover.getLocations().contains(locations)
//								&& leaveApprover.getApproverLevels().contains(leaveApprovalJson.getJobLevel()))
//						.findFirst().orElse(null);
//				if (approver == null) {
//					throw new RuntimeException("No matching LeaveApprover found for the location and level");
//				}
//				// Determine whether to send to both approvers or only the first approver
//				if (leaveApprovalJson.getNoOfLeavesApproved() <= 3) {
//					sendLeaveRequestEmail(approver.getFirstApproverEmail(),
//							leaveApprovalJson.getEmployeeId() + " Leave request submitted by employeeId for review",
//							leaveApprovalJson);
//				} else {
//					sendLeaveRequestEmail(approver.getFirstApproverEmail(),
//							leaveApprovalJson.getEmployeeId() + " Leave request submitted by employeeId for review",
//							leaveApprovalJson);
//					if (approver.getSecondApproverEmail() != null) {
//						sendLeaveRequestEmail(approver.getSecondApproverEmail(),
//								leaveApprovalJson.getEmployeeId() + " Leave request submitted by employeeId for review",
//								leaveApprovalJson);
//					}
//				}
//			}
//			if (medicalDocumentsName != null && !medicalDocumentsName.isEmpty()) {
//				String uniqueIdentifier = UUID.randomUUID().toString();
//				String originalFileName = medicalDocumentsName.getOriginalFilename();
//				String fileNameWithUniqueIdentifier = uniqueIdentifier + "_" + originalFileName;
//				Path fileNameAndPath = Paths.get(uplaodDirectory, fileNameWithUniqueIdentifier);
//				Files.write(fileNameAndPath, medicalDocumentsName.getBytes());
//				leaveApprovalJson.setMedicalDocumentsName(fileNameWithUniqueIdentifier);
//			}
//			iLeaveRepository.createLeaveApproval(leaveApprovalJson);
//			return approver;
//		} catch (Exception e) {
//			throw new RuntimeException("An error occurred while sending your request." + e);
//		}
//	}

	public LeaveApprover createLeaveApproval(String leaveApproval, MultipartFile medicalDocumentsName)
			throws IOException {
		try {
			LeaveApprover approver = null;
			ObjectMapper mapper = new ObjectMapper();
			LeaveApproval leaveApprovalJson = mapper.readValue(leaveApproval, LeaveApproval.class);
			LeaveType leaveType = entityManager.find(LeaveType.class,
					leaveApprovalJson.getLeaveType().getLeaveTypeId());
			leaveApprovalJson.setLeaveType(leaveType);
			leaveApprovalJson.setNoOfLeavesApproved(leaveApprovalJson.getNumberOfDaysRequested());
			leaveApprovalJson.setApprovedStartDate(leaveApprovalJson.getStartDate());
			leaveApprovalJson.setApprovedEndDate(leaveApprovalJson.getEndDate());

			Long locationId = Long.parseLong(leaveApprovalJson.getLocation());
			Location locations = locationRepository.findByLocationId(locationId);
			if (locations == null) {
				throw new RuntimeException("Location not found");
			}
			List<LeaveApprover> approvers = locations.getLeaveApprover();
			if (!approvers.isEmpty()) {
				approver = approvers.stream()
						.filter(leaveApprover -> leaveApprover.getEndDate() == null
								&& leaveApprover.getLocations().contains(locations)
								&& leaveApprover.getApproverLevels().contains(leaveApprovalJson.getJobLevel()))
						.findFirst().orElse(null);
				if (approver == null) {
					throw new RuntimeException("No matching LeaveApprover found for the location and level");
				}

				// Get details of the first approver based on firstApproverEmpId
				PersonalInfo firstApproverDetails = iPersonalInfoDAO
						.getPersonalInfoByEmployeeId(approver.getFirstApproverEmpId());

				if (approver.getFirstApproverEmail() != null) {
					String emailContent = "Hi " + firstApproverDetails.getNamePrefix() + " "
							+ firstApproverDetails.getFirstName() + " " + firstApproverDetails.getMiddleName() + " "
							+ firstApproverDetails.getLastName() + ",\n\n" + "Your delegate "
							+ leaveApprovalJson.getNameOfEmployee() + " (" + leaveApprovalJson.getEmployeeId()
							+ ") has applied for a leave request for " + leaveApprovalJson.getNumberOfDaysRequested()
							+ " on " + leaveApprovalJson.getRequestDate()
							+ ". Please review and take the necessary action on it.\n\n" + "Employee Name: "
							+ leaveApprovalJson.getNameOfEmployee() + "\n" + "Employee Id: "
							+ leaveApprovalJson.getEmployeeId() + "\n" + "Request date: "
							+ leaveApprovalJson.getRequestDate() + "\n" + "Leave start date: "
							+ leaveApprovalJson.getStartDate() + "\n" + "Leave end date: "
							+ leaveApprovalJson.getEndDate() + "\n" + "Reason: " + leaveApprovalJson.getLeaveReason()
							+ "\n" + "Alternate contact number: " + leaveApprovalJson.getEmergencyContactNumber()
							+ "\n\n" + "Regards,\n" + "HRMS System mailer";

					// Determine whether to send to both approvers or only the first approver
					if (leaveApprovalJson.getNoOfLeavesApproved() <= 3) {
						sendLeaveRequestEmail(approver.getFirstApproverEmail(),
								leaveApprovalJson.getEmployeeId() + " Leave request submitted by employeeId for review",
								leaveApprovalJson, emailContent);
					} else {
						sendLeaveRequestEmail(approver.getFirstApproverEmail(),
								leaveApprovalJson.getEmployeeId() + " Leave request submitted by employeeId for review",
								leaveApprovalJson, emailContent);
						if (approver.getSecondApproverEmail() != null) {
							sendLeaveRequestEmail(approver.getSecondApproverEmail(),
									leaveApprovalJson.getEmployeeId()
											+ " Leave request submitted by employeeId for review",
									leaveApprovalJson, emailContent);
						}
					}
				} else {
					throw new RuntimeException("Employee details not found");
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
			return approver;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while sending your request." + e);
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
			existingApproval.setNoOfLeavesApproved(leaveApprovalJson.getNoOfLeavesApproved());
			double noOfLeavesApproved = leaveApprovalJson.getNoOfLeavesApproved();
			existingApproval.setApprovedStartDate(leaveApprovalJson.getApprovedStartDate());
			existingApproval.setApprovedEndDate(leaveApprovalJson.getApprovedEndDate());

			if (leaveApprovalJson.getApprovalStatus().equals("Accepted")) {
//				sendLeaveRequestApprovedEmail(existingApproval.getEmail(),
//						leaveApprovalJson.getEmployeeId() + " Leave request for employeeId approved", existingApproval);
			} else {
//				sendLeaveRequestRejectedEmail(existingApproval.getEmail(),
//						leaveApprovalJson.getEmployeeId() + " Leave request for employeeId rejected", existingApproval);
			}

			LeaveApprover approver = getLeaveApprover(existingApproval);

			// Get details of the first approver based on firstApproverEmpId
			if (noOfLeavesApproved > 3) {
				PersonalInfo secondApproverDetails = iPersonalInfoDAO
						.getPersonalInfoByEmployeeId(approver.getSecondApproverEmpId());

				String emailContent = "Hi " + secondApproverDetails.getNamePrefix() + " "
						+ secondApproverDetails.getFirstName() + " " + secondApproverDetails.getMiddleName() + " "
						+ secondApproverDetails.getLastName() + ",\n\n" + "Leave request "
						+ leaveApprovalJson.getEmployeeId() + " submitted by " + leaveApprovalJson.getNameOfEmployee()
						+ " (" + leaveApprovalJson.getEmployeeId() + "), " + leaveApprovalJson.getDesignation()
						+ " at location " + leaveApprovalJson.getLocation()
						+ ", is pending for HR review. Please review and take the necessary action on it.\n\n"
						+ "Employee Name: " + leaveApprovalJson.getNameOfEmployee() + "\n" + "Employee Id: "
						+ leaveApprovalJson.getEmployeeId() + "\n" + "Manager name: "
						+ leaveApprovalJson.getApprovingManagerName() + "\n" + "Approval manager email id: "
						+ leaveApprovalJson.getManagerEmail() + "\n" + "Leave reason: "
						+ leaveApprovalJson.getLeaveReason() + "\n" + "Manager remarks: "
						+ leaveApprovalJson.getApprovalRemarks() + "\n" + "Number of leaves requested: "
						+ leaveApprovalJson.getNumberOfDaysRequested() + "\n" + "Number of leaves approved: "
						+ leaveApprovalJson.getNoOfLeavesApproved() + "\n" + "Alternate contact number: "
						+ leaveApprovalJson.getEmergencyContactNumber() + "\n\n" + "Regards,\n" + "HRMS Mail system";

//			if (noOfLeavesApproved > 3) {
//				If the employee ID of the first approver and the employee ID of the second approver are the same
				if (approver.getFirstApproverEmpId().equals(approver.getSecondApproverEmpId())) {
					existingApproval.setApprovalStatus(leaveApprovalJson.getApprovalStatus());
					existingApproval.setHrApprovalStatus(leaveApprovalJson.getApprovalStatus());
				} else {

					sendLeaveRequestForwardedToHREmail(approver.getSecondApproverEmail(),
							leaveApprovalJson.getEmployeeId()
									+ " Leave request by employeeId submitted for final review",
							existingApproval, emailContent);

					existingApproval.setHrApprovalStatus("Pending");
				}
			} else {
				existingApproval.setHrApprovalStatus(existingApproval.getApprovalStatus());
			}

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
				existingApproval.setMedicalDocumentsName(fileNameWithUniqueIdentifier);
			}

			return iLeaveRepository.approvedByManager(leaveRequestId, existingApproval);
		} catch (Exception e) {
			throw new LeaveRequestApprovalException(new MessageResponse("Error while approving leave request." + e));
		}
	}

// In this method find approver of employee with the help of location id and level 
	private LeaveApprover getLeaveApprover(LeaveApproval leaveApproval) {
		Location locations = getLocationForLeaveApproval(leaveApproval);
		if (locations == null) {
			throw new RuntimeException("Location not found");
		}
		List<LeaveApprover> approvers = locations.getLeaveApprover();
		if (approvers.isEmpty()) {
			throw new RuntimeException("No LeaveApprovers found for the location");
		}
		LeaveApprover approver = approvers.stream()
				.filter(leaveApprover -> leaveApprover.getEndDate() == null
						&& leaveApprover.getLocations().contains(locations)
						&& leaveApprover.getApproverLevels().contains(leaveApproval.getJobLevel()))
				.findFirst().orElse(null);
		if (approver == null) {
			throw new RuntimeException("No matching LeaveApprover found for the location and level");
		}
		return approver;
	}

//	In this method find the location with the help of location id 
	private Location getLocationForLeaveApproval(LeaveApproval leaveApproval) {
		Long locationId = Long.parseLong(leaveApproval.getLocation());
		return locationRepository.findByLocationId(locationId);
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
			existingApproval.setNoOfLeavesApproved(leaveApprovalJson.getNoOfLeavesApproved());
			existingApproval.setApprovedStartDate(leaveApprovalJson.getApprovedStartDate());
			existingApproval.setApprovedEndDate(leaveApprovalJson.getApprovedEndDate());

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

			if (leaveApprovalJson.getHrApprovalStatus().equals("Accepted")) {
				sendHRLeaveRequestApprovedEmail(existingApproval.getEmail(),
						leaveApprovalJson.getEmployeeId() + " Leave request for employeeId approved", existingApproval);
			} else {
				sendHRLeaveRequestRejectedEmail(existingApproval.getEmail(),
						leaveApprovalJson.getEmployeeId() + " Leave request for employeeId rejected", existingApproval);
			}

			return iLeaveRepository.approvedOrDenyByHR(leaveRequestId, existingApproval);
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
				LeaveEmployee leaveEmployee = new LeaveEmployee();
				leaveEmployee.setEmployeeId(approval.getEmployeeId());
				leaveEmployee.setEmployeeName(approval.getNameOfEmployee());
				if (existingData.isPresent()) {
					existingData.get().incrementEmployeeCount();
					existingData.get().getLeaveEmployees().add(leaveEmployee);
				} else {
					LeaveCalendarData data = new LeaveCalendarData(date);
					data.incrementEmployeeCount();
					data.setLeaveEmployees(new ArrayList<>(Collections.singletonList(leaveEmployee)));
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
	public BigDecimal calculateTotalNoOfLeavesApprovedByEmployeeInMonthAndStatus(Long employeeId, int year, int month) {
		return iLeaveRepository.calculateTotalNoOfLeavesApprovedByEmployeeInMonthAndStatus(employeeId, year, month);
	}

//	This method find all leave in a year of particular employee 
	@Override
	public List<LeaveCountDTO> getAllLeavesByEmployeeIdAndYear(Long employeeId, int year, String countryName) {

		List<LeaveApproval> leaveApprovals = repo.findByEmployeeIdAndHrApprovalStatus(employeeId, "Accepted");
		List<AcademicCalendar> holidays = calendarRepository.findByYearAndCountry(year, countryName);
		Map<String, Double> leaveTypeTotalDaysMap = new HashMap<>();

		for (LeaveApproval leaveApproval : leaveApprovals) {
			LocalDate startDate = LocalDate.parse(leaveApproval.getStartDate());
			LocalDate endDate = LocalDate.parse(leaveApproval.getEndDate());

			if (endDate.isAfter(LocalDate.of(year, 1, 1)) && startDate.isBefore(LocalDate.of(year + 1, 1, 1))) {
				LocalDate startOfYear = startDate.isBefore(LocalDate.of(year, 1, 1)) ? LocalDate.of(year, 1, 1)
						: startDate;
				LocalDate endOfYear = endDate.isAfter(LocalDate.of(year + 1, 1, 1))
						? LocalDate.of(year + 1, 1, 1).minusDays(1)
						: endDate;

				double diffInDays = ChronoUnit.DAYS.between(startOfYear, endOfYear) + 1;
				String leaveName = leaveApproval.getLeaveType().getLeaveName();
				if ("Half Day".equalsIgnoreCase(leaveName)) {
					diffInDays = diffInDays / 2.0;
					leaveName = "Casual"; // Assign "Casual" as the leave name for half days
				}

				for (AcademicCalendar holiday : holidays) {
					LocalDate startHolidayDate = holiday.getStartHolidayDate();
					LocalDate endHolidayDate = holiday.getEndHolidayDate();
					if (startDate.isBefore(endHolidayDate) && endDate.isAfter(startHolidayDate)) {
						LocalDate overlapStartDate = startDate.isAfter(startHolidayDate) ? startDate : startHolidayDate;
						LocalDate overlapEndDate = endDate.isBefore(endHolidayDate) ? endDate : endHolidayDate;
						diffInDays -= calculateDaysInRange(overlapStartDate, overlapEndDate);
					}
				}

				LocalDate currentDate = startOfYear;
				while (!currentDate.isAfter(endOfYear)) {
					if (currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
						diffInDays--;
					}
					currentDate = currentDate.plusDays(1);
				}

				leaveTypeTotalDaysMap.merge(leaveName, (double) diffInDays, Double::sum);
			}
		}
		List<LeaveCountDTO> result = leaveTypeTotalDaysMap.entrySet().stream()
				.map(entry -> new LeaveCountDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList());
		return result;
	}

	@Override
	public List<LeaveCountDTO> getAllLeaveByMonthByEmployeeId(int year, int month, long employeeId,
			String countryName) {
		List<LeaveApproval> leaves = repo.findByEmployeeIdAndHrApprovalStatus(employeeId, "Accepted");
		List<AcademicCalendar> holidays = calendarRepository.findByYearAndCountry(year, countryName);
		Map<String, Double> leaveTypeTotalDaysMap = new HashMap<>();
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		for (LeaveApproval leave : leaves) {
			String startDateString = leave.getStartDate();
			String endDateString = leave.getEndDate();
			LocalDate startDate = LocalDate.parse(startDateString);
			LocalDate endDate = LocalDate.parse(endDateString);

			if (endDate.isAfter(firstDayOfMonth.minusDays(1)) && startDate.isBefore(lastDayOfMonth.plusDays(1))) {
				startDate = startDate.isBefore(firstDayOfMonth) ? firstDayOfMonth : startDate;
				endDate = endDate.isAfter(lastDayOfMonth) ? lastDayOfMonth : endDate;
				double daysBetweenInclusive = ChronoUnit.DAYS.between(startDate, endDate) + 1;

				if ("Half Day".equalsIgnoreCase(leave.getLeaveType().getLeaveName())) {
					daysBetweenInclusive = (double) daysBetweenInclusive / 2;
				}

				for (AcademicCalendar holiday : holidays) {
					LocalDate startHolidayDate = holiday.getStartHolidayDate();
					LocalDate endHolidayDate = holiday.getEndHolidayDate();
					if (startDate.isBefore(endHolidayDate) && endDate.isAfter(startHolidayDate)) {
						LocalDate overlapStartDate = startDate.isAfter(startHolidayDate) ? startDate : startHolidayDate;
						LocalDate overlapEndDate = endDate.isBefore(endHolidayDate) ? endDate : endHolidayDate;
						daysBetweenInclusive -= calculateDaysInRange(overlapStartDate, overlapEndDate);
					}
				}

				LocalDate currentDate = startDate;
				while (!currentDate.isAfter(endDate)) {
					DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
					if (dayOfWeek == DayOfWeek.SUNDAY) {
						daysBetweenInclusive -= 1;
					}
					currentDate = currentDate.plusDays(1);
				}

				String leaveName = leave.getLeaveType().getLeaveName();
				if ("Half Day".equalsIgnoreCase(leaveName)) {
					leaveName = "Casual"; // Assign "Casual" as the leave name for half days
				}
				leaveTypeTotalDaysMap.merge(leaveName, (double) daysBetweenInclusive, Double::sum);
			}
		}
		List<LeaveCountDTO> result = leaveTypeTotalDaysMap.entrySet().stream()
				.map(entry -> new LeaveCountDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList());
		return result;
	}

	// Helper method to calculate days between two dates inclusive
	private int calculateDaysInRange(LocalDate startDate, LocalDate endDate) {
		return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
	}

//	This method give the total count of employee on leave on a particular date and also give the list of employee.
	@Override
	public LeaveDataDTO getLeaveDataByDate(String date) {
		List<LeaveApproval> leaveApprovals = repo.findByDate(date);
		List<LeaveApproval> acceptedLeaveApprovals = leaveApprovals.stream()
				.filter(approval -> "Accepted".equalsIgnoreCase(approval.getHrApprovalStatus()))
				.collect(Collectors.toList());
		long employeeIdCount = leaveApprovals.stream().map(LeaveApproval::getEmployeeId).distinct().count();
		return new LeaveDataDTO(acceptedLeaveApprovals, employeeIdCount);
	}

//	This method is for sending the mail to the manager.
	private void sendLeaveRequestEmail(String to, String subject, LeaveApproval leaveApproval, String emailContent) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(emailContent);
		mailSender.send(message);
	}

//	private void sendLeaveRequestEmail(String to, String subject, LeaveApproval leaveApproval) {
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(to);
//		message.setSubject(subject);
//
//		String emailContent = "Hi Manager,\n\n" + "Your delegate " + leaveApproval.getNameOfEmployee()
//				+ " (" + leaveApproval.getEmployeeId() + ") has applied for a leave request for "
//				+ leaveApproval.getNumberOfDaysRequested() + " on " + leaveApproval.getRequestDate()
//				+ ". Please review and take the necessary action on it.\n\n" + "Employee Name: "
//				+ leaveApproval.getNameOfEmployee() + "\n" + "Employee Id: " + leaveApproval.getEmployeeId() + "\n"
//				+ "Request date: " + leaveApproval.getRequestDate() + "\n" + "Leave start date: "
//				+ leaveApproval.getStartDate() + "\n" + "Leave end date: " + leaveApproval.getEndDate() + "\n"
//				+ "Reason: " + leaveApproval.getLeaveReason() + "\n" + "Alternate contact number: "
//				+ leaveApproval.getEmergencyContactNumber() + "\n\n" + "Regards,\n" + "HRMS System mailer";
//		message.setText(emailContent);
//		mailSender.send(message);
//	}

//	This method is for sending to the employee if his leave request is accepted.
	private void sendLeaveRequestApprovedEmail(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);

		String emailContent = "Hi " + leaveApproval.getNameOfEmployee() + ",\n\n" + "Your leave request "
				+ leaveApproval.getEmployeeId() + " has been approved by your manager "
				+ leaveApproval.getApprovingManagerName() + " for " + leaveApproval.getNoOfLeavesApproved() + " days. "
				+ "Your leave starts from " + leaveApproval.getApprovedStartDate() + " till "
				+ leaveApproval.getApprovedEndDate() + ".\n\n" + "Regards,\n" + "HRMS Mailer system";
		message.setText(emailContent);
		mailSender.send(message);
	}

//	This method is for sending to the employee if his leave request is rejected.
	private void sendLeaveRequestRejectedEmail(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);

		String emailContent = "Hi " + leaveApproval.getNameOfEmployee() + ",\n\n" + "Your leave request "
				+ leaveApproval.getLeaveRequestId() + " has been rejected by your manager "
				+ leaveApproval.getApprovingManagerName() + ".\n\n" + "Regards,\n" + "HRMS Mailer system";
		message.setText(emailContent);
		mailSender.send(message);
	}

//	This method is for sending the leave request if the number of requested days is more than 3 days and after the first approver's response.
	private void sendLeaveRequestForwardedToHREmail(String to, String subject, LeaveApproval leaveApproval,
			String emailContent) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(emailContent);
		mailSender.send(message);
	}

	private void sendLeaveRequestForwardedToHREmail(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);

		String emailContent = "Hi HR" + ",\n\n" + "Leave request " + leaveApproval.getEmployeeId() + " submitted by "
				+ leaveApproval.getNameOfEmployee() + " (" + leaveApproval.getEmployeeId() + "), "
				+ leaveApproval.getDesignation() + " at location " + leaveApproval.getLocation()
				+ ", is pending for HR review. Please review and take the necessary action on it.\n\n"
				+ "Employee Name: " + leaveApproval.getNameOfEmployee() + "\n" + "Employee Id: "
				+ leaveApproval.getEmployeeId() + "\n" + "Manager name: " + leaveApproval.getApprovingManagerName()
				+ "\n" + "Approval manager email id: " + leaveApproval.getManagerEmail() + "\n" + "Leave reason: "
				+ leaveApproval.getLeaveReason() + "\n" + "Manager remarks: " + leaveApproval.getApprovalRemarks()
				+ "\n" + "Number of leaves requested: " + leaveApproval.getNumberOfDaysRequested() + "\n"
				+ "Number of leaves approved: " + leaveApproval.getNoOfLeavesApproved() + "\n"
				+ "Alternate contact number: " + leaveApproval.getEmergencyContactNumber() + "\n\n" + "Regards,\n"
				+ "HRMS Mail system";
		message.setText(emailContent);
		mailSender.send(message);
	}

//	This method is for sending to the employee if his leave request is accepted by hr.
	private void sendHRLeaveRequestApprovedEmail(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);

		String emailContent = "Hi " + leaveApproval.getNameOfEmployee() + ",\n\n" + "Your leave request "
				+ leaveApproval.getLeaveRequestId() + " has been approved by HR manager " + leaveApproval.getHrName()
				+ " for " + leaveApproval.getNoOfLeavesApproved() + " days. " + "Your leave starts from "
				+ leaveApproval.getApprovedStartDate() + " till " + leaveApproval.getApprovedEndDate() + ".\n\n"
				+ "Regards,\n" + "HRMS Mailer system";
		message.setText(emailContent);
		mailSender.send(message);
	}

//	This method is for sending to the employee if his leave request is rejected by hr.
	private void sendHRLeaveRequestRejectedEmail(String to, String subject, LeaveApproval leaveApproval) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);

		String emailContent = "Hi " + leaveApproval.getNameOfEmployee() + ",\n\n" + "Your leave request "
				+ leaveApproval.getEmployeeId() + " has been rejected by HR manager " + leaveApproval.getHrName()
				+ ".\n\n" + "Regards,\n" + "HRMS Mailer system";
		message.setText(emailContent);
		mailSender.send(message);
	}

}
