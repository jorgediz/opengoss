package org.opengoss.alarm.manager.internal.core;

/**
 * name;level;critical,major,minor,warning;
 * 
 * @author jihua
 * 
 */
public class CustomersWithAlarm {
	private Long ID;

	private String name;

	private String level;

	private Integer critical;

	private Integer major;

	private Integer minor;

	private Integer warning;

	public Integer getCritical() {
		return critical;
	}

	public void setCritical(Integer critical) {
		this.critical = critical;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Integer getMajor() {
		return major;
	}

	public void setMajor(Integer major) {
		this.major = major;
	}

	public Integer getMinor() {
		return minor;
	}

	public void setMinor(Integer minor) {
		this.minor = minor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWarning() {
		return warning;
	}

	public void setWarning(Integer warning) {
		this.warning = warning;
	}

	
}
