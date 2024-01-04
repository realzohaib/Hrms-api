package com.erp.hrms.form.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

	@Override
	public List<ExtraBenefitsApproval> findAllBenefitsApproval() {
		List<ExtraBenefitsApproval> extraBenefitsApprovals = null;
		try {
			extraBenefitsApprovals = entityManager
					.createQuery("SELECT e FROM ExtraBenefitsApproval e ", ExtraBenefitsApproval.class).getResultList();
			return extraBenefitsApprovals;

		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public ExtraBenefitsApproval getBenefitApprovalByExtraBenefitsRequestId(long extraBenefitsRequestId) {
		Query query = entityManager.createQuery(
				"SELECT e FROM ExtraBenefitsApproval e WHERE e.extraBenefitsRequestId = :extraBenefitsRequestId");
		query.setParameter("extraBenefitsRequestId", extraBenefitsRequestId);
		try {
			return (ExtraBenefitsApproval) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<ExtraBenefitsApproval> getBenefitApprovalByEmployeeId(long employeeId) {

		List<ExtraBenefitsApproval> extraBenefitsApprovalsByEmployeeId=null;
		try {
			TypedQuery<ExtraBenefitsApproval> query=entityManager
					.createQuery("SELECT e FROM ExtraBenefitsApproval e WHERE e.employeeId = :employeeId", ExtraBenefitsApproval.class);
			query.setParameter("employeeId", employeeId);
			extraBenefitsApprovalsByEmployeeId = query.getResultList();
			return extraBenefitsApprovalsByEmployeeId;
		}
		catch(NoResultException e) {
		return null;
		}
	}

}
