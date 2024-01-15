package com.erp.hrms.Location.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.Location.entity.response.LocationDto;
import com.erp.hrms.Location.service.LocationService;

@RestController
@RequestMapping("/api/v1")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@PostMapping("/location")
	public ResponseEntity<String> createLocation(@RequestBody Location location) {
		locationService.createLocation(location);
		return new ResponseEntity<>("Location created successfully", HttpStatus.CREATED);
	}

	@GetMapping("/location/id/{locationId}")
	public LocationDto findByLocationId(@PathVariable Long locationId) {
		return locationService.findByLocationId(locationId);
	}

	@GetMapping("/find/all/locations")
	public List<Location> findAllLocations() {
		return locationService.findAllLocations();
	}
	
}
