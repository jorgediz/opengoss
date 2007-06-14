package org.opengoss.cnm.security.core;

import java.util.List;

import org.opengoss.dao.core.DomainObject;


public class Module extends DomainObject{
    private String name;
    private String description;
    private String imgUrl;
    private List<Page> pages;
//    private Page[] pages;

    public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public Module(){
		
	}
	public Module(String moduleName, String imgUrl, List pages) {
        this.name = moduleName;
        this.imgUrl = imgUrl;
        this.pages = pages;
    }

    public Module(String moduleName, String imgUrl, String description, List pages) {
        this.name = moduleName;
        this.imgUrl = imgUrl;
        this.description = description;
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    
    public String getDescription() {
        return description;
    }

	public void setDescription(String description) {
		this.description = description;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}