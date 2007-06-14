package org.opengoss.alarm.core;

/**
 * 枚举AlarmType类型
 * 
 * @author hurr
 * @version 2006-12-12
 */
public enum AlarmType {
	CommunicationAlarm("CommunicationAlarm"), 
	EquipmentAlarm("EquipmentAlarm"), 
	ProcessingError("ProcessingError"), 
	EnvironmentalAlarm("EnvironmentalAlarm"), 
	QualityOfServiceAlarm("QualityOfServiceAlarm"), 
	IntegrityViolation("Integrity Violation"), 
	OperationalViolation("OperationalViolation"), 
	PhysicalViolation("Physical Violation"), 
	SecuOrMechViolation("Security Service or Mechanism Violation"), 
	TimeDomainViolation("Time Domain Violation");

	private String name;

	AlarmType(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
