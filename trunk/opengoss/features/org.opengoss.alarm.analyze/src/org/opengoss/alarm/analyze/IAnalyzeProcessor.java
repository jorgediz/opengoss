package org.opengoss.alarm.analyze;

import org.opengoss.alarm.core.Alarm;

public interface IAnalyzeProcessor {
	
	public String getFilter();
	
	public void execute(Alarm alarm);

}
