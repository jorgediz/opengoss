package org.opengoss.alarm.internal.excetue;

import org.opengoss.alarm.core.AlarmRule;
import org.opengoss.alarm.core.BizAlarm;

public interface RuleExecutor {
	
	public void execute(BizAlarm alarm,AlarmRule rule);

}
