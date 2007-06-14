package org.opengoss.cnm.log.core;

import java.io.Serializable;

public class LogType implements Serializable{
	private final String name;
	
	private LogType(String name){
		this.name = name;
	}
	public String toString(){
		return name;
	}
	public static final LogType OPERATION = new LogType("OperationLog");
	
	public static final LogType SYSTEM = new LogType("SystemLog");
	
}
