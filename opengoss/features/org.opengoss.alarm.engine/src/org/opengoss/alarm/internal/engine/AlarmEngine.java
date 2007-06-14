package org.opengoss.alarm.internal.engine;

import java.io.File;
import java.util.List;

import net.esper.client.Configuration;
import net.esper.client.EPServiceProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opengoss.alarm.core.Alarm;
import org.opengoss.alarm.core.IAlarmDao;
import org.opengoss.alarm.engine.IAlarmEngine;
import org.opengoss.platform.esper.IEsperService;

public class AlarmEngine implements IAlarmEngine {
	
	static final Log logger = LogFactory.getLog(AlarmEngine.class);
	
	private IEsperService esperSerevice;
	
	private EPServiceProvider epServiceProvider = null;
	
	private IAlarmDao alarmDao;

	public AlarmEngine(IEsperService esperService, IAlarmDao alarmDao) {
		this.esperSerevice = esperService;
		this.alarmDao = alarmDao;
	}
	
	public EPServiceProvider getEsper() {
		if(epServiceProvider == null) {
			Configuration config = new Configuration();
			config.configure(new File("./etc/org.opengoss.alarm.engine/esper.cfg.xml"));
			epServiceProvider = esperSerevice.getEPServiceProvider("AlarmEngine", config);
		}
		return epServiceProvider;
	}

	public void addAlarms(List alarms) {
		alarmDao.save(alarms);
	}

	public void deleteAlarms(List alarms) {
		alarmDao.delete(alarms);
	}

	public List<Alarm> getEvents(String filter) {
		return alarmDao.query(filter);
	}

}
