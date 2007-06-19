package org.opengoss.example.log.consumer.internal;

import java.rmi.RemoteException;

import org.opengoss.example.log.IMyLogService;
import org.opengoss.example.log.consumer.ILogConsumer;

public class LogConsumer implements ILogConsumer {

	private IMyLogService logService;

	public void setLogService(IMyLogService logService) {
		this.logService = logService;
	}
	
	public void consume() {
		try {
			logService.log("Yes, It works!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
