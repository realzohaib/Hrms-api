package com.erp.hrms.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.DesignationRepository;
import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.entity.Designation;
import com.erp.hrms.entity.PersonalInfo;

@Service
public class DesignationServiceImpl implements IDesignationService {

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private IPersonalInfoDAO dao;

	@Override
	public void saveDesignation(Designation designation) {
		try {
			// Find existing designation with same employeeId
			List<Designation> existingDesignations = designationRepository
					.findDesignationWithMaxdIdByEmployeeId(designation.getEmployeeId());
			if (!existingDesignations.isEmpty()) {
				Designation existingDesignation = existingDesignations.get(0);

				LocalDate newStartDate = LocalDate.parse(designation.getStartDate());
				LocalDate newEndDate = newStartDate.minusDays(1);

				existingDesignation.setEndDate(newEndDate.toString());
				designationRepository.save(existingDesignation);
			}

			PersonalInfo personalInfo = dao.getPersonalInfoByEmployeeId(designation.getEmployeeId());
			designation.setPersonalinfo(personalInfo);

			designation.setEndDate(null);
			designationRepository.save(designation);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	@Override
	public Designation findByDesignationId(Long did) {

		return designationRepository.findBydId(did);
	}

	@Override
	public List<Designation> findAllDesignation() {

		return designationRepository.findAll();
	}

}
