package org.opengoss.cnm.security.core;

import java.io.Serializable;



public class User extends CommonUser implements Serializable{ 
	public interface Status{
		String ACTIVE ="active";
		String DELETED="deleted";
	}

	private String userName;
	
	private String mail;
	
	private String address;
	
	private String mobile;
	
	private String phone;
	
	private String company;
	
	private String department;

//	private Type type = Type.SYS_USR;
	
	
	private String status;
	
	private String fileRule;// only to operator has content,other userGroup is null 
	
//	private AclEntry aclEntry = new AclEntry(); //user directly connect to userGroup and permissionSet 
//
//	public AclEntry getAclEntry() {
//		return aclEntry;
//	}
//
//	public void setAclEntry(AclEntry aclEntry) {
//		this.aclEntry = aclEntry;
//	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getFileRule() {
		return fileRule;
	}

	public void setFileRule(String fileRule) {
		this.fileRule = fileRule;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
