package com.erp.hrms.api.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.VisaDetail;

@Component
public class VisaExpiryNotification {

	@Autowired
	private IPersonalInfoDAO personalInfoDAO;

	@Autowired
	private JavaMailSender mailSender;

	private String adminEmail = "furqank095@gmail.com";

//	@Scheduled(fixedRate = 30000)
	@Scheduled(cron = "0 0 0 * * *")
	public void sendEmail() throws Exception {
		try {
			LocalDate currentDate = LocalDate.now();

			for (PersonalInfo personalInfo : personalInfoDAO.findAllPersonalInfo()) {
				VisaDetail visaInfo = personalInfo.getVisainfo();
				if (visaInfo != null) {
					String visaType = visaInfo.getVisaType();
					String visaExpiryDateString = visaInfo.getVisaExpiryDate();

					if (visaExpiryDateString != null && !visaExpiryDateString.isEmpty()) {
						LocalDate visaExpiryDate = LocalDate.parse(visaExpiryDateString, DateTimeFormatter.ISO_DATE);
						Period period = Period.between(currentDate, visaExpiryDate);
						int daysUntilExpiry = period.getDays();

						if (daysUntilExpiry <= 60 && daysUntilExpiry >= 1) {
							if (daysUntilExpiry == 20 && visaType.equals("Visit Visa")
									&& !personalInfo.getVisainfo().isFirstVisaEmailSend()) {
								sendNotificationOfVisitVisaFirst(personalInfo.getEmail(), visaExpiryDate, personalInfo);
							} else if (daysUntilExpiry == 10 && visaType.equals("Visit Visa")
									&& !personalInfo.getVisainfo().isSecondVisaEmailSend()) {
								sendNotificationOfVisitVisaSecond(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysUntilExpiry == 60 && visaType.equals("Work Visa")
									&& !personalInfo.getVisainfo().isFirstVisaEmailSend()) {
								sendNotificationOfWorkVisaFirst(personalInfo.getEmail(), visaExpiryDate, personalInfo);
							} else if (daysUntilExpiry == 30 && visaType.equals("Work Visa")
									&& !personalInfo.getVisainfo().isSecondVisaEmailSend()) {
								sendNotificationOfWorkVisaSecond(personalInfo.getEmail(), visaExpiryDate, personalInfo);
							} else if (daysUntilExpiry == 4
									&& !personalInfo.getVisainfo().isFirstContinuouslyVisaEmailSend()) {
								sendNotificationOfContinuouslyFirstEmail(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysUntilExpiry == 3
									&& !personalInfo.getVisainfo().isSecondContinuouslyVisaEmailSend()) {
								sendNotificationOfContinuouslySecondEmail(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysUntilExpiry == 2
									&& !personalInfo.getVisainfo().isThirdContinuouslyVisaEmailSend()) {
								sendNotificationOfContinuouslyThirdEmail(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysUntilExpiry == 1
									&& !personalInfo.getVisainfo().isFourContinuouslyVisaEmailSend()) {
								sendNotificationOfContinuouslyFourEmail(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfVisitVisaFirst(String email, LocalDate visaExpiryDate, PersonalInfo personalInfo)
			throws Exception {
		// Send email notification to employee
		try {
			sendEmail(email, "Visa Expiry Reminder", "Your visit visa will expire on this " + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The visit visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setFirstVisaEmailSend(true);

			personalInfoDAO.updateFirstVisaEmail(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfVisitVisaSecond(String email, LocalDate visaExpiryDate, PersonalInfo personalInfo)
			throws Exception {
		// Send email notification to employee
		try {
			sendEmail(email, "Visa Expiry Reminder", "Your visit visa will expire on this " + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The visit visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setSecondVisaEmailSend(true);
			personalInfoDAO.updateSecondVisaEmail(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfWorkVisaFirst(String email, LocalDate visaExpiryDate, PersonalInfo personalInfo)
			throws Exception {
		// Send email notification to employee
		try {
			sendEmail(email, "Visa Expiry Reminder", "Your work visa will expire on this" + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The work visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setFirstVisaEmailSend(true);
			personalInfoDAO.updateFirstVisaEmail(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfWorkVisaSecond(String email, LocalDate visaExpiryDate, PersonalInfo personalInfo)
			throws Exception {
		// Send email notification to employee
		try {
			sendEmail(email, "Visa Expiry Reminder", "Your work visa will expire on this" + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The work visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setFirstVisaEmailSend(true);
			personalInfoDAO.updateSecondVisaEmail(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfContinuouslyFirstEmail(String email, LocalDate visaExpiryDate,
			PersonalInfo personalInfo) throws Exception {
		try {
			// Send email notification to employee
			sendEmail(email, "Visa Expiry Reminder", "Your visa will expire on this " + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setFirstContinuouslyVisaEmailSend(true);
			personalInfoDAO.updatefirstContinuouslyVisaEmailSend(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfContinuouslySecondEmail(String email, LocalDate visaExpiryDate,
			PersonalInfo personalInfo) throws Exception {
		try {
			// Send email notification to employee
			sendEmail(email, "Visa Expiry Reminder", "Your visa will expire on this " + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setFirstContinuouslyVisaEmailSend(true);
			personalInfoDAO.updateSecondContinuouslyVisaEmailSend(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfContinuouslyThirdEmail(String email, LocalDate visaExpiryDate,
			PersonalInfo personalInfo) throws Exception {
		try {

			// Send email notification to employee
			sendEmail(email, "Visa Expiry Reminder", "Your visa will expire on this " + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setFirstContinuouslyVisaEmailSend(true);
			personalInfoDAO.updateThirdContinuouslyVisaEmailSend(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendNotificationOfContinuouslyFourEmail(String email, LocalDate visaExpiryDate,
			PersonalInfo personalInfo) throws Exception {
		try {
			// Send email notification to employee
			sendEmail(email, "Visa Expiry Reminder", "Your visa will expire on this " + visaExpiryDate + " date");

			// Send email notification to admin
			sendEmail(adminEmail, "Visa Expiry Reminder - Employee: " + email,
					"The visa of employee " + email + " will expire on this " + visaExpiryDate + " date");
			personalInfo.getVisainfo().setFirstContinuouslyVisaEmailSend(true);
			personalInfoDAO.updatefourContinuouslyVisaEmailSend(email);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	private void sendEmail(String to, String subject, String content) throws Exception {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(to);
			mailMessage.setSubject(subject);
			mailMessage.setText(content);

			mailSender.send(mailMessage);

		} catch (Exception e) {

			throw new Exception(e);
		}
	}
}
