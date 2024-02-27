package com.erp.hrms.forgetPassword.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.exception.PersonalEmailExistsException;
import com.erp.hrms.forgetPassword.dao.IPasswordReset;
import com.erp.hrms.forgetPassword.entity.PasswordReset;

@Service
public class PassworfResetService {
	@Autowired
	private IPasswordReset dao;

	@Autowired
	private JavaMailSender javaMailSender;

	public void generateOTP(PasswordReset passwordreset) {
		String email = passwordreset.getEmail();
		if (dao.existsByEmail(email) == false) {
			throw new PersonalEmailExistsException(new MessageResponse("Email not registered"));
		}

		Integer otp = passwordreset.getOtp();
		dao.save(passwordreset);

		sendotp(email, otp);
	}

	public boolean verifyOTP(String email, int OTP) {
		PasswordReset passwordReset = dao.findByEmailAndOtp(email, OTP);
		return passwordReset != null && !passwordReset.isUsed()
				&& !passwordReset.getExpirationDateTime().isBefore(LocalDateTime.now());
	}

	public void sendotp(String email, int otp) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("SI Global Company <mfurqan9988@gmail.com>");
		mailMessage.setTo(email);
		mailMessage.setSubject("OTP for password reset");
		  mailMessage.setText("Dear User,\n\n" +
		            "This email is to inform you that a one-time password (OTP) has been generated for the purpose of resetting your password.\n\n" +
		            "OTP: " + otp + "\n\n" +
		            "Please use this OTP within the next 10 minutes, as it will expire thereafter.\n\n" +
		            "Best regards,\n" +
		            "SI Global Company");		
		  javaMailSender.send(mailMessage);
	}
}
