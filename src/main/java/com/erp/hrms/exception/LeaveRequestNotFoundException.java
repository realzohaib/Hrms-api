package com.erp.hrms.exception;

import com.erp.hrms.api.security.response.MessageResponse;

public class LeaveRequestNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MessageResponse messageResponse;

	public LeaveRequestNotFoundException(MessageResponse messageResponse) {
		super(messageResponse.getMessage());
		this.messageResponse = messageResponse;
	}
}