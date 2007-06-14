package org.opengoss.cnm.log.core;

import java.io.Serializable;

public class LogLevel implements Serializable{
	
	private String name;
	
	private LogLevel(String name){
		this.name = name;
	}
	public String toString(){
		return name;
	}
	
	public final static LogLevel ERROR = new LogLevel("erro");
	
	public final static LogLevel WARN = new LogLevel("warn");
	
	public final static LogLevel INFO = new LogLevel("info");
}
