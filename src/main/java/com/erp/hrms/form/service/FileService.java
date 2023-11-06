package com.erp.hrms.form.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FileService {

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

	public byte[] getFileData(String fileName) throws IOException {
		Path filePath = Paths.get(uplaodDirectory, fileName);
		if (Files.exists(filePath)) {
			return Files.readAllBytes(filePath);
		} else {
			throw new IOException("File not found: " + fileName);
		}
	}

}
