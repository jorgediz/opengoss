package org.opengoss.cnm.log.core;

import java.io.Serializable;
import java.sql.Date;

import org.opengoss.dao.core.DomainObject;

public class SysLog extends DomainObject implements Serializable{
	//necessary info
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public interface Level {
		String INFO = "info";
		String WARN = "warn";
		String ERROR = "error";
	}

	private String level;
	
	private Date time;
	
	private String result;
	
	private String description;
	
	public SysLog(){
		
	}
	
	public SysLog(String level ,String desc){
		this.level = level;
		this.description = desc;
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

	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
}
