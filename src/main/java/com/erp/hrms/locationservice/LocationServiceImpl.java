package com.erp.hrms.locationservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.approver.entity.LeaveApprover;
import com.erp.hrms.locationdao.LocationRepository;
import com.erp.hrms.locationentity.Location;
import com.erp.hrms.locationentity.response.LeaveApproverDto;
import com.erp.hrms.locationentity.response.LocationDto;

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
		response.setIsMaintenanceRequired(findByLocationId.getIsMaintenanceRequired());
		response.setCommentsForMaintenance(findByLocationId.getCommentsForMaintenance());
		response.setInchargeInfo(findByLocationId.getInchargeInfo());
		response.setCountry(findByLocationId.getCountry());

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

	@Override
	public Location updateLocation(Long locationId, Location location) {
		Location existingLocation = locationRepository.findById(locationId)
				.orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
		existingLocation.setConcernedAuthorityEmpId(location.getConcernedAuthorityEmpId());
		existingLocation.setName(location.getName());
		existingLocation.setAddress(location.getAddress());
		existingLocation.setLatitude(location.getLatitude());
		existingLocation.setLongitude(location.getLongitude());
		existingLocation.setIsMaintenanceRequired(location.getIsMaintenanceRequired());
		existingLocation.setCommentsForMaintenance(location.getCommentsForMaintenance());
		existingLocation.setCountry(location.getCountry());
		existingLocation.setInchargeInfo(location.getInchargeInfo());

		return locationRepository.save(existingLocation);
	}
}
