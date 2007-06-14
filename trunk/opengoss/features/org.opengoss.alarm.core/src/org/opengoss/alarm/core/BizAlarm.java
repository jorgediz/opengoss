package org.opengoss.alarm.core;

import org.opengoss.alarm.core.Alarm;

@SuppressWarnings("serial")
public class BizAlarm extends Alarm {

	private Long customerId;

	private String customerName;

	/**
	 * 客户关心的硬件资源信息,如电路名称或设备端口号
	 */
	private String rescInfo;
	/**
	 * SLA信息
	 */
	private String slaInfo;
	/**
	 * 告警业务定位信息,与告警信息中的定位信息粒度不同。主要看客户关注点，如"某地"
	 */
	private String location;

	public String getRescInfo() {
		return rescInfo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setRescInfo(String rescInfo) {
		this.rescInfo = rescInfo;
	}

	public String getSlaInfo() {
		return slaInfo;
	}

	public void setSlaInfo(String slaInfo) {
		this.slaInfo = slaInfo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
