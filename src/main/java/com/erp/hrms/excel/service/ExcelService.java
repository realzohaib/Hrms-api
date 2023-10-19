package com.erp.hrms.excel.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.excel.IPersonalExcelrepo;
import com.erp.hrms.excel.helper.Helper;

@Service
public class ExcelService {
	@Autowired
	private IPersonalExcelrepo repo;

	public void savefile(MultipartFile file) throws IOException {
		List<PersonalInfo> list = Helper.excelpersonalinfolist(file.getInputStream());
		repo.saveAll(list);
	}
}
