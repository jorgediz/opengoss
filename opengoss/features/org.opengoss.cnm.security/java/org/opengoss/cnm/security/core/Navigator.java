package org.opengoss.cnm.security.core;

import java.util.List;

import org.opengoss.dao.core.DomainObject;


public class Navigator extends DomainObject{
    private String roleName;
    private List<Module> modules;

    public Navigator(){
    	
    }
    public String getRoleName() {
        return roleName;
    }

    
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Navigator(String roleName, List moduleList) {
        this.roleName = roleName;
        this.modules = moduleList ;
    }


	public List<Module> getModules() {
		return modules;
	}


	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
}
