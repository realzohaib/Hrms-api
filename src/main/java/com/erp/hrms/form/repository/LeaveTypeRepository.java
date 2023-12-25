package com.erp.hrms.form.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.form.LeaveType;

@Repository
@Transactional
public class LeaveTypeRepository implements ILeaveTypeRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void predefinedLeaveType(List<LeaveType> leaveTypes) {
		for (LeaveType leaveType : leaveTypes) {
			entityManager.persist(leaveType);
		}
	}

	@Override
	public void createLeaveType(LeaveType leaveType) {
		entityManager.persist(leaveType);
	}

	@Override
	public List<LeaveType> findAllLeaveType() {
		List<LeaveType> leaveTypes = null;
		try {
			leaveTypes = entityManager.createQuery("SELECT ly FROM LeaveType ly ", LeaveType.class).getResultList();
			return leaveTypes;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public LeaveType findByLeaveTypeId(Long leaveTypeId) {
		Query query = entityManager.createQuery("SELECT ly FROM LeaveType ly WHERE ly.leaveTypeId = :leaveTypeId");
		query.setParameter("leaveTypeId", leaveTypeId);
		try {
			return (LeaveType) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public LeaveType findByLeaveName(String leaveName) {
		Query query = entityManager.createQuery("SELECT lt FROM LeaveType lt WHERE lt.leaveName = :leaveName");
		query.setParameter("leaveName", leaveName);

		try {
			return (LeaveType) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
