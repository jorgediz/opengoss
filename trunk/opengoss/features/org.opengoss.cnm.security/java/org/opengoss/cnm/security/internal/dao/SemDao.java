package org.opengoss.cnm.security.internal.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.opengoss.cnm.security.core.AclEntry;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.cnm.security.core.User;
import org.opengoss.cnm.security.core.Role;
import org.opengoss.cnm.security.core.dao.ISemDao;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.hibernate.DaoSupport;

public class SemDao extends DaoSupport implements ISemDao {

	public SemDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public AclEntry findAclEntryByUserId(Long id) throws DaoException {
		
		return (AclEntry) this.uniqueResultByNamedQuery("findAclEntryByUserId", id);
	}

	public AclEntry findAclEntryByUserName(String userName) throws DaoException {
		
		return (AclEntry) this.uniqueResultByNamedQuery("findAclEntryByUserName", userName);
	}

	public User findUserById(Long id) {
		try {
			return this.get(User.class, id);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User findUserByName(String name) throws DaoException {
		return (User) this.uniqueResultByNamedQuery("findUserByName", name);
	}

	public User findUserByNameAndStatus(String name, String status) throws DaoException {
		
		return (User) this.uniqueResultByNamedQuery("findUserByNameAndStatus", name,status);
	}

	public List<User> findUserByHql(String hql) {
		
		try {
			return super.list(hql);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Role findUserGroupByType(String type) throws DaoException {
		
		return null;
	}

	public List<Resource> getAllResources() throws DaoException {
		
		return super.listAll(Resource.class);
	}

	public List<Role> getAllRole() throws DaoException {
		
		return super.listAll(Role.class);
	}

/*	public List<Customer> getAllCustomers() throws DaoException {
		
		return super.listAll(Customer.class);
	}
*/
	public List<AclEntry> findAclEntry() throws DaoException {
		return super.listAll(AclEntry.class);
	}

	public void deleteResource(Resource res) throws DaoException {
		
		this.delete(res);
	}

	public Resource findResourceByRescId(Long id) throws DaoException {
		
		return (Resource) super.uniqueResultByNamedQuery("findResourceByRescId", id);
	}

	
}
