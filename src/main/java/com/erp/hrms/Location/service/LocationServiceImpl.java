package com.erp.hrms.Location.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.Location.repository.LocationRepository;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;

	@Override
	public void createLocation(Location location) {

		locationRepository.save(location);
	}

}
