package com.erp.hrms.approver.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.approver.entity.LeaveApprover;
import com.erp.hrms.approver.repository.LeaveApproverRepo;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.entity.response.EmployeeNotificationDTO;
import com.erp.hrms.entity.response.EmployeeResponseDTO;
import com.erp.hrms.entity.response.LeaveApproverDTO;
import com.erp.hrms.entity.response.LocationDTO;
import com.erp.hrms.form.repository.ILeaveRepository;

@Service
public class LeaveApproverServiceImpl implements LeaveApproverService {

	@Autowired
	private LeaveApproverRepo leaveApproverRepo;

	@Autowired
	private IPersonalInfoDAO dao;

	@Autowired
	private ILeaveRepository iLeaveRepository;

//	This method for create leave Approver 
	@Override
	public void createLeaveApprover(LeaveApprover leaveApprover) {
		try {
			leaveApproverRepo.save(leaveApprover);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create Leave Approver");
		}
	}

//	This method for find the approver by Id
	@Override
	public Optional<LeaveApprover> findLeaveApproverById(Long lAId) {
		try {
			return leaveApproverRepo.findById(lAId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to find Leave Approver by ID");
		}
	}

// 	This method for find all leave Approvers
	@Override
	public List<LeaveApprover> findAllLeaveApprovers() {
		try {
			return leaveApproverRepo.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve all Leave Approvers");
		}

	}

//	This method for update leave approver 
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

//	This method for find by first Approver employee Id and his employees list
	@Override
	public List<LeaveApproverDTO> findByFirstApproverEmpId(Long firstApproverEmpId) {
		List<LeaveApprover> leaveApprovers = leaveApproverRepo.findByFirstApproverEmpId(firstApproverEmpId);

		List<LeaveApproverDTO> resultDTOList = new ArrayList<>();
		for (LeaveApprover leaveApprover : leaveApprovers) {
			LeaveApproverDTO leaveApproverDTO = mapToDTO(leaveApprover);
			leaveApproverDTO.setEmployeeData(getEmployeeDataForLocations(leaveApprover.getLocations()));
			resultDTOList.add(leaveApproverDTO);
		}
		return resultDTOList;
	}

//	This method takes a list of Location objects and returns a list of EmployeeResponseDTO objects.
	private List<EmployeeResponseDTO> getEmployeeDataForLocations(List<Location> locations) {
		List<EmployeeResponseDTO> employeeResponseList = new ArrayList<>();
		for (Location location : locations) {
			String locationIdAsString = String.valueOf(location.getLocationId());
			List<PersonalInfo> personalInfoList = dao.getByPostedLocation(locationIdAsString);
			for (PersonalInfo personalInfo : personalInfoList) {
				EmployeeResponseDTO responseDTO = createEmployeeResponseDTO(personalInfo);
				employeeResponseList.add(responseDTO);
			}
		}
		return employeeResponseList;
	}

//	This method maps a LeaveApprover entity to a LeaveApproverDTO
	private LeaveApproverDTO mapToDTO(LeaveApprover leaveApprover) {
		LeaveApproverDTO leaveApproverDTO = new LeaveApproverDTO();
		leaveApproverDTO.setLAId(leaveApprover.getLAId());
		leaveApproverDTO.setFirstApproverEmpId(leaveApprover.getFirstApproverEmpId());
		leaveApproverDTO.setFirstApproverEmail(leaveApprover.getFirstApproverEmail());
		leaveApproverDTO.setSecondApproverEmpId(leaveApprover.getSecondApproverEmpId());
		leaveApproverDTO.setSecondApproverEmail(leaveApprover.getSecondApproverEmail());
		leaveApproverDTO.setStartDate(leaveApprover.getStartDate());
		leaveApproverDTO.setEndDate(leaveApprover.getEndDate());
		leaveApproverDTO.setLocations(mapLocationsToDTO(leaveApprover.getLocations()));
		leaveApproverDTO.setApproverLevels(leaveApprover.getApproverLevels());

		leaveApproverDTO.setEmployeeData(getEmployeeDataForLocations(leaveApprover.getLocations()));
		leaveApproverDTO.setEmployeeNotifications(getEmployeeNotificationsForLeaveApprover());
		return leaveApproverDTO;
	}

//	This method maps a list of Location entities to a list of LocationDTO objects using Java Streams.
	private List<LocationDTO> mapLocationsToDTO(List<Location> locations) {
		if (locations == null || locations.isEmpty()) {
			return Collections.emptyList();
		}
		return locations.stream().map(this::mapLocationToDTO).collect(Collectors.toList());
	}

//	This method maps a single Location entity to a LocationDTO object.
	private LocationDTO mapLocationToDTO(Location location) {
		LocationDTO locationDTO = new LocationDTO();
		locationDTO.setLocationId(location.getLocationId());
		locationDTO.setConcernedAuthorityEmpId(location.getConcernedAuthorityEmpId());
		locationDTO.setName(location.getName());
		locationDTO.setAddress(location.getAddress());
		locationDTO.setLatitude(location.getLatitude());
		locationDTO.setLongitude(location.getLongitude());
		locationDTO.setIsMaintenanceRequired(location.getIsMaintenanceRequired());
		locationDTO.setCommentsForMaintenance(location.getCommentsForMaintenance());
		locationDTO.setCountry(location.getCountry());
		locationDTO.setInchargeInfo(location.getInchargeInfo());
		return locationDTO;
	}

//	This method creates an EmployeeResponseDTO object from a PersonalInfo entity.
	private EmployeeResponseDTO createEmployeeResponseDTO(PersonalInfo personalInfo) {
		EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(personalInfo);
		return responseDTO;
	}

//	This method retrieves a list of pending leave approvals and converts them to a list of EmployeeNotificationDTO objects using the convertLeaveApprovalsToDTO method.
	private List<EmployeeNotificationDTO> getEmployeeNotificationsForLeaveApprover() {
		List<LeaveApproval> findAllLeaveApprovalPending = iLeaveRepository.findAllLeaveApprovalPending();
		List<EmployeeNotificationDTO> employeeNotifications = convertLeaveApprovalsToDTO(findAllLeaveApprovalPending);
		return employeeNotifications;
	}

//	This method converts a list of LeaveApproval entities to a list of EmployeeNotificationDTO objects.
	private List<EmployeeNotificationDTO> convertLeaveApprovalsToDTO(List<LeaveApproval> leaveApprovals) {
		List<EmployeeNotificationDTO> employeeNotifications = new ArrayList<>();
		for (LeaveApproval leaveApproval : leaveApprovals) {
			EmployeeNotificationDTO notificationDTO = new EmployeeNotificationDTO();
			notificationDTO.setLeaveRequestId(leaveApproval.getLeaveRequestId());
			notificationDTO.setEmployeeId(leaveApproval.getEmployeeId());
			notificationDTO.setHrApprovalStatus(leaveApproval.getHrApprovalStatus());
			notificationDTO.setNameOfEmployee(leaveApproval.getNameOfEmployee());
			notificationDTO.setDesignation(leaveApproval.getDesignation());
			notificationDTO.setDepartment(leaveApproval.getDepartment());
			notificationDTO.setJobLevel(leaveApproval.getJobLevel());
			notificationDTO.setLocation(leaveApproval.getLocation());
			notificationDTO.setNumberOfDaysRequested(leaveApproval.getNumberOfDaysRequested());
			notificationDTO.setLeaveReason(leaveApproval.getLeaveReason());
			notificationDTO.setRequestDate(leaveApproval.getRequestDate());
			notificationDTO.setStartDate(leaveApproval.getStartDate());
			notificationDTO.setEndDate(leaveApproval.getEndDate());
			notificationDTO.setApprovingManagerName(leaveApproval.getApprovingManagerName());
			notificationDTO.setApprovalStatus(leaveApproval.getApprovalStatus());
			notificationDTO.setApprovalRemarks(leaveApproval.getApprovalRemarks());
			notificationDTO.setManagerEmail(leaveApproval.getManagerEmail());
			notificationDTO.setHrName(leaveApproval.getHrName());
			notificationDTO.setHrEmail(leaveApproval.getHrEmail());
			notificationDTO.setEmail(leaveApproval.getEmail());
			notificationDTO.setContactNumber(leaveApproval.getContactNumber());
			notificationDTO.setEmergencyContactNumber(leaveApproval.getEmergencyContactNumber());
			notificationDTO.setNoOfLeavesApproved(leaveApproval.getNoOfLeavesApproved());

			employeeNotifications.add(notificationDTO);
		}
		return employeeNotifications;
	}

//	This method for find by second Approver employee Id and his employees list
	@Override
	public List<LeaveApproverDTO> findBySecondApproverEmpId(Long secondApproverEmpId) {
		List<LeaveApprover> leaveApprovers = leaveApproverRepo.findBySecondApproverEmpId(secondApproverEmpId);
		List<LeaveApproverDTO> resultDTOList = new ArrayList<>();

		for (LeaveApprover leaveApprover : leaveApprovers) {
			LeaveApproverDTO leaveApproverDTO = mapToDTO(leaveApprover);
			leaveApproverDTO.setEmployeeData(getEmployeeDataForLocations(leaveApprover.getLocations()));
			resultDTOList.add(leaveApproverDTO);
		}
		return resultDTOList;
	}

}
