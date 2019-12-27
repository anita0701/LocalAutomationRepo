package com.townscript.automation.customexception;

public class InvalidExcelFormatException extends Exception {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6210323459510582752L;

	public InvalidExcelFormatException(String exceptionMessage) {
		super(exceptionMessage);
	}
}
