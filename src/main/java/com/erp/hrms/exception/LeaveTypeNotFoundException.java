package com.erp.hrms.exception;

import com.erp.hrms.api.security.response.MessageResponse;

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
