package org.opengoss.core.util;

@SuppressWarnings("serial")
public class AssertException extends RuntimeException {

	public AssertException(String msg) {
		super(msg);
	}

	public AssertException(String msg, Exception e) {
		super(msg, e);
	}

}
