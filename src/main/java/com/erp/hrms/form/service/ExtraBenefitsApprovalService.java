package com.erp.hrms.form.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.form.ExtraBenefitsApproval;
import com.erp.hrms.exception.ExtraBenefitsApprovalException;
import com.erp.hrms.exception.ExtraBenefitsApprovalNotFoundException;
import com.erp.hrms.form.repository.IExtraBenefitsApprovalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExtraBenefitsApprovalService implements IExtraBenefitsApprovalService {

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

	@Autowired
	IExtraBenefitsApprovalRepository iExtraBenefitsApprovalRepository;

	@Override
	public void createExtraBenefitsApproval(String extraBenefitsApproval, MultipartFile supportingDocumentsName)
			throws IOException {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ExtraBenefitsApproval extraBenefitsApprovalJson = objectMapper.readValue(extraBenefitsApproval,
					ExtraBenefitsApproval.class);

			String uniqueIdentifier = UUID.randomUUID().toString();
			String originalFileName = supportingDocumentsName.getOriginalFilename();
			String fileNameWithUniqueIdentifier = uniqueIdentifier + "_" + originalFileName;

			Path fileNameAndPath = Paths.get(uplaodDirectory, fileNameWithUniqueIdentifier);
			Files.write(fileNameAndPath, supportingDocumentsName.getBytes());
			extraBenefitsApprovalJson.setSupportingDocumentsName(fileNameWithUniqueIdentifier);
			iExtraBenefitsApprovalRepository.createExtraBenefitsApproval(extraBenefitsApprovalJson);
		} catch (Exception e) {
			throw new ExtraBenefitsApprovalException(new MessageResponse("Error while creating extra benefit approval." + e));
		}
	}

	@Override
	public List<ExtraBenefitsApproval> findAllExtraBenefitsApproval() {
		List<ExtraBenefitsApproval> extraBenefitsApprovals = null;
		extraBenefitsApprovals = iExtraBenefitsApprovalRepository.findAllBenefitsApproval();
		if (extraBenefitsApprovals.isEmpty()) {
			throw new ExtraBenefitsApprovalNotFoundException(new MessageResponse("No Benefits approval."));
		}
		extraBenefitsApprovals
				.sort((e1, e2) -> Long.compare(e2.getExtraBenefitsRequestId(), e1.getExtraBenefitsRequestId()));
		return extraBenefitsApprovals;
	}

	@Override
	public ExtraBenefitsApproval getBenefitApprovalByExtraBenefitsRequestId(long extraBenefitsRequestId) {
		ExtraBenefitsApproval extraBenefitsApproval = iExtraBenefitsApprovalRepository
				.getBenefitApprovalByExtraBenefitsRequestId(extraBenefitsRequestId);
		if (extraBenefitsApproval == null) {
			throw new ExtraBenefitsApprovalNotFoundException(
					new MessageResponse("Extra benefits approval with ID: " + extraBenefitsRequestId + " not found."));
		}
		return extraBenefitsApproval;
	}

	@Override
	public List<ExtraBenefitsApproval> getBenefitApprovalByEmployeeId(long employeeId) {
		List<ExtraBenefitsApproval> benefitsApprovalsByEmployeeId= iExtraBenefitsApprovalRepository.getBenefitApprovalByEmployeeId(employeeId);;
		if(benefitsApprovalsByEmployeeId.isEmpty()) {
			throw new ExtraBenefitsApprovalNotFoundException(new MessageResponse("No extra benefits approval with ID: "+employeeId +" not found."));
		}
		benefitsApprovalsByEmployeeId.sort((e1,e2) -> Long.compare(e2.getExtraBenefitsRequestId(), e1.getExtraBenefitsRequestId()));
		return benefitsApprovalsByEmployeeId;
		
	}

}
