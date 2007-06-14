package org.opengoss.alarm.core;

import org.opengoss.dao.core.DomainObject;

@SuppressWarnings("serial")
public class AlarmRule extends DomainObject {
	
	private Long ownerId;//规则设置者的id
	
	private String name;//规则名称
	
	private String type;//规则类型 ：mailForwardRule、simForwardRule, autoAckRule
	
	private String ruleCondition;//规则匹配条件,面向数据库的SQL语句
	
	private String context;//规则动作�的上下文,如短信前转规则需要的短信号码
	
	private String description;//规则说明

	public String getRuleCondition() {
		return ruleCondition;
	}

	public void setRuleCondition(String condition) {
		this.ruleCondition = condition;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
}
