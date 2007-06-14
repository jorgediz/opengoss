package org.opengoss.cnm.security.core;

import java.io.Serializable;

import org.opengoss.dao.core.DomainObject;


public class Role extends DomainObject implements Serializable{
	
	private Navigator  navigator;

	
	public interface Type {
		String MANAGER_ROLE =  "manager";
		String SYS_ROLE ="admin";
		String OPERATOR_ROLE = "operator"; 
	}
	private static final long serialVersionUID = -6402170547332896213L;

	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Navigator getNavigator() {
		return navigator;
	}
	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}
		
}
