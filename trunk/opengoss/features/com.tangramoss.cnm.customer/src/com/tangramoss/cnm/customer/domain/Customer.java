package com.tangramoss.cnm.customer.domain;

import java.util.Date;

import org.opengoss.dao.core.DomainObject;

@SuppressWarnings("serial")
public class Customer extends DomainObject {

	private String fullName;// 客户全称

	private String name;

	private String customerClass;// 即数据模型中的class,客户行业,如：金融等

	private long time;// 入网时间（开通时间）

	private String level;// 客户级别 , 如：金牌、银牌；三星、四星、五星

	private String linkMan;// 联系人

	private String phone;// 联系电话

	private String mobile;// 联系手机

	private String corperator;// 企事法人

	private String address;// 客户地址

	private Boolean informed;// 是否发送通知

	private String message;// 用户短信号码；

	private String email;// EMAIL地址

	private String region;// 所属区域（所属局点）

	private String circuitNo;// 电路编号

	private String circuitInfo;// 电路信息

	private String fax;// 传真

	private String description;// 备注信息

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCircuitInfo() {
		return circuitInfo;
	}

	public void setCircuitInfo(String circuitInfo) {
		this.circuitInfo = circuitInfo;
	}

	public String getCircuitNo() {
		return circuitNo;
	}

	public void setCircuitNo(String circuitNo) {
		this.circuitNo = circuitNo;
	}

	public String getCorperator() {
		return corperator;
	}

	public void setCorperator(String corperator) {
		this.corperator = corperator;
	}

	public String getCustomerClass() {
		return customerClass;
	}

	public void setCustomerClass(String customerClass) {
		this.customerClass = customerClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}


	public Boolean getInformed() {
		return informed;
	}

	public void setInformed(Boolean informed) {
		this.informed = informed;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}


	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

/*	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}*/

}
