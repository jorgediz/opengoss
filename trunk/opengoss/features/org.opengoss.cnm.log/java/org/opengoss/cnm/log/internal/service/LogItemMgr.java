package org.opengoss.cnm.log.internal.service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

import org.opengoss.cnm.log.core.LoggerItem;
import org.opengoss.cnm.log.core.LoggerItemList;
import org.opengoss.cnm.log.internal.dao.ILmDao;
import org.opengoss.core.IStartable;
import org.opengoss.dao.core.DaoException;

/**
 * 统一管理LoggerItemList
 * @author zouxc
 * @date 2006-12-20
 */
public class LogItemMgr {
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private Lock readLock = lock.readLock();
	
	private ILmDao lmDao;
	
	private static LoggerItemList list ;
	
	private static Logger log = Logger.getLogger("LogItemMgr");
	
//	public LogItemMgr(){
//	
//	}


//	public LoggerItem getLoggerItem(LogConstants.LogInstance logInstance){
//		return getLoggerItem(logInstance,true);
//	}
	public LoggerItem getLoggerItem(String logInstance){ 
		
		LoggerItem loggerItem ;
		readLock.lock();
		try {
			loggerItem =  list.getLoggerItemList().get(logInstance);	
		} finally{
			readLock.unlock();
		}
		return loggerItem;
		
	}
	
	public List<LoggerItem> findAllLoggerItem() {
		List<LoggerItem> loggerItemList;
		readLock.lock();
		try{
		 loggerItemList = list.getAllLoggerItem();
		}finally{
			readLock.unlock();
		}
		return loggerItemList;
	}
	
	public static LoggerItemList getList() {
		return list;
	}

	public static void setList(LoggerItemList list) {
		LogItemMgr.list = list;
	}

//	public ILmDao getLmDao() {
//		return lmDao;
//	}
//
//	public void setLmDao(ILmDao lmDao) {
//		this.lmDao = lmDao;
//	}


	public void initLoggerItemList(){
		try {
			list = lmDao.loadLoggerItemList();
			
		}catch(DaoException e ){
			log.warning("can't load loggerItem");
			e.printStackTrace();
		}
	}
	
	public ILmDao getLmDao() {
		return lmDao;
	}

	public void setLmDao(ILmDao lmDao) {
		this.lmDao = lmDao;
	}


	
}
