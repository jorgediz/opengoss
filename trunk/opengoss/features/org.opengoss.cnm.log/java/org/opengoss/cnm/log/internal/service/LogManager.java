package org.opengoss.cnm.log.internal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opengoss.cnm.log.core.LoggerItem;
import org.opengoss.cnm.log.core.LoggerItemList;
import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.OperationSearchData;
import org.opengoss.cnm.log.core.SysLog;
import org.opengoss.cnm.log.core.SysSearchData;
import org.opengoss.cnm.log.core.service.ILogService;
import org.opengoss.cnm.log.internal.dao.ILmDao;
import org.opengoss.cnm.log.internal.exception.CnmException;
import org.opengoss.core.IStartable;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.DomainObject;

public class LogManager implements ILogService ,IStartable{

	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private Lock readLock = lock.readLock();

	private LogItemMgr logItemMgr;
	
	private ILmDao lmDao;
	
	private ILogTaskEngine logTaskEngine;
	
	private Logger log = Logger.getLogger("LogManagement");
	
	private static LoggerItemList list;
	
	private static final String FROM = "from";
	
	private static final String BLANK = " ";
	
	private static final String ALIAS = "o";
	
	private static final String WHERE = "where";
	
	private static final String AND = "and";


	public void start() throws Exception {
		logItemMgr.initLoggerItemList();
		
		list = logItemMgr.getList();
		if (list == null){
			log.warning("init LoggerItemList failed");
			return ;
		}else{
			log.info("init LoggerItemList sucessful");
		}
		
		startUp();

	}
	
	public void startUp() throws CnmException {
		//test
		if (list == null){
			
			return ;
		}
		List<LoggerItem> loggerItemList = list.getAllLoggerItem();
		for (LoggerItem loggerItem : loggerItemList) {
			
			 String logInstance = loggerItem.getLogInstance();
			long logRecordNumber = 0;
			if (LoggerItem.LogInstance.OPERATIONLOG.equals(logInstance)){
				
				logRecordNumber = countLogSize(OperationLog.class); 
				
			}
			if(LoggerItem.LogInstance.SYSTEMLOG.equals(logInstance)){
				
				logRecordNumber = countLogSize(SysLog.class);
			}
		
			if(loggerItem.getLogRecordNumber() != logRecordNumber){ 	
				loggerItem.setLastCountDate(System.currentTimeMillis());
				loggerItem.setLogRecordNumber(logRecordNumber);	
				loggerItem.setCurrentLogSize(logRecordNumber);
			}
		}
	}

	public void stop() {
		
		if (list == null ){
			return ;
		}
		
		logTaskEngine.flushLogBuffer();
		try {
			lmDao.saveLoggerItemList(list);
		} catch (DaoException e) {
			e.printStackTrace();
		}
	
		}
	

	public long countLogSize( Class logKlass ) throws CnmException //Rule 暂时用Object代替
	{
		LoggerItem loggerItem = transform (logKlass);
        
		logTaskEngine.flushLogBuffer(loggerItem);		
			try {
				return lmDao.countAll(logKlass);
			} catch (DaoException e) {
				e.printStackTrace();
			} 
			return 0;
	}
	  private LoggerItem transform(Class logKlass){
	        LoggerItem loggerItem = null;
	        if(LoggerItem.LogInstance.OPERATIONLOG.equals(logKlass.getSimpleName()))
	        {
	            loggerItem = list.getLoggerItem(LoggerItem.LogInstance.OPERATIONLOG);
	        }
	        else if(LoggerItem.LogInstance.SYSTEMLOG.equals(logKlass.getSimpleName()))
	        {
	            loggerItem = list.getLoggerItem(LoggerItem.LogInstance.SYSTEMLOG);
	        }
	        return loggerItem;
	    }


	public ILogTaskEngine getLogTaskEngine() {
		return logTaskEngine;
	}

	public void setLogTaskEngine(ILogTaskEngine logTaskEngine) {
		this.logTaskEngine = logTaskEngine;
	}
	
	public List<OperationLog> loadOperationLogRecord() throws CnmException {
		log.info("enter loadOperationLogRecord method");
		try {
			List<OperationLog> opLogs = lmDao.loadOperationRecord();
			return opLogs;
		} catch (DaoException e) {
			e.printStackTrace();
//			log.warning("load operationRecord has exception");
			return new ArrayList<OperationLog>();
		} 
		/*OperationLog log = new OperationLog();
		log.setDescription("operationLog");
		List<OperationLog> list = new ArrayList<OperationLog>();
		list.add(log);*/
	}

	public List<SysLog> loadSysLogRecord() throws CnmException {
		log.info("enter loadSysLogRecord method");
		try {
			return lmDao.loadSysRecord();
		} catch (DaoException e) {

			e.printStackTrace();
		}
		return null;
	}
	public List<SysLog> findSysLogRecord(SysSearchData data) throws CnmException {
		List<SysLog> result = new ArrayList<SysLog>();
		log.info("enter findSysLogRecord method");
			 String hql= parse2Sql( SysLog.class  , data); //要输入所有的text值内容
			 try {
				 Date time = data.getTime();
				 java.sql.Date date = new java.sql.Date(time.getTime());
				 result =  lmDao.findSysRecordByParam(hql, date);
				 log.log(Level.INFO, "LogManager: exit findLogRecord method");
			} catch (DaoException e) {
				e.printStackTrace();
			}
			 	
			 return result;
	}
	
	public List<DomainObject> findLogRecord(String className, OperationSearchData data) throws CnmException {
		ArrayList<DomainObject> result = new ArrayList<DomainObject>();
		log.log(Level.INFO, "LogManager: enter findLogRecord method");
		if(className.equals("OperationLog")){
			 String hql= parse2Hql( OperationLog.class  , data); //要输入所有的text值内容
			 try {
				result =(ArrayList<DomainObject>) lmDao.findLogRecordByHql( hql);
				 log.log(Level.INFO, "LogManager: exit findLogRecord method");
				 return result;
			} catch (DaoException e) {
				e.printStackTrace();
			}
			
		}
		return null;
//		if (className.equals("SysLog")){
		
	}
	
	/**
	 *传输数据转换成sql语句
	 *sql = from org.opengoss.cnm.log.core.OperationLog o where o.type.name = 'operation'
	 *and o.userName = 'user'  and o.userIP = 'aa' and o.action = 'bb' and o.result = 'a' and o.desc = 'd'
	 * @param s
	 * @return
	 */
	private String parse2Hql(Class clazz, OperationSearchData data ){
		String sql = FROM;
		sql = sql +BLANK+ clazz.getName() + BLANK + ALIAS +BLANK + WHERE ;
	
		if (!data.getUserName().equals("")){
		sql = sql + BLANK  +ALIAS + ".userName = "+"\'"+data.getUserName()+"\'" + BLANK;
		}
		if (!data.getUserIP().equals("")){
		sql = sql +AND  +BLANK +ALIAS + ".userIP = "+"\'"+ data.getUserIP()+"\'" + BLANK;
		}
		if (!data.getAction().equals("")){
		sql = sql +AND  +BLANK +ALIAS + ".action = "+"\'"+ data.getAction()+"\'"+ BLANK;
		}
		if (!data.getResult().equals("")){
		sql = sql +AND  +BLANK +ALIAS + ".result = "+"\'"+ data.getResult()+"\'" +BLANK;
		}
		return sql;
	}
	
	
	private String parse2Sql(Class clazz, SysSearchData data ){ //sysLog的sql语句生成
		String sql = FROM;
		sql = sql +BLANK+ clazz.getName() + BLANK + ALIAS +BLANK + WHERE +BLANK + ALIAS +".level = ";
		sql = sql +"\'"+ data.getLevel() +"\'" + BLANK;
		
		if (!(data.getTime() == null)){
			sql = sql +AND  +BLANK +ALIAS + ".time = ? " + BLANK;
		}
		
		if (!(data.getDesc() == null)){
			sql = sql +AND  +BLANK +ALIAS + ".desc= "+"\'"+ data.getDesc()+"\'" + BLANK;
		}
		if (!(data.getResult() == null)){
			sql = sql +AND  +BLANK +ALIAS + ".result = "+"\'"+ data.getResult()+"\'" + BLANK;
		}
		return sql;
	}
	
	public String hello() {
		return "acho hello";
	}

	public ILmDao getLmDao() {
		return lmDao;
	}

	public void setLmDao(ILmDao lmDao) {
		this.lmDao = lmDao;
	}

	public LogItemMgr getLogItemMgr() {
		return logItemMgr;
	}

	public void setLogItemMgr(LogItemMgr logItemMgr) {
		this.logItemMgr = logItemMgr;
	}


//	public LogUtils getLogUtils() {
//		return logUtils;
//	}
//
//	public void setLogUtils(LogUtils logUtils) {
//		this.logUtils = logUtils;
//	}


}
