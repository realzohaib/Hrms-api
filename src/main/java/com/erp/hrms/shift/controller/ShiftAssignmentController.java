package com.erp.hrms.shift.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.shift.Dao.ShiftAssignmentDaoImpl;
import com.erp.hrms.shift.entity.ShiftAssignment;
import com.erp.hrms.shift.repo.ShiftAssignmentRepo;

@Controller
public class ShiftAssignmentController {
	@Autowired
	private ShiftAssignmentDaoImpl service;

	@Autowired
	private ShiftAssignmentRepo repo;

	// handler Method to assign shift to employee
	@PostMapping("/asign-shift")
	public ResponseEntity<?> saveShift(@RequestBody ShiftAssignment assign) {
		try {
			service.saveAssignedShift(assign);
			return ResponseEntity.ok(new MessageResponse("Shift Alloted"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Failed to save shift assignment"));
		}

	}

	// method to fetch all the employe shift
	@GetMapping("/get-shift")
	public ResponseEntity<?> getAllEmployeeShift() {
		try {
			List<ShiftAssignment> shifts = service.getAlEmployeeShifr();
			if (shifts.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(shifts);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Failed to fetch shift data: " + e.getMessage()));
		}
	}

	// method to fetch emp shift data by id
	@GetMapping("/get-shift-ById /{id}")
	public ResponseEntity<?> getAllEmployeeShiftById(@PathVariable long id) {

		if (!repo.existsByEmployeeId(id)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter valid Employee Id"));
		}
		try {
			List<ShiftAssignment> list = service.getAllEmployeeShiftById(id);
			if (list.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	// method to get emp current shift through emp id
	@GetMapping("/current-shift-ById /{id}")
	public ResponseEntity<?> currenthftById(@PathVariable long id) {
		if (!repo.existsByEmployeeId(id)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter valid Employee Id"));
		}
		try {
			return ResponseEntity.ok(service.currentShftById(id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	@PutMapping("/update-shift-ById /{id}")
	public ResponseEntity<?> updateShftById(@PathVariable long id, @RequestBody ShiftAssignment asign) {
		if (!repo.existsByEmployeeId(id)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter valid Employee Id"));
		}
		try {
			return ResponseEntity.ok(service.updateShift(asign, id));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	// fetches All employee details through their shift start date
	@GetMapping("/shift-byDate/{startDate}")
	public ResponseEntity<?> shiftByDate(@PathVariable String startDate) {
		try {
			LocalDate localDate = LocalDate.parse(startDate);
			List<ShiftAssignment> date = service.findByDate(localDate);
			if (date.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(date);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	// gives emp shift details by shift start date for single emp
	@GetMapping("/shift-byDateandId/{id}/{startDate}")
	public ResponseEntity<?> shiftByDateandempid(@PathVariable long id, @PathVariable String startDate) {
		if (!repo.existsByEmployeeId(id)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter valid Employee Id"));
		}
		try {
			LocalDate localDate = LocalDate.parse(startDate);
			List<ShiftAssignment> date = service.findshiftById(id, localDate);
			if (date.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(date);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	// fetching employees on the basis of shift and shift start date
	@GetMapping("/getShiftByName/{ShiftName}/{Date}")
	public ResponseEntity<?> findShiftAssignmentsByShiftNameAndDate(@PathVariable String ShiftName,
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate Date) {
		
		try {
			List<ShiftAssignment> date = service.findByShift_ShiftNameAndStartDate(ShiftName, Date);
			if (date.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(date);
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Failed to fetch data shift: " + e.getMessage()));
		}
	}

}
