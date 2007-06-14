package org.opengoss.alarm.engine;

import org.opengoss.alarm.core.Alarm;

public interface IAlarmCorrelator {

	void correlate(Alarm alarm) throws Exception;
	
}
