package com.erp.hrms.locationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.locationdao.LocationRepository;
import com.erp.hrms.locationentity.Location;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public void createLocation(Location location) {

		locationRepository.save(location);
	}

}
