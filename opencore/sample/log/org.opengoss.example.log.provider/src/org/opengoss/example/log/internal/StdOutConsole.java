package org.opengoss.example.log.internal;

import org.opengoss.example.log.IConsole;

public class StdOutConsole implements IConsole {

	public void print(String msg) {
		System.out.println(msg);
	}

}
