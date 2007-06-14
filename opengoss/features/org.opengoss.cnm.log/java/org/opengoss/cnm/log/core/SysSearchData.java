package org.opengoss.cnm.log.core;

import java.io.Serializable;
import java.util.Date;

public class SysSearchData implements Serializable{

	
	private String level;
	
	private Date time;
	
	private String result;
	
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	

}