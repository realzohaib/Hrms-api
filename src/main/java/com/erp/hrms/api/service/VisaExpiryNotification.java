package com.erp.hrms.api.service;

import java.time.LocalDate;

import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

						LocalDate visaExpiryDate = LocalDate.parse(visaExpiryDateString,
								DateTimeFormatter.ofPattern("dd-MM-yyyy"));

						long daysBetween = ChronoUnit.DAYS.between(currentDate, visaExpiryDate);

						if (daysBetween <= 60 && daysBetween >= 1) {
							if (daysBetween == 20 && visaType.equals("Visit Visa")
									&& !personalInfo.getVisainfo().isVisaEmailSend20and60daysBefore()) {
								sendNotificationOfVisitVisaFirst(personalInfo.getEmail(), visaExpiryDate, personalInfo);
							} else if (daysBetween == 10 && visaType.equals("Visit Visa")
									&& !personalInfo.getVisainfo().isVisaEmailSend10and30daysBefore()) {
								sendNotificationOfVisitVisaSecond(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysBetween == 60 && visaType.equals("Work Visa")
									&& !personalInfo.getVisainfo().isVisaEmailSend20and60daysBefore()) {
								sendNotificationOfWorkVisaFirst(personalInfo.getEmail(), visaExpiryDate, personalInfo);
							} else if (daysBetween == 30 && visaType.equals("Work Visa")
									&& !personalInfo.getVisainfo().isVisaEmailSend10and30daysBefore()) {
								sendNotificationOfWorkVisaSecond(personalInfo.getEmail(), visaExpiryDate, personalInfo);
							} else if (daysBetween == 4 && !personalInfo.getVisainfo().isVisaEmailSend4daysBefore()) {
								sendNotificationOfContinuouslyFirstEmail(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysBetween == 3 && !personalInfo.getVisainfo().isVisaEmailSend3daysBefore()) {
								sendNotificationOfContinuouslySecondEmail(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysBetween == 2 && !personalInfo.getVisainfo().isVisaEmailSend2daysBefore()) {
								sendNotificationOfContinuouslyThirdEmail(personalInfo.getEmail(), visaExpiryDate,
										personalInfo);
							} else if (daysBetween == 1 && !personalInfo.getVisainfo().isVisaEmailSend1dayBefore()) {
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

			personalInfo.getVisainfo().setVisaEmailSend20and60daysBefore(true);
			personalInfoDAO.update20and60daysBeforeVisaEmail(email);
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

			personalInfo.getVisainfo().setVisaEmailSend10and30daysBefore(true);
			personalInfoDAO.update10and30daysBeforeVisaEmail(email);
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

			personalInfo.getVisainfo().setVisaEmailSend20and60daysBefore(true);
			personalInfoDAO.update20and60daysBeforeVisaEmail(email);
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

			personalInfo.getVisainfo().setVisaEmailSend10and30daysBefore(true);
			personalInfoDAO.update10and30daysBeforeVisaEmail(email);
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

			personalInfo.getVisainfo().setVisaEmailSend4daysBefore(true);
			personalInfoDAO.update4daysBeforeVisaEmailSend(email);
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

			personalInfo.getVisainfo().setVisaEmailSend3daysBefore(true);
			personalInfoDAO.update3daysBeforeVisaEmailSend(email);
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

			personalInfo.getVisainfo().setVisaEmailSend2daysBefore(true);
			personalInfoDAO.update2daysBeforeVisaEmailSend(email);
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

			personalInfo.getVisainfo().setVisaEmailSend1dayBefore(true);
			personalInfoDAO.update1dayBeforeVisaEmailSend(email);
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
