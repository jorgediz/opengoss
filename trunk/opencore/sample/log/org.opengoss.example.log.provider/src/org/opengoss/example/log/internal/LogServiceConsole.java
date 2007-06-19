package org.opengoss.example.log.internal;

import org.opengoss.example.log.IConsole;
import org.osgi.service.log.LogService;

public class LogServiceConsole implements IConsole {

	private LogService delegate;
	
	public void setDelegate(LogService delegate) {
		this.delegate = delegate;
	}
	
	public void print(String msg) {
		delegate.log(LogService.LOG_INFO, msg);
	}

}
