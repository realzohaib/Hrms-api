package com.erp.hrms.attendence.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.attendence.entity.Attendence;
import com.erp.hrms.attendence.repo.IAttendencerepo;
import com.erp.hrms.attendence.service.AttendenceRequest;
import com.erp.hrms.attendence.service.AttendenceResponse;
import com.erp.hrms.attendence.service.AttendenceServiceImpl;
import com.erp.hrms.attendence.service.AttendencenotRegistered;

@RestController
//@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AttendenceController {

	@Autowired
	private AttendenceServiceImpl service;

	@Autowired
	private IAttendencerepo repo;

	// this method will need employee id
	@PostMapping("/punch-In-Time")
	public ResponseEntity<?> punchInTime(@RequestBody Attendence attendence) throws AttendencenotRegistered {

		if (repo.existsByEmployeeIdAndDate(attendence.getEmployeeId(), attendence.getDate())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Employee Already punched -in"));
		}
		try {
			Attendence punchIn = service.punchIn(attendence);
			return ResponseEntity.ok(punchIn);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}

	}

	// needs Employee Id
	@GetMapping("/get-attendence")
	public ResponseEntity<?> getAttendenceById(@RequestBody AttendenceRequest req) throws AttendencenotRegistered {
		try {
			List<Attendence> list = service.getEmployeeAttendence(req.getId());
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}

	}

	// needs AttendenceId
	@PostMapping("/punch-out")
	public ResponseEntity<?> punchOut(@RequestBody AttendenceRequest req) throws AttendencenotRegistered {
		try {
			Attendence punchout = service.punchout(req.getId());
			return ResponseEntity.ok(punchout);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Something went Wrong..!"));
		}

	}

	// needs AttendenceId
	@PostMapping("/break-start")
	public ResponseEntity<?> breakStart(@RequestBody AttendenceRequest req) throws AttendencenotRegistered {
		try {
			Attendence breakStart = service.breakStart(req.getId());
			return ResponseEntity.ok(breakStart);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}

	}

	// needs Attendence Id
	@PostMapping("/break-end")
	public ResponseEntity<?> breakEnd(@RequestBody AttendenceRequest req) throws AttendencenotRegistered {
		try {
			Attendence breakStart = service.breakEnd(req.getId());
			return ResponseEntity.ok(breakStart);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);
		}

	}

	// needs Employee Id
	@GetMapping("/get-attendence-byDate")
	public ResponseEntity<?> AttendenceByDate(@RequestBody AttendenceRequest req) {
		System.out.println("hello");
		try {
			List<Attendence> list = service.getAttendenceByDate(req.getId(), req.getStartDate(), req.getEndODate());
			if (list.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("invalid Data"));
		}
	}

	// needs Employee Id
	@GetMapping("/get-attendence-byMonth/{id}/{year}/{month}")
	public ResponseEntity<?> AttendenceByMonth(@PathVariable long id, @PathVariable int year, @PathVariable int month) {
		try {
			AttendenceResponse fullAttendence = service.fullAttendence(id, year, month);
			return ResponseEntity.ok(fullAttendence);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("invalid Data"));

		}
	}

	@GetMapping("/get-OvertimeRequest")
	public ResponseEntity<?> getEmployeeWithOverTimeStatusPending() {
		try {
			List<Attendence> list = service.getEmployeeWithOverTimeStatusPending();
			if (list.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);

		}
	}

	// need AttendenceId
	@GetMapping("/approve-OvertimeRequest/{id}")
	public ResponseEntity<?> ApproveOverTime(@PathVariable long id) {
		try {
			service.approveOverTime(id);
			return ResponseEntity.ok(new MessageResponse("Overtime Approved"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);

		}
	}

	// need AttendenceId
	@GetMapping("/deny-OvertimeRequest/{id}")
	public ResponseEntity<?> DenyeOverTime(@PathVariable long id) {
		try {
			service.denyOverTime(id);
			return ResponseEntity.ok(new MessageResponse("Overtime Denied"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e);

		}
	}

	// need AttendenceId from Attendence Object
	@PostMapping("/update-overtime")
	public ResponseEntity<?> updateOvertime(@RequestBody Attendence attendence) {
		try {
			Attendence updateOverTime = service.updateOverTime(attendence);
			return ResponseEntity.ok(updateOverTime);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
		}

	}
	
	@GetMapping("/checkAttendance")
	public ResponseEntity<Boolean> checkAttendance(
	        @RequestParam Long employeeId,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

	    boolean attendanceExists = repo.existsByEmployeeIdAndDate(employeeId, date);

	    return ResponseEntity.ok(attendanceExists);
	}


}
