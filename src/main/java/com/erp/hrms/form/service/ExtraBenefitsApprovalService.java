package com.erp.hrms.form.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.entity.form.ExtraBenefitsApproval;
import com.erp.hrms.form.repository.IExtraBenefitsApprovalRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExtraBenefitsApprovalService implements IExtraBenefitsApprovalService {

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

	@Autowired
	IExtraBenefitsApprovalRepository iExtraBenefitsApprovalRepository;

	@Override
	public void createExtraBenefitsApproval(String extraBenefitsApproval, String supportingDocuments)
			throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		ExtraBenefitsApproval extraBenefitsApprovalJson = objectMapper.readValue(extraBenefitsApproval,
				ExtraBenefitsApproval.class);
		Path fileNameAndPath = Paths.get(uplaodDirectory, supportingDocuments);
		Files.write(fileNameAndPath, supportingDocuments.getBytes());
		extraBenefitsApprovalJson.setSupportingDocumentsName(supportingDocuments);
		iExtraBenefitsApprovalRepository.createExtraBenefitsApproval(extraBenefitsApprovalJson);

	}

}
