package org.opengoss.alarm.engine;

import java.util.Map;

public interface IEventCollector {

	void collectEvent(Map<String, Object> eventMap);
	
}
