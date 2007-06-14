package org.opengoss.cnm.security.core;

import org.opengoss.dao.core.DomainObject;

public class CommonUser extends DomainObject{
	
	private String        name;
    private String        password;

    public CommonUser(String name, String password) {

        super ();
        this.name = name;
        this.password = password;
    }

    public CommonUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
