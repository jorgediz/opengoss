package org.opengoss.cnm.log.internal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.opengoss.cnm.log.core.LoggerItem;
import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.SysLog;
import org.opengoss.cnm.log.internal.dao.ILmDao;
import org.opengoss.core.IStartable;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.DomainObject;

public class LogTaskEngine implements ILogTaskEngine ,IStartable {

//	public LogManager logManager ;
	//LogTaskEngineImpl和LogManager互相引用，所以不用roc
	private LogItemMgr logItemMgr;
	
	private ILmDao lmDao;
	
	private static ExecutorService pool;
	
	private static HashMap<String, ArrayList<DomainObject>> logRecordBuffer; 
	
	public void start() throws Exception {
//		logManager = new LogManager();
		
		pool = Executors.newFixedThreadPool(5,
				new ThreadFactory() {
			public Thread newThread(Runnable r) {
				return new Thread(r, "Log Task Thread");
			}
		});
		logRecordBuffer = new HashMap<String, ArrayList<DomainObject>>(2);
        logRecordBuffer.put(LoggerItem.LogInstance.OPERATIONLOG,
                new ArrayList<DomainObject>());
        logRecordBuffer.put(LoggerItem.LogInstance.SYSTEMLOG,
                new ArrayList<DomainObject>());

	}
	 
	public void addTask(DomainObject log, String  logInstance) {
		pool.execute(new Task(log,  logInstance)); 
//		TaskTest test = new TaskTest(log,logInstance);
//		try {
//			test.testSaveLogRecord();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void flushLogBuffer(){
		
		List<LoggerItem> loggerItemList = logItemMgr.findAllLoggerItem();
		for (LoggerItem item : loggerItemList) {
				flushLogBuffer(item);
		}
	}
	
	public void flushLogBuffer(LoggerItem loggerItem){
		  List<DomainObject> logBuffer = logRecordBuffer.get(loggerItem
	                .getLogInstance());
		  if (logBuffer.size() == 0){ //buffer中没有log
			  return ;
		  }
	        List<DomainObject> flushLogBuffer = null;
	        synchronized (logBuffer) {
	            flushLogBuffer(logBuffer, loggerItem);
	        }
	}
	
	public  void flushLogBuffer(List<DomainObject> logBuffer,LoggerItem loggerItem) {
        int invalidNumber = 0;
        try {
            lmDao.save(logBuffer);
        } catch (Exception ex1) {
            for (DomainObject dObject : logBuffer) {
                try {
                    lmDao.save(dObject);
                } catch (Exception ex2) {
                    invalidNumber++;
                }
            }
        }
        long newLogRecordNumber = 0;
        Class clazz = null;
        if (LoggerItem.LogInstance.OPERATIONLOG.equals( loggerItem
                .getLogInstance())) {
            clazz = OperationLog.class;
        } else if (LoggerItem.LogInstance.SYSTEMLOG.equals( loggerItem
                .getLogInstance())) {
            clazz = SysLog.class;
        } 
        try {
			newLogRecordNumber = lmDao.countAll(clazz);
		} catch (DaoException e) {
			e.printStackTrace();
		} //logRecord总的数目
        if ((newLogRecordNumber < loggerItem.getMaxLogSize() || loggerItem.getMaxLogSize() == 0) //设置status的状�?
                && (LoggerItem.AvailabilityStatus.LOGFULL.equals(loggerItem.getAvailabilityStatus()))) 
		{
            loggerItem.setAvailabilityStatus(null);
        }
        if (newLogRecordNumber >= loggerItem.getMaxLogSize() && loggerItem.getMaxLogSize() != 0
                && !(LoggerItem.AvailabilityStatus.LOGFULL.equals(loggerItem.getAvailabilityStatus())))
		{
            loggerItem.setAvailabilityStatus(LoggerItem.AvailabilityStatus.LOGFULL);
        }
        
		loggerItem.setLastCountDate(System.currentTimeMillis());
        loggerItem.setLogRecordNumber(newLogRecordNumber);
        loggerItem.setCurrentLogSize(newLogRecordNumber);
        
        logBuffer.clear();
    
	}
	
	
	 public class Task implements Runnable {

	        private DomainObject logRecord;

//	        private String module;

	        private String logInstance;

	        private LoggerItem loggerItem;

	        private List flushLogBuffer = null;

	        /**
	         * Constructs an empty Task with the default parameter
	         * 
	         * @param log
	         * @param module
	         */
	        Task(DomainObject logRecord,
	        		String logInstance) {

	            this.logRecord = logRecord;
//	            this.module = module;
	            this.logInstance = logInstance;
//	            this.loggerItem = logManager.getLoggerItem(logInstance, false);
	            this.loggerItem = logItemMgr.getLoggerItem(logInstance);
	        }

	        public void run() {

	            try {
	                 saveLogRecord();
	            } catch (Exception e) {
//	                logger.error(e.toString(), e);
	            	// exception will be catched 
	            }

	        }

	        /**
	         * save log record to buffer
	         * 
	         * @throws InterruptedException
	         */
	        private void saveLogRecord() throws InterruptedException,Exception {
	            List<DomainObject> logBuffer = null;
	            if (logRecord instanceof SysLog) {
	                logBuffer = logRecordBuffer.get(LoggerItem.LogInstance.SYSTEMLOG);
	            } 
	            if (logRecord instanceof OperationLog) {
	                OperationLog operationLog = (OperationLog) logRecord;
	                
	                logBuffer = logRecordBuffer.get(LoggerItem.LogInstance.OPERATIONLOG);
	            } 
	           
	            synchronized (logBuffer) {
				    logBuffer.add(logRecord);
				    long maxBufferSize = loggerItem.getMaxBufferSize();
				    if (logBuffer.size() >= maxBufferSize) // flush log buffer if buffer is full 
				    {
				        flushLogBuffer(logBuffer, loggerItem);
				    }
				}

	        }

	    }
	/* //只用于测试
	 public class TaskTest  {

	        private DomainObject logRecord;

//	        private String module;

	        private String logInstance;

	        private LoggerItem loggerItem;

	        private List flushLogBuffer = null;

	        *//**
	         * Constructs an empty Task with the default parameter
	         * 
	         * @param log
	         * @param module
	         *//*
	        TaskTest(DomainObject logRecord,
	        		String logInstance) {

	            this.logRecord = logRecord;
//	            this.module = module;
	            this.logInstance = logInstance;
//	            this.loggerItem = logManager.getLoggerItem(logInstance, false);
	            this.loggerItem = logItemMgr.getLoggerItem(logInstance);
	         }

	     
	        *//**
	         * save log record to buffer
	         * 
	         * @throws InterruptedException
	         *//*
	        public void testSaveLogRecord() throws InterruptedException,Exception {
	            List<DomainObject> logBuffer = null;
	            if (logRecord instanceof SysLog) {
	                logBuffer = logRecordBuffer
	                        .get(LoggerItem.LogInstance.SYSTEMLOG);
	            } 
	            if (logRecord instanceof OperationLog) {
	                OperationLog operationLog = (OperationLog) logRecord;
	                
	                logBuffer = logRecordBuffer
	                        .get(LoggerItem.LogInstance.OPERATIONLOG);
	            } 
	           
	            synchronized (logBuffer) {
				    logBuffer.add(logRecord);
				    long maxBufferSize = loggerItem.getMaxBufferSize();
				    if (logBuffer.size() >= maxBufferSize) // flush log buffer if buffer is full 
				    {
				        flushLogBuffer(logBuffer, loggerItem);
				    }
				}

	        }

	    }*/



	public ILmDao getLmDao() {
		return lmDao;
	}

	public void setLmDao(ILmDao lmDao) {
		this.lmDao = lmDao;
	}

	

	public void stop() throws Exception {
		
	}

	public LogItemMgr getLogItemMgr() {
		return logItemMgr;
	}

	public void setLogItemMgr(LogItemMgr logItemMgr) {
		this.logItemMgr = logItemMgr;
	}

	
}
