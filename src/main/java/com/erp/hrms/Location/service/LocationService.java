package com.erp.hrms.Location.service;

import java.util.List;

import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.Location.entity.response.LocationDto;

public interface LocationService {

	public void createLocation(Location location);

	public LocationDto findByLocationId(Long locationId);
	
	public List<Location> findAllLocations();
}
