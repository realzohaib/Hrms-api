package com.erp.hrms.form.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.form.ExtraBenefitsApproval;

@Repository
@Transactional
public class ExtraBenefitsApprovalRepository implements IExtraBenefitsApprovalRepository {

	
	@Autowired
	EntityManager entityManager;
	
	@Override
	public void createExtraBenefitsApproval(ExtraBenefitsApproval extraBenefitsApproval) {
		entityManager.persist(extraBenefitsApproval);
		
	}

}
