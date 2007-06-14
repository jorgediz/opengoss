package org.opengoss.alarm.internal.core;

import java.util.List;

import org.hibernate.SessionFactory;
import org.opengoss.alarm.core.AlarmRule;
import org.opengoss.alarm.core.IAlarmRuleDao;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.hibernate.DaoSupport;

public class HibernateAlarmRuleDao extends DaoSupport implements IAlarmRuleDao {

	public HibernateAlarmRuleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public AlarmRule getAlarmRuleById(Long id) throws DaoException {
		return (AlarmRule) getDbAccessor().get(AlarmRule.class, id);
	}

	public List<AlarmRule> getAlarmRules(Class<AlarmRule> clazz) throws DaoException {
		return getDbAccessor().find("from " + clazz.getName());
	}

	public AlarmRule getById(Long id) throws DaoException {
		return (AlarmRule)getDbAccessor().get(AlarmRule.class, id);
	}

	public List<AlarmRule> query(String query) throws DaoException {
		return getDbAccessor().find(query);
	}
}
