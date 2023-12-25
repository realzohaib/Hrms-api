package com.erp.hrms.form.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.form.ComplaintForm;
import com.erp.hrms.exception.ComplaintRequestException;
import com.erp.hrms.exception.ComplaintRequestNotFoundException;
import com.erp.hrms.exception.LeaveRequestNotFoundException;
import com.erp.hrms.form.repository.IComplaintRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ComplaintService implements IComplaintService {

	@Autowired
	IComplaintRepository iComplaintRepository;

//	This methos for send the complaint to the manager 
	@Override
	public void sendComplaintRequest(String complaintForm) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		ComplaintForm complaintFormJson = mapper.readValue(complaintForm, ComplaintForm.class);
		iComplaintRepository.sendComplaintRequest(complaintFormJson);
	}

//	This method for get the complaint request by the complaint Id
	@Override
	public ComplaintForm getComplaintRequestById(long complaintId) {
		ComplaintForm complaintForm = iComplaintRepository.getComplaintRequestById(complaintId);
		if (complaintForm == null) {
			throw new ComplaintRequestNotFoundException(
					new MessageResponse("Complaint request with ID " + complaintId + " not found"));
		}
		return complaintForm;
	}

//	This method for get the find all complaints
	@Override
	public List<ComplaintForm> findAllComplaints() {
		List<ComplaintForm> complantForms = null;
		complantForms = iComplaintRepository.findAllComplaints();
		if (complantForms.isEmpty()) {
			throw new ComplaintRequestNotFoundException(new MessageResponse("No Complaints "));
		}
		complantForms.sort((c1, c2) -> Long.compare(c2.getComplaintId(), c1.getComplaintId()));
		return complantForms;
	}

//	This method for approved or denied or pending the employee's complaint
	@Override
	public ComplaintForm complaintApprovedOrDeniedOrPending(long complaintId, String complaintForm) throws IOException {

		ComplaintForm existingComplaint = iComplaintRepository.getComplaintRequestById(complaintId);
		if (existingComplaint == null) {
			throw new ComplaintRequestNotFoundException(
					new MessageResponse("Complaint request with ID " + complaintId + " not found"));
		}
		try {

			ObjectMapper mapper = new ObjectMapper();
			ComplaintForm complaintFormJson = mapper.readValue(complaintForm, ComplaintForm.class);

			existingComplaint.setEmployeeId(complaintFormJson.getEmployeeId());
			existingComplaint.setNameOfEmployee(complaintFormJson.getNameOfEmployee());
			existingComplaint.setDepartment(complaintFormJson.getDepartment());
			existingComplaint.setContactNumber(complaintFormJson.getContactNumber());
			existingComplaint.setPost(complaintFormJson.getPost());
			existingComplaint.setEmail(complaintFormJson.getEmail());
			existingComplaint.setDateOfComplaint(complaintFormJson.getDateOfComplaint());
			existingComplaint.setComplaintRegarding(complaintFormJson.getComplaintRegarding());
			existingComplaint.setAccused(complaintFormJson.getAccused());
			existingComplaint.setDateOfIncident(complaintFormJson.getDateOfIncident());
			existingComplaint.setTimeOfIncident(complaintFormJson.getTimeOfIncident());
			existingComplaint.setLocationOfIncident(complaintFormJson.getLocationOfIncident());
			existingComplaint.setDescribeTheIncident(complaintFormJson.getDescribeTheIncident());
			existingComplaint.setWitnessesOfIncident(complaintFormJson.getWitnessesOfIncident());
			existingComplaint.setNumbersOfComplaints(complaintFormJson.getNumbersOfComplaints());
			existingComplaint.setApprovedOrDeniedOrPending(complaintFormJson.getApprovedOrDeniedOrPending());
			existingComplaint.setRemark(complaintFormJson.getRemark());

			return iComplaintRepository.complaintApprovedOrDeniedOrPending(complaintId, complaintFormJson);
		} catch (Exception e) {
			throw new ComplaintRequestException("Error while approving the complaint form ");
		}
	}

//	This method for find all the Leave Request by employeeId
	@Override
	public List<ComplaintForm> getComplaintRequestByEmployeeId(long employeeId) {
		List<ComplaintForm> complaintForms = iComplaintRepository.getComplaintRequestByEmployeeId(employeeId);
		if (complaintForms.isEmpty()) {
			throw new LeaveRequestNotFoundException(
					new MessageResponse("This employee ID " + employeeId + " not found."));
		}
		complaintForms.sort((c1, c2) -> Long.compare(c2.getComplaintId(), c1.getComplaintId()));
		return complaintForms;
	}

}

