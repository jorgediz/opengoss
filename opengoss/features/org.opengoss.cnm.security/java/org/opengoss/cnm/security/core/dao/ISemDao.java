package org.opengoss.cnm.security.core.dao;

import java.util.List;

import org.opengoss.cnm.security.core.AclEntry;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.cnm.security.core.User;
import org.opengoss.cnm.security.core.Role;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.IDomainDao;

public interface ISemDao extends IDomainDao{ //只对user对象来

//	public DomainObject update(DomainObject object);
	
//	public <T extends DomainObject> T get(Class clazz, long id);
	
	public User findUserById(Long id );
	
	public User findUserByNameAndStatus(String name,String status) throws DaoException;
	
	public User findUserByName(String name) throws DaoException;

	public List<User> findUserByHql(String hql);
	
//	public UserGroup findUserGroupByName(String name); 暂时不需要

	public AclEntry findAclEntryByUserName(String userName) throws DaoException;
	
	public AclEntry findAclEntryByUserId(Long id) throws DaoException;

	public List<AclEntry> findAclEntry() throws DaoException ;
	
	public Role findUserGroupByType(String type) throws DaoException;

	public List<Role> getAllRole() throws DaoException;
	
	public List<Resource> getAllResources() throws DaoException;

	public void deleteResource(Resource res) throws DaoException ;

	public Resource findResourceByRescId(Long id) throws DaoException; 
	
	
//	public AclEntry findACLEntryByName(String name);
	
//	public AclEntry findACLEntryByNameAndId(long id ,String name);

//	public AclEntry findAclEntryBySql(String sql);
	
//	public DomainObject save(DomainObject o); //save all object temporary
	
//	public void deleteACLEntryByUserGroupId(long id);
	
//	public void delete(Class clazz, long id);
}
