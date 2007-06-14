
package org.opengoss.cnm.security.internal.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opengoss.cnm.security.core.AclEntry;
import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.Navigator;
import org.opengoss.cnm.security.core.Permission;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.cnm.security.core.User;
import org.opengoss.cnm.security.core.Role;
import org.opengoss.cnm.security.core.dao.ISemDao;
import org.opengoss.cnm.security.core.dao.IXmlDao;
import org.opengoss.cnm.security.core.service.IUserService;
import org.opengoss.dao.core.DaoException;

public class UserService implements IUserService {

	private ISemDao semDao;

//	private IXmlDao xmlDao;

	private Logger log = Logger.getLogger("UserService");
	private AclEntryService aclEntryService;
	
	public AclEntryService getAclEntryService() {
		return aclEntryService;
	}

	public void setAclEntryService(AclEntryService aclEntryService) {
		this.aclEntryService = aclEntryService;
	}


	public User createDefaultUser() throws CnmSecurityException {
		
		return null;
	}

	
	// is necessary consider synchronized about create , delete ,modify?
	public User create(User user) throws CnmSecurityException, DaoException {
		
		// need authenticate admin?
		// find user througt user.status.active,no use user.status.deleted
		log.log(Level.INFO,"enter UserServiceImpl create method");
		User oldUser = semDao.findUserByNameAndStatus(user.getName(),
				User.Status.ACTIVE);
		if (oldUser != null) {
			// user Name has already exit
			throw new CnmSecurityException();
		}
		user.setStatus(User.Status.ACTIVE);
		
//		AclEntry aclEntry = addDefaultACLEntry(user);
		try {
			semDao.save(user);
		} catch (DaoException e) {
			e.printStackTrace();
		}
//		save(user);
		// log.operationLog() //
		
		log.log(Level.INFO,"exit UserServiceImpl create method");

		return user;
	}

	public  void delete(Long id) throws CnmSecurityException {
		// log.enterMethod()
		// need authenticate admin?
		
		User user = semDao.findUserById(id);
		user.setStatus(User.Status.DELETED);
		try {
			semDao.update(user);
		} catch (DaoException e) {
			e.printStackTrace();
		}
//		deleteRelatedACLEntry(user); //db4o设计
		
		// log.operationLog()
		// log.exitMethod()
	}
	
	public List<User> findUserByHql(String hql) throws CnmSecurityException {
		// need authenticate admin?
		log.info("enter find method");
		
		List<User> users = null ;
		users = semDao.findUserByHql(hql);
		if (users == null){
			throw new CnmSecurityException();
		}
		log.info("exit find method");
		return users;
	}

	public User findUserByName(String name) throws CnmSecurityException, DaoException {
		// log.enterMethod()
		// need authenticate admin?
		log.info("enter findUserByName method");
		User user = semDao.findUserByNameAndStatus(name, User.Status.ACTIVE);
		if (user == null) {
			throw new CnmSecurityException();
		}
		// log.extiMethod()
		log.info("exit findUserByName method");
		return user;
	}

	public  void modify(User user) throws CnmSecurityException {
		// log.enterMethod()
		// need authenticate admin?
		log.info("enter modify method");
		User oldUser = null;
		try {
			oldUser = semDao.get(User.class, user.getId());
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
		// user.setAclEntry(entry);//is right?
		try {
			semDao.update(user);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		log.info("exit modify method");
		// log.operationLog()
		// log.exitMehtod
	}

	public User load(Long id) {
		log.log(Level.INFO,"enter UserServiceImpl load method");
		User user = null;
		try {
			user = (User) semDao.get(User.class, id);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		if (User.Status.ACTIVE.equals(user.getStatus())) {
			log.log(Level.INFO,"exit UserServiceImpl load method");
			return user;
		}
		return null;
	}



	private AclEntry addDefaultACLEntry(User user) { // admin has all
														// operations to all
														// resources
		log.log(Level.INFO,"enter UserServiceImpl addDefaultACLEntry method");
		Role userGroup = new Role();
		userGroup.setType(Role.Type.SYS_ROLE);
		// get all resources from config file
	/*	List<Customer> allCustomer = null;
		try {
			allCustomer = xmlDao.getAllCustomer();
		} catch (DaoException e) {
			e.printStackTrace();
		} // have no
	*/															// hander to
																// allCustomer
																// is null
		Permission permissions = new Permission(); // permissionSet by default
													// is not taken into account
//		暂时不考虑operation的admf
		List<Resource> resources = null;
		try {
			resources = semDao.getAllResources();
		} catch (DaoException e) {
			e.printStackTrace();
		}
		if (resources != null){
			for (Resource resource : resources) { 
				permissions.getPermissionList().add(resource);
			}
		}
		AclEntry aclEntry = new AclEntry(userGroup, permissions,user);
		
		log.log(Level.INFO,"exit UserServiceImpl addDefaultACLEntry method");
		return aclEntry;
	}

	public ISemDao getSemDao() {
		return semDao;
	}

	public void setSemDao(ISemDao semDao) {
		this.semDao = semDao;
	}
/*
	public IXmlDao getXmlDao() {
		return xmlDao;
	}

	public void setXmlDao(IXmlDao xmlDao) {
		this.xmlDao = xmlDao;
	}
*/
	public Navigator authenticate(String name, String pwd) throws CnmSecurityException {
		if (name.equals("admin")){
//			return "admin";
		}
		if(name.equals("manager")){
//			return "manager";
		}
		if (name.equals("operator")){
//			return "operator";
		}
		return null;
//		User user = findUserByName(name);
//		if (user.getPassword().equals(pwd)){
//			UserGroupType type = user.getAclEntry().getUserGroup().getType(); 
//			if (type == UserGroupType.SYS_USERGROUP){
//				return "admin";
//			}
//			if (type == UserGroupType.MANAGER_USERGROUP){
//				return "manager";
//			}
//			if (type == UserGroupType.OPERATOR_USERGROUP){
//				return "operator";
//			}
//		}else{
//			return "login failed";
//		}
//		return null;
		
	}

	public String hello() {
		return "acho hello ";
	}

	public boolean save(User user) throws CnmSecurityException {
		try {
			semDao.save(user);
			return true;
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return false;
	}

	public AclEntry loadAclEntryByUserId(long id) throws DaoException {
		
		return semDao.findAclEntryByUserId(id);
	}

	public void moidfyUserStatus(User user) throws  DaoException {
		
		user.setStatus(User.Status.DELETED);
		semDao.update(user);
	}

	public List<User> LoadAllUsers() throws DaoException {
		
		return (List<User>) semDao.listByNamedQuery("queryActiveUser");
	}


	
}
