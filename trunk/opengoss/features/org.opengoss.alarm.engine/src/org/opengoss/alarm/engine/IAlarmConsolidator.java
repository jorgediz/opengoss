package org.opengoss.alarm.engine;

import java.util.Map;

import org.opengoss.alarm.core.Alarm;

public interface IAlarmConsolidator {

	Alarm consolidate(Map<String, Object> eventMsg);
	
}
