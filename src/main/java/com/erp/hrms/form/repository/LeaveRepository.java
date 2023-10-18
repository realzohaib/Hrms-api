package com.erp.hrms.form.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.form.LeaveApproval;

@Repository
@Transactional
public class LeaveRepository implements ILeaveRepository {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public void createLeaveApproval(LeaveApproval leaveApproval) {
		entityManager.persist(leaveApproval);
	}

	@Override
	public LeaveApproval getleaveRequestById(long leaveRequestId) {
		Query query = entityManager
				.createQuery("SELECT l FROM LeaveApproval l WHERE l.leaveRequestId = :leaveRequestId");
		query.setParameter("leaveRequestId", leaveRequestId);
		try {
			return (LeaveApproval) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<LeaveApproval> getLeaveRequestByEmployeeId(long employeeId) {
		List<LeaveApproval> findAllRequestById = null;
		try {
			TypedQuery<LeaveApproval> query = entityManager
					.createQuery("SELECT l FROM LeaveApproval l WHERE l.employeeId = :employeeId", LeaveApproval.class);
			query.setParameter("employeeId", employeeId);
			findAllRequestById = query.getResultList();
			return findAllRequestById;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<LeaveApproval> findAllLeaveApproval() {
		List<LeaveApproval> leaveApprovals = null;
		try {
			leaveApprovals = entityManager.createQuery("SELECT l FROM LeaveApproval l ", LeaveApproval.class)
					.getResultList();
			return leaveApprovals;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public LeaveApproval approvedByManager(long leaveRequestId, LeaveApproval leaveApproval) {
		try {
			leaveApproval.setLeaveRequestId(leaveRequestId);
			entityManager.merge(leaveApproval);
			return leaveApproval;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<LeaveApproval> findAllLeaveApprovalPending() {
		List<LeaveApproval> leaveApprovals = null;
		try {
			leaveApprovals = entityManager
					.createQuery("SELECT l FROM LeaveApproval l WHERE l.approvalStatus = 'Pending'",
							LeaveApproval.class)
					.getResultList();

			return leaveApprovals;
		} catch (NoResultException e) {
			return null;
		}
	}

}