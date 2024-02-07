package com.erp.hrms.locationcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.locationentity.Location;
import com.erp.hrms.locationentity.response.LocationDto;
import com.erp.hrms.locationservice.LocationService;

@RestController
@RequestMapping("/api/v1")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@PostMapping("/location")
	public ResponseEntity<?> createLocation(@RequestBody Location location) {
		locationService.createLocation(location);
		return new ResponseEntity<>(new MessageResponse("your location is created."), HttpStatus.CREATED);
	}

	@GetMapping("/location/id/{locationId}")
	public LocationDto findByLocationId(@PathVariable Long locationId) {
		return locationService.findByLocationId(locationId);
	}

	@GetMapping("/find/all/locations")
	public List<Location> findAllLocations() {
		return locationService.findAllLocations();
	}

	@PutMapping("/location/update/{locationId}")
	public ResponseEntity<?> updateLocation(@PathVariable Long locationId, @RequestBody Location location) {
		locationService.updateLocation(locationId, location);
		return new ResponseEntity<>(new MessageResponse("Your location is update."), HttpStatus.OK);
	}

}
