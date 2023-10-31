package com.erp.hrms.form.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.exception.LeaveRequestNotFoundException;

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
	public LeaveApproval getleaveRequestById(Long leaveRequestId) {

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
	public List<LeaveApproval> getLeaveRequestByEmployeeId(Long employeeId) {
		List<LeaveApproval> findAllEmployeeId = null;
		try {
			TypedQuery<LeaveApproval> query = entityManager
					.createQuery("SELECT l FROM LeaveApproval l WHERE l.employeeId = :employeeId", LeaveApproval.class);
			query.setParameter("employeeId", employeeId);
			findAllEmployeeId = query.getResultList();
			return findAllEmployeeId;
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
	public LeaveApproval approvedByManager(Long leaveRequestId, LeaveApproval leaveApproval) {
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

	@Override
	public List<LeaveApproval> findAllLeaveApprovalAccepted() {
		List<LeaveApproval> leaveApprovals = null;
		try {
			leaveApprovals = entityManager
					.createQuery("SELECT l FROM LeaveApproval l WHERE l.approvalStatus = 'Accepted'",
							LeaveApproval.class)
					.getResultList();

			return leaveApprovals;
		} catch (NoResultException e) {
			return null;
		}

	}

	@Override
	public List<LeaveApproval> findAllLeaveApprovalRejected() {

		List<LeaveApproval> leaveApprovals = null;
		try {
			leaveApprovals = entityManager
					.createQuery("SELECT l FROM LeaveApproval l WHERE l.approvalStatus = 'Rejected'",
							LeaveApproval.class)
					.getResultList();

			return leaveApprovals;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployee(Long employeeId) {
		String sql = "SELECT SUM(numberOfDaysRequested) FROM LeaveApproval WHERE employeeId = :employeeId";
//		String sql = "SELECT COUNT(numberOfDaysRequested) FROM LeaveApproval WHERE employeeId = :employeeId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("employeeId", employeeId);

		List<?> result = query.getResultList();
		if (result != null && !result.isEmpty()) {
			Object obj = result.get(0);
			if (obj instanceof Number) {
				return new BigDecimal(((Number) obj).doubleValue());
			}
		}
		throw new LeaveRequestNotFoundException(new MessageResponse("No data available now"));
	}

	@Override
	public BigDecimal calculateTotalSpecificNumberOfDaysRequestedByEmployee(Long employeeId, String leaveName) {
		String sql = "SELECT SUM(la.numberOfDaysRequested) FROM LeaveApproval la " + "JOIN la.leaveType lt "
				+ "WHERE la.employeeId = :employeeId AND lt.leaveName = :leaveName";
		Query query = entityManager.createQuery(sql);
		query.setParameter("employeeId", employeeId);
		query.setParameter("leaveName", leaveName);

		List<?> result = query.getResultList();
		if (result != null && !result.isEmpty()) {
			Object obj = result.get(0);
			if (obj instanceof Number) {
				return new BigDecimal(((Number) obj).doubleValue());
			}
		}
		throw new LeaveRequestNotFoundException(new MessageResponse("No data available now"));
	}

}