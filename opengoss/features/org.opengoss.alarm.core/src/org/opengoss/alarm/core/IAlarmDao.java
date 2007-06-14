package org.opengoss.alarm.core;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.IDomainDao;

/**
 * Alarm Dao接口,CRUD操作
 * 
 * @author hurr
 * @version 2006-12-12
 */
public interface IAlarmDao extends IDomainDao {
	
	Alarm getById(Long id) throws DaoException;
	
	Alarm getByAlarmKey(String alarmKey) throws DaoException;
	
	Alarm getAlarmById(Long id) throws DaoException;
	
	List<Alarm> query(String query) throws DaoException;
	
	List<BizAlarm> getBizAlarms(Class<BizAlarm> clazz) throws DaoException;
	
}
