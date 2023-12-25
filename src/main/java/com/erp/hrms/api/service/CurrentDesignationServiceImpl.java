package com.erp.hrms.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.CurrentDesignationRepository;
import com.erp.hrms.entity.CurrentDesignation;

@Service
public class CurrentDesignationServiceImpl implements ICurrentDesignationService {

	@Autowired
	private CurrentDesignationRepository currentDesignationRepository;

	@Override
	public void saveCurrentDesignation(CurrentDesignation currentDesignation) {
		try {
			currentDesignationRepository.save(currentDesignation);

		} catch (Exception exception) {
			exception.getMessage();
		}
	}

	@Override
	public CurrentDesignation findByCurrentDesignationId(Long cdid) {
		return currentDesignationRepository.findBycdId(cdid);
	}

	@Override
	public List<CurrentDesignation> findAllCurrentDesignation() {
		return currentDesignationRepository.findAll();
	}

}
