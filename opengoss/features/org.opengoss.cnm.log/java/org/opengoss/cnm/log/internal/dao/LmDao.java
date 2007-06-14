package org.opengoss.cnm.log.internal.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.opengoss.cnm.log.core.LoggerItem;
import org.opengoss.cnm.log.core.LoggerItemList;
import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.SysLog;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.DomainObject;
import org.opengoss.dao.hibernate.DaoSupport;

public class LmDao extends DaoSupport implements ILmDao {

	public LmDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public <T extends DomainObject> T findLogRecord(Class clazz, Long id) throws  DaoException {
		if (clazz == OperationLog.class){
			return (T) this.uniqueResultByNamedQuery("findOperationLogRecord", id);
		}
		if (clazz == SysLog.class){
			return (T) this.uniqueResultByNamedQuery("findSystemLogRecord", id);
		}
		return null;
	}

	public List<DomainObject> findLogRecordByHql( String hql) throws  DaoException {
		return super.list(hql);
	}
	
	

	public LoggerItemList loadLoggerItemList() throws DaoException {
		List<DomainObject> loggerItems = this.listAll(LoggerItem.class);
		LoggerItemList loggerItemList = new LoggerItemList();
		if (loggerItems.size() != 2){
			return null;
		}else{
			for (int i = 0; i < loggerItems.size(); i++) {
				LoggerItem loggerItem = (LoggerItem) loggerItems.get(i);
				if (loggerItem.getLogInstance().equals(LoggerItem.LogInstance.OPERATIONLOG)){
					loggerItemList.addLoggerItem(LoggerItem.LogInstance.OPERATIONLOG, loggerItem);
				}
				if (loggerItem.getLogInstance().equals(LoggerItem.LogInstance.SYSTEMLOG)){
					loggerItemList.addLoggerItem(LoggerItem.LogInstance.SYSTEMLOG, loggerItem);
				}
				
			}
			return loggerItemList ;
		}
		
	}

	public List<OperationLog> loadOperationRecord() throws DaoException {
		return this.listAll(OperationLog.class);
	}

	public List<SysLog> loadSysRecord() throws DaoException {
		return this.listAll(SysLog.class);
	}

	public void saveLoggerItemList(LoggerItemList object) throws  DaoException {
		List<LoggerItem> list = object.getAllLoggerItem();
		for (int i = 0; i < list.size(); i++) {
			super.update(list.get(i));
		}
	}

	public List<SysLog> findSysRecordByParam(String hql ,Object... objects) throws DaoException {
		return this.list(hql, objects);
	}

	

}
