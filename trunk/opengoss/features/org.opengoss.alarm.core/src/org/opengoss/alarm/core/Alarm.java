package org.opengoss.alarm.core;

import org.opengoss.dao.core.DomainObject;

/**
 * 告警信息(运维人员－－原始告警)
 * @author hurr
 * @version 2006-12-12
 */
@SuppressWarnings("serial")
public class Alarm extends DomainObject {

	// 告警名称
	private String name;

	// 告警类别标识,AlarmKey，用于标识本系统中唯�?
	private String alarmKey;

	// 原始告警标识、厂商的告警实例标识,用于标识厂商系统中唯�?
	private String vendorAlarmId;

	// 原始告警名称、厂商的告警名称
	private String vendorAlarmName;

	/**
	 *  告警类型,标准取�?为： CommunicationAlarm, EquipmentAlarm, ProcessingError,
	 * EnvironmentalAlarm, QualityOfServiceAlarm, Integrity Violation,
	 * Operational Violation, Physical Violation, Security Service or Mechanism
	 * Violation, Time Domain Violation
	 */
	private String alarmType;

	// 可能原因
	private String probableCause;

	// 详细原因
	private String specialProblem;

	/*
	 * 告警级别,取�?为Critical、MAJOR、MINOR、WARNING、Cleared或Indeterminate
	 */
	private Integer perceivedSeverity;

	// 告警产生时间
	private Long alarmRaisedTime;

	// 告警清除时间
	private Long alarmClearedTime;

	// 告警确认时间
	private Long ackTime;

	// 告警确认者标�?
	//-1表示系统用户
	private Integer ackUserId;

	// 告警定位信息
	private String location;

	// 告警�?
	private String alarmSource;

	// 告警附加信息
	private String additionalInfo;

	// 第一次产生时�?
	private long firstOccurence;

	// �?��产生时间
	private long lastOccurence;

	// 告警重复发生次数
	private Integer repeatNumbers = 0;

	// 人工备注信息
	private String memo;

	// 告警记录时间
	private Long recordedTime;

	private int phrase = HandlePhase.INITIALIZED;
	
	public Long getAckTime() {
		return ackTime;
	}

	public void setAckTime(Long ackTime) {
		this.ackTime = ackTime;
	}

	public Integer getAckUserId() {
		return ackUserId;
	}

	public void setAckUserId(Integer ackUserId) {
		this.ackUserId = ackUserId;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public Long getAlarmClearedTime() {
		return alarmClearedTime;
	}

	public void setAlarmClearedTime(Long alarmClearedTime) {
		this.alarmClearedTime = alarmClearedTime;
	}

	public String getAlarmKey() {
		return alarmKey;
	}

	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	public Long getAlarmRaisedTime() {
		return alarmRaisedTime;
	}

	public void setAlarmRaisedTime(Long alarmRaisedTime) {
		this.alarmRaisedTime = alarmRaisedTime;
	}

	public String getAlarmSource() {
		return alarmSource;
	}

	public void setAlarmSource(String alarmSource) {
		this.alarmSource = alarmSource;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public long getFirstOccurence() {
		return firstOccurence;
	}

	public void setFirstOccurence(long firstOccurence) {
		this.firstOccurence = firstOccurence;
	}

	public long getLastOccurence() {
		return lastOccurence;
	}

	public void setLastOccurence(long lastOccurence) {
		this.lastOccurence = lastOccurence;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPerceivedSeverity() {
		return perceivedSeverity;
	}

	public void setPerceivedSeverity(Integer perceivedSeverity) {
		this.perceivedSeverity = perceivedSeverity;
	}

	public String getProbableCause() {
		return probableCause;
	}

	public void setProbableCause(String probleCause) {
		this.probableCause = probleCause;
	}

	public Long getRecordedTime() {
		return recordedTime;
	}

	public void setRecordedTime(Long recordedTime) {
		this.recordedTime = recordedTime;
	}

	public Integer getRepeatNumbers() {
		return repeatNumbers;
	}

	public void setRepeatNumbers(Integer repeatNumbers) {
		this.repeatNumbers = repeatNumbers;
	}

	public String getSpecialProblem() {
		return specialProblem;
	}

	public void setSpecialProblem(String specialProblem) {
		this.specialProblem = specialProblem;
	}

	public String getVendorAlarmId() {
		return vendorAlarmId;
	}

	public void setVendorAlarmId(String vendorAlarmId) {
		this.vendorAlarmId = vendorAlarmId;
	}

	public String getVendorAlarmName() {
		return vendorAlarmName;
	}

	public void setVendorAlarmName(String vendorAlarmName) {
		this.vendorAlarmName = vendorAlarmName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Alarm: { ");
		sb.append("id: ").append(getId()).append(", ");
		sb.append("alarmKey: ").append(alarmKey).append(", ");
		sb.append("raisedTime: ").append(alarmRaisedTime).append(", ");
		sb.append("alarmSource: ").append(alarmSource);
		sb.append("}");
		return sb.toString();
	}

	public int getPhrase() {
		return phrase;
	}

	public void setPhrase(int phrase) {
		this.phrase = phrase;
	}
}
