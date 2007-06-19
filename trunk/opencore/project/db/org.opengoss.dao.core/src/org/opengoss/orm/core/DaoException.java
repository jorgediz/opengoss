package org.opengoss.orm.core;

public class DaoException extends Exception {

	private static final long serialVersionUID = 3763054123917704214L;

	public DaoException() {}

	public DaoException(Exception e) {
		super(e);
	}

	public DaoException(String s) {
		super(s);
	}

}
