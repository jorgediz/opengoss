package org.opengoss.alarm.manager.core;

import java.util.List;

import org.opengoss.alarm.core.AlarmRule;

public interface IOperatorRuleService {

	public List<AlarmRule> getAllAckRule(Long ownerId);

	public AlarmRule getAckRule(Long ruleId);

	public AlarmRule addAckRule(String ruleName, Long operator, Integer severity);

	public boolean deleteAckRule(Long[] ruleIds);

	public boolean updateAckRule(Long ruleId, Long operator, Integer severity);

	public List<AlarmRule> getAllForwardRule(Long ownerId);

	public AlarmRule getForwardRule(Long ruleId);

	public AlarmRule addForwardRule(String ruleName, Long operator,
			Integer severity, String type, String parameters);

	public boolean deleteForwardRule(Long[] ruleIds);

	public boolean updateForwardRule(Long ruleId, Long operator,
			Integer severity, String type, String parameters);

}
