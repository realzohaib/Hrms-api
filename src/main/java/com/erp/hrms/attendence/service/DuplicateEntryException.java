package com.erp.hrms.attendence.service;

public class DuplicateEntryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public DuplicateEntryException(String message) {
		super();
		this.message = message;
	}

	public DuplicateEntryException() {
	}

}