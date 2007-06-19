package org.opengoss.example.log.internal;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.opengoss.example.log.IConsole;
import org.opengoss.example.log.IMyLogService;

public class MyLogService implements IMyLogService {
	
	private transient List<IConsole> consoles = new ArrayList<IConsole>(5);
	
	public void addConsole(IConsole console) {
		consoles.add(console);
	}

	public void removeConsole(IConsole console) {
		consoles.remove(console);
	}
	
	public void log(String msg) throws RemoteException {
		for (IConsole console : consoles) {
			console.print(msg);
		}
	}
	
}
