package com.erp.hrms.api.service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class PersonalInfoFileService {

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/PersonalInfo/images";

	public byte[] getFileData(String fileName) throws IOException {
		Path filePath = Paths.get(uplaodDirectory, fileName);
		if (Files.exists(filePath)) {
			return Files.readAllBytes(filePath);
		} else {
			throw new IOException("File not found: " + fileName);
		}
	}
}
