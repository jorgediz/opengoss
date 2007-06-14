package org.opengoss.cnm.log.core;

import java.io.Serializable;

public class OperationSearchData implements Serializable{

//	private LogType type ;
	
//	private int type ;
	
	private String userName;
	
	private String userIP;
	
	private String action;
	
	private String result;
	
	private String desc;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

//	public LogType getType() {
//		return type;
//	}
//
//	public void setType(LogType type) {
//		this.type = type;
//	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

/*	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}*/
	
}
