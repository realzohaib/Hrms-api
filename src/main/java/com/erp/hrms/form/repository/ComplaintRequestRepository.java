package com.erp.hrms.form.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.form.ComplaintForm;

@Repository
@Transactional
public class ComplaintRequestRepository implements IComplaintRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void sendComplaintRequest(ComplaintForm complaintForm) {
		entityManager.persist(complaintForm);
	}

	@Override
	public ComplaintForm getComplaintRequestById(long complaintId) {
		Query query = entityManager.createQuery("SELECT c FROM ComplaintForm c WHERE c.complaintId = :complaintId");
		query.setParameter("complaintId", complaintId);
		try {
			return (ComplaintForm) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<ComplaintForm> findAllComplaints() {

		List<ComplaintForm> complaintForms = null;
		try {
			complaintForms = entityManager.createQuery("SELECT c FROM ComplaintForm c", ComplaintForm.class)
					.getResultList();
			return complaintForms;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public ComplaintForm complaintApprovedOrDeniedOrPending(long complaineId, ComplaintForm complaintForm) {
		try {
			complaintForm.setComplaintId(complaineId);
			entityManager.merge(complaintForm);
			return complaintForm;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<ComplaintForm> getComplaintRequestByEmployeeId(long employeeId) {
		List<ComplaintForm> findAllEmployeeId = null;
		try {
			TypedQuery<ComplaintForm> query = entityManager
					.createQuery("SELECT c FROM ComplaintForm c WHERE c.employeeId = :employeeId", ComplaintForm.class);
			query.setParameter("employeeId", employeeId);
			findAllEmployeeId = query.getResultList();
			return findAllEmployeeId;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
