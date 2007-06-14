package org.opengoss.alarm.internal.core;

import java.util.List;

import org.hibernate.SessionFactory;
import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.IAlarmDao;
import org.opengoss.alarm.core.BizAlarm;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.hibernate.DaoSupport;

public class HibernateAlarmDao extends DaoSupport implements IAlarmDao {

	public HibernateAlarmDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Alarm getAlarmById(Long id) throws DaoException {
		return (Alarm)getDbAccessor().get(Alarm.class, id);
	}

	public Alarm save(Alarm alarm) throws DaoException {
		getDbAccessor().save(alarm);
		return alarm;
	}

	public List<Alarm> add(List<Alarm> alarms) throws DaoException {
		getDbAccessor().saveOrUpdateAll(alarms);
		return alarms;
	}

	public void delete(Alarm alarm) throws DaoException {
		getDbAccessor().delete(alarm);
	}

	public List<Alarm> getAlarms(Class<Alarm> clazz) throws DaoException {
		return getDbAccessor().find("from " + clazz.getName());
	}

	public List<BizAlarm> getBizAlarms(Class<BizAlarm> clazz) throws DaoException {
		return getDbAccessor().find("from " + clazz.getName());
	}

	public Alarm getByAlarmKey(String alarmKey) throws DaoException {
		List<Alarm> alarms = getDbAccessor().find("from " + Alarm.class.getName() 
				+ " as alarm where alarm.alarmKey='" + alarmKey + "'");
		return (alarms != null && alarms.size() > 0) ? alarms.get(0) : null;
	}

	public Alarm getById(Long id) throws DaoException {
		return (Alarm) getDbAccessor().get(Alarm.class, id);
	}

	public List<Alarm> query(String query) throws DaoException {
		return getDbAccessor().find(query);
	}

	public void update(Alarm alarm) throws DaoException {
		getDbAccessor().update(alarm);
	}

}
