package org.opengoss.cnm.security.core.service;

import java.util.List;

import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.UserData;
import org.opengoss.cnm.security.core.UserSearchData;



/**
 * this interface mainly defines several methods for managing all user information.
 * these methods  allow for adding ,deleting ,finding,modifying user opertaion.
 * @author zouxc
 * @date 2006-12-14
 */
public interface IUserDataService {
	
	public UserData create (UserData user) throws CnmSecurityException; //ACLEntry could be passed by other function
	
	public UserData modify(UserData user ) throws CnmSecurityException;
	
	public void delUsers(Long[] id) ;
	
	//searchCondition was assembled by all search content
	public List<UserData> findUser(UserSearchData data) throws CnmSecurityException;
	
	public UserData findUserByName(String name)throws CnmSecurityException;
	
	public UserData load(Long id);
	
	public List<String> loadResources() ;
	
	public List<UserData> loadAllUser();
	
	public String hello();
}
