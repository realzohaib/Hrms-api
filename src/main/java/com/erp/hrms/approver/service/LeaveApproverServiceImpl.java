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
import com.erp.hrms.entity.response.EmployeeResponseDTO;
import com.erp.hrms.entity.response.LeaveApproverDTO;
import com.erp.hrms.entity.response.LocationDTO;

@Service
public class LeaveApproverServiceImpl implements LeaveApproverService {

	@Autowired
	private LeaveApproverRepo leaveApproverRepo;

	@Autowired
	private IPersonalInfoDAO dao;

	@Override
	public void createLeaveApprover(LeaveApprover leaveApprover) {
		try {
			leaveApproverRepo.save(leaveApprover);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create Leave Approver");
		}
	}

	@Override
	public Optional<LeaveApprover> findLeaveApproverById(Long lAId) {
		try {
			return leaveApproverRepo.findById(lAId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to find Leave Approver by ID");
		}
	}

	@Override
	public List<LeaveApprover> findAllLeaveApprovers() {
		try {
			return leaveApproverRepo.findAll();
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve all Leave Approvers");
		}

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

	private List<EmployeeResponseDTO> getEmployeeDataForLocations(List<Location> locations) {
		List<EmployeeResponseDTO> employeeResponseList = new ArrayList<>();

		for (Location location : locations) {
			// Convert Long to String before calling the existing method
			String locationIdAsString = String.valueOf(location.getLocationId());

			List<PersonalInfo> personalInfoList = dao.getByPostedLocation(locationIdAsString);

			for (PersonalInfo personalInfo : personalInfoList) {
				EmployeeResponseDTO responseDTO = createEmployeeResponseDTO(personalInfo);
				employeeResponseList.add(responseDTO);
			}
		}

		return employeeResponseList;
	}

	private LeaveApproverDTO mapToDTO(LeaveApprover leaveApprover) {
		LeaveApproverDTO leaveApproverDTO = new LeaveApproverDTO();
		leaveApproverDTO.setFirstApproverEmpId(leaveApprover.getFirstApproverEmpId());
		leaveApproverDTO.setFirstApproverEmail(leaveApprover.getFirstApproverEmail());
		leaveApproverDTO.setSecondApproverEmpId(leaveApprover.getSecondApproverEmpId());
		leaveApproverDTO.setSecondApproverEmail(leaveApprover.getSecondApproverEmail());
		leaveApproverDTO.setStartDate(leaveApprover.getStartDate());
		leaveApproverDTO.setEndDate(leaveApprover.getEndDate());
		leaveApproverDTO.setLocations(mapLocationsToDTO(leaveApprover.getLocations()));
		leaveApproverDTO.setApproverLevels(leaveApprover.getApproverLevels());
		leaveApproverDTO.setLAId(leaveApprover.getLAId());

		// Set employee data for each location
		leaveApproverDTO.setEmployeeData(getEmployeeDataForLocations(leaveApprover.getLocations()));

		return leaveApproverDTO;
	}

	private List<LocationDTO> mapLocationsToDTO(List<Location> locations) {
		if (locations == null || locations.isEmpty()) {
			return Collections.emptyList();
		}

		return locations.stream().map(this::mapLocationToDTO).collect(Collectors.toList());
	}

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

	private EmployeeResponseDTO createEmployeeResponseDTO(PersonalInfo personalInfo) {
		EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(personalInfo);
		return responseDTO;
	}

}
