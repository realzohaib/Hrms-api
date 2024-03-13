package com.erp.hrms.form.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.approver.entity.LeaveApprover;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.entity.form.LeaveCalendarData;
import com.erp.hrms.entity.form.LeaveCountDTO;
import com.erp.hrms.entity.form.LeaveDataDTO;
import com.erp.hrms.entity.form.MarkedDate;
import com.erp.hrms.exception.LeaveRequestNotFoundException;
import com.erp.hrms.exception.PersonalInfoNotFoundException;
import com.erp.hrms.form.service.ILeaveService;

@RestController
@RequestMapping("/api/v1")
public class LeaveController {

	@Autowired
	private ILeaveService iLeaveService;

//	This method for send the leave request to manager
	@PostMapping("/leave/requests")
	public ResponseEntity<?> createLeaveApproval(@RequestParam("leaveApproval") String leaveApproval,
			@RequestParam(value = "medicalDocumentsName", required = false) MultipartFile medicalDocumentsName)
			throws IOException {
		try {
			LeaveApprover approval = iLeaveService.createLeaveApproval(leaveApproval, medicalDocumentsName);

			return ResponseEntity.ok(approval);
		} catch (Exception e) {
			return new ResponseEntity<>((new MessageResponse("Error while creating leave approval. " + e)),
					HttpStatus.BAD_REQUEST);
		}
	}

//	This method for get the leave request by LeaveRequestId
	@GetMapping("/leave/requests/{leaveRequestId}")
	public ResponseEntity<?> getleaveRequestById(@PathVariable Long leaveRequestId) throws IOException {
		try {
			LeaveApproval getleaveRequestById = iLeaveService.getleaveRequestById(leaveRequestId);
			return ResponseEntity.ok(getleaveRequestById);
		} catch (LeaveRequestNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse("Error occurred: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error occurred: " + e.getMessage()));
		}
	}

//	This method for find all the Leave Request by employeeId
	@GetMapping("/leave/requests/employee/{employeeId}")
	public ResponseEntity<?> getLeaveRequestByEmployeeId(@PathVariable Long employeeId) throws IOException {
		try {
			return new ResponseEntity<>(iLeaveService.getLeaveRequestByEmployeeId(employeeId), HttpStatus.OK);
		}

		catch (LeaveRequestNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new MessageResponse("Error occurred: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("Error occurred: " + e.getMessage()));
		}
	}

//	This method for find all leave request
	@GetMapping("/leave/requests/all")
	public ResponseEntity<?> findAllLeaveApproval() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApproval(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No Leave request "), HttpStatus.NOT_FOUND);
		}
	}

//	This method for update the leave request by the manager Accepted or Rejected with the help of leaveRequestId
	@PutMapping("/leave/requests/approved/manager/{leaveRequestId}")
	public ResponseEntity<?> approvedByManager(@PathVariable Long leaveRequestId,
			@RequestParam("leaveApproval") String leaveApproval,
			@RequestParam(value = "medicalDocumentsName", required = false) MultipartFile medicalDocumentsName)
			throws IOException {
		try {
			iLeaveService.approvedByManager(leaveRequestId, leaveApproval, medicalDocumentsName);
			return new ResponseEntity<>(new MessageResponse("Done."), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Error while approving leave request. " + e),
					HttpStatus.BAD_REQUEST);
		}

	}

//	This method for update the leave request by the hr Accepted or Rejected with the help of leaveRequestId
	@PutMapping("/leave/requests/approved/hr/{leaveRequestId}")
	public ResponseEntity<?> approvedOrDenyByHR(@PathVariable Long leaveRequestId,
			@RequestParam("leaveApproval") String leaveApproval,
			@RequestParam(value = "medicalDocumentsName", required = false) MultipartFile medicalDocumentsName)
			throws IOException {
		try {
			iLeaveService.approvedOrDenyByHR(leaveRequestId, leaveApproval, medicalDocumentsName);
			return new ResponseEntity<>(new MessageResponse("Done."), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Error while approving leave request. " + e),
					HttpStatus.BAD_REQUEST);
		}
	}

//	This method for find all pending request
	@GetMapping("/leave/requests/pending")
	public ResponseEntity<?> findAllLeaveApprovalPending() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApprovalPending(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No leave request pending"), HttpStatus.NOT_FOUND);
		}
//		return ResponseEntity.ok();
	}

//	This method for find all accepted request
	@GetMapping("/leave/requests/accepted")
	public ResponseEntity<?> findAllLeaveApprovalAccepted() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApprovalAccepted(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No leave request accepted "), HttpStatus.NOT_FOUND);
		}
	}

//	This method for find all rejected request
	@GetMapping("/leave/requests/rejected")
	public ResponseEntity<?> findAllLeaveApprovalRejected() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApprovalRejected(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No leave request rejected "), HttpStatus.NOT_FOUND);
		}
	}

//	This method is to calculate how many employees are on leave in a day.
	@GetMapping("/leave-calendar")
	public ResponseEntity<List<LeaveCalendarData>> getLeaveCalendar() {
		List<LeaveApproval> leaveApprovals = iLeaveService.getAllLeaveApprovalsAccepted();
		List<LeaveCalendarData> calendarData = iLeaveService.generateLeaveCalendar(leaveApprovals);
		return new ResponseEntity<>(calendarData, HttpStatus.OK);
	}

//	This method is to mark the date on which more than 20% leave occurred and or will occur on the following day
	@GetMapping("/marked-calendar-dates")
	public ResponseEntity<?> getMarkedCalendarDates() {
		try {
			List<MarkedDate> markedDates = iLeaveService.markCalendarDates();
			return new ResponseEntity<>(markedDates, HttpStatus.OK);
		} catch (Exception e) {

			return new ResponseEntity<>(new MessageResponse("An error occurred while fetching marked calendar dates."),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/calculate-total-days/{employeeId}/{year}/{month}")
	public ResponseEntity<BigDecimal> calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(
			@PathVariable Long employeeId, @PathVariable int year, @PathVariable int month) {
		BigDecimal totalDays = iLeaveService.calculateTotalNoOfLeavesApprovedByEmployeeInMonthAndStatus(employeeId,
				year, month);
		return ResponseEntity.ok(totalDays);
	}

//	This method the total leaves in a year of particular employee
	@GetMapping("/total/leaves/in-year/{employeeId}/{year}/{countryName}")
	public List<LeaveCountDTO> getTotalLeavesByYear(@PathVariable int year, @PathVariable Long employeeId,
			@PathVariable String countryName) {
		return iLeaveService.getAllLeavesByEmployeeIdAndYear(employeeId, year, countryName);
	}

//	This method give the total leaves in a month of particular employee
	@GetMapping("/total/leaves/in-month/{employeeId}/{year}/{month}/{countryName}")
	public List<LeaveCountDTO> getTotalLeavesByInMonth(@PathVariable int year, @PathVariable int month,
			@PathVariable Long employeeId, @PathVariable String countryName) {
		return iLeaveService.getAllLeaveByMonthByEmployeeId(year, month, employeeId, countryName);
	}

//	This method give the total count of employee on leave on a particular date and also give the list of employee
	@GetMapping("/leave-by-date/{date}")
	public ResponseEntity<LeaveDataDTO> getLeaveDataByDate(@PathVariable String date) {
		try {
			LeaveDataDTO leaveData = iLeaveService.getLeaveDataByDate(date);
			return new ResponseEntity<>(leaveData, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
