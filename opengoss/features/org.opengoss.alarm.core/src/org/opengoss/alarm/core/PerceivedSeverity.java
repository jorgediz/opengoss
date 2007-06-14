/**
 * 枚举告警级别
 */
package org.opengoss.alarm.core;

/**
 * @author hurr
 * @version 2006-12-12
 */
public enum PerceivedSeverity {
	CRITICAL(5, "CRITICAL"), 
	MAJOR(4, "MAJOR"), 
	MINOR(3, "MINOR"), 
	WARNING(2, "WARNING"), 
	INDETERMINATE(1, "INDETERMINATE"), 
	CLEAR(0, "CLEAR");

	private int level;

	private String name;

	PerceivedSeverity(int level, String name) {
		this.level = level;
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public String toString() {

		return name;
	}
	
}
