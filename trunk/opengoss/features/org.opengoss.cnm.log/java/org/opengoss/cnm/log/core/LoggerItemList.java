package org.opengoss.cnm.log.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengoss.dao.core.DomainObject;


/**
 * 维护日志类型描述信息
 * @author zouxc
 * @date 2006-12-15
 */
public class LoggerItemList extends DomainObject implements Serializable {

	private Map<String, LoggerItem> loggerItemList = new HashMap<String, LoggerItem>();
	
	public List<LoggerItem> getAllLoggerItem(){
		
		return new ArrayList( loggerItemList.values() );
	}

	public Map<String, LoggerItem> getLoggerItemList() {
		return loggerItemList;
	}

	public void setLoggerItemList(
			Map<String, LoggerItem> loggerItemList) {
		this.loggerItemList = loggerItemList;
	}
	
	public LoggerItem getLoggerItem(String logInstance){
		return loggerItemList.get(logInstance);
	}
	
	public void addLoggerItem(String logInstance , LoggerItem loggerItem){
		loggerItemList.put(logInstance, loggerItem);
	}
	
	public void removeLoggerItem(String logInstance){
		loggerItemList.remove(logInstance);
	}
	
}
