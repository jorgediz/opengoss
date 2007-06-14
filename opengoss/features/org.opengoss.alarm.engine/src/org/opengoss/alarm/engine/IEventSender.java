package org.opengoss.alarm.engine;

import java.util.Map;

public interface IEventSender {
	
	void sendEvent(Map<String, Object> eventMap);

}
