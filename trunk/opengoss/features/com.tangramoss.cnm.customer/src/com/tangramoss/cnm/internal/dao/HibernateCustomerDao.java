package com.tangramoss.cnm.internal.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.opengoss.cnm.security.core.Resource;
import org.opengoss.dao.core.DaoException;
import org.opengoss.dao.hibernate.DaoSupport;
import org.opengoss.dao.hibernate.IAccessorCallback;

import com.tangramoss.cnm.customer.dao.ICustomerDao;
import com.tangramoss.cnm.customer.domain.Customer;


public class HibernateCustomerDao extends DaoSupport implements ICustomerDao {

	public HibernateCustomerDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

/*	public void deleteById(final Long id) throws DaoException {
		getDbAccessor().execute(new IAccessorCallback() {
			public Object call(Session session) throws HibernateException, SQLException {
				Object obj = session.get(Customer.class, id);
				session.delete(obj);
				return null;
			}
		});
	}

	public Customer getById(Long id) throws DaoException {
		return (Customer)getDbAccessor().get(Customer.class, id);
	}

	public List<Customer> getCustomers(Class<Customer> clazz) throws DaoException {
		List<Customer> list = getDbAccessor().find("from " + clazz.getName());
		ArrayList<Customer> reList = toArrayList(list);
		return reList;
	}

	public List<Customer> query(String query) throws DaoException {
		List<Customer> list = getDbAccessor().find(query);
		ArrayList<Customer> reList = toArrayList(list);
		return reList;
		
	}
	
	protected ArrayList<Customer> toArrayList(List<Customer> tempList){
		ArrayList<Customer> reArrayList = new ArrayList<Customer>();
		Iterator ite = tempList.iterator();
		while(ite.hasNext()){
			Customer customer = (Customer)ite.next();
			reArrayList.add(customer);
		}
		return reArrayList;
	}*/
	
	public Customer findUserByName(String name) throws DaoException {
		return (Customer) this.uniqueResultByNamedQuery("findUserByName", name);
	}

	public List<Customer> findCustomerByHql(String hql) throws DaoException {
		return super.list(hql);
	}

	public Resource findResourceByCustomerId(Long id) throws DaoException {
		
		return null;
	}
	
}
