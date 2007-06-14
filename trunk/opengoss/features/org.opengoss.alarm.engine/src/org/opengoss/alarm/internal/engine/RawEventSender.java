package org.opengoss.alarm.internal.engine;

import java.util.Map;

import org.opengoss.alarm.engine.IEventCollector;
import org.opengoss.alarm.engine.IEventSender;

public class RawEventSender implements IEventSender {

	private IEventCollector collector;

	public RawEventSender(IEventCollector collector) {
		this.collector = collector;
	}

	public void sendEvent(Map<String, Object> eventMap) {
		collector.collectEvent(eventMap);
	}

}
