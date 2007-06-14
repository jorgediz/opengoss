package org.opengoss.cnm.log.internal.service;

import org.opengoss.cnm.log.core.LogAction;
import org.opengoss.cnm.log.core.LogLevel;
import org.opengoss.cnm.log.core.LogResult;
import org.opengoss.cnm.log.core.LoggerItem;
import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.SysLog;
import org.opengoss.cnm.log.core.service.ICnmLog;
import org.opengoss.cnm.log.internal.exception.CnmException;
import org.opengoss.dao.core.DomainObject;

public class CnmLogger implements ICnmLog {

	private ILogTaskEngine logTaskEngine;

	public void enterMethod(Object... arg) {
	}

	public void exitMethod() {

	}

	public void operation(LogAction action, LogResult result, String details)
			throws CnmException {

		OperationLog operationRecord = new OperationLog(action.name(),
				result != null ? result.name() : "", details);
		addTask(operationRecord, LoggerItem.LogInstance.OPERATIONLOG);
	}

	public void sysInfo(LogLevel level, String details) throws CnmException {
		SysLog sysRecord = new SysLog(SysLog.Level.ERROR, details); 
		addTask( sysRecord, LoggerItem.LogInstance.SYSTEMLOG);
	}

	private void addTask(DomainObject logRecord,
			String logInstance) throws CnmException {
		
		logTaskEngine.addTask(logRecord, logInstance);

	}

	public ILogTaskEngine getLogTaskEngine() {
		return logTaskEngine;
	}

	public void setLogTaskEngine(ILogTaskEngine logTaskEngine) {
		this.logTaskEngine = logTaskEngine;
	}

	
}
