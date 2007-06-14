package org.opengoss.probe.trap;

import org.opengoss.alarm.engine.IEventSender;

/**
 * Event dispachter that sends events to analysis and correlation engine.
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 20061217
 * @since 1.0
 */
public interface IEventDispatcher {
	
	void add(IEventSender eventSender);
	
	void remove(IEventSender eventSender);

}
