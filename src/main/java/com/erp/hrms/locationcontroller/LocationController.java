package com.erp.hrms.locationcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.locationentity.Location;
import com.erp.hrms.locationservice.LocationService;

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
	

}