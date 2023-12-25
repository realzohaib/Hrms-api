package com.erp.hrms.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.DesignationRepository;
import com.erp.hrms.entity.Designation;
import com.erp.hrms.entity.Level;

@Service
public class DesignationServiceImpl implements IDesignationService {

	@Autowired
	private DesignationRepository designationRepository;
	
	@Override
	public void saveInitialdesignations() {
//		if (designationRepository.findAll().isEmpty()) {
//			List<Designation> initialDesignations = new ArrayList<>();
//			initialDesignations.add(new Designation("Helper" ));
//		
//			designationRepository.saveAll(initialDesignations);
//		}
	}

	@Override
	public void saveDesignation(Designation designation) {
		try {
			designationRepository.save(designation);

		} catch (Exception exception) {
			exception.printStackTrace();
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
