package com.erp.hrms.locationservice;

import java.util.List;

import com.erp.hrms.locationentity.Location;
import com.erp.hrms.locationentity.response.LocationDto;

public interface LocationService {

	public void createLocation(Location location);

	public LocationDto findByLocationId(Long locationId);

	public List<Location> findAllLocations();
	
	public Location updateLocation(Long locationId, Location location);
}
