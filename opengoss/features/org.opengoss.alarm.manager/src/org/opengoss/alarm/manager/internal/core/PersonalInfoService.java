package org.opengoss.alarm.manager.internal.core;

import org.opengoss.alarm.manager.core.IPersonalInfoService;
import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.User;
import org.opengoss.cnm.security.core.service.IUserService;

public class PersonalInfoService implements IPersonalInfoService{
	
	IUserService personalInfoSer;
	
	public void setIUserService(IUserService service){
		personalInfoSer = service;
	}

	public void modifyInfo(User user) throws CnmSecurityException {
		
		personalInfoSer.modify(user);
	}

}
