package org.opengoss.alarm.core;

import java.util.List;

import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.IDomainDao;

public interface IAlarmRuleDao extends IDomainDao {
	
	AlarmRule getById(Long id) throws DaoException;
	
	AlarmRule getAlarmRuleById(Long id) throws DaoException;
	
	List<AlarmRule> query(String query) throws DaoException;
	
	List<AlarmRule> getAlarmRules(Class<AlarmRule> clazz) throws DaoException;

}
