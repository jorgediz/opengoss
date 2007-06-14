package org.opengoss.cnm.security.core;

import java.io.Serializable;

import org.opengoss.dao.core.DomainObject;

public class AclEntry extends DomainObject implements Serializable{

	
	private static final long serialVersionUID = -6310217394347056315L;

	private Role role;

	private Permission permission;
	
	private User user;

	public AclEntry(){
		
	}
	
	public AclEntry(Role role, Permission permission ,User user) {
		this.role = role;
		this.permission = permission;
		this.user = user; 
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
}
