package com.erp.hrms.Location.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.Location.entity.response.LeaveApproverDto;
import com.erp.hrms.Location.entity.response.LocationDto;
import com.erp.hrms.Location.repository.LocationRepository;
import com.erp.hrms.approver.entity.LeaveApprover;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public void createLocation(Location location) {
		locationRepository.save(location);
	}

	@Override
	public LocationDto findByLocationId(Long locationId) {

		LocationDto response = new LocationDto();
		Location findByLocationId = locationRepository.findByLocationId(locationId);

		response.setLocationId(findByLocationId.getLocationId());
		response.setConcernedAuthorityEmpId(findByLocationId.getConcernedAuthorityEmpId());
		response.setName(findByLocationId.getName());
		response.setAddress(findByLocationId.getAddress());
		response.setLatitude(findByLocationId.getLatitude());
		response.setLongitude(findByLocationId.getLongitude());
		response.setIsMaintenanceRequired(findByLocationId.isMaintenanceRequired());
		response.setCommentsForMaintenance(findByLocationId.getCommentsForMaintenance());
		response.setInchargeInfo(findByLocationId.getInchargeInfo());

		List<LeaveApproverDto> leaveApprovers = new ArrayList<>();
		List<LeaveApprover> leaveApproverEntities = findByLocationId.getLeaveApprover();

		for (LeaveApprover leaveApproverEntity : leaveApproverEntities) {
			LeaveApproverDto leaveApproverDto = new LeaveApproverDto();
			leaveApproverDto.setFirstApproverEmpId(leaveApproverEntity.getFirstApproverEmpId());
			leaveApproverDto.setLAId(leaveApproverEntity.getLAId());
			leaveApproverDto.setSecondApproverEmpId(leaveApproverEntity.getSecondApproverEmpId());
			leaveApproverDto.setStartDate(leaveApproverEntity.getStartDate());
			leaveApproverDto.setEndDate(leaveApproverEntity.getEndDate());

			leaveApprovers.add(leaveApproverDto);
		}

		response.setLeaveApprovers(leaveApprovers);
		return response;
	}

	@Override
	public List<Location> findAllLocations() {
		return locationRepository.findAll();
	}

}
