package org.opengoss.alarm.manager.core;

import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.User;

public interface IPersonalInfoService {
	
	public void modifyInfo(User user) throws CnmSecurityException;

}
