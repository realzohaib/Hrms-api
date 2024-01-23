package com.erp.hrms.api.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.VisaDetail;
import com.erp.hrms.entity.notificationhelper.NotificationHelper;
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
	public boolean existsByPersonalContactNo(String personalContactNo) {
		String query = "SELECT COUNT(p) FROM PersonalInfo p WHERE p.personalContactNo = :personalContactNo";
		Long count = entityManager.createQuery(query, Long.class).setParameter("personalContactNo", personalContactNo)
				.getSingleResult();
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
		try {
			List<PersonalInfo> resultList = entityManager
					.createQuery("Select p from PersonalInfo p ", PersonalInfo.class).getResultList();
			return resultList;
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve personal info: " + e.getMessage());
		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmail(String email) {
		try {
			Query query = entityManager.createQuery("SELECT p FROM PersonalInfo p WHERE p.email = :email");
			query.setParameter("email", email);
			return (PersonalInfo) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new PersonalInfoNotFoundException(
					new MessageResponse("No personal information found for this email ID: " + email));
		} catch (Exception ex) {
			throw new RuntimeException("An error occurred while retrieving personal information for email: " + email,
					ex);
		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmailForUpdate(String email) {
		try {
			Query query = entityManager
					.createQuery("SELECT p FROM PersonalInfo p WHERE p.email = :email and p.status = 'Active'");
			query.setParameter("email", email);
			return (PersonalInfo) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new PersonalInfoNotFoundException(new MessageResponse(
					"No personal information found for this email ID: " + email + " or this eamil is inactived."));
		} catch (Exception ex) {
			throw new RuntimeException("An error occurred while retrieving personal information for email: " + email,
					ex);
		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmployeeId(Long employeeId) {
		try {
			Query query = entityManager.createQuery("SELECT p FROM PersonalInfo p WHERE p.employeeId = :employeeId");
			query.setParameter("employeeId", employeeId);
			return (PersonalInfo) query.getSingleResult();
		} catch (NoResultException ex) {
			throw new PersonalInfoNotFoundException(
					new MessageResponse("No personal information found for employee ID: " + employeeId + " or " + ex));
		} catch (Exception ex) {
			throw new RuntimeException(
					"An error occurred while retrieving personal information for employee ID: " + employeeId, ex);
		}
	}

	@Override
	public PersonalInfo deletePersonalInfoByEmail(String email, PersonalInfo personalInfo) {
		try {
			personalInfo.setEmail(email);
			entityManager.merge(personalInfo);
			return personalInfo;
		} catch (Exception e) {
			throw new RuntimeException(
					"No personal information found for this email ID: " + email + " or this eamil is inactived", e);
		}
	}

	@Override
	@Transactional
	public PersonalInfo updatePersonalInfo(String email, PersonalInfo personalInfo) {
		try {
			personalInfo.setEmail(email);
			entityManager.merge(personalInfo);
//			entityManager.persist(personalInfo);
			return personalInfo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean existByID(long employeeId) {
		String query = "SELECT COUNT(p) FROM PersonalInfo p WHERE p.employeeId = :employeeId";
		Long count = entityManager.createQuery(query, Long.class).setParameter("employeeId", employeeId)
				.getSingleResult();
		return count == 0;
	}

	@Override
	@Transactional
	public void update20and60daysBeforeVisaEmail(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.visaEmailSend20and60daysBefore = true WHERE p.email = :email");
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

	public void update10and30daysBeforeVisaEmail(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.VisaEmailSend10and30daysBefore = true WHERE p.email = :email");
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

	public void update4daysBeforeVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.visaEmailSend4daysBefore = true WHERE p.email = :email");
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
	public void update3daysBeforeVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.visaEmailSend3daysBefore = true WHERE p.email = :email");
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

	public void update2daysBeforeVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.visaEmailSend2daysBefore = true WHERE p.email = :email");
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

	public void update1dayBeforeVisaEmailSend(String email) {
		try {
			Query query = entityManager.createQuery(
					"UPDATE PersonalInfo p SET p.visainfo.visaEmailSend1dayBefore = true WHERE p.email = :email");
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

			visaDetail.setVisaEmailSend20and60daysBefore(false);
			visaDetail.setVisaEmailSend10and30daysBefore(false);
			visaDetail.setVisaEmailSend4daysBefore(false);
			visaDetail.setVisaEmailSend3daysBefore(false);
			visaDetail.setVisaEmailSend20and60daysBefore(false);
			visaDetail.setVisaEmailSend1dayBefore(false);

			return entityManager.merge(personalInfo);
		}

		return null;
	}

	@Override
	public List<NotificationHelper> getNotificationFields() {
		List<NotificationHelper> query = entityManager.createQuery(
				"SELECT NEW com.erp.hrms.entity.notificationhelper.NotificationHelper (p.employeeId,p.namePrefix,p.firstName,p.middleName,p.lastName,p.email,"
						+ "p.visainfo.VisaType,p.visainfo.visaIssueyDate,p.visainfo.visaExpiryDate,'visa_expiry' )"
						+ "FROM PersonalInfo p " + "WHERE p.status='Active'",
				NotificationHelper.class).getResultList();
		return query;
	}

	@Override
	public List<PersonalInfo> getPersonalInfoWithPendingBackgroundCheck() {
		String jpqlQuery = "SELECT p FROM PersonalInfo p " + "JOIN p.bgcheck bg " + "WHERE bg.status = 'Pending'";
		TypedQuery<PersonalInfo> query = entityManager.createQuery(jpqlQuery, PersonalInfo.class);
		return query.getResultList();
	}

	@Override
	public List<PersonalInfo> getByPostedLocation(String postedLocation) {
	    try {
	        TypedQuery<PersonalInfo> query = entityManager.createQuery(
	            "SELECT p FROM PersonalInfo p JOIN p.jobDetails j WHERE j.postedLocation = :postedLocation", PersonalInfo.class);
	        query.setParameter("postedLocation", postedLocation);
	        return query.getResultList();
	    } catch (NoResultException ex) {
	        throw new PersonalInfoNotFoundException(
	            new MessageResponse("No personal information found for this posted location: " + postedLocation));
	    } catch (Exception ex) {
	        throw new RuntimeException("An error occurred while retrieving personal information for posted location: " + postedLocation, ex);
	    }
	}
	
}
