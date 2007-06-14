package org.opengoss.alarm.internal.engine;

import net.esper.client.UpdateListener;
import net.esper.event.EventBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.HandlePhase;
import org.opengoss.alarm.core.IAlarmDao;
import org.opengoss.alarm.engine.IAlarmCorrelator;
import org.opengoss.core.IInitializable;
import org.opengoss.dao.core.DaoException;
import org.opengoss.platform.esper.EsperSupport;

public class AlarmCorrelator extends EsperSupport implements IAlarmCorrelator,
		IInitializable, UpdateListener {

	static final Log logger = LogFactory.getLog(AlarmCorrelator.class);

	private IAlarmDao alarmDao;

	public AlarmCorrelator(AlarmEngine engine) {
		super(engine.getEsper());
	}

	public void setAlarmDao(IAlarmDao alarmDao) {
		this.alarmDao = alarmDao;
	}

	public void correlate(Alarm alarm) throws Exception {
		Alarm persistedAlarm = alarmDao.getByAlarmKey(alarm.getAlarmKey());
		Alarm rtAlarm = null;
		if (persistedAlarm == null) {
			rtAlarm = add(alarm);
		} else {
			if (alarm.getPerceivedSeverity() == 0) {
				rtAlarm = clear(persistedAlarm, alarm);
			} else {
				rtAlarm = update(persistedAlarm, alarm);
			}
		}
		if (rtAlarm != null) {
			route(rtAlarm);
		}
	}

	public void init() throws Exception {
		epStatement = esper.getEPAdministrator().createEQL(
				"select * from AlarmEvent.win:time(1 sec) where phrase="
						+ HandlePhase.CONSOLIDED);
		epStatement.addListener(this);
//		epStatement.start();
	}

	public void destory() throws Exception {
		epStatement.removeListener(this);
//		epStatement.stop();
	}

	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		if (newEvents == null) {
			return;
		}
		for (EventBean eventBean : newEvents) {
			Alarm alarm = (Alarm) eventBean.getUnderlying();
			try {
				correlate(alarm);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private Alarm update(Alarm persistedAlarm, Alarm newAlarm)
			throws DaoException {
		persistedAlarm.setAlarmRaisedTime(newAlarm.getAlarmRaisedTime());
		persistedAlarm.setLastOccurence(newAlarm.getAlarmRaisedTime());
		persistedAlarm.setRepeatNumbers(persistedAlarm.getRepeatNumbers() + 1);
		alarmDao.update(persistedAlarm);
		return persistedAlarm;
	}

	private Alarm clear(Alarm persistedAlarm, Alarm newAlarm)
			throws DaoException {
		persistedAlarm.setPerceivedSeverity(0);
		persistedAlarm.setAlarmClearedTime(newAlarm.getAlarmRaisedTime());
		alarmDao.save(persistedAlarm);
		return persistedAlarm;
	}

	private Alarm add(Alarm alarm) throws DaoException {
		alarm = alarmDao.save(alarm);
		return alarm;
	}

	private void route(Alarm persistedAlarm) {
		persistedAlarm.setPhrase(HandlePhase.PERSISTED);
		esper.getEPRuntime().route(persistedAlarm);
	}

}
