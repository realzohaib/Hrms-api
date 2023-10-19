package com.erp.hrms.form.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.entity.form.ExtraBenefitsApproval;
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

	}

}
