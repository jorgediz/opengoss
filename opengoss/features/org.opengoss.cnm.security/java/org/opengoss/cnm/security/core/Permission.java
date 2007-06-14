package org.opengoss.cnm.security.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.opengoss.dao.core.DomainObject;


//多个resource对应相应的操作权限
//例如：customerA：admf		customerB:admf
//现有的需求是大客户经理对选择的大客户信息都有admf操作，所以Operation操作暂时不考虑。
public class Permission extends DomainObject implements Serializable{

//	private Map<Resource , Operation> permissionMap = new HashMap<Resource ,Operation>();

	private List<Resource> permissionList = new ArrayList<Resource>();
	
	public List<Resource> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Resource> permissionList) {
		this.permissionList = permissionList;
	}
	
	public void addResource(Resource r){
		permissionList.add(r);
	}

}
