package org.opengoss.example.log;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMyLogService extends Remote {
	
	public void addConsole(IConsole console);

	public void removeConsole(IConsole console);

	public void log(String message) throws RemoteException;
	
}
