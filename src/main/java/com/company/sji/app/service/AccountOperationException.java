package com.company.sji.app.service;

public class AccountOperationException extends Exception {
	
	private static final long serialVersionUID = 8711776063781509475L;
	
	public AccountOperationException(String errorMessage) {
		super(errorMessage);
	}
	public AccountOperationException(Exception cause) {
		super(cause);
	}
}
