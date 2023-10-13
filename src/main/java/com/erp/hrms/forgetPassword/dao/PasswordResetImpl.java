package com.erp.hrms.forgetPassword.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.erp.hrms.forgetPassword.entity.PasswordReset;

@Repository
@Transactional
public class PasswordResetImpl implements IPasswordReset {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public boolean existsByEmail(String email) {
		String query = "SELECT COUNT(p) FROM UserEntity p WHERE p.email = :email";
		Long count = entityManager.createQuery(query, Long.class).setParameter("email", email).getSingleResult();
		return count > 0;
	}

	@Override
	public void save(PasswordReset passwordReset) {
		entityManager.persist(passwordReset);
	}

	@Override
	public PasswordReset findByEmailAndOtp(String email, int otp) {
		String query = "SELECT p FROM PasswordReset p WHERE p.email = :email AND p.otp = :otp";
		TypedQuery<PasswordReset> typedQuery = entityManager.createQuery(query, PasswordReset.class)
				.setParameter("email", email).setParameter("otp", otp);
		try {
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public PasswordReset getNewpassword(String email) {
		String query = "SELECT p FROM PasswordReset p WHERE p.email = :email";
		PasswordReset result = entityManager.createQuery(query, PasswordReset.class).setParameter("email", email)
				.getSingleResult();
		return result;
	}

}
