/**
 * 
 */
package org.opengoss.alarm.manager.internal.core;

import java.util.List;

import org.opengoss.alarm.core.AlarmRule;
import org.opengoss.alarm.core.IAlarmRuleDao;
import org.opengoss.alarm.manager.core.IOperatorRuleService;
import org.opengoss.dao.core.DaoException;

/**
 * @author hurr
 * 
 */
public class OperatorRuleService implements IOperatorRuleService {

	private IAlarmRuleDao alarmRuleDao;

	public void setAlarmRuleDao(IAlarmRuleDao alarmRuleDao) {
		this.alarmRuleDao = alarmRuleDao;
	}

	private String buildAckCondition(Integer severity) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from Alarm alarm ");
		sBuffer.append("where alarm.perceivedSeverity=" + severity.toString());
		return sBuffer.toString();
	}

	/*
	 * @
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#addAckRule(org.opengoss.alarm.core.AlarmRule)
	 */
	public AlarmRule addAckRule(String ruleName, Long operator, Integer severity) {
		AlarmRule rule = new AlarmRule();

		try {
			// TODO 是否要修改...
			rule.setName(ruleName);
			String condition = buildAckCondition(severity);
			rule.setRuleCondition(condition);
			rule.setOwnerId(operator);
			rule.setType("自动确认规则");
			rule.setContext("");
			rule.setDescription("");

			rule = alarmRuleDao.save(rule);
		} catch (DaoException e) {
			e.printStackTrace();
		}

		return rule;
	}

	// TODO 同构造自动确认规则，可能以后要修改
	private String buildForwardCondition(Integer severity) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from Alarm alarm ");
		sBuffer.append("where alarm.perceivedSeverity=" + severity.toString());
		return sBuffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#addForwardRule(org.opengoss.alarm.core.AlarmRule)
	 */
	public AlarmRule addForwardRule(String ruleName, Long operator,
			Integer severity, String type, String parameters) {
		AlarmRule rule = new AlarmRule();

		try {
			// TODO 是否要修改...Email前转规则，短信前转规则
			rule.setName(ruleName);
			rule.setOwnerId(operator);
			String condition = buildForwardCondition(severity);
			rule.setRuleCondition(condition);
			rule.setType(type);
			rule.setContext(parameters);
			rule.setDescription("");

			rule = alarmRuleDao.save(rule);
		} catch (DaoException e) {
			e.printStackTrace();
		}

		return rule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#deleteAckRule(java.lang.Long)
	 */
	public boolean deleteAckRule(Long[] ruleIds) {
		return deleteRule(ruleIds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#deleteForwardRule(java.lang.Long)
	 */
	public boolean deleteForwardRule(Long[] ruleIds) {
		return deleteRule(ruleIds);
	}

	/*
	 * @ delete Rule by Id
	 */
	private boolean deleteRule(Long[] ruleIds) {
		try {
			for (int i = 0; i < ruleIds.length; i++) {
				AlarmRule rule = alarmRuleDao.getAlarmRuleById(ruleIds[i]);
				if (rule != null) {
					alarmRuleDao.delete(rule);
				}
			}
			return true;
		} catch (DaoException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#getAckRule()
	 */
	public AlarmRule getAckRule(Long ruleId) {
		return getRule(ruleId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#getForwardRule()
	 */
	public AlarmRule getForwardRule(Long ruleId) {
		return getRule(ruleId);
	}

	private AlarmRule getRule(Long ruleId) {
		try {
			AlarmRule rule = alarmRuleDao.getAlarmRuleById(ruleId);
			if (rule != null) {
				return rule;
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String buildRuleSql(Long ownerId, String type) {
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("from AlarmRule alarmRule ");
		sBuffer.append("where alarmRule.ownerId=" + ownerId);
		sBuffer.append(" and alarmRule.type='" + type + "'");
		return sBuffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#getAllAckRule(int,
	 *      int)
	 */
	public List<AlarmRule> getAllAckRule(Long ownerId) {
		// TODO 中文部分要修改
		String query = buildRuleSql(ownerId, "自动确认规则");
		try {
			List<AlarmRule> rules = alarmRuleDao.query(query);
			return rules;
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#getAllForwardRule(int,
	 *      int)
	 */
	public List<AlarmRule> getAllForwardRule(Long ownerId) {
		// TODO 中文部分要修改
		String queryM = buildRuleSql(ownerId, "短信前转规则");
		String queryE = buildRuleSql(ownerId, "Email前转规则");
		try {
			List<AlarmRule> rulesM = alarmRuleDao.query(queryM);
			List<AlarmRule> rulesE = alarmRuleDao.query(queryE);
			rulesM.addAll(rulesE);
			System.out.println(rulesM.size());
			return rulesM;
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#updateAckRule(java.lang.Long,
	 *      org.opengoss.alarm.core.AlarmRule)
	 */
	public boolean updateAckRule(Long ruleId, Long operator, Integer severity) {
		try {
			AlarmRule rule = alarmRuleDao.getAlarmRuleById(ruleId);
			rule.setOwnerId(operator);
			String condition = buildAckCondition(severity);
			rule.setRuleCondition(condition);
			alarmRuleDao.update(rule);
			return true;
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opengoss.alarm.manager.core.IOperatorRuleServices#updateForwardRule(java.lang.Long,
	 *      org.opengoss.alarm.core.AlarmRule)
	 */
	public boolean updateForwardRule(Long ruleId, Long operator,
			Integer severity, String type, String parameters) {
		try {
			// TODO 是否要修改...Email前转规则，短信前转规则
			AlarmRule rule = alarmRuleDao.getAlarmRuleById(ruleId);
			rule.setOwnerId(operator);
			String condition = buildForwardCondition(severity);
			rule.setRuleCondition(condition);
			rule.setType(type);
			rule.setContext(parameters);
			alarmRuleDao.update(rule);
			return true;
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
