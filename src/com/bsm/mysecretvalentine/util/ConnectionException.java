package com.bsm.mysecretvalentine.util;

public class ConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7159876483281222133L;

	public ConnectionException() {
		super();
	}

	public ConnectionException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ConnectionException(String detailMessage) {
		super(detailMessage);
	}

	public ConnectionException(Throwable throwable) {
		super(throwable);
	}

}
