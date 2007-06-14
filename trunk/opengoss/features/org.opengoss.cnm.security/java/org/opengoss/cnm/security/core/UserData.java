package org.opengoss.cnm.security.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 相当于前台展现层的结构体信息，后台用于转换成复杂的信息
 * @author zouxc
 *
 */
public class UserData  implements Serializable{
	
	private Long id ;
	
	private String name;
	
	private String password;
	
	private String userName;
	
	private String mail;
	
	private String address;
	
	private String mobile;
	
	private String phone;
	
	private String company;
	
	private String department;
	
	private String roleInfo; //manager代表选择了manager的userGroup，operator就代表选择了operator的userGroup

	private List<String> allResc = new ArrayList<String>();
	
	private List<String> selResc = new ArrayList<String>();

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


	public List<String> getAllResc() {
		return allResc;
	}

	public void setAllResc(List<String> allResc) {
		this.allResc = allResc;
	}

	public String getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(String roleInfo) {
		this.roleInfo = roleInfo;
	}

	public List<String> getSelResc() {
		return selResc;
	}

	public void setSelResc(List<String> selResc) {
		this.selResc = selResc;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


}
