package com.bim.commons.exceptions;

public class UnauthorizedException extends BimException {

	private static final long serialVersionUID = 5030470675688395784L;

	public UnauthorizedException() {
		super();
	}

	public UnauthorizedException(String message) {
		super(message);
	}

}