package com.erp.hrms.api.service;

import java.util.List;

import com.erp.hrms.entity.Designation;

public interface IDesignationService {

	public void saveDesignation(Designation designation);

	public Designation findByDesignationId(Long did);

	public List<Designation> findAllDesignation();

	public void saveInitialdesignations();
}
