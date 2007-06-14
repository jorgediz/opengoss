package org.opengoss.cnm.log.internal.dao;

import java.util.List;

import org.opengoss.cnm.log.core.LoggerItemList;
import org.opengoss.cnm.log.core.OperationLog;
import org.opengoss.cnm.log.core.SysLog;
import org.opengoss.cnm.log.internal.exception.CnmException;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.DomainObject;
import org.opengoss.dao.core.IDomainDao;

/**
 * 提供保存和查找操作，系统日志记录
 * @author zouxc
 * @date 2006-12-15
 */
public interface ILmDao extends IDomainDao{
	  
	 public <T extends DomainObject> T findLogRecord(Class clazz ,Long id ) throws  DaoException;
	  
	 public List<DomainObject> findLogRecordByHql( String sql) throws  DaoException;
	 
	 public List<OperationLog> loadOperationRecord() throws DaoException;
	 
	 public List<SysLog> loadSysRecord() throws  DaoException;
	 
	 public List<SysLog> findSysRecordByParam(String hql ,Object...objects ) throws DaoException;
	 /**
		 * load object of specifid class from db 
		 * @param clazz
		 * @return
	 * @throws DaoException 
		 */
	public LoggerItemList loadLoggerItemList() throws  DaoException;
		
		/**
		 * save object of specified class to db
		 * @param object
		 * @throws DaoException 
		 */
	public void saveLoggerItemList (LoggerItemList object)throws DaoException;
	 
}
