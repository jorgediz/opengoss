package com.tangramoss.cnm.customer.dao;

import java.util.List;

import org.opengoss.cnm.security.core.Resource;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.core.IDomainDao;

import com.tangramoss.cnm.customer.domain.Customer;


public interface ICustomerDao extends IDomainDao {
	
	/*Customer getById(Long id) throws DaoException;
	
	List<Customer> query(String query) throws DaoException;
	
	List<Customer> getCustomers(Class<Customer> clazz) throws DaoException;

	void deleteById(Long id) throws DaoException;
	*/
	
	public Resource findResourceByCustomerId(Long id) throws DaoException;
	
	public List<Customer> findCustomerByHql(String hql) throws  DaoException;
	
	public Customer findUserByName(String name)throws  DaoException;
	
}
