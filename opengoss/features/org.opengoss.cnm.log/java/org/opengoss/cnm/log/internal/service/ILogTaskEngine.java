package org.opengoss.cnm.log.internal.service;


import org.opengoss.cnm.log.core.LoggerItem;
import org.opengoss.dao.core.DomainObject;

public interface ILogTaskEngine {
    /**
     * add task to thread pool
     * @param log
     */
    public void addTask(DomainObject log, String logInstance);
	
    /**
	 * flush all the log record buffer in the log task engine
	 * @param logBuffer
	 * @param loggerItem
	 */
	public void flushLogBuffer();
	/**
	 * flush all the log record buffer in the log task engine
	 * @param logBuffer
	 * @param loggerItem
	 */
	public void flushLogBuffer(LoggerItem loggerItem);
	
}
