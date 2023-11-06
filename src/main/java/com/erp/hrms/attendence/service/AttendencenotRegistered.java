package com.erp.hrms.attendence.service;

public class AttendencenotRegistered extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;

	public AttendencenotRegistered() {
	}

	public AttendencenotRegistered(String message) {

		this.message = message;
	}

}