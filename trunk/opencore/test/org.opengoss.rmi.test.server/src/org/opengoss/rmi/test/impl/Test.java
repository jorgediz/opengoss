package org.opengoss.rmi.test.impl;

import org.opengoss.rmi.test.ITest;

public class Test implements ITest {

	public String echo(String message) {
		return message;
	}

}
