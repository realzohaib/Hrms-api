package com.erp.hrms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.erp.hrms.api.security.response.MessageResponse;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LeaveTypeNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MessageResponse messageResponse;

	public LeaveTypeNotFoundException(MessageResponse messageResponse) {
		super(messageResponse.getMessage());
		this.messageResponse = messageResponse;
	}

}
