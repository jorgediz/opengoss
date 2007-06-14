package org.opengoss.cnm.log.core;

import java.io.Serializable;

import org.opengoss.dao.core.DomainObject;


public class LoggerItem extends DomainObject implements Serializable{

//	private LogConstants.LogInstance logInstance; //和loggerItemList中的信息有重复

	public interface LogInstance {
		
		String OPERATIONLOG = "OperationLog";
		String SYSTEMLOG = "SysLog";
	}
	
	public interface AvailabilityStatus{
		String LOGFULL = "Log Full";
	}
	
	private String logInstance;
	
	private long  currentLogSize; // record current log record numbers   
	 
	private long  logRecordNumber; //init number readed from config file
	
	private long maxLogSize;
	
	private long  lastCountDate = 0;

	private long  maxBufferSize;
	
	private String availabilityStatus;
	
	public long getMaxBufferSize() {
		return maxBufferSize;
	}

	public void setMaxBufferSize(long maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	public long getLastCountDate() {
		return lastCountDate;
	}

	public void setLastCountDate(long lastCountDate) {
		this.lastCountDate = lastCountDate;
	}

	public long getCurrentLogSize() {
		return currentLogSize;
	}

	public void setCurrentLogSize(long currentLogSize) {
		this.currentLogSize = currentLogSize;
	}
//
//	public LogConstants.LogInstance getLogInstance() {
//		return logInstance;
//	}
//
//	public void setLogInstance(LogConstants.LogInstance logInstance) {
//		this.logInstance = logInstance;
//	}

	public long getLogRecordNumber() {
		return logRecordNumber;
	}

	public void setLogRecordNumber(long logRecordNumber) {
		this.logRecordNumber = logRecordNumber;
	}

	public long getMaxLogSize() {
		return maxLogSize;
	}

	public void setMaxLogSize(long maxLogSize) {
		this.maxLogSize = maxLogSize;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public String getLogInstance() {
		return logInstance;
	}

	public void setLogInstance(String logInstance) {
		this.logInstance = logInstance;
	}

	/*public LogConstants.AvailabilityStatus getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(
			LogConstants.AvailabilityStatus availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	} 
	*/
}
