package com.erp.hrms.api.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.VisaDetail;
import com.erp.hrms.exception.PersonalInfoNotFoundException;

@Repository
@Transactional
public class PersonalInfoDAO implements IPersonalInfoDAO {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public boolean existsByEmail(String email) {
		String query = "SELECT COUNT(p) FROM PersonalInfo p WHERE p.email = :email";
		Long count = entityManager.createQuery(query, Long.class).setParameter("email", email).getSingleResult();
		return count > 0;
	}

	@Override
	public void savePersonalInfo(PersonalInfo personalInfo) {
		try {
			entityManager.persist(personalInfo);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public List<PersonalInfo> findAllPersonalInfo() {
		List<PersonalInfo> resultList = null;
		try {
			resultList = entityManager.createQuery("Select p from PersonalInfo p ", PersonalInfo.class).getResultList();
		} catch (Exception e) {
			System.out.println("Error occured: " + e);
		}
		return resultList;
	}

	@Override
	public PersonalInfo getPersonalInfoByEmail(String email) {
		try {
			Query query = entityManager.createQuery("SELECT p FROM PersonalInfo p WHERE p.email = :email");
			query.setParameter("email", email);
			return (PersonalInfo) query.getSingleResult();
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(
					new MessageResponse("No personal information found for this email ID: " + email));
		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmailForUpdate(String email) {
		try {
			Query query = entityManager
					.createQuery("SELECT p FROM PersonalInfo p WHERE p.email = :email and p.status = 'Active'");
			query.setParameter("email", email);
			return (PersonalInfo) query.getSingleResult();
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"No personal information found for this email ID: " + email + " or this eamil is inactived"));
		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmployeeId(Long employeeId) {
		try {
			Query query = entityManager.createQuery("SELECT p FROM PersonalInfo p WHERE p.employeeId = :employeeId");
			query.setParameter("employeeId", employeeId);
			return (PersonalInfo) query.getSingleResult();
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(
					new MessageResponse("No personal information found for employee ID: " + employeeId));
		}
	}

	@Override
	public PersonalInfo deletePersonalInfoByEmail(String email, PersonalInfo personalInfo) {
		try {
			personalInfo.setEmail(email);
			entityManager.merge(personalInfo);
			return personalInfo;
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException(
					"No personal information found for this email ID: " + email + " or this eamil is inactived", e);
		}
	}

	@Override
	public PersonalInfo updatePersonalInfo(String email, PersonalInfo personalInfo) {
		try {
			personalInfo.setEmail(email);
			entityManager.merge(personalInfo);
			return personalInfo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean existByID(long id) {
		String query = "SELECT COUNT(p) FROM PersonalInfo p WHERE p.id = :id";
		Long count = entityManager.createQuery(query, Long.class).setParameter("id", id).getSingleResult();
		return count == 0;
	}

	@Override
	@Transactional
	public void updateFirstVisaEmail(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.firstVisaEmailSend = true WHERE p.email = :email");
			query.setParameter("email", email);
			int rowsUpdated = query.executeUpdate();

			if (rowsUpdated == 0) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"An error occurred while updating personal information for this email ID: " + email));
		}
	}

	@Override
	@Transactional
	public void updateSecondVisaEmail(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.secondVisaEmailSend = true WHERE p.email = :email");
			query.setParameter("email", email);
			int rowsUpdated = query.executeUpdate();

			if (rowsUpdated == 0) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"An error occurred while updating personal information for this email ID: " + email));
		}
	}

	@Override
	@Transactional
	public void updatefirstContinuouslyVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.firstContinuouslyVisaEmailSend = true WHERE p.email = :email");
			query.setParameter("email", email);
			int rowsUpdated = query.executeUpdate();

			if (rowsUpdated == 0) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"An error occurred while updating personal information for this email ID: " + email));
		}
	}

	@Override
	@Transactional
	public void updateSecondContinuouslyVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.secondContinuouslyVisaEmailSend = true WHERE p.email = :email");
			query.setParameter("email", email);
			int rowsUpdated = query.executeUpdate();

			if (rowsUpdated == 0) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"An error occurred while updating personal information for this email ID: " + email));
		}
	}

	@Override
	@Transactional
	public void updateThirdContinuouslyVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.thirdContinuouslyVisaEmailSend = true WHERE p.email = :email");
			query.setParameter("email", email);
			int rowsUpdated = query.executeUpdate();

			if (rowsUpdated == 0) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"An error occurred while updating personal information for this email ID: " + email));
		}
	}

	@Override
	@Transactional
	public void updatefourContinuouslyVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.fourContinuouslyVisaEmailSend = true WHERE p.email = :email");
			query.setParameter("email", email);
			int rowsUpdated = query.executeUpdate();

			if (rowsUpdated == 0) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}
		} catch (Exception ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"An error occurred while updating personal information for this email ID: " + email));
		}
	}

	@Override
	public PersonalInfo updateVisaDetails(Long employeeId, String visaIssueDate, String visaExpiryDate) {

		PersonalInfo personalInfo = new PersonalInfo();
		personalInfo = entityManager.find(PersonalInfo.class, employeeId);
		VisaDetail visaDetail = personalInfo.getVisainfo();

		if (visaDetail != null) {
			visaDetail.setVisaIssueyDate(visaIssueDate);
			visaDetail.setVisaExpiryDate(visaExpiryDate);

			visaDetail.setFirstVisaEmailSend(false);
			visaDetail.setSecondVisaEmailSend(false);
			visaDetail.setFirstContinuouslyVisaEmailSend(false);
			visaDetail.setSecondContinuouslyVisaEmailSend(false);
			visaDetail.setThirdContinuouslyVisaEmailSend(false);
			visaDetail.setFourContinuouslyVisaEmailSend(false);

			return entityManager.merge(personalInfo);
		}

		return null;
	}
}
