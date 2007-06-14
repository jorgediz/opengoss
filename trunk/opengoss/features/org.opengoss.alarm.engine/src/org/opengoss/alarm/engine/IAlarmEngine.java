package org.opengoss.alarm.engine;

import java.util.List;

import net.esper.client.EPServiceProvider;

public interface IAlarmEngine {
	
	String NAME = "AlarmEngine";
	
	EPServiceProvider getEsper();
	
	void addAlarms(List alarms);
	
	void deleteAlarms(List alarms);
	
	List getEvents(String fitler);
	

}
