package com.tangramoss.cnm.customer.domain;

import java.io.Serializable;

public class CustomerData implements Serializable {

	private Long id;

	private String name;

	private String fullName;

	private long time;

	// 用户级别：金牌，银牌，五星，四星，三星
	private int level = 0;

	private String linkMan;

	private String phone;

	private String mobile;

	private String address;

	private Boolean isInformed;

	private String message;

	private String mail;

	// optional info
	private String domain; // 客户行业

	private String operationType; // 行业类型

	private String corperator; // 企业法人

	private String region; // 所属区域

	private String circuitNO; // 电路编号

	private String circuitInfo; // 电路信息

	private String fax; // 传真

	private String desc; // 备注信息

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

	public String getCircuitNO() {
		return circuitNO;
	}

	public void setCircuitNO(String circuitNO) {
		this.circuitNO = circuitNO;
	}

	public String getCorperator() {
		return corperator;
	}

	public void setCorperator(String corperator) {
		this.corperator = corperator;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}




	public Boolean getIsInformed() {
		return isInformed;
	}

	public void setIsInformed(Boolean isInformed) {
		this.isInformed = isInformed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
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

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}