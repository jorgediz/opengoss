package org.opengoss.cnm.log.internal.service;

import java.util.logging.Logger;

import org.opengoss.cnm.log.core.LoggerItemList;
import org.opengoss.cnm.log.internal.dao.IGenericDao;
//deprecated
public class LogUtils {

	private static Logger log = Logger.getLogger("LogUtils");
	
	private	  IGenericDao genericDao;
	
	public LoggerItemList loadLoggerItemList(){
		log.info("enter loadLoggerItemList method");
		return (LoggerItemList) genericDao.load(LoggerItemList.class);
	}
	public void saveLoggerItemList (LoggerItemList list){
		genericDao.save(list);
	}
	public IGenericDao getGenericDao() {
		return genericDao;
	}
	public void setGenericDao(IGenericDao genericDao) {
		this.genericDao = genericDao;
	}
	
}
