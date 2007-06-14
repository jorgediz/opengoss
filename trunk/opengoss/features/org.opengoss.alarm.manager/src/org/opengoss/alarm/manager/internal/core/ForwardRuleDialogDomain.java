package org.opengoss.alarm.manager.internal.core;

public class ForwardRuleDialogDomain {
	
	/**
	 * objectArray["id"] = ruleId;//id
		objectArray["ruleName"] = ruleNameStr;//规则名称
		objectArray["customerName"] = customerNameStr;//客户名称
		objectArray["alarmSerceivedSeverity"] = alarmSeverityStr;//告警级别
		objectArray["type"] = ruleTypeStr;//Email前转规则
		objectArray["context"] = ruleContextStr;//短信号码
	 */
	
	Long id;
	
	String ruleName;
	
	String customerName;
	
	String alarmSerceivedSeverity;
	
	String ruleType;
	
	String ruleContext;

	public String getAlarmSerceivedSeverity() {
		return alarmSerceivedSeverity;
	}

	public void setAlarmSerceivedSeverity(String alarmSerceivedSeverity) {
		this.alarmSerceivedSeverity = alarmSerceivedSeverity;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuleContext() {
		return ruleContext;
	}

	public void setRuleContext(String ruleContext) {
		this.ruleContext = ruleContext;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

}
