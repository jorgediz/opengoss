package org.opengoss.orm.core;

import java.util.List;

public class Page {
	
	private Integer count;
	
	private List list;

	public Page(){
		
	}
	public Page(Integer count,List list){
		this.count = count ;
		this.list  = list ;
	}
	
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
