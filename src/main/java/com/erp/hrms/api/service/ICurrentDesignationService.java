package com.erp.hrms.api.service;

import java.util.List;

import com.erp.hrms.entity.CurrentDesignation;

public interface ICurrentDesignationService {

	public void saveCurrentDesignation(CurrentDesignation currentDesignation);

	public CurrentDesignation findByCurrentDesignationId(Long cdid);

	public List<CurrentDesignation> findAllCurrentDesignation();
}
