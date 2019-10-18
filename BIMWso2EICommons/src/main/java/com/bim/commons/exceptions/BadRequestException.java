package com.bim.commons.exceptions;

public class BadRequestException extends BimException {

	private static final long serialVersionUID = -8977974695631909523L;

	public BadRequestException() {
		super();
	}

	public BadRequestException(String message) {
		super(message);
	}
	
}