package org.opengoss.cnm.log.core;

import java.io.Serializable;
import java.util.Date;

import org.opengoss.dao.core.DomainObject;

public class OperationLog extends DomainObject implements Serializable { //参看model文件
	
	//necessary info
	
	private String userName;
	
	private String userIP;
	
	private Date time;
	
    private String action;

    private String  result;
    
    private String description;

    //optional info
    private String userId;

//    private String  moduleName;
    
    public OperationLog(){
    	
    }

    public OperationLog( String action, String result, String desc) {
    	
        this.action = action;
        this.result = result;
        this.description = desc;
        this.time = new Date ();
    }



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getResult() {
		return result;
	}



	public void setResult(String result) {
		this.result = result;
	}




	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



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

}
