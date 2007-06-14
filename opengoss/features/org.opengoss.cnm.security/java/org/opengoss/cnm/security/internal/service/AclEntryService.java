package org.opengoss.cnm.security.internal.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opengoss.cnm.security.core.AclEntry;
import org.opengoss.cnm.security.core.CnmSecurityException;
import org.opengoss.cnm.security.core.Navigator;
import org.opengoss.cnm.security.core.Permission;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.cnm.security.core.Role;
import org.opengoss.cnm.security.core.User;
import org.opengoss.cnm.security.core.dao.ISemDao;
import org.opengoss.cnm.security.core.dao.IXmlDao;
import org.opengoss.cnm.security.core.service.IAclEntryService;
import org.opengoss.dao.core.DaoException;

public class AclEntryService implements IAclEntryService {

	private ISemDao semDao;
	
	private Logger log = Logger.getLogger("ACLEntryServiceImpl");

	private List<Resource> resources ;
	
	private List<Role> roles;
	
	public void loadInitData() throws DaoException{
	
		try {
			resources = semDao.getAllResources();
			roles = semDao.getAllRole();
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}
	
	public AclEntry create(AclEntry aclEntry) throws CnmSecurityException, DaoException {
		// log.enterMethod()
		// need authenticate admin?
		log.log(Level.INFO,"enter ACLEntryServiceImpl create method");
		//用 findAclEntryByUserId时不能保证user的id已经有,或者说user已经被保存 
		AclEntry oldACLEntry = semDao.findAclEntryByUserName(aclEntry.getUser().getName());
		if(oldACLEntry != null){
			throw new CnmSecurityException();
		}
//		checkACLEntry(aclEntry);
		/*UserGroup group = semDao.findUserGroupByType(aclEntry.getUserGroup().getType());
		aclEntry.setUserGroup(group);*/
		try {
			aclEntry = (AclEntry) semDao.save(aclEntry);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		//securityLog
		//log.exitMethod()
		log.log(Level.INFO,"enter ACLEntryServiceImpl create method");
		return aclEntry;
	}


	public void modify(AclEntry aclEntry) throws CnmSecurityException, DaoException {
		log.log(Level.INFO,"enter ACLEntryServiceImpl modify method");
//		AclEntry oldAclEntry = semDao.findAclEntryByUserName(aclEntry.getUser().getName());
		if(aclEntry == null){
			throw new CnmSecurityException();
		}
		try {
//			UserGroup group = oldAclEntry.getUserGroup();
//			Permission permission = oldAclEntry.getPermission();
			//暂时不删permission
			//semDao.delete(permission);
			semDao.update(aclEntry);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		log.log(Level.INFO,"exit ACLEntryServiceImpl modify method");
		
	}
	
	public void modify(Long id ,AclEntry aclEntry) throws CnmSecurityException {
		
		//need authenticate admin?
		
//		if(!oldACLEntry.getName().equals(aclEntry.getName())){ //什么情况下会产生这样的情况，在修改的情况下为什么考虑这样情况
//			oldACLEntry = semDAO.findACLEntryByName(id, aclEntry.getName());
//			if (oldACLEntry != null){
//				throw new CNMSecurityException();//ACLEntry 的名字已经存在了
//			}
//		}
//        
		//securityLog
	}
	

	/* 
	 * user不删除,aclEntry也不需要删除,只需要更新
	 * (non-Javadoc)
	 * @see org.opengoss.cnm.security.core.IAclEntryService#delete(java.lang.Long)
	 */
	public void delete(AclEntry aclEntry) throws  DaoException {
//		log.enterMethod();
		//need authenticate admin?
		if (!aclEntry.getRole().getType().equals(Role.Type.OPERATOR_ROLE)){
			deletePermissionByAclEntry(aclEntry);
		}
		semDao.delete(aclEntry);
//		//securityLog
//		//log.exitMethod()
	}

	public void deletePermissionByAclEntry(AclEntry aclEntry) throws DaoException {
		Permission permisson = aclEntry.getPermission();
		semDao.delete(permisson);
		aclEntry.setPermission(null);
		semDao.update(aclEntry);
	}
	
	public List<AclEntry> find() throws CnmSecurityException {
		return null;
	}
	
	public AclEntry findAclEntryByUserId(Long userId) throws DaoException {
		
		return semDao.findAclEntryByUserId(userId);
		
	}
	public Navigator authenticate(String name, String pwd) {
		try {
			User user = (User) semDao.uniqueResultByNamedQuery("queryUserByNameAndPwd", name,pwd);
			if (user == null){
				return null;
			}else{
				AclEntry aclEntry = (AclEntry) semDao.uniqueResultByNamedQuery("findAclEntryByUserId", user.getId());
				if (aclEntry.getRole().getType().equals(Role.Type.SYS_ROLE)){
					return getNavigator(Role.Type.SYS_ROLE);
				}
				if(aclEntry.getRole().getType().equals(Role.Type.MANAGER_ROLE)){
					return getNavigator(Role.Type.MANAGER_ROLE);
				}
				if(aclEntry.getRole().getType().equals(Role.Type.OPERATOR_ROLE)){
					return getNavigator(Role.Type.OPERATOR_ROLE);
				}
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Navigator getNavigator(String roleType) {
		for (Role role : roles) {
			if (role.getType().equals(roleType)){
				return role.getNavigator();
			}
		}
		return null;
	}
	
	public List<Resource> findRescsByUserId(Long userId) throws DaoException {
		
		AclEntry aclEntry = findAclEntryByUserId(userId);
		return aclEntry.getPermission().getPermissionList();
		
	}

	public ISemDao getSemDao() {
		return semDao;
	}

	public void setSemDao(ISemDao semDao) {
		this.semDao = semDao;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public List<Role> getUserGroups() {
		return roles;
	}


	
	
}
