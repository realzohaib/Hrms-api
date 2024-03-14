package com.erp.hrms.shift.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.shift.dao.ShiftAssignmentDaoImpl;
import com.erp.hrms.shift.entity.ShiftAssignment;
import com.erp.hrms.shift.repo.ShiftAssignmentRepo;

@Controller
@RequestMapping("/api/v1")
public class ShiftAssignmentController {
	@Autowired
	private ShiftAssignmentDaoImpl service;

	@Autowired
	private ShiftAssignmentRepo repo;

	// handler Method to assign shift to employee
//	@PostMapping("/asign-shift")
	@PostMapping("/shift/asign")
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
//	@GetMapping("/get-Allshift")
	@GetMapping("/shift/asign")
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
//	@GetMapping("/get-shift-ById/{id}")
	@GetMapping("/shift/asign/{empId}")
	public ResponseEntity<?> getAllEmployeeShiftById(@PathVariable long empId) {

		if (!repo.existsByEmployeeId(empId)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter valid Employee Id"));
		}
		try {
			List<ShiftAssignment> list = service.getAllEmployeeShiftById(empId);
			if (list.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	// method to get emp current shift through emp id
//	@GetMapping("/current-shift-ById /{id}")
	@GetMapping("/shift/asign/current/{empId}")
	public ResponseEntity<?> currenthftById(@PathVariable long empId) {
		if (!repo.existsByEmployeeId(empId)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter valid Employee Id"));
		}
		try {
			return ResponseEntity.ok(service.currentShftById(empId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

//	@PutMapping("/update-shift-ById")
	@PutMapping("/shift/asign")
	public ResponseEntity<?> updateShftById(@RequestBody ShiftAssignment asign) {
		try {
			return ResponseEntity.ok(service.updateShift(asign));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}
		
	}

	// fetches All employee details through their shift start date
//	@GetMapping("/shift-byStartDate/{startDate}")
	@GetMapping("/sift/asign/start-date/{startDate}")
	public ResponseEntity<?> shiftByDate(@PathVariable String startDate) {
		try {
			LocalDate localDate = LocalDate.parse(startDate);
			List<ShiftAssignment> date = service.findByStartDate(localDate);
			if (date.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(date);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	// gives emp shift details by shift start date for single emp
//	@GetMapping("/shift-byDateandId/{id}/{startDate}")
	@GetMapping("/shift/asign/date-Id/{empId}/{startDate}")
	public ResponseEntity<?> shiftByDateandempid(@PathVariable long empId, @PathVariable String startDate) {
		if (!repo.existsByEmployeeId(empId)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Please enter valid Employee Id"));
		}
		try {
			LocalDate localDate = LocalDate.parse(startDate);
			List<ShiftAssignment> date = service.findshiftById(empId, localDate);
			if (date.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(date);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}

	}

	// fetching employees on the basis of shift and shift start date
//	@GetMapping("/getShiftByName/{ShiftName}/{Date}")
	@GetMapping("/shift/asign/shiftbyname/{ShiftName}/{Date}")
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
	
	@DeleteMapping("/shift/asign/shift-allocation/cancel/{assignmentId}")
	public ResponseEntity<?>deleteShiftAssignment(@PathVariable Long assignmentId){
		try {
			service.deleteShiftAllocation(assignmentId);
			return ResponseEntity.ok().body("Shift allocation cancelled successfully");			
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(e.getMessage());		}
	}
	
	@GetMapping("/shift/asign/assignmentId/{assignmentId}")
	public ResponseEntity<?> getShiftShftByAssignmentId(@PathVariable long assignmentId) {
		try {
			ShiftAssignment shiftById = service.getShiftById(assignmentId);
			if(shiftById == null) {
				return ResponseEntity.ok("NO Record Found");
			}
			return ResponseEntity.ok(shiftById);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetch data shift" + e.getMessage()));
		}
		
	}

}