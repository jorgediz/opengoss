package org.opengoss.cnm.security.core.service;

import java.util.List;

import org.opengoss.cnm.security.core.AclEntry;
import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.Navigator;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.cnm.security.core.Role;
import org.opengoss.dao.core.DaoException;



public interface IAclEntryService {
	
	public void loadInitData() throws DaoException;
	
	public AclEntry create(AclEntry aclEntry) throws CnmSecurityException, DaoException;
	
	public void delete(AclEntry aclEntry) throws  DaoException; 
	
	public void modify(Long id , AclEntry aclEntry) throws CnmSecurityException;
	
	public void modify(AclEntry aclEntry) throws CnmSecurityException, DaoException ;
	
	public List<AclEntry> find() throws CnmSecurityException;
	
//	public AclEntry findACLEntryByNameAndId(long id ,String name) throws CnmSecurityException;
	
	public void deletePermissionByAclEntry(AclEntry aclEntry) throws DaoException;
	
	public AclEntry findAclEntryByUserId(Long userId) throws DaoException;
	
	public List<Resource> findRescsByUserId(Long userId) throws DaoException;
	
	public List<Resource> getResources();
	
	public List<Role> getUserGroups();
	
	public Navigator authenticate(String name,String pwd) ;
}
