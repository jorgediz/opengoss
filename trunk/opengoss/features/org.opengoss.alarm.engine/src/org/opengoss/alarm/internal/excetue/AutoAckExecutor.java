package org.opengoss.alarm.internal.excetue;

import org.opengoss.alarm.core.AlarmRule;
import org.opengoss.alarm.core.BizAlarm;

/**
 * @author zhufan
 *告警自动确认规则
 */
public class AutoAckExecutor implements RuleExecutor {

	public void execute(BizAlarm alarm, AlarmRule rule) {
		
		alarm.setAckTime(System.currentTimeMillis());
		alarm.setAckUserId(Integer.valueOf(-1));
	}

}
