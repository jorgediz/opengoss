package org.opengoss.cnm.security.core;

import java.io.Serializable;

import org.opengoss.dao.core.DomainObject;

public class Resource extends DomainObject implements Serializable {

	private String rescName; //customer name
 
	private String description;//

	private Long rescId;  //customer Id

	/*public long getRescId() {
		return rescId;
	}
	public void setRescId(long rescId) {
		this.rescId = rescId;
	}*/
	// for hibernate
	public Resource (){
		
	}
	public Resource(Long rescId , String rescName) {
		this.rescId = rescId;
		this.rescName = rescName;
	}
	public Resource(Long rescId , String rescName,String description) {
		this(rescId,rescName);
		this.description = description ;
	}
/*
//	
 * private Customer customer;//rescID
 * 
	public Resource(Customer customer) { // 资源的名字和客户的名字一样，备注也一致
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

*/	
	public String getDesc() {
		return description;
	}

	public void setDesc(String desc) {
		this.description = desc;
	}

	public String getRescName() {
		return rescName;
	}

	public void setRescName(String rescName) {
		this.rescName = rescName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public Long getRescId() {
		return rescId;
	}
	public void setRescId(Long rescId) {
		this.rescId = rescId;
	}

}
