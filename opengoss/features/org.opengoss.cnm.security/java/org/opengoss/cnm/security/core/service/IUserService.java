package org.opengoss.cnm.security.core.service;

import java.util.List;

import org.opengoss.cnm.security.core.AclEntry;
import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.Navigator;
import org.opengoss.cnm.security.core.User;
import org.opengoss.dao.core.DaoException;

public interface IUserService {

	public User create(User user) throws CnmSecurityException, DaoException; 
	
	public User createDefaultUser() throws CnmSecurityException;
	
	public boolean save(User user) throws CnmSecurityException;
	
	public void modify(User user) throws CnmSecurityException;
	
	public void moidfyUserStatus(User user ) throws DaoException;
	
	public User load(Long id);
	
	public List<User> LoadAllUsers() throws DaoException;
	
	public AclEntry loadAclEntryByUserId(long id) throws DaoException;
	
	public void delete(Long id) throws CnmSecurityException;
	
	public List<User> findUserByHql(String hql) throws CnmSecurityException;
	
	public User findUserByName(String name)throws CnmSecurityException, DaoException;
	
	public Navigator authenticate(String name, String pwd) throws CnmSecurityException;
	
	
	public String hello() ;
}
