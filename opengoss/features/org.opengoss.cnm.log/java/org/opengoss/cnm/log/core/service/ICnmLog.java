package org.opengoss.cnm.log.core.service;

import org.opengoss.cnm.log.core.LogAction;
import org.opengoss.cnm.log.core.LogLevel;
import org.opengoss.cnm.log.core.LogResult;
import org.opengoss.cnm.log.internal.exception.CnmException;

public interface ICnmLog {

	public void enterMethod(Object... arg);
	
	public void exitMethod();
	
	public void operation(LogAction action, LogResult result, String details) throws CnmException;
	
	public void sysInfo(LogLevel level, String details) throws CnmException;
}
