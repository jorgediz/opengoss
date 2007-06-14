package org.opengoss.cnm.security.core;

import org.opengoss.dao.core.DomainObject;

public class Page extends DomainObject{
    private String name;
    private String description;
    private String url;

    public Page(){
    	
    }
    public Page(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }

    public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}
